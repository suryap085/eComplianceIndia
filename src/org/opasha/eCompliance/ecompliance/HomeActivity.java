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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;

import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ECounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.FutureMissedDoseOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialCounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Master_QA_Questions;
import org.opasha.eCompliance.ecompliance.DbOperations.NetworkOperatorOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDiabetesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsRepeatCounsellingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.QualityAuditingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Scan_Identification_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisR_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansROperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.UnSupervisedDoseReasonOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VideosCategoryOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VideosOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorLoginOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Dose;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.IrisRModel;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List_Obj;
import org.opasha.eCompliance.ecompliance.Model.NetworkOperator;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.Model.ScanRModel;
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.Synchronization.SyncBackupData;
import org.opasha.eCompliance.ecompliance.database.module.SyncTask;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.DefaultMark;
import org.opasha.eCompliance.ecompliance.util.DoseUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.TestIds;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.IddkCaptureInfo;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.IrisDefault;
import org.opasha.eCompliance.ecompliance.util.IrisUtility;
import org.opasha.eCompliance.ecompliance.util.Logger;
import org.opasha.eCompliance.ecompliance.util.MediaData;
import org.opasha.eCompliance.ecompliance.util.MissedDoseGeneration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.futronictech.SDKHelper.FTR_PROGRESS;
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;
import com.futronictech.SDKHelper.IIdentificationCallBack;
import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
import com.futronictech.SDKHelper.VersionCompatible;
import com.google.gson.Gson;
import com.iritech.driver.UsbNotification;
import com.iritech.iddk.android.HIRICAMM;
import com.iritech.iddk.android.Iddk2000Apis;
import com.iritech.iddk.android.IddkCaptureStatus;
import com.iritech.iddk.android.IddkCommStd;
import com.iritech.iddk.android.IddkComparisonResult;
import com.iritech.iddk.android.IddkConfig;
import com.iritech.iddk.android.IddkDataBuffer;
import com.iritech.iddk.android.IddkDeviceInfo;
import com.iritech.iddk.android.IddkEyeSubType;
import com.iritech.iddk.android.IddkImage;
import com.iritech.iddk.android.IddkImageFormat;
import com.iritech.iddk.android.IddkImageKind;
import com.iritech.iddk.android.IddkInteger;
import com.iritech.iddk.android.IddkIrisQuality;
import com.iritech.iddk.android.IddkResult;

public class HomeActivity extends Activity implements IIdentificationCallBack {
	boolean isError = false;
	boolean isClosing = false;
	boolean isScanOn = false;
	ProgressDialog pd;
	ImageView imgScan;
	TextView lblHeader, lblText, counselorDetail, CenterName, machineType;
	LinearLayout layout;
	Button visitCount, missedCount, pendingCount, btnFBSResult, btnHBA1cResult,
			syncbutton;
	int DoseCount, SelfAdminDose = 0;
	ImageView imgSignOff;
	String pTreatmentId = "";
	Button btnUnsupervisedDose;
	PopupWindow mpopup;
	public MachineAuth machineAuth;
	private UsbDeviceDataExchangeImpl usb_host_ctx = null;
	private FutronicSdkBase m_Operation;
	public static final int MESSAGE_SHOW_MSG = 1;
	public static final int MESSAGE_SHOW_IMAGE = 2;
	public static final int MESSAGE_COMPLETE = 3;
	public static final int MESSAGE_SHOW_HEADER = 4;
	public static final int MESSAGE_SHOW_GREEN = 5;
	public static final int MESSAGE_SHOW_RED = 6;
	public static final int MESSAGE_IDENTIFICATION = 7;
	public static final int MESSAGE_UNSUPERVISED_GONE = 8;
	public static final int MESSAGE_SHOW_YELLOW = 9;
	public static final int MESSAGE_SHOW_IDENTIFICATION_NOT_FOUND = 10;
	private static Bitmap mBitmapFP = null;
	ArrayList<ScanModel> scans;
	ArrayList<PatientDoseTakenPriorViewModel> patientdosetakenlist;
	private boolean isUnsupervisedEnable = false;
	SyncBackupData syncbackuptask;
	private boolean isInitialCounselingPopup = false;
	private ToggleButton btncameraOn_Off;

	// Variable Declaration for IrisEnrollment and Identification
	private static final String _FORMAT_REJECT_MSG = "The captured image's quality is not sufficient for %s.\nPlease capture another image with subject's eye opened widely and moved slowly towards the camera.";
	private static final String STR_REJECT_ENROLLMENT = String.format(
			_FORMAT_REJECT_MSG, "enrollment");
	private static final String STR_REJECT_IDENTIFICATION = String.format(
			_FORMAT_REJECT_MSG, "identification");
	private static final String STR_WARNING_QUALITY_ENROLLMENT = "The captured image is enrollable but is not in sufficient quality to warrant the best accuracy."
			+ "The subject is recommended to have his/her iris image recaptured with the eye opened widely and moved slowly towards the camera.\n"
			+ "Do you want to proceed anyway?";
	private static final int IRIS_ENROLL = 14;
	private static final int IRIS_IDENTIFY = 16;
	private static final int IRIS_HANDLE_ERROR = 17;
	private HIRICAMM mDeviceHandle = null;
	private IddkCaptureStatus mCurrentStatus = null;
	private IddkResult mCaptureResult = null;
	private IddkCaptureInfo mCaptureInfo = null;
	private IrisDefault mManiaConfig = null;
	private UsbNotification mUsbNotification = null;
	private ArrayList<IddkImage> monoBestImage;
	private IddkDataBuffer capturedTemplate;
	private MediaData mMediaData = null;
	private Bitmap mCurrentBitmap = null;
	private boolean mIspreviewing = false;
	private int irisRegCurrentAction = 0;
	private int mTotalScore = 0;
	private int mUsableArea = 0;
	private boolean mIsCameraReady = false;
	private int mScreenWidth = 0;
	private static Iddk2000Apis mApis;

	private boolean IrisLoaded;

	ProgressDialog progressDialogTemplates;

	private enum eIdentifyResult {
		IRI_IDENTIFY_DIFFERENT, IRI_IDENTIFY_LOOKLIKE, IRI_IDENTIFY_SAME, IRI_IDENTIFY_DUPLICATED
	};

	private int minIrisQuality;
	private String eye;

	private boolean isStartCp = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApis = Iddk2000Apis.getInstance(this);
		minIrisQuality = ((eComplianceApp) this.getApplication()).IrisRegQuality;

