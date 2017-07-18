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

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MasterAuditQuesSpnValueOperations {
	public static long AddQuestions(int id, int qId, boolean isActive,
			String language, int priority, String value, Context context) {
		deleteQuestion(id, context);
		ContentValues quest = new ContentValues();
		quest.put(Schema.MASTER_AUDIT_QUESTION_SPN_ID, id);
		quest.put(Schema.MASTER_AUDIT_QUESTION_SPN_IS_ACTIVE, isActive);
		quest.put(Schema.MASTER_AUDIT_QUESTION_SPN_LANGUAGE, language);
		quest.put(Schema.MASTER_AUDIT_QUESTION_SPN_PRIORITY, priority);
		quest.put(Schema.MASTER_AUDIT_QUESTION_SPN_Q_ID, qId);
		quest.put(Schema.MASTER_AUDIT_QUESTION_SPN_VALUE, value);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestionsSpnValue);
		try {
			return dbFactory.database.insert(
					Schema.TABLE_MASTER_AUDIT_QUESTION_SPN, null, quest);
		} catch (Exception e) {
			return -1;
		} finally {
			dbFactory.CloseDatabase();
		}
	}

	public static void deleteall(Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestionsSpnValue);
		try {
			dbFactory.database.delete(Schema.TABLE_MASTER_AUDIT_QUESTION_SPN,
					null, null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	private static void deleteQuestion(int id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestionsSpnValue);
		try {
			dbFactory.database
					.delete(Schema.TABLE_MASTER_AUDIT_QUESTION_SPN,
							Schema.MASTER_AUDIT_QUESTION_SPN_ID + "='" + id
									+ "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static ArrayList<String> getvalue(String query, Context context) {
		if (query == null)
			query = "";
		ArrayList<String> retList = new ArrayList<String>();

		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestionsSpnValue);
		try {
			Cursor dbCursor;
			if (query.isEmpty()) {
				dbCursor = dbFactory.database.query(
						Schema.TABLE_MASTER_AUDIT_QUESTION_SPN, null,
						Schema.MASTER_AUDIT_QUESTION_SPN_IS_ACTIVE + " = 1",
						null, null, null, null);
			} else {
				dbCursor = dbFactory.database.query(
						Schema.TABLE_MASTER_AUDIT_QUESTION_SPN, null, query,
						null, null, null,
						Schema.MASTER_AUDIT_QUESTION_SPN_PRIORITY);
			}
			while (dbCursor.moveToNext()) {
				if (retList.size() == 0)
					retList.add(context.getResources().getString(
							R.string.select));
				retList.add(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_SPN_VALUE)));

			}
		} catch (Exception e) {

		}
		return retList;
	}
}
