/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opasha.eCompliance.ecompliance.Adapters.SpinnerCategoryAdapter;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerCenterAdapter;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerScheduleAdapter;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerStageAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialLabMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.LocationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientGenderAgeOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientV2_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.Schedule;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class NewPatientActivity extends Activity {

	private ScrollView scrollLayout;
	private LinearLayout hivLayout, doseTakenLayout, IPDoseLayout,
			ExtIpDoseLayout, CPDoseLayout, diseaseSite_layout,
			otherpatientlayout;
	private TextView txtViewTreatmentid, txtViewErrorShow, txtViewCenter;
	private EditText editTxtName, editTxtPhone, editTxtAge, editTxtIP,
			editTxtEx_IP, editTxtCP, labDate, labDmc, labNo, labweight,
			edtAddress, edtDiseaseSite, edtNikshayId, edtTbnumber,
			edtAadhaarno, otherpatientType;
	private Spinner spinnerStage, spinnerSchedule, spinnerCategory,
			spinnerCenter, spinnerInitialLab, selectGender, hivResult,
			spinnerdiseaseClassification, spinnerPatientType, spinnerLabMonth,
			spinnerSmokingHistory, spinnerPatientSource;
	private CheckBox chkMoit; // Change MOIT of Fingerprint Registration - This
	// makes sure that registration happens even if
	// fingerprint sampling is not consistent.
	// chkMoit is true -> MOIT is false, chkMoit is
	// false -> MOIT is true
	SpinnerStageAdapter stageAdapter;
	SpinnerScheduleAdapter scheduleAdapter;
	SpinnerCategoryAdapter categoryAdapter;
	SpinnerCenterAdapter centerAdapter;
	ArrayList<Master> regimenList;
	ArrayList<Center> centerList;

	private StringBuilder verifyTextBox = new StringBuilder();
	private StringBuilder validateTextBox = new StringBuilder();
	String currentDay, stage, center, schedule, query, name, phone,
			treatmentId, tabId, PatientType;
	boolean IsInitialLabOn = true;
	private boolean oldID = false;
	private boolean isHivEnable = true;
	String machineType = "P";
	int centerId = 0;
	String category = Enums.CategoryType.getCategoryType(CategoryType.CAT1);
	private int year;
	private int month;
	private int day;
	private Boolean isSmoking;
	static final int DATE_DIALOG_ID = 999;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		scrollLayout = (ScrollView) findViewById(R.id.newPatientLayout);
		hivResult = (Spinner) findViewById(R.id.hivResult);
		hivLayout = (LinearLayout) findViewById(R.id.hivLayout);
		txtViewTreatmentid = (TextView) findViewById(R.id.txtViewTreatmentIdAdd);
		txtViewErrorShow = (TextView) findViewById(R.id.txtviewPatientErrorAdd);
		editTxtName = (EditText) findViewById(R.id.edittxtPatientNameAdd);
		editTxtPhone = (EditText) findViewById(R.id.edittxtPatientPhoneAdd);
		spinnerStage = (Spinner) findViewById(R.id.spinnerPatientStageAdd);
		spinnerSchedule = (Spinner) findViewById(R.id.spinnerPatientScheduleAdd);
		spinnerCategory = (Spinner) findViewById(R.id.spinnerPatientCategoryAdd);
		spinnerCenter = (Spinner) findViewById(R.id.spinnerPatientCenterAdd);
		txtViewCenter = (TextView) findViewById(R.id.txtViewCenter);
		selectGender = (Spinner) findViewById(R.id.selectGender);
		editTxtAge = (EditText) findViewById(R.id.age);
		chkMoit = (CheckBox) findViewById(R.id.chkMoit);
		editTxtIP = (EditText) findViewById(R.id.edit_txt_IP);
		editTxtEx_IP = (EditText) findViewById(R.id.edit_txt_EX_IP);
		editTxtCP = (EditText) findViewById(R.id.edit_txt_CP);

		doseTakenLayout = (LinearLayout) findViewById(R.id.doseTakenLayout);
		IPDoseLayout = (LinearLayout) findViewById(R.id.IPDoseLayout);
		ExtIpDoseLayout = (LinearLayout) findViewById(R.id.ExtIpDoseLayout);
		CPDoseLayout = (LinearLayout) findViewById(R.id.CPDoseLayout);

		labDate = (EditText) findViewById(R.id.labDate);
		labDmc = (EditText) findViewById(R.id.labDmc);
		labNo = (EditText) findViewById(R.id.labno);
		labweight = (EditText) findViewById(R.id.labweight);
		spinnerdiseaseClassification = (Spinner) findViewById(R.id.spinnerPatientDiseaseAdd);
		spinnerPatientType = (Spinner) findViewById(R.id.spinnerPatientType);
		spinnerLabMonth = (Spinner) findViewById(R.id.spinnerPatientLabMonth);
		edtAddress = (EditText) findViewById(R.id.patientAddress);
		edtDiseaseSite = (EditText) findViewById(R.id.DiseaseSite);
		edtNikshayId = (EditText) findViewById(R.id.nikashayId);
		edtTbnumber = (EditText) findViewById(R.id.tbnumber);
		spinnerSmokingHistory = (Spinner) findViewById(R.id.spinnersmokinghistory);
		diseaseSite_layout = (LinearLayout) findViewById(R.id.diseaseSite_layout);

		// Added two new field Aadhaar Number and PatientSource
		edtAadhaarno = (EditText) findViewById(R.id.adharno);
		spinnerPatientSource = (Spinner) findViewById(R.id.spinnerPatientSource);
		otherpatientlayout = (LinearLayout) findViewById(R.id.otherpatientlayout);
		otherpatientType = (EditText) findViewById(R.id.otherpatientType);

		loadDefaultValues();
		if (!ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_hiv_enable, this).equals("1")) {
			isHivEnable = false;
			hivLayout.setVisibility(View.GONE);
		}
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			treatmentId = extras.getString(IntentKeys.key_treatment_id);
			txtViewTreatmentid.setText(treatmentId);
			oldID = true;
			try {
				ReportType reportType = (ReportType) extras
						.get(IntentKeys.key_intent_from);
				if (reportType == ReportType.positiveContact) {
					oldID = false;
					editTxtName.setText(extras
							.getString(IntentKeys.key_patient_Name));
					editTxtPhone.setText(extras
							.getString(IntentKeys.key_phone_no));
					editTxtAge.setText(extras
							.getString(IntentKeys.key_patient_age));
					String gender = extras
							.getString(IntentKeys.key_patient_gender);
					if (gender.startsWith("M")) {
						selectGender.setSelection(1);
					} else {
						selectGender.setSelection(2);
					}
				}
			} catch (Exception e) {
			}
		} else {
			treatmentId = getNewTreatmentId();
			txtViewTreatmentid.setText(treatmentId);
		}

		spinnerInitialLab = (Spinner) findViewById(R.id.spinnerPatientLabResult);
		ArrayList<String> LabResult = InitialLabMasterOperations.getResults(
				Schema.MASTER_LAB_IS_ACTIVE + "=1", this);
		ArrayAdapter<String> labAdapter = new ArrayAdapter<String>(this,
				R.layout.lab_spinner_row, LabResult);
		spinnerInitialLab.setAdapter(labAdapter);
		spinnerInitialLab.setSelection(0);

		spinnerPatientType
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						PatientType = spinnerPatientType.getSelectedItem()
								.toString();
						if (spinnerPatientType.getSelectedItemPosition() == 6) {
							otherpatientlayout.setVisibility(View.VISIBLE);
						} else {
							otherpatientlayout.setVisibility(View.GONE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				category = ((TextView) findViewById(R.id.spinnerCategory))
						.getText().toString();
				if (!category.equals(getResources().getString(
						R.string.selectCategory))) {
					loadStageSpinner(category);
					loadScheduleByCategory(category);
					return;

				} else {
					loadStageSpinner(category);
					loadScheduleSpinner(
							Enums.StageType.getStageType(StageType.IP)
									.toString(), category);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinnerdiseaseClassification
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spinnerdiseaseClassification
								.getSelectedItemPosition() == 2) {
							diseaseSite_layout.setVisibility(View.VISIBLE);
						} else {
							diseaseSite_layout.setVisibility(View.GONE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		labDate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);

				return false;
			}
		});

		spinnerStage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				stage = ((TextView) findViewById(R.id.spinnerStage)).getText()
						.toString();
				if (stage.equals("IP")) {
					doseTakenLayout.setVisibility(View.VISIBLE);
					IPDoseLayout.setVisibility(View.VISIBLE);
					ExtIpDoseLayout.setVisibility(View.GONE);
					CPDoseLayout.setVisibility(View.GONE);

				} else if (stage.equals("CP")) {
					doseTakenLayout.setVisibility(View.VISIBLE);
					IPDoseLayout.setVisibility(View.VISIBLE);
					ExtIpDoseLayout.setVisibility(View.VISIBLE);
					CPDoseLayout.setVisibility(View.VISIBLE);

				} else if (stage.equals("Ext-IP")) {
					doseTakenLayout.setVisibility(View.VISIBLE);
					IPDoseLayout.setVisibility(View.VISIBLE);
					ExtIpDoseLayout.setVisibility(View.VISIBLE);
					CPDoseLayout.setVisibility(View.GONE);
				}
				loadScheduleSpinner(stage, category);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinnerSmokingHistory
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						if (spinnerSmokingHistory.getSelectedItem().toString()
								.equals("Yes")) {
							isSmoking = true;
						} else if (spinnerSmokingHistory.getSelectedItem()
								.toString().equals("No")) {
							isSmoking = false;
						} else {
							isSmoking = false;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		spinnerInitialLab
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						spinnerdiseaseClassification.setEnabled(true);
						if (position > 0 && position < 2) {
							spinnerdiseaseClassification.setSelection(2);
							spinnerdiseaseClassification.setEnabled(false);
						} else {
							if (position >= 2) {
								spinnerdiseaseClassification.setSelection(1);
								spinnerdiseaseClassification.setEnabled(false);
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		if (((eComplianceApp) this.getApplication()).IsIris) {
			chkMoit.setVisibility(View.INVISIBLE);
		}

		spinnerLabMonth.setSelection(1);
		spinnerLabMonth.setEnabled(false);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		if (id == DATE_DIALOG_ID) {
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog2 = new DatePickerDialog(this, date, year,
					month, day);
			dialog2.getDatePicker().setCalendarViewShown(false);
			dialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
			return dialog2;
		}

		return null;
	}

	private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int day, int month, int year) {
			labDate.setText(new StringBuilder().append(day).append("-")
					.append(month + 1).append("-").append(year));
		}
	};

	public void onSaveClick(View v) {
		String centerTabId = tabId;
		if (machineType.equals("C")) {
			center = ((TextView) findViewById(R.id.spinnerCenter)).getText()
					.toString().trim();
			centerTabId = CentersOperations.getMachineId(center, this);
		}

		// Check Tab Before registration
		if (centerTabId.equals("")) {
			try {
				Intent intent = new Intent(this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, getResources()
						.getString(R.string.errorTabId));
				intent.putExtra(IntentKeys.key_signal_type, "Bad");
				this.finish();
				startActivity(intent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				return;
			} catch (Exception e) {
				Intent intent = new Intent(this, HomeActivity.class);
				this.finish();
				startActivity(intent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				return;
			}
		}
		if (spinnerPatientType.getSelectedItemPosition() == 6) {
			PatientType = otherpatientType.getText().toString().trim();
		}

		int centerid = CentersOperations.getCenterId(centerTabId, this);
		// Check Center ID before registration
		if (centerid < 1) {
			try {
				Intent intent = new Intent(this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, getResources()
						.getString(R.string.errorCenterId));
				intent.putExtra(IntentKeys.key_signal_type, "Bad");
				this.finish();
				startActivity(intent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				return;
			} catch (Exception e) {
				Intent intent = new Intent(this, HomeActivity.class);
				this.finish();
				startActivity(intent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				return;
			}
		}

		name = editTxtName.getText().toString().trim();
		phone = editTxtPhone.getText().toString().trim();

		if (VerifyBlankField() == false) {
			txtViewErrorShow.setText(verifyTextBox.toString().trim());
			getRedAnimation();
			verifyTextBox.delete(0, verifyTextBox.length());
			return;
		}

		if (validateTextField() == false) {
			txtViewErrorShow.setText(validateTextBox.toString().trim());
			getRedAnimation();
			validateTextBox.delete(0, validateTextBox.length());
			return;
		}

		GpsTracker tracker = new GpsTracker(this);
		if (tracker.canGetLocation()) {
			LocationOperations.addLocation(this, treatmentId,
					tracker.getLatitude(), tracker.getLongitude());
		}

		tracker = null;

		GenUtils.setAppPendingToday(this);
		if (oldID = false) {
			OldPatientDetails();

		} else {

			Intent intent = new Intent(NewPatientActivity.this,
					EnrollActivity.class);

			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_used_device, this).equals("iris")) {
				intent = new Intent(NewPatientActivity.this,
						EnrollIrisActivity.class);
			}

			intent.putExtra(IntentKeys.key_treatment_id, treatmentId);
			intent.putExtra(IntentKeys.key_visitor_type,
					Enums.VisitorType.Patient.toString());
			try {

				intent.putExtra(IntentKeys.key_contact_id, getIntent()
						.getExtras().getString(IntentKeys.key_contact_id));
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (chkMoit.isChecked()) {
				intent.putExtra(IntentKeys.key_moit_status, false);
			}

			intent.putExtra(IntentKeys.key_patient_Name, name);
			intent.putExtra(IntentKeys.key_phone_no, phone);
			intent.putExtra(IntentKeys.key_patient_age, editTxtAge.getText()
					.toString());
			intent.putExtra(IntentKeys.key_current_stage, stage);
			schedule = ((TextView) findViewById(R.id.spinnerSchedule))
					.getText().toString();
			intent.putExtra(IntentKeys.key_current_schedule, schedule);
			intent.putExtra(IntentKeys.key_current_category, category);
			intent.putExtra(IntentKeys.key_patient_gender, selectGender
					.getSelectedItem().toString());
			intent.putExtra(IntentKeys.key_hiv_screening_result, hivResult
					.getSelectedItem().toString());

			intent.putExtra(IntentKeys.kay_patient_lab_month, spinnerLabMonth
					.getSelectedItemPosition() == 0 ? "" : spinnerLabMonth
					.getSelectedItem().toString());
			intent.putExtra(IntentKeys.key_patient_initial_lab,
					spinnerInitialLab.getSelectedItemPosition() == 0 ? ""
							: spinnerInitialLab.getSelectedItem().toString());
			intent.putExtra(
					IntentKeys.key_patient_lab_Date,
					labDate.getText().toString().length() == 0l ? "" : GenUtils
							.datetolong(labDate.getText().toString()));
			intent.putExtra(IntentKeys.key_patient_lab_DMC, labDmc.getText()
					.toString().length() == 0 ? "" : labDmc.getText()
					.toString());
			intent.putExtra(IntentKeys.key_patient_lab_No, labNo.getText()
					.toString().length() == 0 ? "" : labNo.getText().toString());
			intent.putExtra(IntentKeys.key_patient_lab_Weight, labweight
					.getText().toString().length() == 0 ? "" : labweight
					.getText().toString());
			intent.putExtra(IntentKeys.key_tab_id, centerTabId);
			intent.putExtra(IntentKeys.key_patient_prior_IPdose, editTxtIP
					.getText().toString());
			intent.putExtra(IntentKeys.key_patient_prior_CPdose, editTxtCP
					.getText().toString());
			intent.putExtra(IntentKeys.key_patient_prior_ExtIPdose,
					editTxtEx_IP.getText().toString());

			intent.putExtra(IntentKeys.key_patient_address, edtAddress
					.getText().toString().length() == 0 ? "" : edtAddress
					.getText().toString());
			intent.putExtra(IntentKeys.key_patient_diseaseSite, edtDiseaseSite
					.getText().toString().length() == 0 ? "" : edtDiseaseSite
					.getText().toString());
			intent.putExtra(
					IntentKeys.key_patient_disease,
					spinnerdiseaseClassification.getSelectedItemPosition() == 0 ? ""
							: spinnerdiseaseClassification.getSelectedItem()
									.toString());
			intent.putExtra(IntentKeys.key_patient_Type, spinnerPatientType
					.getSelectedItemPosition() == 0 ? "" : PatientType);

			intent.putExtra(IntentKeys.key_patient_nikshayId, edtNikshayId
					.getText().toString().length() == 0 ? "" : edtNikshayId
					.getText().toString());
			intent.putExtra(IntentKeys.key_patient_tbNumber, edtTbnumber
					.getText().toString().length() == 0 ? "" : edtTbnumber
					.getText().toString());
			intent.putExtra(IntentKeys.key_patient_smoking_history, isSmoking);
			intent.putExtra(IntentKeys.key_patient_aadhaarNo, edtAadhaarno
					.getText().toString().trim());
			intent.putExtra(IntentKeys.key_patient_patientSource,
					spinnerPatientSource.getSelectedItem().toString());
			startActivity(intent);
			this.finish();
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		}

	}

	public void onCancelClick(View v) {
		toHome();
	}

	private void loadCategorySpinner() {
		regimenList = RegimenMasterOperations.getAllCategoryForSpinner(true,
				this);
		if (!regimenList.isEmpty()) {
			categoryAdapter = new SpinnerCategoryAdapter(this, regimenList);
			spinnerCategory.setAdapter(categoryAdapter);
		}
	}

	private void loadCenterSpinner() {
		centerList = CentersOperations.getCenter(this);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("C")) {
					machineType = c.machineType;
					tabId = c.machineID;
					centerAdapter = new SpinnerCenterAdapter(this,
							CentersOperations.getCenterForSpinner(true, this));
					spinnerCenter.setAdapter(centerAdapter);
					return;
				}
			}
		}
		if (machineType.equals("P")) {
			spinnerCenter.setVisibility(View.GONE);
			txtViewCenter.setVisibility(View.GONE);
			if (!centerList.isEmpty()) {
				for (Center c : centerList) {
					if (c.machineType.equals("P")) {
						tabId = c.machineID;
						machineType = c.machineType;
						return;
					}
				}
			}
		}
	}

	private void loadStageSpinner(String category) {
		query = Schema.REGIMEN_MASTER_CATEGORY + "= '" + category + "'";
		regimenList = RegimenMasterOperations.getAllStage(query, this);
		if (!regimenList.isEmpty()) {
			stageAdapter = new SpinnerStageAdapter(this, regimenList);
			spinnerStage.setAdapter(stageAdapter);
			return;
		}
	}

	private void loadScheduleSpinner(String stage, String category) {
		if (category.equals(getResources().getString(R.string.selectCategory))) {
			category = Enums.CategoryType.getCategoryType(CategoryType.CAT1);
		}
		query = Schema.REGIMEN_MASTER_STAGE + "= '" + stage + "' and "
				+ Schema.REGIMEN_MASTER_CATEGORY + "= '" + category + "'";
		regimenList = RegimenMasterOperations.getAllSchedule(query, this);
		if (!regimenList.isEmpty()) {
			scheduleAdapter = new SpinnerScheduleAdapter(this, regimenList);
			spinnerSchedule.setAdapter(scheduleAdapter);
			String currentDay = getScheuldeSelection(stage);
			int tempindex = 0;
			for (Master sm : regimenList) {
				if (sm.schedule.equals(currentDay)) {
					spinnerSchedule.setSelection(tempindex);
					break;
				}
				tempindex++;
			}
			tempindex = 0;
			return;
		}

	}

	private void loadScheduleByCategory(String category) {
		loadScheduleSpinner(RegimenMasterOperations.getInitialStageByCategory(
				category, this), category);
	}

	private String getScheuldeSelection(String stage) {
		currentDay = GenUtils.getCurrentDay();
		if (stage.equals(Enums.StageType.getStageType(StageType.IP).toString())
				|| stage.equals(Enums.StageType.getStageType(StageType.ExtIP)
						.toString())) {
			if (currentDay.equals(Enums.Schedule.Monday.toString())
					|| currentDay.equals(Enums.Schedule.Wednesday.toString())
					|| currentDay.equals(Enums.Schedule.Friday.toString())) {
				currentDay = Enums.Schedule.MWF.toString();
			} else if (currentDay.equals(Enums.Schedule.Tuesday.toString())
					|| currentDay.equals(Enums.Schedule.Thursday.toString())
					|| currentDay.equals(Enums.Schedule.Saturday.toString())) {
				currentDay = Enums.Schedule.TThs.toString();
			}
		}
		return currentDay;
	}

	private void loadDefaultValues() {
		loadCategorySpinner();
		loadCenterSpinner();
		loadStageSpinner(Enums.CategoryType.getCategoryType(CategoryType.CAT1)
				.toString());
		loadScheduleSpinner(Enums.StageType.getStageType(StageType.IP)
				.toString(),
				Enums.CategoryType.getCategoryType(CategoryType.CAT1));
	}

	private boolean validateTextField() {
		Pattern ps = Pattern.compile("[a-zA-Z .]+");
		Matcher ms = ps.matcher(editTxtName.getText().toString().trim());
		boolean bs = ms.matches();
		Pattern ps1 = Pattern.compile("^[0-9]*$");
		Matcher ms1 = ps1.matcher(editTxtPhone.getText().toString().trim());
		boolean bs1 = ms1.matches();
		Matcher ageMatcher = ps1.matcher(editTxtAge.getText().toString());
		boolean ageMat = ageMatcher.matches();
		schedule = ((TextView) findViewById(R.id.spinnerSchedule)).getText()
				.toString();
		if (bs == false) {
			validateTextBox.append("\n"
					+ getResources().getString(R.string.enterCorrectName));
		}
		if ((editTxtPhone.getText().toString().trim().length() > 0 && editTxtPhone
				.getText().toString().trim().length() < 10)
				|| bs1 == false) {
			validateTextBox.append("\n"
					+ getResources().getString(R.string.enterCorrectPhone));
		}

		if (stage.equals("IP")) {

			if (category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT1))) {

				if (schedule.equals("Daily")) {

					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catI_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT1daily));
					}
				} else {

					if (Integer.parseInt(editTxtIP.getText().toString()) > 24) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT1));
					}
				}
			} else if (category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT2))) {
				if (schedule.equals("Daily")) {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catII_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT2daily));
					}
				} else {
					if (Integer.parseInt(editTxtIP.getText().toString()) > 36) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT2));
					}
				}
			}

		}
		if (stage.equals("Ext-IP")) {
			if (category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT1))) {
				if (schedule.equals("Daily")) {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catI_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT1daily));

					} else if (Integer.parseInt(editTxtIP.getText().toString()) <= IntentKeys.key_catI_IP_Daily - 1) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT1daily));
					}

					else if (Integer
							.parseInt(editTxtEx_IP.getText().toString()) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT1));

					}
				} else {
					if (Integer.parseInt(editTxtIP.getText().toString()) > 24) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT1));

					} else if (Integer.parseInt(editTxtIP.getText().toString()) <= 23) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT1));
					}

					else if (Integer
							.parseInt(editTxtEx_IP.getText().toString()) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT1));

					}
				}

			} else if (category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT2))) {

				if (schedule.equals("Daily")) {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catII_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT2daily));
					} else if (Integer.parseInt(editTxtIP.getText().toString()) <= IntentKeys.key_catII_IP_Daily - 1) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT2daily));
					} else if (Integer.parseInt(editTxtEx_IP.getText()
							.toString()) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT2));

					}
				} else {
					if (Integer.parseInt(editTxtIP.getText().toString()) > 36) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT2));
					} else if (Integer.parseInt(editTxtIP.getText().toString()) <= 35) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT2));
					} else if (Integer.parseInt(editTxtEx_IP.getText()
							.toString()) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT2));

					}
				}

			}

		}

		if (stage.equals("CP")) {
			String Ex_IP_Doss = editTxtEx_IP.getText().toString().length() == 0 ? "0"
					: editTxtEx_IP.getText().toString();
			if (category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT1))) {
				if (schedule.equals("Daily")) {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catI_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT1daily));
					} else if (Integer.parseInt(editTxtIP.getText().toString()) <= IntentKeys.key_catI_IP_Daily - 1
							&& Integer.parseInt(editTxtIP.getText().toString()) != 24) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT1daily));
					} else if (Integer.parseInt(Ex_IP_Doss) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT1));

					} else if (Integer.parseInt(editTxtCP.getText().toString()) > IntentKeys.key_catI_CP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxCPDoseCAT1daily));

					} else if (Integer.parseInt(Ex_IP_Doss) <= 11
							&& Integer.parseInt(Ex_IP_Doss) != 0) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessExtIPDoseCAT1));

					}
				} else {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catI_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT1daily));
					}else if (Integer.parseInt(editTxtIP.getText().toString()) <= IntentKeys.key_catI_IP_Daily - 1
							&& Integer.parseInt(editTxtIP.getText().toString()) != 24) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT1daily));
					}else if (Integer.parseInt(Ex_IP_Doss) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT1));

					} else if (Integer.parseInt(editTxtCP.getText().toString()) > 18) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxCPDoseCAT1));

					} else if (Integer.parseInt(Ex_IP_Doss) <= 11
							&& Integer.parseInt(Ex_IP_Doss) != 0) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessExtIPDoseCAT1));

					}
				}

			} else if (category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT2))) {
				if (schedule.equals("Daily")) {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catII_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT2daily));
					} else if (Integer.parseInt(editTxtIP.getText().toString()) <= IntentKeys.key_catII_IP_Daily - 1
							&& Integer.parseInt(editTxtIP.getText().toString()) != 36) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT2daily));
					} else if (Integer.parseInt(Ex_IP_Doss) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT2));

					} else if (Integer.parseInt(editTxtCP.getText().toString()) > IntentKeys.key_catII_CP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxCPDoseCAT2daily));

					} else if (Integer.parseInt(Ex_IP_Doss) <= 11
							&& Integer.parseInt(Ex_IP_Doss) != 0) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessExtIPDoseCAT2));

					}
				} else {
					if (Integer.parseInt(editTxtIP.getText().toString()) > IntentKeys.key_catII_IP_Daily) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxIPDoseCAT2daily));
					}else if (Integer.parseInt(editTxtIP.getText().toString()) <= IntentKeys.key_catII_IP_Daily - 1
							&& Integer.parseInt(editTxtIP.getText().toString()) != 36) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessIPDoseCAT2daily));
					}else if (Integer.parseInt(Ex_IP_Doss) > 12) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxExtIPDoseCAT2));

					} else if (Integer.parseInt(editTxtCP.getText().toString()) > 22) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.maxCPDoseCAT2));

					} else if (Integer.parseInt(Ex_IP_Doss) <= 11
							&& Integer.parseInt(Ex_IP_Doss) != 0) {
						validateTextBox.append("\n"
								+ getResources().getString(
										R.string.lessExtIPDoseCAT2));

					}
				}
			}

		}

		if (!ageMat) {
			validateTextBox.append("\n"
					+ getResources().getString(R.string.enterCorrectAge));
		} else if (editTxtAge.getText().toString().length() > 0) {
			try {
				int age = Integer.parseInt(editTxtAge.getText().toString());
				if (age <= 0 || age > 120) {
					validateTextBox.append("\n"
							+ getResources()
									.getString(R.string.enterCorrectAge));
				}
			} catch (Exception e) {

			}
		}
		if (validateTextBox.toString().trim().length() != 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean VerifyBlankField() {

		if (spinnerPatientType.getSelectedItemPosition() == 6) {
			if (otherpatientType.getText().toString().trim().length() == 0) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.otherpatienthint));
			}
		}
		if (editTxtName.getText().toString().trim().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterName));
		}
		if (category.equals(getResources().getString(R.string.selectCategory))) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectCategory));
		}
		if (machineType.equals("C")) {
			if (center.equals(getResources().getString(R.string.selectCenter))) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.selectCenter));
			}
		}

		if (selectGender.getSelectedItemId() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectGender));
		}
		if (editTxtAge.getText().toString().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterAge));

		}

		if (stage.equals("IP")) {
			if (editTxtIP.getText().toString().length() == 0) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.enterIP));
			}
		}
		if (stage.equals("Ext-IP")) {
			if (editTxtEx_IP.getText().toString().length() == 0) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.enterExtIP));
			}

		}

		if (stage.equals("CP")) {
			if (editTxtIP.getText().toString().length() == 0) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.enterExtIP));
			}

			if (editTxtCP.getText().toString().length() == 0) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.enterCP));

			}
		}

		if (spinnerInitialLab.getSelectedItem().toString()
				.equals(getResources().getString(R.string.selectLabResult))) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectLabResult));
		}

		if (spinnerLabMonth.getSelectedItemPosition() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectmonth));
		}
		if (spinnerdiseaseClassification.getSelectedItemPosition() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectdisease));
		}

		if (labDate.getText().toString().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterlabdate));
		}
		if (labDmc.getText().toString().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterlabdmc));
		}
		if (labNo.getText().toString().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterlabno));
		}
		if (labweight.getText().toString().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterpatientweight));
		}

		if (spinnerdiseaseClassification.getSelectedItemPosition() == 2) {
			if (edtDiseaseSite.getText().toString().length() == 0) {
				verifyTextBox.append("\n"
						+ getResources().getString(R.string.enterdiseaseSite));
			}
		}
		if (isHivEnable) {
			if (hivResult.getSelectedItemPosition() == 0) {
				verifyTextBox
						.append("\n"
								+ getResources().getString(
										R.string.selectScreenResult));
			}
		}

		if (spinnerPatientType.getSelectedItemPosition() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectpatienttype));
		}

		if (edtAadhaarno.getText().toString().trim().length() > 0
				&& edtAadhaarno.getText().toString().trim().length() < 12) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enteradharnumber));
		}

		if (verifyTextBox.toString().trim().length() != 0) {
			return false;

		} else {
			return true;
		}
	}

	private void getRedAnimation() {
		scrollLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) scrollLayout
				.getBackground();
		txtViewErrorShow
				.setBackgroundResource(R.drawable.black_to_red_transition);
		TransitionDrawable transition2 = (TransitionDrawable) txtViewErrorShow
				.getBackground();
		transition1.startTransition(0);
		transition2.startTransition(0);
		transition1.reverseTransition(6000);
		transition2.reverseTransition(6000);
	}

	private String getNewTreatmentId() {
		return tabId + (AppStateConfigurationOperations.getMaxId(this) + 1);
	}

	private void toHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
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
				toHome();
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

	public void OldPatientDetails() {

		String centerTabId = tabId;
		long currentTime = System.currentTimeMillis();
		if (machineType.equals("C")) {
			center = ((TextView) findViewById(R.id.spinnerCenter)).getText()
					.toString().trim();
			centerTabId = CentersOperations.getMachineId(center, this);
		}
		PatientsOperations.addPatient(treatmentId, name, "Active", phone,
				tabId, true, currentTime, ((eComplianceApp) this
						.getApplication()).LastLoginId, false, GenUtils
						.getCurrentDateLong(), CentersOperations.getCenterId(
						centerTabId, this), edtAddress.getText().toString(),
				edtDiseaseSite.getText().toString(),
				spinnerdiseaseClassification.getSelectedItem().toString(),
				PatientType, edtNikshayId.getText().toString(), edtTbnumber
						.getText().toString(), isSmoking, this);
		int ag = Integer.parseInt(editTxtAge.getText().toString());
		PatientGenderAgeOperations.addGenderAge(treatmentId, selectGender
				.getSelectedItem().toString(), ag, System.currentTimeMillis(),
				false, ((eComplianceApp) this.getApplication()).LastLoginId,
				this);
		PatientDosePriorOperations.addPatient(treatmentId, editTxtIP.getText()
				.toString(), editTxtEx_IP.getText().toString(), editTxtCP
				.getText().toString(), currentTime, false, this);
		TreatmentInStagesOperations.addTreatmentStage(treatmentId,
				RegimenMasterOperations.getRegimenId(
						Schema.REGIMEN_MASTER_CATEGORY + "= '" + category
								+ "' and " + Schema.REGIMEN_MASTER_STAGE + "='"
								+ stage + "' and "
								+ Schema.REGIMEN_MASTER_SCHEDULE + "= '"
								+ schedule + "'", this), GenUtils
						.getCurrentDateLong(), System.currentTimeMillis(),
				((eComplianceApp) this.getApplication()).LastLoginId, false,
				this);
		PatientV2_Operations.addPatient(treatmentId, edtAadhaarno.getText()
				.toString(), spinnerPatientSource.getSelectedItem().toString(),
				currentTime,
				((eComplianceApp) this.getApplication()).LastLoginId, false,
				this);
		GenUtils.setAppPendingToday(this);
		Intent i = new Intent(this, EnrollActivity.class);

		// Send to Iris Enrollment
		if (((eComplianceApp) this.getApplication()).IsIris) {
			i = new Intent(NewPatientActivity.this, EnrollIrisActivity.class);
		}
		i.putExtra(IntentKeys.key_treatment_id, treatmentId);
		try {
			i.putExtra(IntentKeys.key_contact_id, getIntent().getExtras()
					.getString(IntentKeys.key_contact_id));
		} catch (Exception e) {
		}
		i.putExtra(IntentKeys.key_visitor_type,
				Enums.VisitorType.Patient.toString());
		this.finish();
		startActivity(i);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

}