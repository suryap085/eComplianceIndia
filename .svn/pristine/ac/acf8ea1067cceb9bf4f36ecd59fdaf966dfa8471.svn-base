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
import org.opasha.eCompliance.ecompliance.Model.Patient;
import org.opasha.eCompliance.ecompliance.TextFree.PatientIconOperation;
import org.opasha.eCompliance.ecompliance.modal.wcf.SetPatientViewModel;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.Enums.StatusType;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientsOperations {

	public static long addPatient(String ID, String name, String status,
			String phoneNumber, String machineID, boolean isCounsellingPending,
			long creationTimeStamp, String createdBy, boolean isDeleted,
			long regDate, int centerId, String address, String diseaseSite,
			String diseaseClassification, String patientType,
			String nikashayId, String tbNumber, Boolean smokingHistory,
			Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(ID, creationTimeStamp, context)) {
			return 1;
		}
		deletePatientSoft(ID, context);
		ContentValues patientValues = new ContentValues();
		patientValues.put(Schema.PATIENTS_TREATMENT_ID, ID);
		patientValues.put(Schema.PATIENTS_NAME, name);
		patientValues.put(Schema.PATIENTS_STATUS, status);
		patientValues.put(Schema.PATIENTS_PHONE_NUMBER, phoneNumber);
		patientValues.put(Schema.PATIENTS_MACHINE_ID, machineID);
		patientValues.put(Schema.PATIENTS_CENTER_ID, centerId);
		patientValues.put(Schema.PATIENTS_IS_COUNSELLING_PENDING,
				isCounsellingPending);
		patientValues
				.put(Schema.PATIENTS_CREATION_TIMESTAMP, creationTimeStamp);
		patientValues.put(Schema.PATIENTS_CREATED_BY, createdBy);
		patientValues.put(Schema.PATIENTS_IS_DELETED, isDeleted);
		patientValues.put(Schema.PATIENTS_REG_DATE, regDate);

		patientValues.put(Schema.PATIENTS_ADDRESS, address);
		patientValues.put(Schema.PATIENTS_DISEASE, diseaseClassification);
		patientValues.put(Schema.PATIENTS_DISEASE_SITE, diseaseSite);
		patientValues.put(Schema.PATIENTS_TYPE, patientType);
		patientValues.put(Schema.PATIENTS_NIKSHAY_ID, nikashayId);
		patientValues.put(Schema.PATIENTS_TBNUMBER, tbNumber);
		patientValues.put(Schema.PATIENTS_SMOKING_HISTORY, smokingHistory);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			return dbfactory.database.insert(Schema.TABLE_PATIENTS, null,
					patientValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(ArrayList<PatientViewModel> patient,
			Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);

		dbfactory.database.beginTransaction();
		for (PatientViewModel view : patient) {
			ContentValues patientValues = new ContentValues();
			patientValues.put(Schema.PATIENTS_TREATMENT_ID, view.Treatment_Id);
			patientValues.put(Schema.PATIENTS_NAME, view.Name);
			patientValues.put(Schema.PATIENTS_STATUS, view.Status);
			patientValues.put(Schema.PATIENTS_PHONE_NUMBER, view.Phone_Number);
			patientValues.put(Schema.PATIENTS_MACHINE_ID, view.Tab_Id);
			patientValues.put(Schema.PATIENTS_CENTER_ID, view.Center_Id);
			patientValues.put(Schema.PATIENTS_IS_COUNSELLING_PENDING,
					view.Is_Counseling_Pending);
			patientValues.put(Schema.PATIENTS_CREATION_TIMESTAMP, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Creation_TimeStamp)));
			patientValues.put(Schema.PATIENTS_CREATED_BY, view.Created_By);
			patientValues.put(Schema.PATIENTS_IS_DELETED, view.Is_Deleted);
			patientValues.put(Schema.PATIENTS_REG_DATE, GenUtils
					.getTimeFromTicks(GenUtils
							.lastThreeDigitsZero(view.Registration_Date)));

			patientValues.put(Schema.PATIENTS_ADDRESS, view.PatientAddress);
			patientValues.put(Schema.PATIENTS_DISEASE,
					view.DiseaseClassification);
			patientValues.put(Schema.PATIENTS_DISEASE_SITE, view.EPSite);
			patientValues.put(Schema.PATIENTS_TYPE, view.PT_Type);
			patientValues.put(Schema.PATIENTS_NIKSHAY_ID, view.Nikshay_Id);
			patientValues.put(Schema.PATIENTS_TBNUMBER, view.TBNo);
			patientValues.put(Schema.PATIENTS_SMOKING_HISTORY,
					view.Hist_Smoking);

			dbfactory.database.insert(Schema.TABLE_PATIENTS, null,
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
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			if (dbfactory.database.query(
					Schema.TABLE_PATIENTS,
					null,
					Schema.PATIENTS_TREATMENT_ID + "= '" + Id + "' and "
							+ Schema.PATIENTS_CREATION_TIMESTAMP + " > "
							+ creationTimestamp, null, null, null, null)
					.getCount() > 1) {
				dbfactory.CloseDatabase();
				return false;
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return true;
	}

	// Public function to change isDeleted of a contact
	public static int deletePatientSoft(String treatmentId, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.PATIENTS_IS_DELETED, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);

		int num = -1;
		try {
			num = dbfactory.database.update(Schema.TABLE_PATIENTS, isDeleted,
					Schema.PATIENTS_TREATMENT_ID + "=?",
					new String[] { treatmentId });
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		return num;
	}

	// Public function to delete a patient
	public static void deletePatientHard(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			dbfactory.database.delete(Schema.TABLE_PATIENTS,
					Schema.PATIENTS_TREATMENT_ID + "='" + treatmentId + "'",
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbfactory.CloseDatabase();
	}

	/**
	 * Check if Patient Exists
	 * 
	 * @param treatmentID
	 *            - Treatment Id of Patient
	 * @param context
	 * @return - True if Exists, else false.
	 */
	public static boolean patientExists(String treatmentID, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			Cursor dbCursor = dbfactory.database.query(Schema.TABLE_PATIENTS,
					null, Schema.PATIENTS_TREATMENT_ID + "=?",
					new String[] { treatmentID }, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				isExists = true;
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return isExists;
	}
	
	public static void UpdateInitialCounseling(String treatmentId, Context context) {
		ContentValues values = new ContentValues();
		values.put(Schema.PATIENTS_IS_COUNSELLING_PENDING, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			dbfactory.database.update(Schema.TABLE_PATIENTS, values,
					Schema.PATIENTS_IS_DELETED + " = 0 and "
							+ Schema.PATIENTS_TREATMENT_ID + "='"
							+ treatmentId
							+ "'", null);
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
	}

	public static ArrayList<Patient> getPatients(String filterExpression,
			Context context) {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, null, null, null, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, filterExpression, null,
						null, null, null, null); // if
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
					Patient patients = new Patient();
					patients.treatmentID = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID));
					patients.name = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_NAME));
					patients.Status = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_STATUS));
					patients.phoneNumber = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_PHONE_NUMBER));
					patients.machineID = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_MACHINE_ID));
					patients.isCounsellingPending = Boolean
							.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.PATIENTS_IS_COUNSELLING_PENDING)));
					patients.RegDate = dbCursor.getLong(dbCursor
							.getColumnIndex(Schema.PATIENTS_REG_DATE));
					patients.centerId = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.PATIENTS_CENTER_ID));
					patients.hivResult = HIVOperation.getResult(
							patients.treatmentID, context);
					patients.creationTimeStamp = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.PATIENTS_CREATION_TIMESTAMP));
					patients.address = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_ADDRESS));
					patients.disease = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_DISEASE));
					patients.diseaseSite = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_DISEASE_SITE));
					patients.patientType = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TYPE));
					patients.nikshayId = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_NIKSHAY_ID));
					patients.tbNumber = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TBNUMBER));
					patients.smokingHistory = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENTS_SMOKING_HISTORY)));
					patientList.add(patients);

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

	public static ArrayList<String> getPatientsForScans(Context context) {
		ArrayList<String> patientList = new ArrayList<String>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor;
		try {
			
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, Schema.PATIENTS_IS_DELETED + "=0 AND " + Schema.PATIENTS_STATUS + " IN ('Active','Transferred Internally','Default')", null,
						null, null, null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values

			if (dbCursor.moveToFirst()) {

				do {
					patientList.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)));

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

	
	/**
	 * Get Patient Details
	 * 
	 * @param treatmentId
	 *            -Treatment Id
	 * @param context
	 * @return
	 */

	public static Patient getPatientDetails(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Patient patient = new Patient();
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_PATIENTS, null, Schema.PATIENTS_TREATMENT_ID
							+ "='" + treatmentId + "'", null, null, null,
					Schema.PATIENTS_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				patient.setPatient(
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_NAME)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_STATUS)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_PHONE_NUMBER)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_MACHINE_ID)),
						dbCursor.getString(
								dbCursor.getColumnIndex(Schema.PATIENTS_IS_COUNSELLING_PENDING))
								.equals("1"),
						dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.PATIENTS_REG_DATE)),

						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_ADDRESS)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_DISEASE)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_DISEASE_SITE)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_TYPE)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_NIKSHAY_ID)),
						dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_TBNUMBER)),
						GenUtils.getBoolean(dbCursor.getInt(dbCursor
								.getColumnIndex(Schema.PATIENTS_SMOKING_HISTORY))),

						dbCursor.getInt(dbCursor
								.getColumnIndex(Schema.PATIENTS_CENTER_ID)));

			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return patient;
	}

	public static String getQueryForMissed(String filterExpression,
			Context context) {
		String query = "";
		StringBuilder querybulder = new StringBuilder();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, null, null, null, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, filterExpression, null,
						null, null, null, null); // if
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
					querybulder
							.append(Schema.DOSE_ADMINISTRATION_TREATMENT_ID
									+ "= '"
									+ dbCursor.getString(dbCursor
											.getColumnIndex(Schema.PATIENTS_TREATMENT_ID))
									+ "'  or ");

				} while (dbCursor.moveToNext());
			}
			if (querybulder.length() > 4) {
				querybulder.setLength(querybulder.length() - 3);
				query = querybulder.toString();
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return query;
	}

	public static long getRegDate(String treatmentId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		long patient = 0;
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_PATIENTS, null, Schema.PATIENTS_TREATMENT_ID
							+ "='" + treatmentId + "'", null, null, null,
					Schema.PATIENTS_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToLast()) {
				patient =

				dbCursor.getLong(dbCursor
						.getColumnIndex(Schema.PATIENTS_REG_DATE));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return patient;
	}

	public static String getActivePatient(String query, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		StringBuilder Ids = new StringBuilder();
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_PATIENTS, null, query, null, null, null,
					Schema.PATIENTS_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToFirst()) {
				do {
					Ids.append("'"
							+ dbCursor.getString(dbCursor
									.getColumnIndex(Schema.PATIENTS_TREATMENT_ID))
							+ "' ,");
				} while (dbCursor.moveToNext());
				if (Ids.length() > 1) {
					Ids.setLength(Ids.length() - 1);
				}

			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return Ids.toString();
	}

	public static ArrayList<String> getActivePatientList(String query,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		ArrayList<String> Ids = new ArrayList<String>();
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_PATIENTS, null, query, null, null, null,
					Schema.PATIENTS_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToFirst()) {
				do {
					Ids.add(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)));
				} while (dbCursor.moveToNext());

			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return Ids;
	}

	public static ArrayList<String> getActivePatientListForTextFree(
			String query, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		ArrayList<String> Ids = new ArrayList<String>();
		try {
			Cursor dbCursor = dbfactory.database.query(true,
					Schema.TABLE_PATIENTS, null, query, null, null, null,
					Schema.PATIENTS_CREATION_TIMESTAMP, null);
			if (dbCursor.moveToFirst()) {
				do {
					if (PatientIconOperation.isIcon(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)),
							context))
						Ids.add(dbCursor.getString(dbCursor
								.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)));
				} while (dbCursor.moveToNext());

			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return Ids;
	}

	public static ArrayList<SetPatientViewModel> patientSync(
			String filterExpression, Context context) {
		ArrayList<SetPatientViewModel> patientList = new ArrayList<SetPatientViewModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, null, null, null, null,
						null, null); // if selection is null return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_PATIENTS, null, filterExpression, null,
						null, null, null, null); // if
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
					SetPatientViewModel patients = new SetPatientViewModel();
					patients.Treatment_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID));
					patients.Name = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_NAME));
					patients.Status = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_STATUS));
					patients.Phone_Number = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_PHONE_NUMBER));
					patients.Tab_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_MACHINE_ID));
					patients.Center_Id = dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.PATIENTS_CENTER_ID));
					patients.Registration_Date = GenUtils.longToDate(dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.PATIENTS_REG_DATE)));
					patients.Is_Counseling_Pending = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENTS_IS_COUNSELLING_PENDING)));
					patients.Creation_TimeStamp = GenUtils
							.longToDate(dbCursor.getLong(dbCursor
									.getColumnIndex(Schema.PATIENTS_CREATION_TIMESTAMP)));
					patients.Created_By = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_CREATED_BY));

					patients.PatientAddress = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_ADDRESS));
					patients.DiseaseClassification = dbCursor
							.getString(dbCursor
									.getColumnIndex(Schema.PATIENTS_DISEASE));
					patients.EPSite = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_DISEASE_SITE));
					patients.PT_Type = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TYPE));
					patients.Nikshay_Id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_NIKSHAY_ID));
					patients.TBNo = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TBNUMBER));
					patients.Hist_Smoking = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENTS_SMOKING_HISTORY)));

					patients.Is_Deleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENTS_IS_DELETED)));
					patientList.add(patients);

				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			dbfactory.database.delete(Schema.TABLE_PATIENTS, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static ArrayList<Patient> getPatientsFromLegacySystemFp(Context context) {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor;
		try {

			dbCursor = dbfactory.database.query(
					true,
					Schema.TABLE_PATIENTS,
					null,
					Schema.PATIENTS_IS_DELETED + " = 0 and "
							+ Schema.PATIENTS_STATUS + "='"
							+ Enums.StatusType.getStatusType(StatusType.Active)
							+ "'", null, null, null, null, null); // if

			if (dbCursor.moveToFirst()) {

				do {
					String id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID));
					if (!ScansOperations.isTreatmentidIdExist(id, context)) {
						Patient patients = new Patient();
						patients.setPatient(
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_NAME)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_STATUS)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_PHONE_NUMBER)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_MACHINE_ID)),
								Boolean.parseBoolean(dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_IS_COUNSELLING_PENDING))),
								dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.PATIENTS_REG_DATE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_ADDRESS)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_DISEASE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_DISEASE_SITE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_TYPE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_NIKSHAY_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_TBNUMBER)),
								GenUtils.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.PATIENTS_SMOKING_HISTORY))),
								dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.PATIENTS_CENTER_ID)));
						patientList.add(patients);
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}
	
	
	public static ArrayList<Patient> getPatientsFromLegacySystemIris(Context context) {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		Cursor dbCursor;
		try {

			dbCursor = dbfactory.database.query(
					true,
					Schema.TABLE_PATIENTS,
					null,
					Schema.PATIENTS_IS_DELETED + " = 0 and "
							+ Schema.PATIENTS_STATUS + "='"
							+ Enums.StatusType.getStatusType(StatusType.Active)
							+ "'", null, null, null, null, null); // if

			if (dbCursor.moveToFirst()) {

				do {
					String id = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENTS_TREATMENT_ID));
					if (!ScansIrisOperations.isTreatmentidIdExist(id, context)) {
						Patient patients = new Patient();
						patients.setPatient(
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_TREATMENT_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_NAME)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_STATUS)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_PHONE_NUMBER)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_MACHINE_ID)),
								Boolean.parseBoolean(dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_IS_COUNSELLING_PENDING))),
								dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.PATIENTS_REG_DATE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_ADDRESS)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_DISEASE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_DISEASE_SITE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_TYPE)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_NIKSHAY_ID)),
								dbCursor.getString(dbCursor
										.getColumnIndex(Schema.PATIENTS_TBNUMBER)),
								GenUtils.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.PATIENTS_SMOKING_HISTORY))),
								dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.PATIENTS_CENTER_ID)));
						patientList.add(patients);
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

	public static int CountDefaults(Context context) {
		int retVal = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			retVal = dbFactory.database
					.rawQuery(
							" select * from "
									+ Schema.TABLE_PATIENTS
									+ " where "
									+ Schema.PATIENTS_STATUS
									+ " = '"
									+ Enums.StatusType.getStatusType(StatusType.Default)
									+ "' and " + Schema.PATIENTS_IS_DELETED
									+ "=0", null).getCount();
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;

	}

	public static int getPatientCount(String query, Context context) {
		int retVal = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		String retString = " select * from " + Schema.TABLE_PATIENTS
				+ " where " + Schema.PATIENTS_TREATMENT_ID + " in (" + query
				+ ") and " + Schema.PATIENTS_IS_DELETED + "=0";
		try {
			retVal = dbFactory.database.rawQuery(retString, null).getCount();
		} catch (Exception e) {
		}

		dbFactory.CloseDatabase();
		return retVal;
	}

	public static int getActivePatientCount(Context context) {
		int retVal = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			retVal = dbFactory.database.rawQuery(
					" select * from " + Schema.TABLE_PATIENTS + " where "
							+ Schema.PATIENTS_STATUS + " = '"
							+ Enums.StatusType.getStatusType(StatusType.Active)
							+ "' and " + Schema.PATIENTS_IS_DELETED + "=0",
					null).getCount();
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;
	}

	public static int getNewPatientCount(String query, Context context) {
		int retVal = 0;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		String ret = "select * from " + Schema.TABLE_PATIENTS + " where "
				+ query;
		try {
			retVal = dbFactory.database.rawQuery(ret, null).getCount();

		} catch (Exception e) {
		}

		dbFactory.CloseDatabase();
		return retVal;
	}

	public static boolean isInitialCounsellingPending(String pTreatmentId,
			Context context) {
		boolean isCounsellingComplete = true;
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Patients);
		try {
			Cursor dbCursor = dbFactory.database.query(Schema.TABLE_PATIENTS,
					null, Schema.PATIENTS_IS_DELETED + "=0 and "
							+ Schema.PATIENTS_TREATMENT_ID + "= '"
							+ pTreatmentId + "'", null, null, null, null, null);
			dbCursor.moveToFirst();
			isCounsellingComplete = GenUtils
					.getBoolean(dbCursor.getInt(dbCursor
							.getColumnIndex(Schema.PATIENTS_IS_COUNSELLING_PENDING)));
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return isCounsellingComplete;
	}

}