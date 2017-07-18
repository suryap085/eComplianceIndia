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
import org.opasha.eCompliance.ecompliance.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class InitialLabMasterOperations extends Activity {

	public static void addLab(int Id, String labResult, boolean isActive,
			Context context) {
		hardDeleteReason(Id, context);
		if (isActive == true) {
			ContentValues reason = new ContentValues();
			reason.put(Schema.MASTER_LAB_ID, Id);
			reason.put(Schema.MASTER_LAB_NAME, labResult);
			reason.put(Schema.MASTER_LAB_IS_ACTIVE, isActive);
			hardDeleteReason(Id, context);
			DbFactory dbfactory = new DbFactory(context)
					.CreateDatabase(TableEnum.InitialLabMaster);

			try {
				dbfactory.database
						.insert(Schema.TABLE_MASTER_LAB, null, reason);

			} catch (Exception e) {

			} finally {
				dbfactory.CloseDatabase();
			}
		}

	}

	public static void hardDeleteReason(int Id, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabMaster);

		try {
			dbfactory.database.delete(Schema.TABLE_MASTER_LAB,
					Schema.MASTER_LAB_ID + " = " + Id, null);

		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}

	}

	public static ArrayList<String> getResults(String filter, Context context) {
		ArrayList<String> map = new ArrayList<String>();
		map.add(context.getResources().getString(R.string.selectLabResult));
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabMaster);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_MASTER_LAB,
					null, null, null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				do {

					map.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.MASTER_LAB_NAME)));
				} while (dbCursor.moveToNext());
			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return map;
	}

	public static int getId(String value, Context context) {
		int id = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabMaster);
		try {
			Cursor dbCursor = dbFactory.database.query(true,
					Schema.TABLE_MASTER_LAB, null, Schema.MASTER_LAB_NAME
							+ "='" + value + "'", null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				id = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.MASTER_LAB_ID));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return id;
	}
	
	public static String getValue(int id, Context context) {
		String value = "";
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.InitialLabMaster);
		try {
			Cursor dbCursor = dbFactory.database.query(true,
					Schema.TABLE_MASTER_LAB, null, Schema.MASTER_LAB_ID
							+ "=" + id, null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				value = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.MASTER_LAB_NAME));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return value;
	}

}
