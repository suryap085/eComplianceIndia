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
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.UnSupervisedDoseReasonOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.UnsupervisedReasonMasterOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.Model.Reason;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.DoseUtils;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class UnsupervisedDoseActivity extends Activity {
	public Spinner numOfUnsupervisedDoseSpinner;
	public Spinner unsupervisedDoseReasonSpinner;
	public Button btnSave, btnCancel;
	Master regimen;
	LinearLayout unsupervisedLayout;
	TextView txtHeader;
	String treatmentID;
	Bundle extras;
	ArrayList<Reason> list = new ArrayList<Reason>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unsupervised_dose);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		extras = getIntent().getExtras();
		if (extras != null) {
			txtHeader = (TextView) findViewById(R.id.txtHeader);
			treatmentID = extras.getString(IntentKeys.key_treatment_id);
			txtHeader.setText(getResources().getString(
					R.string.unsupervised_Dose)
					+ " " + treatmentID);
			numOfUnsupervisedDoseSpinner = (Spinner) findViewById(R.id.spinUnsupervisedDose);
			addItemsOnUnsupervisedDoseSpinner();
			unsupervisedDoseReasonSpinner = (Spinner) findViewById(R.id.spinUnsupervisedDoseReason);
			addItemsOnUnsupervisedDoseReasonSpinner();
			btnSave = (Button) findViewById(R.id.btnSave);
			unsupervisedLayout = (LinearLayout) findViewById(R.id.unsupervisedDoseLayout);
			unsupervisedLayout
					.setBackgroundResource(R.drawable.grey_to_red_transition);
		}
	}

	public void addItemsOnUnsupervisedDoseSpinner() {
		int unsupervisedCount = 0;
		int availableLimit = 0;

		regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentID,
						this), this);

		if (regimen.stage.equals(Enums.StageType.getStageType(StageType.IP))
				|| regimen.stage.equals(Enums.StageType
						.getStageType(StageType.ExtIP))) {
			unsupervisedCount = PatientDosePriorOperations
					.getIP_ExIP_DosesCount(treatmentID, regimen.catagory, "IP",
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this)
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
							treatmentID, regimen.catagory, "Ext-IP",
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this);
			if(regimen.daysFrequency==1&&!regimen.catagory.equals("MDR"))
			{
				availableLimit = ((eComplianceApp) this.getApplication()).maxIPExIPUnsupervisedDoseforDailyRegimen
						- unsupervisedCount;
			}else{
			availableLimit = ((eComplianceApp) this.getApplication()).maxIPExIPUnsupervisedDose
					- unsupervisedCount;
			}
		} else if (regimen.stage.equals(Enums.StageType
				.getStageType(StageType.CP))) {
			unsupervisedCount = PatientDosePriorOperations
					.getIP_ExIP_DosesCount(treatmentID, regimen.catagory, "CP",
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this);
			if (regimen.daysFrequency == 1&&!regimen.catagory.equals("MDR")) {
				availableLimit = ((eComplianceApp) this.getApplication()).maxCPUnsupervisedDoseforDailyRegimen
						- unsupervisedCount;
			} else {
				availableLimit = ((eComplianceApp) this.getApplication()).maxCPUnsupervisedDose
						- unsupervisedCount;
			}
		}

		List<String> temp = new ArrayList<String>();

		int i = 1;
		while (availableLimit > 0) {
			temp.add(String.valueOf(i++));
			availableLimit--;
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, temp);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		numOfUnsupervisedDoseSpinner.setAdapter(dataAdapter);
	}

	public void addItemsOnUnsupervisedDoseReasonSpinner() {
		list = UnsupervisedReasonMasterOperations.getReason(
				Schema.REASON_MASTER_IS_ACTIVE + "= 1", this);
		ArrayAdapter<Reason> dataAdapter = new ArrayAdapter<Reason>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unsupervisedDoseReasonSpinner.setAdapter(dataAdapter);
		unsupervisedDoseReasonSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parentView,
							View childView, int position, long id) {
					}

					public void onNothingSelected(AdapterView<?> parentView) {
					}
				});
	}

	public void onSaveClick(View v) {
		int remainDose = 0;
		int value = Integer.parseInt(numOfUnsupervisedDoseSpinner
				.getSelectedItem().toString());
		regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentID,
						this), this);

		ArrayList<PatientDoseTakenPriorViewModel> patientdosetakenlist = PatientDosePriorOperations
				.getPatientDosePrior(Schema.PATIENT_DOSETAKEN_PRIOR_ID + "= '"
						+ treatmentID + "' and "
						+ Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED + "=0",
						this);
		if (patientdosetakenlist.size() > 0) {
			int doses = 0;
			int DoseCount = PatientDosePriorOperations.getIP_ExIP_DosesCount(
					treatmentID, regimen.catagory, regimen.stage,
					Enums.DoseType.getDoseType(DoseType.Supervised).toString(),
					this)
					+ PatientDosePriorOperations.getIP_ExIP_DosesCount(
							treatmentID, regimen.catagory, regimen.stage,
							Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString(), this)
					+ PatientDosePriorOperations
							.getSelfAdmidDosesCount(
									treatmentID,
									regimen.catagory,
									regimen.stage,
									Enums.DoseType.getDoseType(
											DoseType.SelfAdministered)
											.toString(), this);

			if (regimen.catagory.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT1))) {

				if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.IP))) {

					DoseCount = DoseCount
							+ patientdosetakenlist.get(0).TakenIpDoses;
					if (regimen.daysFrequency == 1) {
						doses = IntentKeys.key_catI_IP_Daily;
					} else {
						doses = 24;
					}
					remainDose = doses - DoseCount;
					if (value > remainDose) {
						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra(IntentKeys.key_message_home,
								"Can give only " + remainDose
										+ " Unsupervised Dose in current stage");
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Bad.toString());
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
						return;

					}
				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.ExtIP))) {
					DoseCount = DoseCount
							+ patientdosetakenlist.get(0).TakenExtIpDoses;

					remainDose = 12 - DoseCount;
					if (value > remainDose) {
						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra(IntentKeys.key_message_home,
								"Can give only " + remainDose
										+ " Unsupervised Dose in current stage");
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Bad.toString());
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
						return;

					}
				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.CP))) {
					DoseCount = DoseCount
							+ patientdosetakenlist.get(0).TakenCpDoses;

					if (regimen.schedule.equals("Daily")) {
						doses = IntentKeys.key_catI_CP_Daily;
					} else {
						doses = 18;
					}
					remainDose = doses - DoseCount;
					if (value > remainDose) {
						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra(IntentKeys.key_message_home,
								"Can give only " + remainDose
										+ " Unsupervised Dose in current stage");
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Bad.toString());
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
						return;

					}
				}

			} else if (regimen.catagory.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT2))) {

				if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.IP))) {
					DoseCount = DoseCount
							+ patientdosetakenlist.get(0).TakenIpDoses;
					if (regimen.daysFrequency == 1) {
						doses = IntentKeys.key_catII_IP_Daily;
					} else {
						doses = 36;
					}
					remainDose = doses - DoseCount;
					if (value > remainDose) {
						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra(IntentKeys.key_message_home,
								"Can give only " + remainDose
										+ " Unsupervised Dose in current stage");
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Bad.toString());
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
						return;

					}
				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.ExtIP))) {
					DoseCount = DoseCount
							+ patientdosetakenlist.get(0).TakenExtIpDoses;

					remainDose = 12 - DoseCount;
					if (value > remainDose) {
						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra(IntentKeys.key_message_home,
								"Can give only " + remainDose
										+ " Unsupervised Dose in current stage");
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Bad.toString());
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
						return;

					}
				} else if (regimen.stage.equals(Enums.StageType
						.getStageType(StageType.CP))) {
					DoseCount = DoseCount
							+ patientdosetakenlist.get(0).TakenCpDoses;
					if (regimen.schedule.equals("Daily")) {
						doses = IntentKeys.key_catII_CP_Daily;
					} else {
						doses = 22;
					}
					remainDose = doses - DoseCount;
					if (value > remainDose) {
						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra(IntentKeys.key_message_home,
								"Can give only " + remainDose
										+ " Unsupervised Dose in current stage");
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Bad.toString());
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);
						return;

					}
				}
			}
		}

		GpsTracker gps = new GpsTracker(this);

		long doseDate = GenUtils.getCurrentDateLong();
		int sefAdminDoses = DoseAdminstrationOperations.getUnsupervisedCount(
				treatmentID, this);
		Patient patient = DoseAdminstrationOperations
				.getlastSupervisedWithRegimen(treatmentID, this);

		Master reg = RegimenMasterOperations
				.getRegimen(patient.regimenID, this);

		Master PatReg = TreatmentInStagesOperations.getPatientRegimen(
				treatmentID, this);

		if (sefAdminDoses == 0) {
			doseDate = patient.doseDate
					+ (regimen.daysFrequency * GenUtils.ONE_DAY);
			int doseDay = GenUtils.dateToDay(patient.doseDate);
			if (!GenUtils.isScheduledOn(doseDay, PatReg.scheduleDays)) {
				if (reg.stage.equals("CP")) {
					double doseDay1 = 7 - Math.getExponent(PatReg.scheduleDays);
					if (doseDay > doseDay1) {
						double diff = 7 + doseDay1 - doseDay;
						doseDate += (diff * GenUtils.ONE_DAY);
					} else {
						if (doseDay < doseDay1) {
							double diff = doseDay1 - doseDay;
							doseDate += (diff * GenUtils.ONE_DAY);
						}
					}
				} else
					doseDate -= GenUtils.ONE_DAY;
			}
		} else {
			doseDate = DoseAdminstrationOperations.getlastUnSupervised(
					treatmentID, this)
					+ (regimen.daysFrequency * GenUtils.ONE_DAY);
		}
		long startDate = doseDate;

		if (reg.catagory.equals(Enums.CategoryType
				.getCategoryType(CategoryType.CAT1))
				|| reg.catagory.equals(Enums.CategoryType
						.getCategoryType(CategoryType.CAT2))) {
			if (reg.stage.equals(Enums.StageType.getStageType(StageType.CP))) {
				if (GenUtils.dateToDay(patient.doseDate)
						- GenUtils.ScheduleToDay(PatReg.schedule) == 1) {
					doseDate = startDate - 7 * GenUtils.ONE_DAY;
				}

			}
		}

		startDate = doseDate;
		for (int i = 0; i < value; i++) {
			if (!regimen.selfAdminSunday) {
				if (GenUtils.dateToDay(doseDate) < 2) {
					doseDate += GenUtils.ONE_DAY;
				} else if (regimen.schedule.equals("TThS")) {
					if (GenUtils.dateToDay(doseDate) == 2) {
						doseDate += GenUtils.ONE_DAY;
					}
				}
			}

			if (regimen.stage.equals("CP")) {
				if (regimen.schedule.equals("Saturday")
						|| regimen.schedule.equals("Tuesday")
						|| regimen.schedule.equals("Thursday")) {
					if (GenUtils.dateToDay(doseDate) == 2) {
						doseDate += GenUtils.ONE_DAY;
					}
				}

				DoseUtils
						.AddDose(
								treatmentID,
								Enums.DoseType.Unsupervised.toString(),
								doseDate,
								regimen.ID,
								System.currentTimeMillis(),
								((eComplianceApp) this.getApplicationContext()).LastLoginId,
								gps.getLatitude(), gps.getLongitude(), this);

				if (regimen.daysFrequency == 1) {
					if (GenUtils.dateToDay(doseDate) == 7) {
						doseDate += GenUtils.ONE_DAY;
						DoseUtils
								.AddDose(
										treatmentID,
										Enums.DoseType.SelfAdministered
												.toString(),
										doseDate,
										regimen.ID,
										System.currentTimeMillis(),
										((eComplianceApp) this
												.getApplicationContext()).LastLoginId,
										gps.getLatitude(), gps.getLongitude(),
										this);
					}
				}

				for (int j = 0; j < regimen.numSelfAdmin; j++) {
					doseDate += (regimen.selfAdminFreq * 86400000);

					if (!regimen.selfAdminSunday) {
						if (GenUtils.dateToDay(doseDate) <= 2) {
							doseDate += 86400000;
						}
					}

					DoseUtils
							.AddDose(
									treatmentID,
									Enums.DoseType.SelfAdministered.toString(),
									doseDate,
									regimen.ID,
									System.currentTimeMillis(),
									((eComplianceApp) this
											.getApplicationContext()).LastLoginId,
									gps.getLatitude(), gps.getLongitude(), this);

				}

			} else {
				DoseUtils
						.AddDose(
								treatmentID,
								Enums.DoseType.Unsupervised.toString(),
								doseDate,
								regimen.ID,
								System.currentTimeMillis(),
								((eComplianceApp) this.getApplicationContext()).LastLoginId,
								gps.getLatitude(), gps.getLongitude(), this);
				if (regimen.daysFrequency == 1) {
					if (GenUtils.dateToDay(doseDate) == 7) {
						doseDate += GenUtils.ONE_DAY;
						DoseUtils
								.AddDose(
										treatmentID,
										Enums.DoseType.SelfAdministered
												.toString(),
										doseDate,
										regimen.ID,
										System.currentTimeMillis(),
										((eComplianceApp) this
												.getApplicationContext()).LastLoginId,
										gps.getLatitude(), gps.getLongitude(),
										this);
					}
				}
			}
			doseDate += (regimen.unsupervisedFreq * GenUtils.ONE_DAY);

		}
		Reason CurrentItem = (Reason) unsupervisedDoseReasonSpinner
				.getSelectedItem();
		UnSupervisedDoseReasonOperations.addUnsupervisedDose(treatmentID,
				startDate, doseDate
						- (regimen.selfAdminFreq * GenUtils.ONE_DAY),
				CurrentItem.Id, System.currentTimeMillis(),
				((eComplianceApp) this.getApplicationContext()).LastLoginId,
				this);
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, value + " "
				+ getResources().getString(R.string.unsupervisedDosesave));
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onCancelClick(View v) {
		toHome();
	}

	private void toHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, "");
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public void onBackPressed() {
		toHome();
	}
}
