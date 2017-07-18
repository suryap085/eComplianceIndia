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
import org.opasha.eCompliance.ecompliance.AutoSyncReceiver;
import org.opasha.eCompliance.ecompliance.HomeActivity;
import org.opasha.eCompliance.ecompliance.LocationReceiver;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.TextFree.HomeActivityTextFree;

import android.content.Context;
import android.os.AsyncTask;

public class SyncTask extends
		AsyncTask<SyncTask.SyncTaskPayLoad, Object, SyncTask.SyncTaskPayLoad> {
	MachineAuth machineAuth;

	public static class SyncTaskPayLoad {
		Context context;

		Object[] objects;

		public SyncTaskPayLoad(Context context, Object[] objects) {
			this.context = context;
			this.objects = objects;
		}
	}

	@Override
	protected SyncTaskPayLoad doInBackground(SyncTaskPayLoad... params) {
		SyncTask.SyncTaskPayLoad payload = params[0];
		int actType = ((Integer) payload.objects[0]).intValue();
		if (actType == 1) {
			machineAuth = SyncData.Sync(payload.context, true);
		} else {

			if (actType <= 4) {
				machineAuth = SyncData.Sync(payload.context, false);
			} else {
				if (actType == 5) {
					SyncData.sendQuickLocations(payload.context);
				} else {
					machineAuth = SyncData.Send(payload.context);//Only send data. This is not sync
				}
			}
		}
		return payload;
	}

	@Override
	public void onPostExecute(SyncTask.SyncTaskPayLoad payload) {
		int actType = ((Integer) payload.objects[0]).intValue();
		HomeActivity homeAct ;
		switch (actType) {
		case 1:
			AuthKeyActivity act = (AuthKeyActivity) payload.objects[1];
			act.goHome();
			break;
		case 2:
			homeAct = (HomeActivity) payload.objects[1];
			((eComplianceApp)homeAct.getApplication()).GetConfigurtions();
			homeAct.machineAuth = machineAuth;
			homeAct.ShowNotification();
			break;
		case 3: // Called from the Receiver.
			AutoSyncReceiver receiver = (AutoSyncReceiver) payload.objects[1];
			receiver.syncComplete(payload.context);
			break;
		case 5: // Called from the Location Receiver.
			LocationReceiver locReceiver = (LocationReceiver) payload.objects[1];
			locReceiver.syncComplete();
			break;
		case 4:
			HomeActivityTextFree homeActs = (HomeActivityTextFree) payload.objects[1];
			homeActs.showNotification();
			break;
		case 6:
			homeAct = (HomeActivity) payload.objects[1];
			homeAct.machineAuth = machineAuth;
			homeAct.markSendComplete();
			break;
		default:
			break;
		}
	}
}
