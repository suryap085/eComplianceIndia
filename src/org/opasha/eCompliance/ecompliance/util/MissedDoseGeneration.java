/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.util.ArrayList;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbOperations.AppStateConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.FutureMissedDoseOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientDosePriorOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientHospitalizationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums.AppStateKeyValues;
import org.opasha.eCompliance.ecompliance.util.Enums.CategoryType;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.StageType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;

import android.content.Context;

/**
 * @author Ruchin Kumar
 * 
 */
public class MissedDoseGeneration {

	public static boolean generateMissedDoses(Context context) {

		String lastMissedDate = AppStateConfigurationOperations
				.getKeyValue(
						Enums.AppStateKeyValues.LastMissedDoseLoged.toString(),
						context);
		if (lastMissedDate.equals("")) {
			AppStateConfigurationOperations.addAppStateConfiguration(
					Enums.AppStateKeyValues.getAppKey(
							AppStateKeyValues.LastMissedDoseLoged).toString(),
					String.valueOf(System.currentTimeMillis()), context);
			return false;
		}
		long missedDoseDay = Long.parseLong(lastMissedDate);
		long diff = System.currentTimeMillis() - missedDoseDay;
		int diffDays = (int) (diff / (86400000));
		if (diffDays <= 0) {
			return false;
		}
		for (int i = 0; i < diffDays; i++) {
			ArrayList<String> temp = DoseAdminstrationOperations
					.getPendingListMissed(TreatmentInStagesOperations
							.getPendingList(
									RegimenMasterOperations.getRegimenIdForDay(
											missedDoseDay, context), context),
							missedDoseDay, context);
			GpsTracker gps = new GpsTracker(context);
			for (String p : temp) {
				boolean generateMissedDose = true;

				Master regimen = TreatmentInStagesOperations.getPatientRegimen(
						p, context);
				ArrayList<Patient> patientlist = PatientsOperations
						.getPatients(Schema.PATIENTS_TREATMENT_ID + "= '" + p
								+ "' and " + Schema.PATIENTS_IS_DELETED + "=0",
								context);
				ArrayList<PatientDoseTakenPriorViewModel> patientdosetakenlist = PatientDosePriorOperations
						.getPatientDosePrior(Schema.PATIENT_DOSETAKEN_PRIOR_ID
								+ "= '" + p + "' and "
								+ Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED
								+ "=0", context);
				int DoseCount = PatientDosePriorOperations
						.getIP_ExIP_DosesCount(p, regimen.catagory,
								regimen.stage,
								Enums.DoseType.getDoseType(DoseType.Supervised)
										.toString(), context)
						+ PatientDosePriorOperations.getIP_ExIP_DosesCount(p,
								regimen.catagory, regimen.stage, Enums.DoseType
										.getDoseType(DoseType.Unsupervised)
										.toString(), context)
						+ PatientDosePriorOperations.getSelfAdmidDosesCount(p,
								regimen.catagory, regimen.stage, Enums.DoseType
										.getDoseType(DoseType.SelfAdministered)
										.toString(), context);
				if (regimen.catagory.equals(Enums.CategoryType
						.getCategoryType(CategoryType.CAT1))) {
					if (regimen.stage.equals(Enums.StageType
							.getStageType(StageType.CP))) {
						try {
							DoseCount = DoseCount
									+ patientdosetakenlist.get(0).TakenCpDoses;
						} catch (Exception e) {
						}
						if (regimen.daysFrequency == 1) {
							if (DoseCount >= IntentKeys.key_catI_CP_Daily) {
								generateMissedDose = false;
							}
						} else {
							if (DoseCount >= 18) {
								generateMissedDose = false;
							}
						}

					}
				} else if (regimen.catagory.equals(Enums.CategoryType
						.getCategoryType(CategoryType.CAT2))) {

					if (regimen.stage.equals(Enums.StageType
							.getStageType(StageType.CP))) {
						try {
							DoseCount = DoseCount
									+ patientdosetakenlist.get(0).TakenCpDoses;
						} catch (Exception e) {
						}
						if (regimen.daysFrequency == 1) {
							if (DoseCount >= IntentKeys.key_catII_CP_Daily) {
								generateMissedDose = false;
							}
						} else {
							if (DoseCount >= 24) {
								generateMissedDose = false;
							}
						}

					}
				} 

				if (generateMissedDose) {
					if (PatientHospitalizationOperations
							.HospitalisedPatientExists(p, context)) {
						if (!PatientHospitalizationOperations
								.getHospitalisedPatientDateExistsForDay(p,
										missedDoseDay, context)) {
							DoseUtils
									.AddDose(
											p,
											Enums.DoseType.getDoseType(
													DoseType.Missed).toString(),
											GenUtils.dateTimeToDate(missedDoseDay),
											regimen.ID,
											System.currentTimeMillis(),
											((eComplianceApp) context
													.getApplicationContext()).LastLoginId,
											gps.getLatitude(), gps.longitude,
											context);
						}
					} else {
						DoseUtils.AddDose(p,
								Enums.DoseType.getDoseType(DoseType.Missed)
										.toString(), GenUtils
										.dateTimeToDate(missedDoseDay),
								regimen.ID, System.currentTimeMillis(),
								((eComplianceApp) context
										.getApplicationContext()).LastLoginId,
								gps.getLatitude(), gps.longitude, context);
					}
				}

				if (regimen.numMissed > 1) {
					long doseStart = missedDoseDay;
					for (int j = 0; j < regimen.numMissed - 1; j++) {
						doseStart += (regimen.missedFreq * 86400000);
						if (!regimen.missedSunday) {
							if (GenUtils.dateToDay(doseStart) <= 2) {
								doseStart += 86400000;
							}
						}
						FutureMissedDoseOperations.addDose(p, Enums.DoseType
								.getDoseType(DoseType.Missed).toString(),
								GenUtils.dateTimeToDate(doseStart), regimen.ID,
								System.currentTimeMillis(),
								((eComplianceApp) context
										.getApplicationContext()).LastLoginId,
								gps.getLatitude(), gps.longitude, context);
					}
				}
			}
			for (Patient patDose : FutureMissedDoseOperations.getdoses(
					Schema.FUTURE_MISSED_DOSE_DOSE_DATE + "="
							+ GenUtils.dateTimeToDate(missedDoseDay), context)) {
				DoseUtils.AddDose(patDose.treatmentID, patDose.doseType,
						patDose.doseDate, patDose.regimenID,
						System.currentTimeMillis(), patDose.createdBy,
						patDose.latitude, patDose.longitude, context);
			}
			FutureMissedDoseOperations.doseSoftDeleteForDay(
					GenUtils.dateTimeToDate(missedDoseDay), context);
			missedDoseDay = missedDoseDay + 86400000;
		}
		AppStateConfigurationOperations.addAppStateConfiguration(
				Enums.AppStateKeyValues.getAppKey(
						AppStateKeyValues.LastMissedDoseLoged).toString(),
				String.valueOf(System.currentTimeMillis()), context);
		return true;
	}
}
