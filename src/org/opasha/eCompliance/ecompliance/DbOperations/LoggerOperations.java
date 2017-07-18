/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.DbOperations;

import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.modal.wcf.ClientLog;
import org.opasha.eCompliance.ecompliance.modal.wcf.ClientLoggerViewModel;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class LoggerOperations {
	public static long add(Context context, String tag, String message) {
		ContentValues logValues = new ContentValues();
		logValues.put(Schema.LOGGER_TAG, tag);
		logValues.put(Schema.LOGGER_MESSAGE, message);
		logValues.put(Schema.LOGGER_TIMESTAMP, System.currentTimeMillis());

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Logger);
		long returnVal = -1;
		try {
			returnVal = dbfactory.database.insert(Schema.TABLE_LOGGER, null,
					logValues);
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		return returnVal;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Logger);
		try {
			dbfactory.database.delete(Schema.TABLE_LOGGER, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static ClientLoggerViewModel get(String filterExpression,
			Context context) {
		String machineId = ConfigurationOperations.getKeyValue(
				ConfigurationKeys.key_Machine_Id, context);
		ClientLoggerViewModel logs = new ClientLoggerViewModel();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Logger);
		try {
			if (filterExpression.trim().equals("")) {
				dbFactory.database.query(false, Schema.TABLE_LOGGER, null,
						null, null, null, null, Schema.LOGGER_TIMESTAMP, null);
			} else {
				Cursor dbCursor = dbFactory.database.query(false,
						Schema.TABLE_LOGGER, null, filterExpression, null,
						null, null, Schema.LOGGER_TIMESTAMP, null);
				if (dbCursor.moveToFirst()) {
					do {
						ClientLog clog = new ClientLog();
						clog.MachineId = Integer.parseInt(machineId);
						clog.CreationTimeStamp = GenUtils
								.longToDate(dbCursor.getLong(dbCursor
										.getColumnIndex(Schema.LOGGER_TIMESTAMP)));
						clog.Description = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.LOGGER_MESSAGE));
						clog.Tag = dbCursor.getString(dbCursor
								.getColumnIndex(Schema.LOGGER_TAG));
						logs.ClientLogs.add(clog);
					} while (dbCursor.moveToNext());
				}
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return logs;
	}

}
