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
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient_DoseTaken_Prior;
import org.opasha.eCompliance.ecompliance.modal.wcf.Patient.PatientDoseTakenPriorViewModel;
import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import org.opasha.eCompliance.ecompliance.util.Enums.DoseType;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PatientDosePriorOperations {

	public static long addPatient(String ID, String TakenIP_Dose,
			String TakenExtIP_Dose, String TakenCP_Dose,
			long creationTimeStamp, boolean isDeleted, Context context) {
		creationTimeStamp = GenUtils.lastThreeDigitsZero(creationTimeStamp);
		if (!shouldUpdate(ID, creationTimeStamp, context)) {
			return 1;
		}
		deletePatientDoseSoft(ID, context);
		ContentValues patientValues = new ContentValues();
		patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_ID, ID);
		patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_IP_DOSE, TakenIP_Dose);
		patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_EXTIP_DOSE,
				TakenExtIP_Dose);
		patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_CP_DOSE, TakenCP_Dose);
		patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP,
				creationTimeStamp);
		patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED, isDeleted);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientDosePrior);
		try {
			return dbfactory.database.insert(
					Schema.TABLE_PATIENT_DOSETAKEN_PRIOR, null, patientValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static long bulkInsert(
			ArrayList<PatientDoseTakenPriorViewModel> takenDose, Context context) {

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientDosePrior);

		dbfactory.database.beginTransaction();
		for (PatientDoseTakenPriorViewModel view : takenDose) {
			ContentValues patientValues = new ContentValues();
			patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_ID,
					view.TreatmentID);
			patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_IP_DOSE,
					view.TakenIpDoses);
			patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_EXTIP_DOSE,
					view.TakenExtIpDoses);
			patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_CP_DOSE,
					view.TakenCpDoses);
			patientValues.put(
					Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP, GenUtils
							.lastThreeDigitsZero(GenUtils
									.getTimeFromTicks(view.CreationTimeStamp)));
			patientValues.put(Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED,
					view.IsDeleted);

			dbfactory.database.insert(Schema.TABLE_PATIENT_DOSETAKEN_PRIOR,
					null, patientValues);
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
				.CreateDatabase(TableEnum.PatientDosePrior);
		try {
			if (dbfactory.database
					.query(Schema.TABLE_PATIENT_DOSETAKEN_PRIOR,
							null,
							Schema.PATIENT_DOSETAKEN_PRIOR_ID
									+ "= '"
									+ Id
									+ "' and "
									+ Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP
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

	// Public function to change isDeleted of a contact
	public static int deletePatientDoseSoft(String treatmentId, Context context) {

		ContentValues isDeleted = new ContentValues();
		isDeleted.put(Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED, true);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientDosePrior);

		int num = -1;
		try {
			num = dbfactory.database.update(
					Schema.TABLE_PATIENT_DOSETAKEN_PRIOR, isDeleted,
					Schema.PATIENT_DOSETAKEN_PRIOR_ID + "=?",
					new String[] { treatmentId });
		} catch (Exception e) {
		}

		dbfactory.CloseDatabase();
		return num;
	}

	public static boolean patientExists(String treatmentID, Context context) {
		boolean isExists = false;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientDosePrior);
		try {
			Cursor dbCursor = dbfactory.database.query(
					Schema.TABLE_PATIENT_DOSETAKEN_PRIOR, null,
					Schema.PATIENT_DOSETAKEN_PRIOR_ID + "=?",
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

	// public function to get patientTakenDosePrior details
	public static ArrayList<PatientDoseTakenPriorViewModel> getPatientDosePrior(
			String fiterExpression, Context context) {
		ArrayList<PatientDoseTakenPriorViewModel> patientList = new ArrayList<PatientDoseTakenPriorViewModel>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientDosePrior);
		Cursor dbCursor;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_PATIENT_DOSETAKEN_PRIOR, null,
					fiterExpression, null, null, null, null, null); // if
																	// selection
																	// is null
																	// return
			if (dbCursor.moveToFirst()) {

				do {

					PatientDoseTakenPriorViewModel patients = new PatientDoseTakenPriorViewModel();
					patients.TreatmentID = dbCursor.getString(dbCursor
							.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_ID));
					patients.TakenIpDoses = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_IP_DOSE));
					patients.TakenExtIpDoses = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_EXTIP_DOSE));
					patients.TakenCpDoses = dbCursor
							.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_CP_DOSE));
					patients.IsDeleted = GenUtils
							.getBoolean(dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED)));
					patients.CreationTimeStamp = dbCursor
							.getLong(dbCursor
									.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP));

					patientList.add(patients);

				} while (dbCursor.moveToNext());
			}

		} catch (Exception e) {

		} finally {
			dbfactory.CloseDatabase();
		}
		return patientList;
	}

	// public function to get patient total Supervised dose count
	public static int getSupervisdDoseCount(String treatmentId, Context context) {
		int returnValue = 0;
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.DoseAdminstration);

		Cursor dbCursor;

		try {
			dbCursor = dbfactory.database.query(true,

			Schema.TABLE_DOSE_ADMINISTRATION, null,
					Schema.DOSE_ADMINISTRATION_TREATMENT_ID + " = '"
							+ treatmentId + "'", null, null, null,
					Schema.DOSE_ADMINISTRATION_DOSE_DATE, null);

			if (dbCursor.moveToFirst()) {
				do {
					if (dbCursor
							.getString(
									dbCursor.getColumnIndex(Schema.DOSE_ADMINISTRATION_DOSE_TYPE))
							.equals(Enums.DoseType.getDoseType(
									DoseType.Supervised).toString())) {
						returnValue++;
					}
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
		}
		return returnValue;
	}

	// public function to sync DoseTakenPrior Data
	public static ArrayList<Patient_DoseTaken_Prior> patientDoseTakenSync(
			String filterExpression, Context context) {
		ArrayList<Patient_DoseTaken_Prior> patienttakendose = new ArrayList<Patient_DoseTaken_Prior>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientDosePrior);
		Cursor dbCursor;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbFactory.database
						.query(false,
								Schema.TABLE_PATIENT_DOSETAKEN_PRIOR,
								null,
								null,
								null,
								null,
								null,
								Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP,
								null);
			} else {
				dbCursor = dbFactory.database
						.query(false,
								Schema.TABLE_PATIENT_DOSETAKEN_PRIOR,
								null,
								filterExpression,
								null,
								null,
								null,
								Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP,
								null);
				if (dbCursor.moveToFirst()) {
					do {
						Patient_DoseTaken_Prior dosePrior = new Patient_DoseTaken_Prior();
						dosePrior.Treatment_Id = dbCursor
								.getString(dbCursor
										.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_ID));
						dosePrior.Ip_Dose = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_IP_DOSE));
						dosePrior.ExtIp_Dose = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_EXTIP_DOSE));
						dosePrior.Cp_Dose = dbCursor
								.getInt(dbCursor
										.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_CP_DOSE));
						dosePrior.Creation_TimeStamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP)));
						dosePrior.Is_Deleted = GenUtils
								.getBoolean(dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED)));
						patienttakendose.add(dosePrior);
					} while (dbCursor.moveToNext());
				}
			}
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
		return patienttakendose;
	}

	// public function to get unsupervised dose count on the basis of category
	// and stage
	public static int getIP_ExIP_DosesCount(String treatmentId,
			String category, String stage, String doseType, Context context) {
		int doseCount = 0;
		ArrayList<Master> regimens = RegimenMasterOperations.getRegimen(
				Schema.REGIMEN_MASTER_CATEGORY + " = '" + category + "' and "
						+ Schema.REGIMEN_MASTER_STAGE + " = '" + stage + "'",
				context);

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
										+ " = '" + treatmentId + "' and "
										+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID
										+ " = '" + m.regimenId + "' and "
										+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
										+ " = '" + doseType + "'", null, null,
								null, null, null).getCount();
			} catch (Exception e) {
			}
		}
		dbfactory.CloseDatabase();
		return doseCount;
	}

	public static int getSelfAdmidDosesCount(String treatmentId,
			String category, String stage, String doseType, Context context) {
		int doseCount = 0;
		ArrayList<Master> regimens = RegimenMasterOperations.getRegimen(
				Schema.REGIMEN_MASTER_CATEGORY + " = '" + category + "' and "
						+ Schema.REGIMEN_MASTER_STAGE + " = '" + stage
						+ "' and " + Schema.REGIMEN_MASTER_DAYS_FREQUENCY
						+ " = 1", context);

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
										+ " = '" + treatmentId + "' and "
										+ Schema.DOSE_ADMINISTRATION_REGIMEN_ID
										+ " = '" + m.regimenId + "' and "
										+ Schema.DOSE_ADMINISTRATION_DOSE_TYPE
										+ " = '" + doseType + "'", null, null,
								null, null, null).getCount();
			} catch (Exception e) {
			}
		}
		dbfactory.CloseDatabase();
		return doseCount;
	}
}