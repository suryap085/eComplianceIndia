/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EnterOldIdActivity extends Activity {
	private TextView txtViewError;
	private EditText editTxtAshaId;
	LinearLayout mainLayout;
	String ashaID = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_old_id);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		txtViewError = (TextView) findViewById(R.id.txtViewErrorOldID);
		editTxtAshaId = (EditText) findViewById(R.id.txtOldAshaId);
		mainLayout = (LinearLayout) findViewById(R.id.layoutEnterOldID);
	}

	public void onSaveClick(View v) {
		ashaID = editTxtAshaId.getText().toString().trim();
		if (!ashaID.equals("")) {
			if (PatientsOperations.patientExists(ashaID, this) == false) {
				Intent intent = new Intent(this, NewPatientActivity.class);
				intent.putExtra(IntentKeys.key_treatment_id, ashaID);
				this.finish();
				startActivity(intent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			} else if (PatientsOperations.patientExists(ashaID, this) == true) {
				if(ScansOperations.isTreatmentidIdExist(ashaID, this))
				{
					Intent intent = new Intent(this, EditPatientActivity.class);
					intent.putExtra(IntentKeys.key_treatment_id, ashaID);
					intent.putExtra(IntentKeys.key_new_patient, true);
					startActivity(intent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
					this.finish();
				}
				else
				{
					Intent i = new Intent(this, EnrollActivity.class);
					i.putExtra(IntentKeys.key_visitor_type, "Patient");
					i.putExtra(IntentKeys.key_treatment_id, ashaID);
					i.putExtra(IntentKeys.key_intent_from, ReportType.PatientsFromLegacySystem);
					startActivity(i);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
					this.finish();
				}	
			}
		} else {
			getRedAnimation();
			txtViewError.setText(getResources().getString(R.string.enterID)
					.toString());
		}
	}

	public void onCancelClick(View v) {

		Intent intent = new Intent(this, HomeActivity.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	private void getRedAnimation() {
		mainLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) mainLayout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);

	}

}
