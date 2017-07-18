/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DoseUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class UnsupervisedDoseTextFree extends Activity {
	LinearLayout main;
	TextView txtHeader;
	String treatmentID;
	Bundle extras;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_regimen_text_free);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		main = (LinearLayout) findViewById(R.id.selectCategory);
		extras = getIntent().getExtras();
		if (extras == null) {
			this.finish();
			return;
		}
		treatmentID = extras.getString(IntentKeys.key_treatment_id);
		addItemsOnUnsupervisedDoseSpinner();
	}

	public void addItemsOnUnsupervisedDoseSpinner() {
		int unsupervisedCount = 0;
		int maxNumOfDose = Integer
				.parseInt(ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_max_unsupervised_dose, this));
		int regimenId = TreatmentInStagesOperations.getPatientRegimenId(
				treatmentID, this);
		String stage = RegimenMasterOperations.getSatge(regimenId, this);
		if ((DoseAdminstrationOperations.getLastDose(treatmentID, this).doseType)
				.toString().equals(Enums.DoseType.Unsupervised.toString())) {
			unsupervisedCount = DoseAdminstrationOperations
					.getUnsupervisedCount(treatmentID, this);
		}
		int availableLimit = maxNumOfDose - unsupervisedCount;
		if (stage.equals(Enums.StageType.CP.toString())) {
			availableLimit = availableLimit / 3;
		}
		// List<String> temp = new ArrayList<String>();
		int Value = 0;
		for (int i = 0; i < availableLimit; i++) {
			if (stage.equals(Enums.StageType.CP.toString())) {
				Value = Value + 3;
				if (i >= availableLimit) {
					break;
				}
			} else {
				Value++;
			}
			ImageView b = new ImageView(this);
			LinearLayout.LayoutParams mra = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 100);
			mra.setMargins(0, 0, 0, 30);
			b.setLayoutParams(mra);
			b.setPadding(2, 2, 2, 2);
			Bitmap map = GenUtils.getBitmapFromAsset(
					"unsupervised/" + String.valueOf(Value) + ".png", this);
			// Drawable drawableImage = new BitmapDrawable(this.getResources(),
			// );
			b.setImageBitmap(map);
			b.setBackgroundResource(R.drawable.edit_box_animation);
			// temp.add(String.valueOf(Value));
			b.setTag(String.valueOf(Value));
			main.addView(b);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = main.getChildCount();
					for (int i = 0; i < count; i++) {
						main.getChildAt(i).setSelected(false);
					}
					v.setSelected(true);
				}
			});

		}

	}

	public void onNextClick(View v) {
		int count = main.getChildCount();
		for (int i = 0; i < count; i++) {
			if (main.getChildAt(i).isSelected()) {
				View view = main.getChildAt(i);
				onSaveClick(view.getTag().toString());
			}
		}
	}

	public void onBackClick(View v) {
		goBack();
	}

	private void goBack() {
		startActivity(new Intent(this, HomeActivityTextFree.class));
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	private void gohome() {
		Intent intent = new Intent(this, HomeActivityTextFree.class);
		intent.putExtra(IntentKeys.key_message_home, "");
		intent.putExtra(IntentKeys.key_signal_type, Signal.Good.toString());
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	public void onSaveClick(String noOfDoses) {
		GpsTracker gps = new GpsTracker(this);
		int value = Integer.parseInt(noOfDoses);
		long nextDose = DoseAdminstrationOperations.getLastDose(treatmentID,
				this).doseDate + GenUtils.ONE_DAY;
		int regimenid = TreatmentInStagesOperations.getPatientRegimenId(
				treatmentID, this);
		int scheduleDays = RegimenMasterOperations.getRegimen(regimenid, this).scheduleDays;
		long doseStart = 0;
		int i = 1;
		do {
			int weekday = GenUtils.dateToDay(nextDose);
			boolean schToday = GenUtils.isScheduledOn(weekday, scheduleDays);
			if (schToday) {
				if (i == 1) {
					doseStart = nextDose;
				}
				DoseUtils.AddDose(treatmentID, Enums.DoseType.Unsupervised
						.toString(), nextDose, TreatmentInStagesOperations
						.getPatientRegimenId(treatmentID, this), System
						.currentTimeMillis(), ((eComplianceApp) this
						.getApplicationContext()).LastLoginId, gps
						.getLatitude(), gps.getLongitude(), this);
				// *AS HARD_CODED for CP Doses - Special Case
				Master regimen = RegimenMasterOperations.getRegimen(
						TreatmentInStagesOperations.getPatientRegimenId(
								treatmentID, this), this);
				if (regimen.stage.equals(Enums.StageType
						.getStageType(Enums.StageType.CP))) {
					long doseDate = nextDose + GenUtils.TWO_DAY;
					for (int j = 0; j < 2; j++) {
						if (regimen.schedule.equals(Enums.Schedule.Monday
								.toString())
								|| regimen.schedule
										.equals(Enums.Schedule.Wednesday
												.toString())
								|| regimen.schedule
										.equals(Enums.Schedule.Friday
												.toString())) {
							if (GenUtils.dateToDay(doseDate) == 1) {
								doseDate = doseDate + GenUtils.ONE_DAY;
							}
						} else {
							if (GenUtils.dateToDay(doseDate) == 1) {
								doseDate = doseDate + GenUtils.ONE_DAY;
							}
							if (GenUtils.dateToDay(doseDate) == 2) {
								doseDate = doseDate + GenUtils.ONE_DAY;
							}
						}
						i++;
						DoseUtils
								.AddDose(
										treatmentID,
										Enums.DoseType.Unsupervised.toString(),
										doseDate,
										TreatmentInStagesOperations
												.getPatientRegimenId(
														treatmentID, this),
										System.currentTimeMillis(),
										((eComplianceApp) this
												.getApplicationContext()).LastLoginId,
										gps.getLatitude(), gps.getLongitude(),
										this);
						doseDate = doseDate + GenUtils.TWO_DAY;
					}
				}

				nextDose = nextDose + GenUtils.ONE_DAY;
				i++;
			} else {
				nextDose = nextDose + GenUtils.ONE_DAY;
			}
		} while (i <= value);
		gohome();
	}

	public void onCancelClick(View v) {
		gohome();
	}

}
