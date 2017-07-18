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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ECounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.FutureMissedDoseOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.HIVOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialCounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialLabMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.LocationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.LoggerOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.MachineLocationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.MasterAuditQuesSpnValueOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.MasterDiabetesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Master_QA_Questions;
import org.opasha.eCompliance.ecompliance.DbOperations.NetworkOperatorOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDiabetesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientGenderAgeOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientHospitalizationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabFollowUpOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabsOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientStatusMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientV2_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.Patient_Initial_Lab_Data_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PositiveContactsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.QualityAuditingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.QualityAuditingSummaryOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Scan_Identification_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisR_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansROperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.UnSupervisedDoseReasonOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.UnsupervisedReasonMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VideosCategoryOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VideosOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorLoginOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.MachineAuth;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.NetworkOperator;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.Model.USSDSyncModel;
import org.opasha.eCompliance.ecompliance.TextFree.ApplicationIconOperation;
import org.opasha.eCompliance.ecompliance.TextFree.MasterIconOperation;
import org.opasha.eCompliance.ecompliance.TextFree.PatientIconOperation;
import org.opasha.eCompliance.ecompliance.modal.wcf.ClientLoggerViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.MachineLocations;
import org.opasha.eCompliance.ecompliance.modal.wcf.MasterResources;
import org.opasha.eCompliance.ecompliance.modal.wcf.Master_Resource;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientAgeGender;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientDetail;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientIconGet;
import org.opasha.eCompliance.ecompliance.modal.wcf.ScanLogs;
import org.opasha.eCompliance.ecompliance.modal.wcf.ScansIrisViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.VisitorDetails;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.ContactDeatils;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.PositiveContacts;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.SavePositiveContactModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.Deleted_Scan;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.DoseAdminViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.FutureDoseViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDetails2ViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDiabetes;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientHIV;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientHospitalizationViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientLabFollowup;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientLocationViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientsViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.ScansViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.TreatmentInStagesViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.VisitorViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.VisitorsGetViewModel;
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.DbUtils;
import org.opasha.eCompliance.ecompliance.util.DoseUtils;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.GpsTracker;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SyncData {
	private static boolean lastSyncUpdateAtClient = true;
	private static boolean lastSyncUpdateAtServer = true;
	static MachineAuth machineAuth;
	private static long lastSync = 0;
	private static String SYNC_AUTHENTICATION = "SyncAuthenticate/Authenticate?";
	private static String SYNC_MASTER = "SyncMaster/GetMastersV2?";
	private static String SYNC_ICONS = "SyncMaster/GetIconMaster?";
	private static String SYNC_CONFIGURATION = "SyncConfiguration/GetConfigurations?";
	private static String SYNC_SEND_PATIENT = "SyncPatient/SetPatientsV2";
	private static String SYNC_SEND_VISITOR = "SyncVisitor/SetVisitors";
	private static String SYNC_GET_PATIENT = "SyncPatient/GetPatientsV2?";
	private static String SYNC_GET_VISITOR = "SyncVisitor/GetVisitors?";
	private static String SYNC_SET_LAST_SYNC = "SyncServerTimings/SetLastSync?";
	private static String SYNC_SET_AUTH = "SyncAuthenticate/SetAuthentication?";
	private static String SYNC_SEND_LOCATIONS = "SyncLocations/SetLocations";
	private static String SYNC_SEND_LOGS = "SyncLogger/SendLogs";
	private static String SYNC_GET_USSD = "SyncUssd/GetUSSD?";
	private static String SYNC_GET_POSITIVE_CONTACT = "CTSyncPositiveContacts/GetPositiveContact?";
	private static String SYNC_SEND_POSITIVE_CONTACT = "CTSyncPositiveContacts/SetPositiveContact";
	private static String SYNC_SEND_SCANLOGS = "SyncLogger/SendScanLogs";
	private static String SEND_FINALIZE = "SyncServerTimings/FinalizeSend?";
	private static String SYNC_SEND_IRIS = "SyncScan/SetScansIris";
	private static String SYNC_GET_IRIS = "SyncScan/GetScansIris?";
	public static String apiPath;

	public static MachineAuth Sync(Context context, boolean syncAll) {

		if (ConfigurationOperations.getKeyValue(ConfigurationKeys.key_sync_api,
				context).isEmpty()) {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_machine_demo, context).equals(
					"true")) {
				ConfigurationOperations.addConfiguration(
						ConfigurationKeys.key_sync_api,
						"http://ecomplianceapi-demo.opashareports.org/api/",
						context);
			} else {
				ConfigurationOperations
						.addConfiguration(ConfigurationKeys.key_sync_api,
								"http://ecomplianceapi.opashareports.org/api/",
								context);
			}
		}

		try {
			lastSync = Long.parseLong(AppStateConfigurationOperations
					.getKeyValue(AppStateConfigurationKeys.Key_Last_Sync,
							context));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}

		updateVisitedPatientData(context);

		if (GenUtils.IsInternetConnected(context)) {
			if (GenUtils.CheckServerRunning(context)) {
				machineAuth = getMachneAuth(context);
				if (machineAuth.errorMessage.equals("3")) {
					return machineAuth;
				} else if (machineAuth.errorMessage.equals("1")) {
					return machineAuth;
				}
				// Save GPS Location Data on every sync
				GpsTracker tracker = new GpsTracker(context);
				if (tracker.canGetLocation()) {
					MachineLocationOperations.add(context,
							tracker.getLatitude(), tracker.getLongitude());
				}
				tracker = null;
				if (machineAuth.activationPending) {
					if (machineAuth.isActive) {
						// new machine sync
						updateMasterTables(context);
						updateConfiguration(context);
						updateOperatorUSSD(context);
						updateActivationKey(context);
						updateLastSync(context);

					}
				} else if (!machineAuth.activationPending) {
					if (machineAuth.isActive) {
						// regular sync
						updateMasterTables(context);
						updateConfiguration(context);
						updateOperatorUSSD(context);
						long currTime = System.currentTimeMillis();
						if (ConfigurationOperations.getKeyValue(
								ConfigurationKeys.key_is_backup, context)
								.equals("1")) {
							new SendBackup(context);
							ConfigurationOperations.deleteKey(
									ConfigurationKeys.key_is_backup, context);
							return machineAuth;
						}

						getPatients(context, syncAll);
						getIris(context, syncAll);
						if (!syncAll) {
							sendPatients(context);
							sendIris(context);
						}
						sendScanIdentification(context);
						getVisitors(context, syncAll);
						if (!syncAll)
							sendVisitor(context);
						sendLocations(context);
						if (ConfigurationOperations.getKeyValue(
								ConfigurationKeys.key_is_positive_con_enable,
								context).equals("1"))
							getPositiveContact(context, syncAll);
						if (!syncAll)
							sendPositiveContact(context);
						sendLogs(context);
						updatePatientIcons(context);
						if (!machineAuth.machine_locked) {
							updateActivationKey(context);
						}
						updateLastSync(context, currTime, true); // Use time
						// when the
						// sync started
						updateVisitedPatientData(context);

					} else if (!machineAuth.isActive) {
						// Stop application
						sendPatients(context);
						sendScanIdentification(context);
						sendVisitor(context);
						sendLocations(context);
						sendLogs(context);
						sendIris(context);
						deleteAllTables(context);
					}
				}
				ArrayList<String> tem = MasterIconOperation.getIcons(
						Schema.MASTER_ICON_USED + " =0", context);
				for (String n : tem) {
					if (!PatientIconOperation.getId(n, context).isEmpty()) {
						MasterIconOperation.updateIsUsed(n, true, context);
					}
				}
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_Is_Inactive_patient_delete,
						context).equals("1")) {
					long days = System.currentTimeMillis();
					try {
						days = System.currentTimeMillis()
								- (86400000 * (Long
										.parseLong(ConfigurationOperations
												.getKeyValue(
														ConfigurationKeys.key_Inactive_patient_delete_after_days,
														context))));
					} catch (Exception e) {
						days = System.currentTimeMillis();
					}
					ArrayList<Patient> list = PatientsOperations.getPatients(
							Schema.PATIENTS_STATUS
									+ "<>'"
									+ Enums.StatusType
											.getStatusType(StatusType.Active)
									+ "' and " + Schema.PATIENTS_IS_DELETED
									+ " =0 and "
									+ Schema.PATIENTS_CREATION_TIMESTAMP
									+ " < " + days, context);
					for (Patient p : list) {
						DbUtils.hardDeletePatient(p.treatmentID, context);
					}
				}
				MasterIconOperation.deleteUnusedIcons(context);
				((eComplianceApp) context.getApplicationContext())
						.setAfterSyncMisc(context);
				if (((eComplianceApp) context.getApplicationContext()).IsAppTextFree) {
					GenUtils.updateIsUsedMasterIcons(lastSync, context);
					ArrayList<String> lists = MasterIconOperation.getIcons(
							Schema.MASTER_ICON_DOWNLOADED + "=0", context);
					if (lists.size() == 0) {
						((eComplianceApp) context.getApplicationContext()).IsResourceDownloaded = true;
					} else {
						((eComplianceApp) context.getApplicationContext()).IsResourceDownloaded = false;
					}
				}
			} else {
				Logger.LogToDb(context, "Sync", "Server not responding.");
			}
		} else {
			Logger.LogToDb(context, "Sync", "Internet Not Connected.");
		}

		if (syncAll) {
			try {
				PatientsOperations.getActivePatientCount(context);
				TreatmentInStagesOperations.getTreatmentIdByRegimenIds("1",
						context);
				DoseAdminstrationOperations.getmissedCount("", context);
				HIVOperation.getResult("", context);
				PatientDosePriorOperations.getPatientDosePrior("", context);
				PatientGenderAgeOperations.getAgeGender("", context);
				PatientHospitalizationOperations
						.getHospitalisedPatient(context);
				PatientLabFollowUpOperations.getlastLabData("", context);
				PatientLabsOperation.getLabs("", context);
				PatientV2_Operations.getPatients("", context);
				PositiveContactsOperations.getPendingContactList(context);
				ScansROperations.getScansR(context);
				ScansOperations.getScans(context, "");
				ScansIrisR_Operations.getScansIrisR(context);
				ScansIrisOperations.getScans(context, "");
				VisitorsOperations.getVisitor("", context);
				VideosCategoryOperations.GetVideoList("", "", "", context);
				VideosOperations.GetVideoList(context);
				ECounselingOperations.maxVideoId("", "", "", context);
			} catch (Exception e) {
			}
		}

		return machineAuth;
	}

	public static MachineAuth Send(Context context) {

		if (ConfigurationOperations.getKeyValue(ConfigurationKeys.key_sync_api,
				context).isEmpty()) {
			if (ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_is_machine_demo, context).equals(
					"true")) {
				ConfigurationOperations.addConfiguration(
						ConfigurationKeys.key_sync_api,
						"http://ecomplianceapi-demo.opashareports.org/api/",
						context);
			} else {
				ConfigurationOperations
						.addConfiguration(ConfigurationKeys.key_sync_api,
								"http://ecomplianceapi.opashareports.org/api/",
								context);
			}
		}

		try {
			lastSync = Long.parseLong(AppStateConfigurationOperations
					.getKeyValue(AppStateConfigurationKeys.Key_Last_Sync,
							context));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}

		if (GenUtils.IsInternetConnected(context)) {
			if (GenUtils.CheckServerRunning(context)) {
				machineAuth = getMachneAuth(context);
				if (machineAuth.errorMessage.equals("3")) {
					return machineAuth;
				} else if (machineAuth.errorMessage.equals("1")) {
					return machineAuth;
				}
				// Save GPS Location Data on every send
				GpsTracker tracker = new GpsTracker(context);
				if (tracker.canGetLocation()) {
					MachineLocationOperations.add(context,
							tracker.getLatitude(), tracker.getLongitude());
				}
				tracker = null;
				if (machineAuth.activationPending) {
					return machineAuth;
				} else if (!machineAuth.activationPending) {
					if (machineAuth.isActive) {
						sendPatients(context);
						sendScanIdentification(context);
						sendVisitor(context);
						sendLocations(context);
						sendPositiveContact(context);
						sendLogs(context);
						sendFinalize(context);
						sendIris(context);
					}
				}
			} else {
				Logger.LogToDb(context, "Send", "Server not responding.");
			}
		} else {
			Logger.LogToDb(context, "Send", "Internet Not Connected.");
		}
		return machineAuth;
	}

	/*
	 * function to update the patient doses if it is in VisitedToday list but
	 * not in DoseAdmin Table
	 */
	private static void updateVisitedPatientData(Context context) {

		long timerDuration = 0;
		try {
			String key = AppStateConfigurationOperations.getKeyValue(
					AppStateConfigurationKeys.key_user_auto_sync_duration,
					context);
			if (key.equals("Server")) {
				timerDuration = Long.parseLong(ConfigurationOperations
						.getKeyValue(ConfigurationKeys.key_auto_sync_duration,
								context));
			} else {
				timerDuration = Long
						.parseLong(AppStateConfigurationOperations
								.getKeyValue(
										AppStateConfigurationKeys.key_user_auto_sync_duration,
										context));
			}
		} catch (NumberFormatException e) {
		}
		// Start Receiver only when duration is more than 0 milliseconds.
		if (timerDuration == 0) {
			return;
		}

		if (((eComplianceApp) context.getApplicationContext()).currentDate != GenUtils
				.getCurrentDateLong()) {
			((eComplianceApp) context.getApplicationContext()).currentDate = GenUtils
					.getCurrentDateLong();
			GenUtils.setAppMissedDose(context);
			GenUtils.setAppVisitedToday(context);
			GenUtils.setAppPendingToday(context);
			GenUtils.setHospitalisedPatient(context);
			return;
		}

		ArrayList<String> treatmentId = ((eComplianceApp) context
				.getApplicationContext()).visitToday;
		for (int i = 0; i < treatmentId.size(); i++) {
			if (!DoseAdminstrationOperations.IsTodayExists(treatmentId.get(0)
					.toString(), context)) {
				String lastLoginId = ((eComplianceApp) context
						.getApplicationContext()).LastLoginId;
				GpsTracker gps = new GpsTracker(context);
				DoseUtils.AddDose(treatmentId.get(i), Enums.DoseType.Supervised
						.toString(), GenUtils.getCurrentDateLong(),
						TreatmentInStagesOperations.getPatientRegimenId(
								treatmentId.get(i), context), System
								.currentTimeMillis(), lastLoginId, gps
								.getLatitude(), gps.getLongitude(), context);
				generateCpSelfAdminDoses(treatmentId.get(i), gps, lastLoginId,
						context);
				FutureMissedDoseOperations.doseSoftDeleteForPatient(
						treatmentId.get(i), context); // Delete all future
				// missed
			}
		}
	}

	/* function used for generateSelfAdminDosed */
	public static void generateCpSelfAdminDoses(String treatmentId,
			GpsTracker gps, String lastLoginId, Context context) {
		Master regimen = RegimenMasterOperations.getRegimen(
				TreatmentInStagesOperations.getPatientRegimenId(treatmentId,
						context), context);
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_enable_user_self_admin, context).equals(
				"1")) {
			if (regimen.numSelfAdmin == 0)
				return;
			ArrayList<Patient> doses = DoseAdminstrationOperations.getdoses(
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "' and "
							+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID + "="
							+ regimen.ID, context);
			if (doses.size() == 1)
				return;
			for (int i = doses.size() - 2; i >= 0; i--) {
				if (doses.get(i).doseType.equals("SelfAdministered"))
					return;
				if (doses.get(i).doseType.equals("Supervised"))
					break;
			}
			long lastSupervisedDoseDate = 0;

			if (doses.get(doses.size() - 2).doseType.equals("Supervised")) {
				lastSupervisedDoseDate = doses.get(doses.size() - 2).doseDate;
			} else {
				for (int j = doses.size() - 2; j > 0; j--) {
					if (doses.get(j).doseType.equals("Supervised")) {
						if (doses.get(j).doseDate > lastSupervisedDoseDate)
							lastSupervisedDoseDate = doses.get(j).doseDate;
					}
				}
			}
			if (lastSupervisedDoseDate == 0)
				return;

		} else {
			ArrayList<Patient> doses = DoseAdminstrationOperations.getdoses(
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "' and "
							+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID + "="
							+ regimen.ID, context);
			if (doses.size() == 0)
				return;
			long doseDate = GenUtils.getCurrentDateLong();
			for (int i = 0; i < regimen.numSelfAdmin; i++) {
				doseDate += (regimen.selfAdminFreq * 86400000);
				if (!regimen.selfAdminSunday) {
					if (GenUtils.dateToDay(doseDate) <= 2) {
						doseDate += 86400000;
					}
				}
				if (!regimen.catagory.equals("MDR")) {
					DoseUtils.AddDose(treatmentId,
							Enums.DoseType.SelfAdministered.toString(),
							doseDate, regimen.ID, System.currentTimeMillis(),
							lastLoginId, gps.getLatitude(), gps.getLongitude(),
							context);
				}

			}
		}
	}

	private static void sendPositiveContact(Context context) {
		SavePositiveContactModel PosContacts = new SavePositiveContactModel();
		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {
			PosContacts = PositiveContactsOperations.dataSync(
					Schema.POSTIVE_CONTACT_CREATION_TIME + " > " + lastSync
							+ " and " + Schema.POSTIVE_CONTACT_CREATION_TIME
							+ " <=" + endDate, context);
		} else {
			PosContacts = PositiveContactsOperations.dataSync(
					Schema.POSTIVE_CONTACT_CREATION_TIME + " > " + lastSync,
					context);
		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_POSITIVE_CONTACT, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(PosContacts);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			// entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			// entity.setContentType("application/json");
			request.setEntity(entity);
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			// int code = httpResp.getStatusLine().getStatusCode();
			HttpResponse httpResp = httpClient.execute(request);
			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
				lastSyncUpdateAtClient = false;
			}
		} catch (Exception e) {
			lastSyncUpdateAtClient = false;
		}
	}

	private static void getPositiveContact(Context context, boolean syncAll) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQueryCompressed(SYNC_GET_POSITIVE_CONTACT,
					context)
					+ "machineId="
					+ machineAuth.machineId
					+ "&syncAll=";
			if (AppStateConfigurationOperations.getKeyValue(
					AppStateConfigurationKeys.key_Sync_All, context)
					.equals("1")) {
				query = query + "1";
			} else {
				query = query + "0";
			}
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			// InputStream stream = entity.getContent();
			int contentLength = (int) entity.getContentLength();
			if (contentLength != 0) {
				char[] buffer = new char[contentLength];
				InputStream stream = entity.getContent();
				InputStreamReader reader = new InputStreamReader(stream);
				int hasRead = 0;
				while (hasRead < contentLength)
					hasRead += reader.read(buffer, hasRead, contentLength
							- hasRead);
				stream.close();
				// JsonReader reader = new JsonReader(new
				// InputStreamReader(stream));
				Gson gson = new Gson();
				PositiveContacts model = gson.fromJson(new String(buffer),
						PositiveContacts.class);
				for (ContactDeatils v : model.PosContacts) {
					PositiveContactsOperations.add(v.Contact_Id, v.Name,
							v.Address, v.Age, v.Gender, v.Phone, v.Ref_Id,
							v.Ref_Phone, v.Treatment_id,
							GenUtils.getTimeFromTicks(v.Creation_Timestamp),
							v.Is_Deleted, v.CreatedBy, v.Center_Id, v.refName,
							context);
				}
			}
		} catch (Exception e) {
			lastSyncUpdateAtServer = false;
		}
	}

	private static void updateOperatorUSSD(Context context) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQueryCompressed(SYNC_GET_USSD, context)
					+ "machineId=" + machineAuth.machineId;
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			// InputStream stream = entity.getContent();
			int contentLength = (int) entity.getContentLength();

			if (contentLength != 0) {
				char[] buffer = new char[contentLength];
				InputStream stream = entity.getContent();
				InputStreamReader reader = new InputStreamReader(stream);

				int hasRead = 0;
				while (hasRead < contentLength)
					hasRead += reader.read(buffer, hasRead, contentLength
							- hasRead);
				stream.close();

				// JsonReader reader = new JsonReader(new
				// InputStreamReader(stream));
				Gson gson = new Gson();
				USSDSyncModel model = gson.fromJson(new String(buffer),
						USSDSyncModel.class);
				for (NetworkOperator v : model.USSD) {
					NetworkOperatorOperation.addOperator(v.Name, v.Number,
							v.IsActive, context);
				}
			}
		} catch (Exception e) {
		}
	}

	private static String GenerateQuery(String path, Context context) {
		return ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_sync_api, context) + path;

	}

	public static String GenerateQueryCompressed(String path, Context context) {
		String apiPath = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_sync_api, context);
		return apiPath + path;
	}

	private static void updatePatientIcons(Context context) {
		String icons = MasterIconOperation.getAllIcons(context);
		int freeIcons = MasterIconOperation.getFreeIconsCount(context);
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQueryCompressed(SYNC_ICONS, context)
					+ "machineId=" + machineAuth.machineId + "&iconsIds="
					+ icons + "&freeIcons=" + freeIcons;
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			// InputStream stream = entity.getContent();
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
				Gson gson = new Gson();
				MasterResources model = gson.fromJson(new String(buffer),
						MasterResources.class);
				for (Master_Resource res : model.resource) {
					if (res.Is_Active)
						MasterIconOperation.addIcon(res.ID, res.Resource_Name,
								false, false, context);
				}
			}
		} catch (Exception e) {
			// lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	public static void sendQuickLocations(Context context) {

		MachineLocations locationList = new MachineLocations();
		locationList.locations = MachineLocationOperations.get(null, context);
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_LOCATIONS, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(locationList);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			request.setEntity(entity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(request);
			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
			}
			MachineLocationOperations.deleteAll(context);
		} catch (Exception e) {
			// lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	private static void sendLocations(Context context) {
		MachineLocations locationList = new MachineLocations();
		long lastSync = 0;
		try {
			lastSync = Long.parseLong(AppStateConfigurationOperations
					.getKeyValue(AppStateConfigurationKeys.Key_Last_Sync,
							context));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}
		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {
			locationList.locations = MachineLocationOperations.get(
					Schema.MACHINE_LOCATION_CREATION_TIMESTAMP + ">" + lastSync
							+ " and "
							+ Schema.MACHINE_LOCATION_CREATION_TIMESTAMP + "<="
							+ endDate, context);
		} else {
			locationList.locations = MachineLocationOperations.get(
					Schema.MACHINE_LOCATION_CREATION_TIMESTAMP + " > "
							+ lastSync, context);
		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_LOCATIONS, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(locationList);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			// entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			// entity.setContentType("application/json");
			request.setEntity(entity);
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(request);

			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
				lastSyncUpdateAtClient = false;
			} else {
				MachineLocationOperations.deleteAll(context);
			}
		} catch (Exception e) {
			lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	private static void sendLogs(Context context) {
		ClientLoggerViewModel clientLogs = new ClientLoggerViewModel();

		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {
			clientLogs = LoggerOperations.get(Schema.LOGGER_TIMESTAMP + ">"
					+ lastSync + " and " + Schema.LOGGER_TIMESTAMP + "<="
					+ endDate, context);
		} else {
			clientLogs = LoggerOperations.get(Schema.LOGGER_TIMESTAMP + " > "
					+ lastSync, context);
		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_LOGS, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(clientLogs);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			entity.setContentType("application/json");
			request.setEntity(entity);
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(request);

			if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT)
				LoggerOperations.emptyTable(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void updateActivationKey(Context context) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(GenerateQueryCompressed(
					SYNC_SET_AUTH, context)
					+ "machineId="
					+ machineAuth.machineId
					+ "&androidId="
					+ Secure.getString(context.getContentResolver(),
							Secure.ANDROID_ID) + "&isActive=1");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse httpResp = client.execute(request);

			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
				lastSyncUpdateAtClient = false;

		} catch (IOException e) {
			lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	private static void updateLastSync(Context context) {
		try {
			if (lastSyncUpdateAtServer) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(GenerateQueryCompressed(
						SYNC_SET_LAST_SYNC, context)
						+ "machineId="
						+ machineAuth.machineId);
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");
				request.setHeader("Accept-Encoding", "gzip");
				// get the response
				client.execute(request);
			}
			if (lastSyncUpdateAtClient)
				AppStateConfigurationOperations.addAppStateConfiguration(
						AppStateConfigurationKeys.Key_Last_Sync,
						String.valueOf(System.currentTimeMillis()), context);
			return;
		} catch (IOException e) {
			lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	/**
	 * Set Last Sync - Overloaded
	 * 
	 * @param context
	 *            - Context
	 * @param lastSyncTime
	 *            - Last Sync Time in milliseconds
	 */
	private static void updateLastSync(Context context, long lastSyncTime,
			boolean isFromClient) {
		try {
			if (lastSyncUpdateAtServer) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(GenerateQueryCompressed(
						SYNC_SET_LAST_SYNC, context)
						+ "machineId="
						+ machineAuth.machineId);
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");
				request.setHeader("Accept-Encoding", "gzip");
				// get the response
				client.execute(request);
			}
			if (isFromClient)
				if (lastSyncUpdateAtClient)
					AppStateConfigurationOperations.addAppStateConfiguration(
							AppStateConfigurationKeys.Key_Last_Sync,
							String.valueOf(lastSyncTime), context);
			return;
		} catch (IOException e) {
			lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	private static void sendFinalize(Context context) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(GenerateQueryCompressed(
					SEND_FINALIZE, context)
					+ "machineId="
					+ machineAuth.machineId);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			client.execute(request);
		} catch (IOException e) {
			lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}
	}

	private static void deleteAllTables(Context context) {
		PatientsOperations.emptyTable(context);
		TreatmentInStagesOperations.emptyTable(context);
		DoseAdminstrationOperations.emptyTable(context);
		InitialCounselingOperations.emptyTable(context);
		LocationOperations.emptyTable(context);
		ScansOperations.emptyTable(context);
		VisitorsOperations.emptyTable(context);
		VisitorLoginOperations.emptyTable(context);
		RegimenMasterOperations.emptyTable(context);
		ConfigurationOperations.emptyTable(context);
		CentersOperations.emptyTable(context);
	}

	private static void getVisitors(Context context, boolean syncAll) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQueryCompressed(SYNC_GET_VISITOR, context)
					+ "machineId=" + machineAuth.machineId + "&syncAll=";
			if (AppStateConfigurationOperations.getKeyValue(
					AppStateConfigurationKeys.key_Sync_All, context)
					.equals("1")) {
				query = query + "1";
			} else {
				query = query + "0";
			}
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			// InputStream stream = entity.getContent();
			int contentLength = (int) entity.getContentLength();

			if (contentLength != 0) {
				char[] buffer = new char[contentLength];
				InputStream stream = entity.getContent();
				InputStreamReader reader = new InputStreamReader(stream);

				int hasRead = 0;
				while (hasRead < contentLength)
					hasRead += reader.read(buffer, hasRead, contentLength
							- hasRead);
				stream.close();

				// JsonReader reader = new JsonReader(new
				// InputStreamReader(stream));
				Gson gson = new Gson();
				VisitorsGetViewModel model = gson.fromJson(new String(buffer),
						VisitorsGetViewModel.class);
				if (syncAll) {
					VisitorsOperations.bulkInsert(model.Visitors, context);
					ScansOperations.bulkInsertVisitor(model.Scans, context);
				} else {
					for (VisitorViewModel v : model.Visitors) {
						VisitorsOperations
								.addVisitor(
										v.Visitor_Id,
										v.Name,
										v.Visitor_Type,
										GenUtils.getTimeFromTicks(v.Registration_Date),
										v.Status,
										String.valueOf(v.Phone_Number),
										v.Is_Authenticated,
										GenUtils.getTimeFromTicks(v.Creation_TimeStamp),
										v.Created_By, v.Is_Deleted, v.Tab_id,
										context);
					}
					for (org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.ScansViewModel s : model.Scans) {
						String[] tempScan = s.Scan1.split(",");
						byte[] scanData = new byte[tempScan.length];
						for (int j = 0; j < tempScan.length; j++) {
							scanData[j] = Byte.valueOf(tempScan[j]);
						}
						ScansOperations
								.addScan(
										s.Treatment_Id,
										"",
										scanData,
										GenUtils.getTimeFromTicks(s.Creation_TimeStamp),
										s.Created_By, context);
					}
				}
			}
		} catch (Exception e) {
			lastSyncUpdateAtServer = false;
		}
	}

	private static void sendVisitor(Context context) {
		VisitorDetails visitorList = new VisitorDetails();

		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {
			visitorList.Visitors = VisitorsOperations.visitorSync(
					Schema.VISITORS_CREATION_TIMESTAMP + ">" + lastSync
							+ " and " + Schema.VISITORS_CREATION_TIMESTAMP
							+ "<=" + endDate, context);
			visitorList.VisitorLogins = VisitorLoginOperations
					.VisitorloginsSync(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP
							+ " >" + lastSync + " and "
							+ Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP + " <= "
							+ endDate, context);
		} else {
			visitorList.Visitors = VisitorsOperations.visitorSync(
					Schema.VISITORS_CREATION_TIMESTAMP + " > " + lastSync,
					context);
			visitorList.VisitorLogins = VisitorLoginOperations
					.VisitorloginsSync(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP
							+ " > " + lastSync, context);
		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_VISITOR, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(visitorList);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			// entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			// entity.setContentType("application/json");
			request.setEntity(entity);
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(request);

			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
				lastSyncUpdateAtClient = false;
		} catch (Exception e) {
			lastSyncUpdateAtClient = false;
			e.printStackTrace();
		}

	}

	private static void getIris(Context context, boolean syncAll) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQueryCompressed(SYNC_GET_IRIS, context)
					+ "machineId=" + machineAuth.machineId + "&syncAll=";
			if (AppStateConfigurationOperations.getKeyValue(
					AppStateConfigurationKeys.key_Sync_All, context)
					.equals("1")) {
				query = query + "1";
			} else {
				query = query + "0";
			}
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			// InputStream stream = entity.getContent();
			int contentLength = (int) entity.getContentLength();

			if (contentLength != 0) {
				char[] buffer = new char[contentLength];
				InputStream stream = entity.getContent();
				InputStreamReader reader = new InputStreamReader(stream);

				int hasRead = 0;
				while (hasRead < contentLength)
					hasRead += reader.read(buffer, hasRead, contentLength
							- hasRead);
				stream.close();

				// JsonReader reader = new JsonReader(new
				// InputStreamReader(stream));
				Gson gson = new Gson();
				com.google.gson.stream.JsonReader jreader = new com.google.gson.stream.JsonReader(
						new StringReader(new String(buffer)));
				jreader.setLenient(true);
				PatientsViewModel model = gson.fromJson(jreader,
						PatientsViewModel.class);
				if (syncAll) {
					ScansIrisOperations.bulkInsert(model.ScanIris, context);
				} else {
					for (ScansIrisViewModel sv : model.ScanIris) {
						String[] tempScan = sv.Scan.split(",");
						byte[] scanData = new byte[tempScan.length];
						for (int j = 0; j < tempScan.length; j++) {
							scanData[j] = Byte.valueOf(tempScan[j]);
						}
						ScansIrisOperations
								.addScan(
										sv.Treatment_Id,
										sv.IrisEye,
										scanData,
										GenUtils.getTimeFromTicks(sv.Creation_TimeStamp),
										sv.Created_By, context);
					}
				}
			}
		} catch (Exception e) {
			lastSyncUpdateAtServer = false;
		}
	}

	private static void getPatients(Context context, boolean syncAll) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String query = GenerateQueryCompressed(SYNC_GET_PATIENT, context)
					+ "machineId=" + machineAuth.machineId + "&syncAll=";
			if (AppStateConfigurationOperations.getKeyValue(
					AppStateConfigurationKeys.key_Sync_All, context)
					.equals("1")) {
				query = query + "1";
			} else {
				query = query + "0";
			}
			HttpGet request = new HttpGet(query);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.addHeader("Accept-Encoding", "gzip");

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
				Gson gson = new Gson();
				// Add JsonReader to remove some extra special characters
				com.google.gson.stream.JsonReader jreader = new com.google.gson.stream.JsonReader(
						new StringReader(new String(buffer)));
				jreader.setLenient(true);
				PatientsViewModel model = gson.fromJson(jreader,
						PatientsViewModel.class);

				if (syncAll) {
					PatientsOperations.bulkInsert(model.Patients, context);
					PatientGenderAgeOperations.bulkInsert(
							model.PatientAgeGender, context);
					TreatmentInStagesOperations.bulkInsert(
							model.TreatmentInStageses, context);
					LocationOperations.bulkInsert(model.PatientLocations,
							context);
					DoseAdminstrationOperations.bulkInsert(model.DoseAdmins,
							context);
					FutureMissedDoseOperations.bulkInsert(
							model.FutureMissedDose, context);

					HIVOperation.bulkInsert(model.PatientHIV, context);
					PatientDiabetesOperations.bulkInsert(model.PatientDiabetes,
							context);
					ScansOperations.bulkInsert(model.Scans, context);
					PatientDosePriorOperations.bulkInsert(
							model.PatientTakenDose, context);
					PatientHospitalizationOperations.bulkInsert(
							model.PatientHospitalization, context);
					PatientLabFollowUpOperations.bulkInsert(model.LabFollowUp,
							context);
					PatientV2_Operations.bulkInsert(model.PatientOtherDetails,
							context);
				} else {
					for (PatientViewModel patient : model.Patients) {
						PatientsOperations
								.addPatient(
										patient.Treatment_Id,
										patient.Name,
										patient.Status,
										String.valueOf(patient.Phone_Number),
										patient.Tab_Id,
										patient.Is_Counseling_Pending,
										GenUtils.getTimeFromTicks(patient.Creation_TimeStamp),
										patient.Created_By,
										patient.Is_Deleted,
										GenUtils.getTimeFromTicks(patient.Registration_Date),
										patient.Center_Id,
										patient.PatientAddress, patient.EPSite,
										patient.DiseaseClassification,
										patient.PT_Type, patient.Nikshay_Id,
										patient.TBNo, patient.Hist_Smoking,
										context);
					}
					for (PatientAgeGender ag : model.PatientAgeGender) {
						PatientGenderAgeOperations
								.addGenderAge(
										ag.TreatmentId,
										ag.Gender,
										ag.Age,
										GenUtils.getTimeFromTicks(ag.Creation_Time_Stamp),
										ag.Is_Deleted, ag.Created_By, context);
					}
					for (TreatmentInStagesViewModel tis : model.TreatmentInStageses) {
						TreatmentInStagesOperations
								.addTreatmentStage(
										tis.Treatment_Id,
										tis.Regimen_Id,
										GenUtils.getTimeFromTicks(tis.Start_Date),
										GenUtils.getTimeFromTicks(tis.Creation_TimeStamp),
										tis.Created_By, tis.Is_Deleted, context);
					}
					for (PatientLocationViewModel pl : model.PatientLocations) {
						LocationOperations.addLocation(context,
								pl.Treatment_Id, pl.Latitude, pl.Logitude);
					}

					for (DoseAdminViewModel da : model.DoseAdmins) {
						DoseUtils
								.AddDose(
										da.Treatment_Id,
										da.Dose_Type,
										GenUtils.getTimeFromTicks(da.Dose_Date),
										da.Regimen_Id,
										GenUtils.getTimeFromTicks(da.Creation_TimeStamp),
										da.Created_By, da.Latitude,
										da.Longitude, context);
					}

					for (FutureDoseViewModel fm : model.FutureMissedDose) {
						if (!FutureMissedDoseOperations.doseExist(
								fm.Treatment_Id,
								GenUtils.getTimeFromTicks(fm.Dose_Date),
								context)) {
							FutureMissedDoseOperations
									.addDose(
											fm.Treatment_Id,
											fm.Dose_Type,
											GenUtils.getTimeFromTicks(fm.Dose_Date),
											fm.Regimen_Id,
											GenUtils.getTimeFromTicks(fm.Creation_TimeStamp),
											fm.Created_By, fm.Latitude,
											fm.Longitude, context);
						}
					}

					for (PatientIconGet da : model.PatientIcons) {
						PatientIconOperation
								.addIcon(
										da.Treatment_Id,
										da.Icon,
										GenUtils.getTimeFromTicks(da.Creation_TimeStamp),
										da.Is_Active, context);
					}
					for (PatientHIV da : model.PatientHIV) {
						HIVOperation
								.Add(da.Treatment_ID,
										da.Final_Result,
										da.Screening_Result,
										da.Created_By,
										da.Is_Deleted,
										GenUtils.getTimeFromTicks(da.Creation_Timestamp),
										context);
					}

					for (PatientDiabetes d : model.PatientDiabetes) {
						PatientDiabetesOperations.add(d.Treatment_ID,
								d.Test_ID, d.Result, d.Andministered_By,
								GenUtils.getTimeFromTicks(d.Test_Date),
								d.Is_Deleted, context);
					}

					for (ScansViewModel sv : model.Scans) {
						String[] tempScan = sv.Scan1.split(",");
						byte[] scanData = new byte[tempScan.length];
						for (int j = 0; j < tempScan.length; j++) {
							scanData[j] = Byte.valueOf(tempScan[j]);
						}
						ScansOperations
								.addScan(
										sv.Treatment_Id,
										"",
										scanData,
										GenUtils.getTimeFromTicks(sv.Creation_TimeStamp),
										sv.Created_By, context);
					}

					for (PatientDoseTakenPriorViewModel pd : model.PatientTakenDose) {
						PatientDosePriorOperations
								.addPatient(
										pd.TreatmentID,
										String.valueOf(pd.TakenIpDoses),
										String.valueOf(pd.TakenExtIpDoses),
										String.valueOf(pd.TakenCpDoses),
										GenUtils.getTimeFromTicks(pd.CreationTimeStamp),
										pd.IsDeleted, context);
					}

					for (PatientHospitalizationViewModel ph : model.PatientHospitalization) {
						PatientHospitalizationOperations
								.addHospitalisedPatient(
										ph.ID,
										ph.TreatmentId,
										GenUtils.getTimeFromTicks(ph.StartDate),
										GenUtils.getTimeFromTicks(ph.EndDate),
										ph.Notes,
										ph.CreatedBy,
										GenUtils.getTimeFromTicks(ph.CreationTimeStamp),
										context);
					}

					for (Deleted_Scan delScan : model.DeletedScans) {
						ScansOperations.deleteScans(context,
								delScan.Treatment_Id);
					}

					for (PatientLabFollowup followUp : model.LabFollowUp) {
						PatientLabFollowUpOperations
								.addPatientLabFollowUpData(
										followUp.Lab_TreatmentId,
										followUp.labMonth,
										followUp.labResult,
										GenUtils.getTimeFromTicks(followUp.labDate),
										followUp.labDmc,
										followUp.labNo,
										followUp.labWeight,
										followUp.labCreated_By,
										followUp.labTabId,
										GenUtils.getTimeFromTicks(followUp.labCreationTome_Stamp),
										followUp.labIs_Deleted, context);
					}

					for (PatientDetails2ViewModel patientdetailsv2 : model.PatientOtherDetails) {
						PatientV2_Operations
								.addPatient(
										patientdetailsv2.Treatment_Id,
										patientdetailsv2.AdhaarNo,
										patientdetailsv2.PatientSource,
										GenUtils.getTimeFromTicks(patientdetailsv2.Creation_TimeStamp),
										patientdetailsv2.Created_By,
										patientdetailsv2.Is_Deleted, context);
					}
				}

			}

		} catch (Exception e) {
			lastSyncUpdateAtServer = false;
			Log.e("patinet get sync error-------", e.toString());
			e.printStackTrace();
		}
	}

	private static void sendIris(Context context) {
		PatientDetail allpatients = new PatientDetail();

		// Sync back 2 Days of data or number of milliseconds defined in Key
		// "key_sync_back_data_milli"
		long lastDoseMinTwoDays = lastSync - GenUtils.TWO_DAY; // DEFAULT SYNC
		// BACK DATA
		// FROM 2 DAYS
		if (((eComplianceApp) context.getApplicationContext()).syncBackDataMilli > 0) {
			lastDoseMinTwoDays = lastSync
					- ((eComplianceApp) context.getApplicationContext()).syncBackDataMilli;
		}

		if (lastDoseMinTwoDays < 0) {
			lastDoseMinTwoDays = 0;
		}

		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {

			allpatients.ScansIris = ScansIrisOperations.ScanSync(
					Schema.SCANS_IRIS_CREATION_TIMESTAMP + ">" + lastSync
							+ " and " + Schema.SCANS_IRIS_CREATION_TIMESTAMP
							+ " <= " + endDate, context);

		} else {

			allpatients.ScansIris = ScansIrisOperations.ScanSync(
					Schema.SCANS_IRIS_CREATION_TIMESTAMP + " > " + lastSync,
					context);

		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_IRIS, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			String jsonData = gson.toJson(allpatients);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			request.setEntity(entity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(request);

			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
				lastSyncUpdateAtClient = false;
		} catch (Exception e) {
			Log.e("IrisSync patinet error---------", e.toString());
			lastSyncUpdateAtClient = false;
		}
	}

	private static void sendPatients(Context context) {
		PatientDetail allpatients = new PatientDetail();

		// Sync back 2 Days of data or number of milliseconds defined in Key
		// "key_sync_back_data_milli"
		long lastDoseMinTwoDays = lastSync - GenUtils.TWO_DAY; // DEFAULT SYNC
		// BACK DATA
		// FROM 2 DAYS
		if (((eComplianceApp) context.getApplicationContext()).syncBackDataMilli > 0) {
			lastDoseMinTwoDays = lastSync
					- ((eComplianceApp) context.getApplicationContext()).syncBackDataMilli;
		}

		if (lastDoseMinTwoDays < 0) {
			lastDoseMinTwoDays = 0;
		}

		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {

			allpatients.Patients = PatientsOperations.patientSync(
					Schema.PATIENTS_CREATION_TIMESTAMP + " > " + lastSync
							+ " and " + Schema.PATIENTS_CREATION_TIMESTAMP
							+ "<= " + endDate, context);
			allpatients.patientDiabetes = PatientDiabetesOperations.dataSync(
					Schema.PATIENT_DIABETES_CREATION_TIMESTAMP + " > "
							+ lastSync + " and "
							+ Schema.PATIENT_DIABETES_CREATION_TIMESTAMP
							+ "<= " + endDate, context);
			allpatients.PatientAgeGender = PatientGenderAgeOperations
					.patientSync(Schema.PATIENT_AGE_GENDER_CREATIONTIME + " > "
							+ lastSync + " and "
							+ Schema.PATIENT_AGE_GENDER_CREATIONTIME + "<= "
							+ endDate, context);
			allpatients.PatientLocations = LocationOperations
					.patientLocationSync(Schema.LOCATION_TIMESTAMP + " >  "
							+ lastSync + " and " + Schema.LOCATION_TIMESTAMP
							+ "<=" + endDate, context);
			allpatients.TreatmentInStageses = TreatmentInStagesOperations
					.TreatmentInStageSync(
							Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP
									+ "> "
									+ lastSync
									+ " and "
									+ Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP
									+ " <= " + endDate, context);
			allpatients.DoseAdmins = DoseAdminstrationOperations.getdosesSync(
					Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP + " >"
							+ lastSync + " and "
							+ Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP
							+ "<= " + endDate, context);
			allpatients.FutureMissedDose = FutureMissedDoseOperations
					.getdosesSync(Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP
							+ " >" + lastSync + " and "
							+ Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP
							+ "<= " + endDate, context);
			allpatients.Scans = ScansOperations.ScanSync(
					Schema.SCANS_CREATION_TIMESTAMP + ">" + lastSync + " and "
							+ Schema.SCANS_CREATION_TIMESTAMP + " <= "
							+ endDate, context);
			allpatients.InitialCounselings = InitialCounselingOperations
					.counselingSync(Schema.INTIALCOUNSELING_START_TIME + ">"
							+ lastSync + " and "
							+ Schema.INTIALCOUNSELING_END_TIME + " <="
							+ endDate, context);
			allpatients.DoseUnsupervisedReasons = UnSupervisedDoseReasonOperations
					.ReasonSync(Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP
							+ " > " + lastSync + " and "
							+ Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP
							+ " <= " + endDate, context);
			allpatients.PatientLab = PatientLabsOperation.getLabs(
					Schema.LAB_CREATION_TIMESTAMP + ">" + lastSync + " and "
							+ Schema.LAB_CREATION_TIMESTAMP + "<=" + endDate,
					context);
			allpatients.PatientIcon = PatientIconOperation.getIcons(
					Schema.PATIENT_ICON_CREATION_TIMESTAMP + " > " + lastSync
							+ " and " + Schema.PATIENT_ICON_CREATION_TIMESTAMP
							+ " <= " + endDate, context);
			allpatients.PatientHiv = HIVOperation.resultSync(
					Schema.PATIENT_HIV_CREATION_TIMESTAMP + " > " + lastSync
							+ " and " + Schema.PATIENT_HIV_CREATION_TIMESTAMP
							+ " <= " + endDate, context);
			allpatients.Audits = QualityAuditingOperations.sync(
					Schema.AUDIT_QUESTION_CREATION_TIME + " > " + lastSync
							+ " and " + Schema.PATIENT_HIV_CREATION_TIMESTAMP
							+ " <= " + endDate, context);
			allpatients.AuditsSummary = QualityAuditingSummaryOperations.sync(
					Schema.AUDIT_QUESTION_SUM_CREATION_TIME + " > " + lastSync
							+ " and " + Schema.AUDIT_QUESTION_SUM_CREATION_TIME
							+ " <= " + endDate, context);

			allpatients.PatientDoseTakenPriors = PatientDosePriorOperations
					.patientDoseTakenSync(
							Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP
									+ " > "
									+ lastSync
									+ " and "
									+ Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP
									+ " <= " + endDate, context);
			allpatients.PatientInitialLabData = Patient_Initial_Lab_Data_Operations
					.patientInitialDataSync(
							Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP
									+ " > "
									+ lastSync
									+ " and "
									+ Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP
									+ " <= " + endDate, context);

			allpatients.SetPatientLabFollowUp = PatientLabFollowUpOperations
					.patientlabfollowUpSync(
							Schema.PATIENT_LAB_CREATION_TIMESTAMP + " > "
									+ lastSync + " and "
									+ Schema.PATIENT_LAB_CREATION_TIMESTAMP
									+ " <= " + endDate, context);
			allpatients.PatientOtherDetails = PatientV2_Operations.patientSync(
					Schema.PATIENT_CREATIONTIME_STAMP + " > " + lastSync
							+ " and " + Schema.PATIENT_CREATIONTIME_STAMP
							+ " <= " + endDate, context);
			allpatients.eCounselingvideos = ECounselingOperations
					.eCounselingSync(Schema.ECOUNSELING_SYNC_TIMESTAMP + " > "
							+ lastSync + " and "
							+ Schema.ECOUNSELING_SYNC_TIMESTAMP + " <= "
							+ endDate, context);

		} else {
			allpatients.Patients = PatientsOperations.patientSync(
					Schema.PATIENTS_CREATION_TIMESTAMP + " > "
							+ lastDoseMinTwoDays, context);
			allpatients.PatientAgeGender = PatientGenderAgeOperations
					.patientSync(Schema.PATIENT_AGE_GENDER_CREATIONTIME + " > "
							+ lastDoseMinTwoDays, context);
			allpatients.PatientLocations = LocationOperations
					.patientLocationSync(Schema.LOCATION_TIMESTAMP + " >  "
							+ lastDoseMinTwoDays, context);
			allpatients.TreatmentInStageses = TreatmentInStagesOperations
					.TreatmentInStageSync(
							Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP
									+ " > " + lastDoseMinTwoDays, context);

			allpatients.DoseAdmins = DoseAdminstrationOperations.getdosesSync(
					Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP + " > "
							+ lastDoseMinTwoDays, context);

			allpatients.Scans = ScansOperations
					.ScanSync(Schema.SCANS_CREATION_TIMESTAMP + " > "
							+ lastSync, context);
			allpatients.InitialCounselings = InitialCounselingOperations
					.counselingSync(Schema.INTIALCOUNSELING_END_TIME + " > "
							+ lastSync, context);
			allpatients.DoseUnsupervisedReasons = UnSupervisedDoseReasonOperations
					.ReasonSync(Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP
							+ " > " + lastDoseMinTwoDays, context);
			allpatients.PatientLab = PatientLabsOperation.getLabs(
					Schema.LAB_CREATION_TIMESTAMP + ">" + lastSync, context);
			allpatients.PatientIcon = PatientIconOperation.getIcons(
					Schema.PATIENT_ICON_CREATION_TIMESTAMP + " > " + lastSync,
					context);
			allpatients.PatientHiv = HIVOperation.resultSync(
					Schema.PATIENT_HIV_CREATION_TIMESTAMP + " > " + lastSync,
					context);
			allpatients.patientDiabetes = PatientDiabetesOperations.dataSync(
					Schema.PATIENT_DIABETES_CREATION_TIMESTAMP + " > "
							+ lastSync, context);
			allpatients.Audits = QualityAuditingOperations.sync(
					Schema.AUDIT_QUESTION_CREATION_TIME + " > " + lastSync,
					context);
			allpatients.AuditsSummary = QualityAuditingSummaryOperations.sync(
					Schema.AUDIT_QUESTION_SUM_CREATION_TIME + " > " + lastSync,
					context);

			allpatients.PatientDoseTakenPriors = PatientDosePriorOperations
					.patientDoseTakenSync(
							Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP
									+ " > " + lastDoseMinTwoDays, context);
			allpatients.PatientInitialLabData = Patient_Initial_Lab_Data_Operations
					.patientInitialDataSync(
							Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP
									+ " > " + lastSync, context);
			allpatients.SetPatientLabFollowUp = PatientLabFollowUpOperations
					.patientlabfollowUpSync(
							Schema.PATIENT_LAB_CREATION_TIMESTAMP + " > "
									+ lastSync, context);
			allpatients.PatientOtherDetails = PatientV2_Operations.patientSync(
					Schema.PATIENT_CREATIONTIME_STAMP + " > " + lastSync,
					context);
			allpatients.eCounselingvideos = ECounselingOperations
					.eCounselingSync(Schema.ECOUNSELING_SYNC_TIMESTAMP + " > "
							+ lastSync, context);

		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_PATIENT, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(allpatients);
			
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			// entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			// entity.setContentType("application/json");
			request.setEntity(entity);
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(request);

			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
				lastSyncUpdateAtClient = false;
		} catch (Exception e) {

			Log.e("sync patinet error---------", e.toString());
			lastSyncUpdateAtClient = false;
		}
	}

	private static void updateConfiguration(Context context) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(GenerateQueryCompressed(
					SYNC_CONFIGURATION, context)
					+ "machineId="
					+ machineAuth.machineId);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			JsonReader reader = new JsonReader(new InputStreamReader(stream));
			reader.beginArray();
			while (reader.hasNext()) {
				String key = "";
				String value = "";
				reader.beginObject();
				while (reader.hasNext()) {
					String name = reader.nextName();
					if (name.equals("Key")) {
						key = reader.nextString();
					} else if (name.equals("Value")) {
						value = reader.nextString();
					} else if (name.equals("MultipleValue")) {
						reader.nextBoolean();
					} else {
						reader.skipValue();
					}
				}
				if (key.equals("key_center_id_name")) {
					String[] center = value.split(",");
					int centerId = 0;
					try {
						centerId = Integer.parseInt(center[0]);
					} catch (Exception e) {
					}
					if (center.length == 4)
						CentersOperations.addCenter(centerId, center[1],
								center[2], center[3], context);
					else if (center.length == 3)
						CentersOperations.addCenter(centerId, center[1],
								center[2], "", context);
				} else if (key.equals("key_max_id")) {
					int tempVal = 0;
					try {
						tempVal = Integer.parseInt(value);
					} catch (Exception e) {
					}

					if (AppStateConfigurationOperations.getMaxId(context) <= tempVal) {
						AppStateConfigurationOperations
								.addAppStateConfiguration(key,
										String.valueOf(tempVal), context);
					}
				} else if (key.equals(AppStateConfigurationKeys.Key_Last_Sync)) {
					AppStateConfigurationOperations.addAppStateConfiguration(
							key, value, context);
				} else {
					ConfigurationOperations.addConfiguration(key, value,
							context);
				}
				reader.endObject();
			}
			reader.endArray();
			reader.close();
		} catch (Exception e) {
			lastSyncUpdateAtClient = false;
		}

	}

	private static void updateMasterTables(Context context) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(GenerateQueryCompressed(SYNC_MASTER,
					context) + "machineId=" + machineAuth.machineId);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// get the response
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			JsonReader reader = new JsonReader(new InputStreamReader(stream));
			// reader.beginArray();
			reader.beginObject();
			
			VideosCategoryOperations.emptyTable(context);
			VideosOperations.emptyTable(context);
			
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("RegimensV2")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateRegimen(reader, context);
					}
					reader.endArray();
				} else if (name.equals("UnsupervisedReasons")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateUnsupervisedReason(reader, context);
					}
					reader.endArray();
				} else if (name.equals("LabResults")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateLabResults(reader, context);
					}
					reader.endArray();
				} else if (name.equals("Status")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateStatus(reader, context);
					}
					reader.endArray();
				} else if (name.equals("AppResource")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateAppResource(reader, context);
					}
					reader.endArray();
				} else if (name.equals("DiabetesResult")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateMasterDiabetes(reader, context);
					}
					reader.endArray();

				} else if (name.equals("QaQuestionMaster")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateMasterQA(reader, context);
					}
					reader.endArray();

				} else if (name.equals("QaQuestionMasterSpn")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateMasterQASpn(reader, context);
					}
					reader.endArray();
				} else if (name.equals("VideoMasters")) {
					reader.beginArray();
					while (reader.hasNext()) {
						updateMasterVideo(reader, context);
					}
					reader.endArray();
				} else {

					reader.skipValue();
				}
			}
			reader.endObject();
			reader.close();
		} catch (Exception e) {
		}
	}

	private static void updateMasterQASpn(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String Resultname = "";
		String language = "";
		int priority = 0;
		int qusertionId = 0;

		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Question_Id")) {
				qusertionId = reader.nextInt();
			} else if (name.equals("Name")) {
				Resultname = reader.nextString();
			} else if (name.equals("ID")) {
				id = reader.nextInt();
			} else if (name.equals("Is_Active")) {
				isActive = reader.nextBoolean();
			} else if (name.equals("Language")) {
				language = reader.nextString();
			} else if (name.equals("Priority")) {
				priority = reader.nextInt();

			} else {
				reader.skipValue();
			}
		}
		MasterAuditQuesSpnValueOperations.AddQuestions(id, qusertionId,
				isActive, language, priority, Resultname, context);
		reader.endObject();

	}
	
	private static void updateMasterVideo(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		String stage = "";
		String isDaily = "";
		String Category = "";
		int priority = 0;
		String fileName = "";

		
		
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("FileName")) {
				fileName = reader.nextString();
			} else if (name.equals("Category")) {
				Category = reader.nextString();
			} else if (name.equals("Stage")) {
				stage = reader.nextString();
			} else if (name.equals("IsDaily")) {
				isDaily = reader.nextString();
			} else if (name.equals("Priority")) {
				priority = reader.nextInt();
			} else {
				reader.skipValue();
			}
		}
		VideosCategoryOperations.addVideos(fileName, Category, stage, isDaily, priority, context);
		VideosOperations.addVideos(fileName, fileName, context);
		reader.endObject();
	}

	private static void updateMasterQA(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String Resultname = "";
		int type = 0;
		int priority = 0;
		int parentId = 0;
		int userType = 0;
		String language = "";

		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Question_id")) {
				id = reader.nextInt();
			} else if (name.equals("Name")) {
				Resultname = reader.nextString();
			} else if (name.equals("Language")) {
				language = reader.nextString();
			} else if (name.equals("Question_Type")) {
				type = reader.nextInt();
			} else if (name.equals("Priority")) {
				priority = reader.nextInt();
			} else if (name.equals("Parent_Id")) {
				parentId = reader.nextInt();
			} else if (name.equals("User_Type")) {
				userType = reader.nextInt();
			} else if (name.equals("Is_Active")) {
				isActive = reader.nextBoolean();
			} else {
				reader.skipValue();
			}
		}
		Master_QA_Questions.AddQuestions(id, Resultname, isActive, language,
				type, priority, parentId, userType, context);
		reader.endObject();

	}

	private static void updateAppResource(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String ResourceName = "";
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				id = reader.nextInt();
			} else if (name.equals("IsActive")) {
				isActive = reader.nextBoolean();
			} else if (name.equals("ResourceName")) {
				ResourceName = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		if (isActive)
			ApplicationIconOperation.addIcon(id, ResourceName, false, false,
					context);
		reader.endObject();

	}

	private static void updateMasterDiabetes(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String Resultname = "";
		int testId = 0;
		double minValue = 0;
		double maxValue = 0;
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				id = reader.nextInt();
			} else if (name.equals("Name")) {
				Resultname = reader.nextString();
			} else if (name.equals("Test_Id")) {
				testId = reader.nextInt();
			} else if (name.equals("Min_Value")) {
				minValue = reader.nextDouble();
			} else if (name.equals("Max_Value")) {
				maxValue = reader.nextDouble();
			} else if (name.equals("Is_Active")) {
				isActive = reader.nextBoolean();
			} else {
				reader.skipValue();
			}
		}
		if (isActive)
			MasterDiabetesOperations.add(id, Resultname, minValue, maxValue,
					isActive, testId, context);
		reader.endObject();

	}

	private static void updateUnsupervisedReason(JsonReader reader,
			Context context) throws IOException {

		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String reason = "";
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				id = reader.nextInt();
			} else if (name.equals("IsActive")) {
				isActive = reader.nextBoolean();
			} else if (name.equals("Reason")) {
				reason = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		UnsupervisedReasonMasterOperations.addReason(id, reason, isActive,
				context);
		reader.endObject();
	}

	private static void updateLabResults(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String result = "";
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				id = reader.nextInt();
			} else if (name.equals("IsActive")) {
				isActive = reader.nextBoolean();
			} else if (name.equals("LabResult")) {
				result = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		InitialLabMasterOperations.addLab(id, result, isActive, context);
		reader.endObject();
	}

	private static void updateStatus(JsonReader reader, Context context)
			throws IOException {

		reader.beginObject();
		int id = 0;
		boolean isActive = false;
		String statusName = "";
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				id = reader.nextInt();
			} else if (name.equals("IsActive")) {
				isActive = reader.nextBoolean();
			} else if (name.equals("StatusName")) {
				statusName = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		PatientStatusMasterOperations.addPatientStatus(id, statusName,
				isActive, context);
		reader.endObject();
	}

	private static void updateRegimen(JsonReader reader, Context context)
			throws IOException {
		reader.beginObject();
		int activeDays = 0;
		String category = "";
		int daysFrequency = 0;
		boolean isActive = false;
		int priority = 0;
		int regimenId = 0;
		String schedule = "";
		String stage = "";

		int numSelfAdmin = 0;
		int numMissed = 0;
		int selfAdminFreq = 0;
		int missedFreq = 0;
		int unsupervisedFreq = 0;
		boolean selfAdminSunday = false;
		boolean missedSunday = false;

		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("ActiveDays")) {
				activeDays = reader.nextInt();
			} else if (name.equals("Category")) {
				category = reader.nextString();
			} else if (name.equals("DaysFrequency")) {
				daysFrequency = reader.nextInt();
			} else if (name.equals("IsActive")) {
				isActive = reader.nextBoolean();
			} else if (name.equals("Priority")) {
				priority = reader.nextInt();
			} else if (name.equals("RegimenId")) {
				regimenId = reader.nextInt();
			} else if (name.equals("Schedule")) {
				schedule = reader.nextString();
			} else if (name.equals("Stage")) {
				stage = reader.nextString();
			} else if (name.equals("SelfAdminNum")) {
				numSelfAdmin = reader.nextInt();
			} else if (name.equals("SelfAdminFreq")) {
				selfAdminFreq = reader.nextInt();
			} else if (name.equals("SelfAdminSundayInclusion")) {
				selfAdminSunday = reader.nextBoolean();
			} else if (name.equals("MissedDoseNum")) {
				numMissed = reader.nextInt();
			} else if (name.equals("MissedFreq")) {
				missedFreq = reader.nextInt();
			} else if (name.equals("MissedSundayInclusion")) {
				missedSunday = reader.nextBoolean();
			} else if (name.equals("UnsupervisedFrequency")) {
				unsupervisedFreq = reader.nextInt();
			} else {
				reader.skipValue();
			}
		}
		RegimenMasterOperations.addRegimen(regimenId, category, stage,
				daysFrequency, schedule, activeDays, priority, numSelfAdmin,
				selfAdminFreq, selfAdminSunday, numMissed, missedFreq,
				missedSunday, isActive, unsupervisedFreq, context);
		reader.endObject();
	}

	public static MachineAuth getMachneAuth(Context context) {
		// MachineAuth machineAuthTemp = new MachineAuth();
		try {
			DefaultHttpClient client = new DefaultHttpClient();

			HttpGet request = new HttpGet(GenerateQuery(SYNC_AUTHENTICATION,
					context)
					+ "machineId="
					+ Integer.parseInt(ConfigurationOperations.getKeyValue(
							ConfigurationKeys.key_Machine_Id, context))
					+ "&androidId="
					+ Secure.getString(context.getContentResolver(),
							Secure.ANDROID_ID));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			// request.setHeader("Accept-Encoding", "gzip");
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
				machineAuth = new MachineAuth();
				JSONObject auth = new JSONObject(new String(buffer));
				machineAuth.machineId = auth.getInt("MachineId");
				machineAuth.startDate = auth.getLong("StartDate");
				machineAuth.endDate = auth.getLong("EndDate");
				machineAuth.isActive = auth.getBoolean("IsActive");
				machineAuth.errorMessage = auth.getString("ErrorMessage");
				machineAuth.machine_locked = auth.getBoolean("Machine_Locked");
				machineAuth.activationPending = auth
						.getBoolean("ActivationPending");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return machineAuth;
	}

	private static void sendScanIdentification(Context context) {
		ScanLogs scanlogs = new ScanLogs();

		long endDate = GenUtils.getTimeFromTicks(machineAuth.endDate);
		if (endDate != 0
				&& GenUtils.getTimeFromTicks(machineAuth.endDate) > GenUtils
						.getTimeFromTicks(machineAuth.startDate)) {
			scanlogs.ScanIdentifications = Scan_Identification_Operations
					.ScanIdentifySync(Schema.IDENTIFY_CREATION_TIMESTAMP + ">"
							+ lastSync + " and "
							+ Schema.IDENTIFY_CREATION_TIMESTAMP + "<="
							+ endDate, context);

		} else {
			scanlogs.ScanIdentifications = Scan_Identification_Operations
					.ScanIdentifySync(Schema.IDENTIFY_CREATION_TIMESTAMP
							+ " > " + lastSync, context);

		}
		try {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.serializeNulls().setDateFormat(java.text.DateFormat.LONG)
					.setPrettyPrinting().setVersion(1.0).create();
			HttpPost request = new HttpPost(GenerateQueryCompressed(
					SYNC_SEND_SCANLOGS, context));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "gzip");
			// Build JSON string
			String jsonData = gson.toJson(scanlogs);
			StringEntity entity = new StringEntity(jsonData, "UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			entity.setContentType("application/json");
			request.setEntity(entity);
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}