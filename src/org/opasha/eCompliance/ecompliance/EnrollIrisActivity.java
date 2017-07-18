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

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.HIVOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialLabMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.LocationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientGenderAgeOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabFollowUpOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabsOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientV2_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PositiveContactsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IddkCaptureInfo;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.IrisDefault;
import org.opasha.eCompliance.ecompliance.util.IrisUtility;
import org.opasha.eCompliance.ecompliance.util.Logger;
import org.opasha.eCompliance.ecompliance.util.MediaData;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
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

/**
 * @author abhishek
 * 
 */
public class EnrollIrisActivity extends Activity {

	private ImageView imgScan;
	private ImageView imgScan1;
	private ImageView imgScan2;
	private ImageView imgScan3;
	private ImageView imgScan4;
	private ImageView imgScan5;
	private ImageView imgScan6;
	private ImageView imgScan7;
	private ImageView imgScan8;
	private TextView lblStatus;
	private TextView lblHeader;
	LinearLayout layout;
	TextView showId;
	// ImageView imgStatic;
	String contactId = "";
	String name, phone, stage, schedule, category, selectGender, hivResult,
			spinnerInitialLab, editTxtAge, center, tabId, centerTabId, type,
			machineId, status, labDMC, labNo, labWeight, labMonth, address,
			diseaseClassification, diseaseSite, patientType, nikashayId,
			tbNumber, aadhaarNumber, patient_Source;
	private static String treatmentId = "";
	private static VisitorType visitorType;
	private Enums.ReportType reptype = null;
	long labDate;
	private Boolean smokingHistory;
	// --------------------
	// Finger Print Related
	// --------------------

	boolean isClosing = false;
	boolean isScanOn = false;
	private boolean oldID = false;
	private boolean isHivEnable = true;
	private boolean IsInitialLabOn = true;

	// private UsbDeviceDataExchangeImpl usb_host_ctx = null;

	public static final int MESSAGE_SHOW_MSG = 1;
	public static final int MESSAGE_SHOW_IMAGE = 2;
	public static final int MESSAGE_ENABLE_CONTROLS = 3;
	public static final int MESSAGE_COMPLETE = 4;
	public static final int MESSAGE_SHOW_HEADER = 5;
	public static final int MESSAGE_CANCELED = 6;
	public static final int MESSAGE_SHOW_GREEN = 7;
	public static final int MESSAGE_SHOW_RED = 8;
	// public static final int MESSAGE_SHOW_LEFT_INDEX = 9;
	// public static final int MESSAGE_SHOW_RIGHT_INDEX = 10;
	public static final int MESSAGE_SAVE_HEADER = 11;
	public static final int MESSAGE_IDENTIFICATION = 12;

	private Bitmap mBitmapFP = null;

	private int eyeCount = 0;

	private boolean isLastError = false;
	private String savedHeader = "";

	private String IPDose, ExtIPDose, CPDose;

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
	private boolean mIsPermissionDenied = false;
	private int mScreenWidth = 0;
	private Iddk2000Apis mApis;

	private int totalNumberOfTemplates = 0;

	private enum eIdentifyResult {
		IRI_IDENTIFY_DIFFERENT, IRI_IDENTIFY_LOOKLIKE, IRI_IDENTIFY_SAME, IRI_IDENTIFY_DUPLICATED
	};

