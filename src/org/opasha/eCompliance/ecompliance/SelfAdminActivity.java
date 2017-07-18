/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SelfAdminActivity extends Activity {
	Spinner selfAdmin;
	EditText noOfDoses;
	TextView errorACA;
	LinearLayout noOfSelfAdminDoseLay;
	int MaxSelfAdmin;
	String treatmentId;
	long lastSupervisedDoseDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_admin);
		selfAdmin = (Spinner) findViewById(R.id.selfAdminSpn);
		noOfDoses = (EditText) findViewById(R.id.noOfDoses);
		noOfSelfAdminDoseLay = (LinearLayout) findViewById(R.id.noofSelfAdminDoseLay);
		errorACA = (TextView) findViewById(R.id.errorACA);
		Bundle extra = getIntent().getExtras();
		if (extra == null) {
			cancel();
		}
		treatmentId = extra.getString(IntentKeys.key_treatment_id);
		lastSupervisedDoseDate = extra
				.getLong(IntentKeys.key_last_supervised_dose);
		Master regimen = RegimenMasterOperations.getRegimen(
				DoseAdminstrationOperations.getPatientRegimenidByDate(
						Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
								+ treatmentId + "' and "
								+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + "="
								+ lastSupervisedDoseDate, this), this);
		MaxSelfAdmin = 0;
		try {
			MaxSelfAdmin = regimen.numSelfAdmin;
		} catch (Exception e) {
		}
		selfAdmin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (selfAdmin.getSelectedItemId() == 1)
					noOfSelfAdminDoseLay.setVisibility(View.VISIBLE);
				else
					noOfSelfAdminDoseLay.setVisibility(View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void onDoneClick(View v) {

		int selfAdminTaken = 1;
		int noSelfAdmin = 0;
		if (selfAdmin.getSelectedItemPosition() == 1) {
			if (noOfDoses.getText().toString().trim().isEmpty()) {
				errorACA.setText(getResources().getString(
						R.string.enterCorrectSelfAdmin));
				return;
			}
			selfAdminTaken = 0;
			Pattern ps1 = Pattern.compile("^[0-9]*$");
			Matcher ms1 = ps1.matcher(noOfDoses.getText().toString().trim());
			boolean bs1 = ms1.matches();
			if (bs1 == false) {
				errorACA.setText(getResources().getString(
						R.string.enterCorrectSelfAdmin));
				return;
			}
			try {
				if ((Integer.parseInt(noOfDoses.getText().toString().trim())) > MaxSelfAdmin) {
					errorACA.setText(getResources().getString(
							R.string.enterCorrectSelfAdmin));
					return;
				}
				noSelfAdmin = Integer.parseInt(noOfDoses.getText().toString()
						.trim());
			} catch (Exception e) {
			}
		}
		Intent intent = this.getIntent();
		intent.putExtra(IntentKeys.key_is_all_self_admin_taken, selfAdminTaken);
		intent.putExtra(IntentKeys.key_no_of_self_admin_taken, noSelfAdmin);
		intent.putExtra(IntentKeys.key_treatment_id, treatmentId);
		intent.putExtra(IntentKeys.key_last_supervised_dose,
				lastSupervisedDoseDate);
		this.setResult(RESULT_OK, intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onCancelClick(View v) {
		cancel();
	}

	private void cancel() {
		Intent intent = this.getIntent();
		this.setResult(RESULT_CANCELED, intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}