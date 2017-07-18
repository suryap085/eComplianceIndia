/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.modal.wcf.Visitor;

import java.util.ArrayList;

public class VisitorsGetViewModel {
	public ArrayList<VisitorViewModel> Visitors;

	public ArrayList<ScansViewModel> Scans;

	public VisitorsGetViewModel() {
		Visitors = new ArrayList<VisitorViewModel>();
		Scans = new ArrayList<ScansViewModel>();

	}

}