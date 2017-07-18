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
import java.util.Locale;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientHospitalizationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;

import android.content.Context;
import android.content.res.Configuration;

public class LoadAppData {

	public static ArrayList<String> getVisitedPatients(Context context) {
		return DoseAdminstrationOperations.getVisitedPatients(
				Schema.DOSE_ADMINISTRATION_DOSE_DATE
						+ " = "
						+ GenUtils.getCurrentDateLong()
						+ " and "
						+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
						+ "='"
						+ Enums.DoseType.getDoseType(DoseType.Supervised)
								.toString() + "'", context);
	}

	public static ArrayList<String> getMissedPatients(Context context) {
		return DoseAdminstrationOperations.getmissedCount(PatientsOperations
				.getQueryForMissed(Schema.PATIENTS_STATUS
						+ " ='"
						+ Enums.StatusType.getStatusType(StatusType.Active)
								.toString() + "' and "
						+ Schema.PATIENTS_IS_DELETED + "=0", context), context);
	}

	public static ArrayList<String> getPendingPatients(Context context) {

		return DoseAdminstrationOperations.getPendingList(
				TreatmentInStagesOperations.getPendingList(
						RegimenMasterOperations.getRegimenIdForDay(
								GenUtils.getCurrentDateLong(), context),
						context), GenUtils.getCurrentDateLong(), context);

	}
	
	public static ArrayList<String> getHospitalisedPatient(Context context)
	{
		return PatientHospitalizationOperations.getHospitalisedPatient(context);
	}

	public static void setloacle(Context context) {
		Configuration config = context.getResources().getConfiguration();
		String language = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_language, context);
		if (language == null)
			language = "en";
		Locale locale = null;
		locale = new Locale(language);
		if (!"".equals(language)
				&& !config.locale.getLanguage().equals(language)) {
			Locale.setDefault(locale);

			config.locale = locale;

			context.getResources().updateConfiguration(config, null);
		}
	}

}
