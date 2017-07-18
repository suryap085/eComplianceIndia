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
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.modal.wcf.FutureMissedDose;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.FutureDoseViewModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FutureMissedDoseOperations {

	public static void addDose(String ID, String doseType, long doseDate,
			int regimenId, long creationTimeStamp, String createdBy,
			double latitude, double longitude, Context context) {
		deleteDose(ID, doseDate, context);
		ContentValues DoseValues = new ContentValues();
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_TREATMENT_ID, ID);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_DOSE_TYPE, doseType);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_DOSE_DATE, doseDate);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_REGIMEN_ID, regimenId);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP,
				creationTimeStamp);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_LATITUDE, latitude);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_LONGITUDE, longitude);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_CREATED_BY, createdBy);
		DoseValues.put(Schema.FUTURE_MISSED_DOSE_IS_DELETED, 0);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);

		try {
			dbfactory.database.insert(Schema.TABLE_FUTURE_MISSED_DOSE, null,
					DoseValues);
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();

	}

	public static long bulkInsert(ArrayList<FutureDoseViewModel> fm,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);

		dbfactory.database.beginTransaction();
		for (FutureDoseViewModel view : fm) {
			ContentValues DoseValues = new ContentValues();
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_TREATMENT_ID,
					view.Treatment_Id);
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_DOSE_TYPE, view.Dose_Type);
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_DOSE_DATE,
					GenUtils.getTimeFromTicks(view.Dose_Date));
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_REGIMEN_ID,
					view.Regimen_Id);
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(view.Creation_TimeStamp));
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_LATITUDE, view.Latitude);
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_LONGITUDE, view.Longitude);
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_CREATED_BY,
					view.Created_By);
			DoseValues.put(Schema.FUTURE_MISSED_DOSE_IS_DELETED, 0);

			dbfactory.database.insert(Schema.TABLE_FUTURE_MISSED_DOSE, null,
					DoseValues);
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

	private static void deleteDose(String iD, long doseDate, Context context) {
		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.FUTURE_MISSED_DOSE_IS_DELETED, true);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);
		try {
			dbfactory.database.update(Schema.TABLE_FUTURE_MISSED_DOSE,
					isDeleted, Schema.FUTURE_MISSED_DOSE_TREATMENT_ID + "='"
							+ iD + "' and "
							+ Schema.FUTURE_MISSED_DOSE_DOSE_DATE + "="
							+ doseDate, null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();

	}

	public static void doseSoftDeleteForDay(long dosedate, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.FUTURE_MISSED_DOSE_IS_DELETED, true);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);
		try {
			dbfactory.database.update(Schema.TABLE_FUTURE_MISSED_DOSE,
					isDeleted, Schema.FUTURE_MISSED_DOSE_DOSE_DATE + "="
							+ dosedate, null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static void doseSoftDeleteForPatient(String treatmentId,
			Context context) {
		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.FUTURE_MISSED_DOSE_IS_DELETED, true);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);
		try {
			dbfactory.database.update(Schema.TABLE_FUTURE_MISSED_DOSE,
					isDeleted, Schema.FUTURE_MISSED_DOSE_TREATMENT_ID + "='"
							+ treatmentId + "'", null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static void doseHardDelete(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);
		try {
			dbfactory.database.delete(Schema.TABLE_FUTURE_MISSED_DOSE,
					Schema.FUTURE_MISSED_DOSE_IS_DELETED + "=1", null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static ArrayList<Patient> getdoses(String filterExpression,
			Context context) {
		ArrayList<Patient> treatmentList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_FUTURE_MISSED_DOSE, null,
						Schema.FUTURE_MISSED_DOSE_IS_DELETED + "=0", null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_FUTURE_MISSED_DOSE, null,
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

			if (dbCursor.moveToFirst()) {
				do {
					Patient doses = new Patient();
					doses.setPatient(
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_TREATMENT_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_DOSE_TYPE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_DOSE_DATE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_REGIMEN_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_CREATED_BY)));
					doses.latitude = dbCursor
							.getDouble(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_LATITUDE));
					doses.longitude = dbCursor
							.getDouble(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_LONGITUDE));
					treatmentList.add(doses);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return treatmentList;
	}

	public static boolean doseExist(String treatmentId, long dosedate,
			Context context) {
		if (getdoses(
				Schema.FUTURE_MISSED_DOSE_TREATMENT_ID + "='" + treatmentId
						+ "' and " + Schema.FUTURE_MISSED_DOSE_DOSE_DATE + "="
						+ dosedate, context).size() == 0)
			return false;
		return true;
	}

	public static ArrayList<FutureMissedDose> getdosesSync(
			String filterExpression, Context context) {
		ArrayList<FutureMissedDose> doseList = new ArrayList<FutureMissedDose>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.FutureMissedDose);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_FUTURE_MISSED_DOSE, null, null, null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_FUTURE_MISSED_DOSE, null,
						filterExpression, null,
						Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP, null,
						null, null); // if
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
					FutureMissedDose doses = new FutureMissedDose();
					doses.Latitude = dbCursor
							.getDouble(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_LATITUDE));
					doses.Longitude = dbCursor
							.getDouble(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_LONGITUDE));
					doses.Treatment_Id = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_TREATMENT_ID));
					doses.Dose_Type = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_DOSE_TYPE));
					doses.Dose_Date = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_DOSE_DATE)));
					doses.Creation_TimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP)));
					doses.Regimen_Id = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_REGIMEN_ID));
					doses.Created_By = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_CREATED_BY));
					doses.Is_Deleted = Boolean
							.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.FUTURE_MISSED_DOSE_IS_DELETED)));

					doseList.add(doses);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return doseList;
	}
}
