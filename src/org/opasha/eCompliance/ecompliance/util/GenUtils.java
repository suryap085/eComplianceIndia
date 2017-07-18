/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.FTPClient;
import org.opasha.eCompliance.ecompliance.BackupActivity;
import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.FtpCredential;
import org.opasha.eCompliance.ecompliance.Model.IdentificationResultCustom;
import org.opasha.eCompliance.ecompliance.Model.ScanModel;
import org.opasha.eCompliance.ecompliance.TextFree.MasterIconOperation;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.futronictech.SDKHelper.FtrIdentifyRecord;
import com.futronictech.SDKHelper.FtrIdentifyResult;
import com.futronictech.SDKHelper.FutronicIdentification;
import com.futronictech.SDKHelper.FutronicSdkBase;

public class GenUtils {

	public static final long ONE_DAY = 86400000;
	public static final long TWO_DAY = 172800000;
	public static File sd;
	public static String MachineID;

	public static String getCurrentDay() {
		return new SimpleDateFormat("EEEE").format(Calendar.getInstance()
				.getTime());
	}

	public static long getTicksFromTime(long timeValue) {
		final long TICKS_AT_EPOCH = 621355968000000000L;
		long ticks = 0;
		ticks = (timeValue * 10000) + TICKS_AT_EPOCH;
		return ticks;
	}

	public static long lastThreeDigitsZero(long date) {
		return (date / 1000) * 1000;
	}

	public static long getTimeFromTicks(long tickValue) {
		final long TICKS_AT_EPOCH = 621355968000000000L;
		long timeValue = (tickValue - TICKS_AT_EPOCH) / 10000;
		Date date = new Date(timeValue);
		TimeZone tz = TimeZone.getDefault();
		int offsetFromUtc = tz.getOffset(date.getTime());

		return (timeValue - offsetFromUtc);
	}

