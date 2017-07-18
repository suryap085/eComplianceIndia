/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.DbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DbFactory {

	public enum TableEnum {
		ECounseling, ScansR, Audit_Question_sum, Audit_Question, MasterAuditQuestionsSpnValue, MasterAuditQuestions, Patient_Diabetes, Master_Diabetes, Positive_Contacts, HivResult, iconDownloadLock, RegimenMaster, StageMaster, PatientStatusMaster, VisitorTypeMaster, DoseTypeMaster, Centers, AppStateConfiguration, Configuration, Patients, TreatmentInStages, DoseAdminstration, Visitors, VisitorLogin, Scans, InitialCounseling, Location, UnSupervisedDoseReason, ReasonMaster, Logger, MachineLocation, PatientLabs, InitialLabMaster, PatientIcon, MasterIcon, MasterApplicationIcon, NetWorkOperator, patientGenderAge, FutureMissedDose, PatientDosePrior, ScanIdentification, Hospitalization, InitialLabData, patientlabData, RepeatCounselling, Patientv2, ScansIris, ScansIrisR, Videos, VideosCategory
	};

	public Context context;
	private SQLiteHelperWrapper dbHelper;
	public SQLiteDatabase database;

	public DbFactory(Context context) {
		this.context = context;
	}

	public DbFactory CreateDatabase(TableEnum dbenum) throws SQLiteException {
		String dbName = "";
		StringBuilder querybuilder = new StringBuilder();
		int version = 0;
		String table = "";
		switch (dbenum) {
		
		case Videos:
			querybuilder.append("create table ")
					.append(Schema.TABLE_VIDEOS).append(" ( ");
			querybuilder.append(Schema.VIDEOS_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.VIDEOS_FILENAME).append(
					" text, ");
			querybuilder.append(Schema.VIDEOS_DESCRIPTION).append(
					" text); ");
			dbName = Schema.DATABASE_VIDEOS;
			version = Schema.DATABASE_VIDEOS_VERSION;
			table = Schema.TABLE_VIDEOS;
			break;
		case VideosCategory:
			querybuilder.append("create table ")
					.append(Schema.TABLE_VIDEOS_CATEGORY).append(" ( ");
			querybuilder.append(Schema.VIDEOS_CAT_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.VIDEOS_CAT_FILENAME).append(
					" text, ");
			querybuilder.append(Schema.VIDEOS_CAT_CATEGORY).append(
					" text, ");
			querybuilder.append(Schema.VIDEOS_CAT_TREATMENT_STAGE).append(
					" text, ");
			querybuilder.append(Schema.VIDEOS_CAT_TREATMENT_DAILY).append(
					" text, ");
			querybuilder.append(Schema.VIDEOS_CAT_PRIOIRITY).append(
					" integer); ");
			dbName = Schema.DATABASE_VIDEOS_CATEGORY;
			version = Schema.DATABASE_VIDEOS_CATEGORY_VERSION;
			table = Schema.TABLE_VIDEOS_CATEGORY;
			break;
		case ScansIris:
			querybuilder.append("create table ")
					.append(Schema.TABLE_SCANS_IRIS).append(" ( ");
			querybuilder.append(Schema.SCANS_IRIS_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.SCANS_IRIS_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.SCANS_IRIS_EYE).append(
					" text, ");
			querybuilder.append(Schema.SCANS_IRIS_SCAN).append(" BLOB, ");
			querybuilder.append(Schema.SCANS_IRIS_CREATION_TIMESTAMP).append(
					" numeric, ");
			querybuilder.append(Schema.SCANS_IRIS_CREATED_BY)
					.append(" text );");
			dbName = Schema.DATABASE_SCANS_IRIS;
			version = Schema.DATABASE_SCANS_IRIS_VERSION;
			table = Schema.TABLE_SCANS_IRIS;
			break;
		case ScansIrisR:
			querybuilder.append("create table ")
					.append(Schema.TABLE_SCANSIRIS_R).append(" ( ");
			querybuilder.append(Schema.SCANSIRIS_R_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.SCANSIRIS_R_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.SCANSIRIS_R_IRIS_EYE).append(
					" text, ");
			querybuilder.append(Schema.SCANSIRIS_R_SCAN).append(" text, ");
			querybuilder.append(Schema.SCANSIRIS_R_CREATION_TIMESTAMP).append(
					" numeric, ");
			querybuilder.append(Schema.SCANSIRIS_R_CREATED_BY).append(
					" text );");
			dbName = Schema.DATABASE_SCANSIRIS_R;
			version = Schema.DATABASE_SCANSIRIS_R_VERSION;
			table = Schema.TABLE_SCANSIRIS_R;
			break;
		
		case ECounseling:
			querybuilder.append("create table ")
			.append(Schema.TABLE_ECOUNSELING).append(" ( ");
			querybuilder.append(Schema.ECOUNSELING_ID).append(
			" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.ECOUNSELING_TREATMENT_ID).append(
			" text, ");
			querybuilder.append(Schema.ECOUNSELING_USER_ID).append(
			" text, ");
			querybuilder.append(Schema.ECOUNSELING_VIDEO_ID).append(" numeric, ");
			querybuilder.append(Schema.ECOUNSELING_START_TIME).append(
			" numeric, ");
			querybuilder.append(Schema.ECOUNSELING_END_TIME).append(
			" numeric, ");
			querybuilder.append(Schema.ECOUNSELING_STAGE).append(
					" text, ");
			querybuilder.append(Schema.ECOUNSELING_TREATMENTDAILY).append(
					" text, ");
			querybuilder.append(Schema.ECOUNSELING_SYNC_TIMESTAMP).append(
					" numeric );");
			
			dbName = Schema.DATABASE_ECOUNSELING;
			version = Schema.ECOUNSELING_VERSION;
			table = Schema.TABLE_ECOUNSELING;
			break;
			
		case Audit_Question_sum:
			querybuilder.append("create table ")
					.append(Schema.TABLE_AUDIT_QUESTION_SUM).append(" ( ");
			querybuilder.append("Row_Id").append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_ID).append(" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_ANSWER).append(
					" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_ISFINAL).append(
					" numeric, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_IS_DELETED).append(
					" numeric, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_QUESTION_TEXT)
					.append(" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_QA_ID).append(
					" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_SUM_CREATION_TIME)
					.append(" numeric );");

			dbName = Schema.DATABASE_AUDIT_QUESTION_SUM;
			version = Schema.DATABASE_AUDIT_QUESTION_SUM_VERSION;
			table = Schema.TABLE_AUDIT_QUESTION_SUM;
			break;
		case MasterAuditQuestionsSpnValue:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MASTER_AUDIT_QUESTION_SPN)
					.append(" ( ");
			querybuilder.append(" Row_Id ").append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_SPN_ID).append(
					" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_SPN_Q_ID).append(
					" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_SPN_IS_ACTIVE)
					.append(" numeric, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_SPN_LANGUAGE)
					.append(" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_SPN_PRIORITY)
					.append(" numeric, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_SPN_VALUE).append(
					" text );");

			dbName = Schema.DATABASE_MASTER_AUDIT_QUESTION_SPN;
			version = Schema.DATABASE_MASTER_AUDIT_QUESTION_SPN_VERSION;
			table = Schema.TABLE_MASTER_AUDIT_QUESTION_SPN;
			break;

		case MasterAuditQuestions:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MASTER_AUDIT_QUESTION).append(" ( ");
			querybuilder.append(" Row_Id ").append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_ID).append(
					" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_TYPE).append(
					" numeric, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_PARENT_ID).append(
					" numeric, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_USER_TYPE).append(
					" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_NAME).append(
					" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_LANGUAGE).append(
					" text, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_IS_ACTIVE).append(
					" numeric, ");
			querybuilder.append(Schema.MASTER_AUDIT_QUESTION_PRIORITY).append(
					" numeric );");

			dbName = Schema.DATABASE_MASTER_AUDIT_QUESTION;
			version = Schema.DATABASE_MASTER_AUDIT_QUESTION_VERSION;
			table = Schema.TABLE_MASTER_AUDIT_QUESTION;
			break;
		case Audit_Question:
			querybuilder.append("create table ")
					.append(Schema.TABLE_AUDIT_QUESTION).append(" ( ");
			querybuilder.append("Row_Id").append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.AUDIT_QUESTION_ID).append(" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_ANSWER).append(" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_IS_DELETED).append(
					" numeric, ");
			querybuilder.append(Schema.AUDIT_QUESTION_ISFINAL).append(
					" numeric, ");
			querybuilder.append(Schema.AUDIT_QUESTION_QA_ID).append(" text, ");
			querybuilder.append(Schema.AUDIT_QUESTION_CREATION_TIME).append(
					" numeric );");

			dbName = Schema.DATABASE_AUDIT_QUESTION;
			version = Schema.DATABASE_AUDIT_QUESTION_VERSION;
			table = Schema.TABLE_AUDIT_QUESTION;
			break;
		case Patient_Diabetes:
			querybuilder.append("create table ")
					.append(Schema.TABLE_PATIENT_DIABETES).append(" ( ");
			querybuilder.append(Schema.PATIENT_DIABETES_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.PATIENT_DIABETES_ID).append(" text, ");
			querybuilder.append(Schema.PATIENT_DIABETES_TEST_ID).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_DIABETES_RESULT).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_DIABETES_ADMINISTRATED_BY)
					.append(" text, ");
			querybuilder.append(Schema.PATIENT_DIABETES_CREATION_TIMESTAMP)
					.append(" numeric, ");
			querybuilder.append(Schema.PATIENT_DIABETES_IS_DELETED).append(
					" numeric );");
			dbName = Schema.DATABASE_PATIENT_DIABETES;
			version = Schema.VERSION_PATIENT_DIABETES;
			table = Schema.TABLE_PATIENT_DIABETES;
			break;
		case Master_Diabetes:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MASTER_DIABETES).append(" ( ");
			querybuilder.append(Schema.MASTER_DIABETES_ID).append(
					" integer PRIMARY KEY , ");
			querybuilder.append(Schema.MASTER_DIABETES_NAME).append(" text, ");
			querybuilder.append(Schema.MASTER_DIABETES_TEST_ID).append(
					" integer, ");
			querybuilder.append(Schema.MASTER_DIABETES_MIN_VALUE).append(
					" text, ");
			querybuilder.append(Schema.MASTER_DIABETES_MAX_VALUE).append(
					" text, ");
			querybuilder.append(Schema.MASTER_DIABETES_IS_ACTIVE).append(
					" numeric );");
			dbName = Schema.DATABASE_MASTER_DIABETES;
			version = Schema.VERSION_MASTER_DIABETES;
			table = Schema.TABLE_MASTER_DIABETES;
			break;
		case Positive_Contacts:
			querybuilder.append("create table ")
					.append(Schema.TABLE_POSTIVE_CONTACT).append(" ( ");
			querybuilder.append(Schema.POSTIVE_CONTACT_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_ID).append(" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_NAME).append(" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_REF_NAME).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_ADDRESS).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_AGE).append(" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_GENDER).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_CENTER_ID).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_PHONE)
					.append(" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_CREATED_BY).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_REF_ID).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_REF_PHONE).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.POSITIVE_CONTACT_IS_DELETED).append(
					" numeric, ");
			querybuilder.append(Schema.POSTIVE_CONTACT_CREATION_TIME).append(
					" numeric );");
			dbName = Schema.DATABASE_POSTIVE_CONTACT;
			version = Schema.VERSION_POSTIVE_CONTACT;
			table = Schema.TABLE_POSTIVE_CONTACT;
			break;
		case HivResult:
			querybuilder.append("create table ")
					.append(Schema.TABLE_PATIENT_HIV).append(" ( ");
			querybuilder.append(Schema.PATIENT_HIV_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.PATIENT_HIV_ID).append(" text, ");
			querybuilder.append(Schema.PATIENT_HIV_CREATED_BY)
					.append(" text, ");
			querybuilder.append(Schema.PATIENT_HIV_FINAL_RESULT).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_HIV_IS_DELETED).append(
					" numeric, ");
			querybuilder.append(Schema.PATIENT_HIV_SCREENING_RESULT).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_HIV_CREATION_TIMESTAMP).append(
					" numeric );");
			dbName = Schema.DATABASE_PATIENT_HIV;
			version = Schema.VERSION_PATIENT_HIV;
			table = Schema.TABLE_PATIENT_HIV;

			break;
		case iconDownloadLock:
			querybuilder.append("create table ")
					.append(Schema.TABLE_ICON_DOWNLOAD_LOCK).append(" ( ");
			querybuilder.append(Schema.ICON_DOWNLOAD_LOCK_ID).append(
					" integer PRIMARY KEY, ");
			querybuilder.append(Schema.ICON_DOWNLOAD_LOCK_NAME).append(
					" text, ");
			querybuilder.append(Schema.ICON_DOWNLOAD_LOCK_ENQUEUE_ID).append(
					" numeric );");
			dbName = Schema.DATABASE_ICON_DOWNLOAD_LOCK;
			version = Schema.DATABASE_ICON_DOWNLOAD_LOCK_VERSION;
			table = Schema.TABLE_ICON_DOWNLOAD_LOCK;
			break;

		case RegimenMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_REGIMEN_MASTER).append(" ( ");
			querybuilder.append(Schema.REGIMEN_MASTER_ID).append(
					" integer PRIMARY KEY, ");
			querybuilder.append(Schema.REGIMEN_MASTER_CATEGORY).append(
					" text, ");
			querybuilder.append(Schema.REGIMEN_MASTER_STAGE).append(" text, ");
			querybuilder.append(Schema.REGIMEN_MASTER_DAYS_FREQUENCY).append(
					" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_SCHEDULE).append(
					" text, ");
			querybuilder.append(Schema.REGIMEN_MASTER_DAYS)
					.append(" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_PRIORITY).append(
					" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_SELF_ADMIN_NUM).append(
					" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_SELF_ADMIN_FREQ).append(
					" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_SELF_ADMIN_SUNDAY)
					.append(" numeric, ");
			querybuilder.append(Schema.REGIMEN_MASTER_MISSED_NUM).append(
					" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_MISSED_FREQ).append(
					" integer, ");
			querybuilder.append(Schema.REGIMEN_MASTER_MISSED_SUNDAY).append(
					" numeric, ");
			querybuilder.append(Schema.REGIMEN_MASTER_UNSUPERVISED_FREQUENCY)
					.append(" numeric, ");
			querybuilder.append(Schema.REGIMEN_MASTER_IS_ACTIVE).append(
					" numeric );");
			dbName = Schema.DATABASE_REGIMEN_MASTER;
			version = Schema.DATABASE_REGIMEN_MASTER_VERSION;
			table = Schema.TABLE_REGIMEN_MASTER;
			break;
		case StageMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_STAGE_MASTER).append(" ( ");
			querybuilder.append(Schema.STAGE_MASTER_ID).append(
					" integer PRIMARY KEY, ");
			querybuilder.append(Schema.STAGE_MASTER_NAME).append(" text, ");
			querybuilder.append(Schema.STAGE_MASTER_IS_ACTIVE).append(
					" numeric ); ");
			dbName = Schema.DATABASE_STAGE_MASTER;
			version = Schema.DATABASE_STAGE_MASTER_VERSION;
			table = Schema.TABLE_STAGE_MASTER;
			break;
		case PatientStatusMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_PATIENT_STATUS_MASTER).append(" ( ");
			querybuilder.append(Schema.PATIENT_STATUS_MASTER_ID).append(
					" integer PRIMARY KEY, ");
			querybuilder.append(Schema.PATIENT_STATUS_MASTER_NAME).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_STATUS_MASTER_IS_ACTIVE).append(
					" numeric );");
			dbName = Schema.DATABASE_PATIENT_STATUS_MASTER;
			version = Schema.DATABASE_PATIENT_STATUS_MASTER_VERSION;
			table = Schema.TABLE_PATIENT_STATUS_MASTER;
			break;
		case VisitorTypeMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_VISITOR_TYPE_MASTER).append(" ( ");
			querybuilder.append(Schema.VISITOR_TYPE_MASTER_ID).append(
					" integer PRIMARY KEY, ");
			querybuilder.append(Schema.VISITOR_TYPE_MASTER_NAME).append(
					" text, ");
			querybuilder.append(Schema.VISITOR_TYPE_MASTER_IS_ACTIVE).append(
					" numeric ); ");
			dbName = Schema.DATABASE_VISITOR_TYPE_MASTER;
			version = Schema.DATABASE_VISITOR_TYPE_MASTER_VERSION;
			table = Schema.TABLE_VISITOR_TYPE_MASTER;
			break;
		case DoseTypeMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_DOSE_TYPE_MASTER).append(" ( ");
			querybuilder.append(Schema.DOSE_TYPE_MASTER_ID).append(
					" integer PRIMARY KEY, ");
			querybuilder.append(Schema.DOSE_TYPE_MASTER_NAME).append(" text, ");
			querybuilder.append(Schema.DOSE_TYPE_MASTER_IS_ACTIVE).append(
					" numeric ); ");
			dbName = Schema.DATABASE_DOSE_TYPE_MASTER;
			version = Schema.DATABASE_DOSE_TYPE_MASTER_VERSION;
			table = Schema.TABLE_DOSE_TYPE_MASTER;
			break;
		case Centers:
			querybuilder.append("create table ").append(Schema.TABLE_CENTERS)
					.append(" ( ");
			querybuilder.append(Schema.CENTERS_MACHINE_ID).append(
					" text PRIMARY KEY, ");
			querybuilder.append(Schema.CENTERS_CENTER_ID).append(" integer , ");
			querybuilder.append(Schema.CENTERS_MACHINE_TYPE).append(" text, ");
			querybuilder.append(Schema.CENTERS_NAME).append(" text );");
			dbName = Schema.DATABASE_CENTERS;
			version = Schema.DATABASE_CENTERS_VERSION;
			table = Schema.TABLE_CENTERS;
			break;
		case AppStateConfiguration:
			querybuilder.append("create table ")
					.append(Schema.TABLE_APP_STATE_CONFIGURATION).append(" ( ");
			querybuilder.append(Schema.APP_STATE_CONFIGURATION_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.APP_STATE_CONFIGURATION_KEY).append(
					" text, ");
			querybuilder.append(Schema.APP_STATE_CONFIGURATION_VALUE).append(
					" text );");
			dbName = Schema.DATABASE_APP_STATE_CONFIGURATION;
			version = Schema.DATABASE_APP_STATE_CONFIGURATION_VERSION;
			table = Schema.TABLE_APP_STATE_CONFIGURATION;
			break;
		case Configuration:
			querybuilder.append("create table ")
					.append(Schema.TABLE_CONFIGURATION).append(" ( ");
			querybuilder.append(Schema.CONFIGURATION_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.CONFIGURATION_KEY).append(" text, ");
			querybuilder.append(Schema.CONFIGURATION_VALUE).append(" text );");
			dbName = Schema.DATABASE_CONFIGURATION;
			version = Schema.DATABASE_CONFIGURATION_VERSION;
			table = Schema.TABLE_CONFIGURATION;
			break;
		case Patients:
			querybuilder.append("create table ").append(Schema.TABLE_PATIENTS)
					.append(" ( ");
			querybuilder.append(Schema.PATIENTS_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.PATIENTS_TREATMENT_ID).append(" text, ");
			querybuilder.append(Schema.PATIENTS_NAME).append(" text, ");
			querybuilder.append(Schema.PATIENTS_STATUS).append(" text, ");
			querybuilder.append(Schema.PATIENTS_CENTER_ID)
					.append(" integer , ");
			querybuilder.append(Schema.PATIENTS_PHONE_NUMBER)
					.append(" text , ");
			querybuilder.append(Schema.PATIENTS_MACHINE_ID).append(" text , ");
			querybuilder.append(Schema.PATIENTS_REG_DATE).append(" numeric , ");
			querybuilder.append(Schema.PATIENTS_IS_COUNSELLING_PENDING).append(
					" numeric , ");
			querybuilder.append(Schema.PATIENTS_CREATION_TIMESTAMP).append(
					" numeric , ");
			querybuilder.append(Schema.PATIENTS_CREATED_BY).append(" text, ");

			querybuilder.append(Schema.PATIENTS_ADDRESS).append(" text, ");
			querybuilder.append(Schema.PATIENTS_DISEASE).append(" text, ");
			querybuilder.append(Schema.PATIENTS_DISEASE_SITE).append(" text, ");
			querybuilder.append(Schema.PATIENTS_TYPE).append(" text, ");
			querybuilder.append(Schema.PATIENTS_NIKSHAY_ID).append(" text, ");
			querybuilder.append(Schema.PATIENTS_TBNUMBER).append(" text, ");
			querybuilder.append(Schema.PATIENTS_SMOKING_HISTORY).append(
					" numeric, ");

			querybuilder.append(Schema.PATIENTS_IS_DELETED).append(
					" numeric );");
			dbName = Schema.DATABASE_PATIENTS;
			version = Schema.DATABASE_PATIENTS_VERSION;
			table = Schema.TABLE_PATIENTS;
			break;

		case Patientv2:
			querybuilder.append("create table ").append(Schema.TABLE_PATIENTV2)
					.append(" ( ");
			querybuilder.append(Schema.PATIENT_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.PATIENT_TREATMENT_ID).append(" text, ");
			querybuilder.append(Schema.PATIENT_AADHAAR_NUMBER)
					.append(" text, ");
			querybuilder.append(Schema.PATIENT_PATIENT_SOURCE)
					.append(" text, ");

			querybuilder.append(Schema.PATIENT_CREATIONTIME_STAMP).append(
					" numeric , ");
			querybuilder.append(Schema.PATIENT_CREATED_BY).append(" text, ");
			querybuilder.append(Schema.PATIENT_IS_DELETED)
					.append(" numeric );");
			dbName = Schema.DATABASE_PATIENTV2;
			version = Schema.VERSION_PATIENTV2;
			table = Schema.TABLE_PATIENTV2;
			break;

		case TreatmentInStages:
			querybuilder.append("create table ")
					.append(Schema.TABLE_TREATMENT_IN_STAGES).append(" ( ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_TREATMENT_ID)
					.append(" text, ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_REGIMEN_ID).append(
					" integer, ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_START_DATE).append(
					" numeric, ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_CREATION_TIMESTAMP)
					.append(" numeric, ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_CREATED_BY).append(
					" text, ");
			querybuilder.append(Schema.TREATMENT_IN_STAGES_IS_DELETED).append(
					" numeric );");
			dbName = Schema.DATABASE_TREATMENT_IN_STAGES;
			version = Schema.DATABASE_TREATMENT_IN_STAGES_VERSION;
			table = Schema.TABLE_TREATMENT_IN_STAGES;
			break;
		case DoseAdminstration:
			querybuilder.append("create table ")
					.append(Schema.TABLE_DOSE_ADMINISTRATION).append(" ( ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_TREATMENT_ID)
					.append(" text, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_DOSE_TYPE).append(
					" text, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_DOSE_DATE).append(
					" numeric, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_REGIMEN_ID).append(
					" integer, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_LATITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_LONGITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_CREATION_TIMESTAMP)
					.append(" numeric, ");
			querybuilder.append(Schema.DOSE_ADMINISTRATION_CREATED_BY).append(
					" text ); ");
			dbName = Schema.DATABASE_DOSE_ADMINISTRATION;
			version = Schema.DATABASE_DOSE_ADMINISTRATION_VERSION;
			table = Schema.TABLE_DOSE_ADMINISTRATION;
			break;
		case FutureMissedDose:
			querybuilder.append("create table ")
					.append(Schema.TABLE_FUTURE_MISSED_DOSE).append(" ( ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_DOSE_TYPE).append(
					" text, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_DOSE_DATE).append(
					" numeric, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_REGIMEN_ID).append(
					" integer, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_LATITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_LONGITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_CREATION_TIMESTAMP)
					.append(" numeric, ");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_CREATED_BY).append(
					" text,");
			querybuilder.append(Schema.FUTURE_MISSED_DOSE_IS_DELETED).append(
					" numeric );");
			dbName = Schema.DATABASE_FUTURE_MISSED_DOSE;
			version = Schema.DATABASE_FUTURE_MISSED_DOSE_VERSION;
			table = Schema.TABLE_FUTURE_MISSED_DOSE;
			break;
		case Visitors:
			querybuilder.append("create table ").append(Schema.TABLE_VISITORS)
					.append(" ( ");
			querybuilder.append(Schema.VISITORS_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.VISITORS_ID).append(" text, ");
			querybuilder.append(Schema.VISITORS_NAME).append(" text, ");
			querybuilder.append(Schema.VISITORS_VISITOR_TYPE).append(" text, ");
			querybuilder.append(Schema.VISITORS_TAB_ID).append(" text, ");
			querybuilder.append(Schema.VISITORS_REGISTRATION_DATE).append(
					" numeric, ");
			querybuilder.append(Schema.VISITORS_STATUS).append(" text, ");
			querybuilder.append(Schema.VISITORS_PHONE1).append(" text, ");

			querybuilder.append(Schema.VISITORS_IS_AUTHENTICATED).append(
					" numeric, ");
			querybuilder.append(Schema.VISITORS_CREATION_TIMESTAMP).append(
					" numeric, ");
			querybuilder.append(Schema.VISITORS_CREATED_BY).append(" text, ");
			querybuilder.append(Schema.VISITORS_IS_DELETED).append(
					" numeric );");
			dbName = Schema.DATABASE_VISITORS;
			version = Schema.DATABASE_VISITORS_VERSION;
			table = Schema.TABLE_VISITORS;
			break;
		case VisitorLogin:
			querybuilder.append("create table ")
					.append(Schema.TABLE_VISITOR_LOGIN).append(" ( ");
			querybuilder.append(Schema.VISITOR_LOGIN_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.VISITOR_LOGIN_VISITOR_ID).append(
					" text, ");
			querybuilder.append(Schema.VISITOR_LOGIN_LOGIN_TIMESTAMP).append(
					" numeric, ");
			querybuilder.append(Schema.VISITOR_LOGIN_LATITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.VISITOR_LOGIN_LONGITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.VISITOR_LOGIN_MACHINE_ID).append(
					" text );");
			dbName = Schema.DATABASE_VISITOR_LOGIN;
			version = Schema.DATABASE_VISITOR_LOGIN_VERSION;
			table = Schema.TABLE_VISITOR_LOGIN;
			break;
		case Scans:
			querybuilder.append("create table ").append(Schema.TABLE_SCANS)
					.append(" ( ");
			querybuilder.append(Schema.SCANS_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.SCANS_TREATMENT_ID).append(" text, ");
			querybuilder.append(Schema.SCANS_VISITOR_TYPE).append(" text, ");
			querybuilder.append(Schema.SCANS_SCAN).append(" BLOB, ");
			querybuilder.append(Schema.SCANS_CREATION_TIMESTAMP).append(
					" numeric, ");
			querybuilder.append(Schema.SCANS_CREATED_BY).append(" text );");
			dbName = Schema.DATABASE_SCANS;
			version = Schema.DATABASE_SCANS_VERSION;
			table = Schema.TABLE_SCANS;
			break;
		case InitialCounseling:
			querybuilder.append("create table ")
					.append(Schema.TABLE_INITIALCOUNSELING).append(" ( ");
			querybuilder.append(Schema.INTIALCOUNSELING_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.INTIALCOUNSELING_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.INTIALCOUNSELING_START_TIME).append(
					" numeric, ");
			querybuilder.append(Schema.INTIALCOUNSELING_END_TIME).append(
					" numeric );");
			dbName = Schema.DATABASE_INITIALCOUNSELING;
			version = Schema.DATABASE_INITIALCOUNSELING_VERSION;
			table = Schema.TABLE_INITIALCOUNSELING;
			break;
		case Location:
			querybuilder.append("create table ").append(Schema.TABLE_LOCATION)
					.append(" ( ");
			querybuilder.append(Schema.LOCATION_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.LOCATION_TREATMENT_ID).append(" text, ");
			querybuilder.append(Schema.LOCATION_LATITUDE).append(" numeric, ");
			querybuilder.append(Schema.LOCATION_TIMESTAMP).append(" numeric, ");
			querybuilder.append(Schema.LOCATION_LONGITUDE)
					.append(" numeric );");
			dbName = Schema.DATABASE_LOCATION;
			version = Schema.DATABASE_LOCATION_VERSION;
			table = Schema.TABLE_LOCATION;
			break;
		case Logger:
			querybuilder.append("create table ").append(Schema.TABLE_LOGGER)
					.append(" ( ");
			querybuilder.append(Schema.LOGGER_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.LOGGER_TAG).append(" text, ");
			querybuilder.append(Schema.LOGGER_MESSAGE).append(" text, ");
			querybuilder.append(Schema.LOGGER_TIMESTAMP).append(" numeric );");
			dbName = Schema.DATABASE_LOGGER;
			version = Schema.DATABASE_LOGGER_VERSION;
			table = Schema.TABLE_LOGGER;
			break;
		case UnSupervisedDoseReason:
			querybuilder.append("create table ")
					.append(Schema.TABLE_UNSUPERVISED_DOSE_REASON)
					.append(" ( ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_STARTDATE).append(
					" numeric, ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_ENDDATE).append(
					" numeric, ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_REASON).append(
					" integer, ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_CREATION_TIMESTAMP)
					.append(" numeric, ");
			querybuilder.append(Schema.UNSUPERVISED_DOSE_CREATED_BY).append(
					" text );");
			dbName = Schema.DATABASE_UNSUPERVISED_DOSE_REASON;
			version = Schema.DATABASE_UNSUPERVISED_DOSE_VERSION;
			table = Schema.TABLE_UNSUPERVISED_DOSE_REASON;
			break;
		case ReasonMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_REASON_MASTER).append(" ( ");
			querybuilder.append(Schema.REASON_MASTER_ID).append(
					" integer PRIMARY KEY ,");
			querybuilder.append(Schema.REASON_MASTER_REASON).append(" text, ");
			querybuilder.append(Schema.REASON_MASTER_IS_ACTIVE).append(
					" numeric ); ");
			dbName = Schema.DATABASE_REASON_MASTER;
			version = Schema.DATABASE_REASON_MASTER_VERSION;
			table = Schema.TABLE_REASON_MASTER;
			break;
		case MachineLocation:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MACHINE_LOCATION).append(" ( ");
			querybuilder.append(Schema.MACHINE_LOCATION_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.MACHINE_LOCATION_LATITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.MACHINE_LOCATION_LONGITUDE).append(
					" numeric, ");
			querybuilder.append(Schema.MACHINE_LOCATION_CREATION_TIMESTAMP)
					.append(" numeric );");
			dbName = Schema.DATABASE_MACHINE_LOCATION;
			version = Schema.DATABASE_MACHINE_LOCATION_VERSION;
			table = Schema.TABLE_MACHINE_LOCATION;
			break;
		case PatientLabs:
			querybuilder.append("create table ").append(Schema.TABLE_LAB)
					.append("( ").append(Schema.LAB_ID)
					.append(" integer primary key autoincrement, ")
					.append(Schema.LAB_TREATMENT_ID).append(" text, ")
					.append(Schema.LAB_RESULT_ID).append(" integer, ")
					.append(Schema.LAB_CREATION_TIMESTAMP).append(" numeric);");
			dbName = Schema.DATABASE_LAB;
			version = Schema.DATABASE_LAB_VERSION;
			table = Schema.TABLE_LAB;
			break;
		case InitialLabMaster:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MASTER_LAB).append(" ( ");
			querybuilder.append(Schema.MASTER_LAB_ID).append(
					" integer PRIMARY KEY ,");
			querybuilder.append(Schema.MASTER_LAB_NAME).append(" text, ");
			querybuilder.append(Schema.MASTER_LAB_IS_ACTIVE).append(
					" numeric ); ");
			dbName = Schema.DATABASE_MASTER_LAB;
			version = Schema.DATABASE_MASTER_LAB_VERSION;
			table = Schema.TABLE_MASTER_LAB;
			break;
		case PatientIcon:
			querybuilder.append("create table ")
					.append(Schema.TABLE_PATIENT_ICON).append(" ( ");
			querybuilder.append(Schema.PATIENT_ICON_ID).append(" text ,");
			querybuilder.append(Schema.PATIENT_ICON_ICON).append(" text, ");
			querybuilder.append(Schema.PATIENT_ICON_CREATION_TIMESTAMP).append(
					" numeric ); ");
			dbName = Schema.DATABASE_PATIENT_ICON;
			version = Schema.DATABASE_PATIENT_ICON_VERSION;
			table = Schema.TABLE_PATIENT_ICON;
			break;
		case MasterIcon:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MASTER_ICON).append(" ( ");
			querybuilder.append(Schema.MASTER_ICON_ID).append(" integer ,");
			querybuilder.append(Schema.MASTER_ICON_NAME).append(" text, ");
			querybuilder.append(Schema.MASTER_ICON_USED).append(" numeric, ");
			querybuilder.append(Schema.MASTER_ICON_DOWNLOADED).append(
					" numeric ); ");
			dbName = Schema.DATABASE_MASTER_ICON;
			version = Schema.DATABASE_MASTER_ICON_VERSION;
			table = Schema.TABLE_MASTER_ICON;
			break;
		case MasterApplicationIcon:
			querybuilder.append("create table ")
					.append(Schema.TABLE_MASTER_APPLICATION_ICON).append(" ( ");
			querybuilder.append(Schema.MASTER_APPLICATION_ICON_ID).append(
					" integer ,");
			querybuilder.append(Schema.MASTER_APPLICATION_ICON_NAME).append(
					" text, ");
			querybuilder.append(Schema.MASTER_APPLICATION_ICON_DOWNLOADED)
					.append(" numeric ); ");
			dbName = Schema.DATABASE_MASTER_APPLICATION_ICON;
			version = Schema.DATABASE_MASTER_APPLICATION_ICON_VERSION;
			table = Schema.TABLE_MASTER_APPLICATION_ICON;
			break;
		case NetWorkOperator:
			querybuilder.append("create table ")
					.append(Schema.TABLE_NETWORK_OPERATOR).append(" ( ");
			querybuilder.append(Schema.NETWORK_OPERATOR_ROW_ID).append(
					"  integer primary key autoincrement ,");
			querybuilder.append(Schema.NETWORK_OPERATOR_NAME).append(" text, ");
			querybuilder.append(Schema.NETWORK_OPERATOR_NUMBER).append(
					" text ); ");
			dbName = Schema.DATABASE_NETWORK_OPERATOR;
			version = Schema.VERSION_NETWORK_OPERATOR;
			table = Schema.TABLE_NETWORK_OPERATOR;
			break;
		case patientGenderAge:
			querybuilder.append("create table ")
					.append(Schema.TABLE_PATIENT_AGE_GENDER).append(" ( ");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_ROW_ID).append(
					"  integer primary key autoincrement ,");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_ID).append(" text, ");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_AGE).append(
					" integer, ");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_GENDER).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_CREATIONTIME).append(
					" numeric, ");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_IS_DELETED).append(
					" numeric, ");
			querybuilder.append(Schema.PATIENT_AGE_GENDER_CREATED_BY).append(
					" text ); ");
			dbName = Schema.DATABASE_PATIENT_AGE_GENDER;
			version = Schema.VERSION_PATIENT_AGE_GENDER;
			table = Schema.TABLE_PATIENT_AGE_GENDER;
			break;

		case PatientDosePrior:
			querybuilder.append("create table ")
					.append(Schema.TABLE_PATIENT_DOSETAKEN_PRIOR).append(" ( ");
			querybuilder.append(Schema.PATIENT_DOSETAKEN_PRIOR_ROW_ID).append(
					"  integer primary key autoincrement , ");
			querybuilder.append(Schema.PATIENT_DOSETAKEN_PRIOR_ID).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_DOSETAKEN_PRIOR_IP_DOSE).append(
					" integer, ");
			querybuilder.append(Schema.PATIENT_DOSETAKEN_PRIOR_EXTIP_DOSE)
					.append(" integer, ");
			querybuilder.append(Schema.PATIENT_DOSETAKEN_PRIOR_CP_DOSE).append(
					" integer, ");
			querybuilder.append(Schema.PATIENT_DOSETAKEN_PRIOR_IS_DELETED)
					.append(" numeric, ");
			querybuilder.append(
					Schema.PATIENT_DOSETAKEN_PRIOR_CREATION_TIMESTAMP).append(
					" numeric ); ");

			dbName = Schema.DATABASE_PATIENT_DOSETAKEN_PRIOR;
			version = Schema.VERSION_PATIENT_DOSETAKEN_PRIOR;
			table = Schema.TABLE_PATIENT_DOSETAKEN_PRIOR;
			break;

		case ScanIdentification:
			querybuilder.append("create table ")
					.append(Schema.TABLE_IDENTIFICATION).append(" ( ");
			querybuilder.append(Schema.IDENTIFY_ID).append(
					"  integer primary key autoincrement , ");
			querybuilder.append(Schema.IDENTIFY_TAB_ID).append(" text, ");
			querybuilder.append(Schema.IDENTIFY_TREATMENT_ID).append(" text, ");
			querybuilder.append(Schema.IDENTIFY_NOTES).append(" text, ");
			querybuilder.append(Schema.IDENTIFY_IS_IDENTIFIED).append(
					" numeric, ");
			querybuilder.append(Schema.IDENTIFY_CREATION_TIMESTAMP).append(
					" numeric ); ");

			dbName = Schema.DATABASE_IDENTIFICATION;
			version = Schema.VERSION_IDENTIFICATION;
			table = Schema.TABLE_IDENTIFICATION;
			break;

		case Hospitalization:
			querybuilder.append("create table ")
					.append(Schema.TABLE_HOSPITALIZATION).append(" ( ");
			querybuilder.append(Schema.HOSPITALIZAION_ROW_ID).append(
					"  integer primary key autoincrement , ");
			querybuilder.append(Schema.HOSPITALIZAION_TRANSACTION_ID).append(
					" numeric, ");
			querybuilder.append(Schema.HOSPITALIZAION_TREATMENT_ID).append(
					" text, ");
			querybuilder.append(Schema.HOSPITALIZAION_START_DATE).append(
					" numeric, ");
			querybuilder.append(Schema.HOSPITALIZAION_END_DATE).append(
					" numeric, ");
			querybuilder.append(Schema.HOSPITALIZAION_NOTES).append(" text, ");
			querybuilder.append(Schema.HOSPITALIZAION_CERATED_BY).append(
					" text, ");
			querybuilder.append(Schema.HOSPITALIZAION_CREATION_TIMESTAMP)
					.append(" numeric ); ");

			dbName = Schema.DATABASE_HOSPITALIZATION;
			version = Schema.VERSION_HOSPITALIZATION;
			table = Schema.TABLE_HOSPITALIZATION;
			break;

		case InitialLabData:
			querybuilder.append("create table ")
					.append(Schema.TABLE_INITIAL_LAB_DATA).append(" ( ");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_ROW_ID).append(
					"  integer primary key autoincrement ,");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_TREATMENTID).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_DATA).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_CREATED_BY).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_TAB_ID).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_ISDELETED).append(
					" numeric, ");
			querybuilder.append(Schema.PATIENT_INITIAL_LAB_CREATION_TIMESTAMP)
					.append(" numeric ); ");
			dbName = Schema.DATABASE_INITIAL_LAB_DATA;
			version = Schema.VERSION_INITIAL_LAB_DATA;
			table = Schema.TABLE_INITIAL_LAB_DATA;
			break;

		case patientlabData:
			querybuilder.append("create table ").append(Schema.TABLE_LAB_DATA)
					.append(" ( ");
			querybuilder.append(Schema.PATIENT_LAB_ROW_ID).append(
					"  integer primary key autoincrement ,");
			querybuilder.append(Schema.PATIENT_LAB_TREATMENTID).append(
					" text, ");
			querybuilder.append(Schema.PATIENT_LAB_MONTH).append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_DATE).append(" numeric, ");
			querybuilder.append(Schema.PATIENT_LAB_DMC).append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_NUMBER).append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_WEIGHT).append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_RESULT).append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_CREATED_BY)
					.append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_TAB_ID).append(" text, ");
			querybuilder.append(Schema.PATIENT_LAB_ISDELETED).append(
					" numeric, ");
			querybuilder.append(Schema.PATIENT_LAB_CREATION_TIMESTAMP).append(
					" numeric ); ");
			dbName = Schema.DATABASE_LAB_DATA;
			version = Schema.VERSION_LAB_DATA;
			table = Schema.TABLE_LAB_DATA;
			break;

		case RepeatCounselling:
			querybuilder.append("create table ")
					.append(Schema.TABLE_REPEAT_COUNSELLING).append(" ( ");
			querybuilder.append(Schema.REPEAT_COUNSELLING_ROW_ID).append(
					"  integer primary key autoincrement ,");
			querybuilder.append(Schema.REPEAT_COUNSELLING_TREATMENTID).append(
					" text, ");
			querybuilder.append(Schema.REPEAT_COUNSELLING_TABID).append(
					" text, ");
			querybuilder.append(Schema.REPEAT_COUNSELLING_TIMESTAMP).append(
					" numeric ); ");
			dbName = Schema.DATABASE_REPEAT_COUNSELLING;
			version = Schema.VERSION_REPEAT_COUNSELLING;
			table = Schema.TABLE_REPEAT_COUNSELLING;
			break;
		case ScansR:
			querybuilder.append("create table ").append(Schema.TABLE_SCANS_R)
					.append(" ( ");
			querybuilder.append(Schema.SCANS_R_ROW_ID).append(
					" integer PRIMARY KEY AUTOINCREMENT, ");
			querybuilder.append(Schema.SCANS_R_TREATMENT_ID).append(" text, ");
			querybuilder.append(Schema.SCANS_R_VISITOR_TYPE).append(" text, ");
			querybuilder.append(Schema.SCANS_R_SCAN).append(" text, ");
			querybuilder.append(Schema.SCANS_R_CREATION_TIMESTAMP).append(
					" numeric, ");
			querybuilder.append(Schema.SCANS_R_CREATED_BY).append(" text );");
			dbName = Schema.DATABASE_SCANS_R;
			version = Schema.DATABASE_SCANS_R_VERSION;
			table = Schema.TABLE_SCANS_R;
			break;
			
		
		default:
			break;
		}

		dbHelper = new SQLiteHelperWrapper(context, dbName, table,
				querybuilder.toString(), version);
		try {
			database = dbHelper.getWritableDatabase();
		} catch (Exception e) {
		}
		return this;
	};

	public void CloseDatabase() {
		if (database != null) {
			if (database.isOpen())
				dbHelper.close();
		}
	}
}
