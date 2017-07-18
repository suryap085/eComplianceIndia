/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.Model;

public class Center {
	public String machineID;
	public String machineType;
	public String centerName;
	public int centerId;

	public void setCenterName(String center) {
		this.centerName = center;
	}

	public void setCenter(String machineId, String machineType,
			String centerName, int centerId) {
		this.machineID = machineId;
		this.machineType = machineType;
		this.centerName = centerName;
		this.centerId = centerId;
	}

}
