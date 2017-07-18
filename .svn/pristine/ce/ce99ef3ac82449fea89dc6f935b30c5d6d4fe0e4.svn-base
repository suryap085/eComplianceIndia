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

import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class SpinnerScheduleAdapter extends BaseAdapter implements
		android.widget.Filterable {

	private Activity activity;
	ArrayList<Master> ListSchedule;
	private static LayoutInflater inflater = null;

	public SpinnerScheduleAdapter(Activity a, ArrayList<Master> p) {
		activity = a;
		ListSchedule = p;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
	
		return ListSchedule.size();
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
			vi = inflater.inflate(R.layout.schedule_spinner, null);

		TextView cetgory = (TextView) vi.findViewById(R.id.spinnerSchedule);

		Master schedule = ListSchedule.get(position);// Critical. Need to test

		// Setting all values in listview
		cetgory.setText(schedule.schedule);
		return vi;

	}

	public Filter getFilter() {
		return null;
	}

}
