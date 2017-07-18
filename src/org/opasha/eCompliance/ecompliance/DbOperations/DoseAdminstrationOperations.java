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
import org.opasha.eCompliance.ecompliance.Model.Dose;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.modal.wcf.Doses;
import org.opasha.eCompliance.ecompliance.modal.wcf.DosesFull;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.DoseAdminViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DoseAdminstrationOperations {

	public static void addDose(String ID, String doseType, long doseDate,
			int regimenId, long creationTimeStamp, String createdBy,
			double latitude, double longitude, Context context) {
		if (ValidateDose(doseType, regimenId, doseDate, ID, context)) {
			if (canAddDose(ID, regimenId, doseDate, doseType, context)) {

				if (IsExists(ID, context)) {
					if (!isDoseTimeStampExist(ID, doseType, doseDate,
							creationTimeStamp, context)) {
						ContentValues DoseValues = new ContentValues();
						DoseValues.put(Schema.DOSE_ADMINISTRATION_TREATMENT_ID,
								ID);
						DoseValues.put(Schema.DOSE_ADMINISTRATION_DOSE_TYPE,
								doseType);
						DoseValues.put(Schema.DOSE_ADMINISTRATION_DOSE_DATE,
								doseDate);
						DoseValues.put(Schema.DOSE_ADMINISTRATION_REGIMEN_ID,
								regimenId);
						DoseValues.put(
								Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP,
								creationTimeStamp);
						DoseValues.put(Schema.DOSE_ADMINISTRATION_LATITUDE,
								latitude);
						DoseValues.put(Schema.DOSE_ADMINISTRATION_LONGITUDE,
								longitude);
						DoseValues.put(Schema.DOSE_ADMINISTRATION_CREATED_BY,
								createdBy);
						DbFactory dbfactory = new DbFactory(context)
								.CreateDatabase(TableEnum.DoseAdminstration);

						try {
							dbfactory.database.insert(
									Schema.TABLE_DOSE_ADMINISTRATION, null,
									DoseValues);
						} catch (Exception e) {
						}

						dbfactory.CloseDatabase();
					}
				} else {
					ContentValues DoseValues = new ContentValues();
					DoseValues.put(Schema.DOSE_ADMINISTRATION_TREATMENT_ID, ID);
					DoseValues.put(Schema.DOSE_ADMINISTRATION_DOSE_TYPE,
							doseType);
					DoseValues.put(Schema.DOSE_ADMINISTRATION_DOSE_DATE,
							doseDate);
					DoseValues.put(Schema.DOSE_ADMINISTRATION_REGIMEN_ID,
							regimenId);
					DoseValues.put(
							Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP,
							creationTimeStamp);
					DoseValues.put(Schema.DOSE_ADMINISTRATION_LATITUDE,
							latitude);
					DoseValues.put(Schema.DOSE_ADMINISTRATION_LONGITUDE,
							longitude);
					DoseValues.put(Schema.DOSE_ADMINISTRATION_CREATED_BY,
							createdBy);
					DbFactory dbfactory = new DbFactory(context)
							.CreateDatabase(TableEnum.DoseAdminstration);

					try {
						dbfactory.database.insert(
								Schema.TABLE_DOSE_ADMINISTRATION, null,
								DoseValues);
					} catch (Exception e) {
					}

					dbfactory.CloseDatabase();
				}
			}
		}
	}

	public static void deleteDose(String Id, long date, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		try {
			dbfactory.database.delete(Schema.TABLE_DOSE_ADMINISTRATION,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='" + Id
							+ "' and " + Schema.DOSE_ADMINISTRATION_DOSE_DATE
							+ "='" + date + "'", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbfactory.CloseDatabase();
	}

	public static long bulkInsert(ArrayList<DoseAdminViewModel> doseadmin,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);

		dbfactory.database.beginTransaction();
		for (DoseAdminViewModel view : doseadmin) {
			ContentValues DoseValues = new ContentValues();
			DoseValues.put(Schema.DOSE_ADMINISTRATION_TREATMENT_ID,
					view.Treatment_Id);
			DoseValues
					.put(Schema.DOSE_ADMINISTRATION_DOSE_TYPE, view.Dose_Type);
			DoseValues.put(Schema.DOSE_ADMINISTRATION_DOSE_DATE,
					GenUtils.getTimeFromTicks(view.Dose_Date));
			DoseValues.put(Schema.DOSE_ADMINISTRATION_REGIMEN_ID,
					view.Regimen_Id);
			DoseValues.put(Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP,
					GenUtils.getTimeFromTicks(view.Creation_TimeStamp));
			DoseValues.put(Schema.DOSE_ADMINISTRATION_LATITUDE, view.Latitude);
			DoseValues
					.put(Schema.DOSE_ADMINISTRATION_LONGITUDE, view.Longitude);
			DoseValues.put(Schema.DOSE_ADMINISTRATION_CREATED_BY,
					view.Created_By);

			dbfactory.database.insert(Schema.TABLE_DOSE_ADMINISTRATION, null,
					DoseValues);
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

	public static boolean IsExists(String treatmentID, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "=?",
					new String[] { treatmentID }, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return isExists;
	}

	public static boolean IsTodayExists(String treatmentID, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(
					true,
					Schema.TABLE_DOSE_ADMINISTRATION,
					null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE + " = "
							+ GenUtils.getCurrentDateLong() + " and "
							+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentID + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return isExists;
	}

	public static boolean isDoseTimeStampExist(String treatmentID,
			String doseType, long doseDate, long creationTimeStamp,
			Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentID + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToFirst()) {
				if (dbCursor
						.getString(
								dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
						.equals(doseType)
						&& dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP)) == creationTimeStamp
						&& dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE)) == doseDate) {
					isExists = true;
				}
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return isExists;
	}

	public static ArrayList<String> getPendingList(
			ArrayList<String> patientlist, long date, Context context) {

		ArrayList<String> Ids = new ArrayList<String>();

		try {
			DbFactory dbfactory = new DbFactory(context)
					.CreateDatabase(TableEnum.DoseAdminstration);
			Cursor dbCursor = null;

			for (String p : patientlist) {
				try {
					dbCursor = dbfactory.database.query(
							true,
							Schema.TABLE_DOSE_ADMINISTRATION,
							null,
							Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='" + p
									+ "' and "
									+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
									+ " <> '"
									+ DoseType.SelfAdministered.toString()
									+ "'", null, null, null,
							Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);
					if (dbCursor.moveToLast()) {
						int frequency = RegimenMasterOperations
								.getfrequency(
										dbCursor.getInt(dbCursor
												.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID)),
										context);
						long lastDoseDate = dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
						if (lastDoseDate < date) {
							if (dbCursor
									.getString(
											dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
									.equals(DoseType.Unsupervised.toString())) {
								dbCursor.moveToPrevious();
								if (dbCursor
										.getString(
												dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
										.equals(DoseType.Unsupervised
												.toString())) {
									dbCursor.moveToPrevious();
								}
							}
						}
						frequency = RegimenMasterOperations
								.getfrequency(
										dbCursor.getInt(dbCursor
												.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID)),
										context);
						lastDoseDate = dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
						int regimenId = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
						Master reg = RegimenMasterOperations.getRegimen(
								regimenId, context);
						if (!GenUtils.isScheduledOn(
								GenUtils.dateToDay(lastDoseDate),
								reg.scheduleDays)) {
							lastDoseDate -= GenUtils.ONE_DAY;
						}
						if (((date - lastDoseDate) / 86400000) > (frequency / 2)) {
							Ids.add(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID)));
						}
					} else {
						Ids.add(p);
					}
				} catch (Exception e) {
				}
			}
			dbfactory.CloseDatabase();
			dbCursor.close();
		} catch (Exception e) {
			Logger.e(context, "Get Pending", "DB Factory Crash");
		}
		return Ids;
	}

	public static int getPatientRegimenidByDate(String query, Context context) {
		int id = -1;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null, query, null, null,
					null, Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				do {
					id = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
					if (id != 0) {
						dbfactory.CloseDatabase();
						return id;
					}
				} while (dbCursor.moveToPrevious());
			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();
		dbCursor.close();

		return id;
	}

	private static boolean canAddDose(String ID, int regimenId, long doseDate,
			String doseType, Context context) {
		boolean isOn = true;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "= '" + ID
							+ "' and " + Schema.DOSE_ADMINISTRATION_DOSE_DATE
							+ "=" + doseDate + " and "
							+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID + "="
							+ regimenId, null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isOn = canUpdateDose(doseType, dbCursor.getString(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE)));
				if (isOn) {
					dbfactory.database.delete(Schema.TABLE_DOSE_ADMINISTRATION,
							Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "= '"
									+ ID + "' and "
									+ Schema.DOSE_ADMINISTRATION_DOSE_DATE
									+ "=" + doseDate + " and "
									+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID
									+ "=" + regimenId, null);
				}
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();

		return isOn;
	}

	private static boolean canUpdateDose(String sourceDose, String targetDose) {
		if (targetDose.equals(sourceDose)) // Both Doses are Same
			return false;

		if (targetDose.equals("Supervised")) // Target is Supervised so no
			// updates
			return false;

		if (sourceDose.equals("Supervised")) // Source is Supervised but source
			// and target not equal so
			// update
			return true;

		if (sourceDose.equals("SelfAdministered")) // Source is SelfAdmin but
			// source and target not
			// equal nor target is
			// Supervised so update
			return true;

		if (sourceDose.equals("Unsupervised")) // For Unsupervised Cases
		{
			return !targetDose.equals("SelfAdministered");
		}
		return false; // Source is Missed Dose
	}

	public static ArrayList<Patient> getPatientDoseforMissed(
			String filterExpression, Context context) {
		ArrayList<Patient> doseList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			if (filterExpression.length() == 0) {
				String query = "Select max("
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + ") as "
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " , "
						+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID + " from "
						+ Schema.TABLE_DOSE_ADMINISTRATION + " group by "
						+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID;
				dbCursor = dbfactory.database.rawQuery(query, null);
			} else {

				String query = "Select max("
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + ") as "
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " , "
						+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID + " from "
						+ Schema.TABLE_DOSE_ADMINISTRATION + " where "
						+ filterExpression + " group by "
						+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID;
				dbCursor = dbfactory.database.rawQuery(query, null);

			}

			if (dbCursor.moveToFirst()) {
				do {
					Patient doses = new Patient();
					doses.setPatient(
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE)));
					doseList.add(doses);
				} while (dbCursor.moveToNext());

			}

		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return doseList;

	}

	public static ArrayList<Patient> getdoses(String filterExpression,
			Context context) {
		ArrayList<Patient> treatmentList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null, null, null,
						null, null, Schema.DOSE_ADMINISTRATION_DOSE_DATE, null); // if
				// selection
				// is
				// null
				// return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null,
						filterExpression, null, null, null,
						Schema.DOSE_ADMINISTRATION_DOSE_DATE, null); // if
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
					Patient doses = new Patient();
					doses.setPatient(
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATED_BY)));

					treatmentList.add(doses);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return treatmentList;
	}

	public static ArrayList<String> getmissedCount(String filterExpression,
			Context context) {
		ArrayList<String> IDs = new ArrayList<String>();

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			if (filterExpression.length() != 0) {
				String query = "Select max("
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + ") as "
						+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " , "
						+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID + " from "
						+ Schema.TABLE_DOSE_ADMINISTRATION + " where "
						+ filterExpression + " group by "
						+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID;
				dbCursor = dbfactory.database.rawQuery(query, null);
				if (dbCursor.moveToFirst()) {
					String treatmentID;
					long doseDate;
					do {
						treatmentID = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID));
						doseDate = dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
						if (IsMissed(treatmentID, doseDate, context))
							IDs.add(treatmentID);
					} while (dbCursor.moveToNext());

				}
			}

		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();

		}
		return IDs;

	}

	private static boolean IsMissed(String treatmentId, long doseDate,
			Context context) {

		boolean ismissed = false;

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);

		Cursor dbCursor = null;

		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID
							+ "='"
							+ treatmentId
							+ "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
							+ "='"
							+ Enums.DoseType.getDoseType(DoseType.Missed)
									.toString() + "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + "="
							+ doseDate, null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToLast()) {
				ismissed = true;
			}
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return ismissed;
	}

	public static ArrayList<String> getVisitedPatients(String filterExpression,
			Context context) {
		ArrayList<String> Ids = new ArrayList<String>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null, null, null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null,
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
					Ids.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID)));
				} while (dbCursor.moveToNext());

			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return Ids;
	}

	public static ArrayList<String> getPendingListMissed(
			ArrayList<String> patientlist, long date, Context context) {
		ArrayList<String> Ids = new ArrayList<String>();
		try {
			DbFactory dbfactory = new DbFactory(context)
					.CreateDatabase(TableEnum.DoseAdminstration);
			Cursor dbCursor = null;
			for (String p : patientlist) {
				try {
					dbCursor = dbfactory.database.query(
							true,
							Schema.TABLE_DOSE_ADMINISTRATION,
							null,
							Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='" + p
									+ "' and "
									+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
									+ " <> '"
									+ DoseType.SelfAdministered.toString()
									+ "'", null, null, null,
							Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);
					if (dbCursor.moveToLast()) {
						int regimenId = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
						Master reg = RegimenMasterOperations.getRegimen(
								regimenId, context);
						try {
							int frequency = RegimenMasterOperations
									.getfrequency(regimenId, context);
							long doseDate = dbCursor
									.getLong(dbCursor
											.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
							long lastDoseDate = doseDate
									+ (GenUtils.ONE_DAY * frequency);
							if (!GenUtils.isScheduledOn(
									GenUtils.dateToDay(doseDate),
									reg.scheduleDays)) {
								lastDoseDate -= GenUtils.ONE_DAY;
							}
							if (date > lastDoseDate) {
								Ids.add(dbCursor.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID)));
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

					} else {
						Ids.add(p);
					}
				} catch (Exception e) {
				}
			}
			dbfactory.CloseDatabase();
			dbCursor.close();
		} catch (Exception e) {
			Logger.e(context, "Get Pending", "DB Factory Crash");
		}
		return Ids;
	}

	public static ArrayList<String> getDefaultIds(String patientlist,
			long CompareDateTick, Context context) {
		ArrayList<String> returnList = new ArrayList<String>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;

		try {
			dbCursor = dbfactory.database.rawQuery("select "
					+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID + ", max("
					+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + ") as "
					+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " from "
					+ Schema.TABLE_DOSE_ADMINISTRATION + " where "
					+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE + " <> '"
					+ Enums.DoseType.getDoseType(DoseType.Missed).toString()
					+ "' and " + Schema.DOSE_ADMINISTRATION_TREATMENT_ID
					+ " in (" + patientlist + ") group by "
					+ Schema.DOSE_ADMINISTRATION_TREATMENT_ID, null);
			if (dbCursor.moveToFirst()) {
				String Id;
				long date;
				do {
					Id = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID));
					date = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
					if (date < CompareDateTick) {
						if (isPatientDefault(Id, date, CompareDateTick, context)) {

							if (PatientHospitalizationOperations
									.HospitalisedPatientExists(Id, context)) {
								long endDate = PatientHospitalizationOperations
										.getEndDate(Id, context);
								String End_Date = String.valueOf(endDate);

								if (!End_Date.startsWith("-")) {

									long diff = GenUtils.getCurrentDateLong()
											- endDate;
									int diffDays = (int) (diff / (86400000));
									if (diffDays >= 60) {
										returnList.add(Id);
									}

								}
							} else
								returnList.add(Id);
						}
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return returnList;
	}

	private static boolean isPatientDefault(String id, long doseDate,
			long CompareDateTick, Context context) {
		boolean returnList = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {

			dbCursor = dbfactory.database.query(
					true,
					Schema.TABLE_DOSE_ADMINISTRATION,
					null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID
							+ "='"
							+ id
							+ "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
							+ " = '"
							+ Enums.DoseType.getDoseType(DoseType.Missed)
									.toString() + "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + " > "
							+ doseDate, null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);
			if (dbCursor.moveToFirst()) {
				long dosedate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
				if (dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE)) <= CompareDateTick) {
					returnList = true;
				}

			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();
		dbCursor.close();
		return returnList;
	}

	public static Dose getLastDose(String treatmentId, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Dose dose = new Dose();
		Cursor dbCursor = null;

		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,

					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToLast()) {
				dose.doseType =

				dbCursor.getString(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE));
				dose.doseDate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));

			} else {
				dose.doseType = "";
				dose.doseDate = 0;
			}
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		dbCursor.close();
		return dose;
	}

	public static Dose getPrevDose(String treatmentId, long date,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Dose dose = new Dose();
		Cursor dbCursor = null;

		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,

					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_DATE + "='"
							+ date + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToFirst()) {
				do {
					dose.doseType =

					dbCursor.getString(dbCursor
							.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE));
					dose.doseDate = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
				} while (dbCursor.moveToNext());
			} else {
				dose.doseType = "";
				dose.doseDate = 0;
			}
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		dbCursor.close();
		return dose;
	}

	public static long getlastSupervised(String treatmentId, Context context) {
		long dosedate = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);

		Cursor dbCursor = null;

		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID
							+ "='"
							+ treatmentId
							+ "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
							+ "='"
							+ Enums.DoseType.getDoseType(DoseType.Supervised)
									.toString() + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToLast()) {

				dosedate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));

			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();
		dbCursor.close();
		return dosedate;
	}

	public static Patient getlastSupervisedWithRegimen(String treatmentId,
			Context context) {
		Patient patient = new Patient();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID
							+ "='"
							+ treatmentId
							+ "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
							+ "='"
							+ Enums.DoseType.getDoseType(DoseType.Supervised)
									.toString() + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToLast()) {
				patient.doseDate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
				patient.regimenID = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return patient;
	}

	public static ArrayList<Dose> getPatientAllDoses(String treatmentId,
			Context context) {
		ArrayList<Dose> treatmentList = new ArrayList<Dose>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + " = '"
							+ treatmentId + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToFirst()) {
				do {
					Dose doses = new Dose();
					doses.setDoses(
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE)),
							dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID)));

					treatmentList.add(doses);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return treatmentList;
	}

	public static ArrayList<Doses> getdosesSync(String filterExpression,
			Context context) {
		ArrayList<Doses> doseList = new ArrayList<Doses>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null, null, null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null,
						filterExpression, null, null, null,
						Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP, null); // if
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
					try {

						long doseDate = dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));

						Doses doses = new Doses();
						doses.Latitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_LATITUDE));
						doses.Longitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_LONGITUDE));
						doses.Treatment_Id = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID));
						doses.Dose_Type = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE));
						doses.Dose_Date = GenUtils.longToDate(doseDate);
						doses.Creation_TimeStamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP)));
						doses.Regimen_Id = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
						doses.Created_By = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATED_BY));
						doseList.add(doses);

					} catch (Exception e1) {
						Log.e("Sync Dose", e1.getMessage());
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("Sync Dose", e.getMessage());
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return doseList;
	}

	public static ArrayList<DosesFull> getdosesForMissedRemoval(
			String filterExpression, Context context) {
		ArrayList<DosesFull> doseList = new ArrayList<DosesFull>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		Cursor dbCursor = null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null, null, null,
						null, null, null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_DOSE_ADMINISTRATION, null,
						filterExpression, null, null, null,
						Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP, null); // if
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
					try {

						long doseDate = dbCursor
								.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));

						DosesFull doses = new DosesFull();
						doses.Id = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_ROW_ID));
						doses.Latitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_LATITUDE));
						doses.Longitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_LONGITUDE));
						doses.Treatment_Id = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_TREATMENT_ID));
						doses.Dose_Type = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE));
						doses.Dose_Date = GenUtils.longToDate(doseDate);
						doses.Creation_TimeStamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP)));
						doses.Regimen_Id = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
						doses.Created_By = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_CREATED_BY));
						doseList.add(doses);

					} catch (Exception e1) {
						Log.e("Sync Dose", e1.getMessage());
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("Sync Dose", e.getMessage());
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return doseList;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		try {
			dbfactory.database.delete(Schema.TABLE_DOSE_ADMINISTRATION, null,
					null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static int getUnsupervisedCount(String treatmentId, Context context) {
		int returnValue = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);

		Cursor dbCursor = null;

		try {
			dbCursor = dbfactory.database.query(true,

			Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + " = '"
							+ treatmentId + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToLast()) {
				while (dbCursor
						.getString(
								dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
						.equals(Enums.DoseType.Unsupervised.toString())
						|| dbCursor
								.getString(
										dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
								.equals(Enums.DoseType.SelfAdministered
										.toString())) {
					if (dbCursor
							.getString(
									dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
							.equals(Enums.DoseType.Unsupervised.toString())) {
						int regId = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.DOSE_ADMINISTRATION_REGIMEN_ID));
						Master reg = RegimenMasterOperations.getRegimen(regId,
								context);
						if (reg.stage.equals("CP")) {
							returnValue += 3;
						} else {
							returnValue++;
						}
					}
					if (dbCursor.moveToPrevious() == false)
						break;
				}
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return returnValue;
	}

	public static int getDosesCountExceptMissed(String treatmentId,
			int regimenID, Context context) {
		int doseCount = 0;

		Master regimen = RegimenMasterOperations.getRegimen(regimenID, context);
		ArrayList<Master> regimens = RegimenMasterOperations.getRegimen(
				Schema.REGIMEN_MASTER_CATEGORY + "='" + regimen.catagory
						+ "' and " + Schema.REGIMEN_MASTER_STAGE + " = '"
						+ regimen.stage + "' and "
						+ Schema.REGIMEN_MASTER_IS_ACTIVE + " = 1", context);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		for (Master m : regimens) {
			try {
				doseCount = doseCount
						+ dbfactory.database.query(
								true,
								Schema.TABLE_DOSE_ADMINISTRATION,
								null,
								Schema.DOSE_ADMINISTRATION_TREATMENT_ID
										+ "='"
										+ treatmentId
										+ "' and "
										+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID
										+ "="
										+ m.regimenId
										+ " and "
										+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
										+ " != '"
										+ Enums.DoseType.getDoseType(
												DoseType.Missed).toString()
										+ "'", null, null, null, null, null)
								.getCount();
			} catch (Exception e) {
			}
		}
		dbfactory.CloseDatabase();
		return doseCount;
	}

	public static void doseHardDelete(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		try {
			dbfactory.database.delete(Schema.TABLE_DOSE_ADMINISTRATION,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + "='"
							+ treatmentId + "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}

	public static void doseHardDeleteByQuery(String query, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		try {
			dbfactory.database.delete(Schema.TABLE_DOSE_ADMINISTRATION, query,
					null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}

	public static long getlastUnSupervised(String treatmentID, Context context) {
		long dosedate = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);

		Cursor dbCursor = null;

		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID
							+ "='"
							+ treatmentID
							+ "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
							+ "='"
							+ Enums.DoseType.getDoseType(DoseType.Unsupervised)
									.toString() + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);
			if (dbCursor.moveToLast()) {
				dosedate = dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_DATE));
			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();
		dbCursor.close();
		return dosedate;

	}

	private static boolean IsValidToday(int regimenId, int dayWeek) {
		boolean retValue = false;

		switch (dayWeek) {
		case 1: // Sunday
			retValue = false;
			break;
		case 2: // Monday
			if (regimenId == 1 || regimenId == 3 || regimenId == 11
					|| regimenId == 13 || regimenId == 5 || regimenId == 15)
				retValue = true;
			break;
		case 3: // Tuesday
			if (regimenId == 2 || regimenId == 4 || regimenId == 12
					|| regimenId == 14 || regimenId == 6 || regimenId == 16)
				retValue = true;
			break;
		case 4: // Wednesday
			if (regimenId == 1 || regimenId == 3 || regimenId == 11
					|| regimenId == 13 || regimenId == 7 || regimenId == 17)
				retValue = true;
			break;
		case 5: // THursday
			if (regimenId == 2 || regimenId == 4 || regimenId == 12
					|| regimenId == 14 || regimenId == 8 || regimenId == 18)
				retValue = true;
			break;
		case 6: // Friday
			if (regimenId == 1 || regimenId == 3 || regimenId == 11
					|| regimenId == 13 || regimenId == 9 || regimenId == 19)
				retValue = true;
			break;
		case 7: // Saturday
			if (regimenId == 2 || regimenId == 4 || regimenId == 12
					|| regimenId == 14 || regimenId == 10 || regimenId == 20)
				retValue = true;
			break;
		}

		return retValue;
	}

	private static boolean ValidateDose(String doseType, int regimenId,
			long doseDate, String ID, Context context) {
		if (regimenId < 21) {
			long preDoseDate = 0;
			if (doseType == Enums.DoseType.getDoseType(DoseType.Supervised)
					.toString()) {
				preDoseDate = doseDate - GenUtils.ONE_DAY;
				Dose dose = getPrevDose(ID, preDoseDate, context);
				if (dose.doseType != "") {
					if (dose.doseType == Enums.DoseType.getDoseType(
							DoseType.Missed).toString()) {
						deleteDose(ID, preDoseDate, context);
					} else if (dose.doseType == Enums.DoseType.getDoseType(
							DoseType.Supervised).toString()) {
						if (regimenId == dose.regimenId) {
							if (!IsValidToday(regimenId,
									GenUtils.dateToDay(doseDate))) {
								return false;
							}
						}
					}
				}
			} else {
				if (doseType == Enums.DoseType.getDoseType(DoseType.Missed)
						.toString()) {
					// Check if next day dose is Supervised. If Yes, do not add
					// dose.
					// If dose day is saturday, check dose for Monday
					long nextDoseDate = doseDate + GenUtils.ONE_DAY;
					if (GenUtils.dateToDay(nextDoseDate) == 1) // Sunday
					{
						nextDoseDate += GenUtils.ONE_DAY;
					}

					Dose dose = getPrevDose(ID, nextDoseDate, context); // Get
					// Next
					// Date
					// Dose

					if (dose.equals("Supervised")
							|| dose.equals("Unsupervised")
							|| dose.equals("SelfAdministered")) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void deleteCurrentDayMissedDose(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);
		try {
			dbfactory.database.delete(
					Schema.TABLE_DOSE_ADMINISTRATION,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE + "='"
							+ GenUtils.getCurrentDateLong() + "' and "
							+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE + "='"
							+ DoseType.Missed.toString() + "'", null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();
	}
}
