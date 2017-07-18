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
import java.util.HashMap;
import java.util.Map;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientHospitalizationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.Model.Dose;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import android.app.Fragment;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DoseReportActivity extends Fragment {
	private final int NUM_OF_DAYS_IN_WEEK = 7;
	private final long ONE_DAY = 86400000;
	HashMap<Integer, String> stages = new HashMap<Integer, String>();
	int doseDrawableId = R.drawable.dose_background;// for dose background.
	String stage = "";// use for dose background.
	String treamentId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		treamentId = getActivity().getIntent().getExtras()
				.getString(IntentKeys.key_treatment_id);

		return DoseView();
	}

	/**
	 * @return calendar view for doses
	 */
	private View DoseView() {
		TableLayout mainLayout = getTableLayout();
		// TableLayout headerLayout = getDoseHeaderLayout(treamentId);
		ScrollView bodyLayout = bodyLayout();
		ArrayList<Dose> allDoses = DoseAdminstrationOperations
				.getPatientAllDoses(treamentId, getActivity());
		ArrayList<Dose> hospDoses = PatientHospitalizationOperations
				.getPatienthospitalisedDose(treamentId, getActivity());

		boolean showLineSelfAdmin = false;
		boolean ifPatientInCp = false;
		if (TreatmentInStagesOperations.getPatientStage(treamentId,
				getActivity()) != null) {
			ifPatientInCp = TreatmentInStagesOperations
					.getPatientStage(treamentId, getActivity()).equals("CP");
		}

		// get all doses and map all of them in hashmap for all dates between
		// dose start and end date
		Map<Long, String> map = new HashMap<Long, String>();
		if (!allDoses.isEmpty()) {
			TableLayout monthLayout = getTableLayout();
			long doseStart = allDoses.get(0).doseDate;
			long doseEnd = allDoses.get(allDoses.size() - 1).doseDate;
			long hospEnd = 0;
			if (hospDoses.size() > 0) {
				hospEnd = hospDoses.get(hospDoses.size() - 1).doseDate;
			}

			if (hospEnd > doseEnd)
				doseEnd = hospEnd;

			if (ifPatientInCp)
				doseEnd += ONE_DAY;

			for (long i = doseStart; i <= doseEnd; i = i + ONE_DAY) {
				map.put(i, "");
			}
			for (Dose d : allDoses) {
				map.put(d.doseDate, d.doseType + "," + d.regimenId);
			}

			for (Dose d : hospDoses) {
				if (map.containsKey(d.doseDate)) {
					String value = map.get(d.doseDate);
					if (value.contains("Supervised")
							|| value.contains("SelfAdministered"))
						continue;
				}
				map.put(d.doseDate, d.doseType + "," + d.regimenId);
			}

			TableLayout tempLayout = getTableLayout();
			int numMonths = GenUtils.calcMonthsBetween(doseStart, doseEnd);
			long monthStart = doseStart;
			// create table layout for all doses from dose start date
			// to
			// dose end date
			for (int i = 0; i <= numMonths; i++) {
				tempLayout.addView(getMonthHeaderLayout(GenUtils
						.toDate(monthStart)));
				tempLayout.addView(getDaysRow());
				long monthLast = GenUtils.monthEndDate(monthStart);
				int dayOfMonth = GenUtils.monthDay(monthStart);
				int dayOfWeek = GenUtils.dateToDay(monthStart);
				TableRow row = getTableRow();
				for (int l = 1; l < dayOfWeek; l++) {
					row.addView(getDoseView(0, "", false));
				}
				if (doseEnd <= monthLast) {
					monthLast = doseEnd;
				}
				// layout for a month
				for (long j = monthStart; j <= monthLast; j = j + ONE_DAY) {
					String[] split = map.get(j).split(",");
					if (split.length == 2) {
						if (isCp(Integer.parseInt(split[1]))) {
							String doseType = split[0];

							if (doseType.equals(Enums.DoseType.Supervised
									.toString())
									|| doseType
											.equals(Enums.DoseType.SelfAdministered
													.toString())
									|| doseType
											.equals(Enums.DoseType.Unsupervised
													.toString())) {
								showLineSelfAdmin = true;
							} else {
								showLineSelfAdmin = false;
							}
						} else {
							showLineSelfAdmin = false;
						}
					}
					if (dayOfWeek > NUM_OF_DAYS_IN_WEEK) {
						tempLayout.addView(row);
						row = getTableRow();
						dayOfWeek = 1;
					}
					row.addView(getDoseView(dayOfMonth, map.get(j),
							showLineSelfAdmin));
					dayOfWeek++;
					dayOfMonth++;
				}

				for (int k = dayOfWeek; k <= NUM_OF_DAYS_IN_WEEK; k++) {
					row.addView(getDoseView(0, "", false));
				}
				// change to next month
				monthStart = monthLast + ONE_DAY;
				tempLayout.addView(row);
				monthLayout.addView(tempLayout);
				tempLayout = new TableLayout(getActivity());
				tempLayout.setStretchAllColumns(true);
				TableLayout.LayoutParams mra = new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				mra.setMargins(0, 50, 0, 0);
				tempLayout.setLayoutParams(mra);
			}
			bodyLayout.addView(monthLayout);

		}

		// mainLayout.addView(headerLayout);
		// mainLayout.addView(patientDataLayout);
		mainLayout.addView(bodyLayout);

		return mainLayout;
	}

	public ScrollView bodyLayout() {
		ScrollView bodyLayout = new ScrollView(getActivity());
		ScrollView.LayoutParams sv = new ScrollView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bodyLayout.setLayoutParams(sv);
		bodyLayout.setPadding(10, 0, 10, 0);
		return bodyLayout;
	}

	private TableLayout getTableLayout() {
		TableLayout tableLayout = new TableLayout(getActivity());
		tableLayout.setStretchAllColumns(true);
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT));
		return tableLayout;
	}

	private TableRow getTableRow() {
		TableRow tableRowa = new TableRow(getActivity());
		tableRowa.setGravity(Gravity.CENTER);
		tableRowa.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));
		return tableRowa;
	}

	// private TableRow getPatientTableRow() {
	// TableRow tableRowa = new TableRow(getActivity());
	// tableRowa.setOrientation(tableRowa.HORIZONTAL);
	// tableRowa.setLayoutParams(new TableRow.LayoutParams(
	// TableRow.LayoutParams.MATCH_PARENT,
	// TableRow.LayoutParams.MATCH_PARENT));
	// return tableRowa;
	// }

	private TextView getTextView() {
		TextView button = new TextView(getActivity());
		LinearLayout.LayoutParams llp = new TableRow.LayoutParams(5, 60);
		button.setPadding(20, 0, 20, 0);
		button.setLayoutParams(llp);
		button.setGravity(Gravity.CENTER);
		return button;
	}

	private LinearLayout getDoseView(int date, String doseTypeRegimenID,
			boolean showLine) {
		String doseType = "";
		int regimenID = 0;
		if (!doseTypeRegimenID.equals("")) {
			String split[] = doseTypeRegimenID.split(",");
			if (split.length < 2) {
				return new LinearLayout(getActivity());
			}
			doseType = split[0];
			regimenID = Integer.parseInt(split[1]);
		}
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		final ImageView iv = new ImageView(getActivity());
		if (doseType.equals("Hospitalised")) {
			iv.setImageResource(R.drawable.hospitalizedpatientsimg);
		} else if (doseType.equals(Enums.DoseType.getDoseType(
				DoseType.Supervised).toString())) {
			iv.setImageResource(R.drawable.supervised);
		} else if (doseType.equals(Enums.DoseType.getDoseType(
				DoseType.SelfAdministered).toString())) {
			 iv.setImageResource(R.drawable.self_administered);
		} else if (doseType.equals(Enums.DoseType.getDoseType(
				DoseType.Unsupervised).toString())) {
			iv.setImageResource(R.drawable.unsupervised);
		} else if (doseType.equals(Enums.DoseType.getDoseType(DoseType.Missed)
				.toString())) {
			iv.setImageResource(R.drawable.missed);
		}
		iv.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25,
				LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 4, 0, 8);
		iv.setLayoutParams(lp);
		linearLayout.addView(iv);
		final TextView tv = new TextView(getActivity());
		tv.setTextColor(0xFF000000);
		tv.setTypeface(null, Typeface.BOLD);
		if (date != 0) {
			tv.setText("" + date);
		}

		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setTextSize(18);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 20, 0, 20);
		tv.setLayoutParams(lp);
		tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		if (showLine) {
			if (doseType.equals(Enums.DoseType.getDoseType(DoseType.Missed)
					.toString())) {
			} else if (doseType.equals(Enums.DoseType.getDoseType(
					DoseType.Supervised).toString())) {
			} else if (doseType.equals(Enums.DoseType.getDoseType(
					DoseType.SelfAdministered).toString())) {
				tv.setPaintFlags(tv.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
			} else if (doseType.equals(Enums.DoseType.getDoseType(
					DoseType.Unsupervised).toString())) {
			} else if (doseType.equals("Hospitalised")) {
				iv.setImageResource(R.drawable.hospitalizedpatientsimg);
			} else {
				tv.setPaintFlags(tv.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
			}
		}
		linearLayout.addView(tv);
		linearLayout.setBackgroundResource(getDoseStageColor(date, regimenID));
		linearLayout.getBackground().setAlpha(80);
		return linearLayout;
	}

	private int getDoseStageColor(int date, int regimenId) {
		if (date == 0) {
			doseDrawableId = R.drawable.dose_background;

		}
		if (regimenId != 0) {
			if (stages.containsKey(regimenId)) {
				stage = stages.get(regimenId);
			} else {
				stage = RegimenMasterOperations.getSatge(regimenId,
						getActivity());
				stages.put(regimenId, stage);
			}
		}
		if (stage.equals(Enums.StageType.getStageType(StageType.IP).toString())) {
			doseDrawableId = R.drawable.ip_dose_background;
		} else if (stage.equals(Enums.StageType.getStageType(StageType.ExtIP)
				.toString())) {
			doseDrawableId = R.drawable.ext_dose_background;
		} else if (stage.equals(Enums.StageType.getStageType(StageType.CP)
				.toString())) {
			doseDrawableId = R.drawable.cp_dose_background;
		}
		return doseDrawableId;
	}

	private boolean isCp(int regimenId) {

		boolean iscp = false;
		if (regimenId != 0) {
			String stage = RegimenMasterOperations.getSatge(regimenId,
					getActivity());

			if (stage.equals(Enums.StageType.getStageType(StageType.IP)
					.toString())) {
			} else if (stage.equals(Enums.StageType.getStageType(
					StageType.ExtIP).toString())) {
			} else if (stage.equals(Enums.StageType.getStageType(StageType.CP)
					.toString())) {
				iscp = true;
			}
		}
		return iscp;
	}

	/**
	 * add row of days in starting of every month.
	 * 
	 * @return
	 */
	private TableLayout getDaysRow() {

		TableLayout layouta = getTableLayout();
		TableRow row = getTableRow();

		for (int days = 0; days < 7; days++) {
			TextView view = getTextView();
			view.setText(getDay(days));
			view.setTextSize(23);
			view.setTypeface(null, Typeface.BOLD);
			view.setBackgroundColor(getResources().getColor(R.color.CatTwo));
			row.addView(view);
		}
		layouta.addView(row);
		layouta.setPadding(0, 0, 0, 0);
		return layouta;
	}

	/**
	 * header layout to show treatmentId
	 * 
	 * @return
	 */
	// private TableLayout getDoseHeaderLayout(String treatmentId) {
	// TableLayout a = new TableLayout(getActivity());
	// TableLayout.LayoutParams mr = new TableLayout.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	// mr.setMargins(0, 10, 0, 0);
	// a.setLayoutParams(mr);
	// a.setPadding(0, 0, 0, 0);
	// TableRow idRow = getTableRow();
	// TextView id = new TextView(getActivity());
	// id.setText(getResources().getString(R.string.treatmentId) + treatmentId);
	// id.setTextSize(23);
	// id.setTypeface(null, Typeface.BOLD);
	// idRow.addView(id);
	// a.addView(idRow);
	// a.setBackgroundResource(R.drawable.catone_gradient_bg);
	// return a;
	// }

	/**
	 * @param doseMonth
	 * @return month header
	 */
	private TableLayout getMonthHeaderLayout(String doseMonth) {
		TableLayout layouta = getTableLayout();
		TableLayout.LayoutParams mr = new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mr.setMargins(0, 10, 0, 0);
		layouta.setLayoutParams(mr);
		layouta.setPadding(0, 0, 0, 0);
		TableRow monthRow = getTableRow();
		TextView view = getTextView();
		String[] dosemonths = doseMonth.split(",");
		view.setText(dosemonths[0] + ", " + dosemonths[1]);
		view.setTextSize(23);
		view.setTypeface(null, Typeface.BOLD);
		monthRow.addView(view);
		monthRow.setBackgroundResource(R.drawable.dose_month_background);
		layouta.addView(monthRow);
		return layouta;
	}

	private String getDay(int day) {
		switch (day) {
		case 1:
			return "Mo";
		case 2:
			return "Tu";
		case 3:
			return "We";
		case 4:
			return "Th";
		case 5:
			return "Fr";
		case 6:
			return "Sa";
		case 0:
			return "Su";
		default:
			return "MO";
		}
	}
}