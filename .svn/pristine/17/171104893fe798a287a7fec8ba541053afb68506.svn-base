/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Adapters;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AuditQuestionListAdapter {

	private static LayoutInflater inflater = null;

	public static View getView(Master_QA_Question_List list, int position,
			Context activity) {

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView questions;
		final Spinner questionspn;
		TextView Qid;
		final EditText enterdetails;
		View vi = inflater.inflate(R.layout.question_row_view_type, null);
		Qid = (TextView) vi.findViewById(R.id.qId);
		Qid.setText(String.valueOf(position + 1) + ".");
		if (list.MASTER_AUDIT_QUESTION_TYPE == 2) {
			((LinearLayout) vi.findViewById(R.id.layoutquestionWithText))
					.setVisibility(View.GONE);
			((LinearLayout) vi.findViewById(R.id.layoutQuestionSpinnerSpn))
					.setVisibility(View.VISIBLE);
			((LinearLayout) vi.findViewById(R.id.layoutQuestionSpinnerText))
					.setVisibility(View.VISIBLE);

			questionspn = (Spinner) vi.findViewById(R.id.questionspn);
			questions = (TextView) vi.findViewById(R.id.questions);
			questions.setText(list.MASTER_AUDIT_QUESTION_NAME);

			ArrayAdapter<String> labAdapter = new ArrayAdapter<String>(
					activity, R.layout.lab_spinner_row,
					list.MASTER_AUDIT_QUESTION_USER_SPINNER_OPTION);
			questionspn.setAdapter(labAdapter);
			questionspn.setTag(position);
			questionspn.setSelection(0);
			for (int i = 0; i < questionspn.getCount(); i++) {
				if (questionspn.getItemAtPosition(i).toString()
						.equals(list.MASTER_AUDIT_QUESTION_USER_TEXT_Value)) {
					questionspn.setSelection(i);
					break;
				}
			}
		} else if (list.MASTER_AUDIT_QUESTION_TYPE == 3) {
			((LinearLayout) vi.findViewById(R.id.layoutQuestionSpinnerSpn))
					.setVisibility(View.GONE);
			((LinearLayout) vi.findViewById(R.id.layoutQuestionSpinnerText))
					.setVisibility(View.GONE);
			((LinearLayout) vi.findViewById(R.id.layoutquestionWithText))
					.setVisibility(View.VISIBLE);
			questions = (TextView) vi.findViewById(R.id.questionsForText);
			enterdetails = (EditText) vi.findViewById(R.id.enterdetails);
			enterdetails.setTag(position);

			questions.setText(list.MASTER_AUDIT_QUESTION_NAME);

			enterdetails.setText(list.MASTER_AUDIT_QUESTION_USER_TEXT_Value);

		}
		if (position != 0) {
			if (position % 2 == 1) {

				vi.setBackgroundColor(activity.getResources().getColor(
						R.color.qaListOne));
			} else {
				vi.setBackgroundColor(activity.getResources().getColor(
						R.color.qaListTwo));

				return vi;
			}
		} else {
			vi.setBackgroundColor(activity.getResources().getColor(
					R.color.qaListTwo));
		}
		return vi;

	}
}
