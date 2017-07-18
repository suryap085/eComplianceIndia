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

import org.opasha.eCompliance.ecompliance.R;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.GenUtils;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class RegimenMasterOperations extends Activity {

	public static long addRegimen(int ID, String category, String stage,
			int daysFrequency, String schedule, int scheduleDays, int priority,
			int numSelfAdmin, int selfAdminFreq, boolean selfAdminSunday,
			int numMissed, int missedFreq, boolean missedSunday,
			boolean isActive, int unsupervisedFreq, Context context) {
		hardDelete(ID, context);
		ContentValues regimenValues = new ContentValues();
		regimenValues.put(Schema.REGIMEN_MASTER_ID, ID);
		regimenValues.put(Schema.REGIMEN_MASTER_CATEGORY, category);
		regimenValues.put(Schema.REGIMEN_MASTER_STAGE, stage);
		regimenValues.put(Schema.REGIMEN_MASTER_DAYS_FREQUENCY, daysFrequency);
		regimenValues.put(Schema.REGIMEN_MASTER_SCHEDULE, schedule);
		regimenValues.put(Schema.REGIMEN_MASTER_DAYS, scheduleDays);
		regimenValues.put(Schema.REGIMEN_MASTER_PRIORITY, priority);
		regimenValues.put(Schema.REGIMEN_MASTER_SELF_ADMIN_NUM, numSelfAdmin);
		regimenValues.put(Schema.REGIMEN_MASTER_SELF_ADMIN_FREQ, selfAdminFreq);
		regimenValues.put(Schema.REGIMEN_MASTER_SELF_ADMIN_SUNDAY,
				selfAdminSunday);
		regimenValues.put(Schema.REGIMEN_MASTER_MISSED_NUM, numMissed);
		regimenValues.put(Schema.REGIMEN_MASTER_MISSED_FREQ, missedFreq);
		regimenValues.put(Schema.REGIMEN_MASTER_MISSED_SUNDAY, missedSunday);
		regimenValues.put(Schema.REGIMEN_MASTER_UNSUPERVISED_FREQUENCY, unsupervisedFreq);
		regimenValues.put(Schema.REGIMEN_MASTER_IS_ACTIVE, isActive);
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		try {
			return dbfactory.database.insert(Schema.TABLE_REGIMEN_MASTER, null,
					regimenValues);
		} catch (Exception e) {
			return -1;
		} finally {
			dbfactory.CloseDatabase();
		}
	}

	public static void hardDelete(int regimenId, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		try {
			dbFactory.database.delete(Schema.TABLE_REGIMEN_MASTER,
					Schema.REGIMEN_MASTER_ID + " = " + regimenId, null);
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
	}

	public static ArrayList<Master> getRegimen(String filterExpression,
			Context context) {
		ArrayList<Master> regimenList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REGIMEN_MASTER, null, null, null,
						Schema.REGIMEN_MASTER_ID, null, null, null); // if
				// selection
				// is
				// null
				// return
				// all
				// values
				// from
				// the
				// table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REGIMEN_MASTER, null, filterExpression,
						null, Schema.REGIMEN_MASTER_ID, null, null, null); // if
				// selection
				// is
				// not
				// null
				// then
				// return
				// selection
				// values
			}
			if (dbCursor.moveToFirst()) {
				do {
					Master regimen = new Master();
					regimen.setRegimen(
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_ID)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_CATEGORY)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_STAGE)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_DAYS_FREQUENCY)),
							dbCursor.getString(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_SCHEDULE)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_DAYS)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_PRIORITY)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_SELF_ADMIN_NUM)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_SELF_ADMIN_FREQ)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_SELF_ADMIN_SUNDAY))),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_MISSED_NUM)),
							dbCursor.getInt(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_MISSED_FREQ)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_MISSED_SUNDAY))),
							dbCursor.getInt(dbCursor
											.getColumnIndex(Schema.REGIMEN_MASTER_UNSUPERVISED_FREQUENCY)),
							Boolean.parseBoolean(dbCursor.getString(dbCursor
									.getColumnIndex(Schema.REGIMEN_MASTER_IS_ACTIVE))));
					regimenList.add(regimen);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return regimenList;
	}

	public static ArrayList<Master> getAllCategoryForSpinner(
			Boolean needPrompt, Context context) {

		ArrayList<Master> regimenList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Master regimen = new Master();
		if (needPrompt == true) {
			regimen.setCategory(context.getResources().getString(
					R.string.selectCategory));
			regimenList.add(regimen);
		}
		Cursor dbCursor=null;
		try {

			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_REGIMEN_MASTER,
					new String[] { Schema.REGIMEN_MASTER_CATEGORY },
					Schema.REGIMEN_MASTER_IS_ACTIVE + "=1", null, null, null,
					null, null); // if selection is null
									// return
									// all
									// values from the table
			if (dbCursor.moveToFirst()) {
				do {
					Master regimens = new Master();
					regimens.setCategory(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REGIMEN_MASTER_CATEGORY)));
					regimenList.add(regimens);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return regimenList;
	}

	public static ArrayList<Master> getAllStage(String filterExpression,
			Context context) {
		ArrayList<Master> regimenList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REGIMEN_MASTER,
						new String[] { Schema.REGIMEN_MASTER_STAGE }, null,
						null, null, null, Schema.REGIMEN_MASTER_ID, null); // if
				// selection
				// is
				// null
				// return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REGIMEN_MASTER,
						new String[] { Schema.REGIMEN_MASTER_STAGE },

						filterExpression + " and "
								+ Schema.REGIMEN_MASTER_IS_ACTIVE + "=1", null,
						null, null, Schema.REGIMEN_MASTER_ID + " ASC", null);
			}
			if (dbCursor.moveToFirst()) {
				do {
					Master regimen = new Master();
					regimen.setStage(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REGIMEN_MASTER_STAGE)));
					regimenList.add(regimen);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return regimenList;
	}

	public static ArrayList<Master> getAllSchedule(String filterExpression,
			Context context) {
		ArrayList<Master> regimenList = new ArrayList<Master>();
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {
			if (filterExpression.length() == 0) {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REGIMEN_MASTER,
						new String[] { Schema.REGIMEN_MASTER_SCHEDULE }, null,

						null, null, null, Schema.REGIMEN_MASTER_ID, null); // if
				// selection
				// is
				// null
				// return
				// all
				// values from the table
			} else {
				dbCursor = dbfactory.database.query(true,
						Schema.TABLE_REGIMEN_MASTER,
						new String[] { Schema.REGIMEN_MASTER_SCHEDULE },

						filterExpression + " and "
								+ Schema.REGIMEN_MASTER_IS_ACTIVE + " =1",
						null, null, null, Schema.REGIMEN_MASTER_ID, null);
			}
			if (dbCursor.moveToFirst()) {
				do {
					Master regimen = new Master();
					regimen.setSchedule(dbCursor.getString(dbCursor
							.getColumnIndex(Schema.REGIMEN_MASTER_SCHEDULE)));
					regimenList.add(regimen);
				} while (dbCursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
		return regimenList;
	}

	public static String getInitialStageByCategory(String category,
			Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_REGIMEN_MASTER,
					new String[] { Schema.REGIMEN_MASTER_STAGE },

					Schema.REGIMEN_MASTER_CATEGORY + "='" + category + "' and "
							+ Schema.REGIMEN_MASTER_IS_ACTIVE + "=1", null,
					null, null, null);
			dbCursor.moveToFirst();
			return dbCursor.getString(dbCursor
					.getColumnIndex(Schema.REGIMEN_MASTER_STAGE));

		} catch (Exception e) {
			return "";
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
	}

	public static int getRegimenId(String query, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(Schema.TABLE_REGIMEN_MASTER,
					new String[] { Schema.REGIMEN_MASTER_ID }, query, null,
					null, null, null);
			dbCursor.moveToFirst();
			return dbCursor.getInt(dbCursor
					.getColumnIndex(Schema.REGIMEN_MASTER_ID));

		} catch (Exception e) {
			return 1;
		} finally {
			dbfactory.CloseDatabase();
			dbCursor.close();
		}
	}

	public static String getRegimenIdForDay(long date, Context context) {
		StringBuilder query = new StringBuilder();
		int weekDay = GenUtils.dateToDay(date);

		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {

			dbCursor = dbfactory.database.query(true,

			Schema.TABLE_REGIMEN_MASTER, null, null, null, null, null, null,
					null);

			if (dbCursor.moveToFirst()) {
				do {

					if (GenUtils
							.isScheduledOn(
									weekDay,
									dbCursor.getInt(dbCursor
											.getColumnIndex(Schema.REGIMEN_MASTER_DAYS)))) {
						query.append(Schema.TREATMENT_IN_STAGES_REGIMEN_ID
								+ " = "
								+ dbCursor.getInt(dbCursor
										.getColumnIndex(Schema.REGIMEN_MASTER_ID))
								+ " or ");
					}
				} while (dbCursor.moveToNext());
			}
			if (query.length() > 3) {
				query.setLength(query.length() - 3);
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();

		return query.toString();
	}

	public static Master getRegimen(int regimenId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Master regimen = new Master();
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_REGIMEN_MASTER, null, Schema.REGIMEN_MASTER_ID
							+ "=" + regimenId, null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				regimen.ID = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_ID));
				regimen.regimenId = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_ID));
				regimen.catagory = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_CATEGORY));
				regimen.stage = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_STAGE));
				regimen.schedule = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_SCHEDULE));
				regimen.daysFrequency = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_DAYS_FREQUENCY));
				regimen.scheduleDays = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_DAYS));
				regimen.numMissed = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_MISSED_NUM));
				regimen.missedFreq = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_MISSED_FREQ));
				regimen.missedSunday = Boolean
						.parseBoolean(dbCursor.getString(dbCursor
								.getColumnIndex(Schema.REGIMEN_MASTER_MISSED_SUNDAY)));
				regimen.numSelfAdmin = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_SELF_ADMIN_NUM));
				regimen.selfAdminFreq = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_SELF_ADMIN_FREQ));
				regimen.unsupervisedFreq = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_UNSUPERVISED_FREQUENCY));
				regimen.selfAdminSunday = Boolean
						.parseBoolean(dbCursor.getString(dbCursor
								.getColumnIndex(Schema.REGIMEN_MASTER_SELF_ADMIN_SUNDAY)));
			}
		} catch (Exception e) {

		}

		dbfactory.CloseDatabase();
