/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.database.module.AuthenticationTask;
import org.opasha.eCompliance.ecompliance.database.module.SyncTask;
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AuthKeyActivity extends Activity {
	Button save;
	EditText key;
	TextView errorMessage;
	ProgressDialog pd;
	LinearLayout authLayout;
	public MachineAuth machineAuth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_key_layout);
		key = (EditText) findViewById(R.id.authKey);
		save = (Button) findViewById(R.id.btnSave);
		authLayout = (LinearLayout) findViewById(R.id.authLayout);
		errorMessage = (TextView) findViewById(R.id.errorMessage);
	}

	public void onSaveClick(View v) {
		String authKey = key.getText().toString().trim();
		AppStateConfigurationOperations.addAppStateConfiguration(AppStateConfigurationKeys.key_Sync_All,"1", this);
		
		if (authKey.length() <= 0) {
			errorMessage.setVisibility(View.VISIBLE);
			getRedAnimation();
			return;
		}
		if (authKey.startsWith("0000")) {
			ConfigurationOperations.addConfiguration(
					ConfigurationKeys.key_is_machine_demo, "true", this);
		} else {
			ConfigurationOperations.addConfiguration(
					ConfigurationKeys.key_is_machine_demo, "false", this);
		}
		new AuthenticationTask()
				.execute(new AuthenticationTask.AuthTaskPayLoad(
						getApplicationContext(), new Object[] { 1, this,
								key.getText().toString().trim() }));
		pd = ProgressDialog.show(this, "",
				getResources().getString(R.string.pdAuthData), true);

	}

	private void getRedAnimation() {
		authLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) authLayout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	public void ShowNotification() {
		pd.cancel();
		if (machineAuth == null) {
			authErrorMessage();
			return;
		}
		if (machineAuth.machineId == 0 || (!machineAuth.isActive)
				|| machineAuth.errorMessage.equals("keyManagementError")) {
			authErrorMessage();
		} else {
			new SyncTask().execute(new SyncTask.SyncTaskPayLoad(
					getApplicationContext(), new Object[] { 1, this }));
			((eComplianceApp) this.getApplication()).SetupReceiverAutoSync(); // Called
			// when
			// machine
			// becomes
			// active
			pd = ProgressDialog.show(this, "",
					getResources().getString(R.string.PdSyncData), true);

		}

	}

	public void goHome() {
		if (pd != null) {
			pd.cancel();
		}
		int totalActivePatient=PatientsOperations.getActivePatientCount(this);
		AppStateConfigurationOperations.addAppStateConfiguration(AppStateConfigurationKeys.key_Sync_All, "0", this);
		if(totalActivePatient>0)
		{
			AppStateConfigurationOperations.addAppStateConfiguration(
					AppStateConfigurationKeys.key_patients_contains_value, "true", this);
		}
		
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home,
				getResources().getString(R.string.syncComplete));
		intent.putExtra(IntentKeys.key_signal_type, Enums.Signal.Good);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	/**
	 * Show Authentication Error Message
	 */
	private void authErrorMessage() {

		String message = "";
		if (machineAuth == null) {
			message = getResources().getString(
					R.string.internetConnectionUnavailable);
		} else {
			if (machineAuth.errorMessage != null) {
				if (machineAuth.errorMessage.equals("3")) {
					message = getResources().getString(
							R.string.clusterLockMessage);
				} else if (machineAuth.errorMessage.equals("4")) {
					message = getResources().getString(R.string.syncComplete);
				} else if (machineAuth.errorMessage.equals("2")) {
					message = getResources().getString(
							R.string.notificationMachineInactive);
				} else if (machineAuth.errorMessage.equals("1")) {
					message = getResources().getString(R.string.AuthErrorDesc);
				} else if (machineAuth.errorMessage
						.equals("keyManagementError")) {
					message = getResources().getString(
							R.string.keyManagementError);
				}
			} else {
				message = getResources().getString(R.string.AuthErrorDesc);
			}
		}
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.AuthError))
				.setMessage(message)
				.setNeutralButton(getResources().getString(R.string.Retry),
						null).show();
	}

}