	public static Bitmap getBitmapFromAsset(String strName, Context context) {
		AssetManager assetManager = context.getAssets();
		InputStream istr = null;
		try {
			try {
				istr = assetManager.open(strName);
			} catch (FileNotFoundException e) {
				istr = assetManager.open("noimage.png");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return BitmapFactory.decodeStream(istr);
	}

	public static Date longToDate(long val) {
		try {
			Date date = new Date(val);
			return date;
		} catch (Exception ex) {

		}
		return null;
	}

	public static long dateTimeToDate(long val) {
		Date date1 = new Date(val);
		String sDate = new SimpleDateFormat("yyyy-MM-dd").format(date1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = (Date) formatter.parse(sDate);
			long longDate = date.getTime();
			return longDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean getBoolean(int value) {
		switch (value) {
		case 1:
			return true;
		default:
			return false;
		}
	}

	public static long getCurrentDateLong() {
		try {
			String dates = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
					.format(Calendar.getInstance().getTime());
			SimpleDateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) formatter.parse(dates);
			long longDate = date.getTime();
			return longDate;
		} catch (Exception ex) {
		}
		return 0;
	}

	public static long LastmonthEndDate() {
		long dateValue = 0;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MONTH, -1);
		cal.add(Calendar.DATE, -1);
		dateValue = cal.getTimeInMillis();
		return dateValue;

	}

	public static long LastMonthStartdate() {
		long date = 0;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MONTH, -2);
		date = cal.getTimeInMillis();
		return date;
	}

	public static long currentMonthFirstDate() {
		long dateValue = 0;
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DATE, 1);

		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MONTH, -1);
		dateValue = cal.getTimeInMillis();
		return dateValue;
	}

	public static long currentMonthCurrentDate() {
		long dateValue = 0;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MONTH, -1);
		dateValue = cal.getTimeInMillis();
		return dateValue;
	}

	public static String longToDateString(long val) {
		String returnVal = "";
		try {
			Date date = new Date(val);
			SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy", new Locale(
					"en"));
			returnVal = df2.format(date).toString();
		} catch (Exception ex) {
		}
		return returnVal;
	}

	public static String longtotring(long val) {
		String returnVal = "";
		try {
			Date date = new Date(val);
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd",
					new Locale("en"));
			returnVal = df2.format(date).toString();
		} catch (Exception ex) {
		}
		return returnVal;
	}

	public static long monthEndDate(long dateValue) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateValue);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTimeInMillis();
	}

	/**
	 * Calculates the number of months between two dates
	 * 
	 * @param from
	 * @param to
	 * @return absolute value of number of months between the two dates
	 */
	public static int calcMonthsBetween(long date1, long date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date1);
		int minuendMonth = cal.get(Calendar.MONTH);
		int minuendYear = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(date2);
		int subtrahendMonth = cal.get(Calendar.MONTH);
		int subtrahendYear = cal.get(Calendar.YEAR);
		int difference = ((minuendYear - subtrahendYear) * 12)
				+ (minuendMonth - subtrahendMonth);
		return difference >= 0 ? difference : -difference;
	}

	/**
	 * get Date string for a date.
	 * 
	 * @param dateValue
	 *            in long
	 * @return Date in MMMM,YYYY format. Eg. January,2013
	 */
	public static String toDate(long dateValue) {
		String returnVal = "";
		try {
			Date date = new Date(dateValue);
			SimpleDateFormat df2 = new SimpleDateFormat("MMMM,yyyy",
					new Locale("en"));
			returnVal = df2.format(date).toString();
		} catch (Exception ex) {
		}
		return returnVal;
	}

	public static String longToDateTimeString(long val) {
		String returnVal = "";
		try {
			Date date = new Date(val);

			SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy KK:mm aa ",
					new Locale("en"));
			returnVal = df2.format(date).toString();
		} catch (Exception ex) {
		}
		return returnVal;
	}

	/**
	 * Get day of week for a particular date
	 * 
	 * @param dateValue
	 *            in long
	 * @return int value for day Eg. 1 for Sunday and 7 for Saturday
	 */
	public static int dateToDay(long dateValue) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateValue);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int ScheduleToDay(String schedule) {
		int day = 0;
		if (schedule.equals("Monday")) {
			day = 2;
		} else if (schedule.equals("Tuesday")) {
			day = 3;
		} else if (schedule.equals("Wednesday")) {
			day = 4;
		} else if (schedule.equals("Thursday")) {
			day = 5;
		} else if (schedule.equals("Friday")) {
			day = 6;
		} else if (schedule.equals("Saturday")) {
			day = 7;
		} else if (schedule.equals("Sunday")) {
			day = 1;
		}
		return day;
	}

	/**
	 * Get day of month.
	 * 
	 * @param dateValue
	 *            - date to get day of month.
	 * @return
	 */
	public static int monthDay(long dateValue) {
		int returnVal = 0;
		try {
			Date date = new Date(dateValue);
			SimpleDateFormat df2 = new SimpleDateFormat("dd", new Locale("en"));
			returnVal = Integer.parseInt(df2.format(date).toString().trim());
		} catch (Exception ex) {
		}
		return returnVal;
	}

	/**
	 * To change Date from String to millisecond(long)
	 * 
	 * @param string
	 *            date
	 * @return
	 */
	public static long dateStringToLong(String date) {
		long d = System.currentTimeMillis();
		SimpleDateFormat f = new SimpleDateFormat("dd-MMMM-yyyy");
		try {
			d = f.parse(date).getTime();
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return d;
	}

	public static long datetolong(String date) {

		long d = System.currentTimeMillis();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = f.parse(date).getTime();
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Check that regimen ID is schedule for day given as parameter.
	 * 
	 * @param weekDay
	 * @param scheduleDays
	 *            :- from Regimen master table
	 * @return
	 */
	public static boolean isScheduledOn(int weekDay, int activeDays) {
		boolean temp = false;
		String scheduleDays = String.format("%7s",
				Integer.toBinaryString(activeDays));
		//String scheduleDays = "1111111";
		switch (weekDay) {
		case 1:
			if (scheduleDays.charAt(0) == '1') {
				temp = true;
			}
			break;
		case 2:
			if (scheduleDays.charAt(1) == '1') {
				temp = true;
			}
			break;
		case 3:
			if (scheduleDays.charAt(2) == '1') {
				temp = true;
			}
			break;
		case 4:
			if (scheduleDays.charAt(3) == '1') {
				temp = true;
			}
			break;
		case 5:
			if (scheduleDays.charAt(4) == '1') {
				temp = true;
			}
			break;
		case 6:
			if (scheduleDays.charAt(5) == '1') {
				temp = true;
			}
			break;
		case 7:
			if (scheduleDays.charAt(6) == '1') {
				temp = true;
			}
			break;

		}
		return temp;
	}

	public static boolean IsInternetConnected(Context context) {
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState() == NetworkInfo.State.CONNECTED
				|| connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			connected = true;
		}
		return connected;
	}

	public static boolean CheckServerRunning(Context context) {
		boolean retValue = false;
		try {
			URL url = new URL("http://www.ecompliancesuiteindia.com");
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("User-Agent", "eContact Tracing");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
			urlc.connect();
			if (urlc.getResponseCode() == 200) {
				retValue = true;
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	public static void setAppMissedDose(Context context) {
		((eComplianceApp) context.getApplicationContext()).missedDose = LoadAppData
				.getMissedPatients(context);
	}

	public static void setAppPendingToday(Context context) {
		((eComplianceApp) context.getApplicationContext()).pendingDoses = LoadAppData
				.getPendingPatients(context);
	}

	public static void setAppVisitedToday(Context context) {
		((eComplianceApp) context.getApplicationContext()).visitToday = LoadAppData
				.getVisitedPatients(context);
	}

	public static void setHospitalisedPatient(Context context) {
		((eComplianceApp) context.getApplicationContext()).hospitalised = LoadAppData
				.getHospitalisedPatient(context);
	}

	public static IdentificationResultCustom Identify(
			FutronicSdkBase m_Operation, ArrayList<ScanModel> scans, int nResult) {
		byte[] newScan = null;
		final int BUCKET_SIZE = 1000;
		FtrIdentifyRecord[] rgTemplates;
		int foundIndex = -1;
		int numIterations = (scans.size() / BUCKET_SIZE);
		if (scans.size() > (numIterations * BUCKET_SIZE)) {
			numIterations++;
		}
		for (int j = 0; j < numIterations; j++) {
			int startIndex = BUCKET_SIZE * j;
			int endIndex = startIndex + BUCKET_SIZE;
			if (scans.size() <= endIndex) {
				endIndex = scans.size();
			}
			int numEntries = endIndex - startIndex;
			rgTemplates = new FtrIdentifyRecord[numEntries];
			int k = 0;
			for (int i = startIndex; i < endIndex; i++) {
				try {
					rgTemplates[k] = new FtrIdentifyRecord();
					rgTemplates[k].m_KeyValue = scans.get(i).treatmentId
							.getBytes(Charset.defaultCharset());
					rgTemplates[k].m_Template = scans.get(i).scan;
				} catch (Exception e) {
					Log.e("Scan Error", e.getMessage());
				}
				k++;
			}

			newScan = ((FutronicIdentification) m_Operation).getBaseTemplate();

			FtrIdentifyResult Result = new FtrIdentifyResult();
			try {
				nResult = ((FutronicIdentification) m_Operation)
						.Identification(rgTemplates, Result);
			} catch (Exception e) {
				Log.e("Identification", e.getMessage());
			}

			if (nResult == FutronicSdkBase.RETCODE_OK) {
				if (Result.m_Index != -1) {
					foundIndex = Result.m_Index + startIndex;
					j = numIterations; // BREAK FOR LOOP IF SCAN FOUND
				}
			}
		}
		IdentificationResultCustom returnData = new IdentificationResultCustom();
		if (newScan != null) {
			returnData.scanTemplate = newScan;
		}
		returnData.foundIndex = foundIndex;
		returnData.nResult = nResult;
		return returnData;
	}

	public static void updateIsUsedMasterIcons(long lastSync, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.PatientIcon);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_PATIENT_ICON, null, null, null, null, null,
					null);
			while (dbCursor.moveToNext()) {
				MasterIconOperation.updateIsUsed(dbCursor.getString(dbCursor
						.getColumnIndex(Schema.PATIENT_ICON_ICON)), true,
						context);
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();

	}

	public static String makeFirstLetterUpperCase(String name) {

		StringBuffer stringbf = new StringBuffer();
		Matcher m = Pattern
				.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(
						name);
		while (m.find()) {
			m.appendReplacement(stringbf, m.group(1).toUpperCase()
					+ m.group(2).toLowerCase());
		}
		return m.appendTail(stringbf).toString();

	}

	public static void senBackup(Context context) {

		String machineId = DbUtils.getTabId(context);
		if (!machineId.isEmpty()) {
			sd = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File backupRootDir = new File(sd, "//eComplianceClient//"
					+ machineId + "//Backup");
			if (backupRootDir.exists()) {
				if (backupRootDir.isDirectory()) {
					String[] children = backupRootDir.list();
					for (int i = 0; i < children.length; i++) {
						new File(backupRootDir, children[i]).delete();
					}
				}
			} else {
				backupRootDir.mkdirs();
			}

			File data = Environment.getDataDirectory();
			String currentDBPath = "//data//" + context.getPackageName()
					+ "//databases//";
			File currentDB = new File(data, currentDBPath);
			if (currentDB.exists()) {
				if (currentDB.isDirectory()) {
					String[] children = currentDB.list();
					for (int i = 0; i < children.length; i++) {
						SaveBackup(children[i], machineId, context);
					}
					ZipBackup(machineId, context);
				}
			}
		}

	}

	private static void ZipBackup(String machineId, Context context) {
		try {
			if (sd.canWrite()) {
				String zipPath = "//eComplianceClient//" + machineId
						+ "//Backup//database.zip";

				String currentDBPath = "//eComplianceClient//" + machineId
						+ "//Backup//";
				File currentDB = new File(sd, currentDBPath);
				if (currentDB.exists()) {
					if (currentDB.isDirectory()) {
						String[] children = new String[currentDB.list().length];
						int i = 0;
						for (File file : currentDB.listFiles()) {
							String a = file.getName().toString();
							if (!a.equals("database.zip")) {
								children[i] = file.getPath();
								i++;
							}

						}
						File temp = new File(sd, zipPath);
						temp.createNewFile();
						new Compress(children, temp.getPath()).zip();
						GenUtils utils = new GenUtils();
						utils.new UploadTask().execute(new Object[]{machineId, context});
					}
				}
			}

		} catch (Exception e) {
			Logger.e(context, "ZipBackup", e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	private static void SaveBackup(String database, String machineId,
			Context context) {
		try {
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//" + context.getPackageName()
						+ "//databases//" + database;
				String backupDBPath = "//eComplianceClient//" + machineId
						+ "//Backup//" + database;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					if (!backupDB.exists()) {
						backupDB.createNewFile();
					}

					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();

					dst.transferFrom(src, 0, src.size());

					src.close();
					dst.close();

				}

			}
		} catch (Exception e) {

			Logger.e(context, "SaveBackup", e.getMessage());
		}
	}

	class UploadTask extends AsyncTask<Object, Void, String> {

		File sd = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		FTPClient ftpClient = new FTPClient();

		protected String doInBackground(Object... params) {
			try {
				FtpCredential credentials = DbUtils.GetFtpDetails((Context)params[1]);
				
				
				ftpClient.connect(InetAddress
						.getByName(credentials.FtpServer), 21);
				ftpClient.login(credentials.FtpUserId, credentials.FtpPassword);
				if(credentials.IsPassiveMode)
				{
					ftpClient.enterLocalPassiveMode();
				}
				ftpClient
						.changeWorkingDirectory("//eComplianceIndia//ClientDb");

				String machineId = (String)params[0];
				if (ftpClient.getReplyString().contains("250")) {
					ftpClient
							.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
					BufferedInputStream buffIn = null;
					String zipPath = "//eComplianceClient//" + machineId
							+ "//Backup//database.zip";
					File tempZip = new File(sd, zipPath);
					buffIn = new BufferedInputStream(new FileInputStream(
							tempZip.getPath()));

					String remFileName = machineId + "_"
							+ System.currentTimeMillis() + "_database.zip";
					ftpClient.deleteFile(remFileName);
					ftpClient.storeFile(remFileName, buffIn);
					buffIn.close();
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (SocketException e) {
			} catch (UnknownHostException e) {
			} catch (IOException e) {
				Log.e("Error", e.toString());
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
			return null;

		}

		protected void onPostExecute(String arg) {
			BackupActivity act = new BackupActivity();
			act.DeleteBackup();
		}
	}

	public static int gpsHighAccuracy(Context context) {
		int locationSetting = 0;
		try {
			final LocationManager manager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

			if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
					&& manager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locationSetting = 3;
			} else {
				if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

					locationSetting = 1;
				}

				if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
					locationSetting = 2;
				}
			}

		} catch (Exception e1) {
			locationSetting = 4;
		}
		return locationSetting;
	}

	public static int simPresent(Context context) {
		int simPresent = 2;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
				simPresent = 1;
			} else {
				simPresent = 0;
			}
		} catch (Exception e1) {
			simPresent = 2;
		}
		return simPresent;
	}
}
