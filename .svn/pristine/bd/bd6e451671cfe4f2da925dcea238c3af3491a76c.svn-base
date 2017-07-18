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
import org.opasha.eCompliance.ecompliance.Model.PatientLabFollowUpModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientLabFollowup;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.Patient_Lab_FollowUp;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class PatientLabFollowUpOperations {

	public static long addPatientLabFollowUpData(String ID, String labMonth,
			String labResult, long labDate, String labDmc, String labNo,
			String labWeight, String createdBy, String machineID,
			long creationTimeStamp, boolean isDeleted, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (shouldUpdate(ID, creationTimeStamp, labMonth, context)) {

			deletePatientHard(ID, labMonth, context);

		}

		ContentValues patientValues = new ContentValues();
		patientValues.put(Schema.PATIENT_LAB_TREATMENTID, ID);
		patientValues.put(Schema.PATIENT_LAB_MONTH, labMonth);
		patientValues.put(Schema.PATIENT_LAB_RESULT, labResult);
		patientValues.put(Schema.PATIENT_LAB_DATE, labDate);
		patientValues.put(Schema.PATIENT_LAB_DMC, labDmc);
		patientValues.put(Schema.PATIENT_LAB_NUMBER, labNo);
		patientValues.put(Schema.PATIENT_LAB_WEIGHT, labWeight);
		patientValues.put(Schema.PATIENT_LAB_TAB_ID, machineID);
		patientValues.put(Schema.PATIENT_LAB_CREATION_TIMESTAMP,
				creationTimeStamp);
		patientValues.put(Schema.PATIENT_LAB_CREATED_BY, createdBy);
		patientValues.put(Schema.PATIENT_LAB_ISDELETED, isDeleted);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);
		try {
			return dbfactory.database.insert(Schema.TABLE_LAB_DATA, null,
					patientValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<PatientLabFollowup> followUp,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);

		dbfactory.database.beginTransaction();
		for (PatientLabFollowup view : followUp) {
			ContentValues patientValues = new ContentValues();
			patientValues.put(Schema.PATIENT_LAB_TREATMENTID,
					view.Lab_TreatmentId);
			patientValues.put(Schema.PATIENT_LAB_MONTH, view.labMonth);
			patientValues.put(Schema.PATIENT_LAB_RESULT, view.labResult);
			patientValues.put(Schema.PATIENT_LAB_DATE, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.labDate)));
			patientValues.put(Schema.PATIENT_LAB_DMC, view.labDmc);
			patientValues.put(Schema.PATIENT_LAB_NUMBER, view.labNo);
			patientValues.put(Schema.PATIENT_LAB_WEIGHT, view.labWeight);
			patientValues.put(Schema.PATIENT_LAB_TAB_ID, view.labTabId);
			patientValues.put(Schema.PATIENT_LAB_CREATION_TIMESTAMP, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.labCreationTome_Stamp)));
			patientValues
					.put(Schema.PATIENT_LAB_CREATED_BY, view.labCreated_By);
			patientValues.put(Schema.PATIENT_LAB_ISDELETED, view.labIs_Deleted);

			dbfactory.database.insert(Schema.TABLE_LAB_DATA, null,
					patientValues);
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

	private static boolean shouldUpdate(String Id, long creationTimestamp,
			String labMonth, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);
		Cursor dbCursor;
		dbCursor = dbfactory.database.query(true, Schema.TABLE_LAB_DATA, null,
				Schema.PATIENT_LAB_TREATMENTID + "= '" + Id + "' and "
						+ Schema.PATIENT_LAB_MONTH + "='" + labMonth + "'",
				null, null, null, null, null);
		try {
			if (dbCursor.moveToFirst()) {
				if (dbCursor
						.getString(
								dbCursor.getColumnIndex(Schema.PATIENT_LAB_TREATMENTID))
						.equals(Id)
						&& dbCursor
								.getString(
										dbCursor.getColumnIndex(Schema.PATIENT_LAB_MONTH))
								.equals(labMonth)) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.e("labfollowuperror-----------", e.toString());
		}
		dbfactory.CloseDatabase();
		return false;
	}

	public static void deletePatientHard(String treatmentId, String labMonth,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);
		try {
			dbfactory.database.delete(Schema.TABLE_LAB_DATA,
					Schema.PATIENT_LAB_TREATMENTID + "='" + treatmentId
							+ "' and " + Schema.PATIENT_LAB_MONTH + "='"
							+ labMonth + "'", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbfactory.CloseDatabase();
	}

	// Public function to change isDeleted of a contact
	public static int deletePatientInitialLabDataSoft(String treatmentId,
			Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.PATIENT_LAB_ISDELETED, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);

		int num = -1;
		try {
			num = dbfactory.database.update(Schema.TABLE_LAB_DATA, isDeleted,
					Schema.PATIENT_LAB_TREATMENTID + "=?",
					new String[] { treatmentId });
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		return num;
	}

	public static ArrayList<PatientLabFollowUpModel> getPatientsLabfollowUp(
			String filterExpression, Context context) {
		ArrayList<PatientLabFollowUpModel> patientLabData = new ArrayList<PatientLabFollowUpModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_LAB_DATA, null, null, null, null, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_LAB_DATA, null, filterExpression, null,
						null, null, Schema.PATIENT_LAB_CREATION_TIMESTAMP, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values

			}
			if (dbCursor.moveToFirst()) {

				do {
					PatientLabFollowUpModel labfollowup = new PatientLabFollowUpModel();
					labfollowup.Lab_TreatmentId = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_TREATMENTID));
					labfollowup.Lab_Month = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_MONTH));
					labfollowup.Lab_Result = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_RESULT));
					labfollowup.Lab_Date = dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_DATE));
					labfollowup.Lab_Dmc = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_DMC));
					labfollowup.Lab_No = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_NUMBER));
					labfollowup.Lab_Weight = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_WEIGHT));
					labfollowup.Lab_CreationTome_Stamp = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.PATIENT_LAB_CREATION_TIMESTAMP));
					labfollowup.Lab_TabId = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_TAB_ID));
					labfollowup.Lab_Created_By = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_CREATED_BY));

					patientLabData.add(labfollowup);

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

			Log.e("error---------", e.toString());

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientLabData;
	}

	public static ArrayList<Patient_Lab_FollowUp> patientlabfollowUpSync(
			String filterExpression, Context context) {
		ArrayList<Patient_Lab_FollowUp> returnList = new ArrayList<Patient_Lab_FollowUp>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);
		Cursor dbCursor;
		try {
			if (filterExpression.trim().length() <= 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_LAB_DATA, null, null, null, null, null,
						null, null);
			} else {
				dbCursor = dbfactory.database
						.query(true, Schema.TABLE_LAB_DATA, null,
								filterExpression, null,
								Schema.PATIENT_LAB_CREATION_TIMESTAMP, null,
								null, null);
			}
			if (dbCursor.moveToFirst()) {
				do {
					Patient_Lab_FollowUp lab = new Patient_Lab_FollowUp();
					lab.Treatment_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_TREATMENTID));
					lab.Month = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_MONTH));
					lab.Result = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_RESULT));
					lab.Date = GenUtils.longToDate(dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_DATE)));
					lab.Dmc = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_DMC));
					lab.Weight = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_WEIGHT));
					lab.Lab_No = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_NUMBER));
					lab.CreationTime_Stamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.PATIENT_LAB_CREATION_TIMESTAMP)));
					lab.TabId = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_TAB_ID));
					lab.Created_By = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_LAB_CREATED_BY));

					lab.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_LAB_ISDELETED)));
					returnList.add(lab);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnList;
	}

	public static ArrayList<PatientLabFollowUpModel> getlastLabData(
			String filterexpression, Context context) {
		ArrayList<PatientLabFollowUpModel> patientLabData = new ArrayList<PatientLabFollowUpModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);

		Cursor dbCursor;

		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_LAB_DATA,
					null, filterexpression, null,
					Schema.PATIENT_LAB_CREATION_TIMESTAMP, null, null, null);

			// dbCursor = dbfactory.database.query(true, Schema.TABLE_LAB_DATA,
			// null, Schema.PATIENT_LAB_TREATMENTID + "='" + treatmentId
			// + "'", null, null, null,
			// Schema.PATIENT_LAB_CREATION_TIMESTAMP, null);

			if (dbCursor.moveToLast()) {
				PatientLabFollowUpModel labfollowup = new PatientLabFollowUpModel();
				labfollowup.Lab_TreatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_TREATMENTID));
				labfollowup.Lab_Month = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_MONTH));
				labfollowup.Lab_Result = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_RESULT));
				labfollowup.Lab_Date = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_DATE));
				labfollowup.Lab_Dmc = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_DMC));
				labfollowup.Lab_No = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_NUMBER));
				labfollowup.Lab_Weight = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_WEIGHT));
				labfollowup.Lab_CreationTome_Stamp = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_CREATION_TIMESTAMP));
				labfollowup.Lab_TabId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_TAB_ID));
				labfollowup.Lab_Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_CREATED_BY));

				patientLabData.add(labfollowup);

			}
		} catch (Exception e) {
			Log.e("error---------", e.toString());
		}

		dbfactory.CloseDatabase();

		return patientLabData;
	}

	
	public static ArrayList<PatientLabFollowUpModel> getlastPreTreatmentLab(String treatmentId, Context context)
	{
		ArrayList<PatientLabFollowUpModel> patientLabData = new ArrayList<PatientLabFollowUpModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.patientlabData);

		Cursor dbCursor;

		try {
			dbCursor = dbfactory.database.query(true, Schema.TABLE_LAB_DATA,
					null, Schema.PATIENT_LAB_TREATMENTID + "='" +treatmentId + "' and " + Schema.PATIENT_LAB_MONTH + "='Pretreatment'", null,
					Schema.PATIENT_LAB_CREATION_TIMESTAMP, null, null, null);

			// dbCursor = dbfactory.database.query(true, Schema.TABLE_LAB_DATA,
			// null, Schema.PATIENT_LAB_TREATMENTID + "='" + treatmentId
			// + "'", null, null, null,
			// Schema.PATIENT_LAB_CREATION_TIMESTAMP, null);

			if (dbCursor.moveToLast()) {
				PatientLabFollowUpModel labfollowup = new PatientLabFollowUpModel();
				labfollowup.Lab_TreatmentId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_TREATMENTID));
				labfollowup.Lab_Month = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_MONTH));
				labfollowup.Lab_Result = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_RESULT));
				labfollowup.Lab_Date = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_DATE));
				labfollowup.Lab_Dmc = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_DMC));
				labfollowup.Lab_No = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_NUMBER));
				labfollowup.Lab_Weight = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_WEIGHT));
				labfollowup.Lab_CreationTome_Stamp = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_CREATION_TIMESTAMP));
				labfollowup.Lab_TabId = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_TAB_ID));
				labfollowup.Lab_Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_LAB_CREATED_BY));

				patientLabData.add(labfollowup);

			}
		} catch (Exception e) {
			Log.e("error---------", e.toString());
		}

		dbfactory.CloseDatabase();

		return patientLabData;
	}

}
