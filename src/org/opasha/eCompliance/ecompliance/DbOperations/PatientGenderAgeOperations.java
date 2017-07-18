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
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientAgeGender;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.Patient_Age_Gender;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientGenderAgeOperations {
	public static void addGenderAge(String treatmentID, String gender, int age,
			long creationTimeStamp, boolean isDeleted, String createdBy,
			Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(treatmentID, creationTimeStamp, context)) {
			return;
		}
		softDelete(treatmentID, context);
		ContentValues val = new ContentValues();
		val.put(Schema.PATIENT_AGE_GENDER_AGE, age);
		val.put(Schema.PATIENT_AGE_GENDER_ID, treatmentID);
		val.put(Schema.PATIENT_AGE_GENDER_GENDER, gender);
		val.put(Schema.PATIENT_AGE_GENDER_CREATIONTIME, creationTimeStamp);
		val.put(Schema.PATIENT_AGE_GENDER_CREATED_BY, createdBy);
		val.put(Schema.PATIENT_AGE_GENDER_IS_DELETED, isDeleted);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientGenderAge);
		try {
			dbFactory.database.insert(Schema.TABLE_PATIENT_AGE_GENDER, null,
					val);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static long bulkInsert(ArrayList<PatientAgeGender> patient,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientGenderAge);

		dbfactory.database.beginTransaction();
		for (PatientAgeGender view : patient) {
			ContentValues val = new ContentValues();
			val.put(Schema.PATIENT_AGE_GENDER_AGE, view.Age);
			val.put(Schema.PATIENT_AGE_GENDER_ID, view.TreatmentId);
			val.put(Schema.PATIENT_AGE_GENDER_GENDER, view.Gender);
			val.put(Schema.PATIENT_AGE_GENDER_CREATIONTIME, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Creation_Time_Stamp)));
			val.put(Schema.PATIENT_AGE_GENDER_CREATED_BY, view.Created_By);
			val.put(Schema.PATIENT_AGE_GENDER_IS_DELETED, view.Is_Deleted);

			dbfactory.database.insert(Schema.TABLE_PATIENT_AGE_GENDER, null,
					val);
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

	private static boolean shouldUpdate(String id, long creationTimestamp,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientGenderAge);
		try {
			if (dbfactory.database.query(
					Schema.TABLE_PATIENT_AGE_GENDER,
					null,
					Schema.PATIENT_AGE_GENDER_ID + "= '" + id + "' and "
							+ Schema.PATIENT_AGE_GENDER_CREATIONTIME + " > "
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

	private static void softDelete(String treatmentID, Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.PATIENT_AGE_GENDER_IS_DELETED, true);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientGenderAge);
		try {
			dbFactory.database.update(Schema.TABLE_PATIENT_AGE_GENDER, val,
					Schema.PATIENT_AGE_GENDER_ID + " = '" + treatmentID + "'",
					null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static PatientAgeGender getAgeGender(String treatmentId,
			Context context) {
		PatientAgeGender retObj = new PatientAgeGender();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientGenderAge);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_AGE_GENDER, null,
					Schema.PATIENT_AGE_GENDER_ID + " ='" + treatmentId
							+ "' and " + Schema.PATIENT_AGE_GENDER_IS_DELETED
							+ " =0", null, null, null, null);
			dbCursor.moveToFirst();
			retObj.Age = dbCursor.getInt(dbCursor
					.getColumnIndex(Schema.PATIENT_AGE_GENDER_AGE));
			retObj.Gender = dbCursor.getString(dbCursor
					.getColumnIndex(Schema.PATIENT_AGE_GENDER_GENDER));
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retObj;
	}

	public static ArrayList<Patient_Age_Gender> patientSync(
			String filterExpression, Context context) {
		ArrayList<Patient_Age_Gender> patientList = new ArrayList<Patient_Age_Gender>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientGenderAge);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENT_AGE_GENDER, null, null, null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENT_AGE_GENDER, null,
						filterExpression, null, null, null, null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values
			}
			while (dbCursor.moveToNext()) {
				Patient_Age_Gender patients = new Patient_Age_Gender();
				patients.Age = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.PATIENT_AGE_GENDER_AGE));
				patients.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_AGE_GENDER_CREATED_BY));
				patients.Creation_TimeStamp = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.PATIENT_AGE_GENDER_CREATIONTIME)));
				patients.Gender = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_AGE_GENDER_GENDER));
				patients.Treatment_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_AGE_GENDER_ID));
				patients.Is_Deleted = GenUtils
						.getBoolean(dbCursor.getInt(dbCursor
								.getColumnIndex(Schema.PATIENT_AGE_GENDER_IS_DELETED)));
				patientList.add(patients);
			}
		} catch (Exception e) {
			String error = e.getMessage();
			String B = "";

		}
		dbfactory.CloseDatabase();

		return patientList;
	}
}
