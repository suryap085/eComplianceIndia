/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ApplicationIconOperation {

	public static void addIcon(int id, String name, boolean isUsed,
			boolean isDownloaded, Context context) {
		if (iconShouldAdd(id, context)) {
			ContentValues val = new ContentValues();
			val.put(Schema.MASTER_APPLICATION_ICON_ID, id);
			val.put(Schema.MASTER_APPLICATION_ICON_NAME, name);
			val.put(Schema.MASTER_APPLICATION_ICON_DOWNLOADED, isDownloaded);
			DbFactory dbFactory = new DbFactory(context)
					.CreateDatabase(TableEnum.MasterApplicationIcon);
			try {
				dbFactory.database.insert(Schema.TABLE_MASTER_APPLICATION_ICON,
						null, val);
			} catch (Exception e) {
			}
			dbFactory.CloseDatabase();
		}
	}

	private static boolean iconShouldAdd(int id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterApplicationIcon);
		try {
			if (dbFactory.database.query(Schema.TABLE_MASTER_APPLICATION_ICON,
					null, Schema.MASTER_APPLICATION_ICON_ID + "=" + id, null,
					null, null, null).getCount() > 0) {
				dbFactory.CloseDatabase();
				return false;
			}

		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return true;
	}

	public static ArrayList<String> getIconsToDownload(Context context) {
		ArrayList<String> icons = new ArrayList<String>();
		// DbFactory dbFactory = new DbFactory(context)
		// .CreateDatabase(TableEnum.MasterApplicationIcon);
		// try {
		// Cursor dbCursor = dbFactory.database.query(
		// Schema.TABLE_MASTER_APPLICATION_ICON, null,
		// Schema.MASTER_APPLICATION_ICON_DOWNLOADED + "=0", null,
		// null, null, null, null);
		// while (dbCursor.moveToNext()) {
		// icons.add(dbCursor.getString(dbCursor
		// .getColumnIndex(Schema.MASTER_APPLICATION_ICON_NAME)));
		//
		// }
		// } catch (Exception e) {
		//
		// }
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_REGIMEN_MASTER, null,
					Schema.REGIMEN_MASTER_IS_ACTIVE + "=1", null, null, null,
					null, null);
			while (dbCursor.moveToNext()) {
				String cat = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_CATEGORY));
				String stage = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_STAGE));
				String schedule = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_SCHEDULE));
				if (!icons.contains(cat + ".png"))
					icons.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REGIMEN_MASTER_CATEGORY))
							+ ".png");
				if (!icons.contains(stage + ".png"))
					icons.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REGIMEN_MASTER_STAGE))
							+ ".png");
				if (!icons.contains(schedule + ".png"))
					icons.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REGIMEN_MASTER_SCHEDULE))
							+ ".png");
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return icons;
	}

	public static void updateDownloaded(String name, boolean isDownloaded,
			Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.MASTER_APPLICATION_ICON_DOWNLOADED, isDownloaded);

		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MasterApplicationIcon);
		try {
			dbFactory.database.update(Schema.TABLE_MASTER_APPLICATION_ICON,
					val, Schema.MASTER_APPLICATION_ICON_NAME + "='" + name
							+ "'", null);

		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}
}
