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

import org.opasha.eCompliance.ecompliance.Adapters.PostiveContactAdapter;
import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PositiveContactsOperations;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.Positive_Contacts;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.Enums.ReportType;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class PositiveContactsActivity extends Activity implements
		SearchView.OnQueryTextListener {
	ListView positiveConList;
	private SearchView mSearchView;
	ArrayList<Positive_Contacts> finalList;
	ArrayList<Positive_Contacts> filterList;
	PostiveContactAdapter contactAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_positive_contacts);
		positiveConList = (ListView) findViewById(R.id.positiveConList);
		finalList = PositiveContactsOperations.getPendingContactList(this);
		filterList = new ArrayList<Positive_Contacts>();
		filterList.addAll(finalList);
		contactAdapter = new PostiveContactAdapter(this, filterList);
		positiveConList.setAdapter(contactAdapter);
		positiveConList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final String conId = ((TextView)v.findViewById(R.id.conId)).getTag().toString(); 
			final String name = ((TextView)v.findViewById(R.id.name)).getText().toString();
			final String phone = ((ImageView)v.findViewById(R.id.phoneNo)).getTag().toString();
			final String gender = ((ImageView)v.findViewById(R.id.genderIcon)).getTag().toString();
			final String age = ((TextView)v.findViewById(R.id.age)).getText().toString();
				final Dialog dialog = new Dialog(PositiveContactsActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
				dialog.setContentView(R.layout.custom_dialog_box_yes_no);
				// set the custom dialog components - text, image and button
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
						+ getResources()
								.getString(R.string.doYouWantToContinue)
						+ "</p>";
				message.setText(Html.fromHtml(mess));
				title.setText(getResources().getString(R.string.verifyId));
				yesButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(PositiveContactsActivity.this,
								NewPatientActivity.class);
						String treatmentId = DbUtils
								.getTabId(PositiveContactsActivity.this)
								+ (AppStateConfigurationOperations
										.getMaxId(PositiveContactsActivity.this) + 1);				
						i.putExtra(IntentKeys.key_treatment_id, treatmentId);
						i.putExtra(IntentKeys.key_patient_Name, (name.split("\\("))[0].toString().trim());
						i.putExtra(IntentKeys.key_phone_no, phone);
						i.putExtra(IntentKeys.key_patient_gender, gender);
						i.putExtra(IntentKeys.key_patient_age, (age.split(":"))[1].toString().trim());
						i.putExtra(IntentKeys.key_contact_id, conId);
						i.putExtra(IntentKeys.key_intent_from,
								ReportType.positiveContact);
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
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.searchview_in_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);
		return true;
	}

	private void setupSearchView(MenuItem searchItem) {
		if (isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
		} else {
			searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			List<SearchableInfo> searchables = searchManager
					.getSearchablesInGlobalSearch();
			SearchableInfo info = searchManager
					.getSearchableInfo(getComponentName());
			for (SearchableInfo inf : searchables) {
				if (inf.getSuggestAuthority() != null
						&& inf.getSuggestAuthority().startsWith("name")) {
					info = inf;
				}
			}
			mSearchView.setSearchableInfo(info);
		}
		mSearchView.setOnQueryTextListener(this);
	}

	public boolean onQueryTextChange(String newText) {
		filterList.clear();

		if (!newText.equals("")) {
			for (Positive_Contacts list : finalList) {
				try {
					if (list.Name.startsWith(newText)
							|| list.Phone.startsWith(newText)
							|| list.Contact_Id.startsWith(newText)) {
						filterList.add(list);
					}
				} catch (Exception e) {
				}
			}

		} else {
			filterList.addAll(finalList);

		}
		contactAdapter.notifyDataSetChanged();
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	public boolean onClose() {
		return false;
	}

	public void onPhoneClick(View v) {
		ImageView phone = (ImageView) v;
		if (phone != null) {
			Intent i = new Intent(Intent.ACTION_DIAL);
			i.setData(Uri.parse("tel:" + phone.getTag().toString()));
			this.startActivity(i);
		}
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}
