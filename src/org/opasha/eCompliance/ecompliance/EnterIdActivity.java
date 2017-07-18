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
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EnterIdActivity extends Activity {
	EditText txtId;
	Button btnSubmit;
	TextView lblStatus;
	LinearLayout layout;
	VisitorType visitorType;
	String verificationReason;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_id);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		txtId = (EditText) findViewById(R.id.txtAshaId);
		btnSubmit = (Button) findViewById(R.id.btnMarkVisit);
		lblStatus = (TextView) findViewById(R.id.lblStatus);
		layout = (LinearLayout) findViewById(R.id.enterIdlayout);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			this.finish();
		} else {
			verificationReason = extras
					.getString(IntentKeys.key_verification_reason);
		}

		if (verificationReason.equals(getString(R.string.MarkVisit))) {
			btnSubmit.setText(getString(R.string.MarkVisit));
		} else {
			btnSubmit.setText(getString(R.string.Edit));
		}
		
	}

	public void btnOnClick(View v) {
		if (txtId.getText().toString().isEmpty()) {
			ShowError(getResources().getString(R.string.emptyID));
			getRedAnimation();
		} else if (!PatientsOperations.patientExists(txtId.getText().toString()
				.trim().toUpperCase(), this)) {
			if (!VisitorsOperations.visitorExists(txtId.getText().toString()
					.trim().toUpperCase(), this)) {
				ShowError(getResources().getString(R.string.enterCorrectId));
				getRedAnimation();
			} else {
				if (verificationReason.equals(getString(R.string.MarkVisit))) {
					goToVerifyActivity();
				} else {
					Intent editVisitorIntent = new Intent(this,
							EditVisitorActivity.class);
					editVisitorIntent.putExtra(IntentKeys.key_treatment_id,
							txtId.getText().toString().trim().toUpperCase());
					editVisitorIntent.putExtra(IntentKeys.key_intent_from,
							Enums.IntentFrom.Home);
					this.finish();
					startActivity(editVisitorIntent);
					overridePendingTransition(R.anim.right_side_in,
							R.anim.right_side_out);
				}

			}
		} else if (PatientsOperations.getPatientDetails(txtId.getText()
				.toString(), this).Status.equals(Enums.StatusType
				.getStatusType(StatusType.Default).toString())) {
			ShowError(getResources().getString(R.string.defaultCanNotEdit));
			getRedAnimation();
		} else {
			if (verificationReason.equals(getString(R.string.MarkVisit))) {
				goToVerifyActivity();
			} else {
				Intent editPatientIntent = new Intent(this,
						EditPatientActivity.class);
				editPatientIntent.putExtra(IntentKeys.key_treatment_id, txtId
						.getText().toString().trim().toUpperCase());
				editPatientIntent.putExtra(IntentKeys.key_new_patient, false);
				editPatientIntent.putExtra(IntentKeys.key_intent_from,
						Enums.IntentFrom.Home);
				this.finish();
				startActivity(editPatientIntent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			}
		}
	}

	private void goToVerifyActivity() {
		Intent intent = new Intent(this, VerifyActivity.class);
		intent.putExtra(IntentKeys.key_treatment_id, txtId.getText().toString());
		intent.putExtra(IntentKeys.key_verification_reason, verificationReason);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void ShowError(String message) {
		lblStatus.setText(message);
		txtId.setText("");
	}

	public void btnCancelClick(View v) {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, "");
		intent.putExtra(IntentKeys.key_signal_type, Signal.Bad.toString());
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void EnableCntrols(boolean enabled) {
		txtId.setEnabled(enabled);
		btnSubmit.setEnabled(enabled);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, "");
		intent.putExtra(IntentKeys.key_signal_type, Signal.Bad.toString());
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void getRedAnimation() {
		layout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) layout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}
}
