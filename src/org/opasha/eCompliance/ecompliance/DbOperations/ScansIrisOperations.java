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
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.IrisViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.ScansIri;
import org.opasha.eCompliance.ecompliance.modal.wcf.ScansIrisViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.Enums.VisitorType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ScansIrisOperations {

	public static long addScan(String treatmentID, String eye, byte[] scan,
			long creationTimeStamp, String createdBy, Context context) {

		try {
			ScansIrisR_Operations.addScan(treatmentID, eye,
					ScansROperations.convertByteScanToString(scan),
					creationTimeStamp, createdBy, context);
		} catch (Exception e1) {
		}
		ContentValues scanValues = new ContentValues();
		scanValues.put(Schema.SCANS_IRIS_TREATMENT_ID, treatmentID);
		scanValues.put(Schema.SCANS_IRIS_EYE, eye);
		scanValues.put(Schema.SCANS_IRIS_SCAN, scan);
		scanValues.put(Schema.SCANS_IRIS_CREATION_TIMESTAMP, creationTimeStamp);
		scanValues.put(Schema.SCANS_IRIS_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			return dbfactory.database.insert(Schema.TABLE_SCANS_IRIS, null,
					scanValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	// Add scan after backsync and restore
	public static long addScanAfterRestore(String treatmentID, String eye,
			String scan, long creationTimeStamp, String createdBy,
			Context context) {

		String[] tempScan = scan.split(",");
		byte[] scanData = new byte[tempScan.length];
		for (int j = 0; j < tempScan.length; j++) {
			scanData[j] = Byte.valueOf(tempScan[j]);
		}
		ContentValues scanValues = new ContentValues();
		scanValues.put(Schema.SCANS_IRIS_TREATMENT_ID, treatmentID);
		scanValues.put(Schema.SCANS_IRIS_EYE, eye);
		scanValues.put(Schema.SCANS_IRIS_SCAN, scanData);
		scanValues.put(Schema.SCANS_IRIS_CREATION_TIMESTAMP, creationTimeStamp);
		scanValues.put(Schema.SCANS_IRIS_CREATED_BY, createdBy);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			return dbfactory.database.insert(Schema.TABLE_SCANS_IRIS, null,
					scanValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<ScansIrisViewModel> scans,
			Context context) {

		try {
			ScansIrisR_Operations.bulkInsert(scans, context);
		} catch (Exception e1) {
		}

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);

		dbfactory.database.beginTransaction();
		for (ScansIrisViewModel view : scans) {
			String[] tempScan = view.Scan.split(",");
			byte[] scanData = new byte[tempScan.length];
			for (int j = 0; j < tempScan.length; j++) {
				scanData[j] = Byte.valueOf(tempScan[j]);
			}
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANS_IRIS_TREATMENT_ID, view.Treatment_Id);
			scanValues.put(Schema.SCANS_IRIS_EYE, view.IrisEye);
			scanValues.put(Schema.SCANS_IRIS_SCAN, scanData);
			scanValues.put(Schema.SCANS_IRIS_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(view.Creation_TimeStamp));
			scanValues.put(Schema.SCANS_IRIS_CREATED_BY, view.Created_By);

			dbfactory.database
					.insert(Schema.TABLE_SCANS_IRIS, null, scanValues);
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

	public static long bulkInsertVisitor(ArrayList<IrisViewModel> scans,
			Context context) {

		try {
			ScansIrisR_Operations.bulkInsertVisitor(scans, context);
		} catch (Exception e1) {
		}

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);

		dbfactory.database.beginTransaction();
		for (IrisViewModel values : scans) {
			String[] tempScan = values.IrisScan.split(",");
			byte[] scanData = new byte[tempScan.length];
			for (int j = 0; j < tempScan.length; j++) {
				scanData[j] = Byte.valueOf(tempScan[j]);
			}
			ContentValues scanValues = new ContentValues();
			scanValues.put(Schema.SCANS_IRIS_TREATMENT_ID, values.TreatmentId);
			scanValues.put(Schema.SCANS_IRIS_EYE, values.IrisEye);
			scanValues.put(Schema.SCANS_IRIS_SCAN, scanData);
			scanValues.put(Schema.SCANS_IRIS_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(values.CreationTimeStamp));
			scanValues.put(Schema.SCANS_IRIS_CREATED_BY, values.CreatedBy);

			dbfactory.database
					.insert(Schema.TABLE_SCANS_IRIS, null, scanValues);
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
				.CreateDatabase(TableEnum.ScansIris);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS_IRIS,
					null, null, null, null, null, null, null); // if selection
																// is not
			// null then return
			// selection values
			while (dbCursor.moveToNext()) {
				ScanModel sm = new ScanModel();
				sm.scan = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_TREATMENT_ID));
				sm.visitorTyep = VisitorType.Patient;
				sm.eye = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_EYE));
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
		try {
			Cursor dbCursor = dbFactory.database.query(Schema.TABLE_PATIENTS,
					null, Schema.PATIENTS_TREATMENT_ID + " ='" + treatmentId
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
		return returnValue;

	}

	public static ArrayList<ScanModel> getScans(Context context,
			String treatmentId) {
		ArrayList<ScanModel> scans = new ArrayList<ScanModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_SCANS_IRIS, null,
					Schema.SCANS_IRIS_TREATMENT_ID + "='" + treatmentId + "'",
					null, null, null, null, null); // if
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
						.getColumnIndex(Schema.SCANS_IRIS_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_TREATMENT_ID));
				sm.visitorTyep = VisitorType.Patient; // TODO: Fix this if need
														// be
				scans.add(sm);
			}
			return scans;
		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
		}

	}

	public static ArrayList<ScanModel> getScansWithId(Context context,
			String treatmentId) {
		ArrayList<ScanModel> scans = new ArrayList<ScanModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_SCANS_IRIS, null,
					Schema.SCANS_IRIS_TREATMENT_ID + "='" + treatmentId + "'",
					null, null, null, null, null); // if

			while (dbCursor.moveToNext()) {
				ScanModel sm = new ScanModel();
				sm.scan = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_SCAN));
				sm.treatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_TREATMENT_ID));
				sm.visitorTyep = VisitorType.Patient;
				sm.Id = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_ROW_ID));
				scans.add(sm);
			}
			return scans;
		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
		}

	}

	public static void deleteScans(Context context, String treatmentId) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS_IRIS,
					Schema.SCANS_IRIS_TREATMENT_ID + "='" + treatmentId + "'",
					null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static byte[] getScanById(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_SCANS_IRIS, null,
					Schema.SCANS_IRIS_TREATMENT_ID + "='" + treatmentId + "'",
					null, null, null, null, null); // if
			// selection
			// is
			// not
			// null
			// then
			// return
			// selection values
			if (dbCursor.moveToFirst()) {
				return dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_SCAN));
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static ArrayList<ScansIri> ScanSync(String filterExpression,
			Context context) {
		ArrayList<ScansIri> scans = new ArrayList<ScansIri>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		Cursor dbCursor;
		try {

			if (filterExpression.trim().length() <= 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_SCANS_IRIS, null, null, null, null, null,
						null, null);
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_SCANS_IRIS, null, filterExpression, null,
						Schema.SCANS_IRIS_CREATION_TIMESTAMP, null, null, null);
			}
			while (dbCursor.moveToNext()) {
				ScansIri irisView = new ScansIri();
				irisView.Treatment_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_TREATMENT_ID));
				String s = "";
				byte[] scanData = dbCursor.getBlob(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_SCAN));
				for (byte b : scanData) {
					s = s + "," + Integer.toString(b);
				}
				irisView.Scan = s.substring(1);
				irisView.IrisEye = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_EYE));
				irisView.Creation_TimeStamp = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.SCANS_IRIS_CREATION_TIMESTAMP)));
				irisView.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_CREATED_BY));
				scans.add(irisView);
			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return scans;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS_IRIS, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static boolean isTreatmentidIdExist(String treatmentId,
			Context context) {
		boolean retValue = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			if (dbfactory.database.query(true, Schema.TABLE_SCANS_IRIS, null,
					Schema.SCANS_IRIS_TREATMENT_ID + "='" + treatmentId + "'",
					null, null, null, null, null).getCount() > 0) {
				retValue = true;
			}
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		return retValue;
	}

	public static void DelScanById(int Id, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			dbfactory.database.delete(Schema.TABLE_SCANS_IRIS,
					Schema.SCANS_IRIS_ROW_ID + "=" + Id, null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}

	public static long getLastScanDate(String treatmentId, Context context) {
		long dosedate = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);

		Cursor dbCursor;

		try {

			dbCursor = dbfactory.database.query(true, Schema.TABLE_SCANS_IRIS,
					null, Schema.SCANS_IRIS_TREATMENT_ID + "='" + treatmentId
							+ "'", null, null, null,
					Schema.SCANS_IRIS_CREATION_TIMESTAMP, null);

			if (dbCursor.moveToLast()) {

				dosedate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.SCANS_IRIS_CREATION_TIMESTAMP));

			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();

		return dosedate;
	}

	public static int scanIrisCount(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.ScansIris);
		try {
			return dbfactory.database.query(true,
					Schema.TABLE_SCANS_IRIS, null, null, null, null, null,
					null, null).getCount();

			
		} catch (Exception e) {
			
		} finally {
			dbfactory.CloseDatabase();
		}
		return 0;
	}
}
