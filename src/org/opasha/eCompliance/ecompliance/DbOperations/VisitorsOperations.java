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
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.modal.wcf.VisitorInfo;
import org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.VisitorViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class VisitorsOperations {

	public static long addVisitor(String ID, String name, String visitorType,
			long registrationDate, String status, String phone1,
			boolean isAuthenticated, long creationTimeStamp, String createdBy,
			boolean isDeleted, String tabId, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(ID, creationTimeStamp, context)) {
			return 1;
		}
		deleteVisitorHard(ID, context);
		ContentValues visitorValues = new ContentValues();
		visitorValues.put(Schema.VISITORS_ID, ID);
		visitorValues.put(Schema.VISITORS_NAME, name);
		visitorValues.put(Schema.VISITORS_VISITOR_TYPE, visitorType);
		visitorValues.put(Schema.VISITORS_REGISTRATION_DATE, registrationDate);
		visitorValues.put(Schema.VISITORS_STATUS, status);
		visitorValues.put(Schema.VISITORS_PHONE1, phone1);
		visitorValues.put(Schema.VISITORS_IS_AUTHENTICATED, isAuthenticated);
		visitorValues.put(Schema.VISITORS_TAB_ID, tabId);
		visitorValues
				.put(Schema.VISITORS_CREATION_TIMESTAMP, creationTimeStamp);
		visitorValues.put(Schema.VISITORS_CREATED_BY, createdBy);
		visitorValues.put(Schema.VISITORS_IS_DELETED, isDeleted);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		long returnValue = -1;
		try {
			returnValue = dbfactory.database.insert(Schema.TABLE_VISITORS,
					null, visitorValues);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnValue;
	}

	public static long bulkInsert(ArrayList<VisitorViewModel> v, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);

		dbfactory.database.beginTransaction();
		for (VisitorViewModel view : v) {
			ContentValues visitorValues = new ContentValues();
			visitorValues.put(Schema.VISITORS_ID, view.Visitor_Id);
			visitorValues.put(Schema.VISITORS_NAME, view.Name);
			visitorValues.put(Schema.VISITORS_VISITOR_TYPE, view.Visitor_Type);
			visitorValues.put(Schema.VISITORS_REGISTRATION_DATE, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Registration_Date)));
			visitorValues.put(Schema.VISITORS_STATUS, view.Status);
			visitorValues.put(Schema.VISITORS_PHONE1, view.Phone_Number);
			visitorValues.put(Schema.VISITORS_IS_AUTHENTICATED,
					view.Is_Authenticated);
			visitorValues.put(Schema.VISITORS_TAB_ID, view.Tab_id);
			visitorValues.put(Schema.VISITORS_CREATION_TIMESTAMP, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Creation_TimeStamp)));
			visitorValues.put(Schema.VISITORS_CREATED_BY, view.Created_By);
			visitorValues.put(Schema.VISITORS_IS_DELETED, view.Is_Deleted);

			dbfactory.database.insert(Schema.TABLE_VISITORS, null,
					visitorValues);
		}

		try {
			dbfactory.database.setTransactionSuccessful();
			return 0;
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.database.endTransaction();
		}
	}

	private static boolean shouldUpdate(String Id, long creationTimestamp,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		try {
			if (dbfactory.database.query(
					Schema.TABLE_VISITORS,
					null,
					Schema.VISITORS_ID + " = '" + Id + "' and "
							+ Schema.VISITORS_CREATION_TIMESTAMP + " > "
							+ creationTimestamp, null, null, null, null)
					.getCount() > 1) {
				dbfactory.CloseDatabase();
				return false;
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return true;
	}

	/**
	 * Check if Visitor Exists
	 * 
	 * @param visitorId
	 *            - Visitor Id
	 * @param context
	 * @return True: If Exists, Else False
	 */
	public static boolean visitorExists(String visitorId, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_VISITORS,
					null, Schema.VISITORS_ID + "=?",
					new String[] { visitorId }, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}

	// Verify that if Treatment_Id is already in table and is_deleted is 0
	private static long verifyVisitorIsDeleted(String visitorId, Context context) {
		long ln = -1;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_VISITORS,
					new String[] { Schema.VISITORS_ROW_ID,
							Schema.VISITORS_IS_DELETED }, Schema.VISITORS_ID
							+ "=?" + " and " + Schema.VISITORS_IS_DELETED
							+ "=?", new String[] { visitorId, "0" }, null,
					null, null, null);
			dbCursor.moveToFirst();
			ln = dbCursor.getLong(dbCursor
					.getColumnIndex(Schema.VISITORS_ROW_ID));

		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return ln;
	}

	public static String getVisitorType(String visitorId, Context context) {
		String ln = "";
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_VISITORS,
					new String[] { Schema.VISITORS_VISITOR_TYPE },
					Schema.VISITORS_ID + "=?", new String[] { visitorId },
					null, null, null, null);
			dbCursor.moveToFirst();
			ln = dbCursor.getString(dbCursor
					.getColumnIndex(Schema.VISITORS_VISITOR_TYPE));

		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return ln;
	}

	// Public function to change isDeleted of a contact
	public static int deleteVisitorSoft(String visitorId, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.VISITORS_IS_DELETED, 1);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);

		int num = dbfactory.database.update(Schema.TABLE_VISITORS, isDeleted,
				Schema.VISITORS_ID + "=?", new String[] { visitorId });

		dbfactory.CloseDatabase();
		return num;
	}

	public static void deleteVisitorHard(String visitorId, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		try {
			dbfactory.database.delete(Schema.TABLE_VISITORS, Schema.VISITORS_ID
					+ "='" + visitorId + "'", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbfactory.CloseDatabase();

	}

	public static ArrayList<Visitor> getVisitor(String filterExpression,
			Context context) {
		ArrayList<Visitor> visitorList = new ArrayList<Visitor>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_VISITORS, null, null, null, null, null,
						null, null); // if selection is null return
										// all
										// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_VISITORS, null, filterExpression, null,
						null, null, null, null); // if
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
					Visitor visitors = new Visitor();
					visitors.setVisitor(
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_NAME)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_VISITOR_TYPE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.VISITORS_REGISTRATION_DATE)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_STATUS)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_PHONE1)),
							GenUtils.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.VISITORS_IS_AUTHENTICATED))),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.VISITORS_CREATION_TIMESTAMP)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_CREATED_BY)),
							GenUtils.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.VISITORS_IS_DELETED))),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.VISITORS_TAB_ID)));
					visitorList.add(visitors);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return visitorList;
	}

	public static ArrayList<VisitorInfo> visitorSync(String filterExpression,
			Context context) {
		ArrayList<VisitorInfo> visitorList = new ArrayList<VisitorInfo>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_VISITORS, null, null, null, null, null,
						null, null); // if selection is null return
										// all
										// values from the table
			} else {
				dbCursor = dbfactory.database.query(false,
						Schema.TABLE_VISITORS, null, filterExpression, null,
						null, null, null, null); // if
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
					VisitorInfo visitors = new VisitorInfo();
					visitors.Visitor_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_ID));
					visitors.Name = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_NAME));
					visitors.Visitor_Type = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_VISITOR_TYPE));
					visitors.Tab_id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_TAB_ID));
					visitors.Registration_Date = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.VISITORS_REGISTRATION_DATE)));

					visitors.Status = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_STATUS));

					visitors.Phone_Number = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_PHONE1));

					visitors.Is_Authenticated =GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.VISITORS_IS_AUTHENTICATED)));
					visitors.Creation_TimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.VISITORS_CREATION_TIMESTAMP)));
					visitors.Created_By = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_CREATED_BY));
					visitors.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.VISITORS_IS_DELETED)));

					visitorList.add(visitors);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return visitorList;
	}

	public static ArrayList<String> getVisitorsForScans(Context context) {
		ArrayList<String> visitorList = new ArrayList<String>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		Cursor dbCursor;
		try {
			
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_VISITORS, null, Schema.VISITORS_IS_DELETED + "=0 AND " + Schema.VISITORS_STATUS + " IN ('Active')", null,
						null, null, null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values

			if (dbCursor.moveToFirst()) {

				do {
					visitorList.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_ID)));

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return visitorList;
	}

	
	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		try {
			dbfactory.database.delete(Schema.TABLE_VISITORS, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}
	
	public static ArrayList<Visitor> getPatientsFromLegacySystemFp(Context context) {
		ArrayList<Visitor> VisitorList = new ArrayList<Visitor>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		Cursor dbCursor;
		try {

			dbCursor = dbfactory.database.query(
					true,
					Schema.TABLE_VISITORS,
					null,
					Schema.VISITORS_IS_DELETED + " = 0 and "
							+ Schema.VISITORS_STATUS + "='"
							+ Enums.StatusType.getStatusType(StatusType.Active)
							+ "'", null, null, null, null, null); // if

			if (dbCursor.moveToFirst()) {

				do {
					String id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_ID));
					if (!ScansOperations.isTreatmentidIdExist(id, context)) {
						Visitor visitors = new Visitor();
						visitors.setVisitor(
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_NAME)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_VISITOR_TYPE)),
								dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.VISITORS_REGISTRATION_DATE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_STATUS)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_PHONE1)),
								GenUtils.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.VISITORS_IS_AUTHENTICATED))),
								dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.VISITORS_CREATION_TIMESTAMP)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_CREATED_BY)),
								GenUtils.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.VISITORS_IS_DELETED))),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_TAB_ID)));
						VisitorList.add(visitors);
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return VisitorList;
	}
	
	
	public static ArrayList<Visitor> getPatientsFromLegacySystemIris(Context context) {
		ArrayList<Visitor> VisitorList = new ArrayList<Visitor>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		Cursor dbCursor;
		try {

			dbCursor = dbfactory.database.query(
					true,
					Schema.TABLE_VISITORS,
					null,
					Schema.VISITORS_IS_DELETED + " = 0 and "
							+ Schema.VISITORS_STATUS + "='"
							+ Enums.StatusType.getStatusType(StatusType.Active)
							+ "'", null, null, null, null, null); // if

			if (dbCursor.moveToFirst()) {

				do {
					String id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITORS_ID));
					if (!ScansIrisOperations.isTreatmentidIdExist(id, context)) {
						Visitor visitors = new Visitor();
						visitors.setVisitor(
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_NAME)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_VISITOR_TYPE)),
								dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.VISITORS_REGISTRATION_DATE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_STATUS)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_PHONE1)),
								GenUtils.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.VISITORS_IS_AUTHENTICATED))),
								dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.VISITORS_CREATION_TIMESTAMP)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_CREATED_BY)),
								GenUtils.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.VISITORS_IS_DELETED))),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.VISITORS_TAB_ID)));
						VisitorList.add(visitors);
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return VisitorList;
	}
	
	
}
