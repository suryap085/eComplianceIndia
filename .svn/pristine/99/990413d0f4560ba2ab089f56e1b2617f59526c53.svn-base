/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import org.opasha.eCompliance.ecompliance.TextFree.DownloadResourcesTask;
import org.opasha.eCompliance.ecompliance.database.module.SyncTask;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoSyncReceiver extends BroadcastReceiver {
	private boolean IsSyncBusy;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!IsSyncBusy) {
			IsSyncBusy = true;
			Logger.LogToDb(
					context,
					"Auto Sync",
					"Auto Sync Started at "
							+ String.valueOf(GenUtils
									.longToDateTimeString(System
											.currentTimeMillis())));
			new SyncTask().execute(new SyncTask.SyncTaskPayLoad(context.getApplicationContext(),
					new Object[] { 3, this, null }));
			if (((eComplianceApp) context.getApplicationContext()).IsAppTextFree) {
				new DownloadResourcesTask()
						.execute(new DownloadResourcesTask.DownloadTaskPayLoad(
								DbUtils.getTabId(context), context,
								new Object[] { this, 2 }));
			}
		}
	}

	public void syncComplete(Context context) {
		IsSyncBusy = false;
		Logger.LogToDb(
				context,
				"Auto Sync",
				"Auto Sync Completed at "
						+ String.valueOf(GenUtils.longToDateTimeString(System
								.currentTimeMillis())));
	}
}
