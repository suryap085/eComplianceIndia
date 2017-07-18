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

import org.opasha.eCompliance.ecompliance.Adapters.SpinnerStatusAdapter;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
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

public class SelectHivResultActivity extends Activity {
	ArrayList<Master> statusList;
	SpinnerStatusAdapter StatusAdapter;
	Spinner statusSpinner, centerSpinner, typeSpinner;
	LinearLayout newCenter;
	ScrollView selectStatusLayout;
	String result, screeningResult;
	Bundle extra;
	TextView error;
	HashMap<String, String> centerIds;
	ArrayAdapter<String> labAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extra = getIntent().getExtras();
		setContentView(R.layout.activity_select_hiv_result);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		statusSpinner = (Spinner) findViewById(R.id.spinnerStatusEdit);
		typeSpinner = (Spinner) findViewById(R.id.spinnerHIVType);
		Bundle extra = getIntent().getExtras();
		String[] result = extra.getString(IntentKeys.key_hiv_screening_result)
				.split(",");
		String screening = result[0];
		String finalResult = result[1];
		if (screening.equals("+ve")) {
			typeSpinner.setSelection(1);
		} else if (screening.equals("-ve")) {
			typeSpinner.setSelection(2);
		} else if (screening.equals("NA")) {
			typeSpinner.setSelection(3);
		}
		if (finalResult.equals("+ve")) {
			statusSpinner.setSelection(1);
		} else if (finalResult.equals("-ve")) {
			statusSpinner.setSelection(2);
		} else if (finalResult.equals("NA")) {
			statusSpinner.setSelection(3);
		}
		selectStatusLayout = (ScrollView) findViewById(R.id.selectStatusLayout);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				if (typeSpinner.getSelectedItemPosition() == 2
						|| typeSpinner.getSelectedItemPosition() == 3) {
					statusSpinner.setClickable(false);

				} else {
					statusSpinner.setClickable(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void onDoneClick(View v) {
		result = statusSpinner.getSelectedItem().toString();
		screeningResult = typeSpinner.getSelectedItem().toString();
		if (screeningResult.equals("Select Result")) {
			((TextView) findViewById(R.id.errorMassage)).setText(getResources()
					.getString(R.string.selectScreenResult));
			getRedAnimation();
			return;
		}
		if (result.equals("Select Result")) {
			((TextView) findViewById(R.id.errorMassage)).setText(getResources()
					.getString(R.string.selectFinal));
			getRedAnimation();
			return;
		}
		Intent intent = this.getIntent();
		intent.putExtra(IntentKeys.key_current_status, result);
		intent.putExtra(IntentKeys.key_hiv_screening_result, screeningResult);
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

	private void getRedAnimation() {
		selectStatusLayout
				.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) selectStatusLayout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}
