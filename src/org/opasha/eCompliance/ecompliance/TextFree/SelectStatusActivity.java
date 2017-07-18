/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.io.File;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientStatusMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

public class SelectStatusActivity extends Activity {
	PatientDetailsIntent det = new PatientDetailsIntent();
	Enums.IntentFrom intentFrom;
	LinearLayout main;
	boolean fromEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_regimen_text_free);
		main = (LinearLayout) findViewById(R.id.selectCategory);

		Bundle extra = getIntent().getExtras();
		if (extra == null) {
			this.finish();
			return;
		}
		det = new Gson().fromJson(
				extra.getString(IntentKeys.key_petient_details),
				PatientDetailsIntent.class);
		ArrayList<String> mas = PatientStatusMasterOperations.getStatuss(
				Schema.PATIENT_STATUS_MASTER_IS_ACTIVE + "=1", this);
		try {
			det.status = PatientsOperations.getPatientDetails(det.treatmentId,
					this).Status;
		} catch (Exception e) {

		}
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File directory = new File(sd, "//eComplianceClient//"
				+ DbUtils.getTabId(this) + "//resources//App//");
		for (final String m : mas) {
			File file = new File(directory, m + ".png");
			if (!file.exists()) {
				gohome();
				return;
			}
			Bitmap bitmapImage = BitmapFactory
					.decodeFile(Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
							+ "//eComplianceClient//"
							+ DbUtils.getTabId(this)
							+ "//resources//App//" + m + ".png");
			Drawable drawableImage = new BitmapDrawable(this.getResources(),
					bitmapImage);
			ImageView b = new ImageView(this);
			LinearLayout.LayoutParams mra = new LinearLayout.LayoutParams(70,
					70);
			if (m.equals(mas.get(0)))
				mra.setMargins(0, 120, 0, 15);
			else
				mra.setMargins(0, 0, 0, 15);

			b.setLayoutParams(mra);
			b.setPadding(2, 2, 2, 2);
			b.setImageDrawable(drawableImage);
			b.setBackgroundResource(R.drawable.edit_box_animation);
			b.setTag(m);
			try {
				if (m.equals(det.status)) {
					int count = main.getChildCount();
					for (int i = 0; i < count; i++) {
						main.getChildAt(i).setSelected(false);
					}
					b.setSelected(true);
					fromEdit = true;
				}
			} catch (Exception e) {
			}

			if (!fromEdit) {
				if (m.equals(mas.get(0))) {
					b.setSelected(true);
				}
			}
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
				det.status = view.getTag().toString();
				det.backIntent.add(backIntent.status);
				goNext();
			}
		}
	}

	public void onBackClick(View v) {
		goBack();
	}

	private void goNext() {
		if (!det.status.equals(Enums.StatusType
				.getStatusType(StatusType.Active))) {
			startActivity(new Intent(this, ShowFinalRegistration.class)
					.putExtra(IntentKeys.key_petient_details,
							new Gson().toJson(det)));
			this.finish();
			overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
			return;
		}
		startActivity(new Intent(this, SelectCategoryActivity.class).putExtra(
				IntentKeys.key_petient_details, new Gson().toJson(det)));
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);

	}

	private void gohome() {
		startActivity(new Intent(this, HomeActivityTextFree.class));
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	@Override
	public void onBackPressed() {
		goBack();

	}

	private void goBack() {
		backIntent backIntent = det.backIntent.get(det.backIntent.size() - 1);
		det.backIntent.remove(det.backIntent.size() - 1);
		Intent intent = new Intent(this, ReportListActivity.class);
		switch (backIntent) {
		case scan:
			intent = new Intent(this, HomeActivityTextFree.class);

			break;
		case icon:
			intent = new Intent(this, SelectImageActivity.class);
			intent.putExtra(IntentKeys.key_petient_details,
					new Gson().toJson(det));
			break;
		default:

			break;
		}
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}