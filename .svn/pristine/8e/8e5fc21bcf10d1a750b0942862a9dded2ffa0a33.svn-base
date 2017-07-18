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

import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class NewVisitorActivity extends Activity {
	private ScrollView mainLayout;
	private TextView txtViewVisitorType;
	private TextView txtViewVisitorId;
	private EditText editTxtName;
	private EditText editTxtPhone;
	private TextView txtViewError;
	private VisitorType visitorType;
	private StringBuilder verifyTextBox = new StringBuilder();
	private StringBuilder validateTextBox = new StringBuilder();
	private String newTreatmentId;
	private String machineId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_visitor);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			toHome();
		} else {
			visitorType = (VisitorType) extras.get(IntentKeys.key_visitor_type);
			mainLayout = (ScrollView) findViewById(R.id.newVisitorLayout);
			txtViewVisitorType = (TextView) findViewById(R.id.TxtViewVisitorTypeAdd);
			txtViewVisitorId = (TextView) findViewById(R.id.TxtViewVisitorIdAdd);
			editTxtName = (EditText) findViewById(R.id.editTxtVisitorNameAdd);
			editTxtPhone = (EditText) findViewById(R.id.editTxtvisitorPhoneAdd);
			txtViewError = (TextView) findViewById(R.id.txtviewErrorAdd);
			newTreatmentId = getNewTreatmentId();
			mainLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
			txtViewVisitorType.setText(visitorType.toString());
			txtViewVisitorId.setText(newTreatmentId);
		}
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
		if (AppStateConfigurationOperations.updateMaxId(this) != -1) {
			boolean isAuth = false;
			String name = editTxtName.getText().toString();
			String phone = editTxtPhone.getText().toString();

			Intent intent = new Intent(this, EnrollActivity.class);
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_used_device, this).equals("iris")) {
				intent = new Intent(this, EnrollIrisActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			}
			intent.putExtra(IntentKeys.key_visitor_name, name);
			intent.putExtra(IntentKeys.key_visitor_type, Enums.StatusType
					.getStatusType(StatusType.Active).toString());
			intent.putExtra(IntentKeys.key_phone_no, phone);
			intent.putExtra(IntentKeys.key_is_Authenticate, isAuth);
			intent.putExtra(IntentKeys.key_machine_id, machineId);
			intent.putExtra(IntentKeys.key_treatment_id, newTreatmentId);
			intent.putExtra(IntentKeys.key_visitor_type, visitorType.toString());
			this.finish();
			startActivity(intent);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		} else {
			toHome();
		}
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
				&& (visitorType.equals(Enums.VisitorType.PM) || visitorType
						.equals(Enums.VisitorType.Counselor))) {
			verifyTextBox.append("\n"
					+ getResources().getString(R.string.enterPhoneNo));
		}
		if (verifyTextBox.toString().trim().length() != 0) {
			return false;

		} else {
			return true;
		}
	}

	private String getNewTreatmentId() {
		ArrayList<Center> centerList;
		centerList = CentersOperations.getCenter(this);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("C")) {
					machineId = c.machineID;

					return c.machineID
							+ (AppStateConfigurationOperations.getMaxId(this) + 1);

				}
			}
		}
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("P")) {
					machineId = c.machineID;
					return c.machineID
							+ (AppStateConfigurationOperations.getMaxId(this) + 1);

				}
			}
		}
		return "";
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
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, "");
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public void onBackPressed() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_box_yes_no);
		// set the custom dialog components - text, image and button

		Button noButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
		Button yesButton = (Button) dialog.findViewById(R.id.dialogButtonYes);
		TextView message = (TextView) dialog.findViewById(R.id.messageText);
		TextView title = (TextView) dialog.findViewById(R.id.text);
		message.setText(getResources().getString(R.string.doYouWantToContinue));
		title.setText(getResources().getString(R.string.cancellation));
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toHome();

			}
		});
		// if button is clicked, close the custom dialog
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