dbCursor.close();
		return regimen;
	}

	public static String getSatge(int regimenId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		String regimen = "";
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_REGIMEN_MASTER, null,

					Schema.REGIMEN_MASTER_ID + "=" + regimenId, null, null,
					null, null, null);
			if (dbCursor.moveToFirst()) {
				regimen = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_STAGE));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return regimen;
	}

	public static String getRegimenIds(String query, Context context) {
		StringBuilder retVal = new StringBuilder();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		Cursor dbCursor=null;
		try {
			 dbCursor = dbFactory.database.query(
					Schema.TABLE_REGIMEN_MASTER, null, query, null, null, null,
					null, null);
			while (dbCursor.moveToNext()) {
				retVal.append(dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_ID)) + ",");

			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		dbCursor.close();

		if (retVal.length() > 1) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}

	public static int getfrequency(int regimenId, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		int regimen = 1;
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_REGIMEN_MASTER, null, Schema.REGIMEN_MASTER_ID

					+ "=" + regimenId, null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				regimen = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_DAYS_FREQUENCY));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return regimen;
	}

	public static void emptyTable(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		try {
			dbfactory.database.delete(Schema.TABLE_REGIMEN_MASTER, null, null);
		} catch (Exception e) {

		}
		dbfactory.CloseDatabase();

	}

	public static String getCatFirst(Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		String regimen = "";
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_REGIMEN_MASTER, null,
					Schema.REGIMEN_MASTER_IS_ACTIVE + "=1", null, null, null,
					Schema.REGIMEN_MASTER_ID, null);
			if (dbCursor.moveToFirst()) {
				regimen = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_CATEGORY));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return regimen;
	}

	public static String getStageFirst(String cat, Context context) {
		DbFactory dbfactory = new DbFactory(context)
				.CreateDatabase(TableEnum.RegimenMaster);
		String regimen = "";
		Cursor dbCursor=null;
		try {
			dbCursor = dbfactory.database.query(true,
					Schema.TABLE_REGIMEN_MASTER, null,
					Schema.REGIMEN_MASTER_IS_ACTIVE + "=1 and "
							+ Schema.REGIMEN_MASTER_CATEGORY + " = '" + cat
							+ "'", null, null, null, Schema.REGIMEN_MASTER_ID,
					null);
			if (dbCursor.moveToFirst()) {
				regimen = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.REGIMEN_MASTER_STAGE));
			}
		} catch (Exception e) {
		}
		dbfactory.CloseDatabase();
		dbCursor.close();
		return regimen;
	}

}
