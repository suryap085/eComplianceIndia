/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
/**
 * 
 */
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.LoadAppData;
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author abhishek
 * 
 */
public class ConfigurationActivity extends Activity {

	Spinner spinnerLanguage, spinTextFree;
	boolean isSaveClicked;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_layout);
		spinnerLanguage = (Spinner) findViewById(R.id.spinLanguage);
		spinTextFree = (Spinner) findViewById(R.id.spinTextFree);
		setTitle(((eComplianceApp) this.getApplication()).App_Title);
		if (!ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_machine_demo, this).equals("true")
				&& !ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_is_home_page_of_text_on_pm_login,
						this).equals("1")) {
			spinTextFree.setVisibility(View.GONE);
		}

	}

	public void onSaveClick(View v) {
		int j = spinTextFree.getSelectedItemPosition();
		int i = spinnerLanguage.getSelectedItemPosition();
		switch (i) {
		case 0:
			ConfigurationOperations.addConfiguration(
					ConfigurationKeys.key_language, "en", this);
			LoadAppData.setloacle(this);
			break;
		case 1:
			ConfigurationOperations.addConfiguration(
					ConfigurationKeys.key_language, "hi", this);
			LoadAppData.setloacle(this);
			break;
		default:
			break;
		}
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_machine_demo, this).equals("true")) {
			switch (j) {
			case 0:
				((eComplianceApp) this.getApplicationContext()).IsAppTextFree = false;
				break;
			case 1:
				((eComplianceApp) this.getApplicationContext()).IsAppTextFree = true;
				break;
			default:
				break;
			}
		}
		toHome();

	}

	public void onCancelClick(View v) {
		toHome();

	}

	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		toHome();
	}

	public void toHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}
}
