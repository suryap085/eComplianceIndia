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
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.Scan_Identification;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Scan_Identification_Operations {

	public static long addScanIdentification(String TabId, String notes,String treatmentId,
			boolean isIdentified, long creationTimeStamp, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		ContentValues identify = new ContentValues();
		identify.put(Schema.IDENTIFY_TAB_ID, TabId);
		identify.put(Schema.IDENTIFY_TREATMENT_ID, treatmentId);
		identify.put(Schema.IDENTIFY_IS_IDENTIFIED, isIdentified);
		identify.put(Schema.IDENTIFY_CREATION_TIMESTAMP, creationTimeStamp);
		identify.put(Schema.IDENTIFY_NOTES, notes);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScanIdentification);
		try {
			return dbfactory.database.insert(Schema.TABLE_IDENTIFICATION, null,
					identify);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static ArrayList<Scan_Identification> ScanIdentifySync(
			String filterExpression, Context context) {
		ArrayList<Scan_Identification> scanIdentify = new ArrayList<Scan_Identification>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScanIdentification);
		Cursor dbCursor;
		try {

			if (filterExpression.trim().length() <= 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_IDENTIFICATION, null, null, null, null,
						null, null, null);
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_IDENTIFICATION, null, filterExpression,
						null, Schema.IDENTIFY_CREATION_TIMESTAMP, null, null,
						null);
			}
			while (dbCursor.moveToNext()) {
				Scan_Identification scan = new Scan_Identification();
				scan.TabId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.IDENTIFY_TAB_ID));
				scan.TreatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.IDENTIFY_TREATMENT_ID));
				scan.Notes = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.IDENTIFY_NOTES));
				scan.CreationTimeStamp = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.IDENTIFY_CREATION_TIMESTAMP)));
				scan.IsIdentified = GenUtils
						.getBoolean(dbCursor.getInt(dbCursor
								.getColumnIndex(Schema.IDENTIFY_IS_IDENTIFIED)));
				scanIdentify.add(scan);
			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return scanIdentify;
	}

}
