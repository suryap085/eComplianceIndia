/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.modal.wcf;

public class Master_Questions {
	public int id;
	public String name;
	public boolean Is_active;
	public int Type;
	public int Center_Id;

	public void setQuestions(int id, String name, boolean is_active, int Type,
			int center_id) {
		this.id = id;
		this.name = name;
		this.Type = Type;
		this.Center_Id = center_id;
		this.Is_active = is_active;

	}

}
