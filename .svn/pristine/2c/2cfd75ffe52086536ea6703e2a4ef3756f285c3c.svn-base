/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.HomeActivity;
import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IconMessages;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Logger;

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
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;
import com.futronictech.SDKHelper.IIdentificationCallBack;
import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
import com.futronictech.SDKHelper.VersionCompatible;
import com.google.gson.Gson;

public class IdentifyActivityTextFree extends Activity implements
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
	public static final int MESSAGE_SHOW_ICON_MESSAGE = 8;

	private static Bitmap mBitmapFP = null;

	ArrayList<ScanModel> scans;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identify_fp);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		imgScan = (ImageView) findViewById(R.id.imgIdentFpImg);
		lblHeader = (TextView) findViewById(R.id.lblIdentHeader);
		lblText = (TextView) findViewById(R.id.lblIdentText);
		layout = (LinearLayout) findViewById(R.id.layoutHome);
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		scans = ScansOperations.getScans(false, this);
		if (scans.size() > 0) {
			usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
			scanFinger();
		}
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
					getGreenAnimation();
					// lblText.setText(getResources().getString(
					// R.string.userDenyScannerDevice));
				}
				break;
			case MESSAGE_SHOW_ICON_MESSAGE:
				Intent intent = new Intent();
				Enums.IconMessages msgs = (IconMessages) msg.obj;
				switch (msgs) {
				case visitorOrPatientNotExist:
					intent = new Intent(IdentifyActivityTextFree.this,
							HomeActivityTextFree.class);
					intent.putExtra(IntentKeys.key_signal_type,
							Enums.Signal.Bad);
					intent.putExtra(IntentKeys.key_message_home,
							Enums.IconMessages.defaultCanNotEdit);
					IdentifyActivityTextFree.this.finish();
					startActivity(intent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
					Logger.e(IdentifyActivityTextFree.this,
							"visitor not exist",
							"Visitor is not exist in the database");
					break;
				case NA:
					lblText.setBackgroundResource(R.drawable.cat_one_icon);
					break;
				case defaultCanNotEdit:
					intent = new Intent(IdentifyActivityTextFree.this,
							HomeActivityTextFree.class);
					intent.putExtra(IntentKeys.key_signal_type,
							Enums.Signal.Bad);
					intent.putExtra(IntentKeys.key_message_home,
							Enums.IconMessages.defaultCanNotEdit);
					IdentifyActivityTextFree.this.finish();
					startActivity(intent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
					break;
				case identificationCompleteNotFound:
					getRedAnimation();
					break;
				case fingerprintReaderError:
					lblText.setBackgroundResource(R.drawable.futronic_disabled);
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
			case MESSAGE_IDENTIFICATION:
				String treatmentId = (String) msg.obj;
				if (!PatientsOperations.patientExists(treatmentId,
						IdentifyActivityTextFree.this)) {

					GoToHome(Enums.IconMessages.visitorOrPatientNotExist,
							Signal.Bad);

				} else if (PatientsOperations.getPatientDetails(treatmentId,
						IdentifyActivityTextFree.this).Status
						.equals(Enums.StatusType.getStatusType(
								StatusType.Default).toString())) {
					GoToHome(Enums.IconMessages.defaultCanNotEdit, Signal.Bad);
				} else {
					CloseScanner();
					Intent editPatientIntent = new Intent(
							IdentifyActivityTextFree.this,
							SelectStatusActivity.class);
					PatientDetailsIntent det = new PatientDetailsIntent();
					det.treatmentId = treatmentId;
					det.intentFrom = Enums.IntentFrom.EditPatient;
					det.backIntent.add(backIntent.scan);
					det.iconId = PatientIconOperation.getIcon(treatmentId,
							IdentifyActivityTextFree.this);
					if (det.iconId.equals("")) {
						editPatientIntent = new Intent(
								IdentifyActivityTextFree.this,
								SelectImageActivity.class);
					}
					editPatientIntent.putExtra(IntentKeys.key_petient_details,
							new Gson().toJson(det));
					IdentifyActivityTextFree.this.finish();
					startActivity(editPatientIntent);
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);
				}
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
				m_Operation.setFARN(((eComplianceApp) this
						.getApplicationContext()).farValue);
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
				// mHandler.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
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

	public void onHavingTroubleClick(View v) {
		CloseScanner();
		Intent intent = new Intent(this, ReportListActivity.class);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.AllPatients);
		intent.putExtra(IntentKeys.key_intent_from,
				Enums.IntentFrom.EditPatient);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	@Override
	public void OnPutOn(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1, -1,
				Enums.IconMessages.fingerPrintEnable).sendToTarget();
	}

	@Override
	public void OnTakeOff(FTR_PROGRESS Progress) {
		mHandler.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1, -1,
				Enums.IconMessages.fingerPrintEnable).sendToTarget();
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
						} else {
							mHandler.obtainMessage(
									MESSAGE_SHOW_ICON_MESSAGE,
									-1,
									-1,
									Enums.IconMessages.identificationCompleteNotFound)
									.sendToTarget();
							mHandler.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
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
		Intent intent = new Intent(this, IdentifyActivityTextFree.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		GoToHome(Enums.IconMessages.NA, Signal.Good);
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

	private void GoToHome(Enums.IconMessages message, Signal signal) {
		CloseScanner();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void CloseScanner() {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
	}
}
