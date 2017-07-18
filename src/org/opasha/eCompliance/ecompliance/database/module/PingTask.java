/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.database.module;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.opasha.eCompliance.ecompliance.LocationReceiver;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

public class PingTask extends
		AsyncTask<PingTask.PingTaskPayLoad, Object, PingTask.PingTaskPayLoad> {

	private static String SEND_PING = "Beacon/Ping";

	public static class PingTaskPayLoad {
		Context context;

		Object[] objects;

		public PingTaskPayLoad(Context context, Object[] objects) {
			this.context = context;
			this.objects = objects;
		}
	}

	@Override
	protected PingTaskPayLoad doInBackground(PingTaskPayLoad... params) {
		PingTask.PingTaskPayLoad payload = params[0];
		sendPing(payload.context);
		return payload;
	}

	@Override
	public void onPostExecute(PingTask.PingTaskPayLoad payload) {
		LocationReceiver locReceiver = (LocationReceiver) payload.objects[0];
		locReceiver.PingComplete();
		
	}

	@SuppressWarnings("deprecation")
	public static void sendPing(Context context) {

		DefaultHttpClient client = new DefaultHttpClient();
		if (GenUtils.IsInternetConnected(context)) {
			if (GenUtils.CheckServerRunning(context)) {

				// Get Location Setting (GPS)
				// 0 - Off
				// 1 - Sensor Only
				// 2 - Battery Saving
				// 3 - High Accuracy
				int locationSetting = 0;
				try {
					final LocationManager manager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);

					if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
							&& manager
									.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
						locationSetting = 3;
					} else {
						if (manager
								.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

							locationSetting = 1;
						}

						if (manager
								.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
							locationSetting = 2;
						}
					}

				} catch (Exception e1) {
					locationSetting = 4;
				}

				int simPresent = 2;
				try {
					TelephonyManager tm = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
						simPresent = 1;
					} else {
						simPresent = 0;
					}
				} catch (Exception e1) {
					simPresent = 2;
				}

				int versionCode = 0;
				try {
					versionCode = context.getPackageManager().getPackageInfo(
							context.getPackageName(), 0).versionCode;
				} catch (NameNotFoundException e1) {
					versionCode = 0;
				}

				int machineId = Integer
						.parseInt(ConfigurationOperations.getKeyValue(
								ConfigurationKeys.key_Machine_Id, context));

				String query = SyncData.GenerateQueryCompressed(SEND_PING,
						context)
						+ "?machineId="
						+ machineId
						+ "&info="
						+ locationSetting + "" + simPresent + "" + versionCode;

				HttpGet request = new HttpGet(query);
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");
				request.addHeader("Accept-Encoding", "gzip");

				// get the response
				try {
					client.execute(request);
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
			}
		}
	}

}
