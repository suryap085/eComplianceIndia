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

public class StageMasterOperations {

	public static long addStage(int id, String name, boolean isActive,
			Context context) {
		ContentValues stages = new ContentValues();
		stages.put(Schema.STAGE_MASTER_ID, id);
		stages.put(Schema.STAGE_MASTER_NAME, name);
		stages.put(Schema.STAGE_MASTER_IS_ACTIVE, isActive);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.StageMaster);
		try {
			return dbfactory.database.insert(Schema.TABLE_STAGE_MASTER, null,
					stages);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}
	
	public static ArrayList<Master> getStages(String filterExpression,
			Context context) {
		ArrayList<Master> stageList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.StageMaster);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_STAGE_MASTER, null, null, null,
						null, null, null, null); // if selection is null return
													// all
													// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_STAGE_MASTER, null,
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
					Master stage = new Master();
					stage.setStage(
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.STAGE_MASTER_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.STAGE_MASTER_NAME)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.STAGE_MASTER_IS_ACTIVE))));
					stageList.add(stage);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return stageList;
	}
}
