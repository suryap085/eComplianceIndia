/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

public class Enums {
	public enum VisitorType {
		Patient, Counselor, PM, QualityAuditor, CDP, Other;

		public static VisitorType getVisitorType(String s) {
			VisitorType retVal = Other;
			for (VisitorType v : VisitorType.values()) {
				if (v.toString().equals(s)) {
					retVal = v;
				}
			}
			return retVal;
		}

		public static String GetViewString(VisitorType v) {
			String outData;
			switch (v) {
			case CDP:
				outData = "Community DOTS Provider";
				break;
			case Counselor:
				outData = "Counselor";
				break;
			case Other:
				outData = "Others";
				break;
			case Patient:
				outData = "Patient";
				break;
			case PM:
				outData = "Program Manager";
				break;
			default:
				outData = "Quality Auditor";
				break;
			}
			return outData;
		}
	}

	public enum Schedule {
		MWF, TThs, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

		public static Schedule getSchedule(String s) {
			Schedule retVal = MWF;
			for (Schedule v : Schedule.values()) {
				if (v.toString().equals(s)) {
					retVal = v;
				}
			}
			return retVal;
		}

	}

	public enum Signal {
		Good, Bad, Warn;

		public static Signal getSignal(String str) {
			Signal retVal = Good;
			for (Signal s : Signal.values()) {
				if (s.toString().equals(str)) {
					retVal = s;
				}
			}
			return retVal;
		}
	}

	public enum CategoryType {
		CAT1, CAT2, CAT3, CAT4, CAT5, CAT6;

		public static String getCategoryType(CategoryType CAT) {
			String Cat = "";
			switch (CAT) {
			case CAT1:
				Cat = "CAT-I";
				break;
			case CAT2:
				Cat = "CAT-II";
				break;
			case CAT3:
				Cat = "CAT-III";
				break;
			case CAT4:
				Cat = "CAT-IV";
				break;
			case CAT5:
				Cat = "CAT-V";
				break;
			case CAT6:
				Cat = "MDR";
				break;

			}
			return Cat;
		}
	}

	public enum StageType {
		IP, CP, ExtIP;

		public static String getStageType(StageType stage) {
			String retValue = "";
			switch (stage) {
			case IP:
				retValue = "IP";
				break;
			case CP:
				retValue = "CP";
				break;
			case ExtIP:
				retValue = "Ext-IP";
			}
			return retValue;

		}
	}

	public enum StatusType {
		Active, TreatmentComplete, Cured, Default, Failure, TransferOut, SwitchToXDR, Died, TransferredInternally, pending;
		public static String getStatusType(StatusType status) {
			String retValue = "";
			switch (status) {
			case Active:
				retValue = "Active";
				break;
			case TreatmentComplete:
				retValue = "Treatment Complete";
				break;
			case Cured:
				retValue = "Cured";
				break;
			case Default:
				retValue = "Default";
				break;
			case Failure:
				retValue = "Failure";
				break;
			case TransferOut:
				retValue = "Transfer Out";
				break;
			case SwitchToXDR:
				retValue = "Switched On XDR";
				break;
			case Died:
				retValue = "Died";
				break;
			case TransferredInternally:
				retValue = "Transferred Internally";
				break;
			case pending:
				retValue = "Pending";
				break;
			default:
				break;
			}
			return retValue;

		}
	}

	public enum RegimenType {
		Category, Stage, Schedule;

		public static RegimenType getRegimenType(RegimenType str) {
			RegimenType retVal = Category;
			for (RegimenType s : RegimenType.values()) {
				if (s.toString().equals(str.toString())) {
					retVal = s;
				}
			}
			return retVal;
		}
	}

	public enum AppStateKeyValues {
		LastMissedDoseLoged;

		public static AppStateKeyValues getAppKey(AppStateKeyValues keyname) {
			AppStateKeyValues retVal = LastMissedDoseLoged;
			for (AppStateKeyValues s : AppStateKeyValues.values()) {
				if (s.toString().equals(keyname.toString())) {
					retVal = s;
				}
			}
			return retVal;
		}
	}

	public enum DoseType {
		Supervised, SelfAdministered, Missed, Unsupervised;

		public static DoseType getDoseType(DoseType str) {
			DoseType retVal = Missed;
			for (DoseType s : DoseType.values()) {
				if (s.toString().equals(str.toString())) {
					retVal = s;
				}
			}
			return retVal;
		}
	}

	public enum ReportType {
		AllPatients, AllVisitor, PendingPatients, VisitedPatients, MissedPatients, VisitedVisitor, PatientsFromLegacySystem, VisitorReregistration, positiveContact, InactivePatient, hospitalisedPatient;

	}

	public enum IntentFrom {
		Home, Reports, EditPatient, HavingTrouble;

	}

	public enum IconMessages {
		regComplete, syncComplete, visitorRegisterSuccessfull, fingerPrintEnable, fingerPrintDisable, sundayBlock, scanAlreadyExists, NA, defaultCanNotEdit, visitorOrPatientNotExist, identificationCompleteNotFound, fingerprintReaderError, identificationErrorDatabaseIsEmpty
	}

	public enum finger {
		rightThumb, rightIndexFinger, rightMiddleFinger, rightRingFinger, rightPinky, leftThumb, leftIndexFinger, leftMiddleFinger, leftRingFinger, leftPinky
	}

	public enum backIntent {
		status, icon, center, scan, report
	}

	public enum TestIds {
		glucometerTest, HBA1cTest;
		public static int getId(TestIds ids) {
			switch (ids) {
			case glucometerTest:
				return 1;
			default:
				return 2;
			}
		}
	}
}
