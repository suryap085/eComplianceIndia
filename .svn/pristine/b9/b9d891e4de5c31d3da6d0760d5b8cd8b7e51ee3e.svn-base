/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.database.module;

import org.opasha.eCompliance.ecompliance.AuthKeyActivity;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;

import android.content.Context;
import android.os.AsyncTask;

public class AuthenticationTask
		extends
		AsyncTask<AuthenticationTask.AuthTaskPayLoad, Object, AuthenticationTask.AuthTaskPayLoad> {
	MachineAuth machineAuth;

	public static class AuthTaskPayLoad {
		Context context;
		Object[] objects;

		public AuthTaskPayLoad(Context context, Object[] objects) {
			this.context = context;
			this.objects = objects;
		}
	}

	@Override
	protected AuthTaskPayLoad doInBackground(AuthTaskPayLoad... params) {
		AuthenticationTask.AuthTaskPayLoad payload = params[0];
		machineAuth = Authentication.Sync((String) payload.objects[2],
				payload.context);
		return payload;
	}

	@Override
	public void onPostExecute(AuthenticationTask.AuthTaskPayLoad payload) {
		int actType = ((Integer) payload.objects[0]).intValue();
		switch (actType) {

		case 1:
			AuthKeyActivity authAct = (AuthKeyActivity) payload.objects[1];
			authAct.machineAuth = machineAuth;
			authAct.ShowNotification();
			break;

		}
	}
}
