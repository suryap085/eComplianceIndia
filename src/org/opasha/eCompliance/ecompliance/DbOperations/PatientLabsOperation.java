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
import org.opasha.eCompliance.ecompliance.modal.wcf.PatientLabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientLabsOperation {
	public static void addLab(String treatmentId, int labResultId,
			Context context) {
		ContentValues labs = new ContentValues();
		labs.put(Schema.LAB_TREATMENT_ID, treatmentId);
		labs.put(Schema.LAB_RESULT_ID, labResultId);
		labs.put(Schema.LAB_CREATION_TIMESTAMP, System.currentTimeMillis());

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientLabs);
		try {
			dbfactory.database.insert(Schema.TABLE_LAB, null, labs);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static void labHardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientLabs);
		try {
			dbfactory.database.delete(Schema.TABLE_LAB, Schema.LAB_TREATMENT_ID
					+ "= '" + treatmentId + "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}

	public static ArrayList<PatientLabs> getLabs(String query, Context context) {
		ArrayList<PatientLabs> retList = new ArrayList<PatientLabs>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientLabs);
		try {
			Cursor dbCursor = dbFactory.database.query(false, Schema.TABLE_LAB,
					null, query, null, null, null,
					Schema.LAB_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToFirst()) {
				do {
					PatientLabs labs = new PatientLabs();
					labs.Treatment_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.LAB_TREATMENT_ID));
					labs.LabResult = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.LAB_RESULT_ID));
					retList.add(labs);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retList;
	}

}
