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
import org.opasha.eCompliance.ecompliance.Model.IrisRModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.IrisViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.ScansIrisViewModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ScansIrisR_Operations {
	
	public static long addScan(String treatmentID, String eye,
			String scan, long creationTimeStamp, String createdBy,
			Context context) {
		ContentValues scanValues = new ContentValues();
		scanValues.put(Schema.SCANSIRIS_R_TREATMENT_ID, treatmentID);
		scanValues.put(Schema.SCANSIRIS_R_IRIS_EYE, eye);
		scanValues.put(Schema.SCANSIRIS_R_SCAN, scan);
		scanValues.put(Schema.SCANSIRIS_R_CREATION_TIMESTAMP, creationTimeStamp);
		scanValues.put(Schema.SCANSIRIS_R_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);
		try {
			return dbfactory.database.insert(Schema.TABLE_SCANSIRIS_R, null,
					scanValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<ScansIrisViewModel> scans,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);

		dbfactory.database.beginTransaction();
		for (ScansIrisViewModel values : scans) {
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANSIRIS_R_TREATMENT_ID, values.Treatment_Id);
			scanValues.put(Schema.SCANSIRIS_R_IRIS_EYE, values.IrisEye);
			scanValues.put(Schema.SCANSIRIS_R_SCAN, values.Scan);
			scanValues.put(Schema.SCANSIRIS_R_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(values.Creation_TimeStamp));
			scanValues.put(Schema.SCANSIRIS_R_CREATED_BY, values.Created_By);
			dbfactory.database.insert(Schema.TABLE_SCANSIRIS_R, null, scanValues);
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

	public static long bulkInsertVisitor(
			ArrayList<IrisViewModel> scans,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);

		dbfactory.database.beginTransaction();
		for (IrisViewModel values : scans) {
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANSIRIS_R_TREATMENT_ID, values.TreatmentId);
			scanValues.put(Schema.SCANSIRIS_R_IRIS_EYE, values.IrisEye);
			scanValues.put(Schema.SCANSIRIS_R_SCAN, values.IrisScan);
			scanValues.put(Schema.SCANSIRIS_R_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(values.CreationTimeStamp));
			scanValues.put(Schema.SCANSIRIS_R_CREATED_BY, values.CreatedBy);
			dbfactory.database.insert(Schema.TABLE_SCANSIRIS_R, null, scanValues);
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

	public static void deleteScans(Context context, String treatmentId) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANSIRIS_R,
					Schema.SCANSIRIS_R_TREATMENT_ID + "='" + treatmentId + "'",
					null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANSIRIS_R, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static String convertByteScanToString(byte[] scanData) {
		String s = "";
		for (byte b : scanData) {
			s = s + "," + Integer.toString(b);
		}
		return s.substring(1);
	}

	public static ArrayList<IrisRModel> getScansIrisR(Context context) {
		ArrayList<IrisRModel> scans = new ArrayList<IrisRModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANSIRIS_R, null,
					null, null, null, null, null, null);
			while (dbCursor.moveToNext()) {
				IrisRModel sm = new IrisRModel();
				sm.scan = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANSIRIS_R_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANSIRIS_R_TREATMENT_ID));
				sm.CreatedBy = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANSIRIS_R_CREATED_BY));
				sm.eye = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANSIRIS_R_IRIS_EYE));
				sm.CreationTimeStap = dbCursor
						.getLong(dbCursor
								.getColumnIndex(Schema.SCANSIRIS_R_CREATION_TIMESTAMP));
				scans.add(sm);
			}
			return scans;
		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}

	}
	
	public static boolean isTreatmentidIdExist(String treatmentId,
			Context context) {
		boolean retValue = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);
		try {
			if (dbfactory.database.query(true, Schema.TABLE_SCANSIRIS_R, null,
					Schema.SCANSIRIS_R_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, null, null).getCount() > 0) {
				retValue = true;
			}
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return retValue;
	}
	
	public static int scanIrisRCount(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIrisR);
		try {
			return dbfactory.database.query(true,
					Schema.TABLE_SCANSIRIS_R, null, null, null, null, null,
					null, null).getCount();
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return 0;
	}

}
