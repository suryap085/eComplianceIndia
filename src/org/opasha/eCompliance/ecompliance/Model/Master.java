/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Model;

public class Master {

	// For four master table
	// a. patient_status_master
	// b. stage_master
	// c. visitor_type_master
	// d. dose_type_master
	public int ID;
	public String name;
	public boolean isActive;

	// For Regimen_master Table
	public int regimenId;
	public String catagory;
	public String stage;
	public int daysFrequency;
	public String schedule;
	public int scheduleDays;
	
	public int numSelfAdmin;
	public int selfAdminFreq;
	public boolean selfAdminSunday;
	
	public int numMissed;
	public int missedFreq;
	public boolean missedSunday;
	
	public int unsupervisedFreq;
	
	public int priority;
	public boolean regimenIsActive;

	public void setPatientStatus(int id, String name, boolean isActive) {
		this.ID = id;
		this.name = name;
		this.isActive = isActive;
	}

	public void setStage(int id, String name, boolean isActive) {
		this.ID = id;
		this.name = name;
		this.isActive = isActive;
	}

	public void setVisitorType(int id, String name, boolean isActive) {
		this.ID = id;
		this.name = name;
		this.isActive = isActive;
	}

	public void setDoseType(int id, String name, boolean isActive) {
		this.ID = id;
		this.name = name;
		this.isActive = isActive;
	}

	public void setRegimen(int id, String category, String stage,
			int daysFrequency, String schedule, int scheduleDays,
			int priority, int numSelfAdmin, int selfAdminFreq, boolean selfAdminSunday, 
			int numMissed, int missedFreq, boolean missedSunday, int unsupervisedFreq, boolean isActive) {
		this.regimenId = id;
		this.catagory = category;
		this.stage = stage;
		this.daysFrequency = daysFrequency;
		this.schedule = schedule;
		this.scheduleDays = scheduleDays;

		this.priority = priority;
		this.regimenIsActive = isActive;
		this.selfAdminFreq = selfAdminFreq;
		this.numSelfAdmin = numSelfAdmin;
		this.selfAdminSunday = selfAdminSunday;
		this.missedFreq = missedFreq;
		this.missedSunday = missedSunday;
		this.numMissed = numMissed;
		this.unsupervisedFreq = unsupervisedFreq;
	}

	public void setCategory(String category) {
		this.catagory = category;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
}
