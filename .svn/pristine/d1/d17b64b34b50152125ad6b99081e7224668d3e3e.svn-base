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
import org.opasha.eCompliance.ecompliance.modal.wcf.InitialCounseling;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Initial Counselling Operations
 * 
 * @author Abhishek Sinha
 * 
 */
public class InitialCounselingOperations {

	/**
	 * Add Initial Counselling data
	 * 
	 * @param treatmentId
	 *            - Treatment Id of Patient
	 * @param context
	 */
	public static void Add(String treatmentId, Context context) {
		ContentValues values = new ContentValues();
		values.put(Schema.INTIALCOUNSELING_TREATMENT_ID, treatmentId);
		values.put(Schema.INTIALCOUNSELING_START_TIME,
				System.currentTimeMillis());
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			dbfactory.database.insert(Schema.TABLE_INITIALCOUNSELING, null,
					values);
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
	}

	/**
	 * Update Initial Counselling Data
	 * 
	 * @param treatmentId
	 *            - Treatment Id
	 * @param context
	 */
	public static void Update(String treatmentId, Context context) {
		ContentValues values = new ContentValues();
		values.put(Schema.INTIALCOUNSELING_END_TIME, System.currentTimeMillis());

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			dbfactory.database.update(Schema.TABLE_INITIALCOUNSELING, values,
					Schema.INTIALCOUNSELING_TREATMENT_ID + "='" + treatmentId
							+ "'", null);
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
	}

	/**
	 * Delete Initial Counselling Data
	 * 
	 * @param treatmentId
	 *            - Treatment Id
	 * @param context
	 */
	public static void Delete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			dbfactory.database.delete(Schema.TABLE_INITIALCOUNSELING,
					Schema.INTIALCOUNSELING_TREATMENT_ID + "='" + treatmentId
							+ "'", null);
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
	}

	/**
	 * Check if Initial Counselling Data Exists for a patient
	 * 
	 * @param treatmentId
	 *            - Treatment Id
	 * @param context
	 * @return True if exists, False otherwise
	 */
	public static boolean Exists(String treatmentId, Context context) {
		boolean retValue = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_INITIALCOUNSELING, null,
					Schema.INTIALCOUNSELING_TREATMENT_ID + "='" + treatmentId
							+ "'", null, null, null, null, null);
			if (dbCursor.getCount() > 0) {
				retValue = true;
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();

		return retValue;
	}

	/**
	 * Get Start Time of Initial Counselling
	 * 
	 * @param treatmentId
	 *            - Treatment Id
	 * @param context
	 * @return - Start Time
	 */
	public static long CounselingStartTime(String treatmentId, Context context) {
		long startTime = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_INITIALCOUNSELING, null,
					Schema.INTIALCOUNSELING_TREATMENT_ID + "='" + treatmentId
							+ "'", null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				startTime = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.INTIALCOUNSELING_START_TIME));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return startTime;
	}

	public static ArrayList<InitialCounseling> counselingSync(
			String filterExpression, Context context) {
		ArrayList<InitialCounseling> returnList = new ArrayList<InitialCounseling>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		Cursor dbCursor;
		try {
			if (filterExpression.trim().length() <= 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_INITIALCOUNSELING, null, null, null, null,
						null, null, null);
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_INITIALCOUNSELING, null, filterExpression,
						null, Schema.INTIALCOUNSELING_START_TIME, null, null,
						null);
			}
			if (dbCursor.moveToFirst()) {
				do {
					InitialCounseling ic = new InitialCounseling();
					ic.Treatment_Id = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.INTIALCOUNSELING_TREATMENT_ID));
					ic.Start_Time = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.INTIALCOUNSELING_START_TIME)));
					ic.End_Time = GenUtils.longToDate(dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.INTIALCOUNSELING_END_TIME)));
					returnList.add(ic);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnList;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			dbfactory.database.delete(Schema.TABLE_INITIALCOUNSELING, null,
					null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static void counselingHardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialCounseling);
		try {
			dbfactory.database.delete(Schema.TABLE_INITIALCOUNSELING,
					Schema.INTIALCOUNSELING_TREATMENT_ID + "='" + treatmentId
							+ "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}
}
