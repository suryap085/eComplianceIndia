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

import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.IntentFrom;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class EditVisitorActivity extends Activity {
	private ScrollView mainLayout;
	private TextView txtViewVisitorType;
	private TextView txtViewVisitorId;
	private EditText editTxtName;
	private EditText editTxtPhone;
	private TextView txtViewError;
	private String visitorType;
	private StringBuilder verifyTextBox = new StringBuilder();
	private StringBuilder validateTextBox = new StringBuilder();
	private String TreatmentId;
	private Enums.ReportType reporttype;
	private ArrayList<Visitor> visitorList;
	Bundle extras;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_visitor);
		extras = getIntent().getExtras();
		if (extras == null) {
			toHome();
		} else {
			TreatmentId = extras.getString(IntentKeys.key_treatment_id);
			reporttype = (ReportType) extras.get(IntentKeys.key_report_type);
			visitorList = VisitorsOperations.getVisitor(Schema.VISITORS_ID
					+ "= '" + TreatmentId + "' and "
					+ Schema.VISITORS_IS_DELETED + "= 0", this);
			visitorType = visitorList.get(0).visitorType;
			if (!visitorList.isEmpty()) {
				mainLayout = (ScrollView) findViewById(R.id.newVisitorLayout);
				txtViewVisitorType = (TextView) findViewById(R.id.TxtViewVisitorTypeAdd);
				txtViewVisitorId = (TextView) findViewById(R.id.TxtViewVisitorIdAdd);
				editTxtName = (EditText) findViewById(R.id.editTxtVisitorNameAdd);
				editTxtPhone = (EditText) findViewById(R.id.editTxtvisitorPhoneAdd);
				txtViewError = (TextView) findViewById(R.id.txtviewErrorAdd);
				mainLayout
						.setBackgroundResource(R.drawable.grey_to_red_transition);

				txtViewVisitorType.setText(visitorList.get(0).visitorType);
				txtViewVisitorId.setText(TreatmentId);
				editTxtName.setText(visitorList.get(0).name);
				editTxtPhone.setText(visitorList.get(0).phone);
			}
		}
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
	}

	public void onSaveClick(View v) {

		if (VerifyBlankField() == false) {
			txtViewError.setText(verifyTextBox.toString().trim());
			getRedAnimation();
			verifyTextBox.delete(0, verifyTextBox.length());
			return;
		}

		if (validateTextField() == false) {
			txtViewError.setText(validateTextBox.toString().trim());
			getRedAnimation();
			validateTextBox.delete(0, validateTextBox.length());
			return;
		}

		VisitorsOperations.addVisitor(TreatmentId, editTxtName.getText()
				.toString().trim(), visitorList.get(0).visitorType,
				visitorList.get(0).registrationDate, "Active", editTxtPhone
						.getText().toString().trim(), visitorList.get(0).isAuthenticated,
				System.currentTimeMillis(),
				((eComplianceApp) this.getApplicationContext()).LastLoginId,
				false, visitorList.get(0).tabId, this);

		toHome(visitorList.get(0).visitorType + " "
				+ editTxtName.getText().toString() + "(" + TreatmentId + ") "
				+ getResources().getString(R.string.updated) + "!");
	}

	private boolean validateTextField() {
		Pattern ps = Pattern.compile("[a-zA-Z .]+");
		Matcher ms = ps.matcher(editTxtName.getText().toString().trim());
		boolean bs = ms.matches();

		Pattern ps1 = Pattern.compile("^[0-9]*$");
		Matcher ms1 = ps1.matcher(editTxtPhone.getText().toString().trim());
		boolean bs1 = ms1.matches();
		if (bs == false) {
			validateTextBox.append("\n"
					+ getResources().getString(R.string.enterCorrectName));
		}
		if ((editTxtPhone.getText().toString().trim().length() > 0 && editTxtPhone
				.getText().toString().trim().length() < 10)
				|| bs1 == false) {
			validateTextBox.append("\n"
					+ getResources().getString(R.string.enterCorrectPhone));
		}

		if (validateTextBox.toString().trim().length() != 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean VerifyBlankField() {
		if (editTxtName.getText().toString().trim().length() == 0) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterName));
		}
		if (editTxtPhone.getText().toString().trim().length() == 0
				&& (visitorType.equals(Enums.VisitorType
						.GetViewString(VisitorType.PM)) || visitorType
						.equals(Enums.VisitorType
								.GetViewString(VisitorType.Counselor)))) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterPhoneNo));
		}
		if (verifyTextBox.toString().trim().length() != 0) {
			return false;

		} else {
			return true;
		}
	}

	private void getRedAnimation() {
		mainLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) mainLayout
				.getBackground();
		txtViewError.setBackgroundResource(R.drawable.black_to_red_transition);
		TransitionDrawable transition2 = (TransitionDrawable) txtViewError
				.getBackground();
		transition1.startTransition(0);
		transition2.startTransition(0);
		transition1.reverseTransition(6000);
		transition2.reverseTransition(6000);
	}

	public void onCancelClick(View v) {
		toHome();
	}

	private void toHome() {
		Intent intent = new Intent();

		try {
			Enums.IntentFrom from = (IntentFrom) extras
					.get(IntentKeys.key_intent_from);
			switch (from) {
			case Reports:

				intent = new Intent(this, VisitorReportActivity.class);
				intent.putExtra(IntentKeys.key_report_type, reporttype);
				intent.putExtra(IntentKeys.key_intent_from, from);
				break;

			case Home:
			default:
				intent = new Intent(this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, "");
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Good.toString());
				break;
			}

		} catch (Exception e) {
			intent = new Intent(this, HomeActivity.class);
			intent.putExtra(IntentKeys.key_message_home, "");
			intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		}
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void toHome(String str) {
		Intent intent = new Intent();

		try {
			Enums.IntentFrom from = (IntentFrom) extras
					.get(IntentKeys.key_intent_from);
			switch (from) {
			case Reports:

				intent = new Intent(this, VisitorReportActivity.class);
				intent.putExtra(IntentKeys.key_report_type, reporttype);
				intent.putExtra(IntentKeys.key_intent_from, from);
				break;
			case Home:
			default:
				intent = new Intent(this, HomeActivity.class);
				intent.putExtra(IntentKeys.key_message_home, str);
				intent.putExtra(IntentKeys.key_signal_type,
						Signal.Good.toString());
				break;
			}

		} catch (Exception e) {
			intent = new Intent(this, HomeActivity.class);
			intent.putExtra(IntentKeys.key_message_home, str);
			intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		}
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public void onBackPressed() {
		toHome();
	}
}
