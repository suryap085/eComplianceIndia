/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.net.HttpURLConnection;
import java.net.URL;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiagnosisActivity extends Activity {
	private ImageView txtInternet,txtServer,txtSim;
	private TextView txtLocation, lat;
	private LinearLayout layoutsim, layoutLocation, layoutInternet,
			layoutServer, layoutLatlong;
	private int isSimAvailable, isLocation;
	GpsTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diagnisis_activity);
		initializeView();
		tracker = new GpsTracker(this);

		try {
			new Connection().execute();
		} catch (Exception e) {
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		startActivity(new Intent(this, HomeActivity.class));
		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.finish();
	}

	private class Connection extends AsyncTask<String, String, Boolean> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = ProgressDialog.show(DiagnosisActivity.this,
					"Diagnose Utility", "Please wait...");
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean retvalue = false;
			try {
				URL url = new URL("http://www.ecompliancesuiteindia.com");
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setRequestProperty("User-Agent", "eContact Tracing");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1000 * 30);
				urlc.connect();
				if (urlc.getResponseCode() == 200) {
					retvalue = true;
				}
			} catch (Exception e) {
				Log.e("error", e.toString());
			}
			return retvalue;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			layoutInternet.setVisibility(View.VISIBLE);
			layoutServer.setVisibility(View.VISIBLE);
			layoutsim.setVisibility(View.VISIBLE);
			layoutLocation.setVisibility(View.VISIBLE);
			if (result) {
				txtServer.setImageResource(R.drawable.supervised);
			} else {
				txtServer.setImageResource(R.drawable.missed);
			}
			try {

				if (GenUtils.IsInternetConnected(DiagnosisActivity.this)) {
					txtInternet.setImageResource(R.drawable.supervised);
				} else {
					txtInternet.setImageResource(R.drawable.missed);
				}
			} catch (Exception e) {
				
			}

			try {

				lat.setText("Latitude: " + tracker.getLatitude()
						+ " , Longitude: " + tracker.getLongitude());

				layoutLatlong.setVisibility(View.VISIBLE);
				isLocation = tracker.locationAccuracyType();
				if (isLocation == 1) {
					txtLocation.setText("GPS Provider");
				} else if (isLocation == 2) {
					txtLocation.setText("Network Provider");
				} else if (isLocation == 3) {
					txtLocation.setText("High Accuracy");
				} else if (isLocation == 0) {
					txtLocation.setText("Location service is disable");
				}

			} catch (Exception e) {

			}
			try {

				isSimAvailable = GenUtils.simPresent(DiagnosisActivity.this);
				if (isSimAvailable == 1) {
					txtSim.setImageResource(R.drawable.supervised);
				} else if (isSimAvailable == 0) {
					txtSim.setImageResource(R.drawable.missed);
				}
			} catch (Exception e) {

			}
		}

	}

	public void initializeView() {

		txtInternet = (ImageView) findViewById(R.id.txtinternet);
		txtServer = (ImageView) findViewById(R.id.txtserver);
		txtSim = (ImageView) findViewById(R.id.txtsim);
		txtLocation = (TextView) findViewById(R.id.txtlocation);
		lat = (TextView) findViewById(R.id.txtlatlong);
		layoutLatlong = (LinearLayout) findViewById(R.id.layoutlatlong);
		layoutInternet = (LinearLayout) findViewById(R.id.layoutInternet);
		layoutServer = (LinearLayout) findViewById(R.id.layoutServer);
		layoutsim = (LinearLayout) findViewById(R.id.layoutsim);
		layoutLocation = (LinearLayout) findViewById(R.id.layoutLocation);

	}

}
