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
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.Model.NetworkOperator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class NetworkOperatorOperation {
	public static void addOperator(String name, String number,
			Boolean isActive, Context context) {
		deleteName(name, context);
		if (isActive) {
			ContentValues val = new ContentValues();
			val.put(Schema.NETWORK_OPERATOR_NAME, name);
			val.put(Schema.NETWORK_OPERATOR_NUMBER, number);
			DbFactory dbFactory = new DbFactory(context)
					.CreateDatabase(TableEnum.NetWorkOperator);
			try {
				dbFactory.database.insert(Schema.TABLE_NETWORK_OPERATOR, null,
						val);
			} catch (Exception e) {
			}
			dbFactory.CloseDatabase();
		}
	}

	private static void deleteName(String name, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.NetWorkOperator);
		try {
			dbFactory.database.delete(Schema.TABLE_NETWORK_OPERATOR,
					Schema.NETWORK_OPERATOR_NAME + "= '" + name + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();

	}

	public static ArrayList<NetworkOperator> getOperators(Context context) {
		ArrayList<NetworkOperator> retList = new ArrayList<NetworkOperator>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.NetWorkOperator);

		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_NETWORK_OPERATOR, null, null, null, null,
					null, null);
			while (dbCursor.moveToNext()) {
				NetworkOperator obj = new NetworkOperator();
				obj.Name = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.NETWORK_OPERATOR_NAME));
				obj.Number = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.NETWORK_OPERATOR_NUMBER));
				retList.add(obj);
			}
		} catch (Exception e) {

		}
		dbFactory.CloseDatabase();
		if (retList.size() == 0) {
			NetworkOperator obj = new NetworkOperator();
			obj.Name = "Airtel";
			obj.Number = "*123";
			retList.add(obj);
			obj.Name = "Vodafone";
			obj.Number = "*111";
			retList.add(obj);
			obj.Name = "Vodafone";
			obj.Number = "*111";
		}
		return retList;
	}

}
