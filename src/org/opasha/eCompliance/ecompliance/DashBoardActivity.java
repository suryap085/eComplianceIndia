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
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorLoginOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DefaultMark;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableRow;
import android.widget.TextView;

public class DashBoardActivity extends Activity {
	TextView ipValue, cpValue, exIpValue, cat2IpValue, cat2CpValue,
			cat2ExIpValue, total, nextdefault, lastdefault, currentRegistered,
			admin, Sync, Sync1, admin1, lastRegistered1, currentRegistered1,
			lastdefault1, nextdefault1, total1, ExIp2, Cp2, Ip2, ip1, cp1,
			exIp1;
	TableRow cat1, cat2, Registration, Default, time;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		long comparetime = 0;
		long lastsynctime = 0;
		Sync1 = (TextView) findViewById(R.id.Sync1);
		Sync1.setText(getResources().getString(R.string.sync1));
		admin1 = (TextView) findViewById(R.id.admin1);
		admin1.setText(getResources().getString(R.string.visitortime));
		currentRegistered1 = (TextView) findViewById(R.id.currentRegistered1);
		currentRegistered1.setText(getResources().getString(
				R.string.currentmonthnewreg));
		lastdefault1 = (TextView) findViewById(R.id.lastdefault1);
		lastdefault1.setText(getResources().getString(R.string.defaults));
		nextdefault1 = (TextView) findViewById(R.id.nextdefault1);
		nextdefault1.setText(getResources().getString(R.string.tentative));
		total1 = (TextView) findViewById(R.id.total1);
		total1.setText(getResources().getString(R.string.active));
		ExIp2 = (TextView) findViewById(R.id.ExIp2);
		ExIp2.setText(getResources().getString(R.string.cat2exip));
		Cp2 = (TextView) findViewById(R.id.Cp2);
		Cp2.setText(getResources().getString(R.string.cat2cp));
		Ip2 = (TextView) findViewById(R.id.Ip2);
		Ip2.setText(getResources().getString(R.string.cat2ip));
		ip1 = (TextView) findViewById(R.id.ip1);
		ip1.setText(getResources().getString(R.string.ip));
		cp1 = (TextView) findViewById(R.id.cp1);
		cp1.setText(getResources().getString(R.string.cp));
		exIp1 = (TextView) findViewById(R.id.exIp1);
		exIp1.setText(getResources().getString(R.string.exip));
		Default = (TableRow) findViewById(R.id.Default);
		cat2 = (TableRow) findViewById(R.id.cat2);
		Registration = (TableRow) findViewById(R.id.Registration);
		cat1 = (TableRow) findViewById(R.id.cat1);
		ipValue = (TextView) findViewById(R.id.ipValue);
		cpValue = (TextView) findViewById(R.id.cpValue);
		exIpValue = (TextView) findViewById(R.id.exIpValue);
		cat2IpValue = (TextView) findViewById(R.id.cat2IpValue);
		cat2CpValue = (TextView) findViewById(R.id.cat2CpValue);
		cat2ExIpValue = (TextView) findViewById(R.id.cat2ExIpValue);
		total = (TextView) findViewById(R.id.total);
		nextdefault = (TextView) findViewById(R.id.nextdefault);
		lastdefault = (TextView) findViewById(R.id.lastdefault);
		currentRegistered = (TextView) findViewById(R.id.currentRegistered);
		Sync = (TextView) findViewById(R.id.Sync);
		admin = (TextView) findViewById(R.id.admin);
		ipValue.setText(getResources().getString(R.string.ip));
		ipValue.setText(String.valueOf(PatientsOperations.getPatientCount(
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

		cpValue.setText(String.valueOf(PatientsOperations.getPatientCount(
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
		exIpValue
				.setText(String.valueOf(PatientsOperations.getPatientCount(
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
												+ "'", this), this), this)));
		cat2IpValue
				.setText(String.valueOf(PatientsOperations.getPatientCount(
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
		cat2ExIpValue
				.setText(String.valueOf(PatientsOperations.getPatientCount(
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
												+ "'", this), this), this)));
		cat2CpValue
				.setText(String.valueOf(PatientsOperations.getPatientCount(
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

		lastsynctime = Long.valueOf(AppStateConfigurationOperations
				.getKeyValue(AppStateConfigurationKeys.Key_Last_Sync, this));

		try {
			comparetime = Long.parseLong((ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_last_sync_compare_value, this)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (System.currentTimeMillis() - lastsynctime > comparetime) {
				Sync.setBackgroundColor(Color.RED);
				Sync1.setBackgroundColor(Color.RED);
			} else {

				Sync.setBackgroundColor(Color.GREEN);
				Sync1.setBackgroundColor(Color.GREEN);
			}

		} catch (NumberFormatException e1) {

			e1.printStackTrace();
		}

		total.setText(String.valueOf(PatientsOperations
				.getActivePatientCount(this)));

		Sync.setText(GenUtils.longToDateTimeString((Long
				.valueOf(AppStateConfigurationOperations.getKeyValue(
						AppStateConfigurationKeys.Key_Last_Sync, this)))));

		admin.setText(GenUtils.longToDateTimeString(Long
				.valueOf(VisitorLoginOperations.getLastLoginVisitor(this))));
		if (VisitorLoginOperations.getLastLoginVisitor(this) == 0) {
			admin.setText(getResources().getString(R.string.na));
		}

		lastdefault.setText(String.valueOf(PatientsOperations
				.CountDefaults(this)));
		int val = DefaultMark.tatnativeCount(this);
		String a = String.valueOf(val);
		nextdefault.setText(a);

		currentRegistered.setText(String.valueOf(PatientsOperations
				.getNewPatientCount(
						Schema.PATIENTS_REG_DATE + " >= "
								+ GenUtils.currentMonthFirstDate() + " and "
								+ Schema.PATIENTS_REG_DATE + " <= "
								+ GenUtils.currentMonthCurrentDate() + " and "
								+ Schema.PATIENTS_IS_DELETED + "=0", this)));

	}
}
