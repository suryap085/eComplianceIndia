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

import org.opasha.eCompliance.ecompliance.Adapters.SpinnerCenterAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectCenterActivity extends Activity {
	ArrayList<Center> centerList;
	SpinnerCenterAdapter centerAdapter;
	Spinner spinnerCenter;
	TextView centerError;
	String center;
	String error;
	ScrollView mainLayout;
	Bundle extra;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_center);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		extra = getIntent().getExtras();
		spinnerCenter = (Spinner) findViewById(R.id.spinnerCenterEdit);
		centerError = (TextView) findViewById(R.id.centerError);
		mainLayout = (ScrollView) findViewById(R.id.selectCenterLayout);
		loadCenterSpinner();
	}

	public void onDoneClick(View v) {

		Intent intent = this.getIntent();
		intent.putExtra(IntentKeys.key_current_center,
				((TextView) findViewById(R.id.spinnerCenter)).getText()
						.toString());
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

	private void loadCenterSpinner() {
		centerList = CentersOperations.getCenterForSpinner(false, this);
		if (!centerList.isEmpty()) {
			centerAdapter = new SpinnerCenterAdapter(this,
					CentersOperations.getCenterForSpinner(false, this));
			spinnerCenter.setAdapter(centerAdapter);
			int tempIndex = 0;
			for (Center C : centerList) {

				if (C.centerName.equals(extra.getString(
						IntentKeys.key_current_center).toString())) {
					spinnerCenter.setSelection(tempIndex);
					return;
				}
				tempIndex++;
			}
			return;
		}

	}

	public void onBackPressed() {
		this.finish();

		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}
