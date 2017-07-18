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
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.DeCompress;
import org.opasha.eCompliance.ecompliance.util.DownloadTask;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.ProgressDialog;
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
public class RestoreActivity extends Activity {

	TextView lblStatus;

	String machineId;
	Button btnRestore;
	OutputStream myOutput;
	File sd;
	ProgressDialog pd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_restore);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		machineId = getMachineId();
		btnRestore = (Button) findViewById(R.id.btnBackup);
		btnRestore.setText(getResources().getString(R.string.Restore));
		if (machineId == null) {
			goHome();
		} else {
			if (machineId.isEmpty()) {
				goHome();
			}
			lblStatus = (TextView) findViewById(R.id.lblStatus);
			lblStatus.setText("");
		}
	}

	public void onRestoreClick(View v) {

		if (GenUtils.IsInternetConnected(this)) {
			if (btnRestore.getText().toString() != getResources().getString(
					R.string.Restore)) {
				goHome();
			} else {
				((eComplianceApp) this.getApplicationContext()).IsNetworkProcessBusy = true;
				new DownloadTask()
						.execute(new DownloadTask.DownloadTaskPayLoad(
								machineId, getApplicationContext(),
								new Object[] { 1, RestoreActivity.this }));
				pd = ProgressDialog.show(this, "",
						getResources().getString(R.string.RestorePD), true);
			}
		}

		else {
			Toast.makeText(this,
					"Internet not working..Please check internet connection!",
					Toast.LENGTH_LONG).show();
		}

	}

	public void Restore() {
		sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		UnzipBackup();
		try {

			// Set the folder on the SDcard
			File directory = new File(sd, "//eComplianceClient//" + machineId
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
				btnRestore.setActivated(false);

				if (directory.exists()) {
					if (directory.isDirectory()) {
						String[] children = directory.list();
						for (int i = 0; i < children.length; i++) {
							if (!children[i].equals("database.zip")) {
								SaveRestore(children[i]);
							}
						}
					}
					deleteDirectory(directory);
				}
				Toast.makeText(this,
						getResources().getString(R.string.RestoreSuccessToast),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this,
						getResources().getString(
								R.string.RestoreDirectryNotFound),
						Toast.LENGTH_SHORT).show();
				lblStatus.setText(getResources().getString(
						R.string.RestoreDirectryNotFound));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} catch (ExceptionInInitializerError e) {
			Toast.makeText(this,
					getResources().getString(R.string.RestoreUnsuccessToast),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		pd.cancel();
		btnRestore.setActivated(true);
		btnRestore.setText(getResources().getString(R.string.done));
		((eComplianceApp) this.getApplicationContext()).IsNetworkProcessBusy = false;
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

	private void SaveRestore(String database) {
		try {
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//eComplianceClient//" + machineId
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
					String currText = lblStatus.getText().toString();
					lblStatus.setText(getResources().getString(
							R.string.savedFile)
							+ backupDB.toString() + "\n" + currText);
				}

			}
		} catch (Exception e) {

			String currText = lblStatus.getText().toString();
			lblStatus.setText(e.toString() + "\n" + currText);

		}
	}

	private void UnzipBackup() {
		try {
			if (sd.canWrite()) {
				String zipPath = "//eComplianceClient//" + machineId
						+ "//Backup//database.zip";

				String currentDBPath = "//eComplianceClient//" + machineId
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
