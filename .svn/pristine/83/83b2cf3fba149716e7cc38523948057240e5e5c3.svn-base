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
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientInitialLabDataModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Patient_Initial_Lab_Data_Operations {

	public static long addPatientInitialLabData(String ID, String labData,
			String createdBy, String machineID, long creationTimeStamp,
			boolean isDeleted, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(ID, creationTimeStamp, context)) {
			return 1;
		}
		deletePatientInitialLabDataSoft(ID, context);
		ContentValues patientValues = new ContentValues();
		patientValues.put(Schema.PATIENT_INITIAL_LAB_TREATMENTID, ID);
		patientValues.put(Schema.PATIENT_INITIAL_LAB_DATA, labData);
		patientValues.put(Schema.PATIENT_INITIAL_LAB_TAB_ID, machineID);
		patientValues.put(Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP,
				creationTimeStamp);
		patientValues.put(Schema.PATIENT_INITIAL_LAB_CREATED_BY, createdBy);
		patientValues.put(Schema.PATIENT_INITIAL_LAB_ISDELETED, isDeleted);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabData);
		try {
			return dbfactory.database.insert(Schema.TABLE_INITIAL_LAB_DATA,
					null, patientValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	private static boolean shouldUpdate(String Id, long creationTimestamp,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabData);
		try {
			if (dbfactory.database
					.query(Schema.TABLE_INITIAL_LAB_DATA,
							null,
							Schema.PATIENT_INITIAL_LAB_TREATMENTID
									+ "= '"
									+ Id
									+ "' and "
									+ Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP
									+ " > " + creationTimestamp, null, null,
							null, null).getCount() > 1) {
				dbfactory.CloseDatabase();
				return false;
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return true;
	}

	// Public function to change isDeleted of a contact
	public static int deletePatientInitialLabDataSoft(String treatmentId,
			Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.PATIENT_INITIAL_LAB_ISDELETED, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabData);

		int num = -1;
		try {
			num = dbfactory.database.update(Schema.TABLE_INITIAL_LAB_DATA,
					isDeleted, Schema.PATIENT_INITIAL_LAB_TREATMENTID + "=?",
					new String[] { treatmentId });
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		return num;
	}

	public static ArrayList<PatientInitialLabDataModel> patientInitialDataSync(
			String filterExpression, Context context) {
		ArrayList<PatientInitialLabDataModel> returnList = new ArrayList<PatientInitialLabDataModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabData);
		Cursor dbCursor;
		try {
			if (filterExpression.trim().length() <= 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_INITIAL_LAB_DATA, null, null, null, null,
						null, null, null);
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_INITIAL_LAB_DATA, null, filterExpression,
						null, Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP,
						null, null, null);
			}
			if (dbCursor.moveToFirst()) {
				do {
					PatientInitialLabDataModel initiallab = new PatientInitialLabDataModel();
					initiallab.Treatment_id = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.PATIENT_INITIAL_LAB_TREATMENTID));
					initiallab.LabData = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_INITIAL_LAB_DATA));
					initiallab.Tab_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_INITIAL_LAB_TAB_ID));
					initiallab.CreatedBy = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.PATIENT_INITIAL_LAB_CREATED_BY));
					initiallab.CreationTimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP)));
					initiallab.IsDeleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_INITIAL_LAB_ISDELETED)));
					returnList.add(initiallab);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnList;
	}
	
}
