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

import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.ScansViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ScansOperations {

	public static long addScan(String treatmentID, String visitorType,
			byte[] scan, long creationTimeStamp, String createdBy,
			Context context) {

		try {
			// Handle New Scan -- Add new scan or skip
			ArrayList<ScanModel> scans = ScansOperations.getScansWithId(
					context, treatmentID);
			try {
				if (scans.size() >= ((eComplianceApp) context).maxTemplatesToStore) {
					// Delete Scan number 3 for Treatment Id
					ScansOperations.DelScanById(scans.get(2).Id, context);
				}
			} catch (Exception e1) {
				if (scans.size() >= ((eComplianceApp) context
						.getApplicationContext()).maxTemplatesToStore) {
					// Delete Scan number 3 for Treatment Id
					ScansOperations.DelScanById(scans.get(2).Id, context);
				}
			}
		} catch (Exception e) {
		}

		try {
			ScansROperations.addScan(treatmentID, visitorType,
					ScansROperations.convertByteScanToString(scan),
					creationTimeStamp, createdBy, context);
		} catch (Exception e1) {
		}
		ContentValues scanValues = new ContentValues();
		scanValues.put(Schema.SCANS_TREATMENT_ID, treatmentID);
		scanValues.put(Schema.SCANS_VISITOR_TYPE, visitorType);
		scanValues.put(Schema.SCANS_SCAN, scan);
		scanValues.put(Schema.SCANS_CREATION_TIMESTAMP, creationTimeStamp);
		scanValues.put(Schema.SCANS_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			return dbfactory.database.insert(Schema.TABLE_SCANS, null,
					scanValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<ScansViewModel> scans,
			Context context) {

		try {
			ScansROperations.bulkInsert(scans, context);
		} catch (Exception e1) {
		}

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);

		dbfactory.database.beginTransaction();
		for (ScansViewModel view : scans) {
			String[] tempScan = view.Scan1.split(",");
			byte[] scanData = new byte[tempScan.length];
			for (int j = 0; j < tempScan.length; j++) {
				scanData[j] = Byte.valueOf(tempScan[j]);
			}
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANS_TREATMENT_ID, view.Treatment_Id);
			scanValues.put(Schema.SCANS_VISITOR_TYPE, "");
			scanValues.put(Schema.SCANS_SCAN, scanData);
			scanValues.put(Schema.SCANS_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(view.Creation_TimeStamp));
			scanValues.put(Schema.SCANS_CREATED_BY, view.Created_By);

			dbfactory.database.insert(Schema.TABLE_SCANS, null, scanValues);
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

		try {
			ScansROperations.bulkInsertVisitor(scans, context);
		} catch (Exception e1) {
		}

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);

		dbfactory.database.beginTransaction();
		for (org.opasha.eCompliance.ecompliance.modal.wcf.Visitor.ScansViewModel values : scans) {
			String[] tempScan = values.Scan1.split(",");
			byte[] scanData = new byte[tempScan.length];
			for (int j = 0; j < tempScan.length; j++) {
				scanData[j] = Byte.valueOf(tempScan[j]);
			}
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANS_TREATMENT_ID, values.Treatment_Id);
			scanValues.put(Schema.SCANS_VISITOR_TYPE, "");
			scanValues.put(Schema.SCANS_SCAN, scanData);
			scanValues.put(Schema.SCANS_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(values.Creation_TimeStamp));
			scanValues.put(Schema.SCANS_CREATED_BY, values.Created_By);

			dbfactory.database.insert(Schema.TABLE_SCANS, null, scanValues);
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

	public static ArrayList<ScanModel> getScans(boolean isCalledByEnroll,
			Context context) {
		ArrayList<String> _tempPatients = PatientsOperations
				.getPatientsForScans(context);
		ArrayList<String> _tempVisitors = VisitorsOperations
				.getVisitorsForScans(context);

		ArrayList<ScanModel> scans = new ArrayList<ScanModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS, null,
					null, null, null, null, null, null); // if selection is not
			// null then return
			// selection values
			while (dbCursor.moveToNext()) {
				ScanModel sm = new ScanModel();
				sm.scan = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_TREATMENT_ID));
				sm.visitorTyep = VisitorType.getVisitorType(dbCursor
						.getString(dbCursor
								.getColumnIndex(Schema.SCANS_VISITOR_TYPE)));
				if (_tempPatients.contains(sm.treatmentId)) {
					scans.add(sm);
				} else if (_tempVisitors.contains(sm.treatmentId)) {
					scans.add(sm);
				} else if (isCalledByEnroll) {
					if (!PatientsOperations.patientExists(sm.treatmentId,
							context)) {
						scans.add(sm);
					} else if (!VisitorsOperations.visitorExists(
							sm.treatmentId, context)) {
						scans.add(sm);
					}
				}

			}
			return scans;
		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}

	}

	// return only active,default and transfer internally patient
	public static boolean isPatientActiveDefaultTransferInternally(
			Context context, String treatmentId) {
		boolean returnValue = false;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor = null;
		try {
			dbCursor = dbFactory.database.query(Schema.TABLE_PATIENTS, null,
					Schema.PATIENTS_TREATMENT_ID + " ='" + treatmentId
							+ "' and " + Schema.PATIENTS_IS_DELETED + " =0",
					null, null, null, null);
			dbCursor.moveToFirst();
			if (dbCursor.getString(
					dbCursor.getColumnIndex(Schema.PATIENTS_STATUS)).equals(
					Enums.StatusType.getStatusType(StatusType.Active))
					|| dbCursor.getString(
							dbCursor.getColumnIndex(Schema.PATIENTS_STATUS))
							.equals(Enums.StatusType
									.getStatusType(StatusType.Default))
					|| dbCursor
							.getString(
									dbCursor.getColumnIndex(Schema.PATIENTS_STATUS))
							.equals(Enums.StatusType
									.getStatusType(StatusType.TransferredInternally))) {
				returnValue = true;
			}

		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		dbCursor.close();
		return returnValue;

	}

	public static boolean isVisitorActive(Context context, String treatmentId) {
		boolean returnValue = false;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Visitors);
		Cursor dbCursor = null;
		try {
			dbCursor = dbFactory.database.query(Schema.TABLE_VISITORS, null,
					Schema.VISITORS_ID + " ='" + treatmentId + "' and "
							+ Schema.VISITORS_IS_DELETED + " =0", null, null,
					null, null);
			dbCursor.moveToFirst();
			if (dbCursor.getString(
					dbCursor.getColumnIndex(Schema.VISITORS_STATUS)).equals(
					Enums.StatusType.getStatusType(StatusType.Active))) {
				returnValue = true;
			}

		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		dbCursor.close();
		return returnValue;

	}

	public static ArrayList<ScanModel> getScans(Context context,
			String treatmentId) {
		ArrayList<ScanModel> scans = new ArrayList<ScanModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS, null,
					Schema.SCANS_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, null, null); // if
			// selection
			// is
			// not
			// null
			// then
			// return
			// selection values
			while (dbCursor.moveToNext()) {
				ScanModel sm = new ScanModel();
				sm.scan = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_TREATMENT_ID));
				sm.visitorTyep = VisitorType.getVisitorType(dbCursor
						.getString(dbCursor
								.getColumnIndex(Schema.SCANS_VISITOR_TYPE)));
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

	public static ArrayList<ScanModel> getScansWithId(Context context,
			String treatmentId) {
		ArrayList<ScanModel> scans = new ArrayList<ScanModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS, null,
					Schema.SCANS_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, null, null); // if

			while (dbCursor.moveToNext()) {
				ScanModel sm = new ScanModel();
				sm.scan = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_TREATMENT_ID));
				sm.visitorTyep = VisitorType.getVisitorType(dbCursor
						.getString(dbCursor
								.getColumnIndex(Schema.SCANS_VISITOR_TYPE)));
				sm.Id = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.SCANS_ROW_ID));
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

	public static void deleteScans(Context context, String treatmentId) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			ScansROperations.deleteScans(context, treatmentId);
		} catch (Exception e1) {
		}
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS,
					Schema.SCANS_TREATMENT_ID + "='" + treatmentId + "'", null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static byte[] getScanById(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS, null,
					Schema.SCANS_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, null, null); // if
			// selection
			// is
			// not
			// null
			// then
			// return
			// selection values
			if (dbCursor.moveToFirst()) {
				return dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_SCAN));
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
	}

	public static ArrayList<ScansViewModel> ScanSync(String filterExpression,
			Context context) {
		ArrayList<ScansViewModel> scans = new ArrayList<ScansViewModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		Cursor dbCursor = null;
		try {

			if (filterExpression.trim().length() <= 0) {
				dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS,
						null, null, null, null, null, null, null);
			} else {
				dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS,
						null, filterExpression, null,
						Schema.SCANS_CREATION_TIMESTAMP, null, null, null);
			}
			while (dbCursor.moveToNext()) {
				ScansViewModel sm = new ScansViewModel();
				sm.Treatment_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_TREATMENT_ID));
				String s = "";
				byte[] scanData = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_SCAN));
				for (byte b : scanData) {
					s = s + "," + Integer.toString(b);
				}
				sm.Scan1 = s.substring(1);
				sm.Creation_TimeStamp = GenUtils
						.getTicksFromTime(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.SCANS_CREATION_TIMESTAMP)));
				sm.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_CREATED_BY));
				scans.add(sm);
			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return scans;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static boolean isTreatmentidIdExist(String treatmentId,
			Context context) {
		boolean retValue = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			if (dbfactory.database.query(true, Schema.TABLE_SCANS, null,
					Schema.SCANS_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, null, null).getCount() > 0) {
				retValue = true;
			}
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return retValue;
	}

	public static void DelScanById(int Id, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS, Schema.SCANS_ROW_ID
					+ "=" + Id, null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static long getLastScanDate(String treatmentId, Context context) {
		long dosedate = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);

		Cursor dbCursor = null;

		try {

			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS, null,
					Schema.SCANS_TREATMENT_ID + "='" + treatmentId + "'", null,
					null, null, Schema.SCANS_CREATION_TIMESTAMP, null);

			if (dbCursor.moveToLast()) {

				dosedate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.SCANS_CREATION_TIMESTAMP));

			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();
		dbCursor.close();

		return dosedate;
	}

	// Add scan after backsync and restore
	public static long addScanaAfterRestore(String treatmentID,
			String visitorType, String scan, long creationTimeStamp,
			String createdBy, Context context) {

		String[] tempScan = scan.split(",");
		byte[] scanData = new byte[tempScan.length];
		for (int j = 0; j < tempScan.length; j++) {
			scanData[j] = Byte.valueOf(tempScan[j]);
		}
		ContentValues scanValues = new ContentValues();
		scanValues.put(Schema.SCANS_TREATMENT_ID, treatmentID);
		scanValues.put(Schema.SCANS_VISITOR_TYPE, visitorType);
		scanValues.put(Schema.SCANS_SCAN, scanData);
		scanValues.put(Schema.SCANS_CREATION_TIMESTAMP, creationTimeStamp);
		scanValues.put(Schema.SCANS_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			return dbfactory.database.insert(Schema.TABLE_SCANS, null,
					scanValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}
	public static int scanFpCount(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Scans);
		try {
			return dbfactory.database.query(true,
					Schema.TABLE_SCANS, null, null, null, null, null,
					null, null).getCount();
		} catch (Exception e) {
			
		} finally {
			dbfactory.CloseDatabase();
		}
		return 0;
	}
	

}
