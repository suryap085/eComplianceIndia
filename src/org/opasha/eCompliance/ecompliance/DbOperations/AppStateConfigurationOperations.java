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
import org.opasha.eCompliance.ecompliance.util.AppStateConfigurationKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AppStateConfigurationOperations {

	public static long addAppStateConfiguration(String key, String value,
			Context context) {
		ContentValues appState = new ContentValues();
		appState.put(Schema.APP_STATE_CONFIGURATION_KEY, key);
		appState.put(Schema.APP_STATE_CONFIGURATION_VALUE, value);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.AppStateConfiguration);
		try {
			deleteKey(key, context);
			return dbfactory.database.insert(
					Schema.TABLE_APP_STATE_CONFIGURATION, null, appState);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static String getKeyValue(String key, Context context) {
		String retValue = "";
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.AppStateConfiguration);
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_APP_STATE_CONFIGURATION, null,
					Schema.APP_STATE_CONFIGURATION_KEY + "='" + key + "'",
					null, null, null, null, null); // if selection is not null
													// then return
			// selection values
			if (dbCursor.moveToFirst()) {

				retValue = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.APP_STATE_CONFIGURATION_VALUE));
			}

		} catch (Exception e) {
			return "";
		} finally {
			dbfactory.CloseDatabase();
		}
		return retValue;
	}

	public static long deleteKey(String key, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.AppStateConfiguration);
		try {
			return dbfactory.database
					.delete(Schema.TABLE_APP_STATE_CONFIGURATION,
							Schema.APP_STATE_CONFIGURATION_KEY + "='" + key
									+ "'", null);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static int getMaxId(Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.AppStateConfiguration);
		try {
			Cursor dbcursor = dbFactory.database.query(
					Schema.TABLE_APP_STATE_CONFIGURATION,
					new String[] { Schema.APP_STATE_CONFIGURATION_VALUE },
					Schema.APP_STATE_CONFIGURATION_KEY + " = '"
							+ AppStateConfigurationKeys.Key_maxId + "'", null,
					null, null, null, null);
			if (dbcursor.moveToFirst()) {
				return dbcursor.getInt(dbcursor
						.getColumnIndex(Schema.APP_STATE_CONFIGURATION_VALUE));
			} else {
				addAppStateConfiguration(AppStateConfigurationKeys.Key_maxId,
						"0", context);
				return 0;
			}

		} catch (Exception e) {
			return 0;
		} finally {
			dbFactory.CloseDatabase();
		}
	}

	public static int updateMaxId(Context context) {
		int currMaxId = getMaxId(context);
		ContentValues maxId = new ContentValues();
		maxId.put(Schema.APP_STATE_CONFIGURATION_VALUE, currMaxId + 1);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.AppStateConfiguration);

		try {
			return dbfactory.database.update(
					Schema.TABLE_APP_STATE_CONFIGURATION, maxId,
					Schema.APP_STATE_CONFIGURATION_KEY + "= '"
							+ AppStateConfigurationKeys.Key_maxId + "'", null);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}

	}

	public static ArrayList<String> getAllKeys(Context context) {
		ArrayList<String> retArray = new ArrayList<String>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.AppStateConfiguration);
		try {
			Cursor dbcursor = dbFactory.database.query(
					Schema.TABLE_APP_STATE_CONFIGURATION, null, null, null,
					null, null, null, null);
			while (dbcursor.moveToNext()) {
				retArray.add(

				dbcursor.getString(dbcursor
						.getColumnIndex(Schema.APP_STATE_CONFIGURATION_KEY))
						+ " = "
						+ dbcursor.getString(dbcursor
								.getColumnIndex(Schema.APP_STATE_CONFIGURATION_VALUE)));
			}

		} catch (Exception e) {
		} finally {
			dbFactory.CloseDatabase();
		}
		return retArray;
	}
}
