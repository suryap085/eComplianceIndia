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
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;

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

public class SelectCenterActivity extends Activity {
	PatientDetailsIntent det;
	LinearLayout main;

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
		ArrayList<Center> mas = CentersOperations.getCenterForSpinner(false,
				this);
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File directory = new File(sd, "//eComplianceClient//"
				+ DbUtils.getTabId(this) + "//resources//App//");
		for (final Center m : mas) {
			File file = new File(directory, m.centerName + ".png");
			if (!file.exists()) {
				ScansOperations.deleteScans(this, det.treatmentId);
				gohome();
				this.finish();
				return;
			}
			Bitmap bitmapImage = BitmapFactory
					.decodeFile(Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
							+ "//eComplianceClient//"
							+ DbUtils.getTabId(this)
							+ "//resources//App//" + m.centerName + ".png");
			Drawable drawableImage = new BitmapDrawable(this.getResources(),
					bitmapImage);
			ImageView b = new ImageView(this);
			LinearLayout.LayoutParams mra = new LinearLayout.LayoutParams(100,
					100);
			mra.setMargins(0, 0, 0, 30);
			b.setLayoutParams(mra);
			b.setTag(m.centerName);
			b.setImageDrawable(drawableImage);
			b.setBackgroundResource(R.drawable.edit_box_animation);
			if (m.centerName.equals(mas.get(0).centerName)) {
				b.setSelected(true);
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
				det.center = view.getTag().toString();
				det.backIntent.add(backIntent.center);
				goNext();
			}
		}
	}

	private void goNext() {
		startActivity(new Intent(this, SelectImageActivity.class).putExtra(
				IntentKeys.key_petient_details, new Gson().toJson(det)));
		this.finish();
		overridePendingTransition(R.anim.left_side_out, R.anim.left_side_in);
	}

	public void onBackClick(View v) {
		goBack();
	}

	private void goBack() {
		if (det.intentFrom == Enums.IntentFrom.Home) {
			ScansOperations.deleteScans(this, det.treatmentId);
		}
		startActivity(new Intent(this, HomeActivityTextFree.class));
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

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

}