		if (!ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Is_Machine_Active, this).equals("true")) {
			startActivity(new Intent(this, AuthKeyActivity.class));
			this.finish();
			return;
		}

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_unsupervised_dose_enabled, this).equals(
				"true"))
			isUnsupervisedEnable = true;
		long lastMissedDoseDate = ((eComplianceApp) this.getApplication()).lastMissedDate;
		long currentDate = GenUtils.getCurrentDateLong();
		if (lastMissedDoseDate > 0
				&& (lastMissedDoseDate - currentDate) > 86400000) {
			org.opasha.eCompliance.ecompliance.util.Logger.LogToDb(this,
					"Date Error",
					"Date Time of tab was changed to an earlier date - "
							+ lastMissedDoseDate);
			this.finish();
			return;
		}
		Bundle extras = getIntent().getExtras();
		if (((eComplianceApp) this.getApplication()).IsAppTextFree) {
			Intent intent = new Intent(
					this,
					org.opasha.eCompliance.ecompliance.TextFree.HomeActivityTextFree.class);
			if (extras != null)
				intent.putExtras(extras);
			startActivity(intent);
			this.finish();
			return;
		}
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		setContentView(R.layout.activity_home);
		pendingCount = (Button) findViewById(R.id.pendingPatientcount);
		visitCount = (Button) findViewById(R.id.visitedTodayCount);
		syncbutton = (Button) findViewById(R.id.syncbutton);
		missedCount = (Button) findViewById(R.id.missedDoseCount);
		btnUnsupervisedDose = (Button) findViewById(R.id.btnUnsupervisedDose);
		counselorDetail = (TextView) findViewById(R.id.counselorName);
		CenterName = (TextView) findViewById(R.id.centerName);
		machineType = (TextView) findViewById(R.id.machineType);
		btnFBSResult = (Button) findViewById(R.id.btnFBSResult);
		btnHBA1cResult = (Button) findViewById(R.id.btnHBA1cResult);
		btncameraOn_Off = (ToggleButton) findViewById(R.id.btncameraOn_Off);

		try {
			long lLastSync = Long
					.valueOf(AppStateConfigurationOperations.getKeyValue(
							AppStateConfigurationKeys.Key_Last_Sync, this));
			syncbutton.setText(getResources().getString(R.string.sync) + "\n"
					+ getResources().getString(R.string.synctxt)
					+ GenUtils.longToDateTimeString(lLastSync));

			long lDuration = System.currentTimeMillis() - lLastSync;

			long fifteenMinutes = 900000L;
			long sixtyMinutes = 3600000L;

			if (lDuration > sixtyMinutes) {
				syncbutton.setBackgroundColor(Color.RED);
				Drawable top = getResources().getDrawable(R.drawable.sync_red);
				syncbutton.setCompoundDrawablesWithIntrinsicBounds(null, top,
						null, null);
			} else {
				if (lDuration > fifteenMinutes) {
					syncbutton.setBackgroundColor(getResources().getColor(
							R.color.Orange));
					Drawable top = getResources().getDrawable(
							R.drawable.sync_orange);
					syncbutton.setCompoundDrawablesWithIntrinsicBounds(null,
							top, null, null);
				}
			}

		} catch (Exception e) {
		}
		setDashboard();
		machineType.setText(CentersOperations.getMachineType(this) + "("
				+ DbUtils.getTabId(this) + ")");
		CenterName.setText(CentersOperations.getCenterName(this));
		counselorDetail.setText(ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_counselor_name, this));
		imgScan = (ImageView) findViewById(R.id.imgIdentFpImg);
		lblHeader = (TextView) findViewById(R.id.lblIdentHeader);
		lblText = (TextView) findViewById(R.id.lblIdentText);
		layout = (LinearLayout) findViewById(R.id.layoutHome);
		imgSignOff = (ImageView) findViewById(R.id.imgAdminLoggedIn);
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		// layout.setBackgroundColor(getResources().getColor(R.color.homeNew));

		if (MissedDoseGeneration.generateMissedDoses(this)) {
			PatientsRepeatCounsellingOperations.Delete(this);
			DefaultMark.markDefault(this);
			// Update Taken, Pending and Missed List
			((eComplianceApp) this.getApplication()).setAfterSyncMisc(this);

		}

		// TODO: Delete all missed dose for current date
		DoseAdminstrationOperations.deleteCurrentDayMissedDose(this);

		((eComplianceApp) this.getApplication()).lastMissedDate = currentDate;

		// Show Messages from Other Activity
		if (extras != null) {
			boolean handleScan = false;
			try {
				try {
					String treatmentId = extras
							.getString(IntentKeys.key_handleScan_home);

					if (treatmentId != null) {
						if (!treatmentId.isEmpty()) {
							handleScan = true;

							HandleScan(treatmentId);
						}
					}
				} catch (Exception e) {

				}

				if (!handleScan) {

					String msg = extras.getString(IntentKeys.key_message_home);

					mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1, msg)
							.sendToTarget();
					Signal signal = Signal.getSignal(extras
							.getString(IntentKeys.key_signal_type));
					if (signal == Signal.Good)
						mHandler.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();
					else {
						if (signal == Signal.Bad)
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
					}

				}
			} catch (Exception e) {
			}
		}

		final int scanCheck = CheckScans();
		if (scanCheck > 0) {
			progressDialogTemplates = ProgressDialog.show(HomeActivity.this,
					"eCompliance", "Generating Scans. Please wait...", true);

			Thread thread = new Thread() {
				@Override
				public void run() {
					GenerateScans(scanCheck);
					progressDialogTemplates.dismiss();
				}
			};
			thread.start();
			return;
		}

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_used_device, this).equals("iris")) {
			scans = ScansIrisOperations.getScans(false, this);
			if (scans.size() > 0) {
				IrisLoaded = false;
				scanEye();
				setInitState();
				openDevice();
				// mHandler.obtainMessage(IRIS_ENROLL).sendToTarget();
			}
		} else {
			btncameraOn_Off.setVisibility(View.GONE);
			scans = ScansOperations.getScans(false, this);

			if (scans.size() > 0) {
				usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
				scanFinger();

			}
		}

		btncameraOn_Off
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							startCamera(true);
						} else {
							stopCamera(false);
						}

					}
				});
		showLoggedInImage();

	}

	public void onDownloadClick(View view) {
		onDownloadClick();
	}

	public void onDownloadClick() {

		if (((eComplianceApp) this.getApplication()).IsAllVideosDownloaded) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					"All videos downloaded successfully.").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
			return;
		}

		if (((eComplianceApp) getApplicationContext()).downloadManagerCalled) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					"Download in Progress or all videos downloaded successfully.")
					.sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			return;
		}

		((eComplianceApp) getApplicationContext()).downloadManagerCalled = true;

		DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		ArrayList<String> videosFileNames = VideosOperations.GetVideoList(this);

		File outFileDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
						+ "/eCounseling/Videos");

		if (!outFileDir.exists()) {
			outFileDir.mkdir();
		}
		String outputDirectory = outFileDir.getAbsolutePath();

		for (String fName : videosFileNames) {
			fName += ".mp4";
			// Request request =
			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse("http://ec2-54-208-70-53.compute-1.amazonaws.com/eCounselingIndia/Videos/"
							+ fName));

			File f = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
							+ "/eCounseling/Videos/" + fName);
			if (f.exists()) {
				// DeCompress d = new DeCompress(outputDirectory,
				// f.getPath());
				// d.unzip();
				continue;
			}

			request.setDestinationInExternalPublicDir(
					Environment.DIRECTORY_DOWNLOADS, "eCounseling/Videos/"
							+ fName);
			((eComplianceApp) getApplicationContext()).enqueue.add(dm
					.enqueue(request));
		}

		if (((eComplianceApp) getApplicationContext()).enqueue.size() == 0) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					"All videos downloaded.").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			return;
		} else {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					"Downloading Videos.").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
			return;
		}
	}

	/**
	 * Checks if Scans are properly built
	 * 
	 * @return 0 if both table matches, 1 if Iris Tables don't match, 2 if FP
	 *         tables do not match
	 */
	private int CheckScans() {
		try {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_used_device, HomeActivity.this)
					.equals("iris")) {
				if (ScansIrisR_Operations.scanIrisRCount(HomeActivity.this) != ScansIrisOperations
						.scanIrisCount(HomeActivity.this)) {
					return 1;
				}
			} else {
				if (ScansROperations.scanFpRCount(HomeActivity.this) != ScansOperations
						.scanFpCount(HomeActivity.this)) {
					return 2;
				}
			}
		} catch (Exception e) {
			Log.e("error", e.toString());
		}
		return 0;
	}

	private void GenerateScans(int flag) {
		try {
			if (flag == 1) {
				ArrayList<IrisRModel> irisR = ScansIrisR_Operations
						.getScansIrisR(HomeActivity.this);
				ScansIrisOperations.emptyTable(HomeActivity.this);
				for (int i = 0; i < irisR.size(); i++) {
					try {
						ScansIrisOperations.addScanAfterRestore(
								irisR.get(i).treatmentId, irisR.get(i).eye,
								irisR.get(i).scan,
								irisR.get(i).CreationTimeStap,
								irisR.get(i).CreatedBy, HomeActivity.this);
					} catch (Exception e) {

					}
				}
				isClosing = true;
				try {
					usb_host_ctx.CloseDevice();
				} catch (Exception e) {
					Log.e("error", e.toString());
				}
			} else {

				ArrayList<ScanRModel> scansR = ScansROperations
						.getScansR(HomeActivity.this);
				ScansOperations.emptyTable(HomeActivity.this);
				for (int i = 0; i < scansR.size(); i++) {
					try {
						ScansOperations.addScanaAfterRestore(
								scansR.get(i).treatmentId, "",
								scansR.get(i).scan,
								scansR.get(i).CreationTimeStap,
								scansR.get(i).CreatedBy, HomeActivity.this);

					} catch (Exception e) {
						Log.e("error", e.toString());
					}
				}

			}
		} catch (Exception e) {
			Log.e("error", e.toString());
		}
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onDestroy() {
		isClosing = true;
		try {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_used_device, this).equals("iris")) {
				stopCamera(true);
				mApis.closeDevice(mDeviceHandle);
			} else {
				usb_host_ctx.CloseDevice();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	// Initiating Menu XML file (menu.xml)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_home, menu);

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_legacy_button_enabled_, this).equals(
				"true")) {
			menu.findItem(R.id.menu_home_Patients_from_legacy).setVisible(true);
		}
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_visitor_legacy_button_enabled, this)
				.equals("true")) {
			menu.findItem(R.id.menu_home_Visitors_from_legacy).setVisible(true);
		}

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_dashboard_enabled, this)
				.equals("true")) {
			menu.findItem(R.id.menu_home_DashBoard).setVisible(true);
		}

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_enable_client_auto_sync_dur, this)
				.equals("1")) {
			menu.findItem(R.id.menu_autosyncfrequency).setVisible(true);
		}

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_positive_con_enable, this).equals("1")) {
			menu.findItem(R.id.menu_pos_Contacts).setVisible(true);
		}
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_video_counseling_enabled,
				HomeActivity.this).equals("1")) {
			menu.findItem(R.id.menu_home_download_videos).setVisible(true);
		}
		menu.findItem(R.id.menu_download_resources).setVisible(false);
		return true;
	}

	public void btnHBA1cDoseClick(View v) {
		if (!isSundayOn()) {
			return;
		}
		isClosing = false;
		btnUnsupervisedDose.setVisibility(View.GONE);
		// *AS DEF2221 - Verify if Admin Logged In before editing a patient.
		if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.counselorLoginRequired))
					.sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, DiabetesActivity.class);
		intent.putExtra(IntentKeys.key_treatment_id, v.getTag().toString());
		intent.putExtra(IntentKeys.key_intent_from,
				Enums.TestIds.getId(TestIds.HBA1cTest));
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void btnFBSClick(View v) {
		if (!isSundayOn()) {
			return;
		}
		isClosing = false;
		btnUnsupervisedDose.setVisibility(View.GONE);
		// *AS DEF2221 - Verify if Admin Logged In before editing a patient.
		if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.counselorLoginRequired))
					.sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, DiabetesActivity.class);
		intent.putExtra(IntentKeys.key_treatment_id, v.getTag().toString());
		intent.putExtra(IntentKeys.key_intent_from,
				Enums.TestIds.getId(TestIds.glucometerTest));
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		boolean returnValue = true;
		switch (itemId) {
		case R.id.menu_home_DashBoard:
			startActivity(new Intent(this, Dashboard.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_backup:
			HomeActivity.this.finish();
			startActivity(new Intent(this, BackupActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_Restore:
			HomeActivity.this.finish();
			startActivity(new Intent(this, RestoreActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_setting:
			startActivity(new Intent(this, ConfigurationActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_download_videos:
			onDownloadClick();
			// isClosing = true;
			// try {
			// usb_host_ctx.CloseDevice();
			// } catch (Exception e) {
			// }
			// HomeActivity.this.finish();
			// startActivity(new Intent(this, DownloadVideosActivity.class));
			// overridePendingTransition(R.anim.right_side_in,
			// R.anim.right_side_out);
			break;

		case R.id.menu_home_display_videos:
			isClosing = true;
			try {
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}
			HomeActivity.this.finish();
			startActivity(new Intent(this,
					DisplayStaticVideosListActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;

		case R.id.menu_pos_Contacts:
			isClosing = true;
			try {
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}
			startActivity(new Intent(this, PositiveContactsActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			this.finish();
			break;
		case R.id.menu_home_Patients_from_legacy:
			isClosing = true;
			try {
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}

			Intent i = new Intent(this, PatientReportActivity.class);
			i.putExtra(IntentKeys.key_report_type,
					Enums.ReportType.PatientsFromLegacySystem);
			i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
			startActivity(i);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			this.finish();

			break;
		case R.id.menu_home_Visitors_from_legacy:
			isClosing = true;
			try {
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}

			Intent in = new Intent(this, VisitorReportActivity.class);
			in.putExtra(IntentKeys.key_report_type,
					Enums.ReportType.VisitorReregistration);
			in.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
			startActivity(in);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			this.finish();

			break;

		case R.id.menu_autosyncfrequency:
			setAutoSyncFrequency();
			break;
		case R.id.menu_home_Diagnosis:
			startActivity(new Intent(this, DiagnosisActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		default:
			returnValue = super.onOptionsItemSelected(item);
			break;
		}
		return returnValue;
	}

	public void btnNewVisitorClick(View v) {
		if (!isSundayOn()) {
			return;
		}
		isClosing = false;
		// *AS DEF2221 - Verify if Admin Logged In before editing a patient.
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Admin_Login_Required, this).equals("1")) {
			if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						getResources().getString(
								R.string.counselorLoginRequired))
						.sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "")
						.sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
				return;
			}
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, NewVisitorSelectActivity.class);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	protected void call() {
		TelephonyManager telephonyManager = ((TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE));
		String operatorName = telephonyManager.getNetworkOperatorName();
		if (operatorName.equals("")) {
			new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.networkError))
					.setMessage(
							getResources().getString(R.string.netwrokErrorDes))
					.setNeutralButton(getResources().getString(R.string.ok),
							null).show();
		}
		ArrayList<NetworkOperator> list = NetworkOperatorOperation
				.getOperators(this);
		for (NetworkOperator n : list) {
			if (operatorName.trim().toLowerCase()
					.startsWith(n.Name.trim().toLowerCase())) {
				String number = n.Number;
				startActivityForResult(new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + number + Uri.encode("#"))), 1);
			}
		}
	}

	public void visitClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, PatientReportActivity.class);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.VisitedPatients);
		intent.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void pendingClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, PatientReportActivity.class);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.PendingPatients);
		intent.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void missedClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, PatientReportActivity.class);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.MissedPatients);
		intent.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onHavingTroubleClick(View v) {
		if (!isSundayOn()) {
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, EnterIdActivity.class);
		intent.putExtra(IntentKeys.key_verification_reason,
				getString(R.string.MarkVisit));
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void btnEditVisitorClick(View v) {
		if (!isSundayOn()) {
			return;
		}
		isClosing = false;
		btnUnsupervisedDose.setVisibility(View.GONE);
		// *AS DEF2221 - Verify if Admin Logged In before editing a patient.
		if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.counselorLoginRequired))
					.sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		this.finish();
		Intent intent = new Intent(this, IdentifyActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void btnViewVisitorClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, Reports.class);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onScanClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void btnUnsupervisedDoseClick(View v) {
		isClosing = false;
		btnUnsupervisedDose.setVisibility(View.GONE);
		if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.counselorLoginRequired))
					.sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		int maxNumOfDose = 0;
		int unsupervisedCount = 0;
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(pTreatmentId,
						this), this);
		// try {
		// maxNumOfDose = Integer.parseInt(ConfigurationOperations
		// .getKeyValue(ConfigurationKeys.key_max_unsupervised_dose,
		// this));
		// } catch (NumberFormatException e) {
		//
		// e.printStackTrace();
		// }

		if (regimen.stage.equals(Enums.StageType.getStageType(StageType.IP))
				|| regimen.stage.equals(Enums.StageType
						.getStageType(StageType.ExtIP))) {
			unsupervisedCount = PatientDosePriorOperations
					.getIP_ExIP_DosesCount(pTreatmentId, regimen.catagory,
							"IP",
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this)
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
							pTreatmentId, regimen.catagory, "Ext-IP",
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this);
			if (regimen.daysFrequency == 1) {
				if (unsupervisedCount == ((eComplianceApp) this
						.getApplication()).maxIPExIPUnsupervisedDoseforDailyRegimen) {
					isClosing = false;
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources()
									.getString(
											R.string.patientCannotBeGivenUnsupervisedDose))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				}
				maxNumOfDose = ((eComplianceApp) this.getApplication()).maxIPExIPUnsupervisedDoseforDailyRegimen;
			} else {
				if (unsupervisedCount == ((eComplianceApp) this
						.getApplication()).maxIPExIPUnsupervisedDose) {
					isClosing = false;
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources()
									.getString(
											R.string.patientCannotBeGivenUnsupervisedDose))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				}
				maxNumOfDose = ((eComplianceApp) this.getApplication()).maxIPExIPUnsupervisedDose;
			}
		} else if (regimen.stage.equals(Enums.StageType
				.getStageType(StageType.CP))) {
			unsupervisedCount = PatientDosePriorOperations
					.getIP_ExIP_DosesCount(pTreatmentId, regimen.catagory,
							"CP",
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this);
			if (regimen.daysFrequency == 1) {
				if (unsupervisedCount == ((eComplianceApp) this
						.getApplication()).maxCPUnsupervisedDoseforDailyRegimen) {
					isClosing = false;
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources()
									.getString(
											R.string.patientCannotBeGivenUnsupervisedDose))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				}
				maxNumOfDose = ((eComplianceApp) this.getApplication()).maxCPUnsupervisedDoseforDailyRegimen;
			} else {
				if (unsupervisedCount == ((eComplianceApp) this
						.getApplication()).maxCPUnsupervisedDose) {
					isClosing = false;
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources()
									.getString(
											R.string.patientCannotBeGivenUnsupervisedDose))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				}
				maxNumOfDose = ((eComplianceApp) this.getApplication()).maxCPUnsupervisedDose;
			}
		}
		if (checkMoreDoses(pTreatmentId)) {
			return;
		}

		// int unsupervisedCount =
		// DoseAdminstrationOperations.getUnsupervisedCount(
		// pTreatmentId, this);
		if (((eComplianceApp) this.getApplication()).visitToday
				.contains(pTreatmentId)) {

			if (unsupervisedCount < maxNumOfDose) {
				Intent Intent = new Intent(HomeActivity.this,
						UnsupervisedDoseActivity.class);
				Intent.putExtra(IntentKeys.key_treatment_id, pTreatmentId);
				startActivity(Intent);
				HomeActivity.this.finish();
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			} else {
				if (UnSupervisedDoseReasonOperations.isUnsupervisedToday(
						pTreatmentId, this)) {
					if (unsupervisedCount % maxNumOfDose == 0) {
						isClosing = false;
						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								getResources()
										.getString(
												R.string.patientCannotBeGivenUnsupervisedDose))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
						return;
					} else {
						Intent Intent = new Intent(HomeActivity.this,
								UnsupervisedDoseActivity.class);
						Intent.putExtra(IntentKeys.key_treatment_id,
								pTreatmentId);
						startActivity(Intent);
						HomeActivity.this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
					}
				} else {
					Intent Intent = new Intent(HomeActivity.this,
							UnsupervisedDoseActivity.class);
					Intent.putExtra(IntentKeys.key_treatment_id, pTreatmentId);
					startActivity(Intent);
					HomeActivity.this.finish();
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
				}
			}
		} else {
			if (UnSupervisedDoseReasonOperations.isUnsupervisedToday(
					pTreatmentId, this)) {
				if (unsupervisedCount % maxNumOfDose == 0) {
					isClosing = false;
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources()
									.getString(
											R.string.patientCannotBeGivenUnsupervisedDose))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				} else {
					Intent Intent = new Intent(HomeActivity.this,
							UnsupervisedDoseActivity.class);
					Intent.putExtra(IntentKeys.key_treatment_id, pTreatmentId);
					startActivity(Intent);
					HomeActivity.this.finish();
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
				}
			} else {
				Intent Intent = new Intent(HomeActivity.this,
						UnsupervisedDoseActivity.class);
				Intent.putExtra(IntentKeys.key_treatment_id, pTreatmentId);
				startActivity(Intent);
				HomeActivity.this.finish();
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			}
		}
	}

	// ----- Start Iris Enrollment and Identification-------------------------
	public void scanEye() {
		mDeviceHandle = new HIRICAMM();
		mCurrentStatus = new IddkCaptureStatus();
		mCaptureResult = new IddkResult();
		mManiaConfig = new IrisDefault();
		mCaptureInfo = new IddkCaptureInfo();
		mMediaData = new MediaData(getApplicationContext());

		new IddkResult();
		IddkConfig iddkConfig = new IddkConfig();
		iddkConfig.setCommStd(IddkCommStd.IDDK_COMM_USB);
		iddkConfig.setEnableLog(false);
		Iddk2000Apis.setSdkConfig(iddkConfig);

		// Get notification instance
		mUsbNotification = UsbNotification.getInstance(this);

		// Register detached event for the IriShield
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.addAction("org.opasha.eCompliance.ecompliance.ACTION_USB_DEVICE_ATTACHED");
		registerReceiver(mUsbReceiver, filter);
	}

	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				// Make a notice to user
				mUsbNotification.cancelNofitications();
				mUsbNotification
						.createNotification("IriShield is disconnected.");

				// Send a message to main thread
				// mHandler.obtain(mHandler, 0, null);
				mHandler.obtainMessage(IRIS_ENROLL).sendToTarget();
				// mHandler.dispatchMessage(msg);
			}
			if (action
					.equals("org.opasha.eCompliance.ecompliance.ACTION_USB_DEVICE_ATTACHED")) {
				mHandler.obtainMessage(IRIS_ENROLL).sendToTarget();
			}
		}
	};

	/*****************************************************************************
	 * Reset all the internal states of the application. This function is called
	 * whenever device connection has been changed.
	 *****************************************************************************/
	private void setInitState() {
		mIsCameraReady = false;
		mIspreviewing = false;
		mCurrentStatus.setValue(IddkCaptureStatus.IDDK_IDLE);
		IrisUtility.sleep(1000);
	}

	/*****************************************************************************
	 * This function is used to scan and open IriShield. In case there are
	 * multiple IriShields attached to the Android system, IriShield at index 0
	 * is opened as default.
	 *****************************************************************************/
	private void openDevice() {
		// Clear any internal states
		IddkResult ret = new IddkResult();
		// imgScan.setImageBitmap(null);
		mIsCameraReady = false;
		mCurrentStatus.setValue(IddkCaptureStatus.IDDK_IDLE);
		mIspreviewing = false;

		// Scan and open IriShield again
		ArrayList<String> deviceDescs = new ArrayList<String>();
		ret = mApis.scanDevices(deviceDescs);
		if (ret.intValue() == IddkResult.IDDK_OK && deviceDescs.size() > 0) {
			// We open the IriShield at index 0 as default
			ret = mApis.openDevice(deviceDescs.get(0), mDeviceHandle);
			if (deviceDescs.size() > 0) {
				startCamera(true);
			}

			else {

				// User chooses another IriShield to start a capturing process.
				// We must release any resources of the current IriShield device
				mApis.closeDevice(mDeviceHandle);

				// Reset any internal states of the application
				setInitState();

				if (ret.intValue() == IddkResult.IDDK_OK
						|| ret.intValue() == IddkResult.IDDK_DEVICE_ALREADY_OPEN) {
					// Check device version
					// Our Android SDK not working well with IriShield device
					// version <= 2.24
					IddkDeviceInfo deviceInfo = new IddkDeviceInfo();
					ret = mApis.getDeviceInfo(mDeviceHandle, deviceInfo);
					if (ret.getValue() == IddkResult.IDDK_OK) {
						deviceInfo.getKernelVersion();
						deviceInfo.getKernelRevision();

					} else {
						// Error occurs here
						handleError(ret);
						return;
					}

					mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
							"Device Connected.").sendToTarget();

					deviceDescs.get(0);
					startCamera(true);
				} else {
					// Device not found or something wrong occurs
					if (ret.getValue() == IddkResult.IDDK_DEVICE_ACCESS_DENIED) {
						mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
								"Device access denied. Scanning device ...")
								.sendToTarget();
					} else {
						mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
								"Open device failed. Scanning device ...")
								.sendToTarget();

					}
				}
			}
		} else {
			// There is no IriShield attached to the Android system
			mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
					"Device not found. Scanning device ...").sendToTarget();
		}
	}

	/*****************************************************************************
	 * This function handles any errors that may occur in the program. Notice to
	 * disable start and stop button to prevent other errors.
	 *****************************************************************************/
	public void handleError(IddkResult error) {
		mIsCameraReady = false;

		// If there is a problem with the connection
		if ((error.getValue() == IddkResult.IDDK_DEVICE_IO_FAILED)
				|| (error.getValue() == IddkResult.IDDK_DEVICE_IO_DATA_INVALID)
				|| (error.getValue() == IddkResult.IDDK_DEVICE_IO_TIMEOUT)) {
			mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
					getResources().getString(R.string.IrisError))
					.sendToTarget();
		} else {
			mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
					IrisUtility.getErrorDesc(error)).sendToTarget();

		}
	}

	/*****************************************************************************
	 * Initialize camera and start a capturing process
	 *****************************************************************************/
	private void startCamera(boolean sound) {
		IddkResult ret = new IddkResult();
		if (!mIsCameraReady) {
			IddkInteger imageWidth = new IddkInteger();
			IddkInteger imageHeight = new IddkInteger();
			ret = mApis.initCamera(mDeviceHandle, imageWidth, imageHeight);
			if (ret.intValue() != IddkResult.IDDK_OK) {
				mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
						"Failed to initialize the camera.").sendToTarget();
				handleError(ret);
				return;
			}

			mIsCameraReady = true;

			mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1, "Camera ready")
					.sendToTarget();
		}
		if (!mIspreviewing) {
			if (sound)
				mMediaData.moveEyeClosePlayer.start();
			mCurrentStatus.setValue(IddkCaptureStatus.IDDK_IDLE);
			// Start a capturing process
			CaptureTask captureTask = new CaptureTask(imgScan);
			captureTask.execute(mApis, mCaptureResult, mCurrentStatus);

			mIspreviewing = true;
		}
	}

	/*****************************************************************************
	 * This asynchronous task is run simultaneously with the main thread to
	 * update the current streaming images to captureView. A capturing process
	 * is also implemented in this class.
	 *****************************************************************************/
	private class CaptureTask extends AsyncTask<Object, Bitmap, Integer> {
		ImageView imgScan = null; // Right eye
		IddkResult iRet;
		boolean isBinocularDevice = false;

		public CaptureTask(View imgScan) {
			this.imgScan = (ImageView) imgScan;

			if (imgScan != null) {
				IddkInteger isBino = new IddkInteger();
				mApis.Iddk_IsBinocular(mDeviceHandle, isBino);
				this.imgScan.getLayoutParams().width = mScreenWidth / 2 - 5;
				this.imgScan.getLayoutParams().height = (this.imgScan
						.getLayoutParams().width / 4) * 3;

			}
		}

		/*****************************************************************************
		 * Capturing process is implemented here. It runs simultaneously with
		 * the main thread and update streaming images to captureView. After the
		 * capturing process ends, we get the best image and save it in a
		 * default directory.
		 *****************************************************************************/
		protected Integer doInBackground(Object... params) {
			ArrayList<IddkImage> monoImages = new ArrayList<IddkImage>();
			IddkCaptureStatus captureStatus = new IddkCaptureStatus(
					IddkCaptureStatus.IDDK_IDLE);

			iRet = (IddkResult) params[1];
			Iddk2000Apis mApis = (Iddk2000Apis) params[0];

			boolean bRun = true;
			boolean eyeDetected = false;
			IddkEyeSubType subType = null;
			subType = new IddkEyeSubType(IddkEyeSubType.IDDK_UNKNOWN_EYE);
			IddkInteger maxEyeSubtypes = new IddkInteger();

			iRet = mApis
					.startCapture(mDeviceHandle, mCaptureInfo.getCaptureMode(),
							mCaptureInfo.getCount(),
							mCaptureInfo.getQualitymode(),
							mCaptureInfo.getCaptureOperationMode(), subType,
							true, null);

			if (iRet.intValue() != IddkResult.IDDK_OK) {
				mCaptureResult = iRet;
				return -1;
			}

			while (bRun) {
				if (mCaptureInfo.isShowStream()) {
					iRet = mApis.getStreamImage(mDeviceHandle, monoImages,
							maxEyeSubtypes, captureStatus);

					if (iRet.intValue() == IddkResult.IDDK_OK) {

						Bitmap streamImage = convertBitmap(monoImages.get(0)
								.getImageData(), monoImages.get(0)
								.getImageWidth(), monoImages.get(0)
								.getImageHeight());
						publishProgress(streamImage);

					} else if (iRet.intValue() == IddkResult.IDDK_SE_NO_FRAME_AVAILABLE) {
						// when GetStreamImage returns
						// IDDK_SE_NO_FRAME_AVAILABLE,
						// it does not always mean that capturing process has
						// been finished or encountered problems.
						// It may be because new stream images are not
						// available.
						// We need to query the current capture status to know
						// what happens.
						iRet = mApis.getCaptureStatus(mDeviceHandle,
								captureStatus);
						mCurrentStatus.setValue(captureStatus.getValue());
					}
				} else {
					iRet = mApis.getCaptureStatus(mDeviceHandle, captureStatus);
					mCurrentStatus.setValue(captureStatus.getValue());
					IrisUtility.sleep(60);
				}

				// If GetStreamImage and GetCaptureStatus cause no error,
				// process the capture status
				if (iRet.intValue() == IddkResult.IDDK_OK) {
					// Eye(s) is(are) detected
					if (captureStatus.intValue() == IddkCaptureStatus.IDDK_CAPTURING) {
						if (!eyeDetected) {
							mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
									"Eye detected !").sendToTarget();
							mMediaData.eyeDetectedPlayer.start();
							eyeDetected = true;
							mCurrentStatus
									.setValue(IddkCaptureStatus.IDDK_CAPTURING);
						}
					} else if (captureStatus.intValue() == IddkCaptureStatus.IDDK_COMPLETE) {
						// Capture has finished
						mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
								"Capture finished !").sendToTarget();
						mMediaData.captureFinishedPlayer.start();
						bRun = false;
						mCurrentStatus
								.setValue(IddkCaptureStatus.IDDK_COMPLETE);
					} else if (captureStatus.intValue() == IddkCaptureStatus.IDDK_ABORT) {
						// Capture has been aborted
						bRun = false;
						mCurrentStatus.setValue(IddkCaptureStatus.IDDK_ABORT);
					}
				} else {
					// Terminate the capture if errors occur
					bRun = false;
				}
			}

			mCaptureResult = iRet;
			if (mCurrentStatus.getValue() == IddkCaptureStatus.IDDK_COMPLETE) {
				// Get the best image
				monoBestImage = new ArrayList<IddkImage>();
				capturedTemplate = new IddkDataBuffer();
				mApis.getResultTemplate(mDeviceHandle, capturedTemplate);
				iRet = mApis.getResultImage(mDeviceHandle, new IddkImageKind(
						IddkImageKind.IDDK_IKIND_K1), new IddkImageFormat(
						IddkImageFormat.IDDK_IFORMAT_MONO_RAW), (byte) 1,
						monoBestImage, maxEyeSubtypes);
				if ((!isBinocularDevice && iRet.intValue() == IddkResult.IDDK_OK)
						|| (isBinocularDevice && (iRet.intValue() == IddkResult.IDDK_OK
								|| iRet.intValue() == IddkResult.IDDK_SE_LEFT_FRAME_UNQUALIFIED || iRet
								.intValue() == IddkResult.IDDK_SE_RIGHT_FRAME_UNQUALIFIED))) {
					// Showing the best image so that user can see it
					Bitmap bestImage = null;

					bestImage = convertBitmap(monoBestImage.get(0)
							.getImageData(), monoBestImage.get(0)
							.getImageWidth(), monoBestImage.get(0)
							.getImageHeight());
					publishProgress(bestImage);

				}
				if (iRet.intValue() == IddkResult.IDDK_SE_NO_QUALIFIED_FRAME) {
					// No qualified images
					iRet.setValue(IddkResult.IDDK_SE_NO_QUALIFIED_FRAME);
					mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
							"No frame qualified !").sendToTarget();
					mMediaData.noEyeQualifiedPlayer.start();
					// startCamera(true);
				}
			}

			return 0;
		}

		/*****************************************************************************
		 * Convert the Grayscale image from the camera to bitmap format that can
		 * be used to show to the users.
		 *****************************************************************************/
		private Bitmap convertBitmap(byte[] rawImage, int imageWidth,
				int imageHeight) {
			byte[] Bits = new byte[rawImage.length * 4]; // That's where the
			// RGBA array goes.

			int j;
			for (j = 0; j < rawImage.length; j++) {
				Bits[j * 4] = (byte) (rawImage[j]);
				Bits[j * 4 + 1] = (byte) (rawImage[j]);
				Bits[j * 4 + 2] = (byte) (rawImage[j]);
				Bits[j * 4 + 3] = -1; // That's the alpha
			}

			// Now put these nice RGBA pixels into a Bitmap object
			mCurrentBitmap = Bitmap.createBitmap(imageWidth, imageHeight,
					Bitmap.Config.ARGB_8888);
			mCurrentBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));

			return mCurrentBitmap;
		}

		/*****************************************************************************
		 * Update the current streaming image to the captureView
		 *****************************************************************************/
		protected void onProgressUpdate(Bitmap... bm) {

			mBitmapFP = bm[0];
			mHandler.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();

		}

		/*****************************************************************************
		 * Post processing after the capturing process ends
		 *****************************************************************************/
		protected void onPostExecute(Integer result) {
			IddkResult stopResult = stopCamera(false);
			if (iRet.getValue() != IddkResult.IDDK_OK
					&& stopResult.getValue() != iRet.getValue()) {
				handleError(iRet);
			} else {
				irisRegCurrentAction = IRIS_IDENTIFY;
				// irisRegCurrentAction = IRIS_IDENTIFY;
				doIrisRegWithQualityCheck(mManiaConfig.th_enroll_totalscore,
						mManiaConfig.th_enroll_usablearea);

			}
		}
	}

	private IddkResult stopCamera(boolean sound) {
		IddkResult iRet = new IddkResult();
		iRet.setValue(IddkResult.IDDK_OK);
		if (mIspreviewing) {

			iRet = mApis.stopCapture(mDeviceHandle);
			if (iRet.getValue() != IddkResult.IDDK_OK) {
				handleError(iRet);
				return iRet;
			}

			mIspreviewing = false;
		}

		if (mIsCameraReady) {
			iRet = mApis.deinitCamera(mDeviceHandle);
			if (iRet.getValue() != IddkResult.IDDK_OK) {
				handleError(iRet);
				return iRet;
			}

			mIsCameraReady = false;
		}
		return iRet;
	}

	private void doIrisRegWithQualityCheck(int[] qtotal, int[] qusable) {
		IddkInteger isBino = new IddkInteger();
		mApis.Iddk_IsBinocular(mDeviceHandle, isBino);
		ArrayList<IddkIrisQuality> qualities = new ArrayList<IddkIrisQuality>();
		IddkInteger maxEyeSubtypes = new IddkInteger();
		IddkResult ret = new IddkResult();
		ret = mApis.getResultQuality(mDeviceHandle, qualities, maxEyeSubtypes);
		if ((ret.intValue() != IddkResult.IDDK_OK
				&& ret.intValue() != IddkResult.IDDK_SE_LEFT_FRAME_UNQUALIFIED && ret
				.intValue() != IddkResult.IDDK_SE_RIGHT_FRAME_UNQUALIFIED)
				|| qualities.size() <= 0) {
			handleError(ret);
			return;
		} else {
			mTotalScore = (int) qualities.get(0).getTotalScore();
			mUsableArea = (int) qualities.get(0).getUsableArea();

			if (mTotalScore > minIrisQuality && mUsableArea > minIrisQuality) {
			} else if (mTotalScore <= minIrisQuality
					|| mUsableArea <= minIrisQuality) {
				if (irisRegCurrentAction == IRIS_ENROLL) {

					mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
							STR_REJECT_ENROLLMENT).sendToTarget();

					return;

				} else if (irisRegCurrentAction == IRIS_IDENTIFY) {
					mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
							STR_REJECT_IDENTIFICATION).sendToTarget();

				}

			} else if (IRIS_ENROLL == irisRegCurrentAction) {
				mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
						STR_WARNING_QUALITY_ENROLLMENT).sendToTarget();
				// startCamera(true);
				return;
			}

		}

		if (irisRegCurrentAction == IRIS_IDENTIFY) {
			StringBuffer enrollId = new StringBuffer();
			eIdentifyResult identifyResult = identify(enrollId);
			if (eIdentifyResult.IRI_IDENTIFY_SAME == identifyResult
					|| eIdentifyResult.IRI_IDENTIFY_DUPLICATED == identifyResult) {
				mHandler.obtainMessage(MESSAGE_IDENTIFICATION, -1, -1,
						enrollId.toString()).sendToTarget();
			} else if (eIdentifyResult.IRI_IDENTIFY_DIFFERENT == identifyResult) {

				mHandler.obtainMessage(
						MESSAGE_SHOW_IDENTIFICATION_NOT_FOUND,
						-1,
						-1,
						getResources().getString(
								R.string.identificationCompleteNotFound))
						.sendToTarget();
			}

		}
	}

	/*****************************************************************************
	 * Identify whether the specified ID's iris is enrolled before
	 *****************************************************************************/
	private eIdentifyResult identify(StringBuffer enrollId) {
		eIdentifyResult identifyResult = eIdentifyResult.IRI_IDENTIFY_DIFFERENT;
		IddkResult ret = new IddkResult();
		String result = "";
		ArrayList<IddkComparisonResult> comparisonResults = new ArrayList<IddkComparisonResult>();

		if (!IrisLoaded) {
			SetupTemplates();
			IrisLoaded = true;
		}

		ArrayList<String> Ids = new ArrayList<String>();
		// un-enroll all templates
		mApis.loadGallery(mDeviceHandle, Ids, null, new IddkInteger(),
				new IddkInteger());

		ret = mApis.compare1N(mDeviceHandle, 2.0f, comparisonResults);
		if (ret.getValue() == IddkResult.IDDK_OK
				&& comparisonResults.size() > 0) {
			int i = 0;
			float mindis = 4.0f;
			for (i = 0; i < comparisonResults.size(); i++) {
				if (ret.intValue() == IddkResult.IDDK_OK) {
					if (mindis > comparisonResults.get(i).getDistance()) {
						mindis = comparisonResults.get(i).getDistance();
						result = comparisonResults.get(i).getEnrollId();
					}
				}
			}
			if (mindis < mManiaConfig.th_dedup_distance) {
				identifyResult = eIdentifyResult.IRI_IDENTIFY_DUPLICATED;
			} else if (mindis <= mManiaConfig.th_matching_distance[0]) {
				identifyResult = eIdentifyResult.IRI_IDENTIFY_SAME;
			} else if (mindis <= mManiaConfig.th_matching_distance[1]) {
				identifyResult = eIdentifyResult.IRI_IDENTIFY_LOOKLIKE;
			}
			if (identifyResult != eIdentifyResult.IRI_IDENTIFY_DIFFERENT)
				enrollId.append(result + "_" + mindis);
		}

		if (ret.getValue() == IddkResult.IDDK_OK) {
		}

		return identifyResult;
	}

	// -----------------------------
	// Finger Print Related
	// -----------------------------
	private void scanFinger() {
		if (isClosing)
			return;

		isScanOn = true;
		if (usb_host_ctx.OpenDevice(0, true)) {
			try {
				if (!usb_host_ctx.ValidateContext()) {
					throw new Exception("Can't open USB device");
				}
				m_Operation = new FutronicIdentification((Object) usb_host_ctx);
				// Set control properties

				m_Operation.setFakeDetection(false);
				m_Operation.setFFDControl(true);
				m_Operation.setFARN(((eComplianceApp) this
						.getApplicationContext()).farValue);

				m_Operation.setVersion(VersionCompatible.ftr_version_current);
				((FutronicIdentification) m_Operation).GetBaseTemplate(this);

			} catch (Exception e) {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblText.setText(getResources().getString(
						R.string.cannotStartEnrollmentErrorDescription)
						+ e.getMessage());
			}
		} else {
			if (!usb_host_ctx.IsPendingOpen()) {
				lblText.setText(getResources().getString(
						R.string.canNotStartIdentificationOperation)
						+ "\n"
						+ getResources().getString(
								R.string.canNotOpenScannerDevice));
			}
			try {
				isClosing = true;
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}
		}
		isScanOn = false;
	}

	public void OnPutOn(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1,
				getResources().getString(R.string.putFingeronDevice) + "...")
				.sendToTarget();
	}

	@Override
	public void OnTakeOff(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1,
				getResources().getString(R.string.takeFingerOff) + "...")
				.sendToTarget();
	}

	@Override
	public void UpdateScreenImage(Bitmap Image) {
		int[] allpixels = new int[Image.getHeight() * Image.getWidth()];
		Image.getPixels(allpixels, 0, Image.getWidth(), 0, 0, Image.getWidth(),
				Image.getHeight());
		for (int i = 0; i < Image.getHeight() * Image.getWidth(); i++) {
			if (allpixels[i] == Color.BLACK)
				allpixels[i] = Color.WHITE;
			else
				allpixels[i] = Color.BLACK;
		}
		Image.setPixels(allpixels, 0, Image.getWidth(), 0, 0, Image.getWidth(),
				Image.getHeight());
		mBitmapFP = Image;
		mHandler.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();
	}

	@Override
	public boolean OnFakeSource(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "Fake source detected")
				.sendToTarget();
		return false;
	}

	@Override
	public void OnGetBaseTemplateComplete(boolean bSuccess, int nResult) {
		mHandler.obtainMessage(MESSAGE_UNSUPERVISED_GONE).sendToTarget();
		try {
			if (isClosing)
				return;
			if (bSuccess) {
				if (scans.size() > 0) {
					IdentificationResultCustom idenResult = GenUtils.Identify(
							m_Operation, scans, nResult);
					if (idenResult.nResult == FutronicSdkBase.RETCODE_OK) {

						if (idenResult.foundIndex != -1) {
							// mHandler.obtainMessage(
							// MESSAGE_IDENTIFICATION,
							// -1,
							// -1,
							// scans.get(idenResult.foundIndex).treatmentId)
							// .sendToTarget();
							mHandler.obtainMessage(MESSAGE_IDENTIFICATION, -1,
									-1, idenResult).sendToTarget();

						} else {
							mHandler.obtainMessage(
									MESSAGE_SHOW_IDENTIFICATION_NOT_FOUND,
									-1,
									-1,
									getResources()
											.getString(
													R.string.identificationCompleteNotFound))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();

						}
						m_Operation = null;
						if (!isClosing)
							scanFinger();
					} else {
						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								getResources().getString(
										R.string.fingerprintReaderError))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
						m_Operation = null;
						isError = true;
					}

				} else {
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources()
									.getString(
											R.string.identificationErrorDatabaseIsEmpty))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					m_Operation = null;
					isError = true;
				}
			} else {
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						getResources().getString(
								R.string.fingerprintReaderError))
						.sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				m_Operation = null;
				isError = true;
			}
		} catch (Exception e) {
			Log.e("OnGetBaseTemplateComplete",
					"Error: " + e.getMessage() + e.toString());
			isError = true;
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isClosing)
				return;
			switch (msg.what) {
			case IRIS_ENROLL:
				setInitState();
				openDevice();
				break;
			case IRIS_HANDLE_ERROR:
				String msgs = (String) msg.obj;
				lblText.setText(msgs);
				break;
			case MESSAGE_SHOW_MSG:
				String showMsg = (String) msg.obj;
				lblText.setText(showMsg);
				break;
			case MESSAGE_SHOW_IMAGE:
				imgScan.setImageBitmap(mBitmapFP);
				break;
			case MESSAGE_COMPLETE:
				Intent intent = new Intent(HomeActivity.this,
						HomeActivity.class);
				intent.putExtra("message", "Verified");
				startActivity(intent);
				HomeActivity.this.finish();
				break;
			case MESSAGE_SHOW_IDENTIFICATION_NOT_FOUND:
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_used_device, HomeActivity.this)
						.equals("iris")) {
					if (btncameraOn_Off.isChecked()) {
						btncameraOn_Off.setChecked(false);
					}
					Scan_Identification_Operations.addScanIdentification(
							CentersOperations.getTabId(HomeActivity.this),
							"Iris", "", false, System.currentTimeMillis(),
							HomeActivity.this);
				} else {
					Scan_Identification_Operations.addScanIdentification(
							CentersOperations.getTabId(HomeActivity.this),
							"Fingerprint", "", false,
							System.currentTimeMillis(), HomeActivity.this);
				}

				String showheader = (String) msg.obj;
				lblHeader.setText(showheader);
				if (showheader.equals(getResources().getString(
						R.string.counselorLoginRequired))) {
					btnUnsupervisedDose.setVisibility(View.GONE);
					btnFBSResult.setVisibility(View.GONE);
					btnHBA1cResult.setVisibility(View.GONE);
				}
				lblText.setText("");
				break;
			case MESSAGE_SHOW_HEADER:
				showheader = (String) msg.obj;
				lblHeader.setText(showheader);
				if (showheader == null)
					showheader = "";
				if (showheader.equals(getResources().getString(
						R.string.counselorLoginRequired))) {
					btnUnsupervisedDose.setVisibility(View.GONE);
					btnFBSResult.setVisibility(View.GONE);
					btnHBA1cResult.setVisibility(View.GONE);
				}
				lblText.setText("");
				break;
			case MESSAGE_SHOW_GREEN:
				getGreenAnimation();
				break;
			case MESSAGE_SHOW_RED:
				if (!isClosing) {
					getRedAnimation();
				}
				break;
			case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
				if (!isClosing) {
					getRedAnimation();
					lblText.setText(getResources().getString(
							R.string.userDenyScannerDevice));
				}
				break;
			case MESSAGE_IDENTIFICATION:
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_used_device, HomeActivity.this)
						.equals("iris")) {
					if (btncameraOn_Off.isChecked()) {
						btncameraOn_Off.setChecked(false);
					}

					String value = (String) msg.obj;
					String treatmentId = value.split("_")[0];
					String eye = treatmentId.substring(0, 1);
					treatmentId = treatmentId.substring(1);
					String result = value.split("_")[1];
					HandleScan(treatmentId);
					Scan_Identification_Operations.addScanIdentification(
							CentersOperations.getTabId(HomeActivity.this),
							"Iris. Eye: " + eye + ", Score: " + result,
							treatmentId, true, System.currentTimeMillis(),
							HomeActivity.this);
				} else {
					IdentificationResultCustom irc = (IdentificationResultCustom) msg.obj;

					HandleScanWithEnrolment(irc);
					Scan_Identification_Operations.addScanIdentification(
							CentersOperations.getTabId(HomeActivity.this),
							"Fingerprint",
							scans.get(irc.foundIndex).treatmentId, true,
							System.currentTimeMillis(), HomeActivity.this);
				}
				break;
			case MESSAGE_UNSUPERVISED_GONE:
				btnUnsupervisedDose.setVisibility(View.GONE);
				btnFBSResult.setVisibility(View.GONE);
				btnHBA1cResult.setVisibility(View.GONE);
				break;
			case MESSAGE_SHOW_YELLOW:
				if (!isClosing) {
					getYellowAnimation();
				}
			}
		}

	};

	private void SetupTemplates() {
		ArrayList<String> Ids = new ArrayList<String>();
		IddkResult iret = new IddkResult();
		// un-enroll all templates
		mApis.loadGallery(mDeviceHandle, Ids, null, new IddkInteger(),
				new IddkInteger());
		// if (iret.getValue() == IddkResult.IDDK_GAL_NOT_ENOUGH_SLOT) {
		// iret = mApis.unenrollTemplate(mDeviceHandle, null);
		// mApis.commitGallery(mDeviceHandle);
		// }
		for (String id : Ids) {
			mApis.unenrollTemplate(mDeviceHandle, id);
		}
		mApis.commitGallery(mDeviceHandle);

		for (ScanModel scan : ScansIrisOperations.getScans(true,
				HomeActivity.this)) {
			IddkDataBuffer df = new IddkDataBuffer();
			df.setData(scan.scan);
			if (scan.eye.equals("Left Eye")) {
				eye = "L";
			} else {
				eye = "R";
			}
			mApis.enrollTemplate(mDeviceHandle, eye + scan.treatmentId, df);
		}
		mApis.commitGallery(mDeviceHandle);
	}

	// ----------------------------
	// UI and Animation
	// ----------------------------

	private void getRedAnimation() {
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) layout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	private void getYellowAnimation() {
		layout.setBackgroundResource(R.drawable.grey_to_yellow_transition);
		TransitionDrawable transition1 = (TransitionDrawable) layout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	private void getGreenAnimation() {
		layout.setBackgroundResource(R.drawable.grey_to_green_transition);
		TransitionDrawable transition1 = (TransitionDrawable) layout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	private void setDashboard() {

		// int totalActivePatient =
		// PatientsOperations.getActivePatientCount(this);
		int pendingToday = ((eComplianceApp) this.getApplication()).pendingDoses
				.size();
		if (pendingToday > 0) {
			if (((eComplianceApp) this.getApplication()).visitToday.size() == 0
					&& ((eComplianceApp) this.getApplication()).missedDose
							.size() == 0
					&& ((eComplianceApp) this.getApplication()).pendingDoses
							.size() == 0) {
				if (AppStateConfigurationOperations.getKeyValue(
						AppStateConfigurationKeys.key_patients_contains_value,
						this).equals("true")) {
					visitCount.setText(String.valueOf(((eComplianceApp) this
							.getApplication()).visitToday.size()));
					missedCount.setText(String.valueOf(((eComplianceApp) this
							.getApplication()).missedDose.size()));
					pendingCount.setText(String.valueOf(((eComplianceApp) this
							.getApplication()).pendingDoses.size()));
					AlertDialog.Builder builder = new AlertDialog.Builder(this);

					builder.setCancelable(true).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

								}
							});

					AlertDialog alert = builder.create();
					alert.setTitle("There is some error please Sync the App again");
					alert.show();

				} else {
					visitCount.setText(String.valueOf(((eComplianceApp) this
							.getApplication()).visitToday.size()));
					missedCount.setText(String.valueOf(((eComplianceApp) this
							.getApplication()).missedDose.size()));
					pendingCount.setText(String.valueOf(((eComplianceApp) this
							.getApplication()).pendingDoses.size()));
				}

			} else {
				visitCount.setText(String.valueOf(((eComplianceApp) this
						.getApplication()).visitToday.size()));
				missedCount.setText(String.valueOf(((eComplianceApp) this
						.getApplication()).missedDose.size()));
				pendingCount.setText(String.valueOf(((eComplianceApp) this
						.getApplication()).pendingDoses.size()));
			}
		} else {

			visitCount.setText(String.valueOf(((eComplianceApp) this
					.getApplication()).visitToday.size()));
			missedCount.setText(String.valueOf(((eComplianceApp) this
					.getApplication()).missedDose.size()));
			pendingCount.setText(String.valueOf(((eComplianceApp) this
					.getApplication()).pendingDoses.size()));
		}
	}

	@Override
	public void onBackPressed() {
		isClosing = true;
		isInitialCounselingPopup = false;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
		this.finish();
	}

	private void HandleScanWithEnrolment(IdentificationResultCustom irc) {

		String lastLoginId = ((eComplianceApp) this.getApplication()).LastLoginId;
		String treatmentId = scans.get(irc.foundIndex).treatmentId;
		if (PatientsOperations.patientExists(treatmentId, this)) {
			// Add New Scan
			long lastScanDate = ScansOperations.getLastScanDate(treatmentId,
					this);
			long diff = System.currentTimeMillis() - lastScanDate;
			int diffDays = (int) (diff / (86400000));
			if (diffDays >= 10) {
				ScansOperations.addScan(treatmentId,
						VisitorType.Patient.toString(), irc.scanTemplate,
						System.currentTimeMillis(), lastLoginId, this);
			}

		}
		HandleScan(treatmentId);
	}

	private void HandleScan(String treatmentId) {
		if (isInitialCounselingPopup)
			return;

		boolean isActive = false;
		if (!isSundayOn()) {
			return;
		}
		GpsTracker gps = new GpsTracker(this);
		String lastLoginId = ((eComplianceApp) this.getApplication()).LastLoginId;
		if (PatientsOperations.patientExists(treatmentId, this)) {
			Patient patient = PatientsOperations.getPatientDetails(treatmentId,
					this);

			if (patient.isCounsellingPending) {
				((eComplianceApp) this.getApplication())
						.isAllVideosDownloaded();
				if (!((eComplianceApp) this.getApplication()).IsAllVideosDownloaded) {
					mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
							"All videos are not available for counseling. Please download the videos.")
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "")
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				}

				if (!DoInitialCounselling(treatmentId, patient, lastLoginId)) {
					isInitialCounselingPopup = true;
					showInitialCounsellingMessage(patient.name,
							patient.treatmentID);
					return;
				} else {
					if (ConfigurationOperations.getKeyValue(
							ConfigurationKeys.key_is_video_counseling_enabled,
							HomeActivity.this).equals("1")) {
						int maxVideoId = 0;
						Master r = TreatmentInStagesOperations
								.getPatientRegimen(patient.treatmentID,
										HomeActivity.this);

						String cat = r.catagory;
						String stage = r.stage;

						if (PatientsOperations.isInitialCounsellingPending(
								treatmentId, this) || stage.equals("CP")) {
							stage = "IP";// If Initial Counseling Pending,
											// consider this as
											// IP Patient as his counseling is
											// pending.
						}

						String isDaily = "0";
						if (r.daysFrequency == 1) {
							isDaily = "1";
						}

						ArrayList<String> videos = VideosCategoryOperations
								.GetVideoList(cat, stage, isDaily,
										HomeActivity.this);
						maxVideoId = ECounselingOperations.maxVideoId(
								patient.treatmentID, stage, isDaily,
								HomeActivity.this);
						if (videos.size() != 0 && videos.size() != maxVideoId) {
							if (DoInitialCounselling(treatmentId, patient,
									lastLoginId)) {
								ECounselingOperations.Delete(
										patient.treatmentID, stage, isDaily,
										HomeActivity.this);
								PatientsOperations.UpdateInitialCounseling(
										patient.treatmentID, HomeActivity.this);
								InitialCounselingOperations.Delete(
										patient.treatmentID, HomeActivity.this);
								InitialCounselingOperations.Add(
										patient.treatmentID, HomeActivity.this);
								isInitialCounselingPopup = true;
								showInitialCounsellingMessage(patient.name,
										patient.treatmentID);
								return;
							}
						}
					}
				}
			}

			// check remaining doses of patient
			if (checkMoreDoses(treatmentId)) {
				return;
			}

			showDiabetesButton(treatmentId);
			patient = PatientsOperations.getPatientDetails(treatmentId, this);

			if (((eComplianceApp) this.getApplication()).visitToday
					.contains(treatmentId)) {
				generateCpSelfAdminDoses(treatmentId, gps, lastLoginId);
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						getResources().getString(R.string.Patient)
								+ " "
								+ patient.name
								+ "("
								+ patient.treatmentID
								+ ") "
								+ getResources().getString(
										R.string.alreadyVisitedToday))
						.sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				if (isUnsupervisedEnable)
					btnUnsupervisedDose.setVisibility(View.VISIBLE);
				pTreatmentId = treatmentId;
				return;
			} else {
				if (((eComplianceApp) this.getApplication()).pendingDoses
						.contains(treatmentId)) {

					if (!markInitialCounselingComplete(lastLoginId, patient)) {
						if (repeatCounsling(treatmentId)) {
							return;
						}

					}

					DoseUtils.AddDose(treatmentId, Enums.DoseType.Supervised
							.toString(), GenUtils.getCurrentDateLong(),
							TreatmentInStagesOperations.getPatientRegimenId(
									treatmentId, this), System
									.currentTimeMillis(), lastLoginId, gps
									.getLatitude(), gps.getLongitude(), this);
					generateSelfAdminDoseforDailyRegimen(treatmentId,
							lastLoginId, gps);
					generateCpSelfAdminDoses(treatmentId, gps, lastLoginId);
					FutureMissedDoseOperations.doseSoftDeleteForPatient(
							treatmentId, this); // Delete all future missed
					// doses

					// isScheduleChange(treatmentId);
					((eComplianceApp) this.getApplication()).pendingDoses
							.remove(treatmentId);
					((eComplianceApp) this.getApplication()).visitToday
							.add(treatmentId);
					setDashboard();
					GenerateAlert(patient.treatmentID);
					mHandler.obtainMessage(
							MESSAGE_SHOW_HEADER,
							-1,
							-1,
							getResources().getString(R.string.Patient)
									+ " "
									+ patient.name
									+ "("
									+ patient.treatmentID
									+ ") "
									+ getResources().getString(
											R.string.loggedIn)).sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
					if (isUnsupervisedEnable)
						btnUnsupervisedDose.setVisibility(View.VISIBLE);
					pTreatmentId = treatmentId;
					return;
				} else {
					if (((eComplianceApp) this.getApplication()).missedDose
							.contains(treatmentId)) {
						if (!markInitialCounselingComplete(lastLoginId, patient)) {
							if (repeatCounsling(treatmentId)) {

								return;
							}

						}
						int regimenId = TreatmentInStagesOperations
								.getPatientRegimenId(treatmentId, this);
						Master regimen = RegimenMasterOperations.getRegimen(
								regimenId, this);
						long doseDate = GenUtils.getCurrentDateLong();
						long preDoseDate = (GenUtils.dateTimeToDate(doseDate))
								- GenUtils.ONE_DAY;
						if (!regimen.missedSunday) {
							if (GenUtils.dateToDay(doseDate) == Calendar.MONDAY) {
								preDoseDate -= GenUtils.ONE_DAY;
							}
						}
						String query = Schema.DOSE_ADMINISTRATION_TREATMENT_ID
								+ "= '" + treatmentId + "' and "
								+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " >= "
								+ preDoseDate + " and "
								+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " < "
								+ doseDate + " and "
								+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE + " ='"
								+ DoseType.Missed.toString() + "'";

						if (regimenId < 22) {
							if (DoseAdminstrationOperations.getdoses(query,
									this).isEmpty()) {
								// *AS - Asked by Sandeep Sir and Ashvini to
								// make this
								// change
								// Change Schedule to today's. This is only done
								// for CP.
								// We are checking if patient is taking a dose
								// after 1
								// business day of his current regimen. If he
								// does, we
								// change the schedule to current day

								if (regimen.stage.equals("CP")) {
									String newSchedule = GenUtils
											.getCurrentDay();
									int regId = RegimenMasterOperations
											.getRegimenId(
													Schema.REGIMEN_MASTER_CATEGORY
															+ "= '"
															+ regimen.catagory
															+ "' and "
															+ Schema.REGIMEN_MASTER_STAGE
															+ "='"
															+ regimen.stage
															+ "' and "
															+ Schema.REGIMEN_MASTER_SCHEDULE
															+ "= '"
															+ newSchedule + "'",
													this);
									TreatmentInStagesOperations
											.addTreatmentStage(
													treatmentId,
													regId,
													GenUtils.getCurrentDateLong(),
													System.currentTimeMillis(),
													lastLoginId, false, this);
									regimenId = regId;
								}
							}
						}
						DoseUtils.AddDose(treatmentId,
								Enums.DoseType.Supervised.toString(), doseDate,
								regimenId, System.currentTimeMillis(),
								lastLoginId, gps.getLatitude(),
								gps.getLongitude(), this);
						generateSelfAdminDoseforDailyRegimen(treatmentId,
								lastLoginId, gps);
						generateCpSelfAdminDoses(treatmentId, gps, lastLoginId);
						FutureMissedDoseOperations.doseSoftDeleteForPatient(
								treatmentId, this); // Delete all future missed
						// doses

						// isScheduleChange(treatmentId);
						((eComplianceApp) this.getApplication()).missedDose
								.remove(treatmentId);
						((eComplianceApp) this.getApplication()).visitToday
								.add(treatmentId);
						setDashboard();
						GenerateAlert(patient.treatmentID);
						// GenerateSputumAlert();

						// GenerateBloodandXrayAlert(patient.treatmentID);

						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								getResources().getString(R.string.Patient)
										+ " "
										+ patient.name
										+ "("
										+ patient.treatmentID
										+ ") "
										+ getResources().getString(
												R.string.loggedIn))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();
						if (isUnsupervisedEnable)
							btnUnsupervisedDose.setVisibility(View.VISIBLE);
						pTreatmentId = treatmentId;
						return;
					} else {

						String status = patient.Status;

						if (status.equals(StatusType
								.getStatusType(StatusType.Active))) {
							if (DoseAdminstrationOperations.getLastDose(
									patient.treatmentID, this).doseDate == 0) {
								if (!markInitialCounselingComplete(lastLoginId,
										patient)) {
									if (repeatCounsling(treatmentId)) {

										return;
									}

								}
								DoseUtils.AddDose(treatmentId,
										Enums.DoseType.Supervised.toString(),
										GenUtils.getCurrentDateLong(),
										TreatmentInStagesOperations
												.getPatientRegimenId(
														treatmentId, this),
										System.currentTimeMillis(),
										lastLoginId, gps.getLatitude(), gps
												.getLongitude(), this);
								generateSelfAdminDoseforDailyRegimen(
										treatmentId, lastLoginId, gps);
								generateCpSelfAdminDoses(treatmentId, gps,
										lastLoginId);
								FutureMissedDoseOperations
										.doseSoftDeleteForPatient(treatmentId,
												this); // Delete all future
								// missed doses

								// isScheduleChange(treatmentId);
								((eComplianceApp) this.getApplication()).visitToday
										.add(treatmentId);
								setDashboard();

								GenerateAlert(patient.treatmentID);
								// GenerateSputumAlert();

								// GenerateBloodandXrayAlert(patient.treatmentID);

								mHandler.obtainMessage(
										MESSAGE_SHOW_HEADER,
										-1,
										-1,
										getResources().getString(
												R.string.Patient)
												+ " "
												+ patient.name
												+ "("
												+ patient.treatmentID
												+ ") "
												+ getResources().getString(
														R.string.loggedIn))
										.sendToTarget();
								mHandler.obtainMessage(MESSAGE_SHOW_GREEN)
										.sendToTarget();
								if (isUnsupervisedEnable)
									btnUnsupervisedDose
											.setVisibility(View.VISIBLE);
								pTreatmentId = treatmentId;
								return;
							} else {
								mHandler.obtainMessage(
										MESSAGE_SHOW_HEADER,
										-1,
										-1,
										getResources().getString(
												R.string.Patient)
												+ " "
												+ patient.name
												+ "("
												+ patient.treatmentID
												+ ") "
												+ getResources()
														.getString(
																R.string.hasAlreadyTakenDose))
										.sendToTarget();
								mHandler.obtainMessage(MESSAGE_SHOW_RED)
										.sendToTarget();
								if (isUnsupervisedEnable)
									btnUnsupervisedDose
											.setVisibility(View.VISIBLE);
								pTreatmentId = treatmentId;
								return;
							}
						} else {
							btnFBSResult.setVisibility(View.GONE);
							btnHBA1cResult.setVisibility(View.GONE);
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources()
													.getString(
															R.string.isInactiveCurrentStatus)
											+ status).sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return;
						}
					}
				}
			}
		} else {
			if (VisitorsOperations.visitorExists(treatmentId, this)) {
				ArrayList<Visitor> visitors = VisitorsOperations.getVisitor(
						Schema.VISITORS_ID + "='" + treatmentId + "'", this);
				for (Visitor visitor : visitors) {
					if (!visitor.isDeleted) {
						VisitorType visitorType = VisitorType
								.getVisitorType(visitor.visitorType);

						if (VisitorType.GetViewString(visitorType).equals(
								VisitorType.GetViewString(VisitorType.PM))
								|| VisitorType
										.GetViewString(visitorType)
										.equals(VisitorType
												.GetViewString(VisitorType.Counselor))
								|| VisitorType
										.GetViewString(visitorType)
										.equals(VisitorType
												.GetViewString(VisitorType.CDP))) {
							if (ConfigurationOperations
									.getKeyValue(
											ConfigurationKeys.key_check_admin_authentication,
											this).toLowerCase().equals("false")) {
								visitor.isAuthenticated = true;
							}

							if (!visitor.isAuthenticated) {
								mHandler.obtainMessage(
										MESSAGE_SHOW_HEADER,
										-1,
										-1,
										VisitorType.GetViewString(visitorType)
												+ " "
												+ visitor.name
												+ "("
												+ visitor.visitorID
												+ ") "
												+ getResources().getString(
														R.string.adminNotAuth))
										.sendToTarget();
								mHandler.obtainMessage(MESSAGE_SHOW_RED)
										.sendToTarget();
								return;
							}

						}

						VisitorLoginOperations.addVisitorLogin(treatmentId,
								System.currentTimeMillis(),
								CentersOperations.getTabId(this),
								gps.getLatitude(), gps.getLongitude(), this);
						isActive = true;

						if (visitorType == VisitorType.CDP
								|| visitorType == VisitorType.PM
								|| visitorType == VisitorType.Counselor) {
							((eComplianceApp) this.getApplication()).IsAdminLoggedIn = true;
							((eComplianceApp) this.getApplication()).LoginTime = System
									.currentTimeMillis();
							((eComplianceApp) this.getApplication()).LastLoginId = visitor.visitorID;
							((eComplianceApp) this.getApplication())
									.SignOffWithTimer();
							if (visitorType == VisitorType.PM) {
								((eComplianceApp) this.getApplication()).visitorType = VisitorType.PM;
							} else {
								((eComplianceApp) this.getApplication()).visitorType = VisitorType.Other;
							}
							showLoggedInImage();
						}
						if (visitorType == VisitorType.QualityAuditor) {
							doQualityAudit(visitor.visitorID);
						}

						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								VisitorType.GetViewString(visitorType)
										+ " "
										+ visitor.name
										+ "("
										+ visitor.visitorID
										+ ") "
										+ getResources().getString(
												R.string.loggedIn))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();

						break;

					}
				}
				if (!isActive) {
					mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
							getResources().getString(R.string.visitorInactive))
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					return;
				}
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						"Visitor Data Missing!").sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				return;
			}
		}

	}

	/**
	 * Show diabetes button after patient login
	 */

	private void showDiabetesButton(String treatmentId) {
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_GlucometerTest_enabled, this).equals(
				"1")) {
			if (FBSBtnShow(treatmentId)) {
				btnFBSResult.setVisibility(View.VISIBLE);
				btnFBSResult.setTag(treatmentId.toString());
			}
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_HBA1c_enable, this).equals("1")) {
				if (HBA1cBtnShow(treatmentId)) {
					btnHBA1cResult.setVisibility(View.VISIBLE);
					btnHBA1cResult.setTag(treatmentId.toString());
				}
			}
		}

	}

	private void doQualityAudit(final String qaId) {
		if (ConfigurationOperations
				.getKeyValue(ConfigurationKeys.key_quality_auditing_enable,
						this).trim().equals("1")) {
			final Intent intent = new Intent(this, AuditQuestionsActivity.class);
			if (QualityAuditingOperations.isAnyPendingAudit(qaId, this)) {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				// set the custom dialog components - text, image and button
				Button noButton = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				Button yesButton = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
				TextView message = (TextView) dialog
						.findViewById(R.id.messageText);
				TextView title = (TextView) dialog.findViewById(R.id.text);
				message.setText(getResources().getString(
						R.string.doYouWantToFinalTheAudit));
				title.setText(getResources().getString(R.string.confirmation));
				yesButton.setText(getResources().getString(R.string.finalize));
				noButton.setText(getResources().getString(R.string.Edit));
				yesButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						QualityAuditingOperations.QuestionsFinalize(qaId,
								HomeActivity.this);
						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								getResources().getString(
										R.string.qauaityAuditComplete))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();
						dialog.dismiss();

					}
				});
				// if button is clicked, close the custom dialog
				noButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						loadQaQuestons(qaId);
						dialog.dismiss();

					}
				});
				dialog.show();
			} else {
				intent.putExtra(IntentKeys.key_treatment_id, qaId);
				startActivity(intent);
				HomeActivity.this.finish();
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			}
		}
	}

	private boolean HBA1cBtnShow(String treatmentId) {
		if (!PatientDiabetesOperations.isFirstDiabetes(treatmentId, this)) {
			return false;
		}
		long lastresult = PatientDiabetesOperations.lastResultDate(
				Schema.PATIENT_DIABETES_ID + "='" + treatmentId + "' and "
						+ Schema.PATIENT_DIABETES_TEST_ID + "= "
						+ Enums.TestIds.getId(TestIds.HBA1cTest), this);
		long frequency = 0;
		try {
			frequency = Long.parseLong(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_HBA1c_frequency, this));
		} catch (Exception e) {
		}
		if (lastresult != 0 && frequency != 0) {
			if ((lastresult + (GenUtils.ONE_DAY * frequency)) > GenUtils
					.getCurrentDateLong()) {
				return false;
			}
		}
		return true;
	}

	private boolean FBSBtnShow(String treatmentId) {
		long lastresult = PatientDiabetesOperations.lastResultDate(
				Schema.PATIENT_DIABETES_ID + "='" + treatmentId + "' and "
						+ Schema.PATIENT_DIABETES_TEST_ID + "= "
						+ Enums.TestIds.getId(TestIds.glucometerTest), this);
		long frequency = 0;
		try {
			frequency = Long.parseLong(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_glucometerTest_frequency, this));
		} catch (Exception e) {
		}
		if (lastresult != 0 && frequency != 0) {
			if ((lastresult + (GenUtils.ONE_DAY * frequency)) > GenUtils
					.getCurrentDateLong()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Generate Self Admin Doses for CP Cases
	 * 
	 * @param treatmentId
	 * @param gps
	 * @param lastLoginId
	 */
	public void generateCpSelfAdminDoses(String treatmentId, GpsTracker gps,
			String lastLoginId) {
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentId,
						this), this);
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_enable_user_self_admin, this).equals("1")) {
			if (regimen.numSelfAdmin == 0)
				return;
			ArrayList<Patient> doses = DoseAdminstrationOperations.getdoses(
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "' and "
							+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID + "="
							+ regimen.ID, this);
			if (doses.size() == 1)
				return;
			for (int i = doses.size() - 2; i >= 0; i--) {
				if (doses.get(i).doseType.equals("SelfAdministered"))
					return;
				if (doses.get(i).doseType.equals("Supervised"))
					break;
			}
			long lastSupervisedDoseDate = 0;

			if (doses.get(doses.size() - 2).doseType.equals("Supervised")) {
				lastSupervisedDoseDate = doses.get(doses.size() - 2).doseDate;
			} else {
				for (int j = doses.size() - 2; j > 0; j--) {
					if (doses.get(j).doseType.equals("Supervised")) {
						if (doses.get(j).doseDate > lastSupervisedDoseDate)
							lastSupervisedDoseDate = doses.get(j).doseDate;
					}
				}
			}
			if (lastSupervisedDoseDate == 0)
				return;
			startActivityForResult(
					new Intent(this, SelfAdminActivity.class).putExtra(
							IntentKeys.key_treatment_id, treatmentId).putExtra(
							IntentKeys.key_last_supervised_dose,
							lastSupervisedDoseDate), 2);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);

		} else {
			ArrayList<Patient> doses = DoseAdminstrationOperations.getdoses(
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "' and "
							+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID + "="
							+ regimen.ID, this);
			if (doses.size() == 0)
				return;
			long doseDate = GenUtils.getCurrentDateLong();
			for (int i = 0; i < regimen.numSelfAdmin; i++) {
				doseDate += (regimen.selfAdminFreq * 86400000);

				if (!regimen.selfAdminSunday) {
					if (GenUtils.dateToDay(doseDate) <= 2) {
						doseDate += 86400000;
					}
				}

				if (!regimen.catagory.equals("MDR")) {
					DoseUtils.AddDose(treatmentId,
							Enums.DoseType.SelfAdministered.toString(),
							doseDate, regimen.ID, System.currentTimeMillis(),
							lastLoginId, gps.getLatitude(), gps.getLongitude(),
							this);
				}

			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			long lastSupervisedDoseDate = data.getLongExtra(
					IntentKeys.key_last_supervised_dose, 0);
			String treatmentId = data
					.getStringExtra(IntentKeys.key_treatment_id);
			int selfAdminTaken = data.getIntExtra(
					IntentKeys.key_is_all_self_admin_taken, 0);// 1-All,0-Not
			// taken all
			// doses

			int noSelfAdmin = data.getIntExtra(
					IntentKeys.key_no_of_self_admin_taken, 0);// if
			// selfAdminTaken
			// = 0 then no.
			// of selfAdmin
			// doses taken
			Master regimen = RegimenMasterOperations
					.getRegimen(
							DoseAdminstrationOperations
									.getPatientRegimenidByDate(
											Schema.DOSE_ADMINISTRATION_TREATMENT_ID
													+ "='"
													+ treatmentId
													+ "' and "
													+ Schema.DOSE_ADMINISTRATION_DOSE_DATE
													+ "="
													+ lastSupervisedDoseDate,
											this), this);
			// if (selfAdminTaken == 0) {
			// regimen.numSelfAdmin = noSelfAdmin;
			// }
			long doseDate = lastSupervisedDoseDate;
			for (int i = 0; i < regimen.numSelfAdmin; i++) {
				doseDate += (regimen.selfAdminFreq * 86400000);
				if (!regimen.selfAdminSunday) {
					if (GenUtils.dateToDay(doseDate) <= 2) {
						doseDate += 86400000;
					}
				}
				String doseType = Enums.DoseType.SelfAdministered.toString();
				if (selfAdminTaken == 0)
					if (i > noSelfAdmin - 1)
						doseType = Enums.DoseType.Missed.toString();
				DoseUtils
						.AddDose(
								treatmentId,
								doseType,
								doseDate,
								regimen.ID,
								System.currentTimeMillis(),
								((eComplianceApp) this.getApplicationContext()).LastLoginId,
								new GpsTracker(this).getLatitude(),
								new GpsTracker(this).getLongitude(), this);
			}
		}
	}

	@SuppressLint("NewApi")
	private void GenerateSputumBloodandXrayAlert(boolean sputumAlert,
			boolean bloodAlert, boolean xrayAlert) {
		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) this
				.findViewById(R.id.popupLinearLayout);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.activity_popup,
				viewGroup);
		ImageButton sputum = (ImageButton) layout
				.findViewById(R.id.imageButton1);
		ImageButton blood = (ImageButton) layout
				.findViewById(R.id.imageButton2);
		ImageButton xray = (ImageButton) layout.findViewById(R.id.imageButton3);

		// Get Screen Max Size.
		Point screenMaxPoint = new Point();
		getWindowManager().getDefaultDisplay().getSize(screenMaxPoint);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(this);
		popup.setContentView(layout);
		popup.setWidth(screenMaxPoint.x);
		popup.setHeight(screenMaxPoint.y);
		popup.setFocusable(true);
		if (sputumAlert) {
			sputum.setVisibility(View.VISIBLE);
		}
		if (bloodAlert) {
			blood.setVisibility(View.VISIBLE);
		}
		if (xrayAlert) {
			xray.setVisibility(View.VISIBLE);
		}
		// relative to button's position.

		// Clear the default translucent background
		popup.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.pop_up_button_background));
		popup.getBackground().setAlpha(100);
		popup.setOutsideTouchable(true);
		// Displaying the popup at the specified location, + offsets.
		layout.post(new Runnable() {

			@Override
			public void run() {

				popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 1);

			}
		});

		// Getting a reference to Close button, and close the popup when
		// clicked.
		Button close = (Button) layout.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

	}

	private boolean markInitialCounselingComplete(String lastLoginId,
			Patient patient) {
		if (patient.isCounsellingPending) {
			PatientsOperations.addPatient(patient.treatmentID, patient.name,
					patient.Status, patient.phoneNumber, patient.machineID,
					false, System.currentTimeMillis(), lastLoginId, false,
					patient.RegDate, patient.centerId, patient.address,
					patient.diseaseSite, patient.disease, patient.patientType,
					patient.nikshayId, patient.tbNumber,
					patient.smokingHistory, this);
			return true;
		}
		return false;
	}

	/**
	 * Initial Counselling
	 * 
	 * @param treatmentId
	 * @param patient
	 * @param lastLoginId
	 * @return True if Initial Counselling is Complete. False other wise
	 */
	private boolean DoInitialCounselling(String treatmentId, Patient patient,
			String lastLoginId) {
		boolean returnVal = true;

		if (ConfigurationOperations
				.getKeyValue(ConfigurationKeys.key_initial_counseling, this)
				.toLowerCase().equals("true")) {
			if (InitialCounselingOperations.Exists(treatmentId, this)) {
				String temp = ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_initial_counseling_timeout, this);
				if (!temp.equals("")) {
					long timeout = Long.parseLong(temp);
					if (timeout > 0) {
						long startTime = InitialCounselingOperations
								.CounselingStartTime(treatmentId, this);
						if ((startTime + timeout) > System.currentTimeMillis()) {
							InitialCounselingOperations.Update(treatmentId,
									this);
							PatientsOperations.addPatient(patient.treatmentID,
									patient.name, patient.Status,
									patient.phoneNumber, patient.machineID,
									false, System.currentTimeMillis(),
									lastLoginId, false, patient.RegDate,
									patient.centerId, patient.address,
									patient.diseaseSite, patient.disease,
									patient.patientType, patient.nikshayId,
									patient.tbNumber, patient.smokingHistory,
									this);
						} else {
							InitialCounselingOperations.Delete(treatmentId,
									this);
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									String.format(
											getResources()
													.getString(
															R.string.initialCounsellingTimedOut),
											patient.name + "("

											+ patient.treatmentID + ") "))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							returnVal = false;
						}
					} else {
						InitialCounselingOperations.Update(treatmentId, this);
						PatientsOperations.addPatient(patient.treatmentID,
								patient.name, patient.Status,
								patient.phoneNumber, patient.machineID, false,
								System.currentTimeMillis(), lastLoginId, false,
								patient.RegDate, patient.centerId,
								patient.address, patient.diseaseSite,
								patient.disease, patient.patientType,
								patient.nikshayId, patient.tbNumber,
								patient.smokingHistory, this);
					}
				} else {
					InitialCounselingOperations.Update(treatmentId, this);
					PatientsOperations.addPatient(patient.treatmentID,
							patient.name, patient.Status, patient.phoneNumber,
							patient.machineID, false,
							System.currentTimeMillis(), lastLoginId, false,
							patient.RegDate, patient.centerId, patient.address,
							patient.diseaseSite, patient.disease,
							patient.patientType, patient.nikshayId,
							patient.tbNumber, patient.smokingHistory, this);
				}
			} else {
				InitialCounselingOperations.Add(treatmentId, this);
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						String.format(
								getResources().getString(
										R.string.initialCounsellingStarted),
								patient.name + "(" + patient.treatmentID + ") "))
						.sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();

				returnVal = false;
			}
		}
		return returnVal;
	}

	public void onSignOffClick(View v) {
		((eComplianceApp) this.getApplication()).SignOff();
		showLoggedInImage();
	}

	public void showLoggedInImage() {
		if (((eComplianceApp) this.getApplication()).IsAdminLoggedIn)
			imgSignOff.setVisibility(View.VISIBLE);

		else
			imgSignOff.setVisibility(View.GONE);
	}

	public void onSyncClick() {
		if (GenUtils.IsInternetConnected(this)) {
			isClosing = false;
			if (mIsCameraReady) {
				try {
					stopCamera(false);
				} catch (Exception e) {
				}
			}
			Logger.LogToDb(this, "Manual Sync", "Manual Sync Started at"
					+ GenUtils.longToDateTimeString(System.currentTimeMillis()));
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_sync_with_backup, this).equals("1")) {
				pd = ProgressDialog.show(this, "",
						getResources().getString(R.string.PdSyncData), true);
				SyncBackupData syncbackuptask = new SyncBackupData(this);
				syncbackuptask.execute(this);

			} else {
				new SyncTask()
						.execute(new SyncTask.SyncTaskPayLoad(
								getApplicationContext(), new Object[] { 2,
										this, null }));

				pd = ProgressDialog.show(this, "",
						getResources().getString(R.string.PdSyncData), true);
			}
		} else {
			Toast.makeText(this, "Internet not connected", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * Send Data
	 * 
	 * @param v
	 *            - View
	 */

	public void onSendClick() {
		if (GenUtils.IsInternetConnected(this)) {
			isClosing = false;
			if (mIsCameraReady) {
				try {
					stopCamera(false);
				} catch (Exception e) {
				}
			}
			Logger.LogToDb(
					this,
					"Manual Send Data",
					"Manual Send Data Started at"
							+ GenUtils.longToDateTimeString(System
									.currentTimeMillis()));
			new SyncTask().execute(new SyncTask.SyncTaskPayLoad(
					getApplicationContext(), new Object[] { 6, this, null }));

			pd = ProgressDialog.show(this, "",
					getResources().getString(R.string.PdSendData), true);

		} else {
			Toast.makeText(this, "Internet not connected", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void loadQaQuestons(final String qaId) {
		final Intent intent = new Intent(this, AuditQuestionsActivity.class);
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(HomeActivity.this);
				pd.setMessage(getResources().getString(R.string.loding));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_is_mobile_machine,
						HomeActivity.this).equals("1")) {
					// if machine type is mobile counselor then user
					// type
					// =1,question
					// type =2 for spinner type 1 for check box type 3
					// from text box
					Master_QA_Question_List_Obj obj = new Master_QA_Question_List_Obj();
					obj.list = Master_QA_Questions.getViewList(
							qaId,
							Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE
									+ "= 1 and "
									+ Schema.MASTER_AUDIT_QUESTION_USER_TYPE
									+ " =1 and ("
									+ Schema.MASTER_AUDIT_QUESTION_TYPE
									+ "=2 or "
									+ Schema.MASTER_AUDIT_QUESTION_TYPE
									+ " = 3) and "
									+ Schema.MASTER_AUDIT_QUESTION_LANGUAGE
									+ " ='"
									+ getResources().getConfiguration().locale
											.getLanguage() + "'",
							HomeActivity.this);
					intent.putExtra(IntentKeys.key_qa_answer_third_page,
							new Gson().toJson(obj));

				} else {
					// if machine is not mobile counselor then user
					// type =2,question
					// type =2 for text box
					Master_QA_Question_List_Obj obj = new Master_QA_Question_List_Obj();
					obj.list = Master_QA_Questions.getViewList(
							qaId,
							Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE
									+ "= 1 and "
									+ Schema.MASTER_AUDIT_QUESTION_USER_TYPE
									+ " =2 and ("
									+ Schema.MASTER_AUDIT_QUESTION_TYPE
									+ "=2 or "
									+ Schema.MASTER_AUDIT_QUESTION_TYPE
									+ " = 3) and "
									+ Schema.MASTER_AUDIT_QUESTION_LANGUAGE
									+ " ='"
									+ getResources().getConfiguration().locale
											.getLanguage() + "'",
							HomeActivity.this);
					intent.putExtra(IntentKeys.key_qa_answer_third_page,
							new Gson().toJson(obj));

				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (pd != null) {
					pd.dismiss();
					intent.putExtra(IntentKeys.key_treatment_id, qaId);
					startActivity(intent);
					HomeActivity.this.finish();
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);
				}
			}

		};
		task.execute((Void[]) null);
	}

	public void ShowNotification() {
		String message = getResources().getString(R.string.syncComplete);
		Logger.LogToDb(this, "Manual Sync", "Manual Sync complete at"
				+ GenUtils.longToDateTimeString(System.currentTimeMillis()));
		int messageColor = MESSAGE_SHOW_GREEN;
		if (machineAuth == null) {
			message = getResources().getString(
					R.string.internetConnectionUnavailable);
			messageColor = MESSAGE_SHOW_RED;
		} else {
			if (machineAuth.errorMessage != null) {
				if (machineAuth.errorMessage.equals("3")) {
					message = getResources().getString(
							R.string.clusterLockMessage);
					messageColor = MESSAGE_SHOW_YELLOW;
				} else if (machineAuth.errorMessage.equals("4")) {
					message = getResources().getString(R.string.syncComplete);
					try {
						AppStateConfigurationOperations
								.addAppStateConfiguration(
										AppStateConfigurationKeys.key_Sync_All,
										"0", this);
						int totalActivePatient = PatientsOperations
								.getActivePatientCount(this);
						if (totalActivePatient > 0) {
							AppStateConfigurationOperations
									.addAppStateConfiguration(
											AppStateConfigurationKeys.key_patients_contains_value,
											"true", this);
						}
						syncbutton
								.setText(getResources()
										.getString(R.string.sync)
										+ "\n"
										+ getResources().getString(
												R.string.synctxt)
										+ GenUtils.longToDateTimeString((Long
												.valueOf(AppStateConfigurationOperations
														.getKeyValue(
																AppStateConfigurationKeys.Key_Last_Sync,
																this)))));
					} catch (Exception e) {
					}
				} else if (machineAuth.errorMessage.equals("2")) {
					message = getResources().getString(
							R.string.notificationMachineInactive);
					messageColor = MESSAGE_SHOW_RED;
				} else if (machineAuth.errorMessage.equals("1")) {
					message = getResources().getString(
							R.string.notificationMachineIdNotExist);
					messageColor = MESSAGE_SHOW_RED;
				}
			}
		}
		pd.cancel();
		// mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1, message)
		// .sendToTarget();
		// mHandler.obtainMessage(messageColor).sendToTarget();
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, message);
		if (messageColor == MESSAGE_SHOW_GREEN) {
			intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		} else if (messageColor == MESSAGE_SHOW_RED) {
			intent.putExtra(IntentKeys.key_signal_type, Signal.Bad.toString());
		}
		HomeActivity.this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	public void markSendComplete() {
		String message = getResources().getString(R.string.sendComplete);
		Logger.LogToDb(this, "Manual Send", "Manual Send complete at"
				+ GenUtils.longToDateTimeString(System.currentTimeMillis()));
		int messageColor = MESSAGE_SHOW_GREEN;
		if (machineAuth == null) {
			message = getResources().getString(
					R.string.internetConnectionUnavailable);
			messageColor = MESSAGE_SHOW_RED;
		} else {
			if (machineAuth.errorMessage != null) {
				if (machineAuth.errorMessage.equals("3")) {
					message = getResources().getString(
							R.string.clusterLockMessage);
					messageColor = MESSAGE_SHOW_YELLOW;
				} else if (machineAuth.errorMessage.equals("4")) {
					message = getResources().getString(R.string.sendComplete);
				} else if (machineAuth.errorMessage.equals("2")) {
					message = getResources().getString(
							R.string.notificationMachineInactive);
					messageColor = MESSAGE_SHOW_RED;
				} else if (machineAuth.errorMessage.equals("1")) {
					message = getResources().getString(
							R.string.notificationMachineIdNotExist);
					messageColor = MESSAGE_SHOW_RED;
				}
			}
		}
		pd.cancel();
		mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1, message)
				.sendToTarget();
		mHandler.obtainMessage(messageColor).sendToTarget();
	}

	public void GenerateAlert(String treatmentId) {
		int regimenID = TreatmentInStagesOperations.getPatientRegimenId(
				treatmentId, this);

		int doseCount = 0;
		boolean isSputumAlertGen = false;
		boolean isBloodAlertGen = false;
		boolean isXrayAlertGen = false;
		doseCount = DoseAdminstrationOperations.getDosesCountExceptMissed(
				treatmentId, regimenID, this);
		if (regimenID == 1 || regimenID == 2) {

			if (doseCount == 21) {
				isSputumAlertGen = true;
			}

		} else {
			if (regimenID == 3 || regimenID == 4 || regimenID == 13
					|| regimenID == 14) {

				if (doseCount == 10) {
					isSputumAlertGen = true;
				}
			} else {
				if (regimenID >= 5 && regimenID <= 10) {
					if (doseCount == 21 || doseCount == 33 || doseCount == 48) {
						isSputumAlertGen = true;
					}
				} else {
					if (regimenID >= 11 && regimenID <= 12) {
						if (doseCount == 34) {
							isSputumAlertGen = true;
						}
					} else {
						if (regimenID >= 15 && regimenID <= 20) {
							if (doseCount == 21 || doseCount == 60) {
								isSputumAlertGen = true;
							}
						}

						else {
							if (regimenID == 22) {
								if (doseCount == 24 || doseCount == 48
										|| doseCount == 72 || doseCount == 144) {
									isSputumAlertGen = true;
								}
								if (doseCount == 24 || doseCount == 48
										|| doseCount == 72) {
									isBloodAlertGen = true;
								}
								if (doseCount == 144) {
									isXrayAlertGen = true;
								}
							} else {
								if (regimenID == 23) {
									if (doseCount == 24 || doseCount == 48
											|| doseCount == 72) {
										isSputumAlertGen = true;
									}
								} else {
									if (regimenID == 24) {
										if (doseCount % 72 == 0) {
											isSputumAlertGen = true;
										}
									}
									if (doseCount == 432) {
										isXrayAlertGen = true;
									}
								}
							}
						}
					}
				}
			}

		}

		if (isSputumAlertGen == true || isBloodAlertGen == true
				|| isXrayAlertGen == true) {
			GenerateSputumBloodandXrayAlert(isSputumAlertGen, isBloodAlertGen,
					isXrayAlertGen);
		}
	}

	public void showCpVideos(String treatmentId) {
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_video_counseling_enabled,
				HomeActivity.this).equals("1")) {

			Master r = TreatmentInStagesOperations.getPatientRegimen(
					treatmentId, this);

			String cat = r.catagory;
			String stage = r.stage;

			if (PatientsOperations.isInitialCounsellingPending(treatmentId,
					this)) {
				stage = "IP";// If Initial Counseling Pending, consider this as
								// IP Patient as his counseling is pending.
			}

			String isDaily = "0";
			if (r.daysFrequency == 1) {
				isDaily = "1";
			}

			ArrayList<String> videos = VideosCategoryOperations.GetVideoList(
					cat, stage, isDaily, this); // Get Videos for Category (I or
												// II) and Stage (IP - Start of
												// Treatment, CP - End of IP)

			if (videos.size() == 0) {
				return;
			}

			isClosing = true;
			try {
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}
			Intent counselingActivity = new Intent(HomeActivity.this,
					DisplayVideosActivity.class);
			counselingActivity.putExtra(IntentKeys.key_treatment_id,
					treatmentId);
			counselingActivity.putExtra(IntentKeys.key_visitor_id,
					VisitorLoginOperations.getLastVisitor(HomeActivity.this));
			startActivity(counselingActivity);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			this.finish();
		} else {
			isInitialCounselingPopup = false;

		}
	}

	public boolean repeatCounsling(String treatmentId) {
		((eComplianceApp) this.getApplication()).isAllVideosDownloaded();
		if (!((eComplianceApp) this.getApplication()).IsAllVideosDownloaded) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					"All videos are not available for counseling. Please download the videos.")
					.sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "").sendToTarget();
			mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			return false;
		}
		// If Patient Registered today, do not do repeat counseling
		Patient p = PatientsOperations.getPatientDetails(treatmentId, this);
		if (p.RegDate == GenUtils.getCurrentDateLong()) {
			return false;
		}

		boolean repeatCounselling = false;
		if (PatientsRepeatCounsellingOperations
				.patientExists(treatmentId, this)) {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_video_counseling_enabled,
					HomeActivity.this).equals("1")) {

				Master regimen = RegimenMasterOperations.getRegimen(
						TreatmentInStagesOperations.getPatientRegimenId(
								treatmentId, this), this);
				patientdosetakenlist = PatientDosePriorOperations
						.getPatientDosePrior(Schema.PATIENT_DOSETAKEN_PRIOR_ID
								+ "= '" + treatmentId + "' and "
								+ Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED
								+ "=0", this);
				DoseCount = PatientDosePriorOperations.getIP_ExIP_DosesCount(
						treatmentId, regimen.catagory, regimen.stage,
						Enums.DoseType.getDoseType(DoseType.Supervised)
								.toString(), this)
						+ PatientDosePriorOperations
								.getIP_ExIP_DosesCount(
										treatmentId,
										regimen.catagory,
										regimen.stage,
										Enums.DoseType.getDoseType(
												DoseType.Unsupervised)
												.toString(), this);
				if (patientdosetakenlist.size() > 0) {
					if (regimen.catagory.equals(Enums.CategoryType
							.getCategoryType(CategoryType.CAT1))
							|| regimen.catagory.equals(Enums.CategoryType
									.getCategoryType(CategoryType.CAT2))
							&& regimen.stage.equals(Enums.StageType
									.getStageType(StageType.CP))) {
						DoseCount = DoseCount
								+ patientdosetakenlist.get(0).TakenExtIpDoses;
						if (DoseCount == 0) {

							int maxVideoId = 0;
							Master r = TreatmentInStagesOperations
									.getPatientRegimen(treatmentId,
											HomeActivity.this);

							String cat = r.catagory;
							String stage = r.stage;
							String isDaily = "0";
							if (r.daysFrequency == 1) {
								isDaily = "1";
							}

							ArrayList<String> videos = VideosCategoryOperations
									.GetVideoList(cat, stage, isDaily,
											HomeActivity.this);
							maxVideoId = ECounselingOperations.maxVideoId(
									treatmentId, stage, isDaily,
									HomeActivity.this);
							if (videos.size() != 0
									&& videos.size() != maxVideoId) {
								repeatCounselling = true;
								isStartCp = true;
								InitialCounselingOperations.Delete(treatmentId,
										HomeActivity.this);
								ECounselingOperations.Delete(treatmentId,
										stage, isDaily, HomeActivity.this);
								isInitialCounselingPopup = true;
								showAlertDialog(treatmentId);
							} else {
								if (videos.size() == maxVideoId) {
									PatientsRepeatCounsellingOperations
											.HardDelete(treatmentId, this);
									InitialCounselingOperations.Update(
											treatmentId, this);
									return repeatCounselling;
								}
							}

						}

					} else {
						PatientsRepeatCounsellingOperations.HardDelete(
								treatmentId, this);
						InitialCounselingOperations.Update(treatmentId, this);
						return repeatCounselling;
					}
				}

			} else {
				PatientsRepeatCounsellingOperations.HardDelete(treatmentId,
						this);
				InitialCounselingOperations.Update(treatmentId, this);

				return repeatCounselling;
			}
		} else {

			Master regimen = RegimenMasterOperations.getRegimen(
					TreatmentInStagesOperations.getPatientRegimenId(
							treatmentId, this), this);
			patientdosetakenlist = PatientDosePriorOperations
					.getPatientDosePrior(Schema.PATIENT_DOSETAKEN_PRIOR_ID
							+ "= '" + treatmentId + "' and "
							+ Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED + "=0",
							this);
			DoseCount = PatientDosePriorOperations.getIP_ExIP_DosesCount(
					treatmentId, regimen.catagory, regimen.stage,
					Enums.DoseType.getDoseType(DoseType.Supervised).toString(),
					this)
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
							treatmentId, regimen.catagory, regimen.stage,
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this);
			if (patientdosetakenlist.size() > 0) {
				// for category-I
				if (regimen.catagory.equals(Enums.CategoryType
						.getCategoryType(CategoryType.CAT1))) {
					if (regimen.stage.equals(Enums.StageType
							.getStageType(StageType.ExtIP))) {

						DoseCount = DoseCount
								+ patientdosetakenlist.get(0).TakenExtIpDoses;
						if (DoseCount == 0) {

							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);
						}
					}

					if (regimen.stage.equals(Enums.StageType
							.getStageType(StageType.CP))) {

						DoseCount = DoseCount
								+ patientdosetakenlist.get(0).TakenCpDoses;

						if (DoseCount == 0) {
							isStartCp = true;
							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);
						}

						if (DoseCount == 8) {

							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);

						}

						if (DoseCount == 16) {

							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);

						}
					}
				}

				// for category-II
				if (regimen.catagory.equals(Enums.CategoryType
						.getCategoryType(CategoryType.CAT2))) {
					if (regimen.stage.equals(Enums.StageType
							.getStageType(StageType.ExtIP))) {

						DoseCount = DoseCount
								+ patientdosetakenlist.get(0).TakenExtIpDoses;
						if (DoseCount == 0) {

							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);

						}

					}
					if (regimen.stage.equals(Enums.StageType
							.getStageType(StageType.CP))) {

						DoseCount = DoseCount
								+ patientdosetakenlist.get(0).TakenCpDoses;

						if (DoseCount == 0) {
							isStartCp = true;
							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);

						}

						if (DoseCount == 8) {

							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);

						}

						if (DoseCount == 20) {

							repeatCounselling = true;
							isInitialCounselingPopup = true;
							showAlertDialog(treatmentId);

						}
					}
				}

			}
		}

		if (repeatCounselling) {
			PatientsRepeatCounsellingOperations.addRepeatCounsellingData(
					treatmentId, DbUtils.getTabId(this), this);
			InitialCounselingOperations.Add(treatmentId, HomeActivity.this);

		}

		return repeatCounselling;
	}

	private boolean isSundayOn() {
		boolean retValue = true;
		if (GenUtils.dateToDay(System.currentTimeMillis()) == 1) {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_sunday_enabled, this).equals(
					"false")) {
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.sundayBlockMessage))
						.sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				retValue = false;
			}
		}
		return retValue;
	}

	public void onSyncClick(View v) {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.customdialog);
		LinearLayout layout = (LinearLayout) dialog
				.findViewById(R.id.customLayout);
		TextView sync = (TextView) dialog.findViewById(R.id.sync);
		TextView sendata = (TextView) dialog.findViewById(R.id.senddata);
		layout.setBackgroundDrawable(syncbutton.getBackground());
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		sync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onSyncClick();
			}
		});
		sendata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onSendClick();
			}
		});

		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.CLIP_HORIZONTAL;
		wmlp.x = 314; // x position
		wmlp.y = 157; // y position
		dialog.show();

	}

	public void setAutoSyncFrequency() {
		String[] values = null;
		long timerDuration = 0;
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.auto_sync_frequency_dialog);
		dialog.setTitle(getResources().getString(R.string.setfrequency));
		dialog.setCancelable(true);
		final Spinner spinner = (Spinner) dialog
				.findViewById(R.id.sync_freq_listview);
		try {
			String key = AppStateConfigurationOperations
					.getKeyValue(
							AppStateConfigurationKeys.key_user_auto_sync_duration,
							this);
			if (key != null) {
				if (key.equals("Server")) {
					timerDuration = (Long.parseLong(ConfigurationOperations
							.getKeyValue(
									ConfigurationKeys.key_auto_sync_duration,
									this)) / 60000);

					values = new String[] { String.valueOf(timerDuration),
							"30", "45", "60", "75", "90", "120" };
				} else {
					timerDuration = (Long
							.parseLong(AppStateConfigurationOperations
									.getKeyValue(
											AppStateConfigurationKeys.key_user_auto_sync_duration,
											this)) / 60000);

					values = new String[] {
							String.valueOf((Long.parseLong(ConfigurationOperations
									.getKeyValue(
											ConfigurationKeys.key_auto_sync_duration,
											this)) / 60000)), "30", "45", "60",
							"75", "90", "120" };
				}
			}

		} catch (NumberFormatException e) {
		}

		Button btnsetfrequency = (Button) dialog
				.findViewById(R.id.btnsetfrequency);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(String.valueOf(timerDuration))) {
				spinner.setSelection(i);
				break;
			}
		}

		btnsetfrequency.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String value = String.valueOf((Integer.parseInt(spinner
						.getSelectedItem().toString()) * 60000));
				AppStateConfigurationOperations.addAppStateConfiguration(
						AppStateConfigurationKeys.key_user_auto_sync_duration,
						value, HomeActivity.this);
				showSyncRestartDialog();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void showSyncRestartDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_box_yes_no);

		Button noButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
		Button yesButton = (Button) dialog.findViewById(R.id.dialogButtonYes);
		ImageView img = (ImageView) dialog.findViewById(R.id.imageDialog);
		img.setImageDrawable(getResources().getDrawable(
				R.drawable.application_logo));
		TextView message = (TextView) dialog.findViewById(R.id.messageText);
		TextView title = (TextView) dialog.findViewById(R.id.text);
		message.setText(getResources().getString(R.string.restsrtmsg));
		title.setText(getResources().getString(R.string.restartapp));
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HomeActivity.this.finish();

			}
		});
		// if button is clicked, close the custom dialog
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public boolean checkMoreDoses(String treatmentId) {
		Patient patient = PatientsOperations.getPatientDetails(treatmentId,
				this);
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentId,
						this), this);
		patientdosetakenlist = PatientDosePriorOperations.getPatientDosePrior(
				Schema.PATIENT_DOSETAKEN_PRIOR_ID + "= '" + treatmentId
						+ "' and " + Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED
						+ "=0", this);
		SelfAdminDose = PatientDosePriorOperations.getSelfAdmidDosesCount(
				treatmentId, regimen.catagory, regimen.stage, Enums.DoseType
						.getDoseType(DoseType.SelfAdministered).toString(),
				this);
		DoseCount = PatientDosePriorOperations.getIP_ExIP_DosesCount(
				treatmentId, regimen.catagory, regimen.stage, Enums.DoseType
						.getDoseType(DoseType.Supervised).toString(), this)
				+ PatientDosePriorOperations.getIP_ExIP_DosesCount(treatmentId,
						regimen.catagory, regimen.stage, Enums.DoseType
								.getDoseType(DoseType.Unsupervised).toString(),
						this);

		if (patientdosetakenlist.size() > 0) {
			if (regimen.catagory.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT1))) {

				if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.IP))) {

					DoseCount = DoseCount + SelfAdminDose
							+ patientdosetakenlist.get(0).TakenIpDoses;
					if (regimen.daysFrequency == 1) {
						if (DoseCount > IntentKeys.key_catI_IP_Daily - 1) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					} else {
						if (DoseCount > 23) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					}

				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.ExtIP))) {
					DoseCount = DoseCount + SelfAdminDose
							+ patientdosetakenlist.get(0).TakenExtIpDoses;

					if (DoseCount > 11) {
						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								getResources().getString(R.string.Patient)
										+ " "
										+ patient.name
										+ "("
										+ patient.treatmentID
										+ ") "
										+ getResources().getString(
												R.string.canttakemoredoses))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
						return true;
					}

				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.CP))) {
					DoseCount = DoseCount + SelfAdminDose
							+ patientdosetakenlist.get(0).TakenCpDoses;
					if (regimen.schedule.equals("Daily")) {
						if (DoseCount > IntentKeys.key_catI_CP_Daily - 1) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					} else {
						if (DoseCount > 17) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					}
				}

			} else if (regimen.catagory.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT2))) {

				if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.IP))) {
					DoseCount = DoseCount + SelfAdminDose
							+ patientdosetakenlist.get(0).TakenIpDoses;
					if (regimen.daysFrequency == 1) {
						if (DoseCount > IntentKeys.key_catII_IP_Daily - 1) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					} else {
						if (DoseCount > 35) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					}
				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.ExtIP))) {
					DoseCount = DoseCount + SelfAdminDose
							+ patientdosetakenlist.get(0).TakenExtIpDoses;

					if (DoseCount > 11) {
						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								getResources().getString(R.string.Patient)
										+ " "
										+ patient.name
										+ "("
										+ patient.treatmentID
										+ ") "
										+ getResources().getString(
												R.string.canttakemoredoses))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
						return true;
					}

				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.CP))) {
					DoseCount = DoseCount + SelfAdminDose
							+ patientdosetakenlist.get(0).TakenCpDoses;
					if (regimen.schedule.equals("Daily")) {
						if (DoseCount > IntentKeys.key_catII_CP_Daily - 1) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					} else {
						if (DoseCount > 21) {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources().getString(R.string.Patient)
											+ " "
											+ patient.name
											+ "("
											+ patient.treatmentID
											+ ") "
											+ getResources().getString(
													R.string.canttakemoredoses))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							return true;
						}
					}
				}

			}

		}
		return false;

	}

	public void showAlertDialog(final String treatmentId) {
		final Patient patient = PatientsOperations.getPatientDetails(
				treatmentId, this);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Setting message manually and performing action on
		// button click
		builder.setMessage("Please start the Counselling").setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						// InitialCounselingOperations.Add(treatmentId,
						// HomeActivity.this);
						mHandler.obtainMessage(
								MESSAGE_SHOW_HEADER,
								-1,
								-1,
								String.format(
										getResources()
												.getString(
														R.string.repeatCounsellingStarted),
										patient.name + "("
												+ patient.treatmentID + ") "))
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();

						// Show Counseling Videos at Start of CP
						if (isStartCp) {
							isStartCp = false;

							showCpVideos(patient.treatmentID);
						} else {
							isInitialCounselingPopup = false;

						}

						return;
					}
				});

		// Creating dialog box
		AlertDialog alert = builder.create();
		// Setting the title manually
		alert.setTitle(patient.name + "(" + patient.treatmentID
				+ ") repeat Counselling is pending");
		alert.show();
	}

	public void processFinish(final String output) {
		pd.cancel();

		if (output.contains("Complete")) {

			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra(IntentKeys.key_message_home, output);
			intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
			HomeActivity.this.finish();
			startActivity(intent);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);

		} else {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra(IntentKeys.key_message_home, output);
			intent.putExtra(IntentKeys.key_signal_type, Signal.Bad.toString());
			HomeActivity.this.finish();
			startActivity(intent);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		}
	}

	public void showInitialCounsellingMessage(String name, final String Id) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.custom_dialog_box_yes_no);
		Button noButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
		Button yesButton = (Button) dialog.findViewById(R.id.dialogButtonYes);
		TextView message = (TextView) dialog.findViewById(R.id.messageText);
		TextView title = (TextView) dialog.findViewById(R.id.text);
		message.setText(name + "(" + Id + ") Initial Counselling started.");
		noButton.setVisibility(View.GONE);
		yesButton.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setText("Initial Counselling");
		yesButton.setText(getResources().getString(R.string.ok));
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// isInitialCounselingPopup = false;
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_is_video_counseling_enabled,
						HomeActivity.this).equals("1")) {
					Master r = TreatmentInStagesOperations.getPatientRegimen(
							Id, HomeActivity.this);

					String cat = r.catagory;
					String stage = r.stage;

					if (PatientsOperations.isInitialCounsellingPending(Id,
							HomeActivity.this)) {
						stage = "IP";// If Initial Counseling Pending, consider
										// this as IP Patient as his counseling
										// is pending.
					}

					String isDaily = "0";
					if (r.daysFrequency == 1) {
						isDaily = "1";
					}

					ArrayList<String> videos = VideosCategoryOperations
							.GetVideoList(cat, stage, isDaily,
									HomeActivity.this); // Get Videos for
														// Category (I or II)
														// and Stage (IP - Start
														// of Treatment, CP -
														// End of IP)

					if (videos.size() == 0) {
						return;
					}

					isClosing = true;
					try {
						usb_host_ctx.CloseDevice();
					} catch (Exception e) {
					}
					Intent counselingActivity = new Intent(HomeActivity.this,
							DisplayVideosActivity.class);
					counselingActivity
							.putExtra(IntentKeys.key_treatment_id, Id);
					counselingActivity.putExtra(IntentKeys.key_visitor_id,
							VisitorLoginOperations
									.getLastVisitor(HomeActivity.this));
					startActivity(counselingActivity);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
					HomeActivity.this.finish();
				} else {
					isInitialCounselingPopup = false;
				}

			}
		});

		dialog.show();
		return;
	}

	public void generateSelfAdminDoseforDailyRegimen(String treatmentId,
			String lastLoginId, GpsTracker gps) {
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentId,
						this), this);

		if (regimen.daysFrequency == 1) {
			Dose dose = DoseAdminstrationOperations.getLastDose(treatmentId,
					this);
			long dosedate = dose.doseDate;
			if (regimen.stage
					.equals(Enums.StageType.getStageType(StageType.IP))
					|| regimen.stage.equals(Enums.StageType
							.getStageType(StageType.CP))) {
				if (GenUtils.dateToDay(dose.doseDate) == 7) {
					dosedate = dosedate + GenUtils.ONE_DAY;
					DoseUtils.AddDose(treatmentId,
							Enums.DoseType.SelfAdministered.toString(),
							dosedate, regimen.regimenId,
							GenUtils.getCurrentDateLong(), lastLoginId,
							gps.getLatitude(), gps.getLongitude(), this);
				}
			}

		}

	}

}
