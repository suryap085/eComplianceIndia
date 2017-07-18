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
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewVisitorSelectActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_visitor_list);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
	}


	public void onPatientClick(View v) {

	if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Admin_Login_Required, this).equals("1")) {
			if (((eComplianceApp) this.getApplicationContext()).IsAdminLoggedIn
					&& ((eComplianceApp) this.getApplicationContext()).visitorType == VisitorType.PM) {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.patient_default_language);
				Button next = (Button) dialog.findViewById(R.id.btn_next);

				ImageView local_img = (ImageView) dialog
						.findViewById(R.id.local_img);
				ImageView second_imag = (ImageView) dialog
						.findViewById(R.id.second_img);
				String local_language = ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_local_language, this);
				String second_language = ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_secondary_language, this);

				// set default language
				if (local_language.equals("")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_hi));

				}
				if (second_language.equals("")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_en));
				}

				// set local language
				if (local_language.equals("hi")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_hi));

				} else if (local_language.equals("en")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_en));

				} else if (local_language.equals("ka")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ka));

				} else if (local_language.equals("ma")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ma));

				}

				// set second language
				if (second_language.equals("hi")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_hi));

				} else if (second_language.equals("en")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_en));

				} else if (second_language.equals("ka")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ka));

				} else if (second_language.equals("ma")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ma));

				}
				next.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((eComplianceApp) NewVisitorSelectActivity.this
								.getApplicationContext()).tempQualityBenchmark = ((eComplianceApp) NewVisitorSelectActivity.this
								.getApplicationContext()).qualityBenchmark;
						NewVisitorSelectActivity.this.finish();
						startActivity(new Intent(NewVisitorSelectActivity.this,
								QuestionnaireOneActivity.class));
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);

					}
				});

				dialog.show();
			} else {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				// set the custom dialog components - text, image and button

				Button noButton = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				Button yesButton = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
				TextView message = (TextView) dialog
						.findViewById(R.id.messageText);
				TextView title = (TextView) dialog.findViewById(R.id.text);
				message.setText(getResources().getString(
						R.string.pmLoginRequiredFull));
				title.setText(getResources().getString(
						R.string.patientRegistraton));
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
		} else {
			if (((eComplianceApp) this.getApplicationContext()).IsAdminLoggedIn) {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.patient_default_language);
				Button next = (Button) dialog.findViewById(R.id.btn_next);

				ImageView local_img = (ImageView) dialog
						.findViewById(R.id.local_img);
				ImageView second_imag = (ImageView) dialog
						.findViewById(R.id.second_img);
				String local_language = ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_local_language, this);
				String second_language = ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_secondary_language, this);

				// set default language
				if (local_language.equals("")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_hi));

				}
				if (second_language.equals("")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_en));
				}

				// set local language
				if (local_language.equals("hi")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_hi));

				} else if (local_language.equals("en")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_en));

				} else if (local_language.equals("ka")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ka));

				} else if (local_language.equals("ma")) {
					local_img.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ma));

				}

				// set second language
				if (second_language.equals("hi")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_hi));

				} else if (second_language.equals("en")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_en));

				} else if (second_language.equals("ka")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ka));

				} else if (second_language.equals("ma")) {
					second_imag.setImageDrawable(getResources().getDrawable(
							R.drawable.img_ma));

				}
				next.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((eComplianceApp) NewVisitorSelectActivity.this
								.getApplicationContext()).tempQualityBenchmark = ((eComplianceApp) NewVisitorSelectActivity.this
								.getApplicationContext()).qualityBenchmark;
						NewVisitorSelectActivity.this.finish();
						startActivity(new Intent(NewVisitorSelectActivity.this,
								QuestionnaireOneActivity.class));
						overridePendingTransition(R.anim.right_side_in,
								R.anim.right_side_out);

					}
				});

				dialog.show();
			} else {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				// set the custom dialog components - text, image and button

				Button noButton = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				Button yesButton = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
				TextView message = (TextView) dialog
						.findViewById(R.id.messageText);
				TextView title = (TextView) dialog.findViewById(R.id.text);
				message.setText(getResources().getString(
						R.string.counselorLoginRequired));
				title.setText(getResources().getString(
						R.string.patientRegistraton));
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

		
	}

	public void onCounselorClick(View v) {
		SendToRegister(VisitorType.Counselor);
	}

	public void onQAClick(View v) {
		SendToRegister(VisitorType.QualityAuditor);
	}

	public void onPMClick(View v) {
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Admin_Login_Required, this).equals("1")) {
			if (((eComplianceApp) this.getApplicationContext()).IsAdminLoggedIn
					&& ((eComplianceApp) this.getApplicationContext()).visitorType == VisitorType.PM) {
				SendToRegister(VisitorType.PM);
			} else {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				// set the custom dialog components - text, image and button

				Button noButton = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				Button yesButton = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
				TextView message = (TextView) dialog
						.findViewById(R.id.messageText);
				TextView title = (TextView) dialog.findViewById(R.id.text);
				message.setText(getResources().getString(
						R.string.pmLoginRequiredFull));
				title.setText(getResources().getString(R.string.pmRegistration));
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
		} else {
			SendToRegister(VisitorType.PM);
		}
	}

	public void onCDPClick(View v) {
		SendToRegister(VisitorType.CDP);
	}

	public void onOthersClick(View v) {
		SendToRegister(VisitorType.Other);
	}

	public void SendToRegister(VisitorType visitorType) {
		Intent intent = new Intent(this, NewVisitorActivity.class);
		((eComplianceApp) this.getApplicationContext()).tempQualityBenchmark = ((eComplianceApp) this
				.getApplicationContext()).qualityBenchmark;
		intent.putExtra(IntentKeys.key_visitor_type, visitorType);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.key_message_home, "");
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void toHome() {
		Intent intent = new Intent();
		intent = new Intent(this, HomeActivity.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}
