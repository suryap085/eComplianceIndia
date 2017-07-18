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
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.AUDITS;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class QualityAuditingOperations {

	public static long AddQuestions(int Id, boolean is_final, long time,
			boolean is_deleted, String QA_ID, String answer, Context context) {
		deletequestion(Id, QA_ID, context);
		ContentValues quest = new ContentValues();
		quest.put(Schema.AUDIT_QUESTION_ID, Id);
		quest.put(Schema.AUDIT_QUESTION_ISFINAL, is_final);
		quest.put(Schema.AUDIT_QUESTION_CREATION_TIME, time);
		quest.put(Schema.AUDIT_QUESTION_QA_ID, QA_ID);
		quest.put(Schema.AUDIT_QUESTION_ANSWER, answer);
		quest.put(Schema.AUDIT_QUESTION_IS_DELETED, is_deleted);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			return dbFactory.database.insert(Schema.TABLE_AUDIT_QUESTION, null,
					quest);
		} catch (Exception e) {
			return -1;
		}

		finally {
			dbFactory.CloseDatabase();
		}
	}

	private static void deletequestion(int id, String qA_ID, Context context) {
		DbFactory factory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			factory.database.delete(Schema.TABLE_AUDIT_QUESTION,
					Schema.AUDIT_QUESTION_QA_ID + "= '" + qA_ID + "' and "
							+ Schema.AUDIT_QUESTION_ISFINAL + "=0 and "
							+ Schema.AUDIT_QUESTION_ID + " = " + id, null);
		} catch (Exception e) {

		}
		factory.CloseDatabase();

	}

	public static String getquestionAnswer(String id, int questionid,
			Context context) {
		String retString = "";
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_AUDIT_QUESTION, null, Schema.AUDIT_QUESTION_ID
							+ " = " + questionid + " and "
							+ Schema.AUDIT_QUESTION_QA_ID + "= '" + id
							+ "' and " + Schema.AUDIT_QUESTION_ISFINAL + "=0",
					null, null, null, null);
			if (dbCursor.moveToFirst()) {
				retString = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.AUDIT_QUESTION_ANSWER));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retString;
	}

	public static void QuestionsFinalize(String qaId, Context context) {
		// long interval = Long.valueOf(ConfigurationOperations.getKeyValue(
		// ConfigurationKeys.key_auto_question_delete_duration, context));
		// long current = System.currentTimeMillis();
		// long deletetime = 0;
		// deletetime = current - interval;
		long creationTime = System.currentTimeMillis();
		ContentValues val = new ContentValues();
		val.put(Schema.AUDIT_QUESTION_ISFINAL, true);
		val.put(Schema.AUDIT_QUESTION_CREATION_TIME, creationTime);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			dbFactory.database.update(Schema.TABLE_AUDIT_QUESTION, val,
					Schema.AUDIT_QUESTION_ISFINAL + "=0 and "
							+ Schema.AUDIT_QUESTION_QA_ID + " ='" + qaId + "'",
					null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		QualityAuditingSummaryOperations.QuestionsFinalize(qaId, creationTime,
				context);
	}

	public static boolean isAnyPendingAudit(String id, Context context) {
		boolean retValue = false;
		DbFactory factory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			Cursor dbCursor;
			dbCursor = factory.database.query(Schema.TABLE_AUDIT_QUESTION,
					null, Schema.AUDIT_QUESTION_QA_ID + "= '" + id + "' and "
							+ Schema.AUDIT_QUESTION_ISFINAL + " =0 and "
							+ Schema.AUDIT_QUESTION_IS_DELETED + " =0", null,
					null, null, null);
			if (dbCursor.moveToFirst()) {
				try {
					if ((System.currentTimeMillis()
							- dbCursor
									.getLong(dbCursor
											.getColumnIndex(Schema.AUDIT_QUESTION_CREATION_TIME)) > Long
								.parseLong(ConfigurationOperations
										.getKeyValue(
												ConfigurationKeys.key_auto_question_delete_duration,
												context)))) {
						deleteQuestions(id, context);
					} else {
						retValue = true;
					}
				} catch (Exception e) {
					LoggerOperations
							.add(context,
									"key key_auto_question_delete_duration",
									"key_auto_question_delete_duration is not configured");
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		factory.CloseDatabase();
		return retValue;
	}

	private static void deleteQuestions(String id, Context context) {
		DbFactory factory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			factory.database.delete(Schema.TABLE_AUDIT_QUESTION,
					Schema.AUDIT_QUESTION_QA_ID + ">= '" + id + "' and "
							+ Schema.AUDIT_QUESTION_ISFINAL + "=0 ", null);
		} catch (Exception e) {

		}
		factory.CloseDatabase();
	}

	public static ArrayList<AUDITS> sync(String query, Context context) {
		ArrayList<AUDITS> retList = new ArrayList<AUDITS>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_AUDIT_QUESTION, null, query, null, null, null,
					null);
			while (dbCursor.moveToNext()) {
				boolean isFinal = GenUtils.getBoolean(dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.AUDIT_QUESTION_ISFINAL)));
				if (isFinal) {
					AUDITS obj = new AUDITS();
					obj.Creation_Timestamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.AUDIT_QUESTION_CREATION_TIME)));
					obj.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.AUDIT_QUESTION_IS_DELETED)));
					obj.Is_Final = isFinal;
					obj.Qa_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_QA_ID));
					obj.Question_Answer = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_ANSWER));
					obj.Question_Id = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_ID));
					retList.add(obj);
				}
			}
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
		return retList;
	}
}
