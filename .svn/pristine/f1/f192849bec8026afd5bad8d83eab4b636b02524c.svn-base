/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opasha.eCompliance.ecompliance.R.color;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerCenterAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.HIVOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialLabMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientGenderAgeOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabFollowUpOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabsOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientV2_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.Model.PatientLabFollowUpModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientAgeGender;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientLabs;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDetails2ViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.IntentFrom;
import org.opasha.eCompliance.ecompliance.util.Enums.RegimenType;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class EditPatientActivity extends Activity {
	EditText editTxtName, editTxtPhone, edtTxtAge, labDate, labDmc, labNo,
			labweight, edtAddress, edtDiseaseSite, edtNikshayId, edtTbnumber,
			edtaadhaarNo, otherpatientType;
	Spinner selectGender, spinnerInitialLab, spinnerdiseaseClassification,
			spinnerPatientType, spinnerLabMonth, spinnerSmokingHistory;
	final Context context = this;
	ScrollView mainLayout;
	TextView txtViewTreatmentID, txtViewCategory, txtViewStage,
			txtViewSchedule, txtViewStatus, txtViewCenter, txtViewErrorShow,
			btnChangeHiv, lblLabResult;
	Button btnCategory, btnStage, btnSchedule, btnStatus, btnCenter, btnCancel;
	private StringBuilder verifyTextBox = new StringBuilder();
	private StringBuilder validateTextBox = new StringBuilder();
	boolean isPatientChanged = false;
	boolean isTreatmentChanged = false;
	boolean isHivChange = false;
	boolean isLabDataChange = false;
	String currentHIV, PatientType;
	int centerId;
	LinearLayout centerLayout, hivLayout;
	ArrayList<Patient> patientlist;
	ArrayList<PatientLabFollowUpModel> patientlabfollowuplist;

	PatientAgeGender patientAge;
	ArrayList<Master> regimenlist;
	String treatmentID, machineId;
	ArrayList<Center> centerList;
	String currentCenter, CurrentStatus, CurrentStage, CurrentSchedule,
			currentCategory;
	SpinnerCenterAdapter centerAdapter;
	String machineType = "P";
	Bundle extras;
	ArrayList<PatientDoseTakenPriorViewModel> patientdosetakenlist;
	ArrayList<PatientDetails2ViewModel> patientV2;
	int IPDoseCount, ExtIPDoseCount, CPDoseCount;
	String intialLabResult = "Lab Result : ";
	private LinearLayout diseaseSite_layout, categoryLayoutEdit,
			otherpatientlayout;
	private int year;
	private int month;
	private int day;
	private Boolean isSmoking;
	private ArrayList<String> labMonthList;
	private ArrayAdapter<String> labMonthAdapter;
	private ArrayList<String> LabResult;

	@SuppressLint("DefaultLocale")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_patient);
		extras = getIntent().getExtras();
		hivLayout = (LinearLayout) findViewById(R.id.hivLayout);
		btnChangeHiv = (TextView) findViewById(R.id.btnChangeHiv);

		mainLayout = (ScrollView) findViewById(R.id.editPatientLayout);
		txtViewTreatmentID = (TextView) findViewById(R.id.txtViewTreatmentIdEdit);
		txtViewErrorShow = (TextView) findViewById(R.id.txtviewPatientErrorEdit);
		editTxtName = (EditText) findViewById(R.id.edittxtPatientNameEdit);
		editTxtPhone = (EditText) findViewById(R.id.edittxtPatientPhoneEdit);
		txtViewCategory = (TextView) findViewById(R.id.txtViewCurrentCategory);
		txtViewStage = (TextView) findViewById(R.id.txtViewCurrentStage);
		txtViewSchedule = (TextView) findViewById(R.id.txtViewCurrentSchedule);
		txtViewStatus = (TextView) findViewById(R.id.txtViewCurrentStatus);
		txtViewCenter = (TextView) findViewById(R.id.txtViewCurrentCenter);
		lblLabResult = (TextView) findViewById(R.id.lblLabResult);
		btnCategory = (Button) findViewById(R.id.btnChangeCategory);
		btnStage = (Button) findViewById(R.id.btnChangeStage);
		btnStatus = (Button) findViewById(R.id.btnchangeStatus);
		btnSchedule = (Button) findViewById(R.id.btnChangeSchedule);
		btnCenter = (Button) findViewById(R.id.btnChangeCenter);
		btnCancel = (Button) findViewById(R.id.btnPatientCancelEdit);
		centerLayout = (LinearLayout) findViewById(R.id.centerLayout);
		treatmentID = extras.getString(IntentKeys.key_treatment_id);
		txtViewTreatmentID.setText(treatmentID);
		edtTxtAge = (EditText) findViewById(R.id.age);
		selectGender = (Spinner) findViewById(R.id.selectGender);

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
		spinnerInitialLab = (Spinner) findViewById(R.id.spinnerPatientLabResult);
		edtaadhaarNo = (EditText) findViewById(R.id.adharno);
		categoryLayoutEdit = (LinearLayout) findViewById(R.id.categoryLayoutEdit);
		otherpatientlayout = (LinearLayout) findViewById(R.id.otherpatientlayout);
		otherpatientType = (EditText) findViewById(R.id.otherpatientType);
		labMonthList = new ArrayList<String>();
		labMonthList.add("Select Lab Month");
		labMonthList.add("Pretreatment");
		labMonthList.add("End IP");
		labMonthList.add("Extended-IP");
		labMonthList.add("End treatment");
		lblLabResult.setVisibility(View.GONE);

		LabResult = InitialLabMasterOperations.getResults(
				Schema.MASTER_LAB_IS_ACTIVE + "=1", this);
		ArrayAdapter<String> labAdapter = new ArrayAdapter<String>(this,
				R.layout.lab_spinner_row, LabResult);
		spinnerInitialLab.setAdapter(labAdapter);

		patientdosetakenlist = PatientDosePriorOperations.getPatientDosePrior(
				Schema.PATIENT_DOSETAKEN_PRIOR_ID + "= '" + treatmentID
						+ "' and " + Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED
						+ "=0", context);

		patientlist = PatientsOperations.getPatients(
				Schema.PATIENTS_TREATMENT_ID + "= '" + treatmentID + "' and "
						+ Schema.PATIENTS_IS_DELETED + "=0", this);
		patientV2 = PatientV2_Operations.getPatients(
				Schema.PATIENT_TREATMENT_ID + "= '" + treatmentID + "' and "
						+ Schema.PATIENT_IS_DELETED + "=0", this);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

			Date date1 = sdf
					.parse(GenUtils.longToDateString(patientlist.get(0).RegDate));

			Date date2 = sdf.parse("01/07/16");

			if (date1.compareTo(date2) < 0) {
				labMonthList.add("2 Months CP");
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		labMonthAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, labMonthList);
		labMonthAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLabMonth.setAdapter(labMonthAdapter);
		patientlabfollowuplist = PatientLabFollowUpOperations
				.getlastPreTreatmentLab(treatmentID, this);

		ArrayList<PatientLabs> patLab = PatientLabsOperation.getLabs(
				Schema.LAB_TREATMENT_ID + "= '" + treatmentID + "'", this);
		labDate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showDialog(333);

				return false;
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
		spinnerdiseaseClassification
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
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

		if (!patLab.isEmpty()) {
			intialLabResult += InitialLabMasterOperations.getValue(
					patLab.get(0).LabResult, this);
		}

		// lblLabResult.setText(intialLabResult);

		if (patientlist.get(0).hivResult.equals("")) {
			hivLayout.setVisibility(View.GONE);
		}
		if (patientlist.get(0).hivResult.split(",")[0].equals("-ve")) {
			hivLayout.setVisibility(View.GONE);
		}
		if (!ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_hiv_enable, this).equals("1")) {
			hivLayout.setVisibility(View.GONE);
		}
		currentHIV = patientlist.get(0).hivResult;
		patientAge = PatientGenderAgeOperations.getAgeGender(treatmentID, this);
		try {
			edtTxtAge.setText(String.valueOf(patientAge.Age));
			if (patientAge.Gender.trim().toLowerCase().equals("male")) {
				selectGender.setSelection(1);
			} else {
				selectGender.setSelection(2);
			}
		} catch (Exception e) {

		}
		regimenlist = RegimenMasterOperations.getRegimen(
				Schema.REGIMEN_MASTER_ID
						+ "= "
						+ TreatmentInStagesOperations.getPatientRegimenId(
								treatmentID, this), this);

		if (patientdosetakenlist.size() > 0) {
			int doses = 0;
			if (regimenlist.get(0).catagory.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT1))
					|| regimenlist.get(0).catagory.equals(Enums.CategoryType
							.getCategoryType(CategoryType.CAT2))) {
				if (regimenlist.get(0).stage.equals(Enums.StageType
						.getStageType(StageType.IP))
						|| regimenlist.get(0).stage.equals(Enums.StageType
								.getStageType(StageType.ExtIP))) {

					IPDoseCount = patientdosetakenlist.get(0).TakenIpDoses

							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType.getStageType(StageType.IP),
									Enums.DoseType.getDoseType(
											DoseType.Supervised).toString(),
									context)
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType.getStageType(StageType.IP),
									Enums.DoseType.getDoseType(
											DoseType.Unsupervised).toString(),
									context)
							+ PatientDosePriorOperations
									.getSelfAdmidDosesCount(
											treatmentID,
											regimenlist.get(0).catagory,
											Enums.StageType
													.getStageType(StageType.IP),
											Enums.DoseType.getDoseType(
													DoseType.SelfAdministered)
													.toString(), this);

					ExtIPDoseCount = patientdosetakenlist.get(0).TakenExtIpDoses
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType
											.getStageType(StageType.ExtIP),
									Enums.DoseType.getDoseType(
											DoseType.Supervised).toString(),
									context)
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType
											.getStageType(StageType.ExtIP),
									Enums.DoseType.getDoseType(
											DoseType.Unsupervised).toString(),
									context);

					// check condition
					if (regimenlist.get(0).catagory.equals(Enums.CategoryType
							.getCategoryType(CategoryType.CAT1))) {
						if (regimenlist.get(0).stage.equals(Enums.StageType
								.getStageType(StageType.IP))) {
							if (regimenlist.get(0).daysFrequency == 1) {
								doses = IntentKeys.key_catI_IP_Daily - 4;
							} else {
								doses = 22;
							}
							if (IPDoseCount < doses) {

								btnStage.setEnabled(false);
							}
							if ((IPDoseCount - patientdosetakenlist.get(0).TakenIpDoses) > 0) {
								btnCategory.setEnabled(false);
							}

						} else if (regimenlist.get(0).stage
								.equals(Enums.StageType
										.getStageType(StageType.ExtIP))) {

							if (IPDoseCount + ExtIPDoseCount < 34) {
								btnStage.setEnabled(false);
							}
							if ((IPDoseCount - patientdosetakenlist.get(0).TakenIpDoses)
									+ (ExtIPDoseCount - patientdosetakenlist
											.get(0).TakenExtIpDoses) > 0) {
								btnCategory.setEnabled(false);
							}
						}

					} else if (regimenlist.get(0).catagory
							.equals(Enums.CategoryType
									.getCategoryType(CategoryType.CAT2))) {
						if (regimenlist.get(0).stage.equals(Enums.StageType
								.getStageType(StageType.IP))) {

							if (regimenlist.get(0).daysFrequency == 1) {
								doses = IntentKeys.key_catII_IP_Daily - 4;
							} else {
								doses = 34;
							}

							if (IPDoseCount < doses) {

								btnStage.setEnabled(false);
							}
							if ((IPDoseCount - patientdosetakenlist.get(0).TakenIpDoses) > 0) {
								btnCategory.setEnabled(false);
							}

						} else if (regimenlist.get(0).stage
								.equals(Enums.StageType
										.getStageType(StageType.ExtIP))) {

							if (IPDoseCount + ExtIPDoseCount < 46) {
								btnStage.setEnabled(false);
							}
							if ((IPDoseCount - patientdosetakenlist.get(0).TakenIpDoses)
									+ (ExtIPDoseCount - patientdosetakenlist
											.get(0).TakenExtIpDoses) > 0) {
								btnCategory.setEnabled(false);
							}
						}

					}

				} else if (regimenlist.get(0).stage.equals(Enums.StageType
						.getStageType(StageType.CP))) {
					btnStage.setEnabled(false);
					IPDoseCount = patientdosetakenlist.get(0).TakenIpDoses

							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType.getStageType(StageType.IP),
									Enums.DoseType.getDoseType(
											DoseType.Supervised).toString(),
									context)
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType.getStageType(StageType.IP),
									Enums.DoseType.getDoseType(
											DoseType.Unsupervised).toString(),
									context)
							+ PatientDosePriorOperations
									.getSelfAdmidDosesCount(
											treatmentID,
											regimenlist.get(0).catagory,
											Enums.StageType
													.getStageType(StageType.IP),
											Enums.DoseType.getDoseType(
													DoseType.SelfAdministered)
													.toString(), this);

					ExtIPDoseCount = patientdosetakenlist.get(0).TakenExtIpDoses
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType
											.getStageType(StageType.ExtIP),
									Enums.DoseType.getDoseType(
											DoseType.Supervised).toString(),
									context)
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType
											.getStageType(StageType.ExtIP),
									Enums.DoseType.getDoseType(
											DoseType.Unsupervised).toString(),
									context);
					CPDoseCount = patientdosetakenlist.get(0).TakenCpDoses

							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType.getStageType(StageType.CP),
									Enums.DoseType.getDoseType(
											DoseType.Supervised).toString(),
									context)
							+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
									treatmentID,
									regimenlist.get(0).catagory,
									Enums.StageType.getStageType(StageType.CP),
									Enums.DoseType.getDoseType(
											DoseType.Unsupervised).toString(),
									context)
							+ PatientDosePriorOperations
									.getSelfAdmidDosesCount(
											treatmentID,
											regimenlist.get(0).catagory,
											Enums.StageType
													.getStageType(StageType.CP),
											Enums.DoseType.getDoseType(
													DoseType.SelfAdministered)
													.toString(), this);
					if ((IPDoseCount - patientdosetakenlist.get(0).TakenIpDoses)
							+ (ExtIPDoseCount - patientdosetakenlist.get(0).TakenExtIpDoses)
							+ (CPDoseCount - patientdosetakenlist.get(0).TakenCpDoses) > 0) {
						btnCategory.setEnabled(false);
					}
				}

			}

		}

		try {
			if (patientlabfollowuplist.size() >= 0) {

				ArrayList<PatientLabFollowUpModel> _allLab = PatientLabFollowUpOperations
						.getlastLabData(Schema.PATIENT_LAB_TREATMENTID + "='"
								+ treatmentID + "'", this);

				PatientLabFollowUpModel lastLab = _allLab
						.get(_allLab.size() - 1);

				labDate.setText(GenUtils.longtotring(lastLab.Lab_Date));
				labDmc.setText(lastLab.Lab_Dmc);
				labNo.setText(lastLab.Lab_No);
				labweight.setText(lastLab.Lab_Weight);
				if (lastLab.Lab_Month.equals("")) {
					spinnerLabMonth.setSelection(0);
				}

				else if (lastLab.Lab_Month.equals("Pretreatment")) {
					spinnerLabMonth.setSelection(1);
				} else if (lastLab.Lab_Month.equals("End IP")) {
					spinnerLabMonth.setSelection(2);
				} else if (lastLab.Lab_Month.equals("Extended-IP")) {
					spinnerLabMonth.setSelection(3);
				} else if (lastLab.Lab_Month.equals("End treatment")) {
					spinnerLabMonth.setSelection(4);
				} else if (lastLab.Lab_Month.equals("2 Months CP")) {
					spinnerLabMonth.setSelection(5);
				}

				if (LabResult.size() >= 0) {
					for (int i = 0; i < LabResult.size(); i++) {
						if (patientlabfollowuplist.get(0).Lab_Result
								.equals(LabResult.get(i).toString())) {
							spinnerInitialLab.setSelection(i);
						}
					}
				}

			}
		} catch (Exception e) {

		}

		try {
			if (patientV2.size() > 0) {
				edtaadhaarNo.setText(patientV2.get(0).AdhaarNo);
			}
			editTxtName.setText(patientlist.get(0).name);
			editTxtPhone.setText(patientlist.get(0).phoneNumber);
			edtAddress.setText(patientlist.get(0).address);
			edtNikshayId.setText(patientlist.get(0).nikshayId);
			edtTbnumber.setText(patientlist.get(0).tbNumber);

			// set disease value
			if (patientlist.get(0).disease.equals("")) {
				spinnerdiseaseClassification.setSelection(0);
			} else if (patientlist.get(0).disease.equals("Pulmonary")) {
				spinnerdiseaseClassification.setSelection(1);
			} else if (patientlist.get(0).disease.equals("Extra Pulmonary")) {
				spinnerdiseaseClassification.setSelection(2);
				diseaseSite_layout.setVisibility(View.VISIBLE);
				edtDiseaseSite.setText(patientlist.get(0).diseaseSite);
			}

			// set patientType value
			if (patientlist.get(0).patientType.equals("")) {
				spinnerPatientType.setSelection(0);
			} else if (patientlist.get(0).patientType.equals("New")) {
				spinnerPatientType.setSelection(1);
			} else if (patientlist.get(0).patientType.equals("Relapse")) {
				spinnerPatientType.setSelection(2);
			} else if (patientlist.get(0).patientType.equals("Transfer in")) {
				spinnerPatientType.setSelection(3);
			} else if (patientlist.get(0).patientType.equals("Failure")) {
				spinnerPatientType.setSelection(4);
			} else if (patientlist.get(0).patientType
					.equals("Treatment after default")) {
				spinnerPatientType.setSelection(5);
			} else {
				spinnerPatientType.setSelection(6);
				otherpatientlayout.setVisibility(View.VISIBLE);
				otherpatientType.setText(patientlist.get(0).patientType);
			}

			// set smokingHistory value
			if (patientlist.get(0).smokingHistory == null) {
				spinnerSmokingHistory.setSelection(0);
			}
			if (patientlist.get(0).smokingHistory == true) {
				spinnerSmokingHistory.setSelection(1);
			}
			if (patientlist.get(0).smokingHistory == false) {
				spinnerSmokingHistory.setSelection(2);
			}

		} catch (Exception e) {

		}

		try {
			txtViewCategory.setText(regimenlist.get(0).catagory);
			txtViewStage.setText(regimenlist.get(0).stage);
			txtViewSchedule.setText(regimenlist.get(0).schedule);
			currentCategory = regimenlist.get(0).catagory;
			CurrentStage = regimenlist.get(0).stage;
			CurrentSchedule = regimenlist.get(0).schedule;

		} catch (Exception e) {
			currentCategory = "";
			CurrentStage = "";
			CurrentSchedule = "";
		}
		txtViewStatus.setText(patientlist.get(0).Status);
		btnChangeHiv.setText(patientlist.get(0).hivResult);
		centerId = patientlist.get(0).centerId;
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		getCenterDetails();
		if (extras != null) {
			if (extras.getBoolean(IntentKeys.key_new_patient) == false) {
				centerLayout.setVisibility(View.GONE);
				machineId = patientlist.get(0).machineID;
			} else if (extras.getBoolean(IntentKeys.key_new_patient) == true) {
				if (machineType.equals("C")) {
					txtViewCenter.setText(CentersOperations.getCenter(
							patientlist.get(0).centerId, this));
					currentCenter = txtViewCenter.getText().toString();
				}
			}
		}
		if (machineType.equals("C")) {
			centerLayout.setVisibility(View.VISIBLE);
			txtViewCenter.setText(CentersOperations.getCenter(
					patientlist.get(0).centerId, this));
			currentCenter = txtViewCenter.getText().toString();
		}
		if (!patientlist.get(0).Status.equals(Enums.StatusType.getStatusType(
				StatusType.Active).toString())) {
			btnCategory.setVisibility(View.GONE);
			btnStage.setVisibility(View.GONE);
			btnSchedule.setVisibility(View.GONE);
			if (machineType.equals("C")) {
				btnCenter.setVisibility(View.GONE);
			}
		} else {
			btnCategory.setVisibility(View.VISIBLE);
			btnStage.setVisibility(View.VISIBLE);
			btnSchedule.setVisibility(View.VISIBLE);
			if (machineType.equals("C")) {
				btnCenter.setVisibility(View.VISIBLE);
			}
		}

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// custom dialog
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				// set the custom dialog components - text, image and button

				Button noButton = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				Button yesButton = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
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
		});

		// *AS 14/11/2016 - Disable Schedule Button. Schedule change will be
		// done by Implementation Team.
		btnSchedule.setEnabled(false);

		spinnerdiseaseClassification.setEnabled(false);

		spinnerLabMonth.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 1) {
					if (LabResult.size() >= 0) {
						for (int i = 0; i < LabResult.size(); i++) {
							if (patientlabfollowuplist.get(0).Lab_Result
									.equals(LabResult.get(i).toString())) {
								spinnerInitialLab.setSelection(i);
							}
						}
					}
					spinnerInitialLab.setEnabled(false);
				} else {
					spinnerInitialLab.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		if (spinnerLabMonth.getSelectedItemPosition() == 1) {
			spinnerInitialLab.setEnabled(false);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		if (id == 333) {
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

	public void btnChangeHiv(View v) {
		Intent selectResult = new Intent(this, SelectHivResultActivity.class);
		selectResult.putExtra(IntentKeys.key_hiv_screening_result,
				patientlist.get(0).hivResult);
		startActivityForResult(selectResult, 6);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onSaveClick(View v) {

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
		if (machineType.equals("C")) {
			// if (extras.getBoolean(IntentKeys.key_new_patient) == true) {
			machineId = CentersOperations.getMachineId(txtViewCenter.getText()
					.toString(), this);
			// }
		}
		if (spinnerPatientType.getSelectedItemPosition() == 6) {
			PatientType = otherpatientType.getText().toString().trim();
		}
		if (!isPatientChanged) {
			if (!editTxtName.getText().toString().trim()
					.equals(patientlist.get(0).name)
					|| !editTxtPhone
							.getText()
							.toString()
							.trim()
							.equals(String.valueOf(patientlist.get(0).phoneNumber))) {
				isPatientChanged = true;
			}
		}

		if (!isLabDataChange) {
			if (patientlabfollowuplist.size() > 0) {
				if (!patientlabfollowuplist.get(0).Lab_Month
						.equals(spinnerLabMonth.getSelectedItem().toString())
						|| !patientlabfollowuplist.get(0).Lab_Result
								.equals(spinnerInitialLab.getSelectedItem()
										.toString())
						|| !GenUtils.longtotring(
								patientlabfollowuplist.get(0).Lab_Date).equals(
								labDate.getText().toString())
						|| !patientlabfollowuplist.get(0).Lab_Dmc.equals(labDmc
								.getText().toString())
						|| !patientlabfollowuplist.get(0).Lab_No.equals(labNo
								.getText().toString())
						|| !patientlabfollowuplist.get(0).Lab_Weight
								.equals(labweight.getText().toString())) {
					isLabDataChange = true;
				}
			}
		}
		long currentTime = System.currentTimeMillis();
		long labdate;
		if (labDate.getText().toString().length() == 0) {
			labdate = 0l;
		} else {
			labdate = GenUtils.datetolong(labDate.getText().toString());
		}
		// if (isPatientChanged) {
		String status = txtViewStatus.getText().toString();
		PatientsOperations.addPatient(treatmentID, editTxtName.getText()
				.toString().trim(), status, editTxtPhone.getText().toString()
				.trim(), CentersOperations.getTabId(this),
				patientlist.get(0).isCounsellingPending, currentTime,
				((eComplianceApp) this.getApplicationContext()).LastLoginId,
				false, patientlist.get(0).RegDate, centerId, edtAddress
						.getText().toString().length() == 0 ? "" : edtAddress
						.getText().toString(), edtDiseaseSite.getText()
						.toString().length() == 0 ? "" : edtDiseaseSite
						.getText().toString(), spinnerdiseaseClassification
						.getSelectedItemPosition() == 0 ? ""
						: spinnerdiseaseClassification.getSelectedItem()
								.toString(), spinnerPatientType
						.getSelectedItemPosition() == 0 ? "" : PatientType,
				edtNikshayId.getText().toString().length() == 0 ? ""
						: edtNikshayId.getText().toString(), edtTbnumber
						.getText().toString().length() == 0 ? "" : edtTbnumber
						.getText().toString(), isSmoking, this);

		String patSource = "Operation Asha";

		try {
			patSource = patientV2.get(0).PatientSource;
			if (patSource == null || patSource.equals("")) {
				patSource = "Operation Asha";
			}
		} catch (Exception e2) {
			patSource = "Operation Asha";
		}

		PatientV2_Operations.addPatient(treatmentID, edtaadhaarNo.getText()
				.toString(), patSource, currentTime, ((eComplianceApp) this
				.getApplicationContext()).LastLoginId, false, context);

		try {

		} catch (Exception e) {
		}

		if (!status.equals(Enums.StatusType.getStatusType(StatusType.Active))) {
			
				isTreatmentChanged = false;
				TreatmentInStagesOperations
						.addTreatmentStage(
								treatmentID,
								0,
								GenUtils.getCurrentDateLong(),
								System.currentTimeMillis(),
								((eComplianceApp) this.getApplicationContext()).LastLoginId,
								false, context);
			
		}
		// }
		if (patientAge.Gender != null && patientAge.Age != 0) {
			int age = 0;
			try {
				age = Integer.parseInt(edtTxtAge.getText().toString());
			} catch (Exception e) {
			}

			if (!patientAge.Gender.equals(selectGender.getSelectedItem()
					.toString()) || patientAge.Age != age) {
				PatientGenderAgeOperations
						.addGenderAge(
								treatmentID,
								selectGender.getSelectedItem().toString(),
								age,
								System.currentTimeMillis(),
								false,
								((eComplianceApp) this.getApplicationContext()).LastLoginId,
								this);
			}
		} else {
			int age = 0;
			try {
				age = Integer.parseInt(edtTxtAge.getText().toString());
			} catch (Exception e) {
			}
			PatientGenderAgeOperations
					.addGenderAge(
							treatmentID,
							selectGender.getSelectedItem().toString(),
							age,
							currentTime,
							false,
							((eComplianceApp) this.getApplicationContext()).LastLoginId,
							this);

		}

		if (isTreatmentChanged) {
			TreatmentInStagesOperations
					.addTreatmentStage(
							treatmentID,
							RegimenMasterOperations.getRegimenId(
									Schema.REGIMEN_MASTER_CATEGORY
											+ "= '"
											+ txtViewCategory.getText()
													.toString()
											+ "' and "
											+ Schema.REGIMEN_MASTER_STAGE
											+ "='"
											+ txtViewStage.getText().toString()
											+ "' and "
											+ Schema.REGIMEN_MASTER_SCHEDULE
											+ "= '"
											+ txtViewSchedule.getText()
													.toString() + "'", this),
							GenUtils.getCurrentDateLong(),
							System.currentTimeMillis(),
							((eComplianceApp) this.getApplicationContext()).LastLoginId,
							false, this);
		}
		if (isHivChange) {
			String[] result = btnChangeHiv.getText().toString().split(",");
			HIVOperation
					.Add(treatmentID,
							result[0],
							result[1],
							((eComplianceApp) this.getApplicationContext()).LastLoginId,
							false, System.currentTimeMillis(), this);
		}
		if (isLabDataChange) {
			PatientLabFollowUpOperations.addPatientLabFollowUpData(treatmentID,
					spinnerLabMonth.getSelectedItemPosition() == 0 ? ""
							: spinnerLabMonth.getSelectedItem().toString(),
					spinnerInitialLab.getSelectedItemPosition() == 0 ? ""
							: spinnerInitialLab.getSelectedItem().toString(),
					labdate, labDmc.getText().toString().length() == 0 ? ""
							: labDmc.getText().toString(), labNo.getText()
							.toString().length() == 0 ? "" : labNo.getText()
							.toString(), labweight.getText().toString()
							.length() == 0 ? "" : labweight.getText()
							.toString(), ((eComplianceApp) this
							.getApplication()).LastLoginId, CentersOperations
							.getTabId(this), currentTime, false, this);
		}

		GenUtils.setAppPendingToday(this);
		GenUtils.setAppMissedDose(this);
		toHome(getResources().getString(R.string.patientSaveDetail));
	}

	private boolean validateTextField() {
		Pattern ps = Pattern.compile("[a-zA-Z .]+");
		Matcher ms = ps.matcher(editTxtName.getText().toString().trim());
		boolean bs = ms.matches();
		Pattern ps1 = Pattern.compile("^[0-9]*$");
		Matcher ms1 = ps1.matcher(editTxtPhone.getText().toString().trim());
		boolean bs1 = ms1.matches();
		Matcher ageMatcher = ps1.matcher(edtTxtAge.getText().toString());
		boolean ageMat = ageMatcher.matches();
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
		if (!ageMat) {
			validateTextBox.append("\n"
					+ getResources().getString(R.string.enterCorrectAge));
		} else if (edtTxtAge.getText().toString().length() > 0) {
			try {
				int age = Integer.parseInt(edtTxtAge.getText().toString());
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
		if (edtTxtAge.getText().toString().trim().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterAge));
		}
		if (selectGender.getSelectedItemPosition() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectGender));
		}
		if (spinnerPatientType.getSelectedItemPosition() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.selectpatienttype));
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

		if (edtaadhaarNo.getText().toString().trim().length() > 0
				&& edtaadhaarNo.getText().toString().trim().length() < 12) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enteradharnumber));
		}
		if (verifyTextBox.toString().trim().length() != 0) {
			return false;
		} else {
			return true;
		}
	}

	public void btnChangeCategory(View v) {
		Intent selectCategory = new Intent(this, SelectRegimenActivity.class);
		selectCategory.putExtra(IntentKeys.key_regimen_type, Enums.RegimenType
				.getRegimenType(RegimenType.Category).toString());
		selectCategory.putExtra(IntentKeys.key_current_category,
				txtViewCategory.getText().toString());
		selectCategory.putExtra(IntentKeys.key_current_stage, txtViewStage
				.getText().toString());
		selectCategory.putExtra(IntentKeys.key_current_schedule,
				txtViewSchedule.getText().toString());
		selectCategory.putExtra(IntentKeys.key_treatment_id, txtViewTreatmentID
				.getText().toString());
		startActivityForResult(selectCategory, 1);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	public void btnChangeStage(View v) {
		Intent selectSatge = new Intent(this, SelectRegimenActivity.class);
		selectSatge.putExtra(IntentKeys.key_regimen_type, Enums.RegimenType
				.getRegimenType(RegimenType.Stage).toString());
		selectSatge.putExtra(IntentKeys.key_current_category, txtViewCategory
				.getText().toString());
		selectSatge.putExtra(IntentKeys.key_current_schedule, txtViewSchedule
				.getText().toString());
		selectSatge.putExtra(IntentKeys.key_current_stage, txtViewStage
				.getText().toString());
		selectSatge.putExtra(IntentKeys.key_treatment_id, txtViewTreatmentID
				.getText().toString());
		startActivityForResult(selectSatge, 2);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	public void btnChangeSchedule(View v) {
		Intent selectSchedule = new Intent(this, SelectRegimenActivity.class);
		selectSchedule.putExtra(IntentKeys.key_regimen_type, Enums.RegimenType
				.getRegimenType(RegimenType.Schedule).toString());
		selectSchedule.putExtra(IntentKeys.key_current_category,
				txtViewCategory.getText().toString());
		selectSchedule.putExtra(IntentKeys.key_current_stage, txtViewStage
				.getText().toString());
		selectSchedule.putExtra(IntentKeys.key_current_schedule,
				txtViewSchedule.getText().toString());
		selectSchedule.putExtra(IntentKeys.key_treatment_id, txtViewTreatmentID
				.getText().toString());
		selectSchedule.putExtra(IntentKeys.key_current_stage, txtViewStage
				.getText().toString().trim());
		startActivityForResult(selectSchedule, 3);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	public void btnChangeStatus(View v) {

		// ArrayList<PatientLabs> patLab = PatientLabsOperation.getLabs(
		// Schema.LAB_TREATMENT_ID + "= '" + treatmentID + "'", this);

		String showAll = "1"; // Case when SP+ or not available
		int labId = 0;

		// if (!patLab.isEmpty()) {
		// if (patLab.get(0).LabResult < 3) {
		// showAll = "0";
		// }
		// }
		if (!patientlabfollowuplist.isEmpty()) {
			if (LabResult.size() >= 0) {
				for (int i = 0; i < LabResult.size(); i++) {
					if (patientlabfollowuplist.get(0).Lab_Result
							.equals(LabResult.get(i).toString())) {
						labId = i;
						break;
					}
				}
			}
		}
		if (!patientlabfollowuplist.isEmpty()) {
			if (labId < 3) {
				showAll = "0";
			}
		}

		Intent selectStatus = new Intent(this, SelectStatusActivity.class);
		selectStatus.putExtra(IntentKeys.key_current_status, txtViewStatus
				.getText().toString());
		selectStatus.putExtra(IntentKeys.key_patient_initial_lab, showAll);
		selectStatus.putExtra(IntentKeys.key_patient_current_category,
				txtViewCategory.getText().toString().trim());
		selectStatus.putExtra(IntentKeys.key_current_stage, txtViewStage
				.getText().toString().trim());
		selectStatus.putExtra(IntentKeys.key_treatment_id, txtViewTreatmentID
				.getText().toString());
		startActivityForResult(selectStatus, 4);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	public void btnChangeCenter(View v) {
		Intent selectCenter = new Intent(this, SelectCenterActivity.class);
		selectCenter.putExtra(IntentKeys.key_current_center, txtViewCenter
				.getText().toString());
		startActivityForResult(selectCenter, 5);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				txtViewCategory.setText(data
						.getStringExtra(IntentKeys.key_current_category));
				txtViewStage.setText(data
						.getStringExtra(IntentKeys.key_current_stage));
				txtViewSchedule.setText(data
						.getStringExtra(IntentKeys.key_current_schedule));
				if (!currentCategory.equals(data
						.getStringExtra(IntentKeys.key_current_category))) {
					txtViewCategory.setBackgroundResource(color.green);
					isTreatmentChanged = true;
					btnStage.setEnabled(true);

				} else {
					txtViewCategory
							.setBackgroundResource(R.drawable.textview_background);
				}
				if (!CurrentStage.equals(data
						.getStringExtra(IntentKeys.key_current_stage))) {
					txtViewStage.setBackgroundResource(color.green);
					isTreatmentChanged = true;

				} else {
					txtViewStage
							.setBackgroundResource(R.drawable.textview_background);

				}
				if (!CurrentSchedule.equals(data
						.getStringExtra(IntentKeys.key_current_schedule))) {
					txtViewSchedule.setBackgroundResource(color.green);
					isTreatmentChanged = true;
				} else {
					txtViewSchedule
							.setBackgroundResource(R.drawable.textview_background);
				}

			}

			if (resultCode == RESULT_CANCELED) {

				// Write your code on no result return

			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				txtViewStage.setText(data
						.getStringExtra(IntentKeys.key_current_stage));
				txtViewSchedule.setText(data
						.getStringExtra(IntentKeys.key_current_schedule));
				if (!CurrentStage.equals(data
						.getStringExtra(IntentKeys.key_current_stage))) {
					txtViewStage.setBackgroundResource(color.green);
					isTreatmentChanged = true;
					// initial_Lab_Data = data
					// .getStringExtra(IntentKeys.key_current_initial_lab_data);

				} else {
					txtViewStage
							.setBackgroundResource(R.drawable.textview_background);

				}
				if (!CurrentSchedule.equals(data
						.getStringExtra(IntentKeys.key_current_schedule))) {
					txtViewSchedule.setBackgroundResource(color.green);
					isTreatmentChanged = true;
				} else {
					txtViewSchedule
							.setBackgroundResource(R.drawable.textview_background);
				}

			}

			if (resultCode == RESULT_CANCELED) {

				// Write your code on no result return

			}
			break;
		case 3:
			if (resultCode == RESULT_OK) {
				txtViewSchedule.setText(data
						.getStringExtra(IntentKeys.key_current_schedule));
				if (!CurrentSchedule.equals(data
						.getStringExtra(IntentKeys.key_current_schedule))) {
					txtViewSchedule.setBackgroundResource(color.green);
					isTreatmentChanged = true;

				} else {
					txtViewSchedule
							.setBackgroundResource(R.drawable.textview_background);
				}

			}

			if (resultCode == RESULT_CANCELED) {

				// Write your code on no result return

			}
			break;
		case 4:
			if (resultCode == RESULT_OK) {
				txtViewStatus.setText(data
						.getStringExtra(IntentKeys.key_current_status));
				// Change centerId if centerId is other than 0
				if (!data.getStringExtra(IntentKeys.key_new_center_id).equals(
						"0")) {
					try {
						centerId = Integer.parseInt(data
								.getStringExtra(IntentKeys.key_new_center_id));
					} catch (Exception e) {
					}
				}
				if (!patientlist.get(0).Status.equals(data
						.getStringExtra(IntentKeys.key_current_status))) {
					txtViewStatus.setBackgroundResource(color.green);
					isPatientChanged = true;
					if (data.getStringExtra(IntentKeys.key_current_status)
							.equals(Enums.StatusType
									.getStatusType(StatusType.Active))) {
						isTreatmentChanged = true;
					}

				} else {
					txtViewStatus
							.setBackgroundResource(R.drawable.textview_background);
				}
				if (!data.getStringExtra(IntentKeys.key_current_status).equals(
						Enums.StatusType.getStatusType(StatusType.Active)
								.toString())) {

					btnCategory.setVisibility(View.GONE);
					btnStage.setVisibility(View.GONE);
					btnSchedule.setVisibility(View.GONE);
					if (machineType.equals("C")) {
						btnCenter.setVisibility(View.GONE);
					}
				} else {
					btnCategory.setVisibility(View.VISIBLE);
					btnStage.setVisibility(View.VISIBLE);
					btnSchedule.setVisibility(View.VISIBLE);
					if (machineType.equals("C")) {
						btnCenter.setVisibility(View.VISIBLE);
					}
				}

			}

			if (resultCode == RESULT_CANCELED) {

				// Write your code on no result return

			}
			break;
		case 5:
			if (resultCode == RESULT_OK) {
				String changeCenter = data
						.getStringExtra(IntentKeys.key_current_center);
				txtViewCenter.setText(data
						.getStringExtra(IntentKeys.key_current_center));
				if (!currentCenter.equals(changeCenter)) {
					txtViewCenter.setBackgroundResource(color.green);
					isPatientChanged = true;
					centerId = CentersOperations.getCenterIdByName(
							changeCenter, this);

				} else {
					txtViewCenter
							.setBackgroundResource(R.drawable.textview_background);
				}

			}

			if (resultCode == RESULT_CANCELED) {

				// Write your code on no result return

			}
			break;
		case 6:
			if (resultCode == RESULT_OK) {
				String finalResult = data
						.getStringExtra(IntentKeys.key_current_status);
				String screen = data
						.getStringExtra(IntentKeys.key_hiv_screening_result);
				btnChangeHiv.setText(screen + "," + finalResult);
				if (!currentHIV.equals(screen + "," + finalResult)) {
					btnChangeHiv.setBackgroundResource(color.green);
					isHivChange = true;
				} else {
					btnChangeHiv
							.setBackgroundResource(R.drawable.textview_background);
					isHivChange = false;
				}

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code on no result return
			}
			break;
		}
	}

	private void getRedAnimation() {
		mainLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) mainLayout
				.getBackground();

		transition1.startTransition(0);

		transition1.reverseTransition(6000);

	}

	private void getCenterDetails() {
		centerList = CentersOperations.getCenter(this);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("C")) {
					machineType = c.machineType;
					machineId = c.machineID;
					return;
				}
			}
		}
		if (machineType.equals("P")) {
			centerLayout.setVisibility(View.GONE);
			if (!centerList.isEmpty()) {
				for (Center c : centerList) {
					if (c.machineType.equals("P")) {
						machineId = c.machineID;
						machineType = c.machineType;
						return;
					}
				}
			}
		}
	}

	private void toHome() {
		Intent intent = new Intent();
		Enums.IntentFrom from = (IntentFrom) extras
				.get(IntentKeys.key_intent_from);
		switch (from) {
		case Home:
			intent = new Intent(this, HomeActivity.class);
			break;
		case Reports:
			intent = new Intent(this, PatientReportActivity.class);
			intent.putExtra(
					IntentKeys.key_report_type,
					(ReportType) getIntent().getExtras().get(
							IntentKeys.key_report_type));
			intent.putExtra(IntentKeys.key_intent_from,
					Enums.IntentFrom.Reports);
			break;
		default:
			break;
		}

		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void toHome(String str) {
		Intent intent = new Intent();
		try {
			Enums.IntentFrom from = (IntentFrom) extras
					.get(IntentKeys.key_intent_from);
			switch (from) {
			case Reports:
				intent = new Intent(this, PatientReportActivity.class);
				intent.putExtra(
						IntentKeys.key_report_type,
						(ReportType) getIntent().getExtras().get(
								IntentKeys.key_report_type));
				intent.putExtra(IntentKeys.key_intent_from,
						Enums.IntentFrom.Reports);
				break;
			case Home:
			default:
				intent = new Intent(this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, str);
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Good.toString());
				break;
			}

		} catch (Exception e) {
			intent = new Intent(this, HomeActivity.class);
			intent.putExtra(IntentKeys.key_message_home, str);
			intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		}
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public void onBackPressed() {
		toHome();

	}

}
