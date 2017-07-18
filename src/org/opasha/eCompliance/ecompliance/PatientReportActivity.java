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
import java.util.ConcurrentModificationException;
import java.util.List;

import org.opasha.eCompliance.ecompliance.Adapters.PatientListAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Dose;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IntentFrom;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class PatientReportActivity extends FragmentActivity {
	static Enums.ReportType repType;
	static Enums.IntentFrom repFrom;
	public static String query = "";

	static List<Patient> entries = new ArrayList<Patient>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		// to give support on lower android version, we are not calling
		// getFragmentManager()

		FragmentManager fm = getFragmentManager();
		entries.clear();
		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			DataListFragment list = new DataListFragment();
			repType = (ReportType) getIntent().getExtras().get(
					IntentKeys.key_report_type);
			repFrom = (IntentFrom) getIntent().getExtras().get(
					IntentKeys.key_intent_from);
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	public class DataListFragment extends ListFragment implements
			LoaderManager.LoaderCallbacks<List<Patient>>, OnQueryTextListener {

		PatientListAdapter mAdapter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			System.out.println("DataListFragment.onActivityCreated");

			// Initially there is no data
			setEmptyText(getResources().getString(R.string.noDataHere));
			// Create an empty adapter we will use to display the loaded data.
			mAdapter = new PatientListAdapter(getActivity());
			setListAdapter(mAdapter);
			setHasOptionsMenu(true);
			// Start out with a progress indicator.
			setListShown(false);
			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.
			inflater.inflate(R.menu.searchview_in_menu, menu);
			MenuItem item = menu.findItem(R.id.action_search);
			SearchView sv = (SearchView) item.getActionView();
			sv.setOnQueryTextListener(this);
			item.setActionView(sv);
		}

		@Override
		public void onListItemClick(final ListView l, View v, int position,
				long id) {
			// Insert desired behavior here.
			TextView txtContactid = ((TextView) v.findViewById(R.id.UniqueId));
			Intent intent = null;
			final String contactId = (String) txtContactid.getText();
			// Set pop-up if report is PatientsFromLegacySystem report before
			// redirecting to enroll page.
			if (repType.equals(Enums.ReportType.PatientsFromLegacySystem)) {

//				if (ConfigurationOperations.getKeyValue(
//						ConfigurationKeys.key_Admin_Login_Required,
//						l.getContext()).equals("1")) {
//					if (((eComplianceApp) l.getContext()
//							.getApplicationContext()).visitorType != VisitorType.PM) {
//						final Dialog dialog = new Dialog(l.getContext());
//						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//						dialog.setContentView(R.layout.custom_dialog_box_yes_no);
//						Button noButton = (Button) dialog
//								.findViewById(R.id.dialogButtonCancel);
//						Button yesButton = (Button) dialog
//								.findViewById(R.id.dialogButtonYes);
//						TextView message = (TextView) dialog
//								.findViewById(R.id.messageText);
//						TextView title = (TextView) dialog
//								.findViewById(R.id.text);
//						message.setText(getResources().getString(
//								R.string.pmLoginRequiredFull));
//						title.setText(getResources().getString(
//								R.string.updateScan));
//						noButton.setVisibility(View.GONE);
//						yesButton.setGravity(Gravity.CENTER);
//						yesButton
//								.setText(getResources().getString(R.string.ok));
//						yesButton.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//
//							}
//						});
//						dialog.show();
//					} else {
//						final Dialog dialog = new Dialog(l.getContext());
//						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//						dialog.setContentView(R.layout.custom_dialog_box_yes_no);
//						// set the custom dialog components - text, image and
//						// button
//						Button noButton = (Button) dialog
//								.findViewById(R.id.dialogButtonCancel);
//						Button yesButton = (Button) dialog
//								.findViewById(R.id.dialogButtonYes);
//						TextView message = (TextView) dialog
//								.findViewById(R.id.messageText);
//						TextView title = (TextView) dialog
//								.findViewById(R.id.text);
//						// Set String as Html to bold text of first line
//						String mess = "<p><b>"
//								+ ((TextView) v.findViewById(R.id.name))
//										.getText().toString()
//								+ "</b> <br>"
//								+ getResources().getString(
//										R.string.doYouWantToContinue) + "</p>";
//						message.setText(Html.fromHtml(mess));
//						title.setText(getResources().getString(
//								R.string.verifyId));
//						yesButton.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//								entries.clear();
//								PatientReportActivity.this.finish();
//								Intent i = new Intent(
//										PatientReportActivity.this,
//										RecheckIdActivity.class);
//								i.putExtra(IntentKeys.key_visitor_type,
//										Enums.VisitorType.Patient.toString());
//								i.putExtra(IntentKeys.key_treatment_id,
//										contactId);
//								i.putExtra(IntentKeys.key_intent_from,
//										ReportType.PatientsFromLegacySystem);
//								query = "";
//								startActivity(i);
//								overridePendingTransition(R.anim.right_side_in,
//										R.anim.right_side_out);
//							}
//						});
//						// if button is clicked, close the custom dialog
//						noButton.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//								return;
//							}
//						});
//						dialog.show();
//					}
//
//				}
//				else
//				{
					final Dialog dialog = new Dialog(l.getContext());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.custom_dialog_box_yes_no);
					// set the custom dialog components - text, image and
					// button
					Button noButton = (Button) dialog
							.findViewById(R.id.dialogButtonCancel);
					Button yesButton = (Button) dialog
							.findViewById(R.id.dialogButtonYes);
					TextView message = (TextView) dialog
							.findViewById(R.id.messageText);
					TextView title = (TextView) dialog
							.findViewById(R.id.text);
					// Set String as Html to bold text of first line
					String mess = "<p><b>"
							+ ((TextView) v.findViewById(R.id.name))
									.getText().toString()
							+ "</b> <br>"
							+ getResources().getString(
									R.string.doYouWantToContinue) + "</p>";
					message.setText(Html.fromHtml(mess));
					title.setText(getResources().getString(
							R.string.verifyId));
					yesButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							entries.clear();
							PatientReportActivity.this.finish();
							Intent i = new Intent(
									PatientReportActivity.this,
									RecheckIdActivity.class);
							i.putExtra(IntentKeys.key_visitor_type,
									Enums.VisitorType.Patient.toString());
							i.putExtra(IntentKeys.key_treatment_id,
									contactId);
							i.putExtra(IntentKeys.key_intent_from,
									ReportType.PatientsFromLegacySystem);
							query = "";
							startActivity(i);
							overridePendingTransition(R.anim.right_side_in,
									R.anim.right_side_out);
						}
					});
					// if button is clicked, close the custom dialog
					noButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							return;
						}
					});
					dialog.show();
				//}

			} else {
				//PatientReportActivity.this.finish();
				intent = new Intent(PatientReportActivity.this,
						PatientDetailsActivity.class);
				intent.putExtra(IntentKeys.key_treatment_id, contactId);
				query = "";
				startActivity(intent);
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			}

		}

		@Override
		public Loader<List<Patient>> onCreateLoader(int arg0, Bundle arg1) {
			System.out.println("DataListFragment.onCreateLoader");
			return new DataListLoader(getActivity(), query);
		}

		@Override
		public void onLoadFinished(Loader<List<Patient>> arg0,
				List<Patient> data) {
			mAdapter.setData(data);
			setTitle("Total: " + entries.size());
			// The list should now be shown.
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Patient>> arg0) {
			mAdapter.setData(null);
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			query = newText;
			getLoaderManager().restartLoader(0, null, this);
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String queries) {
			return false;
		}
	}

	public static class DataListLoader extends AsyncTaskLoader<List<Patient>> {
		List<Patient> mModels;
		Activity a;
		String query;

		public DataListLoader(Context context, String query) {
			super(context);
			a = (Activity) context;
			this.query = query;
		}

		@Override
		public List<Patient> loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			// You should perform the heavy task of getting data from
			// Internet or database or other source
			// Here, we are generating some Sample data

			// Create corresponding array of entries and load with data.
			if (entries.size() != 0) {
				if (!query.equals("")) {
					ArrayList<Patient> retList = new ArrayList<Patient>();
					for (Patient p : entries) {
						if (p.name.toLowerCase().trim()
								.startsWith(query.toLowerCase().trim())
								|| p.treatmentID.toLowerCase().trim()
										.startsWith(query.toLowerCase().trim())) {
							retList.add(p);
						}
					}
					return retList;
				}
				return entries;
			}
			StringBuilder queries = new StringBuilder();
			switch (repType) {
			case InactivePatient:
				queries = new StringBuilder();
				queries.append(Schema.PATIENTS_STATUS
						+ "<>'"
						+ Enums.StatusType.getStatusType(StatusType.Active)
								.toString() + "' and "
						+ Schema.PATIENTS_IS_DELETED + "=0");
				break;
			case AllPatients:
				queries = new StringBuilder();
				queries.append(Schema.PATIENTS_STATUS
						+ "='"
						+ Enums.StatusType.getStatusType(StatusType.Active)
								.toString() + "' and "
						+ Schema.PATIENTS_IS_DELETED + "=0");
				break;
			case MissedPatients:
				for (String s : ((eComplianceApp) ((Activity) a)
						.getApplication()).missedDose) {
					queries.append(Schema.PATIENTS_TREATMENT_ID + " = '" + s
							+ "' or ");
				}
				if (queries.length() > 3) {
					queries.setLength(queries.length() - 3);
				}
				break;
			case VisitedPatients:
				for (String s : ((eComplianceApp) ((Activity) a)
						.getApplication()).visitToday) {
					queries.append(Schema.PATIENTS_TREATMENT_ID + " = '" + s
							+ "' or ");
				}
				if (queries.length() > 3) {
					queries.setLength(queries.length() - 3);
				}
				break;
			case PendingPatients:
				for (String s : ((eComplianceApp) ((Activity) a)
						.getApplication()).pendingDoses) {
					queries.append(Schema.PATIENTS_TREATMENT_ID + " = '" + s
							+ "' or ");
				}
				if (queries.length() > 3) {
					queries.setLength(queries.length() - 3);
				}
				break;

			case hospitalisedPatient:
				for (String s : ((eComplianceApp) ((Activity) a)
						.getApplication()).hospitalised) {
					queries.append(Schema.PATIENTS_TREATMENT_ID + " = '" + s
							+ "' or ");
				}
				if (queries.length() > 3) {
					queries.setLength(queries.length() - 3);
				}
				break;
			default:
				break;
			}
			if (repType.equals(Enums.ReportType.PatientsFromLegacySystem)) {
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_used_device, a).equals("iris")) {
					entries = PatientsOperations
							.getPatientsFromLegacySystemIris(a);
				} else {
					entries = PatientsOperations
							.getPatientsFromLegacySystemFp(a);
				}
			} else {
				entries = PatientsOperations.getPatients(
						Schema.PATIENTS_IS_DELETED + " =0 and ("
								+ queries.toString() + ")", a);
			}

			try {
				for (Patient p : entries) {
					try {
						p.regimenID = TreatmentInStagesOperations
								.getPatientRegimenId(p.treatmentID, a);
						Master regimen = RegimenMasterOperations.getRegimen(
								p.regimenID, a);
						try {
							p.category = regimen.catagory;
							p.schedule = regimen.schedule;
							p.activeDays = regimen.scheduleDays;
							p.stage = regimen.stage;
							p.frequency = regimen.daysFrequency;
						} catch (Exception e) {
						}
					} catch (Exception e) {
					}
					Dose temp = DoseAdminstrationOperations.getLastDose(
							p.treatmentID, a);
					p.doseDate = temp.doseDate;
					p.doseType = temp.doseType;
					p.lastSupervisedDate = DoseAdminstrationOperations
							.getlastSupervised(p.treatmentID, a);
				}
			} catch (ConcurrentModificationException e) {
			}
			return entries;

		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Patient> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<Patient> oldApps = listOfData;
			mModels = listOfData;

			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(listOfData);
			}

			// At this point we can release the resources associated with
			// 'oldApps' if needed; now that the new result is delivered we
			// know that it is no longer in use.
			if (oldApps != null) {
				onReleaseResources(oldApps);
			}
		}

		/**
		 * Handles a request to start the Loader.
		 */
		@Override
		protected void onStartLoading() {
			if (mModels != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mModels);
			}

			if (takeContentChanged() || mModels == null) {
				// If the data has changed since the last time it was loaded
				// or is not currently available, start a load.
				forceLoad();
			}
		}

		/**
		 * Handles a request to stop the Loader.
		 */
		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
			cancelLoad();
		}

		/**
		 * Handles a request to cancel a load.
		 */
		@Override
		public void onCanceled(List<Patient> apps) {
			super.onCanceled(apps);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(apps);
		}

		/**
		 * Handles a request to completely reset the Loader.
		 */
		@Override
		protected void onReset() {
			super.onReset();

			// Ensure the loader is stopped
			onStopLoading();

			// At this point we can release the resources associated with 'apps'
			// if needed.
			if (mModels != null) {
				onReleaseResources(mModels);
				mModels = null;
			}
		}

		/**
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
		protected void onReleaseResources(List<Patient> apps) {
		}

	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		switch (repFrom) {
		case Home:
			i = new Intent(getApplicationContext(), HomeActivity.class);
			break;
		case Reports:
			i = new Intent(getApplicationContext(), Reports.class);

		default:
			break;
		}
		entries.clear();
		query = "";
		startActivity(i);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
		// this.finish();
	}

	public void onEditClick(View v) {
		ImageView view = (ImageView) v;

		if (!((eComplianceApp) this.getApplicationContext()).IsAdminLoggedIn) {
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog_box_yes_no);
			// set the custom dialog components - text, image and button

			Button noButton = (Button) dialog
					.findViewById(R.id.dialogButtonCancel);
			Button yesButton = (Button) dialog
					.findViewById(R.id.dialogButtonYes);
			TextView message = (TextView) dialog.findViewById(R.id.messageText);
			TextView title = (TextView) dialog.findViewById(R.id.text);
			message.setText(getResources().getString(R.string.doYouWantToLogin));
			title.setText(getResources().getString(R.string.adminLoginRequired));
			yesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					toHome();
					query = "";

				}
			});
			// if button is clicked, close the custom dialog
			noButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			PatientReportActivity.this.finish();
			Intent intent = new Intent(this, EditPatientActivity.class);
			intent.putExtra(IntentKeys.key_treatment_id, view.getTag()
					.toString());
			intent.putExtra(IntentKeys.key_intent_from,
					Enums.IntentFrom.Reports);
			intent.putExtra(IntentKeys.key_report_type, repType);
			query = "";
			startActivity(intent);
			overridePendingTransition(R.anim.right_side_in,
					R.anim.right_side_out);
		}
	}

	public void cl_phoneClick(View v) {
		ImageView phone = (ImageView) v;
		if (phone != null) {
			Intent i = new Intent(Intent.ACTION_DIAL);
			i.setData(Uri.parse("tel:" + phone.getTag().toString()));
			query = "";
			this.startActivity(i);
		}
	}

	private void toHome() {
		Intent intent = new Intent();
		intent = new Intent(this, HomeActivity.class);

		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

}