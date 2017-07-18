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
import org.opasha.eCompliance.ecompliance.DownloadVideosActivity;
import org.opasha.eCompliance.ecompliance.Model.FtpCredential;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class VideoDownloadTask
		extends
		AsyncTask<VideoDownloadTask.VideoDownloadTaskPayLoad, Object, VideoDownloadTask.VideoDownloadTaskPayLoad> {

	public static class VideoDownloadTaskPayLoad {
		Context context;

		Object[] objects;
		String machineId;

		public VideoDownloadTaskPayLoad(String machineId, Context context,
				Object[] objects) {
			this.context = context;
			this.objects = objects;
			this.machineId = machineId;
		}
	}
	
	private VideoDownloadTaskPayLoad payload;

	@Override
	protected VideoDownloadTaskPayLoad doInBackground(
			VideoDownloadTaskPayLoad... params) {
		VideoDownloadTask.VideoDownloadTaskPayLoad payload = params[0];
		this.payload = payload;
		download(payload);
		return payload;
	}

	@Override
	public void onPostExecute(VideoDownloadTask.VideoDownloadTaskPayLoad payload) {
		DownloadVideosActivity act = (DownloadVideosActivity) payload.objects[1];
		act.PostDownload();
	}
	
	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);
		String progress = values[0].toString();
		DownloadVideosActivity act = (DownloadVideosActivity) this.payload.objects[1];
		act.showProgress(progress);
	}

	private void download(VideoDownloadTaskPayLoad payload) {

		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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
			ftpClient.changeWorkingDirectory("//eCounselingIndia//Videos");

			if (ftpClient.getReplyString().contains("250")) {
				ftpClient
						.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

				File directory = new File(sd, "//eCounseling//Videos");
				if (!directory.exists()) {
					// deleteDirectory(directory);
					directory.mkdirs();
				}

				try {
					String remFileName = ".zip";
					FTPFile[] files = ftpClient.listFiles(); // ftp list
					
					int j=1;
					for (int i = 0; i < files.length; i++) { // ftp connect
						// forder
						// in files
						
						if (!files[i].isFile()) {
	                        continue;
	                    }


						if (files[i].getName().toString().contains(remFileName)) {
							long remFileSize = files[i].getSize();
							if (remFileSize > 0) {
								String zipPath = "//eCounseling//Videos//"
										+ files[i].getName();
								File tempZip = new File(sd, zipPath);

								if (tempZip.exists()) {
									if (tempZip.length() == remFileSize)
										continue;
								}
								FileOutputStream fos = new FileOutputStream(
										tempZip);
								try {
									ftpClient.retrieveFile(files[i].getName(),
											fos);
									
									//fos.flush();
									fos.close();
									
									this.publishProgress(new Object[]{j + " file(s) downloaded."});
									j++;
									
								} catch(Exception e) {
									//fos.flush();
									fos.close();
								}
							}
						}
					}
				} finally {

				}
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
		} catch (IOException e) {
			Log.e("Error", e.toString());
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
	}
}
