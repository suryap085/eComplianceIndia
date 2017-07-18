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
import org.opasha.eCompliance.ecompliance.Model.ScanRModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.ScansViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ScansROperations {

	public static long addScan(String treatmentID, String visitorType,
			String scan, long creationTimeStamp, String createdBy,
			Context context) {
		ContentValues scanValues = new ContentValues();
		scanValues.put(Schema.SCANS_R_TREATMENT_ID, treatmentID);
		scanValues.put(Schema.SCANS_R_VISITOR_TYPE, visitorType);
		scanValues.put(Schema.SCANS_R_SCAN, scan);
		scanValues.put(Schema.SCANS_R_CREATION_TIMESTAMP, creationTimeStamp);
		scanValues.put(Schema.SCANS_R_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansR);
		try {
			return dbfactory.database.insert(Schema.TABLE_SCANS_R, null,
					scanValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<ScansViewModel> scans,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansR);

		dbfactory.database.beginTransaction();
		for (ScansViewModel values : scans) {
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANS_R_TREATMENT_ID, values.Treatment_Id);
			scanValues.put(Schema.SCANS_R_VISITOR_TYPE, "");
			scanValues.put(Schema.SCANS_R_SCAN, values.Scan1);
			scanValues.put(Schema.SCANS_R_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(values.Creation_TimeStamp));
			scanValues.put(Schema.SCANS_R_CREATED_BY, values.Created_By);
			dbfactory.database.insert(Schema.TABLE_SCANS_R, null, scanValues);
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
			ArrayList<org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.ScansViewModel> scans,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansR);

		dbfactory.database.beginTransaction();
		for (org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.ScansViewModel values : scans) {
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANS_R_TREATMENT_ID, values.Treatment_Id);
			scanValues.put(Schema.SCANS_R_VISITOR_TYPE, "");
			scanValues.put(Schema.SCANS_R_SCAN, values.Scan1);
			scanValues.put(Schema.SCANS_R_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(values.Creation_TimeStamp));
			scanValues.put(Schema.SCANS_R_CREATED_BY, values.Created_By);
			dbfactory.database.insert(Schema.TABLE_SCANS_R, null, scanValues);
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
				.CreateDatabase(TableEnum.ScansR);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS_R,
					Schema.SCANS_R_TREATMENT_ID + "='" + treatmentId + "'",
					null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansR);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS_R, null, null);
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

	public static ArrayList<ScanRModel> getScansR(Context context) {
		ArrayList<ScanRModel> scans = new ArrayList<ScanRModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansR);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS_R, null,
					null, null, null, null, null, null);
			while (dbCursor.moveToNext()) {
				ScanRModel sm = new ScanRModel();
				sm.scan = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_R_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_R_TREATMENT_ID));
				sm.CreatedBy = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_R_CREATED_BY));
				sm.visitorTyep = VisitorType.getVisitorType(dbCursor
						.getString(dbCursor
								.getColumnIndex(Schema.SCANS_R_VISITOR_TYPE)));
				sm.CreationTimeStap = dbCursor
						.getLong(dbCursor
								.getColumnIndex(Schema.SCANS_R_CREATION_TIMESTAMP));
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
				.CreateDatabase(TableEnum.ScansR);
		try {
			if (dbfactory.database.query(true, Schema.TABLE_SCANS_R, null,
					Schema.SCANS_R_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, null, null).getCount() > 0) {
				retValue = true;
			}
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return retValue;
	}
	
	public static int scanFpRCount(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansR);
		try {
			return dbfactory.database.query(true,
					Schema.TABLE_SCANS_R, null, null, null, null, null,
					null, null).getCount();
			
		} catch (Exception e) {
			
		} finally {
			dbfactory.CloseDatabase();
		}
		return 0;
	}

}
