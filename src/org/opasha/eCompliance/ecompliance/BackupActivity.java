/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
/**
 * 
 */
package org.opasha.eCompliance.ecompliance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.Compress;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Logger;
import org.opasha.eCompliance.ecompliance.util.UploadTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author abhishek
 * 
 */
public class BackupActivity extends Activity {

	TextView lblStatus;
	String machineId;
	Button btnBackup;
	File sd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		btnBackup = (Button) findViewById(R.id.btnBackup);
		btnBackup.setText(getResources().getString(R.string.Backup));
		machineId = getMachineId();

		if (machineId == null) {
			goHome();
		} else {
			if (machineId.isEmpty()) {
				goHome();
			}
			lblStatus = (TextView) findViewById(R.id.lblStatus);
		}
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
	}

	public void onBackupClick(View v) {

		if (GenUtils.IsInternetConnected(this)) {
			if (btnBackup.getText().toString()
					.equals(getResources().getString(R.string.Backup))) {
				sd = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				File backupRootDir = new File(sd, "//eComplianceClient//"
						+ machineId + "//Backup");
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

				btnBackup.setActivated(false);
				File data = Environment.getDataDirectory();
				String currentDBPath = "//data//org.opasha.eCompliance.ecompliance//databases//";
				File currentDB = new File(data, currentDBPath);
				if (currentDB.exists()) {
					if (currentDB.isDirectory()) {
						String[] children = currentDB.list();
						for (int i = 0; i < children.length; i++) {
							SaveBackup(children[i]);
						}
						ZipBackup();
					}
				}
				btnBackup.setActivated(true);
				btnBackup.setText(getResources().getString(R.string.done));
			} else {
				goHome();
			}
		} else {
			Toast.makeText(this,
					"Internet not working..Please check internet connection!",
					Toast.LENGTH_LONG).show();
		}
	}

	private void ZipBackup() {
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
						UploadFile();
					}
				}
			}

		} catch (Exception e) {
			Logger.e(this, "ZipBackup", e.getMessage());
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

					String currText = lblStatus.getText().toString();
					lblStatus.setText(getResources().getString(R.string.save)
							+ backupDB.toString() + "\n" + currText);
				}

			}
		} catch (Exception e) {

			String currText = lblStatus.getText().toString();
			lblStatus.setText(e.toString() + "\n" + currText);
			Logger.e(this, "SaveBackup", e.getMessage());
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

	private void UploadFile() {
		new UploadTask().execute(new UploadTask.UploadTaskPayLoad(
				getApplicationContext(), new Object[] { BackupActivity.this }));
	}

	private String getMachineId() {
		ArrayList<Center> centerList;
		String machineID = "";
		centerList = CentersOperations.getCenter(this);
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

	@Override
	public void onBackPressed() {
		goHome();
	}

	public void goHome() {
		this.finish();
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}
