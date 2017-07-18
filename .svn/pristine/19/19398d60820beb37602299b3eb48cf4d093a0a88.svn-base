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

import org.opasha.eCompliance.ecompliance.BackupActivity;
import org.opasha.eCompliance.ecompliance.ConfigurationActivity;
import org.opasha.eCompliance.ecompliance.PatientReportActivity;
import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.Reports;
import org.opasha.eCompliance.ecompliance.RestoreActivity;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialCounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorLoginOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.database.module.SyncTask;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.DefaultMark;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IconMessages;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.DoseUtils;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Logger;
import org.opasha.eCompliance.ecompliance.util.MissedDoseGeneration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.futronictech.SDKHelper.FTR_PROGRESS;
import com.futronictech.SDKHelper.FtrIdentifyRecord;
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;
import com.futronictech.SDKHelper.IIdentificationCallBack;
import com.futronictech.SDKHelper.UsbDeviceDataExchangeImpl;
import com.futronictech.SDKHelper.VersionCompatible;

public class HomeActivityTextFree extends Activity implements
		IIdentificationCallBack {
	private Menu menu;
	boolean isError = false;
	boolean isClosing = false;
	boolean isScanOn = false;
	boolean isScanClicked = true;
	String path;
	ImageView imgScan;
	TableLayout pendinglist;
	ProgressDialog pd;
	MediaController mc;
	TextView lblHeader, lblfingerPrintConnection, lblPatientStatus;
	LinearLayout layout;
	ImageView imgSignOff;
	String pTreatmentId = "";
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
	public static final int MESSAGE_SHOW_YELLOW = 9;
	public static final int MESSAGE_SHOW_FINGER_PRINT_CONNECT = 10;
	public static final int MESSAGE_SHOW_ICON = 11;
	public static final int MESSAGE_SHOW_ADMIN_ICON = 12;
	public static final int MESSAGE_SHOW_ICON_MESSAGE = 13;
	public static final int MESSAGE_SHOW_ICON_STATUS = 14;
	public static final int MESSAGE_SHOW_STATUS_VISIBLE = 15;
	public static final int MESSAGE_SHOW_ICON_COUS_STARTS = 16;
	public static final int MESSAGE_SHOW_ICON_COUS_TIMEOUT = 17;
	private static Bitmap mBitmapFP = null;
	ArrayList<ScanModel> scans;
	FtrIdentifyRecord[] rgTemplates;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		setContentView(R.layout.activity_home_text_free);
		path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
				+ "//eComplianceClient//"
				+ DbUtils.getTabId(this)
				+ "//resources//";
		new IsResourcesDownloadedTask()
				.execute(new IsResourcesDownloadedTask.DownloadTaskPayLoad(
						DbUtils.getTabId(this), getApplicationContext(),
						new Object[] { this, 1 }));
		pendinglist = (TableLayout) findViewById(R.id.videoview);
		lblPatientStatus = (TextView) findViewById(R.id.lblPatientStatus);
		pendinglist.addView(generatePendingList());
		pendinglist.invalidate();
		imgScan = (ImageView) findViewById(R.id.imgIdentFpImg);
		lblHeader = (TextView) findViewById(R.id.lblIdentHeader);
		layout = (LinearLayout) findViewById(R.id.layoutHome);
		imgSignOff = (ImageView) findViewById(R.id.imgAdminLoggedIn);
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		lblfingerPrintConnection = (TextView) findViewById(R.id.lblfingerPrintConnection);
		lblfingerPrintConnection.setClickable(false);
		long lastMissedDoseDate = ((eComplianceApp) this.getApplication()).lastMissedDate;
		long currentDate = GenUtils.getCurrentDateLong();
		if (lastMissedDoseDate == 0 || lastMissedDoseDate != currentDate) {
			DefaultMark.markDefault(this);
			MissedDoseGeneration.generateMissedDoses(this);
			((eComplianceApp) this.getApplication()).lastMissedDate = currentDate;
		}
		// Show Messages from Other Activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			boolean handleScan = false;
			try {
				try {
					String treatmentId = extras
							.getString(IntentKeys.key_handleScan_home);
					if (treatmentId != null) {
						if (!treatmentId.isEmpty()) {
							handleScan = true;
							HandleScanTextFree(treatmentId);
						}
					}
				} catch (Exception e) {
				}
				if (!handleScan) {
					Enums.IconMessages msg = (IconMessages) extras
							.get(IntentKeys.key_message_home);
					mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE,
							-1, -1, msg).sendToTarget();
					Signal signal = Signal.getSignal(extras
							.getString(IntentKeys.key_signal_type));
					if (signal == Signal.Good)
						mHandlerTextFree.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();
					else {
						if (signal == Signal.Bad)
							mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
					}
				}
			} catch (Exception e) {
			}
		}
		scans = ScansOperations.getScans(false, this);
		if (scans.size() > 0) {
			usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandlerTextFree);
			rgTemplates = new FtrIdentifyRecord[scans.size()];
			for (int i = 0; i < scans.size(); i++) {
				rgTemplates[i] = new FtrIdentifyRecord();
				rgTemplates[i].m_KeyValue = scans.get(i).treatmentId
						.getBytes(Charset.defaultCharset());
				rgTemplates[i].m_Template = scans.get(i).scan;
			}
			scanFinger();
		}
		showLoggedInImage();
	}

	private TableLayout generatePendingList() {
		ArrayList<String> list = ((eComplianceApp) this.getApplicationContext()).pendingDoses;
		int c = 0;
		String path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
				+ "/eComplianceClient/"
				+ DbUtils.getTabId(this)
				+ "/resources/";
		TableLayout layouta = getTableLayout();
		TableRow row = getTableRow();
		for (String s : list) {
			String icon = PatientIconOperation.getIcon(s, this);
			Bitmap bitmapImage = BitmapFactory.decodeFile(path + icon);
			Drawable drawableImage = new BitmapDrawable(bitmapImage);
			if (c == 6) {
				layouta.addView(row);
				row = getTableRow();
				c = 0;
			}
			LayoutInflater mInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vi = mInflater.inflate(R.layout.home_image_view, null);
			LinearLayout.LayoutParams llp = new TableRow.LayoutParams(70, 70);
			llp.setMargins(5, 0, 0, 0);
			vi.setLayoutParams(llp);
			ImageView view = (ImageView) vi.findViewById(R.id.imageView);
			view.setBackgroundDrawable(drawableImage);
			row.addView(vi);
			c++;
		}
		for (int a = c; a < 6; a++) {
			LinearLayout view = getTextView();
			row.addView(view);
		}
		layouta.addView(row);
		return layouta;
	}

	private TableRow getTableRow() {
		TableRow tableRowa = new TableRow(this);
		tableRowa.setGravity(Gravity.CENTER);
		tableRowa.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		return tableRowa;
	}

	private LinearLayout getTextView() {
		LinearLayout button = new LinearLayout(this);
		LinearLayout.LayoutParams llp = new TableRow.LayoutParams(70, 70);
		llp.setMargins(5, 0, 0, 0);
		button.setLayoutParams(llp);

		return button;
	}

	private TableLayout getTableLayout() {
		TableLayout tableLayout = new TableLayout(this);
		tableLayout.setStretchAllColumns(true);
		TableLayout.LayoutParams pd = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		pd.setMargins(5, 0, 0, 0);
		tableLayout.setLayoutParams(pd);
		return tableLayout;
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

	// Initiating Menu XML file (menu.xml)

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		boolean returnValue = true;
		switch (itemId) {
		case R.id.menu_home_backup:
			startActivity(new Intent(this, BackupActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_Restore:
			startActivity(new Intent(this, RestoreActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_setting:
			startActivity(new Intent(this, ConfigurationActivity.class));
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
			break;
		case R.id.menu_home_Sync:
			onSyncClick();
			break;
		case R.id.menu_download_resources:
			isClosing = false;
			new DownloadResourcesTask()
					.execute(new DownloadResourcesTask.DownloadTaskPayLoad(
							DbUtils.getTabId(this), getApplicationContext(),
							new Object[] { this, 1 }));
			pd = ProgressDialog.show(this, "", "", true);
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
		default:
			returnValue = super.onOptionsItemSelected(item);
			break;
		}
		return returnValue;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_home_text_free, menu);
		if (((eComplianceApp) this.getApplicationContext()).IsResourceDownloaded) {
			(menu.findItem(R.id.menu_download_resources))
					.setIcon(R.drawable.download_complete_icon);
		} else {
			(menu.findItem(R.id.menu_download_resources))
					.setIcon(R.drawable.download_icon);
		}
		return true;
	}

	public void btnNewVisitorClick(View v) {
		((eComplianceApp) this.getApplicationContext()).tempQualityBenchmark = ((eComplianceApp) this
				.getApplicationContext()).qualityBenchmark;
		if (!isSundayOn()) {
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(
				this,
				org.opasha.eCompliance.ecompliance.TextFree.SelectVisitorTextFree.class);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void visitClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, ReportListActivity.class);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.VisitedPatients);
		intent.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void pendingClick(View v) {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, ReportListActivity.class);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.PendingPatients);
		intent.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
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
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
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
		Intent intent = new Intent(this, ReportListActivity.class);
		intent.putExtra(IntentKeys.key_intent_from,
				Enums.IntentFrom.HavingTrouble);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.AllPatients);
		intent.putExtra(IntentKeys.key_verification_reason,
				getString(R.string.MarkVisit));
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void btnEditVisitorClick(View v) {
		if (!isSundayOn()) {
			return;
		}
		isClosing = false;

		// *AS DEF2221 - Verify if Admin Logged In before editing a patient.
		if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.counselorLoginRequired))
					.sendToTarget();
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "")
					.sendToTarget();
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
			return;
		}
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, IdentifyActivityTextFree.class);
		intent.putExtra(IntentKeys.key_intent_from,
				Enums.IntentFrom.EditPatient);
		intent.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.AllPatients);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
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
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void onScanClick(View v) {
		isScanClicked = true;
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		Intent intent = new Intent(
				this,
				org.opasha.eCompliance.ecompliance.TextFree.HomeActivityTextFree.class);
		startActivity(intent);
		this.finish();
	}

	// -----------------------------
	// Finger Print Related
	// -----------------------------
	private void scanFinger() {
		lblfingerPrintConnection.setClickable(false);
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
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblfingerPrintConnection
						.setBackgroundResource(R.drawable.futronic_disabled);

			}
		} else {
			if (!usb_host_ctx.IsPendingOpen()) {
				lblfingerPrintConnection
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

	public void OnPutOn(FTR_PROGRESS Progress) {
		mHandlerTextFree.obtainMessage(MESSAGE_SHOW_FINGER_PRINT_CONNECT, -1,
				-1,
				getResources().getString(R.string.putFingeronDevice) + "...")
				.sendToTarget();
	}

	@Override
	public void OnTakeOff(FTR_PROGRESS Progress) {
		mHandlerTextFree.obtainMessage(MESSAGE_SHOW_MSG, -1, -1,
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
		mHandlerTextFree.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();
	}

	@Override
	public boolean OnFakeSource(FTR_PROGRESS Progress) {
		mHandlerTextFree.obtainMessage(MESSAGE_SHOW_MSG, -1, -1,
				"Fake source detected").sendToTarget();
		return false;
	}

	@Override
	public void OnGetBaseTemplateComplete(boolean bSuccess, int nResult) {
		mHandlerTextFree.obtainMessage(MESSAGE_SHOW_STATUS_VISIBLE)
				.sendToTarget();
		try {
			if (isClosing)
				return;
			if (bSuccess) {
				if (scans.size() > 0) {
					IdentificationResultCustom idenResult = GenUtils.Identify(
							m_Operation, scans, nResult);
					if (idenResult.nResult == FutronicSdkBase.RETCODE_OK) {
						if (idenResult.foundIndex != -1) {
							mHandlerTextFree
									.obtainMessage(
											MESSAGE_IDENTIFICATION,
											-1,
											-1,
											scans.get(idenResult.foundIndex).treatmentId)
									.sendToTarget();
						} else {
							mHandlerTextFree
									.obtainMessage(
											MESSAGE_SHOW_ICON_MESSAGE,
											-1,
											-1,
											Enums.IconMessages.identificationCompleteNotFound)
									.sendToTarget();
							mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
									.sendToTarget();
						}
						m_Operation = null;
						if (!isClosing)
							scanFinger();
					} else {
						lblfingerPrintConnection
								.setBackgroundResource(R.drawable.futronic_disabled);
						mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
								.sendToTarget();
						m_Operation = null;
						isError = true;
					}
				} else {
					mHandlerTextFree
							.obtainMessage(
									MESSAGE_SHOW_ICON_MESSAGE,
									-1,
									-1,
									Enums.IconMessages.identificationErrorDatabaseIsEmpty)
							.sendToTarget();
					mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
							.sendToTarget();
					Logger.e(this, "identificationErrorDatabaseIsEmpty",
							"identificationErrorDatabaseIsEmpty");
					m_Operation = null;
					isError = true;
				}
			} else {
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				lblfingerPrintConnection
						.setBackgroundResource(R.drawable.futronic_disabled);
				lblfingerPrintConnection.setVisibility(View.VISIBLE);
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

	@Override
	public void onBackPressed() {
		isClosing = true;
		try {
			usb_host_ctx.CloseDevice();
		} catch (Exception e) {
		}
		this.finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	/**
	 * Generate Self Admin Doses for CP Cases
	 * 
	 * @param treatmentId
	 * @param gps
	 * @param lastLoginId
	 */
	private void generateCpSelfAdminDoses(String treatmentId, GpsTracker gps,
			String lastLoginId) {
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentId,
						this), this);
		if (regimen.stage.equals(Enums.StageType
				.getStageType(Enums.StageType.CP))) {
			long doseDate = GenUtils.getCurrentDateLong() + 172800000;
			for (int i = 0; i < 2; i++) {
				if (regimen.schedule.equals(Enums.Schedule.Monday.toString())
						|| regimen.schedule.equals(Enums.Schedule.Wednesday
								.toString())
						|| regimen.schedule.equals(Enums.Schedule.Friday
								.toString())) {
					if (GenUtils.dateToDay(doseDate) == 1) {
						doseDate = doseDate + 86400000;
					}
				} else {
					if (GenUtils.dateToDay(doseDate) == 1) {
						doseDate = doseDate + 86400000;
					}
					if (GenUtils.dateToDay(doseDate) == 2) {
						doseDate = doseDate + 86400000;
					}
				}
				DoseUtils.AddDose(treatmentId, Enums.DoseType.SelfAdministered
						.toString(), doseDate, TreatmentInStagesOperations
						.getPatientRegimenId(treatmentId, this), System
						.currentTimeMillis(), lastLoginId, gps.getLatitude(),
						gps.getLongitude(), this);
				doseDate = doseDate + 172800000;
			}
		}
	}

	@SuppressLint("NewApi")
	private void GenerateSputumAlert() {
		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) this
				.findViewById(R.id.popupLinearLayout);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.activity_popup,
				viewGroup);
		// Get Screen Max Size.
		Point screenMaxPoint = new Point();
		getWindowManager().getDefaultDisplay().getSize(screenMaxPoint);
		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(this);
		popup.setContentView(layout);
		popup.setWidth(screenMaxPoint.x);
		popup.setHeight(screenMaxPoint.y);
		popup.setFocusable(true);
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

	private void markInitialCounselingComplete(String lastLoginId,
			Patient patient) {
		if (patient.isCounsellingPending) {
			PatientsOperations.addPatient(patient.treatmentID, patient.name,
					patient.Status, patient.phoneNumber, patient.machineID,
					false, System.currentTimeMillis(), lastLoginId, false,
					patient.RegDate, patient.centerId, patient.address,
					patient.diseaseSite, patient.disease, patient.patientType,
					patient.nikshayId, patient.tbNumber,
					patient.smokingHistory, this);
		}
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
			String lastLoginId, String iconLocation) {
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
							mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON,
									-1, -1, iconLocation).sendToTarget();
							mHandlerTextFree.obtainMessage(
									MESSAGE_SHOW_ICON_COUS_TIMEOUT)
									.sendToTarget();
							mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
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
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON, -1, -1,
						iconLocation).sendToTarget();
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON_COUS_STARTS)
						.sendToTarget();
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_GREEN)
						.sendToTarget();
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
		isClosing = false;
		Logger.LogToDb(
				this,
				"Manual Sync",
				"Manual Sync Started at"
						+ GenUtils.longToDateString(System.currentTimeMillis()));
		new SyncTask().execute(new SyncTask.SyncTaskPayLoad(
				getApplicationContext(), new Object[] { 4, this, null }));
		pd = ProgressDialog.show(this, "", "", true);
	}

	public boolean GenerateAlert(String treatmentId) {
		int regimenID = TreatmentInStagesOperations.getPatientRegimenId(
				treatmentId, this);
		// DEF2239*-- Alert reminder generated for lab test
		int doseCount = 0;
		boolean isSputumAlertGen = false;
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
					}
				}
			}
		}
		return isSputumAlertGen;
	}

	private boolean isSundayOn() {
		boolean retValue = true;
		if (GenUtils.dateToDay(System.currentTimeMillis()) == 1) {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_sunday_enabled, this).equals(
					"false")) {
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON_MESSAGE, -1,
						-1, Enums.IconMessages.sundayBlock).sendToTarget();
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
				retValue = false;
			}
		}
		return retValue;
	}

	private void HandleScanTextFree(String treatmentId) {
		boolean isActive = false;
		if (!isSundayOn()) {
			return;
		}
		GpsTracker gps = new GpsTracker(this);
		String lastLoginId = ((eComplianceApp) this.getApplication()).LastLoginId;
		if (PatientsOperations.patientExists(treatmentId, this)) {
			String icon = PatientIconOperation.getIcon(treatmentId, this);
			// if (icon.equals("")) {
			// PatientDetailsIntent det = new PatientDetailsIntent();
			// det.iconId = icon;
			// det.treatmentId = treatmentId;
			// det.intentFrom = Enums.IntentFrom.EditPatient;
			// det.backIntent.add(backIntent.scan);
			// startActivity(new Intent(this, SelectImageActivity.class)
			// .putExtra(IntentKeys.key_petient_details,
			// new Gson().toJson(det)));
			// this.finish();
			// return;
			// }
			Patient patient = PatientsOperations.getPatientDetails(treatmentId,
					this);
			if (patient.isCounsellingPending) {
				if (!DoInitialCounselling(treatmentId, patient, lastLoginId,
						path + icon)) {
					return;
				}
			}
			patient = PatientsOperations.getPatientDetails(treatmentId, this);
			if (((eComplianceApp) this.getApplication()).visitToday
					.contains(treatmentId)) {
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON, -1, -1,
						path + icon).sendToTarget();
				mHandlerTextFree.obtainMessage(MESSAGE_SHOW_YELLOW)
						.sendToTarget();
				pTreatmentId = treatmentId;
				return;
			} else {
				if (((eComplianceApp) this.getApplication()).pendingDoses
						.contains(treatmentId)) {
					markInitialCounselingComplete(lastLoginId, patient);
					DoseUtils.AddDose(treatmentId, Enums.DoseType.Supervised
							.toString(), GenUtils.getCurrentDateLong(),
							TreatmentInStagesOperations.getPatientRegimenId(
									treatmentId, this), System
									.currentTimeMillis(), lastLoginId, gps
									.getLatitude(), gps.getLongitude(), this);
					generateCpSelfAdminDoses(treatmentId, gps, lastLoginId);
					((eComplianceApp) this.getApplication()).pendingDoses
							.remove(treatmentId);
					((eComplianceApp) this.getApplication()).visitToday
							.add(treatmentId);
					pendinglist.removeAllViews();
					pendinglist.postInvalidate();
					pendinglist.addView(generatePendingList());
					if (GenerateAlert(patient.treatmentID)) {
						GenerateSputumAlert();
					}
					mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON, -1, -1,
							path + icon).sendToTarget();
					mHandlerTextFree.obtainMessage(MESSAGE_SHOW_GREEN)
							.sendToTarget();
					pTreatmentId = treatmentId;
					return;
				} else {
					if (((eComplianceApp) this.getApplication()).missedDose
							.contains(treatmentId)) {
						markInitialCounselingComplete(lastLoginId, patient);
						DoseUtils
								.AddDose(treatmentId, Enums.DoseType.Supervised
										.toString(), GenUtils
										.getCurrentDateLong(),
										TreatmentInStagesOperations
												.getPatientRegimenId(
														treatmentId, this),
										System.currentTimeMillis(),
										lastLoginId, gps.getLatitude(), gps
												.getLongitude(), this);
						generateCpSelfAdminDoses(treatmentId, gps, lastLoginId);
						((eComplianceApp) this.getApplication()).missedDose
								.remove(treatmentId);
						((eComplianceApp) this.getApplication()).visitToday
								.add(treatmentId);
						if (GenerateAlert(patient.treatmentID)) {
							GenerateSputumAlert();
						}
						mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON, -1,
								-1, path + icon).sendToTarget();
						mHandlerTextFree.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();
						pTreatmentId = treatmentId;
						return;
					} else {
						String status = patient.Status;
						if (status.equals(StatusType
								.getStatusType(StatusType.Active))) {
							if (DoseAdminstrationOperations.getLastDose(
									patient.treatmentID, this).doseDate == 0) {
								markInitialCounselingComplete(lastLoginId,
										patient);
								DoseUtils.AddDose(treatmentId,
										Enums.DoseType.Supervised.toString(),
										GenUtils.getCurrentDateLong(),
										TreatmentInStagesOperations
												.getPatientRegimenId(
														treatmentId, this),
										System.currentTimeMillis(),
										lastLoginId, gps.getLatitude(), gps
												.getLongitude(), this);
								generateCpSelfAdminDoses(treatmentId, gps,
										lastLoginId);
								((eComplianceApp) this.getApplication()).visitToday
										.add(treatmentId);
								if (GenerateAlert(patient.treatmentID)) {
									GenerateSputumAlert();
								}
								mHandlerTextFree.obtainMessage(
										MESSAGE_SHOW_ICON, -1, -1, path + icon)
										.sendToTarget();
								mHandlerTextFree.obtainMessage(
										MESSAGE_SHOW_GREEN).sendToTarget();
								pTreatmentId = treatmentId;
								return;
							} else {
								mHandlerTextFree.obtainMessage(
										MESSAGE_SHOW_ICON, -1, -1, path + icon)
										.sendToTarget();
								mHandlerTextFree.obtainMessage(
										MESSAGE_SHOW_YELLOW).sendToTarget();
								pTreatmentId = treatmentId;
								return;
							}
						} else {
							mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ICON,
									-1, -1, path + icon).sendToTarget();
							mHandlerTextFree.obtainMessage(
									MESSAGE_SHOW_ICON_STATUS, -1, -1, status)
									.sendToTarget();
							mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
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
								mHandlerTextFree.obtainMessage(
										MESSAGE_SHOW_ADMIN_ICON, -1, -1, 1)
										.sendToTarget();
								mHandlerTextFree
										.obtainMessage(MESSAGE_SHOW_RED)
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
							showLoggedInImage();
						}
						if (ConfigurationOperations
								.getKeyValue(
										ConfigurationKeys.key_is_home_page_of_text_on_pm_login,
										this).equals("1")) {
							if (visitorType == VisitorType.PM) {
								((eComplianceApp) this.getApplication()).IsAppTextFree = false;
								Intent intent = new Intent(
										this,
										org.opasha.eCompliance.ecompliance.HomeActivity.class);
								startActivity(intent);
								this.finish();
								return;
							}
						}
						mHandlerTextFree.obtainMessage(MESSAGE_SHOW_ADMIN_ICON,
								-1, -1, 2).sendToTarget();
						mHandlerTextFree.obtainMessage(MESSAGE_SHOW_GREEN)
								.sendToTarget();
						break;
					}
				}
				if (!isActive) {
					mHandlerTextFree.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
							getResources().getString(R.string.visitorInactive))
							.sendToTarget();
					mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED)
							.sendToTarget();
					return;
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandlerTextFree = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isClosing)
				return;
			switch (msg.what) {
			case MESSAGE_SHOW_ICON_COUS_STARTS:
				lblPatientStatus.setVisibility(View.VISIBLE);
				lblPatientStatus
						.setBackgroundResource(R.drawable.counseling_icon);
				break;
			case MESSAGE_SHOW_ICON_COUS_TIMEOUT:
				lblPatientStatus.setVisibility(View.VISIBLE);
				lblPatientStatus
						.setBackgroundResource(R.drawable.counseling_timeout_icon);
				break;
			case MESSAGE_SHOW_STATUS_VISIBLE:
				lblPatientStatus.setVisibility(View.GONE);
				break;
			case MESSAGE_SHOW_MSG:
				String showMsg = (String) msg.obj;
				// TODO:fix this
				// lblText.setText(showMsg);
				break;
			case MESSAGE_SHOW_IMAGE:
				imgScan.setImageBitmap(mBitmapFP);
				break;
			case MESSAGE_COMPLETE:
				Intent intent = new Intent(HomeActivityTextFree.this,
						HomeActivityTextFree.class);
				intent.putExtra("message", "Verified");
				startActivity(intent);
				HomeActivityTextFree.this.finish();
				break;
			case MESSAGE_SHOW_HEADER:
				// videoView.start();
				// int showheader = (Integer) msg.obj;
				// lblHeader.setBackgroundResource(showheader);
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
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.futronic_disabled);
				}
				break;
			case MESSAGE_IDENTIFICATION:
				String treatmentId = (String) msg.obj;
				HandleScanTextFree(treatmentId);
				break;
			case MESSAGE_SHOW_FINGER_PRINT_CONNECT:
				if (isScanClicked) {
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.futronic_enabled);
				}
				break;
			case MESSAGE_SHOW_ICON:
				isScanClicked = false;
				String iconLocation = (String) msg.obj;
				Bitmap bitmapImage = BitmapFactory.decodeFile(iconLocation);
				Drawable drawableImage = new BitmapDrawable(
						getApplicationContext().getResources(), bitmapImage);
				lblfingerPrintConnection.setBackgroundDrawable(drawableImage);
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_unsupervised_dose_enabled,
						HomeActivityTextFree.this).equals("true"))
					lblfingerPrintConnection.setClickable(true);
				break;
			case MESSAGE_SHOW_ICON_MESSAGE:
				isScanClicked = false;
				Enums.IconMessages msgs = (IconMessages) msg.obj;
				switch (msgs) {
				case visitorOrPatientNotExist:
					break;
				case NA:
					break;
				case defaultCanNotEdit:
					break;
				case identificationCompleteNotFound:
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.futronic_enabled);
					getRedAnimation();
					break;
				case fingerprintReaderError:
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.futronic_disabled);
					break;
				case sundayBlock:
					break;
				case identificationErrorDatabaseIsEmpty:
					break;
				case visitorRegisterSuccessfull:
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.admin_not_auth);
					break;
				case syncComplete:
					getGreenAnimation();
					break;
				case regComplete:
					getGreenAnimation();
					scanFinger();
					break;
				default:
					break;
				}
				break;
			case MESSAGE_SHOW_ADMIN_ICON:
				int admin = (Integer) msg.obj;
				isScanClicked = false;
				switch (admin) {
				case 1:// admin is not authenticated.
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.admin_not_auth);
					break;
				default:
					lblfingerPrintConnection
							.setBackgroundResource(R.drawable.admin);
					break;
				}
				break;
			case MESSAGE_SHOW_ICON_STATUS:
				String status = (String) msg.obj;
				isScanClicked = false;
				lblPatientStatus.setVisibility(View.VISIBLE);
				Bitmap bitmapImages = BitmapFactory.decodeFile(path + "//App//"
						+ status + ".png");
				Drawable drawableImages = new BitmapDrawable(
						getApplicationContext().getResources(), bitmapImages);
				lblPatientStatus.setBackgroundDrawable(drawableImages);
				break;
			case MESSAGE_SHOW_YELLOW:
				if (!isClosing) {
					getYellowAnimation();
				}
				break;
			}
		}
	};

	public void showNotification() {
		if (pd != null) {
			pd.cancel();
		}
		isClosing = true;
		Logger.LogToDb(this, "Manual Sync", "Manual Sync complete at"
				+ GenUtils.longToDateString(System.currentTimeMillis()));
		Intent intent = new Intent(this, HomeActivityTextFree.class);
		intent.putExtra(IntentKeys.key_signal_type, Enums.Signal.Good);
		intent.putExtra(IntentKeys.key_message_home,
				Enums.IconMessages.syncComplete);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void showDownloadButton() {
		if (menu != null) {
			MenuItem item = menu.findItem(R.id.menu_download_resources);
			if (((eComplianceApp) this.getApplicationContext()).IsResourceDownloaded) {
				item.setIcon(R.drawable.download_complete_icon);
			} else {
				item.setIcon(R.drawable.download_icon);
			}
		}
	}

	public void onPatientIconClick(View v) {
		isClosing = false;
		lblfingerPrintConnection.setClickable(false);
		if (PatientsOperations.isInitialCounsellingPending(pTreatmentId, this)) {
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
			return;
		}
		if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_HEADER, -1, -1,
					getResources().getString(R.string.counselorLoginRequired))
					.sendToTarget();
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, "")
					.sendToTarget();
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_RED).sendToTarget();
			return;
		}
		int maxNumOfDose = 6;
		try {
			maxNumOfDose = Integer.parseInt(ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_max_unsupervised_dose,
							this));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int takenDose = DoseAdminstrationOperations.getUnsupervisedCount(
				pTreatmentId, this);
		if (takenDose < maxNumOfDose) {
			isClosing = true;
			try {
				usb_host_ctx.CloseDevice();
			} catch (Exception e) {
			}
			Intent Intent = new Intent(HomeActivityTextFree.this,
					UnsupervisedDoseTextFree.class);
			Intent.putExtra(IntentKeys.key_treatment_id, pTreatmentId);
			startActivity(Intent);
			HomeActivityTextFree.this.finish();
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		} else {
			mHandlerTextFree.obtainMessage(MESSAGE_SHOW_YELLOW).sendToTarget();
			return;
		}
	}
}