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
import org.opasha.eCompliance.ecompliance.Model.DiabetesModal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MasterDiabetesOperations {
	public static void add(int id, String name, double minValue,
			double maxValue, boolean isActive, int testId, Context context) {
		deleteid(id, context);
		if (!isActive)
			return;
		ContentValues val = new ContentValues();
		val.put(Schema.MASTER_DIABETES_ID, id);
		val.put(Schema.MASTER_DIABETES_NAME, name);
		val.put(Schema.MASTER_DIABETES_MIN_VALUE, minValue);
		val.put(Schema.MASTER_DIABETES_MAX_VALUE, maxValue);
		val.put(Schema.MASTER_DIABETES_IS_ACTIVE, isActive);
		val.put(Schema.MASTER_DIABETES_TEST_ID, testId);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Master_Diabetes);
		try {
			dbFactory.database.insert(Schema.TABLE_MASTER_DIABETES, null, val);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private static void deleteid(int id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Master_Diabetes);
		try {
			dbFactory.database.delete(Schema.TABLE_MASTER_DIABETES,
					Schema.MASTER_DIABETES_ID + "=" + id, null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();

	}

	public static ArrayList<DiabetesModal> getMinMax(String query,
			Context context) {
		ArrayList<DiabetesModal> retList = new ArrayList<DiabetesModal>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Master_Diabetes);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_MASTER_DIABETES, null, query, null, null,
					null, null);
			while (dbCursor.moveToNext()) {
				DiabetesModal mod = new DiabetesModal();
				mod.result = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.MASTER_DIABETES_NAME));
				try {
					mod.minValue = Double
							.parseDouble(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.MASTER_DIABETES_MIN_VALUE)));
				} catch (Exception e) {
				}
				try {
					mod.maxValue = Double
							.parseDouble(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.MASTER_DIABETES_MAX_VALUE)));
				} catch (Exception e) {
				}
				retList.add(mod);
			}
		} catch (Exception e) {
		}
		return retList;

	}

}
