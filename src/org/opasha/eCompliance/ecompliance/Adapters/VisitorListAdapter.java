/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Adapters;

import java.util.List;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VisitorListAdapter extends ArrayAdapter<Visitor> {
	private final LayoutInflater mInflater;
	Context context;

	public VisitorListAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	public void setData(List<Visitor> data) {
		clear();
		if (data != null) {
			for (Visitor appEntry : data) {
				add(appEntry);
			}
		}
	}

	/**
	 * Populate new items in the list.
	 */
	@Override
	public View getView(int position, View vi, ViewGroup parent) {
		ViewHolder holder;
		String VisitorType = "";
		if (vi == null) {
			vi = mInflater.inflate(R.layout.custom_visitor_row, parent, false);
			holder = new ViewHolder();
			holder.contactId = (TextView) vi.findViewById(R.id.UniqueId);
			holder.name = (TextView) vi.findViewById(R.id.name);

			holder.phone = (ImageView) vi.findViewById(R.id.phone);

			holder.lastVisit = (TextView) vi.findViewById(R.id.lastVisit);
			holder.visitorImage = (ImageView) vi.findViewById(R.id.EditPatient);
			holder.imageLastDose = (ImageView) vi
					.findViewById(R.id.imagelastDose);
			holder.Phoneline = (LinearLayout) vi
					.findViewById(R.id.PhoneIconDivider);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		Visitor VisitorView = new Visitor();
		VisitorView = getItem(position);// Critical. Need to
										// test

		// test

		// Setting all values in listview
		if (VisitorView.visitorType != null) {
			if (VisitorView.visitorType.equals("Counselor")) {
				VisitorType = "Provider";
			} else {
				VisitorType = VisitorView.visitorType;
			}
		}
		holder.name.setText(GenUtils.makeFirstLetterUpperCase(VisitorView.name.trim()) + "(" + VisitorView.visitorID
				+ ") - " + VisitorType);
		holder.contactId.setTag(VisitorView.visitorID);
		holder.visitorImage.setTag(VisitorView.visitorID);
        holder.contactId.setText(VisitorView.visitorID);
		holder.contactId.setVisibility(View.GONE);
		holder.visitorImage
				.setBackgroundResource(getVistorImage(VisitorView.visitorType));
		holder.visitorImage.setMaxHeight(100);
		holder.visitorImage.setMaxWidth(100);
		if (VisitorView.loginTimeStamp == 0) {
			holder.lastVisit.setText(context.getString(R.string.lastVisit)
					+ " = NA");
		} else {
			holder.lastVisit
					.setText(context.getString(R.string.lastVisit)
							+ " = "
							+ GenUtils
									.longToDateTimeString(VisitorView.loginTimeStamp));
		}
		holder.phone.setVisibility(View.VISIBLE);
		holder.phone.setTag(VisitorView.phone);
		if (VisitorView.phone.trim().length() > 0) {
			holder.phone.setImageResource(R.drawable.phone_icon_call);
			holder.phone.setTag(VisitorView.phone);
		} else {
			holder.phone.setVisibility(View.GONE);
			holder.Phoneline.setVisibility(View.GONE);
		}

		return vi;
	}

	private static class ViewHolder {
		TextView contactId;
		LinearLayout rowLayout;
		TextView name;
		ImageView visitorImage;
		ImageView phone;
		ImageView catIcon;
		TextView lastVisit;
		ImageView imageLastDose;
		LinearLayout Phoneline;
	}

	private int getVistorImage(String type) {
		if (type.equals(Enums.VisitorType.CDP.toString())) {
			return R.drawable.community_partners;
		} else if (type.equals(Enums.VisitorType.PM.toString())) {
			return R.drawable.pm;
		} else if (type.equals(Enums.VisitorType.QualityAuditor.toString())) {
			return R.drawable.qa;
		} else if (type.equals(Enums.VisitorType.Other.toString())) {
			return R.drawable.other_visitors;
		} else if (type.equals(Enums.VisitorType.Counselor.toString())) {
			return R.drawable.provider;
		}
		return R.drawable.pm;

	}
}