/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.IntentFrom;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PatientDetailsActivity extends Activity {
	static Enums.IntentFrom repFrom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dose_report);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		repFrom=(IntentFrom) getIntent().getExtras().get(
				IntentKeys.key_intent_from);
	}

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		//super.onBackPressed();
//		Intent i=new Intent();
//		switch (repFrom) {
//		case Home:
//			i = new Intent(getApplicationContext(), HomeActivity.class);
//			break;
//		case Reports:
//			i = new Intent(getApplicationContext(), Reports.class);
//
//		default:
//			break;
//		}
//		startActivity(i);
//		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
//		
//	}
}
