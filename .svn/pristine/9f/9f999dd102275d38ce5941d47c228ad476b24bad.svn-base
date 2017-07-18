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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class VideosCategoryOperations {

	public static long addVideos(String videoFile, String category,
			String stage, String isDaily, int priority, Context context) {
		ContentValues newVideo = new ContentValues();

		newVideo.put(Schema.VIDEOS_CAT_FILENAME, videoFile);
		newVideo.put(Schema.VIDEOS_CAT_CATEGORY, category);
		newVideo.put(Schema.VIDEOS_CAT_TREATMENT_STAGE, stage);
		newVideo.put(Schema.VIDEOS_CAT_TREATMENT_DAILY, isDaily);
		newVideo.put(Schema.VIDEOS_CAT_PRIOIRITY, priority);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VideosCategory);

		try {
			return dbfactory.database.insert(Schema.TABLE_VIDEOS_CATEGORY,
					null, newVideo);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VideosCategory);
		try {
			dbfactory.database.delete(Schema.TABLE_VIDEOS_CATEGORY, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static ArrayList<String> GetVideoList(String category, String stage,
			String isDaily, Context context) {
		ArrayList<String> videos = new ArrayList<String>();

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.VideosCategory);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_VIDEOS_CATEGORY, null,
					Schema.VIDEOS_CAT_CATEGORY + "='" + category + "' and "
							+ Schema.VIDEOS_CAT_TREATMENT_STAGE + "='" + stage
							+ "' and " + Schema.VIDEOS_CAT_TREATMENT_DAILY
							+ "='" + isDaily + "'", null, null, null,
					Schema.VIDEOS_CAT_PRIOIRITY, null);
			while (dbCursor.moveToNext()) {
				videos.add(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.VIDEOS_CAT_FILENAME)));
			}
			dbCursor.close();
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return videos;
	}
}
