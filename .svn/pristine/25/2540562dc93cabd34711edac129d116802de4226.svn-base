/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.DbOperations;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.Model.Dose;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientHospitalizationViewModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientHospitalizationOperations {

	public static long addHospitalisedPatient(int transactionID,
			String treatmentID, long startDate, long endDate, String notes,
			String createdBy, long creationTimeStamp, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (isTransactionIdExists(transactionID, context)) {
			updateHospitalisedPatientData(transactionID, treatmentID,
					startDate, endDate, notes, createdBy, creationTimeStamp,
					context);
			return 1;
		}
		ContentValues hospitalizedValues = new ContentValues();
		hospitalizedValues.put(Schema.HOSPITALIZAION_TRANSACTION_ID,
				transactionID);
		hospitalizedValues.put(Schema.HOSPITALIZAION_TREATMENT_ID, treatmentID);
		hospitalizedValues.put(Schema.HOSPITALIZAION_START_DATE, startDate);
		hospitalizedValues.put(Schema.HOSPITALIZAION_END_DATE, endDate);
		hospitalizedValues.put(Schema.HOSPITALIZAION_NOTES, notes);
		hospitalizedValues.put(Schema.HOSPITALIZAION_CERATED_BY, createdBy);
		hospitalizedValues.put(Schema.HOSPITALIZAION_CREATION_TIMESTAMP,
				creationTimeStamp);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		try {
			return dbfactory.database.insert(Schema.TABLE_HOSPITALIZATION,
					null, hospitalizedValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(
			ArrayList<PatientHospitalizationViewModel> ph, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);

		dbfactory.database.beginTransaction();
		for (PatientHospitalizationViewModel view : ph) {
			ContentValues hospitalizedValues = new ContentValues();
			hospitalizedValues.put(Schema.HOSPITALIZAION_TRANSACTION_ID,
					view.ID);
			hospitalizedValues.put(Schema.HOSPITALIZAION_TREATMENT_ID,
					view.TreatmentId);
			hospitalizedValues.put(Schema.HOSPITALIZAION_START_DATE, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.StartDate)));
			hospitalizedValues.put(Schema.HOSPITALIZAION_END_DATE, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.EndDate)));
			hospitalizedValues.put(Schema.HOSPITALIZAION_NOTES, view.Notes);
			hospitalizedValues.put(Schema.HOSPITALIZAION_CERATED_BY,
					view.CreatedBy);
			hospitalizedValues.put(Schema.HOSPITALIZAION_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.CreationTimeStamp)));

			dbfactory.database.insert(Schema.TABLE_HOSPITALIZATION, null,
					hospitalizedValues);
		}

		try {
			dbfactory.database.setTransactionSuccessful();
			return 0;
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.database.endTransaction();
		}
	}

	public static boolean HospitalisedPatientExists(String treatmentId,
			Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_HOSPITALIZATION, null,
					Schema.HOSPITALIZAION_TREATMENT_ID + "=?",
					new String[] { treatmentId }, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}

	public static boolean isTransactionIdExists(int transactionID,
			Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_HOSPITALIZATION, null,
					Schema.HOSPITALIZAION_TRANSACTION_ID + "=?",
					new String[] { String.valueOf(transactionID) }, null, null,
					null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}

	public static long getEndDate(String treatmentId, Context context) {
		long endDate = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_HOSPITALIZATION, null,
					Schema.HOSPITALIZAION_TREATMENT_ID + "='" + treatmentId
							+ "'", null, null, null,
					Schema.HOSPITALIZAION_END_DATE, null);

			if (dbCursor.moveToLast()) {

				endDate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.HOSPITALIZAION_END_DATE));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return endDate;
	}

	public static void updateHospitalisedPatientData(int transactionID,
			String treatmentID, long startDate, long endDate, String notes,
			String createdBy, long creationTimeStamp, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		ContentValues hospitalizedValues = new ContentValues();

		hospitalizedValues.put(Schema.HOSPITALIZAION_TREATMENT_ID, treatmentID);
		hospitalizedValues.put(Schema.HOSPITALIZAION_START_DATE, startDate);
		hospitalizedValues.put(Schema.HOSPITALIZAION_END_DATE, endDate);
		hospitalizedValues.put(Schema.HOSPITALIZAION_NOTES, notes);
		hospitalizedValues.put(Schema.HOSPITALIZAION_CERATED_BY, createdBy);
		hospitalizedValues.put(Schema.HOSPITALIZAION_CREATION_TIMESTAMP,
				creationTimeStamp);
		dbfactory.database.update(Schema.TABLE_HOSPITALIZATION,
				hospitalizedValues, Schema.HOSPITALIZAION_TRANSACTION_ID + "='"
						+ transactionID + "'", null);

	}

	public static ArrayList<String> getHospitalisedPatient(Context context) {
		ArrayList<String> retList = new ArrayList<String>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		String searchString = "-";
		try {
			Cursor dbCursor = dbFactory.database.query(true,
					Schema.TABLE_HOSPITALIZATION, null,
					Schema.HOSPITALIZAION_END_DATE + " LIKE '%" + searchString
							+ "%'", null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				do {
					retList.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.HOSPITALIZAION_TREATMENT_ID)));

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retList;
	}

	public static ArrayList<Dose> getPatienthospitalisedDose(
			String treatmentId, Context context) {
		ArrayList<Dose> treatmentList = new ArrayList<Dose>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_HOSPITALIZATION, null,
					Schema.HOSPITALIZAION_TREATMENT_ID + " = '" + treatmentId
							+ "'", null, null, null,
					Schema.HOSPITALIZAION_START_DATE, null);

			if (dbCursor.moveToFirst()) {
				do {
					long hospStartDate = dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.HOSPITALIZAION_START_DATE));

					long hospEndDate = dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.HOSPITALIZAION_END_DATE));

					if (hospEndDate < 0) {
						hospEndDate = GenUtils.getCurrentDateLong();
					}

					int regimenId = TreatmentInStagesOperations
							.getPatientRegimenIdByDate(treatmentId,
									hospStartDate, context);

					for (long lDate = hospStartDate; lDate <= hospEndDate; lDate = lDate
							+ GenUtils.ONE_DAY) {
						Dose dose = new Dose();
						dose.setDoses("Hospitalised", lDate, regimenId);
						treatmentList.add(dose);
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return treatmentList;
	}

	public static boolean getHospitalisedPatientDateExistsForDay(
			String treatmentId, long date, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Hospitalization);

		Cursor dbCursor;

		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_HOSPITALIZATION, null,
					Schema.HOSPITALIZAION_TREATMENT_ID + "= '" + treatmentId
							+ "' and " + Schema.HOSPITALIZAION_START_DATE
							+ " >= " + date + " and "
							+ Schema.HOSPITALIZAION_END_DATE + " <= " + date,
					null, null, null, null, null);

			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;

	}
}
