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
import org.opasha.eCompliance.ecompliance.Model.Reason;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UnsupervisedReasonMasterOperations extends Activity {

	private static final int reasonId = 0;
	private static final String unsupervisedDoseReason = null;

	public static void addReason(int Id, String Reason, boolean isActive,
			Context context) {
		hardDeleteReason(Id, context);
		if (isActive == true) {
			ContentValues reason = new ContentValues();
			reason.put(Schema.REASON_MASTER_ID, Id);
			reason.put(Schema.REASON_MASTER_REASON, Reason);
			reason.put(Schema.REASON_MASTER_IS_ACTIVE, isActive);

			DbFactory dbfactory = new DbFactory(context)
					.CreateDatabase(TableEnum.ReasonMaster);

			try {
				dbfactory.database.insert(Schema.TABLE_REASON_MASTER, null,
						reason);

			} catch (Exception e) {

			} finally {
				dbfactory.CloseDatabase();
			}
		}

	}

	public static void hardDeleteReason(int Id, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ReasonMaster);

		try {
			dbfactory.database.delete(Schema.TABLE_REASON_MASTER,
					Schema.REASON_MASTER_ID + " = " + Id, null);

		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}

	}

	public static ArrayList<Reason> getReason(String filterExpression,
			Context context) {
		ArrayList<Reason> Reasonlist = new ArrayList<Reason>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ReasonMaster);

		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REASON_MASTER, null,
						Schema.REASON_MASTER_IS_ACTIVE + "=1", null, null,
						null, null, null);// if selection is null
											// return
											// all
											// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REASON_MASTER, null, filterExpression,
						null, null, null, null, null); // if
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

					Reason reason = new Reason(reasonId, unsupervisedDoseReason);

					int reasonId = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.REASON_MASTER_ID));
					String unsupervisedDoseReason = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REASON_MASTER_REASON));
					int reasonIsActive = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.REASON_MASTER_IS_ACTIVE));

					reason.setReason(reasonId, unsupervisedDoseReason,
							reasonIsActive);
					Reasonlist.add(reason);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return Reasonlist;
	}

}
