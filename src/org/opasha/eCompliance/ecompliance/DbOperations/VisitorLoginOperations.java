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
import org.opasha.eCompliance.ecompliance.Model.Visitor;
import org.opasha.eCompliance.ecompliance.modal.wcf.VisitorLogin;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class VisitorLoginOperations {

	public static long addVisitorLogin(String ID, long loginTimeStamp,
			String machineId, double latitude, double longitude, Context context) {

		ContentValues loginValues = new ContentValues();
		loginValues.put(Schema.VISITOR_LOGIN_VISITOR_ID, ID);
		loginValues.put(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP, loginTimeStamp);
		loginValues.put(Schema.VISITOR_LOGIN_MACHINE_ID, machineId);
		loginValues.put(Schema.VISITOR_LOGIN_LATITUDE, latitude);
		loginValues.put(Schema.VISITOR_LOGIN_LONGITUDE, longitude);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);
		try {
			return dbfactory.database.insert(Schema.TABLE_VISITOR_LOGIN, null,
					loginValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long getLastLogin(String visitorId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);
		long lastLogin = 0;
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_VISITOR_LOGIN, null,
					Schema.VISITOR_LOGIN_VISITOR_ID + "='" + visitorId + "'",
					null, null, null, Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP,
					null);
			if (dbCursor.moveToLast()) {
				lastLogin = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return lastLogin;
	}
	
	public static String getLastVisitor(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);
		String visitorId ="";
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_VISITOR_LOGIN,
					null, null, null, null, null,
					Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				visitorId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.VISITOR_LOGIN_VISITOR_ID));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return visitorId;
	}

	public static ArrayList<Visitor> getVisitforDay(long date, Context context) {
		ArrayList<Visitor> returnlist = new ArrayList<Visitor>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);

		long nextDay = date + 86400000;
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_VISITOR_LOGIN,
					new String[] { Schema.VISITOR_LOGIN_VISITOR_ID },
					Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP + " > " + date
							+ " and " + Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP
							+ " < " + nextDay, null, null, null,
					Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP, null);

			if (dbCursor.moveToFirst()) {
				do {
					Visitor visitor = new Visitor();
					visitor.visitorID = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITOR_LOGIN_VISITOR_ID));
					returnlist.add(visitor);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnlist;
	}

	public static ArrayList<VisitorLogin> VisitorloginsSync(String query,
			Context context) {
		ArrayList<VisitorLogin> returnlist = new ArrayList<VisitorLogin>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);

		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_VISITOR_LOGIN, null, query, null, null, null,
					Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP, null);

			if (dbCursor.moveToFirst()) {
				do {
					VisitorLogin visitor = new VisitorLogin();
					visitor.Latitude = dbCursor.getDouble(dbCursor
							.getColumnIndex(Schema.VISITOR_LOGIN_LATITUDE));
					visitor.Longitude = dbCursor.getDouble(dbCursor
							.getColumnIndex(Schema.VISITOR_LOGIN_LONGITUDE));
					visitor.Visitor_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITOR_LOGIN_VISITOR_ID));
					visitor.Login_TimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP)));
					visitor.Tab_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.VISITOR_LOGIN_MACHINE_ID));
					returnlist.add(visitor);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnlist;
	}

	public static long getLastLoginVisitor(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);
		long lastLogin = 0;
		Cursor dbCursor;
		String visitorType = "";
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_VISITOR_LOGIN,
					null, null, null, null, null,
					Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP, null);

			if (dbCursor.moveToLast()) {
				do {
					visitorType = VisitorsOperations
							.getVisitorType(
									dbCursor.getString(dbCursor
											.getColumnIndex(Schema.VISITOR_LOGIN_VISITOR_ID)),
									context);
					lastLogin = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP));
				} while ((visitorType.equals(Enums.VisitorType.Other))
						& dbCursor.moveToPrevious());

			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return lastLogin;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VisitorLogin);
		try {
			dbfactory.database.delete(Schema.TABLE_VISITOR_LOGIN, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

}
