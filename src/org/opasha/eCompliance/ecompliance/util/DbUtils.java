/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.CentersOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.DoseAdminstrationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.InitialCounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.LocationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientLabsOperation;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansIrisR_Operations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ScansROperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VisitorsOperations;
import org.opasha.eCompliance.ecompliance.Model.Center;
import org.opasha.eCompliance.ecompliance.Model.FtpCredential;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.TextFree.MasterIconOperation;
import org.opasha.eCompliance.ecompliance.TextFree.PatientIconOperation;

import android.content.Context;

public class DbUtils {

	public static boolean IsAdminLoggedIn() {
		// TODO: Add code to get this info from database
		return true;
	}

	public static void hardDeletePatient(String treatmentId, Context context) {
		DoseAdminstrationOperations.doseHardDelete(treatmentId, context);
		InitialCounselingOperations.counselingHardDelete(treatmentId, context);
		LocationOperations.locationHardDelete(treatmentId, context);
		PatientLabsOperation.labHardDelete(treatmentId, context);
		PatientsOperations.deletePatientHard(treatmentId, context);
		ScansOperations.deleteScans(context, treatmentId);
		TreatmentInStagesOperations.treatmentHardDelete(treatmentId, context);
		String icon = PatientIconOperation.getIcon(treatmentId, context);
		MasterIconOperation.hardDeleteIcon(icon, context);
		PatientIconOperation.deleteIcon(treatmentId, context);
	}

	public static String getTabId(Context context) {
		ArrayList<Center> centerList = CentersOperations.getCenter(context);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("C")) {
					return c.machineID;
				}
			}
		}
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("P")) {
					return c.machineID;
				}
			}
		}
		return "";
	}

	public static String getTabType(Context context) {
		ArrayList<Center> centerList = CentersOperations.getCenter(context);
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("C")) {
					return "C";
				}
			}
		}
		if (!centerList.isEmpty()) {
			for (Center c : centerList) {
				if (c.machineType.equals("P")) {
					return "P";
				}
			}
		}
		return "";
	}

	public static void removeUnassignedScans(Context context)
	{
		ArrayList<String> patients = PatientsOperations.getPatientsForScans(context);
		ArrayList<String> visitors = VisitorsOperations.getVisitorsForScans(context);
		patients.addAll(visitors);
		
		ArrayList<ScanModel> irisScans = ScansIrisOperations.getScans(false, context);
		
		for(ScanModel iris: irisScans)
		{
			if(!patients.contains(iris.treatmentId))
			{
				ScansIrisOperations.deleteScans(context, iris.treatmentId);
				ScansIrisR_Operations.deleteScans(context, iris.treatmentId);
			}
		}
		
		ArrayList<ScanModel> fpScans = ScansOperations.getScans(false, context);
		
		for(ScanModel fingerprint: fpScans)
		{
			if(!patients.contains(fingerprint.treatmentId))
			{
				ScansOperations.deleteScans(context, fingerprint.treatmentId);
				ScansROperations.deleteScans(context, fingerprint.treatmentId);
			}
		}
	}
	
	
	/**
	 * Get FTP Details
	 * @param context
	 * @return
	 */
	public static FtpCredential GetFtpDetails(Context context)
	{
		String ftpServer = ConfigurationOperations.getKeyValue(ConfigurationKeys.key_ftp_server, context);
		String ftpUserID = ConfigurationOperations.getKeyValue(ConfigurationKeys.key_ftp_user_id, context);
		String ftpPassword = ConfigurationOperations.getKeyValue(ConfigurationKeys.key_ftp_password, context);
		String ftpPassiveMode = ConfigurationOperations.getKeyValue(ConfigurationKeys.key_ftp_passive_mode, context);
		
		
		if(ftpServer.equals(""))
		{
			ftpServer = "ec2-54-208-70-53.compute-1.amazonaws.com";
		}
		
		if(ftpUserID.equals(""))
		{
			ftpUserID = "ftpuser";
		}
		
		if(ftpPassword.equals(""))
		{
			ftpPassword = "opasha@2016";
		}
		
		
		
		FtpCredential cred = new FtpCredential();
		cred.FtpPassword = ftpPassword;
		cred.FtpServer = ftpServer;
		cred.FtpUserId = ftpUserID;
		
		if(ftpPassiveMode.equals("1"))
		{
			cred.IsPassiveMode = true;
		}
		
		return cred;
	}
}
