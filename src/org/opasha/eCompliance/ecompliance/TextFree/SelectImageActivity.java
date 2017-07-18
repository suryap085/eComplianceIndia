/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.HomeActivity;
import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.Adapters.ImageListAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

public class SelectImageActivity extends Activity {
	ArrayList<String> list;
	ImageListAdapter imagesAdapter;
	GridView gallery;

	PatientDetailsIntent det = new PatientDetailsIntent();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_image);
		list = new ArrayList<String>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			det = new Gson().fromJson(
					extras.getString(IntentKeys.key_petient_details),
					PatientDetailsIntent.class);
		}
		// videoView.setVideoURI(Uri
		// .parse("android.resource://org.opasha.eCompliance.ecompliance/"
		// + R.raw.testing_video));
		gallery = (GridView) findViewById(R.id.gridview);
		list = MasterIconOperation.getNineRendomIcons(this);
		imagesAdapter = new ImageListAdapter(this, list);
		gallery.setAdapter(imagesAdapter);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				ImageView view = (ImageView) v.findViewById(R.id.imageView);
				String id = view.getTag().toString();
				int count = gallery.getChildCount();
				for (int i = 0; i < count; i++) {
					LinearLayout lay = (LinearLayout) gallery.getChildAt(i);
					View la = lay.getChildAt(0);
					String ids = la.getTag().toString();
					if (ids.equals(id)) {
						gallery.getChildAt(i).setSelected(true);
					} else {
						gallery.getChildAt(i).setSelected(false);
					}
				}
			}
		});
	}

	public void onBackClick(View v) {
		goback();
	}

	public void onImageClick(View v) {

		// int id = v.getId();
		// int count = gallery.getChildCount();
		// for (int i = 0; i < count; i++) {
		// int ids = (gallery.getChildAt(i)).getId();
		// if (ids == id) {
		// gallery.getChildAt(i).setSelected(true);
		// } else {
		// gallery.getChildAt(i).setSelected(false);
		// }
		// }
		String id = v.getTag().toString();
		int count = gallery.getChildCount();
		for (int i = 0; i < count; i++) {
			String ids = gallery.getChildAt(i).getTag().toString();
			if (ids.equals(id)) {
				gallery.getChildAt(i).setSelected(true);
			} else {
				gallery.getChildAt(i).setSelected(false);
			}
		}
	}

	private void goback() {
		try {
			backIntent backIntent = det.backIntent
					.get(det.backIntent.size() - 1);
			det.backIntent.remove(det.backIntent.size() - 1);
			Intent intent = new Intent(this, SelectCenterActivity.class);
			intent.putExtra(IntentKeys.key_petient_details,
					new Gson().toJson(det));
			switch (backIntent) {
			case scan:
				if (det.intentFrom == Enums.IntentFrom.Home) {
					ScansOperations.deleteScans(this, det.treatmentId);
				}
				intent = new Intent(this, HomeActivityTextFree.class);

				break;
			default:
				break;
			}
			startActivity(intent);
			this.finish();
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		} catch (Exception e) {
			if (det.intentFrom == Enums.IntentFrom.Home) {
				ScansOperations.deleteScans(this, det.treatmentId);
			}
			startActivity(new Intent(this, HomeActivity.class).putExtra(
					IntentKeys.key_signal_type, Signal.Bad.toString())
					.putExtra(
							IntentKeys.key_message_home,
							getResources().getString(
									R.string.registrationCanceled)));
			this.finish();
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		}

	}

	public void onNextClick(View v) {
		int count = gallery.getChildCount();
		for (int i = 0; i < count; i++) {
			if (gallery.getChildAt(i).isSelected()) {
				LinearLayout lay = (LinearLayout) gallery.getChildAt(i);
				View view = lay.getChildAt(0);
				PatientDetailsIntent p = new PatientDetailsIntent();
				p.iconId = view.getTag().toString();
				p.treatmentId = det.treatmentId;
				p.intentFrom = det.intentFrom;
				p.center = det.center;
				p.backIntent.add(backIntent.icon);
				if (p.intentFrom == Enums.IntentFrom.EditPatient) {
					startActivity(new Intent(
							org.opasha.eCompliance.ecompliance.TextFree.SelectImageActivity.this,
							SelectStatusActivity.class).putExtra(
							IntentKeys.key_petient_details,
							new Gson().toJson(p)));

				} else {
					startActivity(new Intent(
							org.opasha.eCompliance.ecompliance.TextFree.SelectImageActivity.this,
							SelectCategoryActivity.class).putExtra(
							IntentKeys.key_petient_details,
							new Gson().toJson(p)));
				}
				org.opasha.eCompliance.ecompliance.TextFree.SelectImageActivity.this
						.finish();
				overridePendingTransition(R.anim.left_side_out,
						R.anim.left_side_in);
			}
		}
	}

	@Override
	public void onBackPressed() {
		goback();
	}
}
