/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.opasha.eCompliance.ecompliance.BackupActivity;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.Model.FtpCredential;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class UploadTask
		extends
		AsyncTask<UploadTask.UploadTaskPayLoad, Object, UploadTask.UploadTaskPayLoad> {

	public static class UploadTaskPayLoad {
		Context context;

		Object[] objects;

		public UploadTaskPayLoad(Context context, Object[] objects) {
			this.context = context;
			this.objects = objects;
		}
	}

	@Override
	protected UploadTaskPayLoad doInBackground(UploadTaskPayLoad... params) {
		UploadTask.UploadTaskPayLoad payload = params[0];
		upload(payload);
		return payload;
	}

	@Override
	public void onPostExecute(UploadTask.UploadTaskPayLoad payload) {
		try {
			BackupActivity act = (BackupActivity) payload.objects[0];
			act.DeleteBackup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void upload(UploadTaskPayLoad payload) {

		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String machineId = getMachineId(payload.context);
		FTPClient ftpClient = new FTPClient();

		try {
			FtpCredential credentials = DbUtils.GetFtpDetails(payload.context);		
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
				String zipPath = "//eComplianceClient//" + machineId
						+ "//Backup//database.zip";
				File tempZip = new File(sd, zipPath);
				buffIn = new BufferedInputStream(new FileInputStream(
						tempZip.getPath()));
				String remFileName = machineId + "_database.zip";
				ftpClient.deleteFile(remFileName);
				ftpClient.setConnectTimeout(30000);
				ftpClient.storeFile(remFileName, buffIn);
				buffIn.close();
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

	private String getMachineId(Context context) {
		ArrayList<Center> centerList;
		String machineID = "";
		centerList = CentersOperations.getCenter(context);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("C")) {

					return c.machineID;

				}
			}
		}

		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("P")) {

					return c.machineID;
				}
			}
		}
		return machineID;
	}

}
