/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.DbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelperWrapper extends SQLiteOpenHelper {

	private String table;
	private String query;

	public SQLiteHelperWrapper(Context context) {
		super(context, "", null, 0);
	}

	public SQLiteHelperWrapper(Context context, String database_name,
			String query, int version) {
		super(context, database_name, null, version);
		table = query;
	}

	public SQLiteHelperWrapper(Context context, String database_name,
			String table, String query, int version) {
		super(context, database_name, null, version);
		this.query = query;
		this.table = table;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelperWrapper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + table);
		onCreate(db);
	}
}
