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

import org.opasha.eCompliance.ecompliance.Adapters.AuditQuestionListAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Master_QA_Questions;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List_Obj;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;

public class AuditQuestionsActivity extends Activity {
	LinearLayout Qlist;
	String QA_ID, center;
	int fillter = 2;
	Button cancel, next;
	ProgressDialog pd;
	ArrayList<Master_QA_Question_List> qlist = new ArrayList<Master_QA_Question_List>();
	AuditQuestionListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audit_question_list);
		cancel = (Button) findViewById(R.id.cancel);
		next = (Button) findViewById(R.id.next);
		Qlist = (LinearLayout) findViewById(R.id.Qlist);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			QA_ID = bundle.getString(IntentKeys.key_treatment_id);
		}
		try {
			qlist = new Gson().fromJson(
					new String(bundle
							.getString(IntentKeys.key_qa_answer_third_page)),
					Master_QA_Question_List_Obj.class).list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (qlist.size() == 0 || qlist == null) {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_mobile_machine, this).equals("1")) {
				// if machine is mobile counselor then user type =1,question
				// type =2 for text box
				qlist = Master_QA_Questions.getViewList(
						QA_ID,
						Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE
								+ "= 1 and "
								+ Schema.MASTER_AUDIT_QUESTION_USER_TYPE
								+ " =1 and ("
								+ Schema.MASTER_AUDIT_QUESTION_TYPE
								+ "=2 or "
								+ Schema.MASTER_AUDIT_QUESTION_TYPE
								+ "=3) and "
								+ Schema.MASTER_AUDIT_QUESTION_LANGUAGE
								+ " ='"
								+ getResources().getConfiguration().locale
										.getLanguage() + "'", this);
			} else {
				// if machine is not mobile counselor then user type =2,question
				// type =2 for text box
				qlist = Master_QA_Questions.getViewList(
						QA_ID,
						Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE
								+ "= 1 and "
								+ Schema.MASTER_AUDIT_QUESTION_USER_TYPE
								+ " =2 and ("
								+ Schema.MASTER_AUDIT_QUESTION_TYPE
								+ "=2 or "
								+ Schema.MASTER_AUDIT_QUESTION_TYPE
								+ "=3) and "
								+ Schema.MASTER_AUDIT_QUESTION_LANGUAGE
								+ " ='"
								+ getResources().getConfiguration().locale
										.getLanguage() + "'", this);
			}
		}
		for (int i = 0; i < qlist.size(); i++) {
			Master_QA_Question_List obj = qlist.get(i);
			Qlist.addView(AuditQuestionListAdapter.getView(obj, i, this));
		}

	}

	public void onClickNext(View v) {
		cancel.setEnabled(false);
		next.setEnabled(false);
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			String error = "";

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(AuditQuestionsActivity.this);
				pd.setMessage(getResources().getString(R.string.verifying));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				int count = Qlist.getChildCount();
				for (int i = 0; i < count; i++) {
					LinearLayout a = (LinearLayout) Qlist.getChildAt(i);
					if (qlist.get(i).MASTER_AUDIT_QUESTION_TYPE == 2) {

						try {
							if (((Spinner) a.findViewById(R.id.questionspn))
									.getSelectedItemPosition() == 0) {
								error = "Give answer of question no. "
										+ String.valueOf(i + 1);
								return null;
							}
							qlist.get(i).MASTER_AUDIT_QUESTION_USER_TEXT_Value = ((Spinner) a
									.findViewById(R.id.questionspn))
									.getSelectedItem().toString();

						} catch (Exception e) {
						}
					} else if (qlist.get(i).MASTER_AUDIT_QUESTION_TYPE == 3) {
						try {
							qlist.get(i).MASTER_AUDIT_QUESTION_USER_TEXT_Value = ((EditText) a
									.findViewById(R.id.enterdetails)).getText()
									.toString();
						} catch (Exception e) {
						}
					}
				}
				// long creationTime = System.currentTimeMillis();
				// for (Master_QA_Question_List list : qlist) {
				// try {
				// QualityAuditingOperations.AddQuestions(
				// list.MASTER_AUDIT_QUESTION_ID, false,
				// creationTime, false, QA_ID,
				// list.MASTER_AUDIT_QUESTION_USER_TEXT_Value,
				// AuditQuestionsActivity.this);
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// }
				return null;

			}

			@Override
			protected void onPostExecute(Void result) {
				if (pd != null) {
					pd.dismiss();
					cancel.setEnabled(true);
					next.setEnabled(true);
					if (error.isEmpty()) {
						// Intent intent = new
						// Intent(AuditQuestionsActivity.this,
						// HomeActivity.class);
						// intent.putExtra(
						// IntentKeys.key_message_home,
						// getResources()
						// .getString(
						// R.string.qauaityAuditCompleteFirstStage));
						// intent.putExtra(IntentKeys.key_signal_type,
						// Signal.Good.toString());
						// startActivity(intent);
						// AuditQuestionsActivity.this.finish();
						// overridePendingTransition(R.anim.right_side_in,
						// R.anim.right_side_out);
						Intent intent = new Intent(AuditQuestionsActivity.this,
								QualityAuditSummaryReport.class);
						Master_QA_Question_List_Obj obj = new Master_QA_Question_List_Obj();
						obj.list = qlist;
						intent.putExtra(IntentKeys.key_treatment_id, QA_ID);
						try {
							intent.putExtra(
									IntentKeys.key_qa_summary_obj,
									getIntent().getExtras().getString(
											IntentKeys.key_qa_summary_obj));
						} catch (Exception e) {
							// TODO: handle exception
						}
						intent.putExtra(IntentKeys.key_qa_answer_third_page,
								new Gson().toJson(obj));
						startActivity(intent);
						AuditQuestionsActivity.this.finish();
						overridePendingTransition(R.anim.left_side_out,
								R.anim.left_side_in);

					} else {
						showerroMessage(error);
					}
				}
			}
		};
		task.execute((Void[]) null);

	}

	private void showerroMessage(String error2) {
		// Create the alert box
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

		// Set the message to display
		alertbox.setMessage(error2);

		// Add a neutral button to the alert box and assign a click listener
		alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

			// Click listener on the neutral button of alert box
			public void onClick(DialogInterface arg0, int arg1) {
				// The neutral button was clicked
			}
		});

		// show the alert box
		alertbox.show();
	}

	public void OnClickCancel(View v) {
		goquestionTwo();
	}

	@Override
	public void onBackPressed() {
		goquestionTwo();
	}

	private void goquestionTwo() {
		Intent intent = new Intent(AuditQuestionsActivity.this,
				HomeActivity.class);
		intent.putExtra(
				IntentKeys.key_message_home,
				getResources().getString(
						R.string.qualityAuditCanceled));
		intent.putExtra(IntentKeys.key_signal_type, Signal.Bad.toString());
		startActivity(intent);
		AuditQuestionsActivity.this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}
}
