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
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
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

import com.futronictech.SDKHelper.FTR_PROGRESS;
import com.futronictech.SDKHelper.FutronicEnrollment;
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;
import com.futronictech.SDKHelper.IEnrollmentCallBack;
import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
import com.futronictech.SDKHelper.VersionCompatible;

/**
 * @author abhishek
 * 
 */
public class EnrollActivity extends Activity implements IEnrollmentCallBack {

	private ImageView imgScan;
	private TextView lblStatus;
	private TextView lblHeader;
	LinearLayout layout;
	TextView showId;
	ImageView imgStatic;
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

	private UsbDeviceDataExchangeImpl usb_host_ctx = null;

	private FutronicSdkBase m_Operation;

	public static final int MESSAGE_SHOW_MSG = 1;
	public static final int MESSAGE_SHOW_IMAGE = 2;
	public static final int MESSAGE_ENABLE_CONTROLS = 3;
	public static final int MESSAGE_COMPLETE = 4;
	public static final int MESSAGE_SHOW_HEADER = 5;
	public static final int MESSAGE_CANCELED = 6;
	public static final int MESSAGE_SHOW_GREEN = 7;
	public static final int MESSAGE_SHOW_RED = 8;
	public static final int MESSAGE_SHOW_LEFT_INDEX = 9;
	public static final int MESSAGE_SHOW_RIGHT_INDEX = 10;
	public static final int MESSAGE_SAVE_HEADER = 11;
	public static final int MESSAGE_IDENTIFICATION = 12;

	private static Bitmap mBitmapFP = null;

	private static int regType = 1;
	private static int fingerCount = 0;

	private static boolean isLastError = false;
	private static String savedHeader = "";

