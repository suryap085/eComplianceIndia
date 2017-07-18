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
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question;
import org.opasha.eCompliance.ecompliance.Model.Master_QA_Question_List;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Master_QA_Questions {
	public static long AddQuestions(int Id, String name, boolean isActive,
			String language, int type, int priority, int parentId,
			int userType, Context context) {
		deleteQuestion(Id, context);
		ContentValues quest = new ContentValues();
		quest.put(Schema.MASTER_AUDIT_QUESTION_ID, Id);
		quest.put(Schema.MASTER_AUDIT_QUESTION_NAME, name);
		quest.put(Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE, isActive);
		quest.put(Schema.MASTER_AUDIT_QUESTION_LANGUAGE, language);
		quest.put(Schema.MASTER_AUDIT_QUESTION_USER_TYPE, userType);
		quest.put(Schema.MASTER_AUDIT_QUESTION_TYPE, type);
		quest.put(Schema.MASTER_AUDIT_QUESTION_PARENT_ID, parentId);
		quest.put(Schema.MASTER_AUDIT_QUESTION_PRIORITY, priority);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestions);
		try {
			return dbFactory.database.insert(
					Schema.TABLE_MASTER_AUDIT_QUESTION, null, quest);
		} catch (Exception e) {
			return -1;
		} finally {
			dbFactory.CloseDatabase();
		}
	}

	public static void deleteall(Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestions);
		try {
			dbFactory.database.delete(Schema.TABLE_MASTER_AUDIT_QUESTION, null,
					null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	private static void deleteQuestion(int id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestions);
		try {
			dbFactory.database.delete(Schema.TABLE_MASTER_AUDIT_QUESTION,
					Schema.MASTER_AUDIT_QUESTION_ID + "='" + id + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static ArrayList<Master_QA_Question_List> getViewList(String qa_id,
			String query, Context context) {
		if (query == null)
			query = "";
		ArrayList<Master_QA_Question_List> retList = new ArrayList<Master_QA_Question_List>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterAuditQuestions);
		try {
			Cursor dbCursor;
			if (query.isEmpty()) {
				dbCursor = dbFactory.database.query(
						Schema.TABLE_MASTER_AUDIT_QUESTION, null,
						Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE + " = 1", null,
						null, null, null);
			} else {
				dbCursor = dbFactory.database.query(
						Schema.TABLE_MASTER_AUDIT_QUESTION, null, query, null,
						null, null, Schema.MASTER_AUDIT_QUESTION_TYPE);
			}
			while (dbCursor.moveToNext()) {
				if (dbCursor
						.getInt(dbCursor
								.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_PARENT_ID)) != 0) {
					Master_QA_Question obj = new Master_QA_Question();
					obj.MASTER_AUDIT_QUESTION_ID = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_ID));
					obj.MASTER_AUDIT_QUESTION_IS_ACTIVE = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE)));
					obj.MASTER_AUDIT_QUESTION_LANGUAGE = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_LANGUAGE));
					obj.MASTER_AUDIT_QUESTION_NAME = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_NAME));
					obj.MASTER_AUDIT_QUESTION_PARENT_ID = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_PARENT_ID));
					obj.MASTER_AUDIT_QUESTION_PRIORITY = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_PRIORITY));
					obj.MASTER_AUDIT_QUESTION_TYPE = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_TYPE));
					obj.MASTER_AUDIT_QUESTION_USER_TYPE = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_USER_TYPE));
					for (int i = 0; i < retList.size() - 1; i++) {
						if (retList.get(i).MASTER_AUDIT_QUESTION_ID == obj.MASTER_AUDIT_QUESTION_PARENT_ID) {
							retList.get(i).Childs.add(obj);
							break;
						}
					}
				} else {
					Master_QA_Question_List obj = new Master_QA_Question_List();
					obj.MASTER_AUDIT_QUESTION_ID = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_ID));
					obj.MASTER_AUDIT_QUESTION_IS_ACTIVE = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE)));
					obj.MASTER_AUDIT_QUESTION_LANGUAGE = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_LANGUAGE));
					obj.MASTER_AUDIT_QUESTION_NAME = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_NAME));
					obj.MASTER_AUDIT_QUESTION_PARENT_ID = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_PARENT_ID));
					obj.MASTER_AUDIT_QUESTION_PRIORITY = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_PRIORITY));
					obj.MASTER_AUDIT_QUESTION_TYPE = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_TYPE));

					obj.MASTER_AUDIT_QUESTION_USER_TYPE = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.MASTER_AUDIT_QUESTION_USER_TYPE));
					if (obj.MASTER_AUDIT_QUESTION_TYPE == 1) {
						// for check boxes
						try {
							obj.MASTER_AUDIT_QUESTION_USER_Value = Boolean
									.parseBoolean(QualityAuditingOperations
											.getquestionAnswer(
													qa_id,
													obj.MASTER_AUDIT_QUESTION_ID,
													context));

						} catch (Exception e) {
							// TODO: handle exception
						}

					} else {
						// for text box and spinner
						obj.MASTER_AUDIT_QUESTION_USER_TEXT_Value = QualityAuditingOperations
								.getquestionAnswer(qa_id,
										obj.MASTER_AUDIT_QUESTION_ID, context);
					}
					if (obj.MASTER_AUDIT_QUESTION_TYPE == 2) {
						obj.MASTER_AUDIT_QUESTION_USER_SPINNER_OPTION = MasterAuditQuesSpnValueOperations
								.getvalue(
										Schema.MASTER_AUDIT_QUESTION_SPN_IS_ACTIVE
												+ "=1 and "
												+ Schema.MASTER_AUDIT_QUESTION_SPN_Q_ID
												+ "='"
												+ obj.MASTER_AUDIT_QUESTION_ID
												+ "'", context);
					}
					retList.add(obj);
				}
			}
		} catch (Exception e) {
		}
		return retList;
	}
}
