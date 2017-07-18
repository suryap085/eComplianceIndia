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

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.HomeActivity;
import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IconMessages;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.Enums.finger;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
import com.google.gson.Gson;

/**
 * @author abhishek
 * 
 */
public class EnrollActivityTextFree extends Activity implements
		IEnrollmentCallBack {

	private ImageView imgScan;
	private TextView lblStatusOne, lblStatusTwo, lblStatusThree, lblStatusFour;
	private TextView lblHeader;
	LinearLayout layout, scanCountShow;
	TextView showId;
	ImageView imgStaticOne, imgStaticTwo;
	private static String treatmentId = "";
	private static VisitorType visitorType;
	private Enums.ReportType reptype;

	// --------------------
	// Finger Print Related
	// --------------------

	boolean isClosing = false;
	boolean isScanOn = false;
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
	public static final int MESSAGE_TOTAL_COUNT = 12;
	public static final int MESSAGE_SHOW_ICON_MESSAGE = 13;
	public static final int MESSAGE_IDENTIFICATION = 14;
	public static final int MESSAGE_FINGER_ICON_MESSAGE = 15;

	private static Bitmap mBitmapFP = null;

	private static int regType = 1;
	private int fingerCount = 0;

	private static boolean isLastError = false;
	private static String savedHeader = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_fp_text_free);
		Bundle extras = getIntent().getExtras();
		// Return to main screen if no extras are sent.
		/*
		 * if (extras == null) { this.finish(); return; }
		 */

		visitorType = (VisitorType) extras.get(IntentKeys.key_visitor_type);
		treatmentId = CentersOperations.getTabId(this)
				+ (AppStateConfigurationOperations.getMaxId(this) + 1);
		// Intent intent = new Intent(
		// EnrollActivityTextFree.this,
		// org.opasha.eCompliance.ecompliance.TextFree.SelectImageActivity.class);
		//
		// intent.putExtra(IntentKeys.key_treatment_id, treatmentId);
		// startActivity(intent);
		// EnrollActivityTextFree.this.finish();
		try {
			reptype = (ReportType) extras.get(IntentKeys.key_intent_from);
		} catch (Exception e) {
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
	}

	@Override
	public void onDestroy() {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private void startEnrollmentActivity() {
		imgScan = (ImageView) findViewById(R.id.imgRegFpImg);
		imgStaticOne = (ImageView) findViewById(R.id.imgStaticOne);
		imgStaticTwo = (ImageView) findViewById(R.id.imgStaticTwo);
		lblStatusOne = (TextView) findViewById(R.id.lblRegScanText1);
		lblStatusTwo = (TextView) findViewById(R.id.lblRegScanText2);
		lblStatusThree = (TextView) findViewById(R.id.lblRegScanText3);
		lblStatusFour = (TextView) findViewById(R.id.lblRegScanText4);
		lblHeader = (TextView) findViewById(R.id.lblRegHeader);
		scanCountShow = (LinearLayout) findViewById(R.id.scanCountShow);
		layout = (LinearLayout) findViewById(R.id.layout_enroll_fp);
		showId = (TextView) findViewById(R.id.showId);
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		// Finger print Scan
		usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
		lblHeader.setVisibility(View.GONE);
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
			RegisterOthers();
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
				// mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
				// getResources().getString(R.string.primaryFinger))
				// .sendToTarget();
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_LEFT_INDEX).sendToTarget();
				// mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
				// getResources().getString(R.string.secondaryFinger))
				// .sendToTarget();
			}
			scanFinger();
			break;

		default:
			switch (fingerCount) {
			case 0:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightThumb).sendToTarget();
				break;
			case 1:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightIndexFinger).sendToTarget();
				break;
			case 2:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightMiddleFinger).sendToTarget();
				break;
			case 3:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightRingFinger).sendToTarget();
				break;
			case 4:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightPinky).sendToTarget();
				break;
			case 5:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftThumb).sendToTarget();
				break;
			case 6:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftIndexFinger).sendToTarget();
				break;
			case 7:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftMiddleFinger).sendToTarget();
				break;
			case 8:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftRingFinger).sendToTarget();
				break;
			default:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftPinky).sendToTarget();
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
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightThumb).sendToTarget();
				break;
			case 1:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightIndexFinger).sendToTarget();
				break;
			case 2:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightMiddleFinger).sendToTarget();
				break;
			case 3:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightRingFinger).sendToTarget();
				break;
			case 4:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.rightPinky).sendToTarget();
				break;
			case 5:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftThumb).sendToTarget();
				break;
			case 6:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftIndexFinger).sendToTarget();
				break;
			case 7:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftMiddleFinger).sendToTarget();
				break;
			case 8:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftRingFinger).sendToTarget();
				break;
			default:
				mHandler.obtainMessage(MESSAGE_FINGER_ICON_MESSAGE, -1, -1,
						Enums.finger.leftPinky).sendToTarget();
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
				((FutronicEnrollment) m_Operation).setMIOTControlOff(true);
				((FutronicEnrollment) m_Operation).setMaxModels(4);

				m_Operation.setVersion(VersionCompatible.ftr_version_current);
				((FutronicEnrollment) m_Operation).Enrollment(this);
			} catch (Exception e) {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblStatusOne
						.setBackgroundResource(R.drawable.futronic_disabled);
				usb_host_ctx.CloseDevice();
			}
		} else {
			if (!usb_host_ctx.IsPendingOpen()) {
				lblStatusOne
						.setBackgroundResource(R.drawable.futronic_disabled);
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
			case MESSAGE_SHOW_MSG:
				String showMsg = (String) msg.obj;
				// TODO: fix this
				// lblStatusOne.setText(showMsg);
				break;
			case MESSAGE_FINGER_ICON_MESSAGE:
				Enums.finger fin = (finger) msg.obj;
				LinearLayout.LayoutParams lvd = new LinearLayout.LayoutParams(
						90, 90);
				lvd.setMargins(0, 0, 50, 0);
				imgStaticOne.setLayoutParams(lvd);
				imgStaticTwo.setLayoutParams(new LinearLayout.LayoutParams(70,
						70));
				switch (fin) {
				case rightThumb:
					imgStaticOne.setImageResource(R.drawable.left_hand_alpha);
					imgStaticTwo.setImageResource(R.drawable.right_thumb);
					break;
				case rightIndexFinger:
					imgStaticOne.setImageResource(R.drawable.left_hand_alpha);
					imgStaticTwo.setImageResource(R.drawable.right_index);
					break;
				case rightMiddleFinger:
					imgStaticOne.setImageResource(R.drawable.left_hand_alpha);
					imgStaticTwo.setImageResource(R.drawable.right_middle);
					break;
				case rightRingFinger:
					imgStaticOne.setImageResource(R.drawable.left_hand_alpha);
					imgStaticTwo.setImageResource(R.drawable.right_ring);
					break;
				case rightPinky:
					imgStaticOne.setImageResource(R.drawable.left_hand_alpha);
					imgStaticTwo.setImageResource(R.drawable.right_pinky);
					break;
				case leftThumb:
					imgStaticTwo.setImageResource(R.drawable.right_hand_alpha);
					imgStaticOne.setImageResource(R.drawable.left_thumb);
					break;
				case leftIndexFinger:
					imgStaticOne.setImageResource(R.drawable.left_index);
					imgStaticTwo.setImageResource(R.drawable.right_hand_alpha);
					break;
				case leftMiddleFinger:
					imgStaticTwo.setImageResource(R.drawable.right_hand_alpha);
					imgStaticOne.setImageResource(R.drawable.left_middle);
					break;
				case leftRingFinger:
					imgStaticOne.setImageResource(R.drawable.left_ring);
					imgStaticTwo.setImageResource(R.drawable.right_hand_alpha);
					break;
				case leftPinky:
					imgStaticOne.setImageResource(R.drawable.left_pinky);
					imgStaticTwo.setImageResource(R.drawable.right_hand_alpha);
					break;
				default:
					break;
				}
			case MESSAGE_SHOW_IMAGE:
				imgScan.setImageBitmap(mBitmapFP);
				break;
			case MESSAGE_COMPLETE:
				isClosing = true;
				try {
					usb_host_ctx.CloseDevice();
				} catch (Exception e) {
				}
				if (visitorType == VisitorType.Patient) {
					PatientDetailsIntent det = new PatientDetailsIntent();
					Intent intent = new Intent();
					if ((DbUtils.getTabType(EnrollActivityTextFree.this)
							.equals("P"))) {
						det.center = CentersOperations.getCenterName(
								DbUtils.getTabId(EnrollActivityTextFree.this),
								EnrollActivityTextFree.this);

						intent = new Intent(
								EnrollActivityTextFree.this,
								org.opasha.eCompliance.ecompliance.TextFree.SelectImageActivity.class);
					} else {
						intent = new Intent(
								EnrollActivityTextFree.this,
								org.opasha.eCompliance.ecompliance.TextFree.SelectCenterActivity.class);
					}
					det.treatmentId = treatmentId;
					det.intentFrom = Enums.IntentFrom.Home;
					det.backIntent.add(backIntent.scan);
					intent.putExtra(IntentKeys.key_petient_details,
							new Gson().toJson(det));
					startActivity(intent);
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);
					EnrollActivityTextFree.this.finish();
				} else {
					VisitorsOperations.addVisitor(treatmentId, "", visitorType
							.toString(), GenUtils.getCurrentDateLong(),
							"Active", "", false, System.currentTimeMillis(),
							((eComplianceApp) EnrollActivityTextFree.this
									.getApplication()).LastLoginId, false,
							DbUtils.getTabId(EnrollActivityTextFree.this),
							EnrollActivityTextFree.this);
					AppStateConfigurationOperations
							.updateMaxId(EnrollActivityTextFree.this);
					Intent intent = new Intent(EnrollActivityTextFree.this,
							HomeActivity.class);
					intent.putExtra(IntentKeys.key_message_home,
							Enums.IconMessages.visitorRegisterSuccessfull);
					intent.putExtra(IntentKeys.key_signal_type,
							Signal.Good.toString());
					startActivity(intent);
					EnrollActivityTextFree.this.finish();
				}
				break;
			case MESSAGE_CANCELED:
				// *AS Delete data from database if registration is cancelled.
				ScansOperations.deleteScans(EnrollActivityTextFree.this,
						treatmentId);
				isClosing = true;
				try {
					usb_host_ctx.CloseDevice();
				} catch (Exception e) {
				}
				Intent intent = new Intent();
				intent = new Intent(EnrollActivityTextFree.this,
						HomeActivityTextFree.class);
				intent.putExtra(IntentKeys.key_message_home, getResources()
						.getString(R.string.registrationCanceled));
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Bad.toString());
				startActivity(intent);
				EnrollActivityTextFree.this.finish();
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				break;
			case MESSAGE_IDENTIFICATION:
				String treatmentId = (String) msg.obj;
				String[] id = treatmentId.split(",");
				DbUtils.hardDeletePatient(id[0], EnrollActivityTextFree.this);
				treatmentId = id[1];
				if (!PatientsOperations.patientExists(treatmentId,
						EnrollActivityTextFree.this)) {
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					callReg();

				} else if (PatientsOperations.getPatientDetails(treatmentId,
						EnrollActivityTextFree.this).Status
						.equals(Enums.StatusType.getStatusType(
								StatusType.Default).toString())) {
					mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1, -1,
							Enums.IconMessages.defaultCanNotEdit)
							.sendToTarget();
				} else {
					isClosing = true;
					try {
						usb_host_ctx.CloseDevice();
					} catch (Exception e) {
					}
					Intent editPatientIntent = new Intent(
							EnrollActivityTextFree.this,
							SelectStatusActivity.class);
					PatientDetailsIntent dets = new PatientDetailsIntent();
					dets.treatmentId = treatmentId;
					dets.intentFrom = Enums.IntentFrom.EditPatient;
					dets.iconId = PatientIconOperation.getIcon(treatmentId,
							EnrollActivityTextFree.this);
					dets.backIntent.add(backIntent.scan);
					editPatientIntent.putExtra(IntentKeys.key_petient_details,
							new Gson().toJson(dets));
					EnrollActivityTextFree.this.finish();
					startActivity(editPatientIntent);
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);
				}
				break;
			case MESSAGE_SHOW_HEADER:
				String showheader = (String) msg.obj;
				lblStatusOne.setText("");
				break;
			case MESSAGE_SHOW_GREEN:
				if (!isLastError)
					getGreenAnimation();
				break;
			case MESSAGE_SHOW_RED:
				getRedAnimation();
				break;
			case MESSAGE_SHOW_ICON_MESSAGE:
				isScanOn = false;
				Enums.IconMessages msgs = (IconMessages) msg.obj;
				switch (msgs) {
				case visitorOrPatientNotExist:
					break;
				case NA:
					break;
				case defaultCanNotEdit:
					break;
				case identificationCompleteNotFound:
					break;
				case scanAlreadyExists:
					lblHeader.setVisibility(View.VISIBLE);
					lblHeader.setBackgroundResource(R.drawable.admin);
					break;
				case fingerprintReaderError:
					break;
				case identificationErrorDatabaseIsEmpty:
					break;
				default:
					break;
				}
				break;
			case MESSAGE_SHOW_LEFT_INDEX:
				LinearLayout.LayoutParams lvp = new LinearLayout.LayoutParams(
						90, 90);
				lvp.setMargins(0, 0, 50, 0);
				imgStaticOne.setImageResource(R.drawable.left_index_patient);
				imgStaticOne.setLayoutParams(lvp);
				imgStaticTwo
						.setImageResource(R.drawable.right_index_alpha_patient);
				imgStaticTwo.setLayoutParams(new LinearLayout.LayoutParams(70,
						70));
				lblStatusOne
						.setBackgroundResource(R.drawable.circle_green_alpha);
				lblStatusTwo
						.setBackgroundResource(R.drawable.circle_green_alpha);
				lblStatusThree
						.setBackgroundResource(R.drawable.circle_green_alpha);
				lblStatusFour
						.setBackgroundResource(R.drawable.circle_green_alpha);

				break;
			case MESSAGE_SHOW_RIGHT_INDEX:

				LinearLayout.LayoutParams lvs = new LinearLayout.LayoutParams(
						70, 70);
				lvs.setMargins(0, 0, 50, 0);
				imgStaticOne
						.setImageResource(R.drawable.left_index_alpha_patient);
				imgStaticOne.setLayoutParams(lvs);
				imgStaticTwo.setImageResource(R.drawable.right_index_patient);
				imgStaticTwo.setLayoutParams(new LinearLayout.LayoutParams(90,
						90));
				lblStatusOne
						.setBackgroundResource(R.drawable.circle_green_alpha);
				lblStatusTwo
						.setBackgroundResource(R.drawable.circle_green_alpha);
				lblStatusThree
						.setBackgroundResource(R.drawable.circle_green_alpha);
				lblStatusFour
						.setBackgroundResource(R.drawable.circle_green_alpha);
				break;
			case MESSAGE_SAVE_HEADER:
				break;
			case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
				getRedAnimation();
				lblStatusOne
						.setBackgroundResource(R.drawable.futronic_disabled);
				break;
			case MESSAGE_TOTAL_COUNT:
				int totalCount = (Integer) msg.obj;
				switch (totalCount) {
				case 1:
					lblStatusThree
							.setBackgroundResource(R.drawable.circle_green);

					break;
				case 2:
					lblStatusTwo.setBackgroundResource(R.drawable.circle_green);

					break;
				case 3:
					lblStatusOne.setBackgroundResource(R.drawable.circle_green);
					lblHeader.setVisibility(View.GONE);
					break;
				case 0:
					lblStatusFour
							.setBackgroundResource(R.drawable.circle_green);

					break;
				case 4:
					lblStatusOne
							.setBackgroundResource(R.drawable.circle_green_alpha);
					lblStatusTwo
							.setBackgroundResource(R.drawable.circle_green_alpha);
					lblStatusThree
							.setBackgroundResource(R.drawable.circle_green_alpha);
					lblStatusFour
							.setBackgroundResource(R.drawable.circle_green_alpha);
					break;
				default:
					break;
				}
				break;
			}
		}
	};

	@Override
	public boolean OnFakeSource(FTR_PROGRESS arg0) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "Fake source detected")
				.sendToTarget();
		return false;
	}

	@Override
	public void OnPutOn(FTR_PROGRESS arg0) {
		int message = 4;
		if (arg0.m_Count > 1) {
			int countRemains = arg0.m_Total - arg0.m_Count + 1;
			if (countRemains == 3) {
				message = 3;
			} else if (countRemains == 2) {
				message = 2;
			} else if (countRemains == 1) {
				message = 1;
			} else if (countRemains == 0) {
				message = 0;
			}
		}
		mHandler.obtainMessage(MESSAGE_TOTAL_COUNT, -1, -1, message)
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

				ArrayList<ScanModel> scans = ScansOperations.getScans(true, this);
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
				// Set control properties

				int scanQuality = ((FutronicEnrollment) m_Operation)
						.getQuality();
				Logger.LogToDb(this, "Registration Scan Qulaity",
						"Scan Quality: " + scanQuality + ", TREATMENT_ID="
								+ treatmentId);
				if (scanQuality >= ((eComplianceApp) this
						.getApplicationContext()).tempQualityBenchmark) {
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
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
						} else
							callReg();
					}
				} else {
					if (fingerCount >= 10) {
						mHandler.obtainMessage(MESSAGE_COMPLETE).sendToTarget();
					} else {
						if (isScanExists) {
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
							callReg();
						} else
							callReg();
					}
				}
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				mHandler.obtainMessage(
						MESSAGE_SHOW_MSG,
						-1,
						-1,
						"Enrollment failed. Error description: "
								+ FutronicSdkBase.SdkRetCode2Message(nResult))
						.sendToTarget();
				Logger.e(this, "Enrollment failed. Error description:",
						FutronicSdkBase.SdkRetCode2Message(nResult));
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

		isClosing = false;
		mHandler.obtainMessage(MESSAGE_CANCELED).sendToTarget();

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
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, EnrollActivityTextFree.class);
		intent.putExtra(IntentKeys.key_treatment_id, treatmentId);
		intent.putExtra(IntentKeys.key_visitor_type, visitorType);
		intent.putExtra(IntentKeys.key_enrollment_current_finger, fingerCount);
		intent.putExtra(IntentKeys.key_intent_from, reptype);
		this.finish();
		startActivity(intent);
	}

	public void ResetHeader() {
		mHandler.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1, savedHeader)
				.sendToTarget();
	}
}
