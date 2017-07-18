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
import java.util.Calendar;
import java.util.Date;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.modal.wcf.DoseUpsupervisedReason;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UnSupervisedDoseReasonOperations {

	public static long addUnsupervisedDose(String ID, long StartDate,
			long EndDate, int Reason, long creationTimeStamp, String createdBy,
			Context context) {
		ContentValues ReasonDate = new ContentValues();
		ReasonDate.put(Schema.UNSUPERVISED_DOSE_TREATMENT_ID, ID);
		ReasonDate.put(Schema.UNSUPERVISED_DOSE_STARTDATE, StartDate);
		ReasonDate.put(Schema.UNSUPERVISED_DOSE_ENDDATE, EndDate);
		ReasonDate.put(Schema.UNSUPERVISED_DOSE_REASON, Reason);
		ReasonDate.put(Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP,
				creationTimeStamp);
		ReasonDate.put(Schema.UNSUPERVISED_DOSE_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.UnSupervisedDoseReason);

		long retValue = -1;
		try {
			retValue = dbfactory.database.insert(
					Schema.TABLE_UNSUPERVISED_DOSE_REASON, null, ReasonDate);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return retValue;
	}

	public static boolean isUnsupervisedToday(
			String treatmentId, Context context) {
		long lastDate = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.UnSupervisedDoseReason);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
						Schema.TABLE_UNSUPERVISED_DOSE_REASON, null,
						Schema.UNSUPERVISED_DOSE_TREATMENT_ID + "='" + treatmentId + "'", null,
						Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP, null,
						null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values
			if (dbCursor.moveToLast()) {
				lastDate = dbCursor.getLong(dbCursor.getColumnIndex(Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP));
			}
				
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		boolean isTakenToday = false;
		if(lastDate>0)
		{
			Date d = new Date(lastDate);
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(d);
			cal2.setTime(new Date(System.currentTimeMillis()));
			isTakenToday = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
			                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		}
		
		return isTakenToday;
	}
	
	public static ArrayList<DoseUpsupervisedReason> ReasonSync(
			String filterExpression, Context context) {
		ArrayList<DoseUpsupervisedReason> treatmentList = new ArrayList<DoseUpsupervisedReason>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.UnSupervisedDoseReason);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_UNSUPERVISED_DOSE_REASON, null, null,
						null, Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP,
						null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_UNSUPERVISED_DOSE_REASON, null,
						filterExpression, null,
						Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP, null,
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
					DoseUpsupervisedReason reason = new DoseUpsupervisedReason();
					reason.Treatment_Id = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.UNSUPERVISED_DOSE_TREATMENT_ID));
					reason.Start_Date = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.UNSUPERVISED_DOSE_STARTDATE)));
					reason.End_Date = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.UNSUPERVISED_DOSE_ENDDATE)));
					reason.Reason_Id = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.UNSUPERVISED_DOSE_REASON));

					treatmentList.add(reason);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return treatmentList;
	}

	public static void doseHardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.UnSupervisedDoseReason);
		try {
			dbfactory.database.delete(Schema.TABLE_UNSUPERVISED_DOSE_REASON,
					Schema.UNSUPERVISED_DOSE_TREATMENT_ID + "='" + treatmentId
							+ "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}
}
