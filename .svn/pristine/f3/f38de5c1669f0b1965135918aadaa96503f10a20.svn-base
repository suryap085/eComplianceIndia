/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.MachineLocationOperations;
import org.opasha.eCompliance.ecompliance.database.module.PingTask;
import org.opasha.eCompliance.ecompliance.database.module.SyncTask;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationReceiver extends BroadcastReceiver {

	private boolean IsBusy;

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		//Do not send location data when machine is inactive
		if (!ConfigurationOperations.getKeyValue(ConfigurationKeys.key_Is_Machine_Active, this.context).equals("true")) {
			return;
		}
		if (!((eComplianceApp) context.getApplicationContext()).IsNetworkProcessBusy) {
			if (!IsBusy) {
				IsBusy = true;
				GpsTracker tracker = new GpsTracker(context);
				if (tracker.canGetLocation()) {
					MachineLocationOperations.add(context,
							tracker.getLatitude(), tracker.getLongitude());
				}
				tracker = null;
				new SyncTask().execute(new SyncTask.SyncTaskPayLoad(context,
						new Object[] { 5, this, null }));
			}
		}
	}

	public void syncComplete() {
		new PingTask().execute(new PingTask.PingTaskPayLoad(context,
				new Object[] { this }));
	}

	public void PingComplete() {
		IsBusy = false;
	}

}
