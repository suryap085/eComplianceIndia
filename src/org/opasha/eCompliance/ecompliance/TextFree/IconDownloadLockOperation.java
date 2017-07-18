/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.TextFree;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class IconDownloadLockOperation {
	public static void add(String name, long enqueueId, Context context) {
		deleteByname(name, context);
		ContentValues val = new ContentValues();
		val.put(Schema.ICON_DOWNLOAD_LOCK_NAME, name);
		val.put(Schema.ICON_DOWNLOAD_LOCK_ENQUEUE_ID, enqueueId);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.iconDownloadLock);
		try {
			dbFactory.database.insert(Schema.TABLE_ICON_DOWNLOAD_LOCK, null,
					val);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static void deleteByname(String name, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.iconDownloadLock);
		try {
			dbFactory.database.delete(Schema.TABLE_ICON_DOWNLOAD_LOCK,
					Schema.ICON_DOWNLOAD_LOCK_NAME + "= '" + name + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static void deleteByEnqueueId(long id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.iconDownloadLock);
		try {
			dbFactory.database.delete(Schema.TABLE_ICON_DOWNLOAD_LOCK,
					Schema.ICON_DOWNLOAD_LOCK_ENQUEUE_ID + "= " + id, null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static long getEnqueueID(String name, Context context) {
		long id = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.iconDownloadLock);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_ICON_DOWNLOAD_LOCK, null,
					Schema.ICON_DOWNLOAD_LOCK_NAME + "= '" + name + "'", null,
					null, null, null, null);
			if (dbCursor.moveToFirst()) {
				id = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.ICON_DOWNLOAD_LOCK_ENQUEUE_ID));
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return id;
	}
}