	private int minIrisQuality;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_iris);
		Bundle extras = getIntent().getExtras();
		// Return to main screen if no extras are sent.
		// mApis = ((eComplianceApp) this.getApplication()).mApis;
		mApis = Iddk2000Apis.getInstance(this);

		minIrisQuality = ((eComplianceApp) this.getApplication()).IrisRegQuality;
		if (extras == null) {
			this.finish();
			return;
		}
		visitorType = VisitorType.getVisitorType(extras
				.getString(IntentKeys.key_visitor_type));
		treatmentId = extras.getString(IntentKeys.key_treatment_id);

		try {
			reptype = (ReportType) extras.get(IntentKeys.key_intent_from);
			contactId = getIntent().getExtras().getString(
					IntentKeys.key_contact_id);
		} catch (Exception e) {
		}

		imgScan = (ImageView) findViewById(R.id.imgRegIrisImg);
		lblStatus = (TextView) findViewById(R.id.lblRegScanText);
		lblHeader = (TextView) findViewById(R.id.lblRegHeader);
		layout = (LinearLayout) findViewById(R.id.layout_enroll_fp);
		showId = (TextView) findViewById(R.id.showId);
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);

		imgScan1 = (ImageView) findViewById(R.id.imgScan1);
		imgScan2 = (ImageView) findViewById(R.id.imgScan2);
		imgScan3 = (ImageView) findViewById(R.id.imgScan3);
		imgScan4 = (ImageView) findViewById(R.id.imgScan4);
		imgScan5 = (ImageView) findViewById(R.id.imgScan5);
		imgScan6 = (ImageView) findViewById(R.id.imgScan6);
		imgScan7 = (ImageView) findViewById(R.id.imgScan7);
		imgScan8 = (ImageView) findViewById(R.id.imgScan8);

		showId.setText(getResources().getString(R.string.id) + " "
				+ treatmentId);
		showId.setTypeface(null, Typeface.BOLD);

		startEnrollmentActivity();
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		// SaveVisitorsDetail();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isClosing = true;
		try {
			stopCamera(true);
			mApis.closeDevice(mDeviceHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHandler.removeCallbacksAndMessages(null);
	}

	private void startEnrollmentActivity() {

		if (visitorType == VisitorType.Patient) {
			totalNumberOfTemplates = ((eComplianceApp) this.getApplication()).MaxIrisTemplatePatient;
		} else {
			if (visitorType == VisitorType.Counselor) {
				totalNumberOfTemplates = ((eComplianceApp) this
						.getApplication()).MaxIrisTemplateProvider;
			} else {
				if (visitorType == VisitorType.PM) {
					totalNumberOfTemplates = ((eComplianceApp) this
							.getApplication()).MaxIrisTemplatePm;
				} else {
					totalNumberOfTemplates = ((eComplianceApp) this
							.getApplication()).MaxIrisTemplateOthers;
				}
			}
		}

		// Limiting the number of templates to 8 MAX
		if (totalNumberOfTemplates > 8)
			totalNumberOfTemplates = 8;

		if (totalNumberOfTemplates < 2)
			imgScan2.setVisibility(View.GONE);
		if (totalNumberOfTemplates < 3)
			imgScan3.setVisibility(View.GONE);
		if (totalNumberOfTemplates < 4)
			imgScan4.setVisibility(View.GONE);
		if (totalNumberOfTemplates < 5)
			imgScan5.setVisibility(View.GONE);
		if (totalNumberOfTemplates < 6)
			imgScan6.setVisibility(View.GONE);
		if (totalNumberOfTemplates < 7)
			imgScan7.setVisibility(View.GONE);
		if (totalNumberOfTemplates < 8)
			imgScan8.setVisibility(View.GONE);

		callEyesanReg();
	}

	// --------------------
	// Eye Scan Related
	// --------------------
	public void callEyesanReg() {

		if (eyeCount == 0) {
			mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.scanRightEye))
					.sendToTarget();
			if (mDeviceHandle != null) {
				startCamera(true);
			} else {
				scanEye();
			}
		} else {
			updateSCanImages();
			if (eyeCount >= (totalNumberOfTemplates / 2)) {
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.scanLeftEye))
						.sendToTarget();
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.scanRightEye))
						.sendToTarget();
			}
			startCamera(true);
		}
	}

	private void updateSCanImages() {
		if (eyeCount >= 1)
			imgScan1.setImageResource(R.drawable.checked);
		if (eyeCount >= 2)
			imgScan2.setImageResource(R.drawable.checked);
		if (eyeCount >= 3)
			imgScan3.setImageResource(R.drawable.checked);
		if (eyeCount >= 4)
			imgScan4.setImageResource(R.drawable.checked);
		if (eyeCount >= 5)
			imgScan5.setImageResource(R.drawable.checked);
		if (eyeCount >= 6)
			imgScan6.setImageResource(R.drawable.checked);
		if (eyeCount >= 7)
			imgScan7.setImageResource(R.drawable.checked);
		if (eyeCount >= 8)
			imgScan8.setImageResource(R.drawable.checked);
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
		// registerReceiver(mUsbReceiver, filter);
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
		}
	};

	/*****************************************************************************
	 * Reset all the internal states of the application. This function is called
	 * whenever device connection has been changed.
	 *****************************************************************************/
	private void setInitState() {
		mIsCameraReady = false;
		mIsPermissionDenied = false;
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
		mIsPermissionDenied = false;

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
					// stopCamera(false);
					// openDevice();
					// startCamera(true);
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
			new ArrayList<String>();

			StringBuffer enrollId = new StringBuffer();
			eIdentifyResult identifyResult = identify(enrollId);
			if (eIdentifyResult.IRI_IDENTIFY_SAME == identifyResult
					|| eIdentifyResult.IRI_IDENTIFY_DUPLICATED == identifyResult) {
				if (enrollId.toString().equals(treatmentId)) {
					mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
							"Eye already scanned. Try a different angle.")
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				} else {
					mHandler.obtainMessage(MESSAGE_IDENTIFICATION, -1, -1,
							treatmentId + "," + enrollId.toString())
							.sendToTarget();
				}
			} else if (eIdentifyResult.IRI_IDENTIFY_DIFFERENT == identifyResult) {
				try {
					Logger.LogToDb(this, "Enroll Fingerprint", "Quality: "
							+ mTotalScore + ", " + mUsableArea
							+ ", Treatment Id: " + treatmentId);
				} catch (Exception e1) {

				}
				enroll();
			}

		}
	}

	private void enroll() {
		boolean isScanAlreadyExist = false;

		if (!isScanAlreadyExist) {
			// ArrayList<String> Ids = new ArrayList<String>();
			//
			// mApis.loadGallery(mDeviceHandle, Ids, null, new IddkInteger(),
			// new IddkInteger());
			// mApis.enrollCapture(mDeviceHandle, treatmentId);
			// mApis.commitGallery(mDeviceHandle);
			mHandler.obtainMessage(IRIS_HANDLE_ERROR, -1, -1,
					"Enroll successfully").sendToTarget();

			String eye = "Right Eye";
			if (eyeCount < (totalNumberOfTemplates / 2))
				eye = "Left Eye";

			ScansIrisOperations.addScan(treatmentId, eye,
					capturedTemplate.getData(), System.currentTimeMillis(),
					((eComplianceApp) this.getApplication()).LastLoginId, this);

			eyeCount++;
			if (eyeCount < totalNumberOfTemplates) {
				callEyesanReg();
			} else {
				mHandler.obtainMessage(MESSAGE_COMPLETE).sendToTarget();
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
		ArrayList<String> Ids = new ArrayList<String>();

		// Un-enroll all templates
		mApis.loadGallery(mDeviceHandle, Ids, null, new IddkInteger(),
				new IddkInteger());

		for (String id : Ids) {
			mApis.unenrollTemplate(mDeviceHandle, id);
		}
		// mApis.unenrollTemplate(mDeviceHandle, null);
		mApis.commitGallery(mDeviceHandle);

		ArrayList<ScanModel> scans = ScansIrisOperations.getScans(true, this);
		for (ScanModel scan : scans) {
			IddkDataBuffer df = new IddkDataBuffer();
			df.setData(scan.scan);
			mApis.enrollTemplate(mDeviceHandle, scan.treatmentId, df);
			mApis.commitGallery(mDeviceHandle);
		}

		ArrayList<IddkComparisonResult> comparisonResults = new ArrayList<IddkComparisonResult>();

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
				enrollId.append(result);
		}

		if (ret.getValue() == IddkResult.IDDK_OK) {
		}

		return identifyResult;
	}

	// ----- End Iris Enrollment and Identification-------------------------

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
				lblStatus.setText(msgs);

				break;
			case MESSAGE_IDENTIFICATION:
				treatmentId = (String) msg.obj;
				String[] id = treatmentId.split(",");
				DbUtils.hardDeletePatient(id[0], EnrollIrisActivity.this);
				treatmentId = id[1];
				if (!PatientsOperations.patientExists(treatmentId,
						EnrollIrisActivity.this)) {
					if (VisitorsOperations.visitorExists(treatmentId,
							EnrollIrisActivity.this)) {
						stopCamera(false);
						Intent editVisitorintent = new Intent(
								EnrollIrisActivity.this,
								EditVisitorActivity.class);
						editVisitorintent.putExtra(IntentKeys.key_treatment_id,
								treatmentId);
						EnrollIrisActivity.this.finish();
						startActivity(editVisitorintent);
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
					} else {

						GoToHome(getResources()
								.getString(R.string.scanmismatch), Signal.Bad);
					}
				} else if (PatientsOperations.getPatientDetails(treatmentId,
						EnrollIrisActivity.this).Status.equals(Enums.StatusType
						.getStatusType(StatusType.Default).toString())) {
					GoToHome(
							getResources()
									.getString(R.string.defaultCanNotEdit),
							Signal.Bad);
				} else if (PatientsOperations.getPatientDetails(treatmentId,
						EnrollIrisActivity.this).Status.equals(Enums.StatusType
						.getStatusType(StatusType.TransferredInternally)
						.toString())) {
					GoToHome(
							getResources().getString(
									R.string.transferInternallyCanNotEdit),
							Signal.Bad);
				} else {
					stopCamera(false);
					Intent editPatientIntent = new Intent(
							EnrollIrisActivity.this, EditPatientActivity.class);
					editPatientIntent.putExtra(IntentKeys.key_treatment_id,
							treatmentId);
					editPatientIntent.putExtra(IntentKeys.key_new_patient,
							false);
					editPatientIntent.putExtra(IntentKeys.key_intent_from,
							Enums.IntentFrom.Home);
					EnrollIrisActivity.this.finish();
					startActivity(editPatientIntent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
				}
				break;
			case MESSAGE_SHOW_MSG:
				String showMsg = (String) msg.obj;
				lblStatus.setText(showMsg);
				break;
			case MESSAGE_SHOW_IMAGE:
				imgScan.setImageBitmap(getResizedBitmap(mBitmapFP, 400));
				break;
			case MESSAGE_COMPLETE:
				isClosing = true;
				try {
					PositiveContactsOperations.updateTreatmentId(treatmentId,
							contactId, EnrollIrisActivity.this);
				} catch (Exception e) {

				}
				if (visitorType == VisitorType.Patient) {
					SaveDetails();
				} else {
					SaveVisitorsDetail();
				}
				EnrollIrisActivity.this.finish();

				Intent intent = new Intent(EnrollIrisActivity.this,
						HomeActivity.class);

				intent.putExtra(IntentKeys.key_message_home,
						VisitorType.GetViewString(visitorType) + "("
								+ treatmentId + ") "
								+ getResources().getString(R.string.registered));
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Good.toString());
				startActivity(intent);
				break;
			case MESSAGE_CANCELED:
				// *AS Delete data from database if registration is cancelled.
				if (reptype != null) {
					if (!(reptype
							.equals(Enums.ReportType.PatientsFromLegacySystem) || reptype
							.equals(Enums.ReportType.VisitorReregistration))) {
						if (visitorType == VisitorType.Patient) {
							PatientsOperations.deletePatientHard(treatmentId,
									EnrollIrisActivity.this);
							TreatmentInStagesOperations.deletePatientHard(
									treatmentId, EnrollIrisActivity.this);
							PatientLabsOperation.labHardDelete(treatmentId,
									EnrollIrisActivity.this);
							LocationOperations.delete(EnrollIrisActivity.this,
									treatmentId);
						} else {
							VisitorsOperations.deleteVisitorHard(treatmentId,
									EnrollIrisActivity.this);
						}
					}
				} else {
					if (visitorType == VisitorType.Patient) {
						PatientsOperations.deletePatientHard(treatmentId,
								EnrollIrisActivity.this);
						TreatmentInStagesOperations.deletePatientHard(
								treatmentId, EnrollIrisActivity.this);
						PatientLabsOperation.labHardDelete(treatmentId,
								EnrollIrisActivity.this);
						LocationOperations.delete(EnrollIrisActivity.this,
								treatmentId);
					} else {
						VisitorsOperations.deleteVisitorHard(treatmentId,
								EnrollIrisActivity.this);
					}
				}

				ScansIrisOperations.deleteScans(EnrollIrisActivity.this,
						treatmentId);

				intent = new Intent(EnrollIrisActivity.this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, getResources()
						.getString(R.string.registrationCanceled));
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Bad.toString());
				startActivity(intent);
				EnrollIrisActivity.this.finish();
				break;
			case MESSAGE_SHOW_HEADER:
				String showheader = (String) msg.obj;
				lblHeader.setText(showheader);
				lblStatus.setText("");
				break;
			case MESSAGE_SHOW_GREEN:
				if (!isLastError)
					getGreenAnimation();
				break;
			case MESSAGE_SHOW_RED:
				getRedAnimation();
				break;
			case MESSAGE_SAVE_HEADER:
				savedHeader = lblHeader.getText().toString();
				break;

			case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE: {
				getRedAnimation();
				lblStatus.setText(getResources().getString(
						R.string.userDenyScannerDevice));
				break;

			}
			}
		}
	};

	// -------------End Futronic Enrollment and Identification----------------
	protected void SaveVisitorsDetail() {
		Bundle extras = getIntent().getExtras();
		if (reptype != null)
			if (reptype == ReportType.PatientsFromLegacySystem
					|| reptype == ReportType.VisitorReregistration)
				return;
		boolean auth = false;
		if (extras != null) {
			status = extras.getString(IntentKeys.key_visitor_type);
			type = extras.getString(IntentKeys.key_visitor_type);
			name = extras.getString(IntentKeys.key_visitor_name);
			phone = extras.getString(IntentKeys.key_phone_no);
			treatmentId = extras.getString(IntentKeys.key_treatment_id);
			auth = extras.getBoolean(IntentKeys.key_is_Authenticate);
			machineId = extras.getString(IntentKeys.key_machine_id);
		}

		VisitorsOperations.addVisitor(treatmentId, name, type,
				GenUtils.getCurrentDateLong(),
				StatusType.getStatusType(StatusType.Active), phone, auth,
				System.currentTimeMillis(),
				((eComplianceApp) this.getApplicationContext()).LastLoginId,
				false, machineId, this);

	}

	@Override
	public void onBackPressed() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_box_yes_no);
		// set the custom dialog components - text, image and button

		Button noButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
		Button yesButton = (Button) dialog.findViewById(R.id.dialogButtonYes);
		TextView message = (TextView) dialog.findViewById(R.id.messageText);
		TextView title = (TextView) dialog.findViewById(R.id.text);
		message.setText(getResources().getString(R.string.doYouWantToContinue));
		title.setText(getResources().getString(R.string.cancellation));
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isClosing = false;
				mHandler.obtainMessage(MESSAGE_CANCELED).sendToTarget();
				dialog.dismiss();
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

	private void getRedAnimation() {
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
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

	public void onScanClick(View v) {

		stopCamera(true);
		setInitState();
		callEyesanReg();

		// Bundle extras = getIntent().getExtras();
		// isClosing = true;
		// stopCamera(true);
		// setInitState();
		// startCamera(true);
		// try {
		// // usb_host_ctx.CloseDevice();
		// // mApis.closeDevice(mDeviceHandle);
		// } catch (Exception e) {
		// }
		// Intent intent = new Intent(this, EnrollIrisActivity.class);
		// intent.putExtra(IntentKeys.key_treatment_id, treatmentId);
		// intent.putExtra(IntentKeys.key_visitor_type, visitorType.toString());
		// intent.putExtra(IntentKeys.key_enrollment_current_finger, eyeCount);
		// intent.putExtra(IntentKeys.key_intent_from, reptype);
		// try {
		// if (visitorType == VisitorType.Patient) {
		// intent.putExtra(IntentKeys.key_moit_status,
		// extras.getString(IntentKeys.key_moit_status));
		// intent.putExtra(IntentKeys.key_patient_Name,
		// extras.getString(IntentKeys.key_patient_Name));
		// intent.putExtra(IntentKeys.key_phone_no,
		// extras.getString(IntentKeys.key_phone_no));
		// intent.putExtra(IntentKeys.key_patient_gender,
		// extras.getString(IntentKeys.key_patient_gender));
		// intent.putExtra(IntentKeys.key_patient_initial_lab,
		// extras.getString(IntentKeys.key_patient_initial_lab));
		// intent.putExtra(IntentKeys.key_patient_lab_Date,
		// extras.getLong(IntentKeys.key_patient_lab_Date));
		// intent.putExtra(IntentKeys.kay_patient_lab_month,
		// extras.getString(IntentKeys.kay_patient_lab_month));
		// intent.putExtra(IntentKeys.key_patient_lab_DMC,
		// extras.getString(IntentKeys.key_patient_lab_DMC));
		// intent.putExtra(IntentKeys.key_patient_lab_No,
		// extras.getString(IntentKeys.key_patient_lab_No));
		// intent.putExtra(IntentKeys.key_patient_lab_Weight,
		// extras.getString(IntentKeys.key_patient_lab_Weight));
		// intent.putExtra(IntentKeys.key_current_stage,
		// extras.getString(IntentKeys.key_current_stage));
		// intent.putExtra(IntentKeys.key_current_schedule,
		// extras.getString(IntentKeys.key_current_schedule));
		// intent.putExtra(IntentKeys.key_current_category,
		// extras.getString(IntentKeys.key_current_category));
		// intent.putExtra(IntentKeys.key_hiv_screening_result,
		// extras.getString(IntentKeys.key_hiv_screening_result));
		// intent.putExtra(IntentKeys.key_patient_age,
		// extras.getString(IntentKeys.key_patient_age));
		// intent.putExtra(IntentKeys.key_tab_id,
		// extras.getString(IntentKeys.key_tab_id));
		// intent.putExtra(IntentKeys.key_treatment_id,
		// extras.getString(IntentKeys.key_treatment_id));
		// intent.putExtra(IntentKeys.key_patient_prior_IPdose,
		// extras.getString(IntentKeys.key_patient_prior_IPdose));
		// intent.putExtra(IntentKeys.key_patient_prior_CPdose,
		// extras.getString(IntentKeys.key_patient_prior_CPdose));
		// intent.putExtra(IntentKeys.key_patient_prior_ExtIPdose, extras
		// .getString(IntentKeys.key_patient_prior_ExtIPdose));
		//
		// intent.putExtra(IntentKeys.key_patient_address,
		// extras.getString(IntentKeys.key_patient_address));
		// intent.putExtra(IntentKeys.key_patient_diseaseSite,
		// extras.getString(IntentKeys.key_patient_diseaseSite));
		// intent.putExtra(IntentKeys.key_patient_disease,
		// extras.getString(IntentKeys.key_patient_disease));
		// intent.putExtra(IntentKeys.key_patient_Type,
		// extras.getString(IntentKeys.key_patient_Type));
		// intent.putExtra(IntentKeys.key_patient_nikshayId,
		// extras.getString(IntentKeys.key_patient_nikshayId));
		// intent.putExtra(IntentKeys.key_patient_tbNumber,
		// extras.getString(IntentKeys.key_patient_tbNumber));
		// intent.putExtra(IntentKeys.key_patient_smoking_history, extras
		// .getString(IntentKeys.key_patient_smoking_history));
		// intent.putExtra(IntentKeys.key_patient_aadhaarNo,
		// extras.getString(IntentKeys.key_patient_aadhaarNo));
		// intent.putExtra(IntentKeys.key_patient_patientSource,
		// extras.getString(IntentKeys.key_patient_patientSource));
		//
		// }
		// } catch (Exception e) {
		// }
		// try {
		// if (visitorType != VisitorType.Patient) {
		// intent.putExtra(IntentKeys.key_visitor_type,
		// extras.getString(IntentKeys.key_visitor_type));
		//
		// intent.putExtra(IntentKeys.key_visitor_name,
		// extras.getString(IntentKeys.key_visitor_name));
		// intent.putExtra(IntentKeys.key_phone_no,
		// extras.getString(IntentKeys.key_phone_no));
		// intent.putExtra(IntentKeys.key_treatment_id,
		// extras.getString(IntentKeys.key_treatment_id));
		// intent.putExtra(IntentKeys.key_is_Authenticate,
		// extras.getString(IntentKeys.key_is_Authenticate));
		// intent.putExtra(IntentKeys.key_machine_id,
		// extras.getString(IntentKeys.key_machine_id));
		// }
		// } catch (Exception e) {
		// }
		// try {
		// intent.putExtra(IntentKeys.key_contact_id, getIntent().getExtras()
		// .getString(IntentKeys.key_contact_id));
		// } catch (Exception e) {
		// }
		// this.finish();
		// startActivity(intent);
	}

	public void ResetHeader() {
		mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1, savedHeader)
				.sendToTarget();
	}

	private void GoToHome(String message, Signal signal) {
		stopCamera(false);
		Intent intent = new Intent(this, HomeActivity.class);
		if (!message.isEmpty()) {
			intent.putExtra(IntentKeys.key_message_home, message);
			intent.putExtra(IntentKeys.key_signal_type, signal.toString());
		}
		this.finish();
		startActivity(intent);

	}

	private void SaveDetails() {
		Bundle extras = getIntent().getExtras();
		if (reptype != null)
			if (reptype == ReportType.PatientsFromLegacySystem
					|| reptype == ReportType.VisitorReregistration)
				return;
		if (extras != null) {
			name = extras.getString(IntentKeys.key_patient_Name);
			phone = extras.getString(IntentKeys.key_phone_no);
			selectGender = extras.getString(IntentKeys.key_patient_gender);
			spinnerInitialLab = extras
					.getString(IntentKeys.key_patient_initial_lab);
			stage = extras.getString(IntentKeys.key_current_stage);
			schedule = extras.getString(IntentKeys.key_current_schedule);
			category = extras.getString(IntentKeys.key_current_category);
			hivResult = extras.getString(IntentKeys.key_hiv_screening_result);
			editTxtAge = extras.getString(IntentKeys.key_patient_age);
			tabId = extras.getString(IntentKeys.key_tab_id);
			treatmentId = extras.getString(IntentKeys.key_treatment_id);
			IPDose = extras.getString(IntentKeys.key_patient_prior_IPdose);
			CPDose = extras.getString(IntentKeys.key_patient_prior_CPdose);
			ExtIPDose = extras
					.getString(IntentKeys.key_patient_prior_ExtIPdose);

			labDate = extras.getLong(IntentKeys.key_patient_lab_Date);
			labDMC = extras.getString(IntentKeys.key_patient_lab_DMC);
			labNo = extras.getString(IntentKeys.key_patient_lab_No);
			labWeight = extras.getString(IntentKeys.key_patient_lab_Weight);
			labMonth = extras.getString(IntentKeys.kay_patient_lab_month);

			address = extras.getString(IntentKeys.key_patient_address);
			diseaseSite = extras.getString(IntentKeys.key_patient_diseaseSite);
			diseaseClassification = extras
					.getString(IntentKeys.key_patient_disease);
			patientType = extras.getString(IntentKeys.key_patient_Type);
			nikashayId = extras.getString(IntentKeys.key_patient_nikshayId);
			tbNumber = extras.getString(IntentKeys.key_patient_tbNumber);
			smokingHistory = extras
					.getBoolean(IntentKeys.key_patient_smoking_history);
			aadhaarNumber = extras.getString(IntentKeys.key_patient_aadhaarNo);
			patient_Source = extras
					.getString(IntentKeys.key_patient_patientSource);
		}
		long currentTime = System.currentTimeMillis();
		if (oldID == false) {
			if (AppStateConfigurationOperations.updateMaxId(this) != -1) {
				PatientsOperations.addPatient(treatmentId, name,
						Enums.StatusType.getStatusType(StatusType.Active)
								.toString(), phone, tabId, true, currentTime,
						((eComplianceApp) this.getApplication()).LastLoginId,
						false, GenUtils.getCurrentDateLong(), CentersOperations
								.getCenterId(tabId, this), address,
						diseaseSite, diseaseClassification, patientType,
						nikashayId, tbNumber, smokingHistory, this);

				PatientLabFollowUpOperations.addPatientLabFollowUpData(
						treatmentId, labMonth, spinnerInitialLab, labDate,
						labDMC, labNo, labWeight,
						((eComplianceApp) this.getApplication()).LastLoginId,
						tabId, currentTime, false, this);
				int ag = Integer.parseInt(editTxtAge);
				PatientGenderAgeOperations.addGenderAge(treatmentId,
						selectGender, ag, currentTime, false,
						((eComplianceApp) this.getApplication()).LastLoginId,
						this);
				PatientDosePriorOperations.addPatient(treatmentId, IPDose,
						ExtIPDose, CPDose, currentTime, false, this);
				PatientV2_Operations.addPatient(treatmentId, aadhaarNumber,
						patient_Source, currentTime,
						((eComplianceApp) this.getApplication()).LastLoginId,
						false, this);

				if (!ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_is_hiv_enable, this).equals("1")) {
					isHivEnable = false;

					if (isHivEnable) {
						HIVOperation
								.Add(treatmentId,
										"NA",
										hivResult.toString(),
										((eComplianceApp) this.getApplication()).LastLoginId,
										false, System.currentTimeMillis(), this);

					}
					TreatmentInStagesOperations
							.addTreatmentStage(
									treatmentId,
									RegimenMasterOperations
											.getRegimenId(
													Schema.REGIMEN_MASTER_CATEGORY
															+ "= '"
															+ category
															+ "' and "
															+ Schema.REGIMEN_MASTER_STAGE
															+ "='"
															+ stage
															+ "' and "
															+ Schema.REGIMEN_MASTER_SCHEDULE
															+ "= '"
															+ schedule
															+ "'", this),
									GenUtils.getCurrentDateLong(),
									System.currentTimeMillis(),
									((eComplianceApp) this.getApplication()).LastLoginId,
									false, this);

					if (IsInitialLabOn) {
						PatientLabsOperation.addLab(treatmentId,
								InitialLabMasterOperations.getId(
										spinnerInitialLab, this), this);

					}

				}
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_used_device, this).equals("iris")) {
			if (!mIsCameraReady && !mIsPermissionDenied)
				openDevice();

		}
	}

	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 1) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}

		return Bitmap.createScaledBitmap(image, width, height, true);
	}
}
