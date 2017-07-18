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
import org.opasha.eCompliance.ecompliance.modal.wcf.patientLocation;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientLocationViewModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class LocationOperations {
	/**
	 * Add Location Data for Patient
	 * 
	 * @param context
	 * @param treatmentId
	 *            - Treatment Id
	 * @param latitude
	 *            - Latitude. Eg. 17.898982
	 * @param longitude
	 *            - Longitude. Eg. 87.666322
	 * @return
	 */
	public static long addLocation(Context context, String treatmentId,
			double latitude, double longitude) {

		ContentValues locationValues = new ContentValues();
		locationValues.put(Schema.LOCATION_TREATMENT_ID, treatmentId);
		locationValues.put(Schema.LOCATION_LATITUDE, latitude);
		locationValues.put(Schema.LOCATION_LONGITUDE, longitude);
		locationValues.put(Schema.LOCATION_TIMESTAMP,
				System.currentTimeMillis());
		delete(context, treatmentId);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Location);
		long returnVal = -1;
		try {
			returnVal = dbfactory.database.insert(Schema.TABLE_LOCATION, null,
					locationValues);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnVal;

	}

	public static long bulkInsert(ArrayList<PatientLocationViewModel> pl,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Location);

		dbfactory.database.beginTransaction();
		for (PatientLocationViewModel view : pl) {
			ContentValues locationValues = new ContentValues();
			locationValues.put(Schema.LOCATION_TREATMENT_ID, view.Treatment_Id);
			locationValues.put(Schema.LOCATION_LATITUDE, view.Latitude);
			locationValues.put(Schema.LOCATION_LONGITUDE, view.Logitude);
			locationValues.put(Schema.LOCATION_TIMESTAMP,
					System.currentTimeMillis());
			dbfactory.database.insert(Schema.TABLE_LOCATION, null,
					locationValues);
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

	/**
	 * Delete Location Data for Patient
	 * 
	 * @param context
	 * @param treatmentId
	 */
	public static void delete(Context context, String treatmentId) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Location);
		try {
			dbfactory.database.delete(Schema.TABLE_LOCATION,
					Schema.LOCATION_TREATMENT_ID + "='" + treatmentId + "'",
					null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static ArrayList<patientLocation> patientLocationSync(
			String filterExpression, Context context) {
		ArrayList<patientLocation> locations = new ArrayList<patientLocation>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Location);
		try {
			if (filterExpression.trim().equals("")) {
				dbFactory.database
						.query(false, Schema.TABLE_LOCATION, null, null, null,
								null, null, Schema.LOCATION_TIMESTAMP, null);
			} else {
				Cursor dbCursor = dbFactory.database.query(false,
						Schema.TABLE_LOCATION, null, filterExpression, null,
						null, null, Schema.LOCATION_TIMESTAMP, null);
				if (dbCursor.moveToFirst()) {
					do {
						patientLocation location = new patientLocation();
						location.Treatment_Id = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.LOCATION_TREATMENT_ID));
						location.Latitude = dbCursor.getDouble(dbCursor
								.getColumnIndex(Schema.LOCATION_LATITUDE));
						location.Logitude = dbCursor.getDouble(dbCursor
								.getColumnIndex(Schema.LOCATION_LONGITUDE));
						location.Creation_Time_Stamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.LOCATION_TIMESTAMP)));
						locations.add(location);
					} while (dbCursor.moveToNext());
				}
			}
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
		return locations;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Location);
		try {
			dbfactory.database.delete(Schema.TABLE_LOCATION, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static void locationHardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Location);
		try {
			dbfactory.database.delete(Schema.TABLE_LOCATION,
					Schema.LOCATION_TREATMENT_ID + "='" + treatmentId + "'",
					null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}
}
