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

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.backIntent;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.gson.Gson;

public class ReportListActivity extends Activity {
	TableLayout pendingTable;
	Enums.ReportType ReportType;
	Enums.IntentFrom intentFrom;
	LinearLayout buttonLayout;
	PatientDetailsIntent det = new PatientDetailsIntent();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pending_list_text_free);
		pendingTable = (TableLayout) findViewById(R.id.videoview);
		buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		Bundle extra = getIntent().getExtras();
		if (extra == null) {
			this.finish();
			return;
		}
		ReportType = (org.opasha.eCompliance.ecompliance.util.Enums.ReportType) extra
				.get(IntentKeys.key_report_type);
		intentFrom = (org.opasha.eCompliance.ecompliance.util.Enums.IntentFrom) extra
				.get(IntentKeys.key_intent_from);
		pendingTable.addView(generatePendingList());
		pendingTable.invalidate();
		switch (intentFrom) {
		case EditPatient:
			buttonLayout.setVisibility(View.VISIBLE);
			break;
		case HavingTrouble:
			buttonLayout.setVisibility(View.VISIBLE);
			break;
		default:
			buttonLayout.setVisibility(View.GONE);
			break;
		}
	}

	public void onNextClick(View v) {
		TableLayout lay = (TableLayout) pendingTable.getChildAt(0);
		int count = lay.getChildCount();
		for (int i = 0; i < count; i++) {
			TableRow rows = (TableRow) lay.getChildAt(i);
			for (int j = 0; j < 3; j++) {
				try {
					LinearLayout lays = (LinearLayout) rows.getChildAt(j);
					if (lays.getChildAt(0).isSelected()) {
						LinearLayout layout = (LinearLayout) rows.getChildAt(j);
						String iconTemp = layout.getChildAt(0).getTag()
								.toString();
						det.iconId = iconTemp;
						det.treatmentId = PatientIconOperation.getId(iconTemp,
								ReportListActivity.this);
						det.intentFrom = intentFrom;
						if (intentFrom.equals(Enums.IntentFrom.EditPatient)) {
							det.backIntent.add(backIntent.scan);
							det.backIntent.add(backIntent.report);
							startActivity(new Intent(ReportListActivity.this,
									SelectStatusActivity.class).putExtra(
									IntentKeys.key_petient_details,
									new Gson().toJson(det)));
							ReportListActivity.this.finish();
							overridePendingTransition(R.anim.left_side_out,
									R.anim.left_side_in);
							return;
						} else {
							det.verificationReason = getResources().getString(
									R.string.MarkVisit);
							startActivity(new Intent(ReportListActivity.this,
									VerifyActivityTextFree.class).putExtra(
									IntentKeys.key_petient_details,
									new Gson().toJson(det)));
							ReportListActivity.this.finish();
							overridePendingTransition(R.anim.left_side_out,
									R.anim.left_side_in);
							return;
						}
					}
				} catch (Exception e) {

				}
			}
		}
	}

	public void onBackClick(View v) {
		goBack();
	}

	private TableLayout generatePendingList() {
		ArrayList<String> list;
		switch (ReportType) {
		case VisitedPatients:
			list = ((eComplianceApp) this.getApplicationContext()).visitToday;
			break;
		case AllPatients:
			list = PatientsOperations.getActivePatientListForTextFree(
					Schema.PATIENTS_IS_DELETED + "=0 and "
							+ Schema.PATIENTS_STATUS + " ='"
							+ Enums.StatusType.getStatusType(StatusType.Active)
							+ "'", this);
			break;
		default:
			list = ((eComplianceApp) this.getApplicationContext()).pendingDoses;
			break;
		}
		int c = 0;
		String path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
				+ "/eComplianceClient/"
				+ DbUtils.getTabId(this)
				+ "/resources/";
		TableLayout layouta = getTableLayout();
		TableRow row = getTableRow();
		for (String s : list) {
			final String icon = PatientIconOperation.getIcon(s, this);
			Bitmap bitmapImage = BitmapFactory.decodeFile(path + icon);
			Drawable drawableImage = new BitmapDrawable(this.getResources(),
					bitmapImage);
			if (c == 3) {
				layouta.addView(row);
				row = getTableRow();
				c = 0;
			}
			LayoutInflater mInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vi = mInflater.inflate(R.layout.image_view, null);

			ImageView view = (ImageView) vi.findViewById(R.id.imageView);
			view.setTag(icon);
			view.setImageDrawable(drawableImage);
			view.setBackgroundResource(R.drawable.edit_box_animation);
			switch (ReportType) {
			case AllPatients:
				vi.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageView view = (ImageView) v
								.findViewById(R.id.imageView);
						String id = view.getTag().toString();
						TableLayout lay = (TableLayout) pendingTable
								.getChildAt(0);
						String icon = ((ImageView) v
								.findViewById(R.id.imageView)).getTag()
								.toString();
						int count = lay.getChildCount();
						for (int i = 0; i < count; i++) {
							TableRow rows = (TableRow) lay.getChildAt(i);
							for (int j = 0; j < 3; j++) {
								try {
									LinearLayout layout = (LinearLayout) rows
											.getChildAt(j);
									ImageView vis = (ImageView) layout
											.getChildAt(0);
									String iconTemp = vis.getTag().toString();
									if (iconTemp.equals(icon)) {
										vis.setSelected(true);
									} else {
										vis.setSelected(false);
									}
								} catch (Exception e) {

								}
							}
						}

					}
				});
				break;
			default:
				break;
			}
			row.addView(vi);
			c++;
		}
		for (int a = c; a < 3; a++) {
			LayoutInflater mInflaterr = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vi = mInflaterr.inflate(R.layout.image_view, null);
			vi.setVisibility(View.GONE);
			row.addView(vi);

		}
		layouta.addView(row);
		return layouta;
	}

	private TableRow getTableRow() {
		TableRow tableRowa = new TableRow(this);
		tableRowa.setGravity(Gravity.CENTER);
		tableRowa.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		return tableRowa;
	}

	private TableLayout getTableLayout() {
		TableLayout tableLayout = new TableLayout(this);
		tableLayout.setStretchAllColumns(true);
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
		return tableLayout;
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	private void goBack() {
		startActivity(new Intent(this, HomeActivityTextFree.class));
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public static void main(String[] args) {

	}
}
