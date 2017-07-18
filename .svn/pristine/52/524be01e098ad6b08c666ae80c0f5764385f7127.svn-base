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
package org.opasha.eCompliance.ecompliance.TextFree;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author abhishek
 * 
 */
public class ResourceActivity extends Activity {

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
			this.finish();
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		} else {
			if (machineId.isEmpty()) {
				goHome();
			}
			lblStatus = (TextView) findViewById(R.id.lblStatus);
			lblStatus.setText("");
		}
	}

	public void onRestoreClick(View v) {
		if (btnRestore.getText().toString() != getResources().getString(
				R.string.Restore)) {
			this.finish();
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		} else {
			new DownloadResourcesTask()
					.execute(new DownloadResourcesTask.DownloadTaskPayLoad(
							machineId, getApplicationContext(),
							new Object[] { ResourceActivity.this }));
			pd = ProgressDialog.show(this, "",
					getResources().getString(R.string.RestorePD), true);
		}
	}

	public void Restore() {
		if (pd != null) {
			pd.cancel();
		}
		startActivity(new Intent(this, HomeActivityTextFree.class));
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
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}
}
