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
import org.opasha.eCompliance.ecompliance.util.EnterRegimenTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientStatusMasterOperations {

	public static void addPatientStatus(int id, String name, boolean isActive,
			Context context) {
		hardDeleteStatus(id, context);
		if (isActive == true) {
			ContentValues stages = new ContentValues();
			stages.put(Schema.PATIENT_STATUS_MASTER_ID, id);
			stages.put(Schema.PATIENT_STATUS_MASTER_NAME, name);
			stages.put(Schema.PATIENT_STATUS_MASTER_IS_ACTIVE, isActive);
			DbFactory dbfactory = new DbFactory(context)
					.CreateDatabase(TableEnum.PatientStatusMaster);
			try {
				dbfactory.database.insert(Schema.TABLE_PATIENT_STATUS_MASTER,
						null, stages);
			} catch (Exception e) {
			} finally {
				dbfactory.CloseDatabase();
			}
		}
	}

	public static void hardDeleteStatus(int Id, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientStatusMaster);
		try {
			dbfactory.database.delete(Schema.TABLE_PATIENT_STATUS_MASTER,
					Schema.PATIENT_STATUS_MASTER_ID + " = " + Id, null);

		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static void enterStatus(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientStatusMaster);

		try {
			if (dbfactory.database.query(true,
					Schema.TABLE_PATIENT_STATUS_MASTER, null, null, null, null,
					null, null, null).getCount() <= 0) {
				for (int i = 0; i < 8; i++) {
					PatientStatusMasterOperations.addPatientStatus(i + 1,
							EnterRegimenTable.Status[i], true, context);
				}
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();

	}

	public static ArrayList<String> getStatuss(String query, Context context) {
		ArrayList<String> list = new ArrayList<String>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientStatusMaster);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_STATUS_MASTER, null, query, null,
					null, null, null, null);
			while (dbCursor.moveToNext()) {
				list.add(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_STATUS_MASTER_NAME)));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return list;
	}

	public static ArrayList<Master> getStatus(String filterExpression,
			Context context) {
		ArrayList<Master> statusList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientStatusMaster);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENT_STATUS_MASTER, null, null, null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENT_STATUS_MASTER, null,
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
					Master status = new Master();
					status.setPatientStatus(
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_STATUS_MASTER_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.PATIENT_STATUS_MASTER_NAME)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.PATIENT_STATUS_MASTER_IS_ACTIVE))));
					statusList.add(status);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return statusList;
	}
}
