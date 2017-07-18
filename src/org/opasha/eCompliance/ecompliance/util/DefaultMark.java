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

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;

import android.content.Context;
import android.util.Log;

public class DefaultMark {

	/**
	 * @param context
	 */
	public static void markDefault(Context context) {
		String defaultdays = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_default_days, context);
		String is_default_enabled = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_default_enabled, context);

		// String is_default_enabled ="true";
		if (defaultdays.equals("") || is_default_enabled.equals("")
				|| is_default_enabled.equals("false")) {
			return;
		}
		long compareDate = GenUtils.getCurrentDateLong()
				- Long.valueOf(Integer.parseInt(defaultdays)) * 86400000;

		String Ids = PatientsOperations.getActivePatient(Schema.PATIENTS_STATUS
				+ " ='"
				+ Enums.StatusType.getStatusType(StatusType.Active).toString()
				+ "' and " + Schema.PATIENTS_IS_DELETED + "=0 and "
				+ Schema.PATIENTS_IS_COUNSELLING_PENDING + "=" + 0, context); // get
																				// all
																				// active
																				// patients
																				// with
																				// COUNSELLING_PENDING
																				// =
																				// 0
		ArrayList<String> defaultIds = DoseAdminstrationOperations
				.getDefaultIds(Ids, compareDate, context);
		for (String p : defaultIds) {
			Patient patient = PatientsOperations.getPatientDetails(p, context);
			PatientsOperations.addPatient(p, patient.name, Enums.StatusType
					.getStatusType(StatusType.Default).toString(),
					patient.phoneNumber, patient.machineID, false, System
							.currentTimeMillis(), "Auto", false,
					patient.RegDate, patient.centerId, patient.address,
					patient.diseaseSite, patient.disease, patient.patientType,
					patient.nikshayId, patient.tbNumber,
					patient.smokingHistory, context);
			TreatmentInStagesOperations.addTreatmentStage(p, 0,
					GenUtils.getCurrentDateLong(), System.currentTimeMillis(),
					"Auto", false, context);

		}
		ArrayList<Patient> activePatients = PatientsOperations.getPatients(
				Schema.PATIENTS_STATUS
						+ " ='"
						+ Enums.StatusType.getStatusType(StatusType.Active)
								.toString() + "' and "
						+ Schema.PATIENTS_IS_DELETED + "=0 and "
						+ Schema.PATIENTS_IS_COUNSELLING_PENDING + "= 1",
				context); // get all active patients with COUNSELLING_PENDING =
							// 1

		for (Patient p : activePatients) {
			if (p.RegDate < compareDate) {
				PatientsOperations.addPatient(p.treatmentID, p.name,
						Enums.StatusType.getStatusType(StatusType.Default)
								.toString(), p.phoneNumber, p.machineID, false,
						System.currentTimeMillis(), "Auto", false, p.RegDate,
						p.centerId, p.address, p.diseaseSite, p.disease,
						p.patientType, p.nikshayId, p.tbNumber,
						p.smokingHistory, context);
				TreatmentInStagesOperations.addTreatmentStage(p.treatmentID, 0,
						GenUtils.getCurrentDateLong(),
						System.currentTimeMillis(), "Auto", false, context);
			}
		}

	}

	public static int tatnativeCount(Context context) {
		long defaultdays = 0;
		try {
			defaultdays = Long.parseLong(ConfigurationOperations.getKeyValue(
					ConfigurationKeys.key_default_days, context));
		} catch (Exception e) {

		}

		long tantativeTime = 0;
		try {
			tantativeTime = Long.parseLong((ConfigurationOperations
					.getKeyValue(ConfigurationKeys.key_tantative_default_days,
							context)));
		} catch (Exception e) {

		}

		long compareDate = GenUtils.getCurrentDateLong()
				- (defaultdays - tantativeTime) * 86400000;
		String Ids = PatientsOperations.getActivePatient(Schema.PATIENTS_STATUS
				+ " ='"
				+ Enums.StatusType.getStatusType(StatusType.Active).toString()
				+ "' and " + Schema.PATIENTS_IS_DELETED + "=0 and "
				+ Schema.PATIENTS_IS_COUNSELLING_PENDING + "=" + 0, context);
		return DoseAdminstrationOperations.getDefaultIds(Ids, compareDate,
				context).size();

	}
}
