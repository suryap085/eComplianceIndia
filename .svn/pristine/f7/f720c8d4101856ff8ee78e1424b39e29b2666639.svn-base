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

import org.opasha.eCompliance.ecompliance.DbOperations.HIVOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabFollowUpOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.Model.PatientLabFollowUpModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;

import android.widget.TableRow;
import android.widget.TextView;

public class Patient_Data extends Fragment {
	private TextView name, phoneNumber, category, stage, schedule, tbNumber,
			patientType, Nikshayid, hivStatus, status, labsummary;
	String treatmentId;
	private TableLayout tablelayout, headerTableLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		treatmentId = getActivity().getIntent().getExtras()
				.getString(IntentKeys.key_treatment_id);
		View view = inflater
				.inflate(R.layout.patient_details, container, false);

		tablelayout = (TableLayout) view.findViewById(R.id.tableLayout);
		headerTableLayout = (TableLayout) view
				.findViewById(R.id.headertableLayout);

		name = (TextView) view.findViewById(R.id.name);
		phoneNumber = (TextView) view.findViewById(R.id.phone);

		category = (TextView) view.findViewById(R.id.category);
		stage = (TextView) view.findViewById(R.id.stage);

		schedule = (TextView) view.findViewById(R.id.schedule);
		status = (TextView) view.findViewById(R.id.status);

		tbNumber = (TextView) view.findViewById(R.id.tbNumber);
		patientType = (TextView) view.findViewById(R.id.patinetType);

		Nikshayid = (TextView) view.findViewById(R.id.nikshayId);
		hivStatus = (TextView) view.findViewById(R.id.hivstatus);
		labsummary = (TextView) view.findViewById(R.id.labsummary);

		Patient patientLIst = PatientsOperations.getPatientDetails(treatmentId,
				getActivity());
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentId,
						getActivity()), getActivity());
