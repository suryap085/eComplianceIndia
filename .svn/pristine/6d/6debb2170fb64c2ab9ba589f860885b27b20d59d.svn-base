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
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDetails2ViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.Patient_Details_2;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientV2_Operations {

	public static long addPatient(String ID, String aadhaarNo,
			String patientsource, long creationTimeStamp, String createdBy,
			boolean isDeleted, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(ID, creationTimeStamp, context)) {
			return 1;
		}
		deletePatientSoft(ID, context);

		ContentValues patientValues = new ContentValues();
		patientValues.put(Schema.PATIENT_TREATMENT_ID, ID);
		patientValues.put(Schema.PATIENT_AADHAAR_NUMBER, aadhaarNo);
		patientValues.put(Schema.PATIENT_PATIENT_SOURCE, patientsource);
		patientValues.put(Schema.PATIENT_CREATIONTIME_STAMP, creationTimeStamp);
		patientValues.put(Schema.PATIENT_CREATED_BY, createdBy);
		patientValues.put(Schema.PATIENT_IS_DELETED, isDeleted);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);
		try {
			return dbfactory.database.insert(Schema.TABLE_PATIENTV2, null,
					patientValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(
			ArrayList<PatientDetails2ViewModel> patientdetailsv2,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);

		dbfactory.database.beginTransaction();
		for (PatientDetails2ViewModel view : patientdetailsv2) {
			ContentValues patientValues = new ContentValues();
			patientValues.put(Schema.PATIENT_TREATMENT_ID, view.Treatment_Id);
			patientValues.put(Schema.PATIENT_AADHAAR_NUMBER, view.AdhaarNo);
			patientValues
					.put(Schema.PATIENT_PATIENT_SOURCE, view.PatientSource);
			patientValues.put(Schema.PATIENT_CREATIONTIME_STAMP, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Creation_TimeStamp)));
			patientValues.put(Schema.PATIENT_CREATED_BY, view.Created_By);
			patientValues.put(Schema.PATIENT_IS_DELETED, view.Is_Deleted);

			dbfactory.database.insert(Schema.TABLE_PATIENTV2, null,
					patientValues);
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

	private static boolean shouldUpdate(String Id, long creationTimestamp,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);
		try {
			if (dbfactory.database.query(
					Schema.TABLE_PATIENTV2,
					null,
					Schema.PATIENT_TREATMENT_ID + "= '" + Id + "' and "
							+ Schema.PATIENT_CREATIONTIME_STAMP + " > "
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

	// Public function to change isDeleted of a contact
	public static int deletePatientSoft(String treatmentId, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.PATIENT_IS_DELETED, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);

		int num = -1;
		try {
			num = dbfactory.database.update(Schema.TABLE_PATIENTV2, isDeleted,
					Schema.PATIENT_TREATMENT_ID + "=?",
					new String[] { treatmentId });
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		return num;
	}

	public static boolean patientExists(String treatmentID, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_PATIENTV2,
					null, Schema.PATIENT_TREATMENT_ID + "=?",
					new String[] { treatmentID }, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}

	public static ArrayList<PatientDetails2ViewModel> getPatients(
			String filterExpression, Context context) {
		ArrayList<PatientDetails2ViewModel> patientList = new ArrayList<PatientDetails2ViewModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTV2, null, null, null, null, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTV2, null, filterExpression, null,
						null, null, null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values

			}
			if (dbCursor.moveToFirst()) {

				do {
					PatientDetails2ViewModel patients = new PatientDetails2ViewModel();
					patients.Treatment_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_TREATMENT_ID));
					patients.AdhaarNo = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_AADHAAR_NUMBER));
					patients.PatientSource = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_PATIENT_SOURCE));

					patients.Creation_TimeStamp = dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.PATIENT_CREATIONTIME_STAMP));

					patientList.add(patients);

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

	public static ArrayList<Patient_Details_2> patientSync(
			String filterExpression, Context context) {
		ArrayList<Patient_Details_2> patientList = new ArrayList<Patient_Details_2>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patientv2);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTV2, null, null, null, null, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTV2, null, filterExpression, null,
						null, null, null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values

			}
			if (dbCursor.moveToFirst()) {

				do {
					Patient_Details_2 patients = new Patient_Details_2();
					patients.Treatment_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_TREATMENT_ID));
					patients.AdhaarNo = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_AADHAAR_NUMBER));
					patients.PatientSource = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_PATIENT_SOURCE));
					patients.Created_By = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_CREATED_BY));
					patients.Creation_TimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.PATIENT_CREATIONTIME_STAMP)));

					patients.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_IS_DELETED)));
					patientList.add(patients);

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

}
