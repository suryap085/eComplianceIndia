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
import org.opasha.eCompliance.ecompliance.modal.wcf.eCounseling_videos;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ECounselingOperations {

	public static long add(String treatment_id, String stage,String treatmentDaily,String user_id, int video_id,
			long start_time, long end_time, long sync_time, Context context) {

		ContentValues eCounselingValues = new ContentValues();
		eCounselingValues.put(Schema.ECOUNSELING_TREATMENT_ID, treatment_id);
		eCounselingValues.put(Schema.ECOUNSELING_USER_ID, user_id);
		eCounselingValues.put(Schema.ECOUNSELING_START_TIME, start_time);
		eCounselingValues.put(Schema.ECOUNSELING_END_TIME, end_time);
		eCounselingValues.put(Schema.ECOUNSELING_VIDEO_ID, video_id);
		eCounselingValues.put(Schema.ECOUNSELING_SYNC_TIMESTAMP, sync_time);
		eCounselingValues.put(Schema.ECOUNSELING_STAGE, stage);
		eCounselingValues.put(Schema.ECOUNSELING_TREATMENTDAILY, treatmentDaily);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ECounseling);
		try {
			return dbfactory.database.insert(Schema.TABLE_ECOUNSELING, null,
					eCounselingValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static ArrayList<eCounseling_videos> eCounselingSync(
			String filterExpression, Context context) {
		ArrayList<eCounseling_videos> ecounseling = new ArrayList<eCounseling_videos>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ECounseling);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbFactory.database.query(false,
						Schema.TABLE_ECOUNSELING, null, null, null, null, null,
						Schema.ECOUNSELING_SYNC_TIMESTAMP, null);
			} else {
				dbCursor = dbFactory.database.query(false,
						Schema.TABLE_ECOUNSELING, null, filterExpression, null,
						null, null, Schema.ECOUNSELING_SYNC_TIMESTAMP, null);
				if (dbCursor.moveToFirst()) {
					do {
						eCounseling_videos dosePrior = new eCounseling_videos();
						dosePrior.Treatment_Id = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.ECOUNSELING_TREATMENT_ID));
						dosePrior.Provider_Id = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.ECOUNSELING_USER_ID));
						dosePrior.Stage = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.ECOUNSELING_STAGE));
						dosePrior.TreatmentDaily = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.ECOUNSELING_TREATMENTDAILY));
						dosePrior.Video_Id = String
								.valueOf(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.ECOUNSELING_VIDEO_ID)));
						dosePrior.Start_Time = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.ECOUNSELING_START_TIME)));
						dosePrior.End_Time = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.ECOUNSELING_END_TIME)));
						dosePrior.Creation_TimeStamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.ECOUNSELING_SYNC_TIMESTAMP)));
						ecounseling.add(dosePrior);
					} while (dbCursor.moveToNext());
				}
			}
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
		return ecounseling;
	}
	
	 public static int maxVideoId(String TreatmentId, String stage,String isDaily,Context context) {
	        DbFactory dbfactory = new DbFactory(context)
	                .CreateDatabase(DbFactory.TableEnum.ECounseling);
	       Cursor dbCursor = null;
	        try {
	            dbCursor = dbfactory.database
	                    .rawQuery(
	                            " select max(" + Schema.ECOUNSELING_VIDEO_ID + ") as " + Schema.ECOUNSELING_VIDEO_ID + " from "
	                                    + Schema.TABLE_ECOUNSELING
	                                    + " where "
	                                    + Schema.ECOUNSELING_TREATMENT_ID + "='" + TreatmentId + "' and "
	        							+ Schema.ECOUNSELING_STAGE + "='" + stage
	        							+ "' and " + Schema.ECOUNSELING_TREATMENTDAILY
	        							+ "='" + isDaily + "'"
	                                    , null);

	            dbCursor.moveToFirst();
	        } catch (Exception e) {
	            Log.e("error", e.toString());
	        }
	        dbfactory.CloseDatabase();
	        return dbCursor.getInt(dbCursor
	                .getColumnIndex(Schema.ECOUNSELING_VIDEO_ID));
	    }
	 
	 public static void Delete(String treatmentId,String stage,String isDaily, Context context) {
			DbFactory dbfactory = new DbFactory(context)
					.CreateDatabase(TableEnum.ECounseling);
			try {
				dbfactory.database.delete(Schema.TABLE_ECOUNSELING,
						Schema.ECOUNSELING_TREATMENT_ID + "='" + treatmentId + "' and "
    							+ Schema.ECOUNSELING_STAGE + "='" + stage
    							+ "' and " + Schema.ECOUNSELING_TREATMENTDAILY
    							+ "='" + isDaily + "'", null);
			} catch (Exception e) {
			}

			dbfactory.CloseDatabase();
		}

}
