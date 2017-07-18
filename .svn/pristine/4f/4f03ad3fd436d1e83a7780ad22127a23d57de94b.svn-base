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
import java.util.Calendar;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.RegimenMasterOperations;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.modal.wcf.DosesFull;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;

import android.content.Context;
import android.util.Log;

public class DoseUtils {
	// Remove missed dose if supervised dose with in 24 hours.
	public static void AddDose(String ID, String doseType, long doseDate,
			int regimenId, long creationTimeStamp, String createdBy,
			double latitude, double longitude, Context context) {
		if (ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_is_delete_missed_dose_for_last_24_hrs,
				context).isEmpty()
				|| !ConfigurationOperations
						.getKeyValue(
								ConfigurationKeys.key_is_delete_missed_dose_for_last_24_hrs,
								context).equals("1")) {
			Master regimen = RegimenMasterOperations.getRegimen(regimenId,
					context);
			if (doseType.equals(DoseType.Missed.toString())) {
				long nextDoseDate = (doseDate) + GenUtils.TWO_DAY;
				if (!regimen.missedSunday) {
					if (GenUtils.dateToDay(doseDate) == Calendar.SATURDAY) {
						nextDoseDate += GenUtils.ONE_DAY;
					}
				}
				String query = Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "= '"
						+ ID + "' and " + Schema.DOSE_ADMINISTRATION_DOSE_DATE
						+ " > " + creationTimeStamp + " and "
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " <= "
						+ nextDoseDate + " and "
						+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE + " ='"
						+ DoseType.Supervised.toString() + "'";
				ArrayList<Patient> list = DoseAdminstrationOperations.getdoses(
						query, context);
				if (list.size() > 0) {
					return;
				}
			} else if (doseType.equals(DoseType.Supervised.toString())) {
				long preDoseDate = (GenUtils.dateTimeToDate(doseDate))
						- GenUtils.ONE_DAY;
				if (!regimen.missedSunday) {
					if (GenUtils.dateToDay(doseDate) == Calendar.MONDAY) {
						preDoseDate -= GenUtils.ONE_DAY;
					}
				}

				if (regimen.daysFrequency > 1) {

					String query = Schema.DOSE_ADMINISTRATION_TREATMENT_ID
							+ "= '" + ID + "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " >= "
							+ preDoseDate + " and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " < "
							+ creationTimeStamp + " and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE + " ='"
							+ DoseType.Missed.toString() + "'";

					// if (regimenId < 22) {
					// if (DoseAdminstrationOperations.getdoses(query, context)
					// .isEmpty()) {
					// // *AS - Asked by Sandeep Sir and Ashvini to make this
					// // change
					// // Change Schedule to today's. This is only done for CP.
					// // We are checking if patient is taking a dose after 1
					// // business day of his current regimen. If he does, we
					// // change the schedule to current day
					//
					// if (regimen.stage.equals("CP")) {
					// String newSchedule = GenUtils.getCurrentDay();
					// int regId = RegimenMasterOperations.getRegimenId(
					// Schema.REGIMEN_MASTER_CATEGORY + "= '"
					// + regimen.catagory + "' and "
					// + Schema.REGIMEN_MASTER_STAGE
					// + "='" + regimen.stage + "' and "
					// + Schema.REGIMEN_MASTER_SCHEDULE
					// + "= '" + newSchedule + "'",
					// context);
					// TreatmentInStagesOperations.addTreatmentStage(ID,
					// regId, GenUtils.getCurrentDateLong(),
					// System.currentTimeMillis(), createdBy,
					// false, context);
					// regimenId = regId;
					// }
					// } else {
					// DoseAdminstrationOperations.doseHardDeleteByQuery(
					// query, context);
					// }
					// }
					DoseAdminstrationOperations.doseHardDeleteByQuery(query,
							context);
				}

			}
		}

		DoseAdminstrationOperations.addDose(ID, doseType, doseDate, regimenId,
				creationTimeStamp, createdBy, latitude, longitude, context);
	}

	// Remove missed dose if supervised dose with in 24 hours.
	public static void removeMissedDose(Context context) {

		try {
			ArrayList<DosesFull> list = DoseAdminstrationOperations
					.getdosesForMissedRemoval(
							Schema.DOSE_ADMINISTRATION_REGIMEN_ID + "<21",
							context);

			ArrayList<DosesFull> AllMissed = new ArrayList<DosesFull>();
			for (DosesFull m : list) {
				if (m.Dose_Type.endsWith("Missed"))
					AllMissed.add(m);
			}

			ArrayList<Master> regimens = RegimenMasterOperations.getRegimen("",
					context);

			ArrayList<Integer> missedIdToDel = new ArrayList<Integer>();

			for (DosesFull doses : list) {
				if (doses.Dose_Type.equals("Missed"))
					continue;

				for (DosesFull m : AllMissed) {
					if (!m.Treatment_Id.equals(doses.Treatment_Id))
						continue;

					Master regimen = regimens.get(0);
					for (Master r : regimens) {
						if (r.regimenId == doses.Regimen_Id) {
							regimen = r;
						}
					}

					long preDoseDate = 1;
					if (!regimen.missedSunday) {
						if (GenUtils.dateToDay(doses.Dose_Date.getTime()) == 2) {
							preDoseDate = 2;
						}
					}

					if (doses.Dose_Date.getTime() - m.Dose_Date.getTime() == preDoseDate
							* GenUtils.ONE_DAY) {
						if (missedIdToDel.contains(m.Id))
							continue;
						missedIdToDel.add(m.Id);
					}
				}
			}

			String query = Schema.DOSE_ADMINISTRATION_ROW_ID + " IN (";
			int i = 0;
			for (int doseId : missedIdToDel) {
				if (i < 25) {
					query += doseId + ",";
				} else {
					query += doseId + ")";
					Log.e("Query to Delete", query);
					DoseAdminstrationOperations.doseHardDeleteByQuery(query,
							context);
					query = Schema.DOSE_ADMINISTRATION_ROW_ID + " IN (";
					i = 0;
				}
				i++;
			}

			if (i > 0) {
				query = query.substring(0, query.length() - 2) + ")";
				Log.e("Query to Delete", query);
				DoseAdminstrationOperations.doseHardDeleteByQuery(query,
						context);
			}
		} catch (Exception e) {
		}

	}

}
