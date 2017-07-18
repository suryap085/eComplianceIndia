/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.database.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.Compress;
import org.opasha.eCompliance.ecompliance.util.UploadTask;

import android.content.Context;
import android.os.Environment;

public class SendBackup {
	String machineId;

	File sd;

	public SendBackup(Context context) {
		machineId = getMachineId(context);
		onBackupClick(context);
	}

	public void onBackupClick(Context context) {

		sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File backupRootDir = new File(sd, "//eComplianceClient//" + machineId
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
		String currentDBPath = "//data//org.opasha.eCompliance.ecompliance//databases//";
		File currentDB = new File(data, currentDBPath);
		if (currentDB.exists()) {
			if (currentDB.isDirectory()) {
				String[] children = currentDB.list();
				for (int i = 0; i < children.length; i++) {
					SaveBackup(children[i]);
				}
				ZipBackup(context);
			}
		}

	}

	private void ZipBackup(Context context) {
		try {
			if (sd.canWrite()) {
				String zipPath = "//eComplianceClient//" + machineId
						+ "//Backup//database.zip";

				String currentDBPath = "//eComplianceClient//" + machineId
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
						UploadFile(context);
					}
				}
			}

		} catch (Exception e) {

		}
	}

	private void SaveBackup(String database) {
		try {
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//org.opasha.eCompliance.ecompliance//databases//"
						+ database;
				String backupDBPath = "//eComplianceClient//" + machineId
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

		}
	}

	public void DeleteBackup() {
		deleteDirectory(new File(sd, "//eComplianceClient//" + machineId
				+ "//Backup//"));
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

	private void UploadFile(Context context) {
		new UploadTask().execute(new UploadTask.UploadTaskPayLoad(context
				.getApplicationContext(), new Object[] { context }));
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
