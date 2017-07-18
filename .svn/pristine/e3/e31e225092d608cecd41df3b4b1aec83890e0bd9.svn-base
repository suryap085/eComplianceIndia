/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.DbOperations;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ConfigurationOperations {

	public static long addConfiguration(String key, String value,
			Context context) {
		ContentValues confi = new ContentValues();

		confi.put(Schema.CONFIGURATION_KEY, key);
		confi.put(Schema.CONFIGURATION_VALUE, value);
		deleteKey(key, context);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Configuration);

		try {
			return dbfactory.database.insert(Schema.TABLE_CONFIGURATION, null,
					confi);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	// Public function to change isDeleted of a contact
	public static void deleteKey(String key, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Configuration);

		try {
			dbfactory.database.delete(Schema.TABLE_CONFIGURATION,
					Schema.CONFIGURATION_KEY + " = '" + key + "'", null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static String getKeyValue(String KeyName, Context context) {
		String ln = "";
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Configuration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(
					Schema.TABLE_CONFIGURATION, null, Schema.CONFIGURATION_KEY
							+ "= '" + KeyName + "'", null, null, null, null,
					null);
			dbCursor.moveToFirst();
			ln = dbCursor.getString(dbCursor
					.getColumnIndex(Schema.CONFIGURATION_VALUE));

		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return ln;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Configuration);
		try {
			dbfactory.database.delete(Schema.TABLE_CONFIGURATION, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}
	
	public static boolean isExists(String key, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Configuration);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_CONFIGURATION,
					null, Schema.CONFIGURATION_KEY + "=?",
					new String[] { key }, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}
	
	public static int updateKey(String key, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.CONFIGURATION_VALUE, "false");

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Configuration);

		int num = -1;
		try {
			num = dbfactory.database.update(Schema.TABLE_CONFIGURATION, isDeleted,
					Schema.CONFIGURATION_KEY + "=?",
					new String[] { key });
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		return num;
	}
}
