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

public class SpinnerStageAdapter extends BaseAdapter implements
		android.widget.Filterable {

	private Activity activity;
	ArrayList<Master> ListStage;
	private static LayoutInflater inflater = null;

	public SpinnerStageAdapter(Activity a, ArrayList<Master> p) {
		activity = a;
		ListStage = p;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return ListStage.size();
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
			vi = inflater.inflate(R.layout.stage_spinner, null);

		TextView cetgory = (TextView) vi.findViewById(R.id.spinnerStage);

		Master stage = ListStage.get(position);// Critical. Need to test

		// Setting all values in listview
		cetgory.setText(stage.stage);
		return vi;

	}

	public Filter getFilter() {
		return null;
	}

}
