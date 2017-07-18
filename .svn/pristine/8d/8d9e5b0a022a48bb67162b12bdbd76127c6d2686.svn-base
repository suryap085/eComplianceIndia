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
import java.util.List;

import org.opasha.eCompliance.ecompliance.Adapters.VisitorListAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorLoginOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IntentFrom;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VisitorReportActivity extends FragmentActivity {
	static Enums.ReportType repType;
	static Enums.IntentFrom repFrom;
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		// to give support on lower android version, we are not calling
		// getFragmentManager()
		FragmentManager fm = getFragmentManager();

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
			LoaderManager.LoaderCallbacks<List<Visitor>> {

		VisitorListAdapter mAdapter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Initially there is no data
			setEmptyText(getResources().getString(R.string.noDataHere));

			// Create an empty adapter we will use to display the loaded data.
			mAdapter = new VisitorListAdapter(getActivity());
			setListAdapter(mAdapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onListItemClick(final ListView l, View v, int position,
				long id) {
			// Insert desired behavior here.
			final String ContactId = ((TextView) v.findViewById(R.id.UniqueId))
					.getText().toString();
			// Set pop-up if report is PatientsFromLegacySystem report before
			// redirecting to enroll page.
			if (repType.equals(Enums.ReportType.VisitorReregistration)) {
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_Admin_Login_Required,
						l.getContext()).equals("1")) {
					if (((eComplianceApp) l.getContext()
							.getApplicationContext()).visitorType != VisitorType.PM) {
						final Dialog dialog = new Dialog(l.getContext());
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.custom_dialog_box_yes_no);
						Button noButton = (Button) dialog
								.findViewById(R.id.dialogButtonCancel);
						Button yesButton = (Button) dialog
								.findViewById(R.id.dialogButtonYes);
						TextView message = (TextView) dialog
								.findViewById(R.id.messageText);
						TextView title = (TextView) dialog
								.findViewById(R.id.text);
						message.setText(getResources().getString(
								R.string.pmLoginRequiredFull));
						title.setText(getResources().getString(
								R.string.updateScan));
						noButton.setVisibility(View.GONE);
						yesButton.setGravity(Gravity.CENTER);
						yesButton
								.setText(getResources().getString(R.string.ok));
						yesButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();

							}
						});
						dialog.show();
					} else {
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
						final String visitorType = ((TextView) v
								.findViewById(R.id.name)).getText().toString()
								.split("-")[1].trim();

						yesButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								VisitorReportActivity.this.finish();
								Intent i = new Intent(l.getContext(),
										RecheckIdActivity.class);

								i.putExtra(IntentKeys.key_visitor_type,
										visitorType);
								i.putExtra(IntentKeys.key_treatment_id,
										ContactId);
								i.putExtra(IntentKeys.key_intent_from,
										ReportType.VisitorReregistration);

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
					}
				} else {
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
					TextView title = (TextView) dialog.findViewById(R.id.text);
					// Set String as Html to bold text of first line
					String mess = "<p><b>"
							+ ((TextView) v.findViewById(R.id.name)).getText()
									.toString()
							+ "</b> <br>"
							+ getResources().getString(
									R.string.doYouWantToContinue) + "</p>";
					message.setText(Html.fromHtml(mess));
					title.setText(getResources().getString(R.string.verifyId));
					final String visitorType = ((TextView) v
							.findViewById(R.id.name)).getText().toString()
							.split("-")[1].trim();

					yesButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							VisitorReportActivity.this.finish();
							Intent i = new Intent(l.getContext(),
									RecheckIdActivity.class);

							i.putExtra(IntentKeys.key_visitor_type, visitorType);
							i.putExtra(IntentKeys.key_treatment_id, ContactId);
							i.putExtra(IntentKeys.key_intent_from,
									ReportType.VisitorReregistration);

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
				}

			}

		}

		@Override
		public Loader<List<Visitor>> onCreateLoader(int arg0, Bundle arg1) {
			System.out.println("DataListFragment.onCreateLoader");
			return new DataListLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<Visitor>> arg0,
				List<Visitor> data) {
			mAdapter.setData(data);

			// The list should now be shown.
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Visitor>> arg0) {
			mAdapter.setData(null);
		}
	}

	public static class DataListLoader extends AsyncTaskLoader<List<Visitor>> {

		List<Visitor> mModels;
		Activity a;

		public DataListLoader(Context context) {
			super(context);
			a = (Activity) context;
		}

		@Override
		public List<Visitor> loadInBackground() {

			// You should perform the heavy task of getting data from
			// Internet or database or other source
			// Here, we are generating some Sample data

			// Create corresponding array of entries and load with data.

			List<Visitor> entries = new ArrayList<Visitor>();
			String query = null;
			switch (repType) {
			case AllVisitor:
				query = Schema.VISITORS_STATUS
						+ "='"
						+ Enums.StatusType.getStatusType(StatusType.Active)
								.toString() + "' and "
						+ Schema.VISITORS_IS_DELETED + "=0";
				entries = VisitorsOperations.getVisitor(query, a);
				for (Visitor v : entries) {
					v.loginTimeStamp = VisitorLoginOperations.getLastLogin(
							v.visitorID, a);
				}

				break;
			case VisitedVisitor:
				entries = VisitorLoginOperations.getVisitforDay(
						GenUtils.getCurrentDateLong(), a);
				ArrayList<Visitor> details = new ArrayList<Visitor>();
				for (Visitor v : entries) {
					query = Schema.VISITORS_ID + "= '" + v.visitorID + "' and "
							+ Schema.VISITORS_IS_DELETED + "=0";
					details = VisitorsOperations.getVisitor(query, a);
					v.name = details.get(0).name;
					v.phone = details.get(0).phone;
					v.visitorType = details.get(0).visitorType;
					v.registrationDate = details.get(0).registrationDate;
					v.status = details.get(0).status;
					v.loginTimeStamp = VisitorLoginOperations.getLastLogin(
							v.visitorID, a);
				}
				break;

			default:
				break;
			}

			if (repType.equals(Enums.ReportType.VisitorReregistration)) {
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_used_device, a).equals("iris")) {
					entries = VisitorsOperations
							.getPatientsFromLegacySystemIris(a);
				} else {
					entries = VisitorsOperations
							.getPatientsFromLegacySystemFp(a);
				}
			}
			// else {
			// entries = VisitorsOperations.getVisitor(
			// Schema.VISITORS_IS_DELETED + " =0 and ("
			// + query.toString() + ")", a);
			// }
			return entries;

		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Visitor> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<Visitor> oldApps = listOfData;
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
		public void onCanceled(List<Visitor> apps) {
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
		protected void onReleaseResources(List<Visitor> apps) {
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
		startActivity(i);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
		this.finish();
	}

	private void toHome() {
		Intent intent = new Intent();
		intent = new Intent(this, HomeActivity.class);

		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	public void onEditClick(View v) {

		ImageView view = (ImageView) v;
		if (!((eComplianceApp) context.getApplicationContext()).IsAdminLoggedIn) {
			final Dialog dialog = new Dialog(context);
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

			Intent intent = new Intent(context, EditVisitorActivity.class);
			intent.putExtra(IntentKeys.key_treatment_id, view.getTag()
					.toString());
			intent.putExtra(IntentKeys.key_intent_from,
					Enums.IntentFrom.Reports);
			intent.putExtra(IntentKeys.key_report_type, repType);
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
			this.startActivity(i);
		}
	}
}
