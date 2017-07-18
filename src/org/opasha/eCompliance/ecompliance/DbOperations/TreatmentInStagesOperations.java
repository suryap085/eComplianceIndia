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
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.modal.wcf.TreatmentInStage;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.TreatmentInStagesViewModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TreatmentInStagesOperations {

	public static long addTreatmentStage(String ID, int regimenId,
			long startDate, long creationTimeStamp, String createdBy,
			boolean isDeleted, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(ID, creationTimeStamp, context)) {
			return 1;
		}
		ContentValues patientValues = new ContentValues();
		patientValues.put(Schema.TREATMENT_IN_STAGES_TREATMENT_ID, ID);
		patientValues.put(Schema.TREATMENT_IN_STAGES_REGIMEN_ID, regimenId);
		patientValues.put(Schema.TREATMENT_IN_STAGES_START_DATE, startDate);
		patientValues.put(Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP,
				creationTimeStamp);
		patientValues.put(Schema.TREATMENT_IN_STAGES_CREATED_BY, createdBy);
		patientValues.put(Schema.TREATMENT_IN_STAGES_IS_DELETED, isDeleted);

		deletePatientSoft(ID, context);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		try {
			return dbfactory.database.insert(Schema.TABLE_TREATMENT_IN_STAGES,
					null, patientValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<TreatmentInStagesViewModel> tis,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);

		dbfactory.database.beginTransaction();
		for (TreatmentInStagesViewModel view : tis) {
			ContentValues patientValues = new ContentValues();
			patientValues.put(Schema.TREATMENT_IN_STAGES_TREATMENT_ID,
					view.Treatment_Id);
			patientValues.put(Schema.TREATMENT_IN_STAGES_REGIMEN_ID,
					view.Regimen_Id);
			patientValues.put(Schema.TREATMENT_IN_STAGES_START_DATE,
					GenUtils.getTimeFromTicks(view.Start_Date));
			patientValues.put(Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(view.Creation_TimeStamp));
			patientValues.put(Schema.TREATMENT_IN_STAGES_CREATED_BY,
					view.Created_By);
			patientValues.put(Schema.TREATMENT_IN_STAGES_IS_DELETED,
					view.Is_Deleted);

			dbfactory.database.insert(Schema.TABLE_TREATMENT_IN_STAGES, null,
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

	private static boolean shouldUpdate(String id, long creationTimestamp,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		try {
			if (dbfactory.database
					.query(Schema.TABLE_TREATMENT_IN_STAGES,
							null,
							Schema.TREATMENT_IN_STAGES_TREATMENT_ID
									+ " = '"
									+ id
									+ "' and "
									+ Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP
									+ " > " + creationTimestamp, null, null,
							null, null).getCount() > 1) {
				dbfactory.CloseDatabase();
				return false;
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return true;
	}

	public static void deletePatientSoft(String treatmentId, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.TREATMENT_IN_STAGES_IS_DELETED, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);

		dbfactory.database.update(Schema.TABLE_TREATMENT_IN_STAGES, isDeleted,
				Schema.TREATMENT_IN_STAGES_TREATMENT_ID + "=?",
				new String[] { treatmentId });

		dbfactory.CloseDatabase();

	}

	public static void deletePatientHard(String treatmentId, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);

		dbfactory.database.delete(Schema.TABLE_TREATMENT_IN_STAGES,
				Schema.TREATMENT_IN_STAGES_TREATMENT_ID + "='" + treatmentId
						+ "'", null);

		dbfactory.CloseDatabase();
	}

	public static ArrayList<Patient> getTreatment(String filterExpression,
			Context context) {
		ArrayList<Patient> treatmentList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_TREATMENT_IN_STAGES, null, null, null,
						null, null, null, null); // if selection is null return
													// all
													// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_TREATMENT_IN_STAGES, null,
						filterExpression, null, null, null, null, null); // if
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
					Patient regimen = new Patient();
					regimen.setPatient(
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_TREATMENT_ID)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_REGIMEN_ID)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_START_DATE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_CREATED_BY)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_IS_DELETED))));
					treatmentList.add(regimen);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return treatmentList;
	}

	public static ArrayList<String> getPendingList(String query, Context context) {
		ArrayList<String> returnlist = new ArrayList<String>();

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		Cursor dbCursor;
		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_TREATMENT_IN_STAGES, null,
					Schema.TREATMENT_IN_STAGES_IS_DELETED + "=0 and "
							+ Schema.TREATMENT_IN_STAGES_REGIMEN_ID
							+ "<>0 and (" + query + ")", null, null, null,
					null, null);

			if (dbCursor.moveToFirst()) {
				do {

					returnlist
							.add(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_TREATMENT_ID)));

				} while (dbCursor.moveToNext());
			}

		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

		return returnlist;
	}

	public static int getPatientRegimenId(String treatmentId, Context context) {
		int id = -1;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);

		Cursor dbCursor;

		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_TREATMENT_IN_STAGES, null,
					Schema.TREATMENT_IN_STAGES_TREATMENT_ID + "='"
							+ treatmentId + "'", null, null, null,
					Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				do {
					id = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_REGIMEN_ID));
					if (id != 0) {
						dbfactory.CloseDatabase();
						return id;
					}
				} while (dbCursor.moveToPrevious());

			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();

		return id;
	}

	public static int getPatientRegimenIdByDate(String treatmentId,
			long regimenDate, Context context) {
		int id = -1;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);

		Cursor dbCursor;

		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_TREATMENT_IN_STAGES, null,
					Schema.TREATMENT_IN_STAGES_TREATMENT_ID + "='"
							+ treatmentId + "'", null, null, null,
					Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				do {
					id = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_REGIMEN_ID));

					long regStartDate = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_START_DATE));
					if (regimenDate > regStartDate) {
						dbfactory.CloseDatabase();
						return id;
					}
				} while (dbCursor.moveToPrevious());
			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();

		return id;
	}

	public static Master getPatientRegimen(String treatmentId, Context context) {
		int id = -1;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);

		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_TREATMENT_IN_STAGES, null,

					Schema.TREATMENT_IN_STAGES_TREATMENT_ID + "='"
							+ treatmentId + "'", null, null, null,
					Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				do {
					id = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_REGIMEN_ID));
					if (id != 0) {
						dbfactory.CloseDatabase();
						return RegimenMasterOperations.getRegimen(id, context);
					}
				} while (dbCursor.moveToPrevious());

			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();

		return null;
	}

	public static String getTreatmentIdByRegimenIds(String regimenIds,
			Context context) {
		String[] ids = regimenIds.split(",");
		StringBuilder retVal = new StringBuilder();

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		retVal.append(Schema.TREATMENT_IN_STAGES_IS_DELETED + " =0 and (");
		for (String s : ids) {
			retVal.append(Schema.TREATMENT_IN_STAGES_REGIMEN_ID + " = " + s
					+ " or ");
		}
		retVal.setLength(retVal.length() - 3);
		retVal.append(")");
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_TREATMENT_IN_STAGES, null, retVal.toString(),
					null, null, null, null);
			retVal = new StringBuilder();
			while (dbCursor.moveToNext()) {
				retVal.append("'"
						+ dbCursor.getString(dbCursor
								.getColumnIndex(Schema.TREATMENT_IN_STAGES_TREATMENT_ID))
						+ "' ,");

			}
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		if (retVal.length() > 1) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();

	}

	public static ArrayList<TreatmentInStage> TreatmentInStageSync(
			String filterExpression, Context context) {
		ArrayList<TreatmentInStage> treatmentList = new ArrayList<TreatmentInStage>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_TREATMENT_IN_STAGES, null, null, null,
						Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_TREATMENT_IN_STAGES, null,
						filterExpression, null,
						Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP, null,
						null, null); // if
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
					TreatmentInStage TreatmentInStage = new TreatmentInStage();
					TreatmentInStage.Treatment_Id = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_TREATMENT_ID));
					TreatmentInStage.Regimen_Id = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_REGIMEN_ID));
					TreatmentInStage.Start_Date = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_START_DATE)));
					TreatmentInStage.Creation_TimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP)));
					TreatmentInStage.Created_By = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_CREATED_BY));
					TreatmentInStage.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.TREATMENT_IN_STAGES_IS_DELETED)));

					treatmentList.add(TreatmentInStage);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return treatmentList;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		try {
			dbfactory.database.delete(Schema.TABLE_TREATMENT_IN_STAGES, null,
					null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static void treatmentHardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.TreatmentInStages);
		try {
			dbfactory.database.delete(Schema.TABLE_TREATMENT_IN_STAGES,
					Schema.TREATMENT_IN_STAGES_TREATMENT_ID + "='"
							+ treatmentId + "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}

	public static String getPatientStage(String treatmentId, Context context) {
		int regimenId = getPatientRegimenId(treatmentId, context);
		if (regimenId == 0)
			return "";

		Master regimen = RegimenMasterOperations.getRegimen(regimenId, context);

		return regimen.stage;
	}

	public static String getPatientSchedule(String treatmentId, Context context) {
		int regimenId = getPatientRegimenId(treatmentId, context);
		if (regimenId == 0)
			return "";

		Master regimen = RegimenMasterOperations.getRegimen(regimenId, context);

		return regimen.schedule;
	}

}
