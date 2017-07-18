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
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientStatusMasterOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.util.DbUtils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class DownloadResourcesTask
		extends
		AsyncTask<DownloadResourcesTask.DownloadTaskPayLoad, Object, DownloadResourcesTask.DownloadTaskPayLoad> {
	private String patientIcons = "http://www.opashareports.org/eComplianceAndroid/resources/Text%20Less%20Icons/";
	private String ApplicationIcons = "http://www.opashareports.org/eComplianceAndroid/resources/Text%20Less%20Icons/ApplicationIcon/";

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
		DownloadResourcesTask.DownloadTaskPayLoad payload = params[0];
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
			downloadIcon(s, ApplicationIcons, appIconLocation, payload.context);
		}
		for (String s : statusList) {
			downloadIcon(s + ".png", ApplicationIcons, appIconLocation,
					payload.context);
		}
		for (Center c : centerlist) {
			downloadIcon(c.centerName + ".png", ApplicationIcons,
					appIconLocation, payload.context);
		}
		for (String s : patientIconList) {
			downloadIcon(s, patientIcons, patientIconLocation, payload.context);
		}
		patientIconList = MasterIconOperation.getIcons(null, payload.context);
		if (patientIconList.size() == 0) {
			((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = true;
		} else {
			((eComplianceApp) payload.context.getApplicationContext()).IsResourceDownloaded = false;
		}
		return payload;
	}

	@Override
	public void onPostExecute(DownloadResourcesTask.DownloadTaskPayLoad payload) {
		int type = (Integer) payload.objects[1];
		switch (type) {
		case 1:
			HomeActivityTextFree act = (HomeActivityTextFree) payload.objects[0];
			act.showNotification();
			break;
		case 2:
			AutoSyncReceiver receiver = (AutoSyncReceiver) payload.objects[0];
			receiver.syncComplete(payload.context);
		default:
			break;
		}

	}

	private void downloadIcon(String filename, String httpAddress,
			String saveLocation, Context context) {
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File directorys = new File(sd, saveLocation);
		if (!directorys.exists()) {
			directorys.mkdirs();
		} else {
			File file = new File(directorys, filename);
			if (file.exists()) {
				return;
			}
		}
		try {
			((DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE))
					.remove(IconDownloadLockOperation.getEnqueueID(filename,
							context));
			long id = ((DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE))
					.enqueue(new DownloadManager.Request(Uri.parse(httpAddress
							+ Uri.encode(filename)))
							.setAllowedOverRoaming(true)
							.setTitle(filename)
							.setDescription("eCompliance Resource")
							.setDestinationInExternalPublicDir(
									Environment.DIRECTORY_DOWNLOADS,
									saveLocation + filename));
			IconDownloadLockOperation.add(filename, id, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
