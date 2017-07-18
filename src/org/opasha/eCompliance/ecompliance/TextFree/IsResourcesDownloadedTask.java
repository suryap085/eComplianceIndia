/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.io.File;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.AutoSyncReceiver;
import org.opasha.eCompliance.ecompliance.StartActivity;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientStatusMasterOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.DbUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

public class IsResourcesDownloadedTask
		extends
		AsyncTask<IsResourcesDownloadedTask.DownloadTaskPayLoad, Object, IsResourcesDownloadedTask.DownloadTaskPayLoad> {
	boolean shouldCheck = true;

	public static class DownloadTaskPayLoad {
		Context context;

		Object[] objects;
		String machineId;

		public DownloadTaskPayLoad(String machineId, Context context,
				Object[] objects) {
			this.context = context;
			this.objects = objects;
			this.machineId = machineId;
		}
	}

	@Override
	protected DownloadTaskPayLoad doInBackground(DownloadTaskPayLoad... params) {
		IsResourcesDownloadedTask.DownloadTaskPayLoad payload = params[0];
		((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = true;
		String patientIconLocation = "//eComplianceClient//"
				+ DbUtils.getTabId(payload.context) + "//resources//";
		String appIconLocation = "//eComplianceClient//"
				+ DbUtils.getTabId(payload.context) + "//resources//App//";
		ArrayList<String> regimenList = ApplicationIconOperation
				.getIconsToDownload(payload.context);
		ArrayList<String> statusList = PatientStatusMasterOperations
				.getStatuss(Schema.PATIENT_STATUS_MASTER_IS_ACTIVE + "=1",
						payload.context);
		ArrayList<Center> centerlist = CentersOperations.getCenterForSpinner(
				false, payload.context);
		ArrayList<String> patientIconList = MasterIconOperation.getIcons(null,
				payload.context);
		for (String s : regimenList) {
			if (!isIconDownloade(s, appIconLocation, payload.context)) {
				((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = false;
				shouldCheck = false;
				break;
			}

		}
		if (shouldCheck) {
			for (String s : statusList) {
				if (!isIconDownloade(s + ".png", appIconLocation,
						payload.context)) {
					((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = false;
					shouldCheck = false;
					break;
				}

			}
		}
		if (shouldCheck) {
			for (Center c : centerlist) {
				if (!isIconDownloade(c.centerName + ".png", appIconLocation,
						payload.context)) {
					((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = false;
					shouldCheck = false;
					break;
				}
			}
		}
		if (shouldCheck) {
			for (String s : patientIconList) {
				if (!isIconDownloade(s, patientIconLocation, payload.context)) {
					((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = false;
					shouldCheck = false;
					break;
				}
			}
		}
		return payload;
	}

	@Override
	public void onPostExecute(
			IsResourcesDownloadedTask.DownloadTaskPayLoad payload) {
		int type = (Integer) payload.objects[1];
		switch (type) {
		case 1:
			HomeActivityTextFree act = (HomeActivityTextFree) payload.objects[0];
			act.showDownloadButton();
			break;
		case 2:
			AutoSyncReceiver receiver = (AutoSyncReceiver) payload.objects[0];
			receiver.syncComplete(payload.context);
		case 3:
			StartActivity StartAct = (StartActivity) payload.objects[0];
			StartAct.goHome();
		default:
			break;
		}

	}

	private boolean isIconDownloade(String filename, String saveLocation,
			Context context) {
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File directorys = new File(sd, saveLocation);

		File file = new File(directorys, filename);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}
