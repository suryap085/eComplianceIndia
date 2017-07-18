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
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
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

public class SelectStageActivity extends Activity {
	PatientDetailsIntent det;
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
		}
		det = new Gson().fromJson(
				extra.getString(IntentKeys.key_petient_details),
				PatientDetailsIntent.class);
		ArrayList<Master> mas = RegimenMasterOperations.getAllStage(
				Schema.REGIMEN_MASTER_CATEGORY + "='" + det.category + "'",
				this);
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File directory = new File(sd, "//eComplianceClient//"
				+ DbUtils.getTabId(this) + "//resources//App//");
		String stageTemp = TreatmentInStagesOperations.getPatientStage(det.treatmentId, this);
		for (final Master m : mas) {
			File file = new File(directory, m.stage + ".png");
			if (!file.exists()) {
				ScansOperations.deleteScans(this, det.treatmentId);
				gohome();
				this.finish();
			}
			Bitmap bitmapImage = BitmapFactory
					.decodeFile(Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
							+ "//eComplianceClient//"
							+ DbUtils.getTabId(this)
							+ "//resources//App//" + m.stage + ".png");
			Drawable drawableImage = new BitmapDrawable(this.getResources(),
					bitmapImage);
			ImageView b = new ImageView(this);
			LinearLayout.LayoutParams mra = new LinearLayout.LayoutParams(70,
					70);
			mra.setMargins(0, 0, 0, 20);
			b.setLayoutParams(mra);
			b.setTag(m.stage);
			b.setPadding(2, 2, 2, 2);
			b.setImageDrawable(drawableImage);
			b.setBackgroundResource(R.drawable.edit_box_animation);

			try {
				if (m.stage.equals(stageTemp)) {
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
				if (m.stage.equals(mas.get(0).stage)) {
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
				det.stage = view.getTag().toString();
				goNext();
			}
		}
	}

	private void goNext() {
		startActivity(new Intent(this, SelectScheduleActivity.class).putExtra(
				IntentKeys.key_petient_details, new Gson().toJson(det)));
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void onBackClick(View v) {
		goBack();
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
		startActivity(new Intent(this, SelectCategoryActivity.class).putExtra(
				IntentKeys.key_petient_details, new Gson().toJson(det)));
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}
}
