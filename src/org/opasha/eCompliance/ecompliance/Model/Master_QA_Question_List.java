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

public class Master_QA_Question_List {
	public int MASTER_AUDIT_QUESTION_ID;
	public String MASTER_AUDIT_QUESTION_NAME;
	public boolean MASTER_AUDIT_QUESTION_IS_ACTIVE;
	public String MASTER_AUDIT_QUESTION_LANGUAGE;
	public int MASTER_AUDIT_QUESTION_TYPE;
	public int MASTER_AUDIT_QUESTION_PARENT_ID;
	public int MASTER_AUDIT_QUESTION_PRIORITY;
	public ArrayList<Master_QA_Question> Childs;
	public String MASTER_AUDIT_QUESTION_USER_TYPE;
	public boolean MASTER_AUDIT_QUESTION_USER_Value;
	public String MASTER_AUDIT_QUESTION_USER_TEXT_Value;
	public ArrayList<String> MASTER_AUDIT_QUESTION_USER_SPINNER_OPTION;

	public Master_QA_Question_List() {
		Childs = new ArrayList<Master_QA_Question>();
		MASTER_AUDIT_QUESTION_USER_TEXT_Value = "";
		MASTER_AUDIT_QUESTION_USER_SPINNER_OPTION = new ArrayList<String>();
	}
}
