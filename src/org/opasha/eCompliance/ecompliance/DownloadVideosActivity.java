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
import java.io.OutputStream;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.DeCompress;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.VideoDownloadTask;

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
public class DownloadVideosActivity extends Activity {
	TextView viewStatus;
	
	String machineId;
	Button btnDownload;
	OutputStream myOutput;
	File sd;
	ProgressDialog pd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_download_videos);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		machineId = getMachineId();
		btnDownload = (Button) findViewById(R.id.btnBackup);
		btnDownload.setText(R.string.DownloadVideos);
		
		
		if (machineId == null) {
		    goHome();
		} else {
			if (machineId.isEmpty()) {
				goHome();
			}
						
			viewStatus = (TextView) findViewById(R.id.lblStatus);
		}
	}

	public void onRestoreClick(View v) {
		if (GenUtils.IsInternetConnected(this)) {
			
				new VideoDownloadTask()
						.execute(new VideoDownloadTask.VideoDownloadTaskPayLoad(
								machineId, getApplicationContext(),
								new Object[] { 1, DownloadVideosActivity.this }));
				pd = ProgressDialog.show(this, "",
						getResources().getString(R.string.DownloadVideosPD), true);
			}
		
		else {
			Toast.makeText(this,
					"Internet not working..Please check internet connection!",
					Toast.LENGTH_LONG).show();
		}

	}

	public void PostDownload() {
		sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		UnzipBackup();
		try {
				btnDownload.setActivated(false);		
				Toast.makeText(this,
						getResources().getString(R.string.DownloadVideosSuccessToast),
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (ExceptionInInitializerError e) {
			Toast.makeText(this,
					getResources().getString(R.string.DownloadVideosUnsuccessToast),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		pd.cancel();

		viewStatus.setBackgroundColor(getResources().getColor(R.color.YellowGreen));
	}

	private void UnzipBackup() {
		sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		try {
			if (sd.canWrite()) {
				File zipFolderName = new File(sd,"//eCounseling//Videos");
				for(File f: zipFolderName.listFiles())
				{
					if(f.getCanonicalPath().endsWith(".zip"))
					{
						//String zipPath = "//eCounseling//Videos//Videos.zip";
						String currentDBPath = "//eCounseling//Videos";
						File currentDB = new File(sd, currentDBPath);
//						File zipPathFile = new File(sd, zipPath);
						DeCompress d = new DeCompress(currentDB.getPath(),
								f.getPath());
						d.unzip();
					}
				}
				
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

	public void showProgress(String progress)
	{
		pd.setMessage(getResources().getString(R.string.DownloadVideosPD) + "\n" + progress);
	}
	
	public void goHome() {
		this.finish();
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}
