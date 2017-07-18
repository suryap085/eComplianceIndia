/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.opasha.eCompliance.ecompliance.DbOperations.QualityAuditingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.QualityAuditingSummaryOperations;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List_Obj;
import org.opasha.eCompliance.ecompliance.util.Enums.Signal;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.QaSummaryReportObj;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class QualityAuditSummaryReport extends Activity {
	String QA_ID;
	EditText boxes, cards, tbNo, died, transferOut, treatmentComplete, cured,
			defaults, failure, switchToMdr, closingBalanceLastEditTxt,
			newPatientEditTxt, sum_comments_editTxt;
	TextView totalOutcomeTextView, closingBalanceTextView, closingBalanceLabel,
			boxesLabel, cardsLabel, tbNoLabel, closingBalanceLastTxtView,
			newPatientTxtView, totalOutcomeLabelTextView, sum_comments_label;
	QaSummaryReportObj qaSummaryObj;
	ProgressDialog pd;
	Button cancel, next;
	ArrayList<Master_QA_Question_List> qlist = new ArrayList<Master_QA_Question_List>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qa_summary_report);

		Bundle bundle = getIntent().getExtras();
		try {
			QA_ID = bundle.getString(IntentKeys.key_treatment_id);
			qlist = new Gson().fromJson(
					new String(bundle
							.getString(IntentKeys.key_qa_answer_third_page)),
					Master_QA_Question_List_Obj.class).list;
		} catch (Exception e) {
			goHome();
		}
		qaSummaryObj = new QaSummaryReportObj();
		cancel = (Button) findViewById(R.id.cancel);
		next = (Button) findViewById(R.id.next);
		next.setText(getResources().getString(R.string.done));
		cancel.setText(getResources().getString(R.string.Back));
		closingBalanceLastTxtView = (TextView) findViewById(R.id.sum_id_closingOf);
		closingBalanceLastEditTxt = (EditText) findViewById(R.id.sum_edit_closingof);
		sum_comments_editTxt = (EditText) findViewById(R.id.sum_comments_editTxt);
		sum_comments_label = (TextView) findViewById(R.id.sum_comments_label);
		newPatientTxtView = (TextView) findViewById(R.id.sum_id_newPatientin);
		newPatientEditTxt = (EditText) findViewById(R.id.sum_new_patientIn);
		totalOutcomeLabelTextView = (TextView) findViewById(R.id.sum_outcomeIn);
		totalOutcomeTextView = (TextView) findViewById(R.id.sum_outcomeInTxtView);
		died = (EditText) findViewById(R.id.sum_died_editTxt);
		transferOut = (EditText) findViewById(R.id.sum_tran_editTxt);
		treatmentComplete = (EditText) findViewById(R.id.sum_trecom_editTxt);
		cured = (EditText) findViewById(R.id.sum_cured_editTxt);
		defaults = (EditText) findViewById(R.id.sum_default_editTxt);
		failure = (EditText) findViewById(R.id.sum_failure_editTxt);
		switchToMdr = (EditText) findViewById(R.id.sum_switchtoMdr_editTxt);
		closingBalanceLabel = (TextView) findViewById(R.id.sum_closing_balaneOf);
		closingBalanceTextView = (TextView) findViewById(R.id.sum_closingBananceOf_textView);
		boxes = (EditText) findViewById(R.id.sum_boxes_editTxt);
		cards = (EditText) findViewById(R.id.sum_card_editTxt);
		tbNo = (EditText) findViewById(R.id.sum_tbNo_editTxt);
		boxesLabel = (TextView) findViewById(R.id.sum_boxes_label);
		cardsLabel = (TextView) findViewById(R.id.sum_card_label);
		tbNoLabel = (TextView) findViewById(R.id.sum_tbNo_label);
		try {
			qaSummaryObj = new Gson().fromJson(new String(getIntent()
					.getExtras().getString(IntentKeys.key_qa_summary_obj)),
					QaSummaryReportObj.class);
		} catch (Exception e) {
			qaSummaryObj = QualityAuditingSummaryOperations.getPreviousAnswer(
					QA_ID, this);
		}
		if (qaSummaryObj.closingLastMnt != 0)
			closingBalanceLastEditTxt.setText(String
					.valueOf(qaSummaryObj.closingLastMnt));
		try {
			sum_comments_editTxt.setText(qaSummaryObj.Comments);
		} catch (Exception e) {

		}
		if (qaSummaryObj.newPatients != 0) {
			newPatientEditTxt.setText(String.valueOf(qaSummaryObj.newPatients));
			cardsLabel.setText("Cards out of " + qaSummaryObj.newPatients);
			tbNoLabel.setText("TB No. out of " + qaSummaryObj.newPatients);
			boxesLabel.setText("Boxes out of " + qaSummaryObj.newPatients);
		}
		if (qaSummaryObj.totalOutcome != 0)
			totalOutcomeTextView.setText(String
					.valueOf(qaSummaryObj.totalOutcome));
		if (qaSummaryObj.dieds != 0)
			died.setText(String.valueOf(qaSummaryObj.dieds));
		if (qaSummaryObj.cur != 0)
			cured.setText(String.valueOf(qaSummaryObj.cur));
		if (qaSummaryObj.tc != 0)
			treatmentComplete.setText(String.valueOf(qaSummaryObj.tc));
		if (qaSummaryObj.failures != 0)
			failure.setText(String.valueOf(qaSummaryObj.failures));
		if (qaSummaryObj.defaultNo != 0)
			defaults.setText(String.valueOf(qaSummaryObj.defaultNo));
		if (qaSummaryObj.tranOut != 0)
			transferOut.setText(String.valueOf(qaSummaryObj.tranOut));
		if (qaSummaryObj.stomdr != 0)
			switchToMdr.setText(String.valueOf(qaSummaryObj.stomdr));
		if (qaSummaryObj.closingbalancetill != 0)
			closingBalanceTextView.setText(String
					.valueOf(qaSummaryObj.closingbalancetill));
		if (qaSummaryObj.card != 0)
			cards.setText(String.valueOf(qaSummaryObj.card));
		if (qaSummaryObj.tbNo != 0)
			tbNo.setText(String.valueOf(qaSummaryObj.tbNo));
		if (qaSummaryObj.boxes != 0)
			boxes.setText(String.valueOf(qaSummaryObj.boxes));
		Calendar cal = Calendar.getInstance();
		String currentMonth = new SimpleDateFormat("MMMM-yy").format(cal
				.getTime());
		String currentDate = new SimpleDateFormat("dd-MMM-yy").format(cal
				.getTime());
		cal.add(Calendar.MONTH, -1);
		String lastMonth = new SimpleDateFormat("MMMM-yy")
				.format(cal.getTime());
		closingBalanceLastTxtView.setText("Closing Balance of " + lastMonth);
		newPatientTxtView.setText("New Patients in " + currentMonth);
		closingBalanceLabel.setText("Closing Balance till " + currentDate);
		totalOutcomeLabelTextView.setText("Total outcomes in " + currentMonth);

		newPatientEditTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				try {

				} catch (Exception e) {

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {
					int balance = Integer.parseInt(closingBalanceTextView
							.getText().toString());
					closingBalanceTextView.setText(String.valueOf(balance
							- qaSummaryObj.newPatients));
				} catch (Exception e) {

				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					try {
						qaSummaryObj.newPatients = Integer
								.parseInt(newPatientEditTxt.getText()
										.toString());
					} catch (Exception e) {
						qaSummaryObj.newPatients = 0;
					}
					int balance = Integer.parseInt(closingBalanceTextView
							.getText().toString());
					closingBalanceTextView.setText(String.valueOf(balance
							+ qaSummaryObj.newPatients));
				} catch (Exception e) {
				}
				if (qaSummaryObj.newPatients != 0) {
					cardsLabel.setText("Cards out of "
							+ qaSummaryObj.newPatients);
					tbNoLabel.setText("TB No. out of "
							+ qaSummaryObj.newPatients);
					boxesLabel.setText("Boxes out of "
							+ qaSummaryObj.newPatients);
				} else {
					cardsLabel.setText("Cards");
					tbNoLabel.setText("TB No.");
					boxesLabel.setText("Boxes");
				}

			}
		});
		closingBalanceLastEditTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {
					int balance = Integer.parseInt(closingBalanceTextView
							.getText().toString());
					closingBalanceTextView.setText(String.valueOf(balance
							- qaSummaryObj.closingLastMnt));
				} catch (Exception e) {
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					try {
						qaSummaryObj.closingLastMnt = Integer
								.parseInt(closingBalanceLastEditTxt.getText()
										.toString());
					} catch (Exception e) {
						qaSummaryObj.closingLastMnt = 0;
					}

					int balance = Integer.parseInt(closingBalanceTextView
							.getText().toString());
					closingBalanceTextView.setText(String.valueOf(balance
							+ qaSummaryObj.closingLastMnt));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		died.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				try {
					qaSummaryObj.dieds = Integer.parseInt(died.getText()
							.toString());
				} catch (Exception e) {
					qaSummaryObj.dieds = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.dieds);

			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.dieds = Integer.parseInt(died.getText()
							.toString());
				} catch (Exception e) {
					qaSummaryObj.dieds = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.dieds);

			}
		});
		cured.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				try {
					qaSummaryObj.cur = Integer.parseInt(cured.getText()
							.toString());
				} catch (Exception e) {
					qaSummaryObj.cur = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.cur);

			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.cur = Integer.parseInt(cured.getText()
							.toString());
				} catch (Exception e) {
					qaSummaryObj.cur = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.cur);

			}
		});
		treatmentComplete.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {

					qaSummaryObj.tc = Integer.parseInt(treatmentComplete
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.tc = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.tc);
			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.tc = Integer.parseInt(treatmentComplete
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.tc = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.tc);

			}
		});
		defaults.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {

					qaSummaryObj.defaultNo = Integer.parseInt(defaults
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.defaultNo = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.defaultNo);
			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.defaultNo = Integer.parseInt(defaults
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.defaultNo = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.defaultNo);

			}
		});
		transferOut.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {

					qaSummaryObj.tranOut = Integer.parseInt(transferOut
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.tranOut = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.tranOut);
			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.tranOut = Integer.parseInt(transferOut
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.tranOut = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.tranOut);

			}
		});
		failure.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {

					qaSummaryObj.failures = Integer.parseInt(failure.getText()
							.toString());
				} catch (Exception e) {
					qaSummaryObj.failures = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.failures);
			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.failures = Integer.parseInt(failure.getText()
							.toString());
				} catch (Exception e) {
					qaSummaryObj.failures = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.failures);

			}
		});
		switchToMdr.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {

					qaSummaryObj.stomdr = Integer.parseInt(switchToMdr
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.stomdr = 0;
				}
				updateOutcomesBeforeTextChange(qaSummaryObj.stomdr);
			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					qaSummaryObj.stomdr = Integer.parseInt(switchToMdr
							.getText().toString());
				} catch (Exception e) {
					qaSummaryObj.stomdr = 0;
				}
				updateOutcomesafterTextChange(qaSummaryObj.stomdr);
			}
		});
	}

	private void updateOutcomesafterTextChange(int value) {
		int balance = Integer.parseInt(closingBalanceTextView.getText()
				.toString());
		int balance2 = Integer.parseInt(totalOutcomeTextView.getText()
				.toString());
		closingBalanceTextView.setText(String.valueOf(balance - value));
		totalOutcomeTextView.setText(String.valueOf(balance2 + value));
	}

	private void updateOutcomesBeforeTextChange(int value) {
		int balance = Integer.parseInt(closingBalanceTextView.getText()
				.toString());
		int balance2 = Integer.parseInt(totalOutcomeTextView.getText()
				.toString());
		closingBalanceTextView.setText(String.valueOf(balance + value));
		totalOutcomeTextView.setText(String.valueOf(balance2 - value));
	}

	public void OnClickCancel(View v) {
		goBack();
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	private void goHome() {
		startActivity(new Intent(this, HomeActivity.class));
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void goBack() {
		Intent intent = new Intent(this, AuditQuestionsActivity.class);
		intent.putExtra(IntentKeys.key_treatment_id, QA_ID);
		Master_QA_Question_List_Obj obj = new Master_QA_Question_List_Obj();
		obj.list = qlist;
		qaSummaryObj.closingbalancetill = Integer
				.parseInt(closingBalanceTextView.getText().toString());
		qaSummaryObj.totalOutcome = Integer.parseInt(totalOutcomeTextView
				.getText().toString());
		try {
			qaSummaryObj.card = Integer.parseInt(cards.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			qaSummaryObj.Comments = sum_comments_editTxt.getText().toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			qaSummaryObj.tbNo = Integer.parseInt(tbNo.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			qaSummaryObj.boxes = Integer.parseInt(boxes.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		intent.putExtra(IntentKeys.key_qa_summary_obj,
				new Gson().toJson(qaSummaryObj));
		intent.putExtra(IntentKeys.key_qa_answer_third_page,
				new Gson().toJson(obj));
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

	}

	public void onClickNext(View v) {
		cancel.setEnabled(false);
		next.setEnabled(false);
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			String error = "";

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(QualityAuditSummaryReport.this);
				pd.setMessage(getResources().getString(R.string.verifying));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				if (cards.getText().toString().isEmpty()) {
					error = "Enter No. of Card";
					return null;
				}
				if (boxes.getText().toString().isEmpty()) {
					error = "Enter no boxes";
					return null;
				}
				if (tbNo.getText().toString().isEmpty()) {
					error = "Enter no of Tb No.";
					return null;
				}
				try {
					if (Integer.parseInt(cards.getText().toString()) > qaSummaryObj.newPatients) {
						error = "Enter Correct No. of Card";
						return null;
					}
					if (Integer.parseInt(boxes.getText().toString()) > qaSummaryObj.newPatients) {
						error = "Enter Correct No. of Boxes";
						return null;
					}
					if (Integer.parseInt(tbNo.getText().toString()) > qaSummaryObj.newPatients) {
						error = "Enter Correct No. of TB No.";
						return null;
					}

				} catch (Exception e) {

				}

				long creationTime = System.currentTimeMillis();
				for (Master_QA_Question_List list : qlist) {
					try {
						QualityAuditingOperations.AddQuestions(
								list.MASTER_AUDIT_QUESTION_ID, false,
								creationTime, false, QA_ID,
								list.MASTER_AUDIT_QUESTION_USER_TEXT_Value,
								QualityAuditSummaryReport.this);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				qaSummaryObj.closingbalancetill = Integer
						.parseInt(closingBalanceTextView.getText().toString());
				qaSummaryObj.totalOutcome = Integer
						.parseInt(totalOutcomeTextView.getText().toString());
				try {
					qaSummaryObj.card = Integer.parseInt(cards.getText()
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					qaSummaryObj.tbNo = Integer.parseInt(tbNo.getText()
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					qaSummaryObj.boxes = Integer.parseInt(boxes.getText()
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					qaSummaryObj.Comments = sum_comments_editTxt.getText()
							.toString();
				} catch (Exception e) {

				}
				QualityAuditingSummaryOperations.AddQuestions(1,
						closingBalanceLastTxtView.getText().toString(),
						creationTime, false, false, QA_ID,
						qaSummaryObj.closingLastMnt,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(2,
						newPatientTxtView.getText().toString(), creationTime,
						false, false, QA_ID, qaSummaryObj.newPatients,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(3,
						totalOutcomeLabelTextView.getText().toString(),
						creationTime, false, false, QA_ID,
						qaSummaryObj.totalOutcome,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(4, "Died",
						creationTime, false, false, QA_ID, qaSummaryObj.dieds,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(5, "Cured",
						creationTime, false, false, QA_ID, qaSummaryObj.cur,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(6,
						"Treatment Complete", creationTime, false, false,
						QA_ID, qaSummaryObj.tc, QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(7, "Default",
						creationTime, false, false, QA_ID,
						qaSummaryObj.defaultNo, QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(8, "Tranfer Out",
						creationTime, false, false, QA_ID,
						qaSummaryObj.tranOut, QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(9, "Failure",
						creationTime, false, false, QA_ID,
						qaSummaryObj.failures, QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(10,
						"Switched to MDR", creationTime, false, false, QA_ID,
						qaSummaryObj.stomdr, QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(11,
						closingBalanceLabel.getText().toString(), creationTime,
						false, false, QA_ID, qaSummaryObj.closingbalancetill,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(12, cardsLabel
						.getText().toString(), creationTime, false, false,
						QA_ID, qaSummaryObj.card,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(13, boxesLabel
						.getText().toString(), creationTime, false, false,
						QA_ID, qaSummaryObj.boxes,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(14, tbNoLabel
						.getText().toString(), creationTime, false, false,
						QA_ID, qaSummaryObj.tbNo,
						QualityAuditSummaryReport.this);
				QualityAuditingSummaryOperations.AddQuestions(15,
						sum_comments_label.getText().toString(), creationTime,
						false, false, QA_ID, qaSummaryObj.Comments,
						QualityAuditSummaryReport.this);
				return null;

			}

			@Override
			protected void onPostExecute(Void result) {
				if (pd != null) {
					pd.dismiss();
					cancel.setEnabled(true);
					next.setEnabled(true);
					if (error.isEmpty()) {

						Intent intent = new Intent(
								QualityAuditSummaryReport.this,
								HomeActivity.class);
						intent.putExtra(
								IntentKeys.key_message_home,
								getResources()
										.getString(
												R.string.qauaityAuditCompleteFirstStage));
						intent.putExtra(IntentKeys.key_signal_type,
								Signal.Good.toString());
						startActivity(intent);
						QualityAuditSummaryReport.this.finish();
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

}
