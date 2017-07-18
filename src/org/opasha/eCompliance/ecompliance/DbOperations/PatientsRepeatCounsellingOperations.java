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

public class PatientsRepeatCounsellingOperations {

	public static void addRepeatCounsellingData(String treatmentId,
			String tabId, Context context) {
		ContentValues repeatCounselling = new ContentValues();
		repeatCounselling.put(Schema.REPEAT_COUNSELLING_TREATMENTID,
				treatmentId);
		repeatCounselling.put(Schema.REPEAT_COUNSELLING_TABID, tabId);
		repeatCounselling.put(Schema.REPEAT_COUNSELLING_TIMESTAMP,
				System.currentTimeMillis());

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RepeatCounselling);
		try {
			dbfactory.database.insert(Schema.TABLE_REPEAT_COUNSELLING, null,
					repeatCounselling);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static void HardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RepeatCounselling);
		try {
			dbfactory.database.delete(Schema.TABLE_REPEAT_COUNSELLING,
					Schema.REPEAT_COUNSELLING_TREATMENTID + "= '" + treatmentId
							+ "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}

	public static void Delete(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RepeatCounselling);
		try {
			dbfactory.database.delete(Schema.TABLE_REPEAT_COUNSELLING, null,
					null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}

	public static boolean patientExists(String treatmentID, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RepeatCounselling);
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_REPEAT_COUNSELLING, null,
					Schema.REPEAT_COUNSELLING_TREATMENTID + "='" + treatmentID + "'",
					null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				String treatment_Id = dbCursor.getString(dbCursor.getColumnIndex(Schema.REPEAT_COUNSELLING_TREATMENTID));
				if(treatment_Id != null && treatment_Id.equals(treatmentID))
					isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}

}
