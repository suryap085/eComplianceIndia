/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import org.opasha.eCompliance.ecompliance.DbOperations.MasterAuditQuesSpnValueOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.Master_QA_Questions;

import android.content.Context;

public class QAquestions {
	public QAquestions(Context context) {
		 Master_QA_Questions.AddQuestions(1,
				"Operation Asha Name Board", true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(2, "Appointment Certificate", true,
				"en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(3, "Bench", true, "en", 1, 1, 0,
				2, context);
		Master_QA_Questions.AddQuestions(4, "Stool", true, "en", 1, 1, 0,
				2, context);
		Master_QA_Questions.AddQuestions(5, "Rack", true, "en", 1, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(6, "Display Material", true, "en",
				1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(7, "Educational Material", true,
				"en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(8, "Weighting Machine", true, "en",
				1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(9,
				"Check Accuracy of Weighting Machine", true, "en", 1, 1, 0,
				2, context);
		Master_QA_Questions.AddQuestions(10, "Black Board:Present or Not",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(11, "Black Board: Update or Not",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(12, "10lts. Water Jug", true, "en",
				1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(13, "Disposable cups", true, "en",
				1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(14, "Antacid tablet in pink jar",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(15, "Domi tablets in orange jar",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(16,
				"Paracetamol tablets in blue jar", true, "en", 1, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(17, "Jars to follow-up sputum test",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(18,
				"Cloured Marker/Pen(Red & Blue)", true, "en", 1, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(19, "Stock regiter is maintained",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(20, "Stock regiter is up to date",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(21,
				"Stock Medicine matches with stock register", true, "en", 1, 1,
				0, 2, context);
		Master_QA_Questions.AddQuestions(22, "Medicine boxes show:", true,
				"en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(23, "Patient'Name", true, "en", 1,
				1, 22, 2, context);
		Master_QA_Questions.AddQuestions(24, "TB No.", true, "en", 1, 1, 22,
				2, context);
		Master_QA_Questions.AddQuestions(25,
				"Date of treatment commencement", true, "en", 1, 1, 22, 2,
				context);
		Master_QA_Questions.AddQuestions(26, "Tb 01 register present ot not",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(27,
				"Plain register/visitors register", true, "en", 1, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(28,
				"Condition and accuracy of patient's card", true, "en", 2, 1,
				0, 2, context);
		Master_QA_Questions.AddQuestions(29, "Attitude/Behavior of provider",
				true, "en", 2, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(30,
				"Attitude/Behavior of counselor", true, "en", 2, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(31, "Condition of Medicines", true,
				"en", 2, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(32,
				"Training material is available/not", true, "en", 1, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(33,
				"Visits regiter is available/not", true, "en", 1, 1, 0, 2,
				context);
		Master_QA_Questions.AddQuestions(34, "Visits regiter is updated.not",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(35, "Dustbin", true, "en", 1, 1, 0,
				2, context);
		Master_QA_Questions.AddQuestions(36,
				"Hom many patinet's houses were visited", true, "en", 3, 1, 0,
				2, context);
		Master_QA_Questions.AddQuestions(37,
				"How many boxes were matche with patient cards", true, "en", 3,
				1, 0, 2, context);
		Master_QA_Questions.AddQuestions(38,
				"Did the number of empty and full stripes match with the card",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions
				.AddQuestions(
						39,
						"How many dongles were present at the center with counselor?(At least 2 dongles should present with the couselor at the center)",
						true, "en", 2, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(40,
				"Is the dongle connected to the center machine?", true, "en",
				1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(41,
				"Is the center machine locked in a wooden box at center?",
				true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(42,
				"The finger print of the patients are taken on", true, "en", 2,
				1, 0, 2, context);
		Master_QA_Questions.AddQuestions(43,
				"Are  all the patients regiter on eCompliance?", true, "en", 1,
				1, 0, 2, context);
		Master_QA_Questions
				.AddQuestions(
						44,
						"How many patients are not regitered on the eCompliance and why?",
						true, "en", 3, 1, 0, 2, context);
		Master_QA_Questions.AddQuestions(45,
				"Center machine is logged in through:", true, "en", 2, 1, 0,
				2, context);
		Master_QA_Questions
				.AddQuestions(
						46,
						"Is any kind of game installed on the counselor or center machine?",
						true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions
				.AddQuestions(
						47,
						"The dongle used for sending messages by couselor at the center is: ",
						true, "en", 2, 1, 0, 2, context);
		Master_QA_Questions
				.AddQuestions(
						48,
						"Is the center machine or counselor machine broken or damaged from anywhere",
						true, "en", 1, 1, 0, 2, context);
		Master_QA_Questions
				.AddQuestions(
						49,
						"Are there any cuts in the wire of the charger and fingerprint reader?",
						true, "en", 1, 1, 0, 2, context);
		MasterAuditQuesSpnValueOperations.AddQuestions(1, 28, true, "en",
				1, "Good", context);
		MasterAuditQuesSpnValueOperations.AddQuestions(2, 28, true, "en",
				1, "Poor", context);
		MasterAuditQuesSpnValueOperations.AddQuestions(3, 29, true, "en",
				1, "Excellent", context);
		MasterAuditQuesSpnValueOperations.AddQuestions(4, 29, true, "en",
				1, "Good", context);
		MasterAuditQuesSpnValueOperations.AddQuestions(5, 29, true, "en",
				1, "Average", context);
		
	}
}
