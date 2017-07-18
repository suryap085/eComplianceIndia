/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.LocationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ShowFinalRegistration extends Activity {
	TextView id;
	ImageView image;
	PatientDetailsIntent pdi = new PatientDetailsIntent();
	Patient patient;
	int RegimenId;
	String isIconSaved = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_final_reg_text_free);
		id = (TextView) findViewById(R.id.showId);
		image = (ImageView) findViewById(R.id.imgStatic);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			pdi = new Gson().fromJson(
					extras.getString(IntentKeys.key_petient_details),
					PatientDetailsIntent.class);
		}
		pdi.phone = "";
		pdi.name = "";
		switch (pdi.intentFrom) {
		case EditPatient:
			isIconSaved = PatientIconOperation.getId(pdi.iconId, this);
			if (pdi.treatmentId.equals("")) {
				pdi.treatmentId = PatientIconOperation.getId(pdi.iconId, this);
			}
			patient = PatientsOperations.getPatientDetails(pdi.treatmentId,
					this);

			RegimenId = TreatmentInStagesOperations.getPatientRegimenId(
					pdi.treatmentId, this);
			break;
		default:
			break;
		}
		Bitmap bitmapImage = BitmapFactory
				.decodeFile(Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
						+ "//eComplianceClient//"
						+ DbUtils.getTabId(this)
						+ "//resources//" + pdi.iconId);
		Drawable drawableImage = new BitmapDrawable(this.getResources(),
				bitmapImage);
		image.setBackgroundDrawable(drawableImage);
		id.setText(pdi.treatmentId);
	}

	public void onSaveClick(View v) {
		switch (pdi.intentFrom) {
		case Home:
			GpsTracker tracker = new GpsTracker(this);
			if (tracker.canGetLocation()) {
				LocationOperations.addLocation(this, pdi.treatmentId,
						tracker.getLatitude(), tracker.getLongitude());
			}
			AppStateConfigurationOperations.updateMaxId(this);
			PatientIconOperation.addIcon(pdi.treatmentId, pdi.iconId,
					System.currentTimeMillis(), true,
					ShowFinalRegistration.this);
			MasterIconOperation
					.updateIsUsed(
							pdi.iconId,
							true,
							org.opasha.eCompliance.ecompliance.TextFree.ShowFinalRegistration.this);
			PatientsOperations
					.addPatient(
							pdi.treatmentId,
							pdi.name,
							Enums.StatusType.getStatusType(StatusType.Active),
							pdi.phone,
							DbUtils.getTabId(this),
							true,
							System.currentTimeMillis(),
							((eComplianceApp) this.getApplicationContext()).LastLoginId,
							false, GenUtils.getCurrentDateLong(),
							CentersOperations.getCenterIdByName(pdi.center,
									this), pdi.address, pdi.diseaseSite,
							pdi.disease, pdi.patientType, pdi.nikshayId,
							pdi.tbNumber, pdi.smokingHistory, this);
			TreatmentInStagesOperations.addTreatmentStage(pdi.treatmentId,
					RegimenMasterOperations.getRegimenId(
							Schema.REGIMEN_MASTER_CATEGORY + "= '"
									+ pdi.category + "' and "
									+ Schema.REGIMEN_MASTER_STAGE + "='"
									+ pdi.stage + "' and "
									+ Schema.REGIMEN_MASTER_SCHEDULE + "= '"
									+ pdi.schedule + "'", this), GenUtils
							.getCurrentDateLong(), System.currentTimeMillis(),
					((eComplianceApp) this.getApplication()).LastLoginId,
					false, this);

			break;
		case EditPatient:
			if (isIconSaved.equals("")) {
				PatientIconOperation.addIcon(pdi.treatmentId, pdi.iconId,
						System.currentTimeMillis(), true,
						ShowFinalRegistration.this);
				MasterIconOperation
						.updateIsUsed(
								pdi.iconId,
								true,
								org.opasha.eCompliance.ecompliance.TextFree.ShowFinalRegistration.this);
			}

			if (!patient.Status.equals(pdi.status)) {
				PatientsOperations
						.addPatient(
								pdi.treatmentId,
								patient.name,
								pdi.status,
								patient.phoneNumber,
								DbUtils.getTabId(this),
								patient.isCounsellingPending,
								System.currentTimeMillis(),
								((eComplianceApp) this.getApplicationContext()).LastLoginId,
								false, patient.RegDate, patient.centerId,
								patient.address, patient.diseaseSite,
								patient.disease, patient.patientType,
								patient.nikshayId, patient.tbNumber,
								patient.smokingHistory, this);
				if (!pdi.status.equals(Enums.StatusType
						.getStatusType(StatusType.Active))) {
					TreatmentInStagesOperations
							.addTreatmentStage(
									pdi.treatmentId,
									0,
									GenUtils.getCurrentDateLong(),
									System.currentTimeMillis(),
									((eComplianceApp) this.getApplication()).LastLoginId,
									false, this);
					break;
				}
			}
			int newRegimen = RegimenMasterOperations.getRegimenId(
					Schema.REGIMEN_MASTER_CATEGORY + "= '" + pdi.category
							+ "' and " + Schema.REGIMEN_MASTER_STAGE + "='"
							+ pdi.stage + "' and "
							+ Schema.REGIMEN_MASTER_SCHEDULE + "= '"
							+ pdi.schedule + "'", this);
			TreatmentInStagesOperations.addTreatmentStage(pdi.treatmentId,
					newRegimen, GenUtils.getCurrentDateLong(),
					System.currentTimeMillis(),
					((eComplianceApp) this.getApplication()).LastLoginId,
					false, this);
			break;
		default:
			break;
		}
		GenUtils.setAppPendingToday(this);
		goHome();
	}

	private void goHome() {
		Intent intent = new Intent(this, HomeActivityTextFree.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void onCancelClick(View v) {
		if (pdi.intentFrom == Enums.IntentFrom.Home) {
			ScansOperations.deleteScans(this, pdi.treatmentId);
		}
		goHome();
	}

	@Override
	public void onBackPressed() {
		backIntent backIntent = pdi.backIntent.get(pdi.backIntent.size() - 1);
		pdi.backIntent.remove(pdi.backIntent.size() - 1);
		Intent intent = new Intent(this, SelectScheduleActivity.class);
		switch (backIntent) {
		case status:
			intent = new Intent(this, SelectStatusActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent.putExtra(IntentKeys.key_petient_details,
				new Gson().toJson(pdi)));
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}
