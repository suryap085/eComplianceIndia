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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageListAdapter extends BaseAdapter {
	private final LayoutInflater mInflater;
	Context context;
	ArrayList<String> list;

	public ImageListAdapter(Context context, ArrayList<String> list) {

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.list = list;
	}

	/**
	 * Populate new items in the list.
	 */
	@Override
	public View getView(int position, View vi, ViewGroup parent) {

		if (vi == null)
			vi = mInflater.inflate(R.layout.image_view, null);
		ImageView view = (ImageView) vi.findViewById(R.id.imageView);
		view.setAdjustViewBounds(true);
		view.setTag(list.get(position));
		String a = list.get(position);
		String b[] = a.split("/");
		if (b.length > 0) {
			view.setTag(b[b.length - 1]);
		}
		// Critical. Need to
		String file = list.get(position);
		Bitmap bitmapImage = BitmapFactory.decodeFile(list.get(position));
		Drawable drawableImage = new BitmapDrawable(bitmapImage);
		view.setImageDrawable(drawableImage);
		view.setBackgroundResource(R.drawable.edit_box_animation);
		if (position == 0) {
			vi.setSelected(true);
		}
		return vi;
	}

	@Override
	public int getCount() {
		return list.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}