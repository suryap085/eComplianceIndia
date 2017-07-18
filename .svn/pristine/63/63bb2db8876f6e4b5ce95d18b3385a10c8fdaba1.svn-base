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
import java.util.HashMap;
import java.util.List;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerStatusAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientStatusMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectStatusActivity extends Activity {
	ArrayList<Master> statusList;
	SpinnerStatusAdapter StatusAdapter;
	Spinner statusSpinner, centerSpinner;
	LinearLayout newCenter;
	ScrollView selectStatusLayout;
	String status;
	Bundle extra;
	TextView error;
	String category, treatmentID, stage;
	HashMap<String, String> centerIds;
	ArrayAdapter<String> labAdapter;
	final Context context = this;
	ArrayList<PatientDoseTakenPriorViewModel> patientdosetakenlist;
	int IPDoseCount, CPDoseCount, ExtIPDoseCount;
	String[] outcomearr={"Switched on XDR","Outcome pending"};

	String showAll = "1";
	int IP_UnsupervisedDose_Count = 0, CP_UnsupervisedDose_Count = 0,
			Ext_IPUnsupervisedDose_Count = 0, totalDoseCount = 0,
			IP_SupervisedDose_Count = 0, CP_SupervisedDose_Count = 0,
			Ext_IPSupervisedDose_Count = 0, selfAdminDose = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extra = getIntent().getExtras();
		setContentView(R.layout.activity_select_status);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		statusSpinner = (Spinner) findViewById(R.id.spinnerStatusEdit);
		selectStatusLayout = (ScrollView) findViewById(R.id.selectStatusLayout);
		error = (TextView) findViewById(R.id.errorMassage);
		newCenter = (LinearLayout) findViewById(R.id.newCenterSelectLayout);
		try {
			showAll = extra.getString(IntentKeys.key_patient_initial_lab)
					.toString();
			category = extra.getString(IntentKeys.key_patient_current_category);
			treatmentID = extra.getString(IntentKeys.key_treatment_id);
			stage = extra.getString(IntentKeys.key_current_stage);

		} catch (Exception e) {

		}

		loadStatusSpinner();
		statusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String temStatus = ((TextView) findViewById(R.id.spinnerStatus))
						.getText().toString();
				if (temStatus.equals(Enums.StatusType
						.getStatusType(StatusType.TransferredInternally))) {
					newCenter.setVisibility(View.VISIBLE);
					centerSpinner = (Spinner) findViewById(R.id.spinnerNewCenter);
					loadCenterAdapter();
					centerSpinner.setAdapter(labAdapter);
				} else {
					newCenter.setVisibility(View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	protected void loadCenterAdapter() {
		if (centerIds == null) {
			centerIds = new HashMap<String, String>();
			String allCenter = ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_centers_in_zone, this);
			centerIds.put(getResources().getString(R.string.selectCenter), "0");
			if (!allCenter.equals("")) {
				String[] list = allCenter.split(",");

				for (String s : list) {
					String[] idCenter = s.split("-");
					String id = idCenter[0];
					String center = idCenter[1];
					centerIds.put(center, id);
				}
			}

			labAdapter = new ArrayAdapter<String>(this,
					R.layout.lab_spinner_row, new ArrayList<String>(
							centerIds.keySet()));
		}
	}

	public void onDoneClick(View v) {
		status = ((TextView) findViewById(R.id.spinnerStatus)).getText()
				.toString();
		String centerID = "0";
		String centerName = "";

		if (status.equals(Enums.StatusType
				.getStatusType(StatusType.TransferredInternally))) {

			try {
				centerName = centerSpinner.getSelectedItem().toString();
				centerID = centerIds.get(centerName);
			} catch (Exception e) {
			}
			if (centerName.equals(getResources().getString(
					R.string.selectCenter))) {

				error.setText(getResources().getString(R.string.selectCenter));
				getRedAnimation();
				return;

			}
		}
		// get patient doses taken at registration time
		Master regimen = TreatmentInStagesOperations.getPatientRegimen(
				treatmentID, context);
		patientdosetakenlist = PatientDosePriorOperations.getPatientDosePrior(
				Schema.PATIENT_DOSETAKEN_PRIOR_ID + "= '" + treatmentID
						+ "' and " + Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED
						+ "=0", context);
		if (patientdosetakenlist.size() > 0) {
			if (status.equals(Enums.StatusType
					.getStatusType(StatusType.TreatmentComplete))
					|| status.equals(Enums.StatusType
							.getStatusType(StatusType.Cured))) {

				if (PatientsOperations.patientExists(treatmentID, context)) {
					CP_UnsupervisedDose_Count = PatientDosePriorOperations
							.getIP_ExIP_DosesCount(
									treatmentID,
									category,
									"CP",
									Enums.DoseType.getDoseType(
											DoseType.Unsupervised).toString(),
									context);
					CP_SupervisedDose_Count = PatientDosePriorOperations
							.getIP_ExIP_DosesCount(
									treatmentID,
									category,
									"CP",
									Enums.DoseType.getDoseType(
											DoseType.Supervised).toString(),
									context)
							+ patientdosetakenlist.get(0).TakenCpDoses
							+ CP_UnsupervisedDose_Count;

					totalDoseCount = patientdosetakenlist.get(0).TakenCpDoses
							+ PatientDosePriorOperations.getSupervisdDoseCount(
									treatmentID, context)
							+ CP_UnsupervisedDose_Count;
					selfAdminDose = PatientDosePriorOperations
							.getSelfAdmidDosesCount(
									treatmentID,
									category,
									"CP",
									Enums.DoseType.getDoseType(
											DoseType.SelfAdministered)
											.toString(), context);

					if (regimen.catagory.equals(Enums.CategoryType
							.getCategoryType(CategoryType.CAT1))) {
						if (regimen.daysFrequency == 1) {
							totalDoseCount = totalDoseCount + selfAdminDose;
							if (totalDoseCount >= IntentKeys.key_catI_CP_Daily - 4)
								error.setText("Treatment Completed");
							else {
								error.setText(getResources().getString(
										R.string.notComplete));
								return;
							}
						} else {
							if (totalDoseCount > 16)
								error.setText("Treatment Completed");
							else {
								error.setText(getResources().getString(
										R.string.notComplete));
								return;
							}
						}
					}

					else if (regimen.catagory.equals(Enums.CategoryType
							.getCategoryType(CategoryType.CAT2))) {
						if (regimen.daysFrequency == 1) {
							totalDoseCount = totalDoseCount + selfAdminDose;
							if (totalDoseCount >= IntentKeys.key_catII_CP_Daily - 4)
								error.setText("Treatment Completed");
							else {
								error.setText(getResources().getString(
										R.string.notComplete));
								return;
							}
						} else {
							if (totalDoseCount > 20)
								error.setText("Treatment Completed");
							else {
								error.setText(getResources().getString(
										R.string.notComplete));
								return;
							}
						}
					}

				}
			}
		}

		Intent intent = this.getIntent();
		intent.putExtra(IntentKeys.key_current_status, status);
		intent.putExtra(IntentKeys.key_new_center_id, centerID);
		this.setResult(RESULT_OK, intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onCancelClick(View v) {
		Intent intent = this.getIntent();
		this.setResult(RESULT_CANCELED, intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void loadStatusSpinner() {
		statusList = PatientStatusMasterOperations.getStatus(
				Schema.PATIENT_STATUS_MASTER_IS_ACTIVE + "=1", this);
		Master master = new Master();
		if (!category.equals("MDR")) {
			if (showAll.equals("0")) // Remove Cure
			{
				List<Master> toRemove = new ArrayList<Master>();
				for (Master M : statusList) {
					if (M.name.equals("Cured")) {
						toRemove.add(M);
					}
				}
				if (!toRemove.isEmpty()) {
					for (Master i : toRemove) {
						statusList.remove(i);
					}
				}
			} else if (showAll.equals("1")) // Remove Treatment Complete
			{
				List<Master> toRemove = new ArrayList<Master>();
				for (Master M : statusList) {
					if (M.name.equals("Treatment Complete")) {
						toRemove.add(M);
					}
				}
				if (!toRemove.isEmpty()) {
					for (Master i : toRemove) {
						statusList.remove(i);
					}
				}
			}
		}

		if (category.equals("MDR") || category.equals("CAT-IV")) {
			List<Master> toRemove = new ArrayList<Master>();

			for (Master M : statusList) {
				if (M.name.equals("Cured") || M.name.equals("Switched on MDR")) {
					toRemove.add(M);

				}

			}
			if (!toRemove.isEmpty()) {
				for (Master i : toRemove) {
					statusList.remove(i);
				}

//				master.setName("Switched on XDR");
//				statusList.add(master);
				for(int i=0;i<outcomearr.length;i++)
				{
					Master ms=new Master();
					ms.setName(outcomearr[i]);
					statusList.add(ms);
				}
			}
		}

		if (!statusList.isEmpty()) {
			StatusAdapter = new SpinnerStatusAdapter(this, statusList);
			statusSpinner.setAdapter(StatusAdapter);
			int tempIndex = 0;
			for (Master M : statusList) {

				if (M.name.equals(extra
						.getString(IntentKeys.key_current_status).toString())) {
					statusSpinner.setSelection(tempIndex);
					return;
				}
				tempIndex++;
			}
			return;
		}

	}

	private void getRedAnimation() {
		selectStatusLayout
				.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) selectStatusLayout
				.getBackground();
		error.setBackgroundResource(R.drawable.black_to_red_transition);
		TransitionDrawable transition2 = (TransitionDrawable) error
				.getBackground();
		transition1.startTransition(0);
		transition2.startTransition(0);
		transition1.reverseTransition(6000);
		transition2.reverseTransition(6000);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}
