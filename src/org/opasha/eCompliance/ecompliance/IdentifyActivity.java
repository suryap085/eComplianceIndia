/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Scan_Identification_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IddkCaptureInfo;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.IrisDefault;
import org.opasha.eCompliance.ecompliance.util.IrisUtility;
import org.opasha.eCompliance.ecompliance.util.MediaData;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.futronictech.SDKHelper.FTR_PROGRESS;
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;
import com.futronictech.SDKHelper.IIdentificationCallBack;
import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
import com.futronictech.SDKHelper.VersionCompatible;
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

public class IdentifyActivity extends Activity implements
		IIdentificationCallBack {

	boolean isError = false;
	boolean isClosing = false;
	boolean isScanOn = false;
	ImageView imgScan;
	TextView lblHeader;
	TextView lblText;
	LinearLayout layout;
	private UsbDeviceDataExchangeImpl usb_host_ctx = null;
	private FutronicSdkBase m_Operation;
	public static final int MESSAGE_SHOW_MSG = 1;
	public static final int MESSAGE_SHOW_IMAGE = 2;
	public static final int MESSAGE_COMPLETE = 3;
	public static final int MESSAGE_SHOW_HEADER = 4;
	public static final int MESSAGE_SHOW_GREEN = 5;
	public static final int MESSAGE_SHOW_RED = 6;
	public static final int MESSAGE_IDENTIFICATION = 7;
	private static Bitmap mBitmapFP = null;
	ArrayList<ScanModel> scans;

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

	private enum eIdentifyResult {
		IRI_IDENTIFY_DIFFERENT, IRI_IDENTIFY_LOOKLIKE, IRI_IDENTIFY_SAME, IRI_IDENTIFY_DUPLICATED
	};

	private int minIrisQuality;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identify_fp);
		mApis = Iddk2000Apis.getInstance(this);
		minIrisQuality = ((eComplianceApp) this.getApplication()).IrisRegQuality;
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		imgScan = (ImageView) findViewById(R.id.imgIdentFpImg);
		lblHeader = (TextView) findViewById(R.id.lblIdentHeader);
		lblText = (TextView) findViewById(R.id.lblIdentText);
		layout = (LinearLayout) findViewById(R.id.layoutHome);

		layout.setBackgroundResource(R.drawable.grey_to_red_transition);

		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_used_device, this).equals("iris")) {
			scans = ScansIrisOperations.getScans(false, this);
			if (scans.size() > 0) {
				scanEye();
				mHandler.obtainMessage(IRIS_ENROLL).sendToTarget();
			}
		} else {
			scans = ScansOperations.getScans(false, this);
			if (scans.size() > 0) {
				usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
				scanFinger();
			}
		}
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
		super.onDestroy();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
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
				imgScan.setImageBitmap(getResizedBitmap(mBitmapFP, 400));
				break;
			case MESSAGE_SHOW_HEADER:
				String showheader = (String) msg.obj;
				lblHeader.setText(showheader);
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
				String treatmentId = (String) msg.obj;

				if (!PatientsOperations.patientExists(treatmentId,
						IdentifyActivity.this)) {
					if (VisitorsOperations.visitorExists(treatmentId,
							IdentifyActivity.this)) {
						CloseScanner();
						Intent editVisitorintent = new Intent(
								IdentifyActivity.this,
								EditVisitorActivity.class);
						editVisitorintent.putExtra(IntentKeys.key_treatment_id,
								treatmentId);
						IdentifyActivity.this.finish();
						startActivity(editVisitorintent);
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
					} else {
						GoToHome(
								getResources().getString(
										R.string.visitorOrPatientNotExist),
								Signal.Bad);
					}
				} else if (PatientsOperations.getPatientDetails(treatmentId,
						IdentifyActivity.this).Status.equals(Enums.StatusType
						.getStatusType(StatusType.Default).toString())) {
					GoToHome(
							getResources()
									.getString(R.string.defaultCanNotEdit),
							Signal.Bad);
				} else if (PatientsOperations.getPatientDetails(treatmentId,
						IdentifyActivity.this).Status.equals(Enums.StatusType
						.getStatusType(StatusType.TransferredInternally)
						.toString())) {
					GoToHome(
							getResources().getString(
									R.string.transferInternallyCanNotEdit),
							Signal.Bad);
				} else {
					CloseScanner();
					Intent editPatientIntent = new Intent(
							IdentifyActivity.this, EditPatientActivity.class);
					editPatientIntent.putExtra(IntentKeys.key_treatment_id,
							treatmentId);
					editPatientIntent.putExtra(IntentKeys.key_new_patient,
							false);
					editPatientIntent.putExtra(IntentKeys.key_intent_from,
							Enums.IntentFrom.Home);
					IdentifyActivity.this.finish();
					startActivity(editPatientIntent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
				}
				break;
			}
		}
	};

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
				mHandler.obtainMessage(MESSAGE_IDENTIFICATION, -1, -1,
						enrollId.toString()).sendToTarget();
			} else if (eIdentifyResult.IRI_IDENTIFY_DIFFERENT == identifyResult) {

				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
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
						R.string.canNotStartIdentificationOperation)
						+ "\n"
						+ getResources().getString(R.string.errorDescription)
						+ e.getMessage());
			}
		} else {
			if (usb_host_ctx.IsPendingOpen()) {
				// mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			} else {
				// mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
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

	public void onHavingTroubleClick(View v) {
		CloseScanner();
		Intent intent = new Intent(this, EnterIdActivity.class);
		intent.putExtra(IntentKeys.key_visitor_type,
				VisitorType.Patient.toString());
		intent.putExtra(IntentKeys.key_verification_reason,
				getString(R.string.EditVisitor));
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
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
		try {
			if (isClosing)
				return;
			if (bSuccess) {

				if (scans.size() > 0) {

					IdentificationResultCustom idenResult = GenUtils.Identify(
							m_Operation, scans, nResult);
					if (idenResult.nResult == FutronicSdkBase.RETCODE_OK) {

						if (idenResult.foundIndex != -1) {
							mHandler.obtainMessage(
									MESSAGE_IDENTIFICATION,
									-1,
									-1,
									scans.get(idenResult.foundIndex).treatmentId)
									.sendToTarget();
							Scan_Identification_Operations
									.addScanIdentification(
											CentersOperations.getTabId(this),
											"Fingerprint",
											scans.get(idenResult.foundIndex).treatmentId,
											true, System.currentTimeMillis(),
											this);
						} else {
							mHandler.obtainMessage(
									MESSAGE_SHOW_HEADER,
									-1,
									-1,
									getResources()
											.getString(
													R.string.identificationCompleteNotFound))
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							Scan_Identification_Operations
									.addScanIdentification(
											CentersOperations.getTabId(this),
											"Fingerprint", "", false,
											System.currentTimeMillis(), this);
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

	public void onScanClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, IdentifyActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		GoToHome("", Signal.Good);
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

	private void GoToHome(String message, Signal signal) {
		CloseScanner();
		Intent intent = new Intent(this, HomeActivity.class);
		if (!message.isEmpty()) {
			intent.putExtra(IntentKeys.key_message_home, message);
			intent.putExtra(IntentKeys.key_signal_type, signal.toString());
		}
		startActivity(intent);
		this.finish();
	}

	private void CloseScanner() {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
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
