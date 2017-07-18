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
import org.opasha.eCompliance.ecompliance.Model.DiabetesModal;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.DA_Patient_Test_Details;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDiabetes;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientDiabetesOperations {
	public static void add(String id, int testId, String result,
			String adminBy, long creationTimeStamp, boolean isDeleted,
			Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.PATIENT_DIABETES_ID, id);
		val.put(Schema.PATIENT_DIABETES_TEST_ID, testId);
		val.put(Schema.PATIENT_DIABETES_RESULT, result);
		val.put(Schema.PATIENT_DIABETES_ADMINISTRATED_BY, adminBy);
		val.put(Schema.PATIENT_DIABETES_CREATION_TIMESTAMP, creationTimeStamp);
		val.put(Schema.PATIENT_DIABETES_IS_DELETED, isDeleted);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patient_Diabetes);
		try {
			dbFactory.database.insert(Schema.TABLE_PATIENT_DIABETES, null, val);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static long bulkInsert(ArrayList<PatientDiabetes> d, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patient_Diabetes);

		dbfactory.database.beginTransaction();
		for (PatientDiabetes view : d) {
			ContentValues val = new ContentValues();
			val.put(Schema.PATIENT_DIABETES_ID, view.Treatment_ID);
			val.put(Schema.PATIENT_DIABETES_TEST_ID, view.Test_ID);
			val.put(Schema.PATIENT_DIABETES_RESULT, view.Result);
			val.put(Schema.PATIENT_DIABETES_ADMINISTRATED_BY,
					view.Andministered_By);
			val.put(Schema.PATIENT_DIABETES_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(view.Test_Date));
			val.put(Schema.PATIENT_DIABETES_IS_DELETED, view.Is_Deleted);

			dbfactory.database.insert(Schema.TABLE_PATIENT_DIABETES, null, val);
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

	public static void softDelete(String query, Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.PATIENT_DIABETES_IS_DELETED, true);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patient_Diabetes);
		try {
			dbFactory.database.update(Schema.TABLE_PATIENT_DIABETES, val,
					query, null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static long lastResultDate(String query, Context context) {
		long retDate = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patient_Diabetes);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_DIABETES, null, query, null, null,
					null, Schema.PATIENT_DIABETES_CREATION_TIMESTAMP);
			if (dbCursor.moveToLast()) {
				retDate = dbCursor
						.getLong(dbCursor
								.getColumnIndex(Schema.PATIENT_DIABETES_CREATION_TIMESTAMP));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retDate;
	}

	public static boolean isFirstDiabetes(String treatmentId, Context context) {
		boolean retDate = false;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patient_Diabetes);
		try {
			Cursor dbCursor = dbFactory.database
					.query(Schema.TABLE_PATIENT_DIABETES, null,
							Schema.PATIENT_DIABETES_ID + " = '" + treatmentId
									+ "' and "
									+ Schema.PATIENT_DIABETES_TEST_ID + "=1",
							null, null, null,
							Schema.PATIENT_DIABETES_CREATION_TIMESTAMP);
			if (dbCursor.moveToFirst()) {
				retDate = isDiabetic(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_DIABETES_RESULT)),
						context);
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retDate;
	}

	private static boolean isDiabetic(String value, Context context) {
		ArrayList<DiabetesModal> model = MasterDiabetesOperations.getMinMax(
				Schema.MASTER_DIABETES_TEST_ID + "= 1", context);
		double values = 0;
		try {
			values = Double.parseDouble(value);
		} catch (Exception e) {
		}
		for (DiabetesModal m : model) {
			if (values >= m.minValue && values <= m.maxValue) {
				String result = m.result;
				if (result.toLowerCase().trim().equals("diabetic")) {
					return true;
				}

			}
		}
		return false;
	}

	public static ArrayList<DA_Patient_Test_Details> dataSync(String string,
			Context context) {
		ArrayList<DA_Patient_Test_Details> retArrayList = new ArrayList<DA_Patient_Test_Details>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patient_Diabetes);
		try {
			Cursor dbCursor;
			if (string != null) {
				dbCursor = dbFactory.database.query(
						Schema.TABLE_PATIENT_DIABETES, null, string, null,
						null, null, null);
			} else {
				dbCursor = dbFactory.database.query(
						Schema.TABLE_PATIENT_DIABETES, null, null, null, null,
						null, null);
			}
			while (dbCursor.moveToNext()) {
				DA_Patient_Test_Details ret = new DA_Patient_Test_Details();
				ret.Treatment_ID = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_DIABETES_ID));
				ret.Andministered_By = dbCursor
						.getString(dbCursor
								.getColumnIndex(Schema.PATIENT_DIABETES_ADMINISTRATED_BY));
				ret.Test_Date = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.PATIENT_DIABETES_CREATION_TIMESTAMP)));
				ret.Is_Deleted = GenUtils.getBoolean(dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.PATIENT_DIABETES_IS_DELETED)));
				ret.Result = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_DIABETES_RESULT));
				ret.Test_ID = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.PATIENT_DIABETES_TEST_ID));
				retArrayList.add(ret);
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retArrayList;
	}
}
