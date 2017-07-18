/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.database.module;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.modal.wcf.ApiNameViewModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import com.google.gson.Gson;
import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

public class Authentication {

	private static String SYNC_SYNC_AUTHENTICATION = "SyncAuthenticate/InitialAuthenticate?";

	public static MachineAuth Sync(String authKey, Context context) {
		if (GenUtils.IsInternetConnected(context)) {
			if (GenUtils.CheckServerRunning(context)) {
				return syncAuthentication(authKey, context);
			}
		}
		return null;
	}

	private static MachineAuth syncAuthentication(String authKey,
			Context context) {
		MachineAuth machineAuth = new MachineAuth();
		if (!isAuthProceed(authKey, context)) {
			machineAuth.errorMessage = "keyManagementError";
			return machineAuth;
		}
		SYNC_SYNC_AUTHENTICATION = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_sync_api, context)
				+ SYNC_SYNC_AUTHENTICATION;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(SYNC_SYNC_AUTHENTICATION
					+ "authKey="
					+ authKey
					+ "&androidId="
					+ Secure.getString(context.getContentResolver(),
							Secure.ANDROID_ID));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			// get the response
			HttpResponse response = client.execute(request);
			
			HttpEntity entity = response.getEntity();

			int contentLength = (int) entity.getContentLength();

			if (contentLength != 0) {
				char[] buffer = new char[contentLength];
				InputStream stream = entity.getContent();
				InputStreamReader reader = new InputStreamReader(stream);

				int hasRead = 0;
				int currRead = 0;
				while (hasRead < contentLength && currRead >= 0) {
					currRead = reader.read(buffer, hasRead, contentLength
							- hasRead);
					if (currRead > 0)
						hasRead += currRead;
				}
				stream.close();

				JSONObject auth = new JSONObject(new String(buffer));

				Log.i("Info", auth.getString("ErrorMessage"));
				machineAuth.machineId = auth.getInt("MachineId");
				machineAuth.isActive = auth.getBoolean("IsActive");
				machineAuth.machine_locked = auth.getBoolean("Machine_Locked");
				machineAuth.activationPending = auth
						.getBoolean("ActivationPending");
				machineAuth.errorMessage = auth.getString("ErrorMessage");
				if (machineAuth.machineId == 0 || (!machineAuth.isActive)) {
					return machineAuth;
				}
				machineAuth.startDate = auth.getLong("StartDate");
				machineAuth.endDate = auth.getLong("EndDate");

				// machineAuth.lastSyncDate = auth.getLong("LastSyncDate");

				ConfigurationOperations.addConfiguration(
						ConfigurationKeys.key_Is_Machine_Active, "true",
						context);
				ConfigurationOperations.addConfiguration(
						ConfigurationKeys.key_Machine_Id,
						String.valueOf(machineAuth.machineId), context);

			}
		} catch (Exception e) {
			// any exception show the error layout
			e.printStackTrace();
			Log.e("error", e.toString());
		}
		return machineAuth;
	}

	private static boolean isAuthProceed(String authKey, Context context) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"http://keymanagement-prod.us-east-1.elasticbeanstalk.com/api/GetAPI/GetApiName?authKey="
							+ authKey);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			int contentLength = (int) entity.getContentLength();
			if (contentLength != 0) {
				char[] buffer = new char[contentLength];
				InputStream stream = entity.getContent();
				InputStreamReader reader = new InputStreamReader(stream);
				int hasRead = 0;
				int currRead = 0;
				while (hasRead < contentLength && currRead >= 0) {
					currRead = reader.read(buffer, hasRead, contentLength
							- hasRead);
					if (currRead > 0)
						hasRead += currRead;
				}
				stream.close();
				ApiNameViewModel ret = new Gson().fromJson(new String(buffer),
						ApiNameViewModel.class);

				if (ret.errorCode == 3) {
					ConfigurationOperations.addConfiguration(
							ConfigurationKeys.key_sync_api, ret.apiName,
							context);
					return true;
				}
			}
		} catch (Exception e) {
			// any exception show the error layout
			e.printStackTrace();
		}
		return false;
	}
}
