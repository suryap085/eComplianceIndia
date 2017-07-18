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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opasha.eCompliance.ecompliance.DbOperations.MasterDiabetesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDiabetesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.DiabetesModal;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.TestIds;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DiabetesActivity extends Activity {
	EditText hbA1c;
	String treatmentId;
	TextView txtViewfbs, error, txtViewpercentage, diabetesLabel;
	int testId;
	ArrayList<DiabetesModal> model;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diabetes);
		hbA1c = (EditText) findViewById(R.id.edtTxthbc1aresult);
		txtViewfbs = (TextView) findViewById(R.id.txtViewfbs);
		error = (TextView) findViewById(R.id.error);
		diabetesLabel = (TextView) findViewById(R.id.diabetesLabel);
		txtViewpercentage = (TextView) findViewById(R.id.txtViewpercentage);
		Bundle extra = getIntent().getExtras();
		if (extra == null) {
			return;
		}
		treatmentId = extra.getString(IntentKeys.key_treatment_id);
		testId = extra.getInt(IntentKeys.key_intent_from);
		if (testId == Enums.TestIds.getId(TestIds.glucometerTest)) {
			txtViewpercentage.setVisibility(View.GONE);
			diabetesLabel.setText(getResources().getString(R.string.FBSResult));
			diabetesLabel.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
		}
		if (testId == Enums.TestIds.getId(TestIds.glucometerTest)) {
			model = MasterDiabetesOperations.getMinMax(
					Schema.MASTER_DIABETES_TEST_ID + "=" + testId, this);
			hbA1c.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

					setResult();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		} else {
			txtViewfbs.setVisibility(View.GONE);
		}
	}

	public void onSaveClick(View v) {
		double val = 0;
		try {
			val = Double.parseDouble(hbA1c.getText().toString());
		} catch (Exception e) {
		}
		if (val == 0) {
			error.setText(getResources().getString(R.string.enterHBaicResult));
			return;
		}
		Pattern ps1 = Pattern.compile("^[0-9.]*$");
		if (testId == Enums.TestIds.getId(TestIds.glucometerTest)) {
			ps1 = Pattern.compile("^[0-9]*$");
		}
		Matcher ms1 = ps1.matcher(hbA1c.getText().toString().trim());
		boolean bs1 = ms1.matches();

		if (!bs1) {
			if (testId == Enums.TestIds.getId(TestIds.glucometerTest)) {
				error.setText(getResources().getString(
						R.string.enterCorrectFBSResult));
				return;
			}
			error.setText(getResources().getString(
					R.string.enterCorrectHBaicResult));
			return;
		}
		PatientDiabetesOperations.add(treatmentId, testId, hbA1c.getText()
				.toString(),
				((eComplianceApp) this.getApplicationContext()).LastLoginId,
				System.currentTimeMillis(), false, this);
		if (testId == Enums.TestIds.getId(TestIds.glucometerTest)) {
			toHome(getResources().getString(R.string.fbsResultComplete),
					Signal.Good);
			return;
		}
		toHome(getResources().getString(R.string.HBA1CResultComplete),
				Signal.Good);

	}

	private void setResult() {

		double value = 0;
		try {
			value = Double.parseDouble(hbA1c.getText().toString());
		} catch (Exception e) {
		}
		for (DiabetesModal m : model) {
			if (value >= m.minValue && value <= m.maxValue) {
				txtViewfbs.setText(m.result);
				return;
			} else {
				txtViewfbs.setText("");
			}
		}
	}

	public void onCancelClick(View v) {
		if ((testId == Enums.TestIds.getId(TestIds.glucometerTest))) {
			toHome(getResources().getString(R.string.FBSResultCancelled),
					Signal.Bad);
			return;
		}
		toHome(getResources().getString(R.string.HBA1CResultCancelled),
				Signal.Bad);
	}

	private void toHome(String str, Signal type) {
		Intent intent = new Intent();
		intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, str);
		intent.putExtra(IntentKeys.key_signal_type, type);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public void onBackPressed() {
		if ((testId == Enums.TestIds.getId(TestIds.glucometerTest))) {
			toHome(getResources().getString(R.string.FBSResultCancelled),
					Signal.Bad);
			return;
		}
		toHome(getResources().getString(R.string.HBA1CResultCancelled),
				Signal.Bad);
	}
}
