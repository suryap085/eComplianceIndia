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

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecheckIdActivity extends Activity {

	private TextView txtViewStatus;
	private EditText editTxtAshaId;
	LinearLayout mainLayout;
	String ashaID = "";
	VisitorType visitorType;
	String treatmentId;
	ReportType reptype;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recheck_treatmentid);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		txtViewStatus = (TextView) findViewById(R.id.lblStatus);
		editTxtAshaId = (EditText) findViewById(R.id.txtAshaId);
		mainLayout = (LinearLayout) findViewById(R.id.enterIdlayout);

		Bundle extras = getIntent().getExtras();

		// Return to main screen if no extras are sent.
		if (extras == null) {
			this.finish();
			return;
		}

		visitorType = VisitorType.getVisitorType(extras
				.getString(IntentKeys.key_visitor_type));
		treatmentId = extras.getString(IntentKeys.key_treatment_id);
		try {
			reptype = (ReportType) extras.get(IntentKeys.key_intent_from);
		} catch (Exception e) {
		}

		// Back if treatment Id is empty
		if (treatmentId == null || treatmentId.isEmpty()) {
			this.finish();
			return;
		}
	}

	public void onSaveClick(View v) {
		ashaID = editTxtAshaId.getText().toString().trim();
		if (!ashaID.equals("")) {
			if (ashaID.equals(treatmentId)) {
				ArrayList<Visitor> visitor = VisitorsOperations.getVisitor(
						Schema.VISITORS_ID + "= '" + treatmentId + "' and "
								+ Schema.VISITORS_IS_DELETED + "= 0", this);
				Intent i;
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_used_device, this).equals("iris")) {
					i = new Intent(this, EnrollIrisActivity.class);
				} else {
					i = new Intent(this, EnrollActivity.class);
				}
				if (!visitorType.toString().equals(
						Enums.VisitorType.Patient.toString())) {
					i.putExtra(IntentKeys.key_visitor_name, visitor.get(0).name);
					i.putExtra(IntentKeys.key_phone_no, visitor.get(0).phone);
					i.putExtra(IntentKeys.key_is_Authenticate,
							visitor.get(0).isAuthenticated);
					i.putExtra(IntentKeys.key_machine_id, visitor.get(0).tabId);
				}

				i.putExtra(IntentKeys.key_visitor_type, visitorType.toString());
				i.putExtra(IntentKeys.key_treatment_id, treatmentId);
				i.putExtra(IntentKeys.key_intent_from, reptype);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				this.finish();
				return;
			} else {
				new AlertDialog.Builder(this)
						.setTitle(getResources().getString(R.string.error))
						.setMessage(
								getResources().getString(
										R.string.incorrectTreatmentId))
						.setNeutralButton(
								getResources().getString(R.string.ok),
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										toLegacyPatientReport(RecheckIdActivity.this);
									}
								}).show();

			}

		} else {
			getRedAnimation();
			txtViewStatus.setText(getResources().getString(R.string.enterID)
					.toString());
		}
	}

	public void btnCancelClick(View v) {
		toLegacyPatientReport(this);
	}

	@Override
	public void onBackPressed() {
		if(reptype == ReportType.PatientsFromLegacySystem)
		{		
			toLegacyPatientReport(this);
		}
		else
		{
			toVisitorReregReport(this);
		}
	}

	private void getRedAnimation() {
		mainLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) mainLayout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	private void toLegacyPatientReport(Context context) {
		Intent i;
		if (!visitorType.toString()
				.equals(Enums.VisitorType.Patient.toString())) {
			i = new Intent(context, VisitorReportActivity.class);

		} else {
			i = new Intent(context, PatientReportActivity.class);

		}
		i.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.PatientsFromLegacySystem);
		i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(i);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
		this.finish();
	}
	
	private void toVisitorReregReport(Context context) {
		Intent i;
		i = new Intent(context, VisitorReportActivity.class);
		i.putExtra(IntentKeys.key_report_type,
				Enums.ReportType.VisitorReregistration);
		i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Home);
		startActivity(i);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
		this.finish();
	}

}
