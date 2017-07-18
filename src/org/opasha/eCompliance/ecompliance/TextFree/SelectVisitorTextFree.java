/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SelectVisitorTextFree extends Activity {
	LinearLayout layouts, maLayout;
	ImageButton imgProvider, imgPatient, imgPM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_visitor_text_free);
		layouts = (LinearLayout) findViewById(R.id.layouts);
		maLayout = (LinearLayout) findViewById(R.id.main);
		imgPatient = (ImageButton) findViewById(R.id.imgPatient);
		imgProvider = (ImageButton) findViewById(R.id.imgProvider);
		imgPM = (ImageButton) findViewById(R.id.imgPM);
		imgPatient.setSelected(true);

	}

	public void onPatientClick(View v) {
		imgPM.setSelected(false);
		imgProvider.setSelected(false);
		imgPatient.setSelected(true);
	}

	public void onPMClick(View v) {
		imgPatient.setSelected(false);
		imgProvider.setSelected(false);
		imgPM.setSelected(true);
	}

	public void onProviderClick(View v) {
		imgPatient.setSelected(false);
		imgPM.setSelected(false);
		imgProvider.setSelected(true);
	}

	public void onBackClick(View v) {
		goBack();

	}

	public void onNextClick(View v) {
		int count = layouts.getChildCount();
		boolean redAnim = true;
		for (int i = 0; i < count; i++) {
			View view = layouts.getChildAt(i);
			if (view.isSelected()) {
				int id = view.getId();
				if (id == R.id.imgPatient) {
					redAnim = false;
					if (!((eComplianceApp) this.getApplication()).IsAdminLoggedIn) {
						getYellowAnimation();
						return;
					}
					startActivity(new Intent(this, EnrollActivityTextFree.class)
							.putExtra(IntentKeys.key_visitor_type,
									Enums.VisitorType.Patient));
					this.finish();
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);

				} else if (id == R.id.imgPM) {
					redAnim = false;
					startActivity(new Intent(this, EnrollActivityTextFree.class)
							.putExtra(IntentKeys.key_visitor_type,
									Enums.VisitorType.PM));
					this.finish();
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);

				} else if (id == R.id.imgProvider) {
					redAnim = false;
					startActivity(new Intent(this, EnrollActivityTextFree.class)
							.putExtra(IntentKeys.key_visitor_type,
									Enums.VisitorType.Counselor));
					this.finish();
					overridePendingTransition(R.anim.left_side_out,
							R.anim.left_side_in);
				} else {
					getRedAnimation();
				}
			}
		}
		if (redAnim) {
			getRedAnimation();
		}
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	private void goBack() {
		Intent intent = new Intent(this, HomeActivityTextFree.class);
		this.finish();
		startActivity(intent);
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	private void getRedAnimation() {
		maLayout.setBackgroundResource(R.drawable.grey_to_red_transition);
		TransitionDrawable transition1 = (TransitionDrawable) maLayout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}

	private void getYellowAnimation() {
		maLayout.setBackgroundResource(R.drawable.grey_to_yellow_transition);
		TransitionDrawable transition1 = (TransitionDrawable) maLayout
				.getBackground();
		transition1.startTransition(0);
		transition1.reverseTransition(6000);
	}
}