//		ArrayList<PatientLabFollowUpModel> patientlabfollowuplist = PatientLabFollowUpOperations
//				.getPatientsLabfollowUp(Schema.PATIENT_LAB_TREATMENTID + "= '"
//						+ treatmentId + "' and " + Schema.PATIENT_LAB_ISDELETED
//						+ "=0", getActivity());
//		
		ArrayList<PatientLabFollowUpModel> patientlabfollowuplist = PatientLabFollowUpOperations
				.getPatientsLabfollowUp(Schema.PATIENT_LAB_TREATMENTID + "= '"
						+ treatmentId + "'", getActivity());
		ArrayList<PatientLabFollowUpModel> finalList = new ArrayList<PatientLabFollowUpModel>();
		for(PatientLabFollowUpModel pl: patientlabfollowuplist)
		{
			boolean isAdd = true;
			for(PatientLabFollowUpModel temp: finalList)
			{
				if(temp.Lab_Month.equals(pl.Lab_Month))
				{
					isAdd = false;
					if(temp.Lab_CreationTome_Stamp>=pl.Lab_CreationTome_Stamp)
					{
						temp=pl;
					}
				}
			}
			
			if(isAdd)
			{
				finalList.add(pl);
			}
		}
		
		patientlabfollowuplist = finalList;
		
		String hivResult = HIVOperation.getResult(treatmentId, getActivity());

		// add header

		try {
			TableRow idRow = new TableRow(getActivity());
			idRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			idRow.setId(150);

			idRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			TextView id = new TextView(getActivity());
			id.setText(getResources().getString(R.string.treatmentId)
					+ treatmentId);
			id.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.catone_gradient_bg));
			id.setTextSize(20);
			id.setTypeface(null, Typeface.BOLD);
			id.setGravity(Gravity.CENTER);
			idRow.addView(id);
			headerTableLayout.addView(idRow, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		} catch (Exception e) {

		}
		try {
			name.setText("Name: "
					+ GenUtils.makeFirstLetterUpperCase(patientLIst.name));
			phoneNumber.setText("Phone: " + patientLIst.phoneNumber);
			status.setText(patientLIst.Status);
			if (patientLIst.tbNumber != null)
				tbNumber.setText(patientLIst.tbNumber);
			else
				tbNumber.setText("N/A");
			if (patientLIst.nikshayId != null)
				Nikshayid.setText(patientLIst.nikshayId);
			else
				Nikshayid.setText("N/A");
			if (patientLIst.patientType != null)
				patientType.setText(patientLIst.patientType);
			else
				patientType.setText("N/A");
			if (hivResult.equals("")) {
				hivStatus.setText("N/A");
			} else {
				hivStatus.setText(hivResult);
			}
			if (regimen.catagory != null) {
				category.setText(regimen.catagory);
			} else
				category.setText("N/A");
			if (regimen.stage != null) {
				stage.setText(regimen.stage);
			} else
				stage.setText("N/A");
			if (regimen.schedule != null) {
				schedule.setText(regimen.schedule);
			} else
				schedule.setText("N/A");

			if (patientlabfollowuplist.size() > 0) {
				TableRow tr = new TableRow(getActivity());
				tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				tr.setId(100);

				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				TextView month = new TextView(getActivity());
				month.setId(1);
				month.setText("Month");
				month.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.add_contact_layout_background));
				month.setTextSize(18);
				month.setTypeface(null, Typeface.BOLD);
				month.setPadding(2, 10, 2, 10);
				month.setGravity(Gravity.CENTER);
				tr.addView(month);

				TextView date = new TextView(getActivity());
				date.setId(2);
				date.setText("Date");
				date.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.add_contact_layout_background));
				date.setTextSize(18);
				date.setTypeface(null, Typeface.BOLD);
				date.setPadding(2, 10, 2, 10);
				date.setGravity(Gravity.CENTER);
				tr.addView(date);

				TextView dmc = new TextView(getActivity());
				dmc.setId(3);
				dmc.setText("DMC");
				dmc.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.add_contact_layout_background));
				dmc.setTextSize(18);
				dmc.setPadding(2, 10, 2, 10);
				dmc.setTypeface(null, Typeface.BOLD);
				dmc.setGravity(Gravity.CENTER);
				tr.addView(dmc);

				TextView labno = new TextView(getActivity());
				labno.setId(4);
				labno.setText("Lab No");
				labno.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.add_contact_layout_background));
				labno.setTextSize(18);
				labno.setTypeface(null, Typeface.BOLD);
				labno.setPadding(2, 10, 2, 10);
				labno.setGravity(Gravity.CENTER);
				tr.addView(labno);

				TextView result = new TextView(getActivity());
				result.setId(5);
				result.setText("Smear Result");
				result.setTypeface(null, Typeface.BOLD);
				result.setGravity(Gravity.CENTER);
				result.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.add_contact_layout_background));
				result.setTextSize(18);
				result.setPadding(2, 10, 2, 10);
				tr.addView(result);

				TextView weight = new TextView(getActivity());
				weight.setId(6);
				weight.setText("Patient Weight");
				weight.setTypeface(null, Typeface.BOLD);
				weight.setGravity(Gravity.CENTER);
				weight.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.add_contact_layout_background));
				weight.setTextSize(18);
				weight.setPadding(2, 10, 2, 10);
				tr.addView(weight);

				tablelayout.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				for (int i = 0; i < patientlabfollowuplist.size(); i++) {

					try {
						TableRow tablerow = new TableRow(getActivity());
						tablerow.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						tablerow.setId(100 + i);

						tablerow.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));

						TextView labMonth = new TextView(getActivity());
						labMonth.setId(200 + i);
						labMonth.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
						labMonth.setText(patientlabfollowuplist.get(i).Lab_Month);
						labMonth.setTextSize(18);
						labMonth.setGravity(Gravity.CENTER);
						labMonth.setPadding(2, 10, 2, 10);
						labMonth.setBackgroundDrawable(getResources()
								.getDrawable(
										R.drawable.add_contact_layout_background));
						tablerow.addView(labMonth);

						TextView labDate = new TextView(getActivity());
						labDate.setId(300 + i);
						labDate.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
						labDate.setText(GenUtils
								.longtotring(patientlabfollowuplist.get(i).Lab_Date));
						labDate.setTextSize(18);
						labDate.setPadding(2, 10, 2, 10);
						labDate.setGravity(Gravity.CENTER);
						labDate.setBackgroundDrawable(getResources()
								.getDrawable(
										R.drawable.add_contact_layout_background));
						tablerow.addView(labDate);

						TextView labDmc = new TextView(getActivity());
						labDmc.setId(400 + i);
						labDmc.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
						labDmc.setText(GenUtils
								.makeFirstLetterUpperCase(patientlabfollowuplist
										.get(i).Lab_Dmc));
						labDmc.setTextSize(18);
						labDmc.setPadding(2, 10, 2, 10);
						labDmc.setGravity(Gravity.CENTER);
						labDmc.setBackgroundDrawable(getResources()
								.getDrawable(
										R.drawable.add_contact_layout_background));
						tablerow.addView(labDmc);

						TextView labNo = new TextView(getActivity());
						labNo.setId(500 + i);
						labNo.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
						labNo.setText(patientlabfollowuplist.get(i).Lab_No);
						labNo.setTextSize(18);
						labNo.setPadding(2, 10, 2, 10);
						labNo.setGravity(Gravity.CENTER);
						labNo.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.add_contact_layout_background));
						tablerow.addView(labNo);

						TextView labResult = new TextView(getActivity());
						labResult.setId(600 + i);
						labResult.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
						labResult
								.setText(patientlabfollowuplist.get(i).Lab_Result);
						labResult.setTextSize(18);
						labResult.setGravity(Gravity.CENTER);
						labResult.setPadding(2, 10, 2, 10);
						labResult
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.add_contact_layout_background));
						tablerow.addView(labResult);

						TextView labweight = new TextView(getActivity());
						labweight.setId(700 + i);
						labweight.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
						labweight
								.setText(patientlabfollowuplist.get(i).Lab_Weight);
						labweight.setTextSize(18);
						labweight.setGravity(Gravity.CENTER);
						labweight.setPadding(2, 10, 2, 10);
						labweight
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.add_contact_layout_background));
						tablerow.addView(labweight);

						tablelayout.addView(tablerow,
								new TableLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT));
					} catch (Exception e) {

					}
				}
			} else {
				tablelayout.setVisibility(View.GONE);
				labsummary.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			Log.e("error----", e.toString());

		}

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}
}
