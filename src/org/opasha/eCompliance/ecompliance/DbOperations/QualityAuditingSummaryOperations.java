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
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.AUDITS_Summary;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.QaSummaryReportObj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class QualityAuditingSummaryOperations {

	public static long AddQuestions(int Id, String questionText, long time,
			boolean is_deleted, Boolean is_Final, String QA_ID, int answer,
			Context context) {
		deletequestion(Id, QA_ID, context);
		ContentValues quest = new ContentValues();
		quest.put(Schema.AUDIT_QUESTION_SUM_ID, Id);
		quest.put(Schema.AUDIT_QUESTION_SUM_QUESTION_TEXT, questionText);
		quest.put(Schema.AUDIT_QUESTION_SUM_CREATION_TIME, time);
		quest.put(Schema.AUDIT_QUESTION_SUM_QA_ID, QA_ID);
		quest.put(Schema.AUDIT_QUESTION_SUM_ISFINAL, is_Final);
		quest.put(Schema.AUDIT_QUESTION_SUM_ANSWER, answer);
		quest.put(Schema.AUDIT_QUESTION_SUM_IS_DELETED, is_deleted);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			return dbFactory.database.insert(Schema.TABLE_AUDIT_QUESTION_SUM,
					null, quest);
		} catch (Exception e) {
			return -1;
		} finally {
			dbFactory.CloseDatabase();
		}
	}

	public static long AddQuestions(int Id, String questionText, long time,
			boolean is_deleted, Boolean is_Final, String QA_ID, String answer,
			Context context) {
		deletequestion(Id, QA_ID, context);
		ContentValues quest = new ContentValues();
		quest.put(Schema.AUDIT_QUESTION_SUM_ID, Id);
		quest.put(Schema.AUDIT_QUESTION_SUM_QUESTION_TEXT, questionText);
		quest.put(Schema.AUDIT_QUESTION_SUM_CREATION_TIME, time);
		quest.put(Schema.AUDIT_QUESTION_SUM_QA_ID, QA_ID);
		quest.put(Schema.AUDIT_QUESTION_SUM_ISFINAL, is_Final);
		quest.put(Schema.AUDIT_QUESTION_SUM_ANSWER, answer);
		quest.put(Schema.AUDIT_QUESTION_SUM_IS_DELETED, is_deleted);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			return dbFactory.database.insert(Schema.TABLE_AUDIT_QUESTION_SUM,
					null, quest);
		} catch (Exception e) {
			return -1;
		} finally {
			dbFactory.CloseDatabase();
		}
	}

	public static QaSummaryReportObj getPreviousAnswer(String qa_id,
			Context context) {
		QaSummaryReportObj obj = new QaSummaryReportObj();
		DbFactory factory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			Cursor dbCursor = factory.database.query(
					Schema.TABLE_AUDIT_QUESTION_SUM, null,
					Schema.AUDIT_QUESTION_SUM_ISFINAL + " =0 and "
							+ Schema.AUDIT_QUESTION_SUM_QA_ID + " ='" + qa_id
							+ "'", null, null, null, null);
			while (dbCursor.moveToNext()) {
				int id = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ID));
				switch (id) {
				case 1:
					obj.closingLastMnt = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 2:
					obj.newPatients = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 3:
					obj.totalOutcome = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 4:
					obj.dieds = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 5:
					obj.cur = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 6:
					obj.tc = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 7:
					obj.defaultNo = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 8:
					obj.tranOut = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 9:
					obj.failures = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 10:
					obj.stomdr = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 11:
					obj.closingbalancetill = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 12:
					obj.card = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 13:
					obj.boxes = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 14:
					obj.tbNo = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				case 15:
					obj.Comments = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return obj;
	}

	private static void deletequestion(int id, String qA_ID, Context context) {
		DbFactory factory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			factory.database.delete(Schema.TABLE_AUDIT_QUESTION_SUM,
					Schema.AUDIT_QUESTION_SUM_QA_ID + "= '" + qA_ID + "' and "
							+ Schema.AUDIT_QUESTION_SUM_ISFINAL + "=0 and "
							+ Schema.AUDIT_QUESTION_SUM_ID + " = " + id, null);
		} catch (Exception e) {

		}
		factory.CloseDatabase();

	}

	public static String getquestionAnswer(String id, int questionid,
			Context context) {
		String retString = "";
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_AUDIT_QUESTION_SUM, null,
					Schema.AUDIT_QUESTION_SUM_ID + " = " + questionid + " and "
							+ Schema.AUDIT_QUESTION_SUM_QA_ID + "= '" + id
							+ "' and " + Schema.AUDIT_QUESTION_SUM_ISFINAL
							+ "=0", null, null, null, null);
			if (dbCursor.moveToFirst()) {
				retString = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retString;
	}

	public static void QuestionsFinalize(String qaId, long creationTime,
			Context context) {
		// long interval = Long.valueOf(ConfigurationOperations.getKeyValue(
		// ConfigurationKeys.key_auto_question_delete_duration, context));
		// long current = System.currentTimeMillis();
		// long deletetime = 0;
		// deletetime = current - interval;
		ContentValues val = new ContentValues();
		val.put(Schema.AUDIT_QUESTION_SUM_ISFINAL, true);
		val.put(Schema.AUDIT_QUESTION_SUM_CREATION_TIME, creationTime);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			dbFactory.database.update(Schema.TABLE_AUDIT_QUESTION_SUM, val,
					Schema.AUDIT_QUESTION_SUM_ISFINAL + "=0 and "
							+ Schema.AUDIT_QUESTION_SUM_QA_ID + " ='" + qaId
							+ "'", null);
		} catch (Exception e) {
		} finally {
			dbFactory.CloseDatabase();
		}
	}

	private static void deleteQuestions(String id, Context context) {
		DbFactory factory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			factory.database.delete(Schema.TABLE_AUDIT_QUESTION_SUM,
					Schema.AUDIT_QUESTION_SUM_QA_ID + ">= '" + id + "' and "
							+ Schema.AUDIT_QUESTION_SUM_ISFINAL + "=0 ", null);
		} catch (Exception e) {

		}
		factory.CloseDatabase();
	}

	public static ArrayList<AUDITS_Summary> sync(String query, Context context) {
		ArrayList<AUDITS_Summary> retList = new ArrayList<AUDITS_Summary>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Audit_Question_sum);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_AUDIT_QUESTION_SUM, null, query, null, null,
					null, null);
			while (dbCursor.moveToNext()) {
				boolean isFinal = GenUtils.getBoolean(dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ISFINAL)));
				if (isFinal) {
					AUDITS_Summary obj = new AUDITS_Summary();
					obj.Creation_Timestamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.AUDIT_QUESTION_SUM_CREATION_TIME)));
					obj.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.AUDIT_QUESTION_SUM_IS_DELETED)));
					obj.Is_Final = isFinal;
					obj.Question_Text = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.AUDIT_QUESTION_SUM_QUESTION_TEXT));
					obj.Qa_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_QA_ID));
					obj.Question_Answer = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ANSWER));
					obj.Question_Id = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.AUDIT_QUESTION_SUM_ID));
					retList.add(obj);
				}
			}
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
		return retList;
	}
}
