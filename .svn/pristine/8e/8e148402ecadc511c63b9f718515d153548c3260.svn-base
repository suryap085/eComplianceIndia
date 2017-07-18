/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reports extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reports_activity_layout);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		/**
		 * Creating all buttons instances
		 * */
		Button btn_all_patients = (Button) findViewById(R.id.btn_all_patients);
		Button btn_inactive_patient = (Button) findViewById(R.id.btn_inactive_patient);
		Button btn_all_visitors = (Button) findViewById(R.id.btn_all_visitors);
		Button btn_pending_today = (Button) findViewById(R.id.btn_pending_today);
		Button btn_patient_visited = (Button) findViewById(R.id.btn_patient_visited_today);
		Button btn_visitor_logged = (Button) findViewById(R.id.btn_visitors_logged_today);
		Button btn_missed_dose = (Button) findViewById(R.id.btn_missed_dose);
		Button btn_hospitalised_patient=(Button)findViewById(R.id.btn_hospitalised_patient);
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_inactive_report_enable, this).equals(
				"1")) {
			btn_inactive_patient.setVisibility(View.VISIBLE);
		}
		/**
		 * Handling all button click events
		 * */

		btn_all_patients.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PatientReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.AllPatients);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});
		
		btn_hospitalised_patient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PatientReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.hospitalisedPatient);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});
		

		btn_all_visitors.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						VisitorReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.AllVisitor);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});

		btn_pending_today.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PatientReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.PendingPatients);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});

		btn_patient_visited.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PatientReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.VisitedPatients);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});

		btn_visitor_logged.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						VisitorReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.VisitedVisitor);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});

		btn_inactive_patient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PatientReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.InactivePatient);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});

		btn_missed_dose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PatientReportActivity.class);
				i.putExtra(IntentKeys.key_report_type,
						Enums.ReportType.MissedPatients);
				i.putExtra(IntentKeys.key_intent_from, Enums.IntentFrom.Reports);
				startActivity(i);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				Reports.this.finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(), HomeActivity.class);

		startActivity(i);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
		Reports.this.finish();
	}
}
