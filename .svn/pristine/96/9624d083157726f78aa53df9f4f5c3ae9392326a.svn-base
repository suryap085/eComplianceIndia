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

public class VideosOperations {

	public static long addVideos(String videoFile, String description,
			Context context) {
		ContentValues newVideo = new ContentValues();

		newVideo.put(Schema.VIDEOS_FILENAME, videoFile);
		newVideo.put(Schema.VIDEOS_DESCRIPTION, description);
		deleteVideo(videoFile, context);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Videos);

		try {
			return dbfactory.database.insert(Schema.TABLE_VIDEOS, null,
					newVideo);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	// Public function to change isDeleted of a contact
	public static void deleteVideo(String videoFile, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Videos);

		try {
			dbfactory.database.delete(Schema.TABLE_VIDEOS,
					Schema.VIDEOS_FILENAME + " = '" + videoFile + "'", null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Videos);
		try {
			dbfactory.database.delete(Schema.TABLE_VIDEOS, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static ArrayList<String> GetVideoList(Context context) {
		ArrayList<String> videos = new ArrayList<String>();

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Videos);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_VIDEOS,
					null, null, null, null, null, null, null); // if selection
			// is not
			while (dbCursor.moveToNext()) {
				videos.add(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.VIDEOS_FILENAME)));
			}
			dbCursor.close();
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return videos;
	}
}
