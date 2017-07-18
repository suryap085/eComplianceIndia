/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorLoginOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DefaultMark;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Dashboard extends Activity {

	private TextView txtTitle, cat_I, cat1_ip, cat1_Exip, cat1_cp, cat_II,
			cat2_ip, cat2_Exip, cat2_cp, txtdefault, txttentative, newPatient,
			activepatient, lastadminlogin, lastsynctime, mdr, mdr_ip, mdr_Exip,
			mdr_cp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dashboard);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		cat_I = (TextView) findViewById(R.id.cat_I);
		cat1_ip = (TextView) findViewById(R.id.cat1_ip);
		cat1_Exip = (TextView) findViewById(R.id.cat1_Exip);
		cat1_cp = (TextView) findViewById(R.id.cat1_cp);

		cat_II = (TextView) findViewById(R.id.cat_II);
		cat2_ip = (TextView) findViewById(R.id.cat2_ip);
		cat2_Exip = (TextView) findViewById(R.id.cat2_Exip);
		cat2_cp = (TextView) findViewById(R.id.cat2_cp);

		txtdefault = (TextView) findViewById(R.id.txtdefault);
		txttentative = (TextView) findViewById(R.id.txttentative);

		newPatient = (TextView) findViewById(R.id.newPatient);
		activepatient = (TextView) findViewById(R.id.activepatient);

		lastadminlogin = (TextView) findViewById(R.id.lastadminlogin);
		lastsynctime = (TextView) findViewById(R.id.lastsynctime);

		mdr = (TextView) findViewById(R.id.mdr);
		mdr_ip = (TextView) findViewById(R.id.mdr_ip);
		mdr_Exip = (TextView) findViewById(R.id.mdr_Exip);
		mdr_cp = (TextView) findViewById(R.id.mdr_cp);

		Typeface faceTitle = Typeface.createFromAsset(getAssets(),
				"fonts/FFF_Tusj.ttf");
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/CaviarDreams.ttf");
		Typeface face1 = Typeface.createFromAsset(getAssets(),
				"fonts/Capture_it.ttf");
		Typeface faceItalic = Typeface.createFromAsset(getAssets(),
				"fonts/Caviar_Dreams_Bold.ttf");

		txtTitle.setTypeface(faceTitle);
		txtTitle.setTextColor(Color.DKGRAY);
		txtTitle.setText("DASHBOARD");

		cat_I.setTypeface(face1);

		cat1_ip.setTypeface(face);
		cat1_ip.setText("IP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT1)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ "='"
												+ Enums.StageType
														.getStageType(StageType.IP)
												+ "'", this), this), this)));

		cat1_Exip.setTypeface(face);
		cat1_Exip
				.setText("Ext_IP- "
						+ String.valueOf(PatientsOperations.getPatientCount(
								TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
										RegimenMasterOperations.getRegimenIds(
												Schema.REGIMEN_MASTER_CATEGORY
														+ " = '"
														+ Enums.CategoryType
																.getCategoryType(CategoryType.CAT1)
														+ "' and "
														+ Schema.REGIMEN_MASTER_STAGE
														+ "='"
														+ Enums.StageType
																.getStageType(StageType.ExtIP)
														+ "'", this), this),
								this)));

		cat1_cp.setTypeface(face);
		cat1_cp.setText("CP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT1)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ " ='"
												+ Enums.StageType
														.getStageType(StageType.CP)
												+ "'", this), this), this)));

		cat_II.setTypeface(face1);

		cat2_ip.setTypeface(face);

		cat2_ip.setText("IP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT2)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ "='"
												+ Enums.StageType
														.getStageType(StageType.IP)
												+ "'", this), this), this)));

		cat2_Exip.setTypeface(face);
		cat2_Exip
				.setText("Ext_IP- "
						+ String.valueOf(PatientsOperations.getPatientCount(
								TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
										RegimenMasterOperations.getRegimenIds(
												Schema.REGIMEN_MASTER_CATEGORY
														+ " = '"
														+ Enums.CategoryType
																.getCategoryType(CategoryType.CAT2)
														+ "' and "
														+ Schema.REGIMEN_MASTER_STAGE
														+ "='"
														+ Enums.StageType
																.getStageType(StageType.ExtIP)
														+ "'", this), this),
								this)));

		cat2_cp.setTypeface(face);
		cat2_cp.setText("CP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT2)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ " = '"
												+ Enums.StageType
														.getStageType(StageType.CP)
												+ "'", this), this), this)));

		mdr.setTypeface(face1);

		mdr_ip.setTypeface(face);
		mdr_ip.setText("IP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT6)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ "='"
												+ Enums.StageType
														.getStageType(StageType.IP)
												+ "'", this), this), this)));

		mdr_Exip.setTypeface(face);
		mdr_Exip.setText("Ext_IP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT6)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ "='"
												+ Enums.StageType
														.getStageType(StageType.ExtIP)
												+ "'", this), this), this)));

		mdr_cp.setTypeface(face);
		mdr_cp.setText("CP- "
				+ String.valueOf(PatientsOperations.getPatientCount(
						TreatmentInStagesOperations.getTreatmentIdByRegimenIds(
								RegimenMasterOperations.getRegimenIds(
										Schema.REGIMEN_MASTER_CATEGORY
												+ " = '"
												+ Enums.CategoryType
														.getCategoryType(CategoryType.CAT6)
												+ "' and "
												+ Schema.REGIMEN_MASTER_STAGE
												+ " = '"
												+ Enums.StageType
														.getStageType(StageType.CP)
												+ "'", this), this), this)));

		txtdefault.setTypeface(faceItalic);
		txtdefault.setText("Total Default Patient\n"
				+ String.valueOf(PatientsOperations.CountDefaults(this)));

		txttentative.setTypeface(faceItalic);
		txttentative.setText("Tentative Default\n"
				+ String.valueOf(DefaultMark.tatnativeCount(this)));

		newPatient.setTypeface(faceItalic);
		newPatient.setText("Current Month New Patient\n"
				+ String.valueOf(PatientsOperations.getNewPatientCount(
						Schema.PATIENTS_REG_DATE + " >= "
								+ GenUtils.currentMonthFirstDate() + " and "
								+ Schema.PATIENTS_REG_DATE + " <= "
								+ GenUtils.currentMonthCurrentDate() + " and "
								+ Schema.PATIENTS_IS_DELETED + "=0", this)));

		activepatient.setTypeface(faceItalic);
		activepatient
				.setText("Total Active Patient\n"
						+ String.valueOf(PatientsOperations
								.getActivePatientCount(this)));

		if (VisitorLoginOperations.getLastLoginVisitor(this) == 0) {
			lastadminlogin.setText(getResources().getString(R.string.na));
		} else {
			lastadminlogin.setTypeface(face);
			lastadminlogin.setText("Last Admin Login Time\n"
					+ GenUtils.longToDateTimeString(Long
							.valueOf(VisitorLoginOperations
									.getLastLoginVisitor(this))));
		}

		lastsynctime.setTypeface(face);
		lastsynctime
				.setText("Last Sync Time\n"
						+ GenUtils.longToDateTimeString((Long.valueOf(AppStateConfigurationOperations
								.getKeyValue(
										AppStateConfigurationKeys.Key_Last_Sync,
										this)))));

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		startActivity(new Intent(this, HomeActivity.class));
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}
