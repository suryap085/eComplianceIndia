/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientIcon;
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientIconGet;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientIconOperation {
	public static void addIcon(String id, String icon, long creationTimeStamp,
			Boolean isActive, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(id, creationTimeStamp, context)) {
			return;
		}
		deleteIcon(id, context);
		if (!isActive) {
			return;
		}
		ContentValues val = new ContentValues();
		val.put(Schema.PATIENT_ICON_ID, id);
		val.put(Schema.PATIENT_ICON_ICON, icon);
		val.put(Schema.PATIENT_ICON_CREATION_TIMESTAMP, creationTimeStamp);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			dbFactory.database.insert(Schema.TABLE_PATIENT_ICON, null, val);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	
	
	private static boolean shouldUpdate(String treatmentId,
			long creationTimestamp, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			if (dbfactory.database.query(
					Schema.TABLE_PATIENT_ICON,
					null,
					Schema.PATIENT_ICON_ID + " = '" + treatmentId + "' and "
							+ Schema.PATIENT_ICON_CREATION_TIMESTAMP + " > "
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

	public static void deleteIcon(String id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			dbFactory.database.delete(Schema.TABLE_PATIENT_ICON,
					Schema.PATIENT_ICON_ID + " = '" + id + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static String getIcon(String treatmentID, Context context) {
		String retVal = "";
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_ICON, null, Schema.PATIENT_ICON_ID
							+ "='" + treatmentID + "'", null, null, null, null);
			if (dbCursor.moveToFirst()) {
				retVal = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_ICON_ICON));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;
	}

	public static String getId(String icon, Context context) {
		String retVal = "";
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_ICON, null, Schema.PATIENT_ICON_ICON
							+ "='" + icon + "'", null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				retVal = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_ICON_ID));

			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;
	}

	public static boolean isIcon(String id, Context context) {
		boolean retVal = false;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_ICON, null, Schema.PATIENT_ICON_ID
							+ "='" + id + "'", null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				retVal = true;

			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;
	}

	public static ArrayList<PatientIcon> getIcons(String query, Context context) {
		ArrayList<PatientIcon> retList = new ArrayList<PatientIcon>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_ICON, null, query, null, null, null,
					Schema.PATIENT_ICON_CREATION_TIMESTAMP);
			while (dbCursor.moveToNext()) {
				PatientIcon icon = new PatientIcon();
				icon.Treatment_ID = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_ICON_ID));
				icon.Icon = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_ICON_ICON));
				icon.Creation_TimeStamp = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.PATIENT_ICON_CREATION_TIMESTAMP)));
				retList.add(icon);

			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retList;
	}

	public static ArrayList<String> getIconsForMasterUpdate(String query,
			Context context) {
		ArrayList<String> retList = new ArrayList<String>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_ICON, null, query, null, null, null,
					Schema.PATIENT_ICON_CREATION_TIMESTAMP);
			while (dbCursor.moveToNext()) {
				retList.add(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_ICON_ICON)));

			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retList;
	}
}
