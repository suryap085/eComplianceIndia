/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.DbOperations;

import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.modal.wcf.MachineLocationViewModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MachineLocationOperations {

	public static long add(Context context, double latitude, double longitude) {
		ContentValues locData = new ContentValues();
		locData.put(Schema.MACHINE_LOCATION_LATITUDE, latitude);
		locData.put(Schema.MACHINE_LOCATION_LONGITUDE, longitude);
		locData.put(Schema.MACHINE_LOCATION_CREATION_TIMESTAMP,
				System.currentTimeMillis());

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MachineLocation);
		long returnVal = -1;
		try {
			returnVal = dbfactory.database.insert(
					Schema.TABLE_MACHINE_LOCATION, null, locData);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnVal;

	}

	public static ArrayList<MachineLocationViewModel> get(
			String filterExpression, Context context) {
		String machineId = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Machine_Id, context);
		ArrayList<MachineLocationViewModel> locations = new ArrayList<MachineLocationViewModel>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MachineLocation);
		try {
			if (filterExpression==null||filterExpression.trim().equals("")) {
				Cursor dbCursor = dbFactory.database.query(false, Schema.TABLE_MACHINE_LOCATION,
						null, null, null, null, null,
						Schema.MACHINE_LOCATION_CREATION_TIMESTAMP, null);
				if (dbCursor.moveToFirst()) {
					do {
						MachineLocationViewModel location = new MachineLocationViewModel();
						location.Machine_Id = machineId;
						location.Latitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.MACHINE_LOCATION_LATITUDE));
						location.Logitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.MACHINE_LOCATION_LONGITUDE));
						location.Creation_Time_Stamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.MACHINE_LOCATION_CREATION_TIMESTAMP)));
						location.Version_Name = context.getPackageManager()
								.getPackageInfo(context.getPackageName(), 0).versionName;
						location.Version_Code = context.getPackageManager()
								.getPackageInfo(context.getPackageName(), 0).versionCode;
						locations.add(location);
					} while (dbCursor.moveToNext());
				}
				
			} else {
				Cursor dbCursor = dbFactory.database.query(false,
						Schema.TABLE_MACHINE_LOCATION, null, filterExpression,
						null, null, null,
						Schema.MACHINE_LOCATION_CREATION_TIMESTAMP, null);
				if (dbCursor.moveToFirst()) {
					do {
						MachineLocationViewModel location = new MachineLocationViewModel();
						location.Machine_Id = machineId;
						location.Latitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.MACHINE_LOCATION_LATITUDE));
						location.Logitude = dbCursor
								.getDouble(dbCursor
										.getColumnIndex(Schema.MACHINE_LOCATION_LONGITUDE));
						location.Creation_Time_Stamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.MACHINE_LOCATION_CREATION_TIMESTAMP)));
						location.Version_Name = context.getPackageManager()
								.getPackageInfo(context.getPackageName(), 0).versionName;
						location.Version_Code = context.getPackageManager()
								.getPackageInfo(context.getPackageName(), 0).versionCode;
						locations.add(location);
					} while (dbCursor.moveToNext());
				}
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return locations;
	}

	public static void deleteAll(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.MachineLocation);
		try {
			dbfactory.database
					.delete(Schema.TABLE_MACHINE_LOCATION, null, null);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
	}
}
