/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.opasha.eCompliance.ecompliance.RestoreActivity;
import org.opasha.eCompliance.ecompliance.Model.FtpCredential;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadTask
		extends
		AsyncTask<DownloadTask.DownloadTaskPayLoad, Object, DownloadTask.DownloadTaskPayLoad> {

	public static class DownloadTaskPayLoad {
		Context context;

		Object[] objects;
		String machineId;

		public DownloadTaskPayLoad(String machineId, Context context,
				Object[] objects) {
			this.context = context;
			this.objects = objects;
			this.machineId = machineId;
		}
	}

	@Override
	protected DownloadTaskPayLoad doInBackground(DownloadTaskPayLoad... params) {
		DownloadTask.DownloadTaskPayLoad payload = params[0];

		download(payload);
		return payload;
	}

	@Override
	public void onPostExecute(DownloadTask.DownloadTaskPayLoad payload) {
		RestoreActivity act = (RestoreActivity) payload.objects[1];
		act.Restore();
	}

	private void download(DownloadTaskPayLoad payload) {
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		FTPClient ftpClient = new FTPClient();

		try {

			FtpCredential credentials = DbUtils.GetFtpDetails(payload.context);
			boolean isFilePresent = false;
			ftpClient.connect(InetAddress.getByName(credentials.FtpServer), 21);
			ftpClient.login(credentials.FtpUserId, credentials.FtpPassword);
			if(credentials.IsPassiveMode)
			{
				ftpClient.enterLocalPassiveMode();
			}
			ftpClient
					.changeWorkingDirectory("//eComplianceIndia//Restores");

			if (ftpClient.getReplyString().contains("250")) {
				ftpClient
						.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				String zipPath = "//eComplianceClient//" + payload.machineId
						+ "//Backup//database.zip";
				File directory = new File(sd, "//eComplianceClient//"
						+ payload.machineId + "//Backup");
				File tempZip = new File(sd, zipPath);
				if (directory.exists()) {
					deleteDirectory(directory);
				}
				directory.mkdirs();
				FileOutputStream fos = new FileOutputStream(tempZip);
				String remFileName = payload.machineId + "_database.zip";
				FTPFile[] files = ftpClient.listFiles(); // ftp list
				for (int i = 0; i < files.length; i++) { // ftp connect forder
															// in files

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
			}
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
	}

	private static boolean deleteDirectory(File path) {
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
