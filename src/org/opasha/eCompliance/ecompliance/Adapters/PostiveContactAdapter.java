/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Adapters;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.Positive_Contacts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostiveContactAdapter extends BaseAdapter implements
		android.widget.Filterable {
	private Activity activity;
	ArrayList<Positive_Contacts> List;
	private static LayoutInflater inflater = null;

	public PostiveContactAdapter(Activity a, ArrayList<Positive_Contacts> p) {
		activity = a;
		List = p;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return List.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.positive_contact_row_view, null);
		ImageView gender = (ImageView) vi.findViewById(R.id.genderIcon);
		TextView name = (TextView) vi.findViewById(R.id.name);
		LinearLayout phoneLayout = (LinearLayout) vi
				.findViewById(R.id.phoneLayout);
		TextView conId = (TextView) vi.findViewById(R.id.conId);
		ImageView phoneNo = (ImageView) vi.findViewById(R.id.phoneNo);
		TextView refPhone = (TextView) vi.findViewById(R.id.refPhone);
		TextView refId = (TextView) vi.findViewById(R.id.refId);
		TextView address = (TextView) vi.findViewById(R.id.address);
		TextView age = (TextView) vi.findViewById(R.id.age);
		Positive_Contacts list = List.get(position);
		if (list.Gender.startsWith("M"))
			gender.setBackgroundResource(R.drawable.male_icon);
		else
			gender.setBackgroundResource(R.drawable.female_icon);
		gender.setTag(list.Gender);
		conId.setTag(list.Contact_Id);
		name.setText(list.Name + "(" + list.Contact_Id + ")");
		if (!list.Phone.isEmpty())
			phoneNo.setTag(list.Phone);
		else
			phoneLayout.setVisibility(View.GONE);
		if (!list.Ref_Phone.isEmpty())
			refPhone.setText(activity.getResources().getString(
					R.string.refPhone)
					+ ": " + list.Ref_Phone);
		refId.setText(activity.getResources().getString(R.string.refName)
				+ ": " + list.Ref_Name + "(" + list.Ref_Id + ")");
		address.setText(activity.getResources().getString(R.string.address)
				+ ": " + list.Address);
		age.setText(activity.getResources().getString(R.string.age) + ": "
				+ list.Age);
		return vi;
	}

	public Filter getFilter() {
		return null;
	}
}
