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
import org.opasha.eCompliance.ecompliance.Model.Master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DoseTypeMasterOperations {

	public static long addDoseType(int id, String name, boolean isActive,
			Context context) {
		ContentValues doseType = new ContentValues();
		doseType.put(Schema.DOSE_TYPE_MASTER_ID, id);
		doseType.put(Schema.DOSE_TYPE_MASTER_NAME, name);
		doseType.put(Schema.DOSE_TYPE_MASTER_IS_ACTIVE, isActive);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseTypeMaster);
		try {
			return dbfactory.database.insert(
					Schema.TABLE_DOSE_TYPE_MASTER, null, doseType);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}
	
	public static ArrayList<Master> getDoseType(String filterExpression,
			Context context) {
		ArrayList<Master> doseTypeList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseTypeMaster);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_TYPE_MASTER, null, null, null,
						null, null, null, null); // if selection is null return
													// all
													// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_TYPE_MASTER, null,
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
					Master doseType = new Master();
					doseType.setDoseType(
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.DOSE_TYPE_MASTER_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_TYPE_MASTER_NAME)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_TYPE_MASTER_IS_ACTIVE))));
					doseTypeList.add(doseType);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return doseTypeList;
	}
	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseTypeMaster);
		try {
			dbfactory.database.delete(Schema.TABLE_DOSE_TYPE_MASTER, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

}
