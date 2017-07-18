/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Model;

import java.util.ArrayList;

public class Patient {

	public String treatmentID;
	public String name;
	public String Status;
	public String phoneNumber;
	public String machineID;
	public boolean isCounsellingPending;
	public long creationTimeStamp;
	public String createdBy;
	public boolean isDeleted;
	public String category;
	public String stage;
	public String schedule;
	public int frequency;
	public int activeDays;
	public int centerId;
	public long lastSupervisedDate;

	public int regimenID;
	public long startDate;
	public long RegDate;

	public String doseType;
	public long doseDate;

	public String visitorType;
	public String scan;
	public String hivResult;
	
	public String address;
	public String disease;
	public String diseaseSite;
	public String patientType;
	public String nikshayId;
	public String tbNumber;
	public Boolean smokingHistory;
		

	ArrayList<Dose> dose;
	
	public double latitude;
	public double longitude;
	
	public void setPatient(String TreatmentId, String Name, String Status,
			String Phone, String MachineId, boolean IsCounsellingPending,
			long regDate, String address,String disease,String diseaseSite,String patientType,String nikshayId,String tbNumber,boolean smokingHistory,int centerId) {
		this.treatmentID = TreatmentId;
		this.name = Name;
		this.Status = Status;
		this.machineID = MachineId;
		this.isCounsellingPending = IsCounsellingPending;
		this.phoneNumber = Phone;
		this.RegDate = regDate;
		this.address=address;
		this.disease=disease;
		this.diseaseSite=diseaseSite;
		this.patientType=patientType;
		this.nikshayId=nikshayId;
		this.tbNumber=tbNumber;
		this.smokingHistory=smokingHistory;
		this.centerId = centerId;
		
		
	}

	public void setPatient(String treatmentId, int regimenid, long startDate,
			long cretionTimeStamp, String createdby, boolean isdeleted) {
		this.treatmentID = treatmentId;
		this.regimenID = regimenid;
		this.startDate = startDate;
		this.creationTimeStamp = cretionTimeStamp;
		this.createdBy = createdby;
		this.isDeleted = isdeleted;

	}

	public void setPatient(String treatmentId, long currentDoseDate) {
		this.treatmentID = treatmentId;
		this.doseDate = currentDoseDate;

	}

	public void setPatient(String treatmentId, long startDate, int regimenId,
			String name, String status, String phone,
			boolean isCounsellingPending, long regDate) {
		this.treatmentID = treatmentId;
		this.regimenID = regimenId;
		this.startDate = startDate;
		this.name = name;
		this.Status = status;
		this.phoneNumber = phone;
		this.isCounsellingPending = isCounsellingPending;
		this.RegDate = regDate;
	}

	public void setPatient(String treatmentId, long startDate, int regimenId,
			String name, String status, String phone,
			boolean isCounsellingPending, String Category, String stage,
			String schedule, int daysFrequency, int days, long regDate) {

		this.treatmentID = treatmentId;
		this.regimenID = regimenId;
		this.startDate = startDate;
		this.name = name;
		this.Status = status;
		this.phoneNumber = phone;
		this.isCounsellingPending = isCounsellingPending;
		this.category = Category;
		this.stage = stage;
		this.schedule = schedule;
		this.frequency = daysFrequency;
		this.activeDays = days;
		this.RegDate = regDate;

	}

	public void setPatient(String treatmentId, long startDate, int regimenId,
			String name, String status, String phone,
			boolean isCounsellingPending, String Category, String stage,
			String schedule, int frequency, int days, String doseType,
			long doseDate, long regDate) {
		this.treatmentID = treatmentId;
		this.regimenID = regimenId;
		this.startDate = startDate;
		this.name = name;
		this.Status = status;
		this.phoneNumber = phone;
		this.isCounsellingPending = isCounsellingPending;
		this.category = Category;
		this.stage = stage;
		this.schedule = schedule;
		this.doseDate = doseDate;
		this.doseType = doseType;
		this.frequency = frequency;
		this.activeDays = days;
		this.RegDate = regDate;

	}

	public void setPatient(String treatmentId, long startDate, int regimenId,
			String name, String status, String phone,
			boolean isCounsellingPending, String Category, String stage,
			String schedule, int frequency, int days, String doseType,
			long doseDate, long lastSupervised, long regDate) {
		this.treatmentID = treatmentId;
		this.regimenID = regimenId;
		this.startDate = startDate;
		this.name = name;
		this.Status = status;
		this.phoneNumber = phone;
		this.isCounsellingPending = isCounsellingPending;
		this.category = Category;
		this.stage = stage;
		this.schedule = schedule;
		this.doseDate = doseDate;
		this.doseType = doseType;
		this.frequency = frequency;
		this.activeDays = days;
		this.lastSupervisedDate = lastSupervised;
		this.RegDate = regDate;
	}

	public void setPatient(String treatmentId, String DoseType, long DoseDate,
			long timeStamp, int regimenId, String createdBy) {
		this.treatmentID = treatmentId;
		this.doseType = DoseType;
		this.doseDate = DoseDate;
		this.creationTimeStamp = timeStamp;
		this.createdBy = createdBy;
		this.regimenID = regimenId;
	}

	public void setPatient(String treatmentID) {
		this.treatmentID = treatmentID;

	}

	public void setPatient(String treatmentID, int daysfrequency) {
		this.treatmentID = treatmentID;
		this.frequency = daysfrequency;

	}

	public void setPatient(String treatmentId, String name, String phoneNumber,
			String machineID, long regDate) {
		this.treatmentID = treatmentId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.machineID = machineID;
		this.RegDate = regDate;
	}
}
