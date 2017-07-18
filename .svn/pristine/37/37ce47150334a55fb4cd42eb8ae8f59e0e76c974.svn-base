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
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CentersOperations {

	public static long addCenter(int centerId, String machineID,
			String machineType, String centerName, Context context) {
		ContentValues centers = new ContentValues();
		centers.put(Schema.CENTERS_MACHINE_ID, machineID);
		centers.put(Schema.CENTERS_MACHINE_TYPE, machineType);
		centers.put(Schema.CENTERS_CENTER_ID, centerId);
		centers.put(Schema.CENTERS_NAME, centerName);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		try {
			return dbfactory.database.insert(Schema.TABLE_CENTERS, null,
					centers);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	

	public static ArrayList<Center> getCenterForSpinner(Boolean needPrompt,
			Context context) {
		ArrayList<Center> center = new ArrayList<Center>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		Center Center = new Center();
		if (needPrompt == true) {
			Center.setCenterName(context.getResources().getString(
					R.string.selectCenter));
			center.add(Center);
		}
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_CENTERS, new String[] { Schema.CENTERS_NAME },
					Schema.CENTERS_MACHINE_TYPE + "= 'P'", null, null, null,
					null, null);
			if (dbCursor.moveToFirst()) {
				do {
					Center centerlist = new Center();
					centerlist.setCenterName(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.CENTERS_NAME)));
					center.add(centerlist);
				} while (dbCursor.moveToNext());
			}

		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
		}
		return center;
	}

	public static ArrayList<Center> getCenter(Context context) {
		ArrayList<Center> center = new ArrayList<Center>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					null, null, null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				do {
					Center centerlist = new Center();
					centerlist
							.setCenter(
									dbCursor.getString(dbCursor
											.getColumnIndex(Schema.CENTERS_MACHINE_ID)),
									dbCursor.getString(dbCursor
											.getColumnIndex(Schema.CENTERS_MACHINE_TYPE)),
									dbCursor.getString(dbCursor
											.getColumnIndex(Schema.CENTERS_NAME)),
									dbCursor.getInt(dbCursor
											.getColumnIndex(Schema.CENTERS_CENTER_ID)));
					center.add(centerlist);
				} while (dbCursor.moveToNext());
			}

		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
		}
		return center;
	}

	public static String getMachineId(String center, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					new String[] { Schema.CENTERS_MACHINE_ID },
					Schema.CENTERS_NAME + "='" + center + "'", null, null,
					null, null);
			dbCursor.moveToFirst();
			return dbCursor.getString(dbCursor
					.getColumnIndex(Schema.CENTERS_MACHINE_ID));

		} catch (Exception e) {
			return "";
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static String getCenterName(String machineId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					new String[] { Schema.CENTERS_NAME },
					Schema.CENTERS_MACHINE_ID + "='" + machineId + "'", null,
					null, null, null);
			dbCursor.moveToFirst();
			return dbCursor.getString(dbCursor
					.getColumnIndex(Schema.CENTERS_NAME));

		} catch (Exception e) {
			return "";
		} finally {
			dbfactory.CloseDatabase();
		}
	}
	
	public static String getCenter(int machineId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					new String[] { Schema.CENTERS_NAME },
					Schema.CENTERS_CENTER_ID + "='" + machineId + "'", null,
					null, null, null);
			dbCursor.moveToFirst();
			return dbCursor.getString(dbCursor
					.getColumnIndex(Schema.CENTERS_NAME));

		} catch (Exception e) {
			return "";
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static int getCenterId(String machineId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					new String[] { Schema.CENTERS_CENTER_ID },
					Schema.CENTERS_MACHINE_ID + "='" + machineId + "'", null,
					null, null, null);
			dbCursor.moveToFirst();
			return dbCursor.getInt(dbCursor
					.getColumnIndex(Schema.CENTERS_CENTER_ID));

		} catch (Exception e) {
			return 0;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static int getCenterIdByName(String centerName, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					new String[] { Schema.CENTERS_CENTER_ID },
					Schema.CENTERS_NAME + "='" + centerName + "'", null, null,
					null, null);
			dbCursor.moveToFirst();
			return dbCursor.getInt(dbCursor
					.getColumnIndex(Schema.CENTERS_CENTER_ID));

		} catch (Exception e) {
			return 0;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);
		try {
			dbfactory.database.delete(Schema.TABLE_CENTERS, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static String getMachineType(Context context) {

		String returnValue = "";

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);

		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					null, null, null, null, null, null);

			if (dbCursor.moveToFirst()) {

				returnValue = context.getResources().getString(
						R.string.centerMachine);
				do {
					if (dbCursor
							.getString(
									dbCursor.getColumnIndex(Schema.CENTERS_MACHINE_TYPE))
							.equals("C")) {
						returnValue = context.getResources().getString(
								R.string.provider);
					}

				} while (dbCursor.moveToNext());

			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return returnValue;

	}

	public static String getCenterName(Context context) {

		String returnValue = "";

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Centers);

		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_CENTERS,
					null, null, null, null, null, null);

			if (dbCursor.moveToFirst()) {

				returnValue = context.getResources().getString(
						R.string.centerMachine);
				do {
					if (dbCursor
							.getString(
									dbCursor.getColumnIndex(Schema.CENTERS_MACHINE_TYPE))
							.equals("C")) {
						returnValue = "";
						dbfactory.CloseDatabase();
						return returnValue;
					}
					returnValue = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.CENTERS_NAME));
				} while (dbCursor.moveToNext());

			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return returnValue;

	}

	public static String getTabId(Context context)
	{
		String tabId ="";
		ArrayList<Center> centerList = CentersOperations.getCenter(context);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				tabId = c.machineID;
				if (c.machineType.equals("C")) {
					return c.machineID;
				}
			}
		}
		return tabId;
	}
}
