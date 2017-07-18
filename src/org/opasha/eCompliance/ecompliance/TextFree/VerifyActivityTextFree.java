/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IconMessages;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futronictech.SDKHelper.FTR_PROGRESS;
import com.futronictech.SDKHelper.FtrIdentifyRecord;
import com.futronictech.SDKHelper.FtrIdentifyResult;
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;
import com.futronictech.SDKHelper.IIdentificationCallBack;
import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
import com.futronictech.SDKHelper.VersionCompatible;
import com.google.gson.Gson;

public class VerifyActivityTextFree extends Activity implements
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
	public static final int MESSAGE_CANCELED = 5;
	public static final int MESSAGE_SHOW_GREEN = 6;
	public static final int MESSAGE_SHOW_RED = 7;
	public static final int MESSAGE_VERIFICATION = 7;
	public static final int MESSAGE_ERROR = 8;
	public static final int MESSAGE_SHOW_ICON_MESSAGE = 9;

	private static Bitmap mBitmapFP = null;

	private static String treatmentId = "";
	private static String verificationReason;

	ArrayList<ScanModel> scans;
	FtrIdentifyRecord[] rgTemplates;
	PatientDetailsIntent det;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_fp_text_free);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			GoToHome("Error!", Signal.Bad);
		} else {
			det = new Gson().fromJson(
					extras.getString(IntentKeys.key_petient_details),
					PatientDetailsIntent.class);
			treatmentId = det.treatmentId;
			verificationReason = det.verificationReason;
			if (treatmentId.isEmpty()) {
				GoToHome(getResources().getString(R.string.errorIdEmpty),
						Signal.Bad);
			} else {
				imgScan = (ImageView) findViewById(R.id.imgVerifyFpImg);
				lblHeader = (TextView) findViewById(R.id.lblVerifyHeader);
				lblText = (TextView) findViewById(R.id.lblVerifyText);
				layout = (LinearLayout) findViewById(R.id.layout_verify_fp);
				layout.setBackgroundResource(R.drawable.grey_to_red_transition);

				scans = ScansOperations.getScans(this, treatmentId);
				if (scans.size() > 0) {
					usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
					rgTemplates = new FtrIdentifyRecord[scans.size()];
					for (int i = 0; i < scans.size(); i++) {
						rgTemplates[i] = new FtrIdentifyRecord();
						rgTemplates[i].m_KeyValue = scans.get(i).treatmentId
								.getBytes(Charset.defaultCharset());
						rgTemplates[i].m_Template = scans.get(i).scan;
					}
					scanFinger();
				} else {
					GoToHome(
							getResources().getString(R.string.errorIdNotFound),
							Signal.Bad);
				}
			}
		}
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

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOW_MSG:
				String showMsg = (String) msg.obj;
				lblText.setText(showMsg);
				break;
			case MESSAGE_SHOW_IMAGE:
				imgScan.setImageBitmap(mBitmapFP);
				break;
			case MESSAGE_COMPLETE:
				CloseScanner();
				if (verificationReason.equals(getString(R.string.MarkVisit))) {
					Intent intent = new Intent(VerifyActivityTextFree.this,
							HomeActivityTextFree.class);
					intent.putExtra(IntentKeys.key_handleScan_home, treatmentId);
					startActivity(intent);
					VerifyActivityTextFree.this.finish();
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
				} else {
					String treatmentId = (String) msg.obj;
					if (PatientsOperations.patientExists(treatmentId,
							VerifyActivityTextFree.this)) {
						if (PatientsOperations.getPatientDetails(treatmentId,
								VerifyActivityTextFree.this).Status
								.equals(Enums.StatusType.getStatusType(
										StatusType.Default).toString())) {
							GoToHome(
									getResources().getString(
											R.string.defaultCanNotEdit),
									Signal.Bad);
						} else {
							CloseScanner();
							Intent editPatientIntent = new Intent(
									VerifyActivityTextFree.this,
									SelectStatusActivity.class);
							det.backIntent.add(backIntent.scan);
							editPatientIntent.putExtra(
									IntentKeys.key_petient_details,
									new Gson().toJson(det));
							VerifyActivityTextFree.this.finish();
							startActivity(editPatientIntent);
							overridePendingTransition(R.anim.right_side_in,
									R.anim.right_side_out);
						}
					} else {
						// CloseScanner();
						// Intent editVisitorIntent = new Intent(
						// VerifyActivityTextFree.this,
						// EditVisitorActivity.class);
						// editVisitorIntent.putExtra(IntentKeys.key_treatment_id,
						// treatmentId);
						// editVisitorIntent.putExtra(IntentKeys.key_intent_from,
						// Enums.IntentFrom.Home);
						// VerifyActivityTextFree.this.finish();
						// startActivity(editVisitorIntent);
						// overridePendingTransition(R.anim.right_side_in,
						// R.anim.right_side_out);
					}
				}
				break;
			case MESSAGE_ERROR:
				String errorMsg = (String) msg.obj;
				GoToHome(errorMsg, Signal.Bad);
				break;
			case MESSAGE_CANCELED:
				GoToHome(
						getResources().getString(
								R.string.verificationCanceledByUser),
						Signal.Warn);
				break;
			case MESSAGE_SHOW_HEADER:
				String showheader = (String) msg.obj;
				lblHeader.setText(showheader);
				break;
			case MESSAGE_SHOW_GREEN:
				getGreenAnimation();
				break;
			case MESSAGE_SHOW_RED:
				getRedAnimation();
				break;
			case MESSAGE_SHOW_ICON_MESSAGE:
				Enums.IconMessages msgs = (IconMessages) msg.obj;
				switch (msgs) {
				case visitorOrPatientNotExist:
					lblText.setBackgroundResource(R.drawable.cat_two_icon);
					break;
				case NA:
					lblText.setBackgroundResource(R.drawable.cat_one_icon);
					break;
				case defaultCanNotEdit:
					lblText.setBackgroundResource(R.drawable.all_patient);
					break;
				case identificationCompleteNotFound:
					CloseScanner();
					Intent intent = new Intent(VerifyActivityTextFree.this,
							HomeActivityTextFree.class);
					intent.putExtra(IntentKeys.key_signal_type,
							Enums.Signal.Bad.toString());
					intent.putExtra(IntentKeys.key_message_home,
							Enums.IconMessages.identificationCompleteNotFound);
					VerifyActivityTextFree.this.finish();
					startActivity(intent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
					break;
				case fingerprintReaderError:
					lblText.setBackgroundResource(R.drawable.admin_not_auth);
					break;
				case identificationErrorDatabaseIsEmpty:
					lblText.setBackgroundResource(R.drawable.admin);
					break;
				case fingerPrintEnable:
					lblText.setBackgroundResource(R.drawable.futronic_enabled);
					break;
				case fingerPrintDisable:
					lblText.setBackgroundResource(R.drawable.futronic_disabled);
					break;
				default:
					break;
				}
				break;
			case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
				getRedAnimation();
				lblText.setText(getResources().getString(
						R.string.userDenyScannerDevice));
				break;
			}
		}
	};

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
				m_Operation.setFARN(245);

				m_Operation.setVersion(VersionCompatible.ftr_version_current);
				((FutronicIdentification) m_Operation).GetBaseTemplate(this);

			} catch (Exception e) {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblText.setBackgroundResource(R.drawable.futronic_disabled);
			}
		} else {
			if (usb_host_ctx.IsPendingOpen()) {
				// mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblText.setBackgroundResource(R.drawable.futronic_disabled);
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
		mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
		mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1, -1,
				Enums.IconMessages.fingerPrintEnable).sendToTarget();
	}

	@Override
	public void OnTakeOff(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1, -1,
				Enums.IconMessages.fingerPrintEnable).sendToTarget();
		mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
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
		mHandler.obtainMessage(MESSAGE_SHOW_GREEN).sendToTarget();
		mHandler.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();
	}

	@Override
	public boolean OnFakeSource(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "Fake source detected")
				.sendToTarget();
		return false;
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
		Intent intent = new Intent(this, HomeActivityTextFree.class);
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

	@Override
	public void OnGetBaseTemplateComplete(boolean bSuccess, int nResult) {
		try {
			if (isClosing)
				return;
			if (bSuccess) {
				if (scans.size() > 0) {
					FtrIdentifyResult Result = new FtrIdentifyResult();
					nResult = ((FutronicIdentification) m_Operation)
							.Identification(rgTemplates, Result);
					if (nResult == FutronicSdkBase.RETCODE_OK) {
						if (Result.m_Index != -1) {
							mHandler.obtainMessage(MESSAGE_COMPLETE, -1, -1,
									scans.get(Result.m_Index).treatmentId)
									.sendToTarget();
						} else {
							mHandler.obtainMessage(
									MESSAGE_SHOW_ICON_MESSAGE,
									-1,
									-1,
									Enums.IconMessages.identificationCompleteNotFound)
									.sendToTarget();
							return;
						}
						m_Operation = null;
						if (!isClosing)
							scanFinger();
					} else {
						mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1,
								-1, Enums.IconMessages.fingerprintReaderError)
								.sendToTarget();
						mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
						m_Operation = null;
						isError = true;
					}
				} else {
					mHandler.obtainMessage(
							MESSAGE_SHOW_ICON_MESSAGE,
							-1,
							-1,
							Enums.IconMessages.identificationErrorDatabaseIsEmpty)
							.sendToTarget();
					mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
					m_Operation = null;
					isError = true;
				}
			} else {
				mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1, -1,
						Enums.IconMessages.fingerprintReaderError)
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
		Intent intent = new Intent(this, VerifyActivityTextFree.class);
		intent.putExtra(IntentKeys.key_petient_details, new Gson().toJson(det));
		startActivity(intent);
		this.finish();
	}
}
