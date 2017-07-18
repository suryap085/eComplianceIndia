/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.io.File;
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.util.DbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.SparseArray;

public class MasterIconOperation {
	public static void addIcon(int id, String name, boolean isUsed,
			boolean isDownloaded, Context context) {
		if (iconShouldAdd(id, context)) {
			ContentValues val = new ContentValues();
			val.put(Schema.MASTER_ICON_ID, id);
			val.put(Schema.MASTER_ICON_NAME, name);
			val.put(Schema.MASTER_ICON_USED, isUsed);
			val.put(Schema.MASTER_ICON_DOWNLOADED, isDownloaded);
			DbFactory dbFactory = new DbFactory(context)
					.CreateDatabase(TableEnum.MasterIcon);
			try {
				dbFactory.database.insert(Schema.TABLE_MASTER_ICON, null, val);
			} catch (Exception e) {
			}
			dbFactory.CloseDatabase();
		}
	}

	public static void hardDeleteIcon(String name, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			dbFactory.database.delete(Schema.TABLE_MASTER_ICON,
					Schema.MASTER_ICON_NAME + "='" + name + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	private static boolean iconShouldAdd(int id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			if (dbFactory.database.query(Schema.TABLE_MASTER_ICON, null,
					Schema.MASTER_ICON_ID + "=" + id, null, null, null, null)
					.getCount() > 0) {
				dbFactory.CloseDatabase();
				return false;
			}

		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return true;
	}

	public static void updateDownloaded(String name, boolean isDownloaded,
			Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.MASTER_ICON_DOWNLOADED, isDownloaded);

		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			dbFactory.database.update(Schema.TABLE_MASTER_ICON, val,
					Schema.MASTER_ICON_NAME + "='" + name + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static void updateIsUsed(String name, boolean isUsed, Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.MASTER_ICON_USED, isUsed);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			dbFactory.database.update(Schema.TABLE_MASTER_ICON, val,
					Schema.MASTER_ICON_NAME + "='" + name + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static ArrayList<String> getNineRendomIcons(Context context) {
		ArrayList<String> retArray = new ArrayList<String>();
		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File directory = new File(sd, "//eComplianceClient//"
				+ DbUtils.getTabId(context) + "//resources//");
		String path = sd + "//eComplianceClient//" + DbUtils.getTabId(context)
				+ "//resources//";
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			int maxValue = 0;
			int minValue = 0;
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_MASTER_ICON, null, Schema.MASTER_ICON_USED
							+ " = 0 ", null, null, null, Schema.MASTER_ICON_ID,
					null);
			if (dbCursor.moveToFirst()) {
				if (dbCursor.getCount() > 9) {
					SparseArray<String> values = new SparseArray<String>();
					minValue = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.MASTER_ICON_ID));
					do {
						values.put(
								dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.MASTER_ICON_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.MASTER_ICON_NAME)));
					} while (dbCursor.moveToNext());
					dbCursor.moveToLast();
					maxValue = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.MASTER_ICON_ID));
					int i = 0;
					do {
						int number = minValue
								+ (int) (Math.random() * ((maxValue - minValue) + 1));
						if (values.indexOfKey(number) >= 0) {
							if (!retArray.contains(path
									+ values.get(number).toString())) {
								File file = new File(directory, values.get(
										number).toString());
								if (file.exists()) {
									retArray.add(path + values.get(number));
									i++;
								}
							}
						}
					} while (i < 9);
				} else {
					dbCursor.moveToFirst();
					do {
						String value = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.MASTER_ICON_NAME));
						File file = new File(directory, value);
						if (file.exists()) {
							retArray.add(path
									+ dbCursor.getString(dbCursor
											.getColumnIndex(Schema.MASTER_ICON_NAME)));
						}
					} while (dbCursor.moveToNext());
				}
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retArray;
	}

	/**
	 * @param query
	 *            -pass null if need all columns.
	 * @param context
	 * @return
	 */
	public static ArrayList<String> getIcons(String query, Context context) {
		ArrayList<String> retArray = new ArrayList<String>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {

			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_MASTER_ICON, null, query, null, null, null,
					Schema.MASTER_ICON_ID);

			if (dbCursor.moveToFirst()) {
				do {
					retArray.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.MASTER_ICON_NAME)));
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retArray;
	}

	public static void deleteUnusedIcons( Context context) {
		ArrayList<String> retArray = getIcons(
				Schema.MASTER_ICON_USED + " = 1 ", context);
		for (String ab : retArray) {
			if (PatientIconOperation.getId(ab, context).equals("")) {
				hardDeleteIcon(ab, context);
			}
		}
	}

	public static String getAllIcons(Context context) {
		StringBuilder retArray = new StringBuilder();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_MASTER_ICON, null, null, null, null, null,
					Schema.MASTER_ICON_ID);
			if (dbCursor.moveToFirst()) {
				do {
					retArray.append(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.MASTER_ICON_ID)));
					retArray.append(",");
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		if (retArray.length() > 1) {
			retArray.setLength(retArray.length() - 1);
		}
		return retArray.toString();
	}

	public static int getFreeIconsCount(Context context) {
		int retval = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterIcon);
		try {
			retval = (dbFactory.database.query(Schema.TABLE_MASTER_ICON, null,
					Schema.MASTER_ICON_USED + "=0", null, null, null,
					Schema.MASTER_ICON_ID)).getCount();
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retval;
	}

}
