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
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PatientListAdapter extends ArrayAdapter<Patient> {
	private final LayoutInflater mInflater;
	Context context;

	public PatientListAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	public void setData(List<Patient> data) {
		clear();
		if (data != null) {
			for (Patient appEntry : data) {
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
		if (vi == null) {
			vi = mInflater.inflate(R.layout.customlist_row, parent, false);
			holder = new ViewHolder();
			holder.contactId = (TextView) vi.findViewById(R.id.UniqueId);
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.regimen = (TextView) vi.findViewById(R.id.Regimen);
			holder.phone = (ImageView) vi.findViewById(R.id.phone);
			holder.catIcon = (ImageView) vi.findViewById(R.id.list_image);
			holder.lastVisit = (TextView) vi.findViewById(R.id.lastVisit);
			holder.rowLayout = (LinearLayout) vi.findViewById(R.id.customlist);
			holder.imageLastDose = (ImageView) vi
					.findViewById(R.id.imagelastDose);
			holder.Phoneline = (LinearLayout) vi
					.findViewById(R.id.PhoneIconDivider);
			holder.editPatient = (ImageView) vi.findViewById(R.id.EditPatient);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		Patient VisitorView = new Patient();
		holder.editPatient.setBackgroundResource(R.drawable.edit_gradient_bg);
		holder.editPatient.setMaxHeight(100);
		holder.editPatient.setMaxWidth(100);

		VisitorView = getItem(position);// Critical. Need to
		// test
		try {
			if (VisitorView.category.equals(Enums.CategoryType.getCategoryType(
					CategoryType.CAT1).toString())) {
				holder.rowLayout
						.setBackgroundResource(R.drawable.catone_gradient_bg);
			} else if (VisitorView.category.equals(Enums.CategoryType
					.getCategoryType(CategoryType.CAT6).toString())) {
				holder.rowLayout
						.setBackgroundResource(R.drawable.mdr_gradient_bg);
			} else {
				holder.rowLayout
						.setBackgroundResource(R.drawable.cattwo_gradient_bg);
			}

		} catch (Exception e) {
		}

		// if (VisitorView.category.equals(Enums.CategoryType.getCategoryType(
		// CategoryType.CAT1).toString())) {
		// holder.catIcon.setBackgroundResource(R.drawable.cat_i);
		// } else if (VisitorView.category.equals(Enums.CategoryType
		// .getCategoryType(CategoryType.CAT2).toString())) {
		// holder.catIcon.setBackgroundResource(R.drawable.cat_ii);
		// }

		// Setting all values in listview
		holder.name.setText(GenUtils.makeFirstLetterUpperCase(VisitorView.name
				.trim())
				+ "("
				+ VisitorView.treatmentID
				+ ") -"
				+ GenUtils.longToDateString(VisitorView.RegDate));
		holder.contactId.setText(VisitorView.treatmentID);
		holder.editPatient.setTag(VisitorView.treatmentID);

		holder.contactId.setVisibility(View.GONE);
		try {
			holder.regimen.setText(VisitorView.stage + "-"
					+ VisitorView.schedule);
		} catch (Exception e) {
		}
		holder.phone.setVisibility(View.VISIBLE);
		try {
			if (VisitorView.doseType.equals(Enums.DoseType.getDoseType(
					DoseType.Missed).toString())) {
				holder.imageLastDose.setImageResource(R.drawable.thumbs_down);
			} else {
				holder.imageLastDose.setImageResource(R.drawable.thumbsup);
			}
		} catch (Exception e) {

		}

		if (VisitorView.lastSupervisedDate == 0) {
			holder.lastVisit.setText(context.getString(R.string.lastVisit)
					+ " = NA");
		} else {
			holder.lastVisit
					.setText(context.getString(R.string.lastVisit)
							+ " = "
							+ GenUtils
									.longToDateString(VisitorView.lastSupervisedDate));
		}
		if (VisitorView.phoneNumber != null) {
			if (VisitorView.phoneNumber.trim().length() > 0) {
				holder.phone.setImageResource(R.drawable.phone_icon_call);
				holder.phone.setTag(VisitorView.phoneNumber);
			} else {
				holder.phone.setVisibility(View.GONE);
				holder.Phoneline.setVisibility(View.GONE);
			}
		}
		return vi;
	}

	private static class ViewHolder {
		TextView contactId;
		LinearLayout rowLayout;
		TextView name;
		TextView regimen;
		ImageView editPatient;
		ImageView phone;
		ImageView catIcon;
		TextView lastVisit;
		ImageView imageLastDose;
		LinearLayout Phoneline;
	}

}