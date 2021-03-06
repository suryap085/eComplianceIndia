/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionTwoActivity extends Activity {

	TextView questionaire;

	ImageView treatmentCard;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaire);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		questionaire = (TextView) findViewById(R.id.lblPreRegistrationQuestion1);
		treatmentCard = (ImageView) findViewById(R.id.imageTreatmentID);
		treatmentCard.setImageResource(R.drawable.treatment_card);
		questionaire.setText(getResources().getString(
				R.string.DoesPatientHaveID));
	}

	public void onYesClick(View v) {

		Intent intent = new Intent(this, QuestionnaireThreeActivity.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onNoClick(View v) {

		Intent intent = new Intent(this, NewPatientActivity.class);
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

}
