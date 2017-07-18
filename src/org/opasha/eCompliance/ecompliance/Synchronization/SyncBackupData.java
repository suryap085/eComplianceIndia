/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Synchronization;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.opasha.eCompliance.ecompliance.HomeActivity;
import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisR_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansROperations;
import org.opasha.eCompliance.ecompliance.Model.FtpCredential;
import org.opasha.eCompliance.ecompliance.Model.IrisRModel;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.Model.ScanRModel;
import org.opasha.eCompliance.ecompliance.util.Compress;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.DeCompress;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class SyncBackupData extends AsyncTask<Context, Void, String> {

	static MachineAuth machineAuth;
	private static String SYNC_AUTHENTICATION_WITH_BACKUP = "SyncAuthenticate/AuthenticateForBackUp?";
	public static File sd;
	private static String SYNC_WITH_BACKUPS = "SyncFromBackup/Sync?";
	private static int machineId;
	public Context Context = null;
	private boolean scansDeleted = false;

	public SyncBackupData(Context context) {
		this.Context = context;
	}

	@Override
	protected String doInBackground(Context... context) {
		if (GenUtils.IsInternetConnected(context[0])) {
			if (GenUtils.CheckServerRunning(context[0])) {
				try {
					machineId = Integer.parseInt(ConfigurationOperations
							.getKeyValue(ConfigurationKeys.key_Machine_Id,
									context[0]));
					DefaultHttpClient client = new DefaultHttpClient();

					HttpGet request = new HttpGet(GenerateQuery(
							SYNC_AUTHENTICATION_WITH_BACKUP, context[0])
							+ "machineId="
							+ machineId
							+ "&androidId="
							+ Secure.getString(context[0].getContentResolver(),
									Secure.ANDROID_ID));
					request.setHeader("Accept", "application/json");
					request.setHeader("Content-type", "application/json");
					// request.setHeader("Accept-Encoding", "gzip");
					// get the response
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					int contentLength = (int) entity.getContentLength();
					if (contentLength != 0) {
						char[] buffer = new char[contentLength];
						InputStream stream = entity.getContent();
						InputStreamReader reader = new InputStreamReader(stream);
						int hasRead = 0;
						int currRead = 0;
						while (hasRead < contentLength && currRead >= 0) {
							currRead = reader.read(buffer, hasRead,
									contentLength - hasRead);
							if (currRead > 0)
								hasRead += currRead;
						}
						machineAuth = new MachineAuth();
						JSONObject auth = new JSONObject(new String(buffer));
						machineAuth.machineId = auth.getInt("MachineId");
						machineAuth.startDate = auth.getLong("StartDate");
						machineAuth.endDate = auth.getLong("EndDate");
						machineAuth.isActive = auth.getBoolean("IsActive");
						machineAuth.errorMessage = auth
								.getString("ErrorMessage");
						machineAuth.machine_locked = auth
								.getBoolean("Machine_Locked");
						machineAuth.activationPending = auth
								.getBoolean("ActivationPending");
					}

					if (machineAuth.errorMessage.equals("1")) {
						return context[0].getResources().getString(
								R.string.notificationMachineIdNotExist);
					} else if (machineAuth.errorMessage.equals("2")) {
						if(ConfigurationOperations.isExists("key_Is_Machine_Active", Context))
						{
							ConfigurationOperations.updateKey("key_Is_Machine_Active", Context);
						}else
						{
							ConfigurationOperations.addConfiguration("key_Is_Machine_Active", "false", Context);
						}
						
						return context[0].getResources().getString(
								R.string.notificationMachineInactive);
					} 
					else {

						String tabId = DbUtils.getTabId(Context);

						scansDeleted = true;
						// Empty Scan Table - Both FP and IRIS
						ScansOperations.emptyTable(this.Context);
						ScansIrisOperations.emptyTable(this.Context);
						// Create backupFile
						SendBackupforSync(Context);

						// Upload File
						long length = UploadFile(tabId);

						if (length == 0) {
							return "Upload Failed. Please try again...";
						}

						// Call Api for Synchornization with uploaded file
						String result = SyncWithBackup(String.valueOf(length),
								Context);

						String[] res = result.split(",");
						if (res[0].charAt(1) == '2') {
							return "Synchonization Failed - " + res[1]
									+ ". Please try again...";
						}

						Thread.sleep(7000);

						// Download the file
						if (!DownloadFile(tabId)) {
							return "Synchonization Failed - File download incomplete. Please try again...";
						}

						if (!Restore(tabId)) {
							return "Synchonization Failed - File restore incomplete. Please try again...";
						}

						return "";

					}

				} catch (Exception e) {
					e.printStackTrace();
					return "Synchornization Error! Please try again...";
				}
			} else {
				return "Server not working. Please try again...";
			}
		} else {
			return "Internet not working. Please try again...";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// super.onPostExecute(result);

		if (scansDeleted) { // Generate Scans
			try {
				ArrayList<ScanRModel> scansR = ScansROperations
						.getScansR(Context);
				ScansOperations.emptyTable(Context);
				for (int i = 0; i < scansR.size(); i++) {
					try {
						ScansOperations.addScanaAfterRestore(
								scansR.get(i).treatmentId, "",
								scansR.get(i).scan,
								scansR.get(i).CreationTimeStap,
								scansR.get(i).CreatedBy, Context);

					} catch (Exception e) {
						Log.e("SyncBackup", e.getMessage());
					}
				}
				
				
				ArrayList<IrisRModel> irisR = ScansIrisR_Operations.getScansIrisR(Context);
				ScansIrisOperations.emptyTable(Context);
				for (int i = 0; i < irisR.size(); i++) {
					try {
						ScansIrisOperations.addScanAfterRestore(
								irisR.get(i).treatmentId, irisR.get(i).eye,
								irisR.get(i).scan,
								irisR.get(i).CreationTimeStap,
								irisR.get(i).CreatedBy, Context);
					} catch (Exception e) {
						Log.e("SyncBackup", e.getMessage());
					}
				}
				
			} catch (Exception e) {
				Log.e("error", e.toString());
			}
		}
		
		HomeActivity act = ((HomeActivity) this.Context);
		((eComplianceApp) act.getApplication()).GetConfigurtions();
		if (result.equals("")) {
			
			GenUtils.setAppMissedDose(Context);
			GenUtils.setAppVisitedToday(Context);
			GenUtils.setAppPendingToday(Context);
			
			((HomeActivity) this.Context).processFinish(Context.getResources()
					.getString(R.string.syncComplete));
		} else {
			((HomeActivity) this.Context).processFinish(result);
		}
	}

	// ------------------------------------------------
	// FRIEND FUNCTIONS
	// ------------------------------------------------

	private void SendBackupforSync(Context context) {

		String tabId = DbUtils.getTabId(context);
		if (!tabId.isEmpty()) {
			sd = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File backupRootDir = new File(sd, "//eComplianceClient//" + tabId
					+ "//Backup");
			if (backupRootDir.exists()) {
				if (backupRootDir.isDirectory()) {
					String[] children = backupRootDir.list();
					for (int i = 0; i < children.length; i++) {
						new File(backupRootDir, children[i]).delete();
					}
				}
			} else {
				backupRootDir.mkdirs();
			}

			File data = Environment.getDataDirectory();
			String currentDBPath = "//data//" + context.getPackageName()
					+ "//databases//";
			File currentDB = new File(data, currentDBPath);
			if (currentDB.exists()) {
				if (currentDB.isDirectory()) {
					String[] children = currentDB.list();
					for (int i = 0; i < children.length; i++) {
						SaveBackupforSync(children[i], tabId, context);
					}
					ZipBackupforSync(tabId, context);
				}
			}
		}

	}

	@SuppressWarnings("resource")
	private void SaveBackupforSync(String database, String tabId,
			Context context) {
		try {
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//" + context.getPackageName()
						+ "//databases//" + database;
				String backupDBPath = "//eComplianceClient//" + tabId
						+ "//Backup//" + database;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					if (!backupDB.exists()) {
						backupDB.createNewFile();
					}

					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();

					dst.transferFrom(src, 0, src.size());

					src.close();
					dst.close();

				}

			}
		} catch (Exception e) {

			Logger.e(context, "SaveBackup", e.getMessage());
		}
	}

	private void ZipBackupforSync(String tabId, Context context) {
		try {
			if (sd.canWrite()) {
				String zipPath = "//eComplianceClient//" + tabId
						+ "//Backup//database.zip";

				String currentDBPath = "//eComplianceClient//" + tabId
						+ "//Backup//";
				File currentDB = new File(sd, currentDBPath);
				if (currentDB.exists()) {
					if (currentDB.isDirectory()) {
						String[] children = new String[currentDB.list().length];
						int i = 0;
						for (File file : currentDB.listFiles()) {
							String a = file.getName().toString();
							if (!a.equals("database.zip")) {
								children[i] = file.getPath();
								i++;
							}

						}
						File temp = new File(sd, zipPath);
						temp.createNewFile();
						new Compress(children, temp.getPath()).zip();
					}
				}
			}

		} catch (Exception e) {
			Logger.e(context, "ZipBackup", e.getMessage());
		}
	}

	private long UploadFile(String tabId) {
		long length = 0;
		try {
			File sd = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

			FTPClient ftpClient = new FTPClient();
			
			FtpCredential credentials = DbUtils.GetFtpDetails(this.Context);
						
			
			ftpClient.connect(InetAddress
					.getByName(credentials.FtpServer), 21);
			ftpClient.login(credentials.FtpUserId, credentials.FtpPassword);
			if(credentials.IsPassiveMode)
			{
				ftpClient.enterLocalPassiveMode();
			}
			ftpClient.changeWorkingDirectory("//eComplianceIndia//Backups");

			if (ftpClient.getReplyString().contains("250")) {
				ftpClient
						.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				BufferedInputStream buffIn = null;
				String zipPath = "//eComplianceClient//" + tabId
						+ "//Backup//database.zip";
				File tempZipforSync = new File(sd, zipPath);
				buffIn = new BufferedInputStream(new FileInputStream(
						tempZipforSync.getPath()));

				String remFileName = tabId + "_database.zip";
				ftpClient.deleteFile(remFileName);
				ftpClient.storeFile(remFileName, buffIn);
				buffIn.close();
				ftpClient.logout();
				ftpClient.disconnect();
				length = tempZipforSync.length();

				// syncWithBackup(machineId[0],
				// String.valueOf(tempZipforSync.length()), mContext);

			}
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
		} catch (IOException e) {
			Log.e("Error", e.toString());
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return length;
	}

	private String SyncWithBackup(String fileSize, Context context) {

		String result = "";

		try {
			Thread.sleep(5000);
			int id = Integer.parseInt(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_Machine_Id, context));
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQuery(SYNC_WITH_BACKUPS, context)
					+ "machineId=" + id + "&backupSize=" + fileSize;
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			// get the response
			HttpResponse response = client.execute(request);
			// receive response as inputStream
			InputStream inputStream = response.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			}
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		return result;
	}

	private boolean DownloadFile(String tabId) {
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		FTPClient ftpClient = new FTPClient();

		try {
			boolean isFilePresent = false;
			
			FtpCredential credentials = DbUtils.GetFtpDetails(this.Context);
			ftpClient.connect(InetAddress
					.getByName(credentials.FtpServer), 21);
			ftpClient.login(credentials.FtpUserId, credentials.FtpPassword);
			if(credentials.IsPassiveMode)
			{
				ftpClient.enterLocalPassiveMode();
			}
			ftpClient.changeWorkingDirectory("//eComplianceIndia//Restores");

			if (ftpClient.getReplyString().contains("250")) {
				ftpClient
						.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				String zipPath = "//eComplianceClient//" + tabId
						+ "//Backup//database.zip";
				File directory = new File(sd, "//eComplianceClient//" + tabId
						+ "//Backup");
				File tempZip = new File(sd, zipPath);
				if (directory.exists()) {
					deleteDirectory(directory);
				}
				directory.mkdirs();
				FileOutputStream fos = new FileOutputStream(tempZip);
				String remFileName = tabId + "_database.zip";
				FTPFile[] files = ftpClient.listFiles();
				for (int i = 0; i < files.length; i++) {

					if (remFileName.equals(files[i].getName().toString())) {
						if (files[i].getSize() > 0) {
							ftpClient.retrieveFile(remFileName, fos);
							ftpClient.deleteFile(remFileName);
							isFilePresent = true;
							break;
						}

					}
				}
				if (isFilePresent == false) {
					deleteDirectory(directory);
				}

				fos.flush();
				fos.close();

				ftpClient.logout();
				ftpClient.disconnect();

				return true;

			}
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return false;
	}

	private boolean Restore(String tabId) {
		Context context = this.Context;
		sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		UnzipBackup(tabId);
		try {

			// Set the folder on the SDcard
			File directory = new File(sd, "//eComplianceClient//" + tabId
					+ "//Backup");
			File data = Environment.getDataDirectory();
			String currentDBPath = "//data//org.opasha.eCompliance.ecompliance//databases//";
			File currentDB = new File(data, currentDBPath);
			if (directory.exists()) {
				if (directory.isDirectory()) {
					String[] childrens = directory.list();
					if (childrens.length < 5) {
						deleteDirectory(directory);
					}
				}
			}
			if (directory.exists()) {

				// Set the input file stream up:
				if (currentDB.exists()) {
					if (currentDB.isDirectory()) {
						String[] children = currentDB.list();
						for (int i = 0; i < children.length; i++) {
							new File(currentDB, children[i]).delete();
						}
					}
				} else {
					currentDB.mkdirs();
				}

				if (directory.exists()) {
					if (directory.isDirectory()) {
						String[] children = directory.list();
						for (int i = 0; i < children.length; i++) {
							if (!children[i].equals("database.zip")) {
								SaveRestore(children[i], context, tabId);
							}
						}

					}
					deleteDirectory(directory);
				}

			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.RestoreDirectryNotFound),
						Toast.LENGTH_SHORT).show();

			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Sync Backup - Restore", e.toString());
		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
		}
		return false;

	}

	private void UnzipBackup(String tabId) {
		try {
			if (sd.canWrite()) {
				String zipPath = "//eComplianceClient//" + tabId
						+ "//Backup//database.zip";

				String currentDBPath = "//eComplianceClient//" + tabId
						+ "//Backup//";
				File currentDB = new File(sd, currentDBPath);
				File zipPathFile = new File(sd, zipPath);
				DeCompress d = new DeCompress(currentDB.getPath(),
						zipPathFile.getPath());
				d.unzip();
			}

		} catch (Exception e) {
		}
	}

	private void SaveRestore(String database, Context context, String tabId) {
		try {
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//eComplianceClient//" + tabId
						+ "//Backup//" + database;
				String backupDBPath = "//data//org.opasha.eCompliance.ecompliance//databases//"
						+ database;
				File currentDB = new File(sd, currentDBPath);
				File backupDB = new File(data, backupDBPath);

				if (currentDB.exists()) {
					if (!backupDB.exists()) {
						backupDB.createNewFile();
					}
					FileChannel src = null;
					FileChannel dst = null;
					try {
						src = new FileInputStream(currentDB).getChannel();
						dst = new FileOutputStream(backupDB).getChannel();
						dst.transferFrom(src, 0, src.size());

					} finally {
						src.close();
						dst.close();
					}

				}

			}
		} catch (Exception e) {

		}
	}

	// -------------------------------
	// Utilities
	// -------------------------------

	private String GenerateQuery(String path, Context context) {
		return ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_sync_api, context) + path;

	}

	// convert inputstream to String
	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	private boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}
