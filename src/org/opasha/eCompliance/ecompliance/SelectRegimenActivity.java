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
import java.util.List;

import org.opasha.eCompliance.ecompliance.Adapters.SpinnerCategoryAdapter;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerScheduleAdapter;
import org.opasha.eCompliance.ecompliance.Adapters.SpinnerStageAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.RegimenType;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectRegimenActivity extends Activity {

	TextView txtviewCategory;
	TextView txtviewStage;
	TextView txtviewSchedule;
	Spinner catagorySpinner;
	Spinner stageSpinner;
	Spinner scheduleSpinner, spinnerInitialLab;
	LinearLayout layout1;
	LinearLayout layout2;
	LinearLayout layout3, intialLabLayout;
	SpinnerCategoryAdapter catAdapter;
	SpinnerStageAdapter stageAdapter;
	SpinnerScheduleAdapter scheduleAdapter;
	ArrayList<Master> regimenList;
	String query, category, stage;
	Bundle extras;
	String currentDay;
	boolean newIntent = true;
	int tempIndex;
	ArrayList<PatientDoseTakenPriorViewModel> patientdosetakenlist;
	int IPDoseCount, ExtIPDoseCount;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_regimen);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		extras = getIntent().getExtras();
		layout1 = (LinearLayout) findViewById(R.id.layoutEditPatientRegimen1);
		layout2 = (LinearLayout) findViewById(R.id.layoutEditPatientRegimen2);
		layout3 = (LinearLayout) findViewById(R.id.layoutEditPatientRegimen3);
		intialLabLayout = (LinearLayout) findViewById(R.id.labResultLayout);

		patientdosetakenlist = PatientDosePriorOperations.getPatientDosePrior(
				Schema.PATIENT_DOSETAKEN_PRIOR_ID
						+ "= '"
						+ extras.getString(IntentKeys.key_treatment_id)
								.toString() + "' and "
						+ Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED + "=0",
				SelectRegimenActivity.this);

		if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Category)
						.toString())) {
			setRegimenForCategory();

		} else if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Stage).toString())) {
			setRegimenForStage();
			// loadInitialLabData();
		} else if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Schedule)
						.toString())) {
			setRegimenForSchedule();

		}

		if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Category)
						.toString())) {
			stageSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							stage = ((TextView) findViewById(R.id.spinnerStage))
									.getText().toString();
							loadScheduleSpinner(
									stage,
									((TextView) findViewById(R.id.spinnerCategory))
											.getText().toString(),
									scheduleSpinner);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
			catagorySpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							category = ((TextView) findViewById(R.id.spinnerCategory))
									.getText().toString();
							if (!category.equals(getResources().getString(
									R.string.selectCategory))) {
								if (newIntent == true) {
									newIntent = false;
									return;
								}

								loadStageSpinner(category, stageSpinner);
								loadScheduleByCategory(category,
										scheduleSpinner);
								return;

							} else {
								loadStageSpinner(
										extras.getString(
												IntentKeys.key_current_category)
												.toString(), stageSpinner);
								loadScheduleByCategory(
										extras.getString(
												IntentKeys.key_current_category)
												.toString(), scheduleSpinner);

							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});

		}
		if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Stage).toString())) {
			stageSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							stage = ((TextView) findViewById(R.id.spinnerStage))
									.getText().toString();
							if (newIntent == true) {
								newIntent = false;
								return;
							}

							loadScheduleSpinner(
									stage,
									extras.getString(
											IntentKeys.key_current_category)
											.toString(), scheduleSpinner);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
		}

	}

	public void onSaveClick(View v) {
		Intent intent = this.getIntent();
		if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Category)
						.toString())) {
			intent.putExtra(IntentKeys.key_current_category,
					((TextView) findViewById(R.id.spinnerCategory)).getText()
							.toString());
			intent.putExtra(IntentKeys.key_current_stage,
					((TextView) findViewById(R.id.spinnerStage)).getText()
							.toString());
			intent.putExtra(IntentKeys.key_current_schedule,
					((TextView) findViewById(R.id.spinnerSchedule)).getText()
							.toString());

		} else if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Stage).toString())) {
			intent.putExtra(IntentKeys.key_current_stage,
					((TextView) findViewById(R.id.spinnerStage)).getText()
							.toString());
			intent.putExtra(IntentKeys.key_current_schedule,
					((TextView) findViewById(R.id.spinnerSchedule)).getText()
							.toString());
			// if (isChangedStage) {
			// String labdata = spinnerInitialLab.getSelectedItem().toString();
			// intent.putExtra(IntentKeys.key_current_initial_lab_data,
			// spinnerInitialLab.getSelectedItem().toString());
			// }
		} else if (extras.getString(IntentKeys.key_regimen_type).equals(
				Enums.RegimenType.getRegimenType(RegimenType.Schedule)
						.toString())) {
			intent.putExtra(IntentKeys.key_current_schedule,
					((TextView) findViewById(R.id.spinnerSchedule)).getText()
							.toString());

		}
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

	private void setRegimenForSchedule() {
		txtviewSchedule = (TextView) findViewById(R.id.txtViewEditPatientRegimen1);
		scheduleSpinner = (Spinner) findViewById(R.id.spinnerEditPatientRegimen1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// params.gravity=100;
		params.width = 350;
		params.leftMargin = 100;

		layout2.setVisibility(View.GONE);
		layout1.setLayoutParams(params);
		layout3.setVisibility(View.GONE);

		txtviewSchedule.setText(getResources().getString(R.string.schedule));

		loadScheduleSpinner(extras.getString(IntentKeys.key_current_stage)
				.toString(), extras.getString(IntentKeys.key_current_category)
				.toString(), scheduleSpinner);
		tempIndex = 0;
		for (Master M : regimenList) {
			if (M.schedule.equals(extras.getString(
					IntentKeys.key_current_schedule).toString())) {
				scheduleSpinner.setSelection(tempIndex);
				break;
			}
			tempIndex++;
		}
	}

	private void setRegimenForCategory() {
		txtviewCategory = (TextView) findViewById(R.id.txtViewEditPatientRegimen1);
		txtviewStage = (TextView) findViewById(R.id.txtViewEditPatientRegimen2);
		txtviewSchedule = (TextView) findViewById(R.id.txtViewEditPatientRegimen3);
		catagorySpinner = (Spinner) findViewById(R.id.spinnerEditPatientRegimen1);
		stageSpinner = (Spinner) findViewById(R.id.spinnerEditPatientRegimen2);
		scheduleSpinner = (Spinner) findViewById(R.id.spinnerEditPatientRegimen3);
		txtviewCategory.setText(getResources().getString(R.string.category));
		txtviewStage.setText(getResources().getString(R.string.stage));
		txtviewSchedule.setText(getResources().getString(R.string.schedule));
		loadCategorySpinner(catagorySpinner);
		tempIndex = 0;
		for (Master M : regimenList) {
			if (M.catagory.equals(extras.getString(
					IntentKeys.key_current_category).toString())) {
				catagorySpinner.setSelection(tempIndex);

			}
			tempIndex++;
		}
		loadStageSpinner(extras.getString(IntentKeys.key_current_category)
				.toString(), stageSpinner);
		tempIndex = 0;
		for (Master S : regimenList) {
			if (S.stage.equals(extras.getString(IntentKeys.key_current_stage)
					.toString())) {
				stageSpinner.setSelection(tempIndex);

			}
			tempIndex++;
		}
		loadScheduleSpinner(extras.getString(IntentKeys.key_current_stage)
				.toString(), extras.getString(IntentKeys.key_current_category)
				.toString(), scheduleSpinner);
		tempIndex = 0;
		for (Master E : regimenList) {
			if (E.schedule.equals(extras.getString(
					IntentKeys.key_current_schedule).toString())) {
				scheduleSpinner.setSelection(tempIndex);

			}
			tempIndex++;
		}
	}

	private void setRegimenForStage() {
		txtviewStage = (TextView) findViewById(R.id.txtViewEditPatientRegimen1);
		txtviewSchedule = (TextView) findViewById(R.id.txtViewEditPatientRegimen2);
		stageSpinner = (Spinner) findViewById(R.id.spinnerEditPatientRegimen1);
		scheduleSpinner = (Spinner) findViewById(R.id.spinnerEditPatientRegimen2);
		spinnerInitialLab = (Spinner) findViewById(R.id.spinnerPatientLabResult);

		layout3.setVisibility(View.GONE);
		txtviewStage.setText(getResources().getString(R.string.stage));
		txtviewSchedule.setText(getResources().getString(R.string.schedule));

		loadStageSpinner(extras.getString(IntentKeys.key_current_category)
				.toString(), stageSpinner);
		tempIndex = 0;
		for (Master M : regimenList) {
			if (M.stage.equals(extras.getString(IntentKeys.key_current_stage)
					.toString())) {
				stageSpinner.setSelection(tempIndex);
				break;
			}
			tempIndex++;
		}
		loadScheduleByCategory(extras
				.getString(IntentKeys.key_current_category).toString(),
				scheduleSpinner);
		tempIndex = 0;
		for (Master M : regimenList) {
			if (M.schedule.equals(extras.getString(
					IntentKeys.key_current_schedule).toString())) {
				scheduleSpinner.setSelection(tempIndex);
				break;
			}
			tempIndex++;
		}
	}

	private void loadCategorySpinner(Spinner sample) {
		regimenList = RegimenMasterOperations.getAllCategoryForSpinner(false,
				this);
		if (!regimenList.isEmpty()) {
			if (patientdosetakenlist.size() > 0) {

				if (patientdosetakenlist.get(0).TakenIpDoses > 0) {
					sample.setEnabled(false);

				} else if (patientdosetakenlist.get(0).TakenExtIpDoses > 0) {
					sample.setEnabled(false);
				} else if (patientdosetakenlist.get(0).TakenCpDoses > 0) {
					sample.setEnabled(false);
				} else if (DoseAdminstrationOperations
						.getDosesCountExceptMissed(
								extras.getString(IntentKeys.key_treatment_id)
										.toString(),
								regimenList.get(0).regimenId, this) > 0) {
					sample.setEnabled(false);
				}

			}
			catAdapter = new SpinnerCategoryAdapter(this, regimenList);
			sample.setAdapter(catAdapter);
		}
	}

	private void loadStageSpinner(String category, Spinner sample) {
		query = Schema.REGIMEN_MASTER_CATEGORY + "= '" + category + "'";
		regimenList = RegimenMasterOperations.getAllStage(query, this);
		List<Master> toRemove = new ArrayList<Master>();

		if (patientdosetakenlist.size() > 0) {
			IPDoseCount = patientdosetakenlist.get(0).TakenIpDoses

					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(extras
							.getString(IntentKeys.key_treatment_id).toString(),
							category, Enums.StageType
									.getStageType(StageType.IP), Enums.DoseType
									.getDoseType(DoseType.Supervised)
									.toString(), this)
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(extras
							.getString(IntentKeys.key_treatment_id).toString(),
							category, Enums.StageType
									.getStageType(StageType.IP), Enums.DoseType
									.getDoseType(DoseType.Unsupervised)
									.toString(), this)
					+ PatientDosePriorOperations.getSelfAdmidDosesCount(extras
							.getString(IntentKeys.key_treatment_id).toString(),
							category, Enums.StageType
									.getStageType(StageType.IP), Enums.DoseType
									.getDoseType(DoseType.SelfAdministered)
									.toString(), this);

			ExtIPDoseCount = patientdosetakenlist.get(0).TakenExtIpDoses
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(extras
							.getString(IntentKeys.key_treatment_id).toString(),
							category, Enums.StageType
									.getStageType(StageType.ExtIP),
							Enums.DoseType.getDoseType(DoseType.Supervised)
									.toString(), this)
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(extras
							.getString(IntentKeys.key_treatment_id).toString(),
							category, Enums.StageType
									.getStageType(StageType.ExtIP),
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this);
		}

		Master master = TreatmentInStagesOperations.getPatientRegimen(
				extras.getString(IntentKeys.key_treatment_id), this);

		if (!regimenList.isEmpty()) {

			if (patientdosetakenlist.size() > 0) {
				int doses = 0;
				if (category.equals(extras
						.getString(IntentKeys.key_current_category))
						|| category.equals(master.catagory)) {
					if (extras.getString(IntentKeys.key_current_stage).equals(
							Enums.StageType.getStageType(StageType.ExtIP))) {

						if (category.equals(Enums.CategoryType
								.getCategoryType(CategoryType.CAT1))) {
							doses = 34;
						}
						if (category.equals(Enums.CategoryType
								.getCategoryType(CategoryType.CAT2))) {
							doses = 46;
						}

						for (Master M : regimenList) {
							if (IPDoseCount + ExtIPDoseCount < doses) {
								if (M.stage.equals(Enums.StageType
										.getStageType(StageType.IP))
										|| M.stage.equals(Enums.StageType
												.getStageType(StageType.CP))) {
									toRemove.add(M);

								}
							} else {
								if (M.stage.equals(Enums.StageType
										.getStageType(StageType.IP))) {
									toRemove.add(M);

								}
							}

						}
						if (!toRemove.isEmpty()) {
							for (Master i : toRemove) {
								regimenList.remove(i);
							}

						}
					} else if (extras.getString(IntentKeys.key_current_stage)
							.equals(Enums.StageType.getStageType(StageType.CP))) {
						for (Master M : regimenList) {

							if (M.stage.equals(Enums.StageType
									.getStageType(StageType.IP))
									|| M.stage.equals(Enums.StageType
											.getStageType(StageType.ExtIP))) {
								toRemove.add(M);

							}
						}

						if (!toRemove.isEmpty()) {
							for (Master i : toRemove) {
								regimenList.remove(i);
							}

						}
					}

					else if (extras.getString(IntentKeys.key_current_stage)
							.equals(Enums.StageType.getStageType(StageType.IP))) {
						if (category.equals(Enums.CategoryType
								.getCategoryType(CategoryType.CAT1))) {
							if (master.daysFrequency == 1) {
								doses = IntentKeys.key_catI_IP_Daily - 4;
							} else {
								doses = 22;
							}
						}
						if (category.equals(Enums.CategoryType
								.getCategoryType(CategoryType.CAT2))) {
							if (master.daysFrequency == 1) {
								doses = IntentKeys.key_catII_IP_Daily - 4;
							} else {
								doses = 34;
							}
						}
						for (Master M : regimenList) {
							if (IPDoseCount < doses) {
								if (M.stage.equals(Enums.StageType
										.getStageType(StageType.CP))
										|| M.stage.equals(Enums.StageType
												.getStageType(StageType.ExtIP))) {
									toRemove.add(M);

								}
							}
						}

						if (!toRemove.isEmpty()) {
							for (Master i : toRemove) {
								regimenList.remove(i);
							}

						}
					}
					stageAdapter = new SpinnerStageAdapter(this, regimenList);
					sample.setAdapter(stageAdapter);
					return;
				}
			} else {
				stageAdapter = new SpinnerStageAdapter(this, regimenList);
				sample.setAdapter(stageAdapter);
				return;
			}
		}
	}

	private void loadScheduleSpinner(String stage, String category,
			Spinner sample) {
		if (category.equals(getResources().getString(R.string.selectCategory))) {
			category = Enums.CategoryType.getCategoryType(CategoryType.CAT1);
		}
		query = Schema.REGIMEN_MASTER_STAGE + "= '" + stage + "' and "
				+ Schema.REGIMEN_MASTER_CATEGORY + "= '" + category + "'";
		regimenList = RegimenMasterOperations.getAllSchedule(query, this);
		if (!regimenList.isEmpty()) {
			scheduleAdapter = new SpinnerScheduleAdapter(this, regimenList);
			sample.setAdapter(scheduleAdapter);
			currentDay = getScheuldeSelection(stage);
			int tempindex = 0;
			for (Master sm : regimenList) {
				if (sm.schedule.equals(currentDay)) {
					sample.setSelection(tempindex);
					break;
				}
				tempindex++;
			}
			tempindex = 0;
			return;
		}

	}

	private void loadScheduleByCategory(String category, Spinner sample) {

		loadScheduleSpinner(RegimenMasterOperations.getInitialStageByCategory(
				category, this), category, sample);

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

	@Override
	public void onBackPressed() {
		this.finish();

		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}