	public static boolean isMoit = true;
	private String IPDose, ExtIPDose, CPDose;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_fp);
		Bundle extras = getIntent().getExtras();
		// Return to main screen if no extras are sent.
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

		try {
			isMoit = (Boolean) extras.get(IntentKeys.key_moit_status);
		} catch (Exception e) {
			isMoit = true;
		}

		try {
			fingerCount = extras
					.getInt(IntentKeys.key_enrollment_current_finger);
		} catch (Exception e) {
		}
		// Back if treatment Id is empty
		if (treatmentId.isEmpty()) {
			this.finish();
			return;
		}
		startEnrollmentActivity();
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		// SaveVisitorsDetail();
	}

	@Override
	public void onDestroy() {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	private void startEnrollmentActivity() {
		imgScan = (ImageView) findViewById(R.id.imgRegFpImg);
		imgStatic = (ImageView) findViewById(R.id.imgStatic);
		lblStatus = (TextView) findViewById(R.id.lblRegScanText);
		lblHeader = (TextView) findViewById(R.id.lblRegHeader);
		layout = (LinearLayout) findViewById(R.id.layout_enroll_fp);
		showId = (TextView) findViewById(R.id.showId);
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		// Finger print Scan
		usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
		showId.setText(getResources().getString(R.string.id) + " "
				+ treatmentId);
		showId.setTypeface(null, Typeface.BOLD);
		switch (visitorType) {
		case Patient:
			RegisterPatient();
			break;
		case CDP:
			RegisterOthers();
			break;
		case Counselor:
			RegisterCounselor();
			break;
		case PM:
			// RegisterOthers();
			RegisterCounselor();
			break;
		case QualityAuditor:
			RegisterOthers();
			break;
		default:
			RegisterOthers();
			break;
		}
	}

	// --------------------
	// Finger Print Related
	// --------------------
	public void callReg() {
		switch (regType) {
		case 1:
		case 3:
			if (fingerCount == 0) {
				mHandler.obtainMessage(MESSAGE_SHOW_RIGHT_INDEX).sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.primaryFinger))
						.sendToTarget();
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_LEFT_INDEX).sendToTarget();
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.secondaryFinger))
						.sendToTarget();
			}
			scanFinger();

			break;

		default:
			switch (fingerCount) {
			case 0:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.rightThumb))
						.sendToTarget();
				break;
			case 1:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.rightIndexFinger))
						.sendToTarget();
				break;
			case 2:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.rightMiddleFinger))
						.sendToTarget();
				break;
			case 3:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.rightRingFinger))
						.sendToTarget();
				break;
			case 4:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.rightPinky))
						.sendToTarget();
				break;
			case 5:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.leftThumb))
						.sendToTarget();
				break;
			case 6:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.leftIndexFinger))
						.sendToTarget();
				break;
			case 7:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.leftMiddleFinger))
						.sendToTarget();
				break;
			case 8:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.leftRingFinger))
						.sendToTarget();
				break;
			default:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						getResources().getString(R.string.leftPinky))
						.sendToTarget();
				break;
			}
			scanFinger();
			break;
		}

	}

	public void callReg(String message, boolean isError) {
		isLastError = isError;
		mHandler.obtainMessage(MESSAGE_SAVE_HEADER).sendToTarget();
		switch (regType) {
		case 1:
		case 3:
			if (fingerCount == 0) {
				mHandler.obtainMessage(MESSAGE_SHOW_RIGHT_INDEX).sendToTarget();
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.primaryFinger)).sendToTarget();
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_LEFT_INDEX).sendToTarget();
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.secondaryFinger))
						.sendToTarget();
			}
			if (isError) {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			}
			scanFinger();
			break;

		default:
			switch (fingerCount) {
			case 0:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						message + getResources().getString(R.string.rightThumb))
						.sendToTarget();
				break;
			case 1:
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.rightIndexFinger))
						.sendToTarget();
				break;
			case 2:
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.rightMiddleFinger))
						.sendToTarget();
				break;
			case 3:
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.rightRingFinger))
						.sendToTarget();
				break;
			case 4:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						message + getResources().getString(R.string.rightPinky))
						.sendToTarget();
				break;
			case 5:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						message + getResources().getString(R.string.leftThumb))
						.sendToTarget();
				break;
			case 6:
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.leftIndexFinger))
						.sendToTarget();
				break;
			case 7:
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.leftMiddleFinger))
						.sendToTarget();
				break;
			case 8:
				mHandler.obtainMessage(
						MESSAGE_SHOW_HEADER,
						-1,
						-1,
						message
								+ getResources().getString(
										R.string.leftRingFinger))
						.sendToTarget();
				break;
			default:
				mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
						message + getResources().getString(R.string.leftPinky))
						.sendToTarget();
				break;
			}
			scanFinger();
			break;
		}

	}

	private void scanFinger() {
		if (isClosing)
			return;

		isScanOn = false;
		if (usb_host_ctx.OpenDevice(0, true)) {
			try {
				if (!usb_host_ctx.ValidateContext()) {
					throw new Exception("Can't open USB device");
				}
				m_Operation = new FutronicEnrollment((Object) usb_host_ctx);
				// Set control properties
				m_Operation.setFakeDetection(false);
				m_Operation.setFFDControl(true);
				m_Operation.setFARN(((eComplianceApp) this
						.getApplicationContext()).farValue);
				((FutronicEnrollment) m_Operation).setMIOTControlOff(isMoit);
				((FutronicEnrollment) m_Operation)
						.setMaxModels(((eComplianceApp) this
								.getApplicationContext()).maxModel);
				m_Operation.setVersion(VersionCompatible.ftr_version_current);
				((FutronicEnrollment) m_Operation).Enrollment(this);
			} catch (Exception e) {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblStatus.setText(getResources().getString(
						R.string.canNotStartEnrollmentOperation)
						+ "\n"
						+ getResources().getString(R.string.errorDescription)
						+ e.getMessage());
				usb_host_ctx.CloseDevice();
			}
		} else {
			if (!usb_host_ctx.IsPendingOpen()) {
				lblStatus.setText(getResources().getString(
						R.string.canNotStartEnrollmentOperation)
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

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isClosing)
				return;
			switch (msg.what) {
			case MESSAGE_IDENTIFICATION:
				treatmentId = (String) msg.obj;
				String[] id = treatmentId.split(",");
				DbUtils.hardDeletePatient(id[0], EnrollActivity.this);
				treatmentId = id[1];
				if (!PatientsOperations.patientExists(treatmentId,
						EnrollActivity.this)) {
					if (VisitorsOperations.visitorExists(treatmentId,
							EnrollActivity.this)) {
						CloseScanner();
						Intent editVisitorintent = new Intent(
								EnrollActivity.this, EditVisitorActivity.class);
						editVisitorintent.putExtra(IntentKeys.key_treatment_id,
								treatmentId);
						EnrollActivity.this.finish();
						startActivity(editVisitorintent);
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
					} else {
						GoToHome(getResources()
								.getString(R.string.scanmismatch), Signal.Bad);
					}
				} else if (PatientsOperations.getPatientDetails(treatmentId,
						EnrollActivity.this).Status.equals(Enums.StatusType
						.getStatusType(StatusType.Default).toString())) {
					GoToHome(
							getResources()
									.getString(R.string.defaultCanNotEdit),
							Signal.Bad);
				} else if (PatientsOperations.getPatientDetails(treatmentId,
						EnrollActivity.this).Status.equals(Enums.StatusType
						.getStatusType(StatusType.TransferredInternally)
						.toString())) {
					GoToHome(
							getResources().getString(
									R.string.transferInternallyCanNotEdit),
							Signal.Bad);
				} else {
					CloseScanner();
					Intent editPatientIntent = new Intent(EnrollActivity.this,
							EditPatientActivity.class);
					editPatientIntent.putExtra(IntentKeys.key_treatment_id,
							treatmentId);
					editPatientIntent.putExtra(IntentKeys.key_new_patient,
							false);
					editPatientIntent.putExtra(IntentKeys.key_intent_from,
							Enums.IntentFrom.Home);
					EnrollActivity.this.finish();
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
				imgScan.setImageBitmap(mBitmapFP);
				break;
			case MESSAGE_COMPLETE:
				isClosing = true;
				try {
					usb_host_ctx.CloseDevice();
				} catch (Exception e) {
				}
				try {
					PositiveContactsOperations.updateTreatmentId(treatmentId,
							contactId, EnrollActivity.this);
				} catch (Exception e) {

				}
				if (visitorType == VisitorType.Patient) {
					SaveDetails();
				} else {
					SaveVisitorsDetail();
				}
				Intent intent = new Intent(EnrollActivity.this,
						HomeActivity.class);

				intent.putExtra(IntentKeys.key_message_home,
						VisitorType.GetViewString(visitorType) + "("
								+ treatmentId + ") "
								+ getResources().getString(R.string.registered));
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Good.toString());
				startActivity(intent);
				EnrollActivity.this.finish();
				break;
			case MESSAGE_CANCELED:
				// *AS Delete data from database if registration is cancelled.
				if (reptype != null) {
					if (!(reptype
							.equals(Enums.ReportType.PatientsFromLegacySystem) || reptype
							.equals(Enums.ReportType.VisitorReregistration))) {
						if (visitorType == VisitorType.Patient) {
							PatientsOperations.deletePatientHard(treatmentId,
									EnrollActivity.this);
							TreatmentInStagesOperations.deletePatientHard(
									treatmentId, EnrollActivity.this);
							PatientLabsOperation.labHardDelete(treatmentId,
									EnrollActivity.this);
							LocationOperations.delete(EnrollActivity.this,
									treatmentId);
						} else {
							VisitorsOperations.deleteVisitorHard(treatmentId,
									EnrollActivity.this);
						}
					}
				} else {
					if (visitorType == VisitorType.Patient) {
						PatientsOperations.deletePatientHard(treatmentId,
								EnrollActivity.this);
						TreatmentInStagesOperations.deletePatientHard(
								treatmentId, EnrollActivity.this);
						PatientLabsOperation.labHardDelete(treatmentId,
								EnrollActivity.this);
						LocationOperations.delete(EnrollActivity.this,
								treatmentId);
					} else {
						VisitorsOperations.deleteVisitorHard(treatmentId,
								EnrollActivity.this);
					}
				}
				ScansOperations.deleteScans(EnrollActivity.this, treatmentId);
				isClosing = true;
				try {
					usb_host_ctx.CloseDevice();
				} catch (Exception e) {
				}
				intent = new Intent(EnrollActivity.this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, getResources()
						.getString(R.string.registrationCanceled));
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Bad.toString());
				startActivity(intent);
				EnrollActivity.this.finish();
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
			case MESSAGE_SHOW_LEFT_INDEX:
				imgStatic.setImageResource(R.drawable.left_index_patient);
				break;
			case MESSAGE_SHOW_RIGHT_INDEX:
				imgStatic.setImageResource(R.drawable.right_index_patient);
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

	@Override
	public boolean OnFakeSource(FTR_PROGRESS arg0) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "Fake source detected")
				.sendToTarget();
		return false;
	}

	protected void SaveVisitorsDetail() {
		Bundle extras = getIntent().getExtras();
		boolean auth = false;
		if (extras != null) {
			status = extras.getString(IntentKeys.key_visitor_type);
			type = extras.getString(IntentKeys.key_visitor_type);
			name = extras.getString(IntentKeys.key_visitor_name);
			phone = extras.getString(IntentKeys.key_phone_no);
			treatmentId = extras.getString(IntentKeys.key_treatment_id);
			auth = Boolean.getBoolean(extras
					.getString(IntentKeys.key_is_Authenticate));
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
	public void OnPutOn(FTR_PROGRESS arg0) {
		String message = getResources().getString(R.string.scanYourFinger);
		if (arg0.m_Count > 1) {
			message = getResources().getString(R.string.ScanFinger) + " "
					+ (arg0.m_Total - arg0.m_Count + 1) + " "
					+ getResources().getString(R.string.moreTime);
		}
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, message)
				.sendToTarget();

	}

	@Override
	public void OnTakeOff(FTR_PROGRESS arg0) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1,
				getResources().getString(R.string.takeFingerOff) + "...")
				.sendToTarget();
		if (isLastError) {
			isLastError = false;
			ResetHeader();
		}
	}

	@Override
	public void UpdateScreenImage(Bitmap Image) {
		mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
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
	public void OnEnrollmentComplete(boolean bSuccess, int nResult) {
		if (isClosing)
			return;
		try {
			if (bSuccess) {
				boolean isScanExists = false;

				ArrayList<ScanModel> scans = ScansOperations.getScans(true,
						this);
				FutronicSdkBase m_Identify = new FutronicIdentification(
						(Object) usb_host_ctx);
				if (scans.size() > 0) {
					m_Identify.setFakeDetection(false);
					m_Identify.setFFDControl(true);
					m_Identify.setFARN(((eComplianceApp) this
							.getApplicationContext()).farValue);
					m_Identify
							.setVersion(VersionCompatible.ftr_version_current);

				}
				int scanQuality = ((FutronicEnrollment) m_Operation)
						.getQuality();
				Logger.LogToDb(this, "Registration Scan Qulaity",
						"Scan Quality: " + scanQuality + ", TREATMENT_ID="
								+ treatmentId);
				if (scanQuality >= ((eComplianceApp) this
						.getApplicationContext()).tempQualityBenchmark) {
					try {
						Logger.LogToDb(this, "Enroll Fingerprint", "Quality: "
								+ scanQuality + ", Treatment Id: "
								+ treatmentId);
					} catch (Exception e2) {
					}
					byte[] scan = ((FutronicEnrollment) m_Operation)
							.getTemplate();
					if (scans.size() > 0) {
						((FutronicIdentification) m_Identify)
								.setBaseTemplate(scan);
						IdentificationResultCustom idenResult = GenUtils
								.Identify(m_Identify, scans, nResult);

						if (idenResult.nResult == FutronicSdkBase.RETCODE_OK) {
							if (idenResult.foundIndex != -1) {
								isScanExists = true;
								mHandler.obtainMessage(
										MESSAGE_IDENTIFICATION,
										-1,
										-1,
										treatmentId
												+ ","
												+ scans.get(idenResult.foundIndex).treatmentId)
										.sendToTarget();
								return;

							}
						}
					}
					if (!isScanExists) {
						// set status string
						mHandler.obtainMessage(
								MESSAGE_SHOW_MSG,
								-1,
								-1,
								getResources()
										.getString(
												R.string.enrollmentFinishedSuccessfully)
										+ " "
										+ ((FutronicEnrollment) m_Operation)
												.getQuality()).sendToTarget();

						ScansOperations
								.addScan(
										treatmentId,
										visitorType.toString(),
										scan,
										System.currentTimeMillis(),
										((eComplianceApp) this.getApplication()).LastLoginId,
										this);
						fingerCount++;
					} else {

					}
				} else {
					if (((eComplianceApp) this.getApplicationContext()).tempQualityBenchmark > 3)
						((eComplianceApp) this.getApplicationContext()).tempQualityBenchmark -= 2;
				}
				if (regType != 2) {
					if (fingerCount >= 2) {
						mHandler.obtainMessage(MESSAGE_COMPLETE).sendToTarget();
					} else {
						if (isScanExists) {
							callReg(getResources().getString(
									R.string.scanAlreadyExists), true);
						} else
							callReg();
					}
				} else {
					if (fingerCount >= 10) {
						mHandler.obtainMessage(MESSAGE_COMPLETE).sendToTarget();
					} else {
						if (isScanExists) {
							callReg(getResources().getString(
									R.string.scanAlreadyExists), true);
						} else
							callReg();
					}
				}
				mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();

			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				mHandler.obtainMessage(
						MESSAGE_SHOW_MSG,
						-1,
						-1,
						"Enrollment failed. Error description: "
								+ FutronicSdkBase.SdkRetCode2Message(nResult))
						.sendToTarget();
				m_Operation = null;
			}
		} catch (Exception e) {
			Logger.e(this, "OnEnrollmentComplete", "Error: " + e.getMessage()
					+ e.toString());
		}
	}

	public void RegisterPatient() {
		regType = 1;
		callReg();
	}

	public void RegisterCounselor() {
		regType = 2;
		callReg();
	}

	public void RegisterOthers() {
		regType = 3;
		callReg();
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
		Bundle extras = getIntent().getExtras();
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, EnrollActivity.class);
		intent.putExtra(IntentKeys.key_treatment_id, treatmentId);
		intent.putExtra(IntentKeys.key_visitor_type, visitorType.toString());
		intent.putExtra(IntentKeys.key_enrollment_current_finger, fingerCount);
		intent.putExtra(IntentKeys.key_intent_from, reptype);
		try {
			if (visitorType == VisitorType.Patient) {
				intent.putExtra(IntentKeys.key_moit_status,
						extras.getString(IntentKeys.key_moit_status));
				intent.putExtra(IntentKeys.key_patient_Name,
						extras.getString(IntentKeys.key_patient_Name));
				intent.putExtra(IntentKeys.key_phone_no,
						extras.getString(IntentKeys.key_phone_no));
				intent.putExtra(IntentKeys.key_patient_gender,
						extras.getString(IntentKeys.key_patient_gender));
				intent.putExtra(IntentKeys.key_patient_initial_lab,
						extras.getString(IntentKeys.key_patient_initial_lab));
				intent.putExtra(IntentKeys.key_patient_lab_Date,
						extras.getLong(IntentKeys.key_patient_lab_Date));
				intent.putExtra(IntentKeys.kay_patient_lab_month,
						extras.getString(IntentKeys.kay_patient_lab_month));
				intent.putExtra(IntentKeys.key_patient_lab_DMC,
						extras.getString(IntentKeys.key_patient_lab_DMC));
				intent.putExtra(IntentKeys.key_patient_lab_No,
						extras.getString(IntentKeys.key_patient_lab_No));
				intent.putExtra(IntentKeys.key_patient_lab_Weight,
						extras.getString(IntentKeys.key_patient_lab_Weight));
				intent.putExtra(IntentKeys.key_current_stage,
						extras.getString(IntentKeys.key_current_stage));
				intent.putExtra(IntentKeys.key_current_schedule,
						extras.getString(IntentKeys.key_current_schedule));
				intent.putExtra(IntentKeys.key_current_category,
						extras.getString(IntentKeys.key_current_category));
				intent.putExtra(IntentKeys.key_hiv_screening_result,
						extras.getString(IntentKeys.key_hiv_screening_result));
				intent.putExtra(IntentKeys.key_patient_age,
						extras.getString(IntentKeys.key_patient_age));
				intent.putExtra(IntentKeys.key_tab_id,
						extras.getString(IntentKeys.key_tab_id));
				intent.putExtra(IntentKeys.key_treatment_id,
						extras.getString(IntentKeys.key_treatment_id));
				intent.putExtra(IntentKeys.key_patient_prior_IPdose,
						extras.getString(IntentKeys.key_patient_prior_IPdose));
				intent.putExtra(IntentKeys.key_patient_prior_CPdose,
						extras.getString(IntentKeys.key_patient_prior_CPdose));
				intent.putExtra(IntentKeys.key_patient_prior_ExtIPdose, extras
						.getString(IntentKeys.key_patient_prior_ExtIPdose));

				intent.putExtra(IntentKeys.key_patient_address,
						extras.getString(IntentKeys.key_patient_address));
				intent.putExtra(IntentKeys.key_patient_diseaseSite,
						extras.getString(IntentKeys.key_patient_diseaseSite));
				intent.putExtra(IntentKeys.key_patient_disease,
						extras.getString(IntentKeys.key_patient_disease));
				intent.putExtra(IntentKeys.key_patient_Type,
						extras.getString(IntentKeys.key_patient_Type));
				intent.putExtra(IntentKeys.key_patient_nikshayId,
						extras.getString(IntentKeys.key_patient_nikshayId));
				intent.putExtra(IntentKeys.key_patient_tbNumber,
						extras.getString(IntentKeys.key_patient_tbNumber));
				intent.putExtra(IntentKeys.key_patient_smoking_history, extras
						.getString(IntentKeys.key_patient_smoking_history));
				intent.putExtra(IntentKeys.key_patient_aadhaarNo,
						extras.getString(IntentKeys.key_patient_aadhaarNo));
				intent.putExtra(IntentKeys.key_patient_patientSource,
						extras.getString(IntentKeys.key_patient_patientSource));

			}
		} catch (Exception e) {
		}
		try {
			if (visitorType != visitorType.Patient) {
				intent.putExtra(IntentKeys.key_visitor_type,
						extras.getString(IntentKeys.key_visitor_type));

				intent.putExtra(IntentKeys.key_visitor_name,
						extras.getString(IntentKeys.key_visitor_name));
				intent.putExtra(IntentKeys.key_phone_no,
						extras.getString(IntentKeys.key_phone_no));
				intent.putExtra(IntentKeys.key_treatment_id,
						extras.getString(IntentKeys.key_treatment_id));
				intent.putExtra(IntentKeys.key_is_Authenticate,
						extras.getString(IntentKeys.key_is_Authenticate));
				intent.putExtra(IntentKeys.key_machine_id,
						extras.getString(IntentKeys.key_machine_id));
			}
		} catch (Exception e) {
		}
		try {
			intent.putExtra(IntentKeys.key_contact_id, getIntent().getExtras()
					.getString(IntentKeys.key_contact_id));
		} catch (Exception e) {
		}
		this.finish();
		startActivity(intent);
	}

	public void ResetHeader() {
		mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1, savedHeader)
				.sendToTarget();
	}

	private void CloseScanner() {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
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
}
