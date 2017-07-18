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
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientHIV;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.Patient_Hiv;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class HIVOperation {
	public static void Add(String treatmentId, String finalResult,
			String sceerningResult, String createdBy, boolean isDeleted,
			long creationTimestamp, Context context) {
		creationTimestamp = GenUtils.lastThreeDigitsZero(creationTimestamp);
		if (!shouldUpdate(creationTimestamp, context)) {
			return;
		}
		softDelete(treatmentId, context);
		ContentValues values = new ContentValues();
		values.put(Schema.PATIENT_HIV_ID, treatmentId);
		values.put(Schema.PATIENT_HIV_CREATION_TIMESTAMP, creationTimestamp);
		values.put(Schema.PATIENT_HIV_FINAL_RESULT, finalResult);
		values.put(Schema.PATIENT_HIV_CREATED_BY, createdBy);
		values.put(Schema.PATIENT_HIV_SCREENING_RESULT, sceerningResult);
		values.put(Schema.PATIENT_HIV_IS_DELETED, isDeleted);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.HivResult);
		try {
			dbfactory.database.insert(Schema.TABLE_PATIENT_HIV, null, values);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static long bulkInsert(ArrayList<PatientHIV> hiv, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.HivResult);

		dbfactory.database.beginTransaction();
		for (PatientHIV view : hiv) {
			ContentValues values = new ContentValues();
			values.put(Schema.PATIENT_HIV_ID, view.Treatment_ID);
			values.put(Schema.PATIENT_HIV_CREATION_TIMESTAMP, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Creation_Timestamp)));
			values.put(Schema.PATIENT_HIV_FINAL_RESULT, view.Final_Result);
			values.put(Schema.PATIENT_HIV_CREATED_BY, view.Created_By);
			values.put(Schema.PATIENT_HIV_SCREENING_RESULT,
					view.Screening_Result);
			values.put(Schema.PATIENT_HIV_IS_DELETED, view.Is_Deleted);

			dbfactory.database.insert(Schema.TABLE_PATIENT_HIV, null, values);
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

	private static boolean shouldUpdate(long creationTimestamp, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.HivResult);
		try {
			if (dbfactory.database.query(
					Schema.TABLE_PATIENT_HIV,
					null,
					Schema.PATIENT_HIV_CREATION_TIMESTAMP + " > "
							+ creationTimestamp, null, null, null, null)
					.getCount() > 1) {
				dbfactory.CloseDatabase();
				return false;
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return true;
	}

	private static void softDelete(String treatmentId, Context context) {
		ContentValues values = new ContentValues();
		values.put(Schema.PATIENT_HIV_IS_DELETED, true);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.HivResult);
		try {
			dbfactory.database.update(Schema.TABLE_PATIENT_HIV, values,
					Schema.PATIENT_HIV_ID + "='" + treatmentId + "'	 ", null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static String getResult(String treatmentID, Context context) {
		String hivResult = "";
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.HivResult);
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_PATIENT_HIV, null, Schema.PATIENT_HIV_ID
							+ "= '" + treatmentID + "' and "
							+ Schema.PATIENT_HIV_IS_DELETED + "= 0", null,
					null, null, null);
			dbCursor.moveToFirst();
			hivResult = dbCursor.getString(dbCursor
					.getColumnIndex(Schema.PATIENT_HIV_SCREENING_RESULT))
					+ ","
					+ dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_HIV_FINAL_RESULT));
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return hivResult;
	}

	public static ArrayList<Patient_Hiv> resultSync(String query,
			Context context) {
		ArrayList<Patient_Hiv> retList = new ArrayList<Patient_Hiv>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.HivResult);
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_PATIENT_HIV, null, query, null, null, null,
					null);
			while (dbCursor.moveToNext()) {
				Patient_Hiv ret = new Patient_Hiv();
				ret.Treatment_ID = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_HIV_ID));
				ret.Final_Result = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_HIV_FINAL_RESULT));
				ret.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_HIV_CREATED_BY));
				ret.Is_Deleted = GenUtils.getBoolean(dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.PATIENT_HIV_IS_DELETED)));
				ret.Screening_Result = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_HIV_SCREENING_RESULT));
				ret.Creation_Timestamp = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.PATIENT_HIV_CREATION_TIMESTAMP)));
				retList.add(ret);
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return retList;

	}
}
