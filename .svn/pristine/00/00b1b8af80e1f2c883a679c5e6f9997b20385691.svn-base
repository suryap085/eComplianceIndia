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
import org.opasha.eCompliance.ecompliance.TextFree.IsResourcesDownloadedTask;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.DefaultMark;
import org.opasha.eCompliance.ecompliance.util.DoseUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.MissedDoseGeneration;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		if (!ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Is_Machine_Active, this).equals("true")) {
			startActivity(new Intent(this, AuthKeyActivity.class));
			this.finish();
			return;
		}
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {

				final Dialog dialog = new Dialog(StartActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				Button noButton = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				Button yesButton = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
				TextView message = (TextView) dialog
						.findViewById(R.id.messageText);
				TextView title = (TextView) dialog.findViewById(R.id.text);
				message.setText(getResources().getString(
						R.string.isdatetimecorrect)
						+ "\n"
						+ GenUtils.longToDateTimeString(System
								.currentTimeMillis()));
				title.setText(getResources().getString(
						R.string.checksystemdatetime));
				yesButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						final ProgressDialog progressDialog = ProgressDialog
								.show(StartActivity.this, "eCompliance",
										"Setting up Dashboard. Please wait...",
										true);
						Thread mThread = new Thread() {
							/* (non-Javadoc)
							 * @see java.lang.Thread#run()
							 */
							@Override
							public void run() {
								//Remove Missed Doses Prior to taken dose
								if (ConfigurationOperations.getKeyValue(
										ConfigurationKeys.key_is_utility_on_for_24_hours_missed_dose,
										StartActivity.this).isEmpty()
										|| ConfigurationOperations
												.getKeyValue(
														ConfigurationKeys.key_is_utility_on_for_24_hours_missed_dose,
														StartActivity.this).equals("1")) {
									DoseUtils.removeMissedDose(StartActivity.this);
									ConfigurationOperations
											.addConfiguration(
													ConfigurationKeys.key_is_utility_on_for_24_hours_missed_dose,
													"0", StartActivity.this);
								}
								
								//REMOVE unassigned Scans during registration
								DbUtils.removeUnassignedScans(StartActivity.this);
								
								//Mark Default
								DefaultMark.markDefault(StartActivity.this);
								
								//Generate Missed Doses
								boolean isDone = MissedDoseGeneration
										.generateMissedDoses(StartActivity.this);
								((eComplianceApp) StartActivity.this
										.getApplication()).lastMissedDate = 0;
								if (isDone) {
									((eComplianceApp) StartActivity.this
											.getApplication()).lastMissedDate = GenUtils
											.getCurrentDateLong();
								} else {
									try {
										((eComplianceApp) StartActivity.this
												.getApplication()).lastMissedDate = Long
												.parseLong(AppStateConfigurationOperations
														.getKeyValue(
																Enums.AppStateKeyValues.LastMissedDoseLoged
																		.toString(),
																getApplicationContext()));
									} catch (Exception e) {
									}
								}
								((eComplianceApp) StartActivity.this
										.getApplication())
										.setAfterSyncMisc(StartActivity.this);
								
								//Auto-Sync Receiver
								((eComplianceApp) StartActivity.this
										.getApplication())
										.SetupReceiverAutoSync();
								
								//Location and Ping Receiver Setup
								((eComplianceApp) StartActivity.this
										.getApplication())
										.SetupReceiverGetLocation();

								// send backup on open App
								try {
									if (ConfigurationOperations.getKeyValue(
											ConfigurationKeys.key_send_backup_on_start,
											StartActivity.this).isEmpty()
											|| ConfigurationOperations
													.getKeyValue(
															ConfigurationKeys.key_send_backup_on_start,
															StartActivity.this).equals("1"))
									{
										GenUtils.senBackup(StartActivity.this);
									}
								} catch (Exception e) {
								}

								//Check if Text Free System, download resources
								if (!((eComplianceApp) StartActivity.this
										.getApplicationContext()).IsAppTextFree) {
									startActivity(new Intent(
											StartActivity.this,
											HomeActivity.class));
									StartActivity.this.finish();
									progressDialog.dismiss();
								} else {
									new IsResourcesDownloadedTask()
											.execute(new IsResourcesDownloadedTask.DownloadTaskPayLoad(
													DbUtils.getTabId(StartActivity.this),
													getApplicationContext(),
													new Object[] {
															StartActivity.this,
															3 }));
								}

								// startActivity(new Intent(StartActivity.this,
								// HomeActivity.class));
								// StartActivity.this.finish();
								progressDialog.dismiss();

							}
						};
						mThread.start();

					}
				});
				// if button is clicked, close the custom dialog
				noButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
				dialog.show();
			}
		}, 2000);
	}

	public void goHome() {
		startActivity(new Intent(StartActivity.this, HomeActivity.class));
		StartActivity.this.finish();
	}
}
