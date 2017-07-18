/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VideosOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.TextFree.MasterIconOperation;
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.CustomExceptionHandler;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.LoadAppData;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.app.AlarmManager;
import android.app.Application;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class eComplianceApp extends Application {
	public long lastMissedDate;
	public ArrayList<String> visitToday;
	public ArrayList<String> missedDose;
	public ArrayList<String> pendingDoses;
	public ArrayList<String> hospitalised;
	public boolean IsAdminLoggedIn;
	public boolean IsAppTextFree;
	public boolean IsResourceDownloaded;
	public long LoginTime; // Last Login Time
	public String LastLoginId; // Last Admin Logged Id
	public Timer signOffTimer;
	public boolean IsDemo = false;
	public String App_Title;
	public PendingIntent downloadPatientIcon;
	public int maxModel;
	public int farValue;
	public int qualityBenchmark;
	public int tempQualityBenchmark;
	public int maxTemplatesToStore;
	public int maxIPExIPUnsupervisedDose;
	public int maxCPUnsupervisedDose;
	public int maxCPUnsupervisedDoseforDailyRegimen;
	public int maxIPExIPUnsupervisedDoseforDailyRegimen;
	public int AppSyncAll;
	public boolean isUnsupervisedEnable;
	public long syncBackDataMilli;
	public long currentDate;
	public VisitorType visitorType;

	public boolean IsIris;
	public int IrisRegQuality;
	public int MaxIrisTemplatePatient;
	public int MaxIrisTemplatePm;
	public int MaxIrisTemplateProvider;
	public int MaxIrisTemplateOthers;

	public boolean IsNetworkProcessBusy = false;

	public long locationInterval = 300000;

	public boolean downloadManagerCalled = false;
	public ArrayList<Long> enqueue;

	public boolean IsAllVideosDownloaded = false;

	/**
	 * @param args
	 */
	public eComplianceApp() {
		IsAppTextFree = false;
		IsResourceDownloaded = false;
		lastMissedDate = 0;
		visitToday = new ArrayList<String>();
		missedDose = new ArrayList<String>();
		pendingDoses = new ArrayList<String>();
		hospitalised = new ArrayList<String>();
		maxTemplatesToStore = 7;
		syncBackDataMilli = 0L;
		maxIPExIPUnsupervisedDose = 4;
		maxCPUnsupervisedDose = 2;
		maxCPUnsupervisedDoseforDailyRegimen = 2;
		maxIPExIPUnsupervisedDoseforDailyRegimen = 8;
		SignOff();
		currentDate = GenUtils.getCurrentDateLong();
		isUnsupervisedEnable = true;
		IsIris = false;
		IrisRegQuality = 80;

		MaxIrisTemplatePatient = 2;
		MaxIrisTemplatePm = 8;
		MaxIrisTemplateProvider = 8;
		MaxIrisTemplateOthers = 2;

		locationInterval = 300000;// Default interval is 5 Minutes
		enqueue = new ArrayList<Long>();
		IsAllVideosDownloaded = false;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Master_QA_Questions.deleteall(this);
		Logger.e(getApplicationContext(), "eComplianceApp",
				"Application Started!");
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
				this));// Set Default Exception Handler

		GetConfigurtions();
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		App_Title = getResources().getString(R.string.app_name) + " "
				+ pInfo.versionName;
		if (IsDemo) {
			App_Title = getResources().getString(R.string.app_name) + " "
					+ pInfo.versionName + " - "
					+ getResources().getString(R.string.demo);
		}

		tempQualityBenchmark = qualityBenchmark;

		try {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_auto_sync_duration, this).equals("")) {
				if (AppStateConfigurationOperations.getKeyValue(
						AppStateConfigurationKeys.key_user_auto_sync_duration,
						this).equals(""))
					AppStateConfigurationOperations
							.addAppStateConfiguration(
									AppStateConfigurationKeys.key_user_auto_sync_duration,
									"Server", this);
			}

		} catch (Exception e) {
			AppStateConfigurationOperations.addAppStateConfiguration(
					AppStateConfigurationKeys.key_user_auto_sync_duration, "",
					this);
		}

		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
					isAllVideosDownloaded();
					long itemToDel = 0;

					for (long item : ((eComplianceApp) getApplicationContext()).enqueue) {
						itemToDel = item;
					}

					if (itemToDel > 0) {
						((eComplianceApp) getApplicationContext()).enqueue
								.remove(itemToDel);

						if (((eComplianceApp) getApplicationContext()).enqueue
								.size() == 0) {
							((eComplianceApp) getApplicationContext()).downloadManagerCalled = false;
						}
					}
				}
			}
		};

		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));

	}

	public void GetConfigurtions() {
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_text_free, this).equals("true")) {
			IsAppTextFree = true;
		}
		// new QAquestions(this);
		ArrayList<String> list = MasterIconOperation.getIcons(
				Schema.MASTER_ICON_DOWNLOADED + "=0", this);
		if (list.size() == 0) {
			((eComplianceApp) this.getApplicationContext()).IsResourceDownloaded = true;
		}

		maxModel = 4;
		farValue = 225;
		qualityBenchmark = 6;

		try {
			farValue = Integer.parseInt(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_farn_value, this));
		} catch (Exception e) {
			farValue = 225;
		}

		try {
			IrisRegQuality = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_Iris_max_quality, this));
		} catch (Exception e) {
			IrisRegQuality = 80;
		}

		try {

			IsIris = ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_used_device, this).equals("iris");
		} catch (Exception e) {
			IsIris = false;
		}

		try {
			MaxIrisTemplatePatient = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_iris_temp_patient,
							this));
		} catch (Exception e) {
			MaxIrisTemplatePatient = 2;
		}

		try {
			MaxIrisTemplatePm = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_iris_temp_pm, this));
		} catch (Exception e) {
			MaxIrisTemplatePm = 8;
		}

		try {
			MaxIrisTemplateProvider = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_iris_temp_provider,
							this));
		} catch (Exception e) {
			MaxIrisTemplateProvider = 8;
		}

		try {
			MaxIrisTemplateOthers = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_iris_temp_other,
							this));
		} catch (Exception e) {
			MaxIrisTemplateOthers = 2;
		}

		try {
			maxTemplatesToStore = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_templates_store,
							this));
		} catch (Exception e) {
			maxTemplatesToStore = 7;
		}

		try {
			maxTemplatesToStore = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_templates_store,
							this));
		} catch (Exception e) {
			maxTemplatesToStore = 7;
		}

		try {
			maxModel = Integer.parseInt(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_max_scan_model, this));
		} catch (Exception e) {
			maxModel = 4;
		}
		try {
			qualityBenchmark = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_scan_quality_Benchmark,
							this));
		} catch (Exception e) {
			qualityBenchmark = 6;
		}
		try {
			syncBackDataMilli = Long.parseLong(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_sync_back_data_milli,
							this));
		} catch (Exception e) {
			syncBackDataMilli = 0L;
		}
		try {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_unsupervised_dose_enabled, this)
					.equals("true")) {
				isUnsupervisedEnable = true;

			} else
				isUnsupervisedEnable = false;
		} catch (Exception e) {
			isUnsupervisedEnable = true;
		}

		LoadAppData.setloacle(this);
		try {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_machine_demo,
					this.getApplicationContext()).equals("true")) {
				IsDemo = true;
			}
		} catch (Exception e) {
		}
		ConfigurationOperations.addConfiguration(
				ConfigurationKeys.key_is_positive_con_enable, "1", this);

		try {
			locationInterval = Long.parseLong(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_get_location_interval,
							this.getApplicationContext()));
		} catch (Exception e1) {
			locationInterval = 300000;
		}
	}

	/**
	 * Setup Receiver for Auto Saving Location Data after every 20 minutes
	 * 
	 */
	public void SetupReceiverGetLocation() {
		try {
			long interval = locationInterval; // Default is 5 Minutes
			Intent intent = new Intent(
					"org.opasha.eCompliance.ecompliance.AUTO_LOCATION");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
					intent, 0);
			AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
			manager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis() + interval, interval,
					pendingIntent);
			Logger.i(this, "Location and Ping Receiver",
					"Location and Ping Reciver Started.");
		} catch (Exception e1) {
			Logger.e(getApplicationContext(),
					"eComplianceApp.SetupReceiverGetLocation", e1
							.getStackTrace().toString());
		}
	}

	/**
	 * Setup Receiver Auto Sync
	 * 
	 */
	public void SetupReceiverAutoSync() {
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Is_Machine_Active, this).equals("true")) {
			// Get Duration from Configuration
			long timerDuration = 0;
			try {
				String key = AppStateConfigurationOperations.getKeyValue(
						AppStateConfigurationKeys.key_user_auto_sync_duration,
						this);
				if (key.equals("Server")) {
					timerDuration = Long.parseLong(ConfigurationOperations
							.getKeyValue(
									ConfigurationKeys.key_auto_sync_duration,
									this));
				} else {
					timerDuration = Long
							.parseLong(AppStateConfigurationOperations
									.getKeyValue(
											AppStateConfigurationKeys.key_user_auto_sync_duration,
											this));
				}
			} catch (NumberFormatException e) {
			}
			// Start Receiver only when duration is more than 0 milliseconds.
			if (timerDuration > 0) {
				Intent intent = new Intent(
						"org.opasha.eCompliance.ecompliance.AUTO_SYNC_TIMER");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
						0, intent, 0);
				AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
				manager.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis() + timerDuration,
						timerDuration, pendingIntent);
				Logger.i(this, "AutoSyncReceiver",
						"Auto Sync Reciver Started with Duration: "
								+ timerDuration);
			} else
				Logger.e(this, "AutoSyncReceiver",
						"Auto Sync Reciver Not Started.");
		}

	}

	public void SignOff() {
		LoginTime = 0;
		IsAdminLoggedIn = false;
		LastLoginId = "";
	}

	public void setAfterSyncMisc(Context context) {
		GenUtils.setAppPendingToday(context);
		GenUtils.setAppMissedDose(context);
		GenUtils.setAppVisitedToday(context);
		GenUtils.setHospitalisedPatient(context);
		try {
			farValue = Integer.parseInt(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_farn_value, context));
		} catch (Exception e) {
		}
		try {
			maxModel = Integer.parseInt(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_max_scan_model, context));
		} catch (Exception e) {
		}
		try {
			qualityBenchmark = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_scan_quality_Benchmark,
							context));
		} catch (Exception e) {
		}
	}

	public void SignOffWithTimer() {
		try {
			if (signOffTimer != null) {
				signOffTimer.cancel();
				signOffTimer.purge();
			}
		} catch (Exception e) {
			Logger.e(this, "SignOffWithTimer", e.getMessage());
		}
		signOffTimer = new Timer();
		long delay = 7200000L; // Default delay after which
		// Administrator will
		// auto-default
		try {
			String value = ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_admin_auto_log_off_time, this);
			if (value != null) {
				if (!value.isEmpty()) {
					if (Long.parseLong(value) > 0) {
						delay = Long.parseLong(value);
					}
				}
			}
		} catch (Exception e) {
			Logger.e(this, "SignOffWithTimer", e.getMessage());
		}
		try {
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					SignOff();
				}
			};
			signOffTimer.schedule(task, delay);
		} catch (Exception e) {
			Logger.e(this, "SignOffWithTimer", e.getMessage());
		}
	}

	public void isAllVideosDownloaded() {
		IsAllVideosDownloaded = true;
		try {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_video_counseling_enabled, this)
					.equals("1")) {
				ArrayList<String> videos = VideosOperations.GetVideoList(this);
				boolean isAllDownloaded = true;
				for (String video : videos) {
					video += ".mp4";
					File f = new File(
							Environment
									.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
									+ "/eCounseling/Videos/" + video);
					if (!f.exists()) {
						isAllDownloaded = false;
					}
				}
				IsAllVideosDownloaded = isAllDownloaded;
			}
		} catch (Exception e) {
		}
	}
}