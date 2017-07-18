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

import org.opasha.eCompliance.ecompliance.eComplianceApp;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory;
import org.opasha.eCompliance.ecompliance.DbSchema.DbFactory.TableEnum;
import org.opasha.eCompliance.ecompliance.DbSchema.Schema;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.Positive_Contacts;
import org.opasha.eCompliance.ecompliance.modal.wcf.Contacts.SavePositiveContactModel;
import org.opasha.eCompliance.ecompliance.util.GenUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PositiveContactsOperations {
	public static void add(String id, String name, String address, int age,
			String gender, String phone, String refId, String refPhone,
			String treatmentId, long creationtimeStamp, boolean isDeleted,
			String createdBy, String centerId, String refName, Context context) {
		if (!shouldAdd(id, context))
			return;
		softdelete(id, context);
		ContentValues val = new ContentValues();
		val.put(Schema.POSITIVE_CONTACT_ID, id);
		val.put(Schema.POSITIVE_CONTACT_NAME, name);
		val.put(Schema.POSITIVE_CONTACT_ADDRESS, address);
		val.put(Schema.POSITIVE_CONTACT_AGE, age);
		val.put(Schema.POSITIVE_CONTACT_GENDER, gender);
		val.put(Schema.POSITIVE_CONTACT_PHONE, phone);
		val.put(Schema.POSITIVE_CONTACT_REF_ID, refId);
		val.put(Schema.POSITIVE_CONTACT_REF_NAME, refName);
		val.put(Schema.POSITIVE_CONTACT_CENTER_ID, centerId);
		val.put(Schema.POSITIVE_CONTACT_REF_PHONE, refPhone);
		val.put(Schema.POSITIVE_CONTACT_TREATMENT_ID, treatmentId);
		val.put(Schema.POSITIVE_CONTACT_CREATED_BY, createdBy);
		val.put(Schema.POSTIVE_CONTACT_CREATION_TIME, creationtimeStamp);
		val.put(Schema.POSITIVE_CONTACT_IS_DELETED, isDeleted);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Positive_Contacts);
		try {
			dbFactory.database.insert(Schema.TABLE_POSTIVE_CONTACT, null, val);

		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();

	}

	private static boolean shouldAdd(String id, Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Positive_Contacts);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_POSTIVE_CONTACT, null,
					Schema.POSITIVE_CONTACT_ID + "='" + id + "' and "
							+ Schema.POSITIVE_CONTACT_IS_DELETED + "=0", null,
					null, null, null, null);
			if (dbCursor.moveToFirst()) {
				if (!dbCursor
						.getString(
								dbCursor.getColumnIndex(Schema.POSITIVE_CONTACT_TREATMENT_ID))
						.isEmpty()) {
					dbFactory.CloseDatabase();
					return false;
				}
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return true;
	}

	private static void softdelete(String id, Context context) {
		ContentValues val = new ContentValues();
		val.put(Schema.POSITIVE_CONTACT_IS_DELETED, true);
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Positive_Contacts);
		try {
			dbFactory.database.update(Schema.TABLE_POSTIVE_CONTACT, val,
					Schema.POSITIVE_CONTACT_ID + "='" + id + "'", null);
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}

	public static SavePositiveContactModel dataSync(String query,
			Context context) {
		SavePositiveContactModel retVal = new SavePositiveContactModel();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Positive_Contacts);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_POSTIVE_CONTACT, null, query, null, null,
					null, null);
			while (dbCursor.moveToNext()) {
				Positive_Contacts ret = new Positive_Contacts();
				ret.Address = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_ADDRESS));
				ret.Age = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_AGE));
				ret.Creation_Timestamp = GenUtils
						.longToDate(dbCursor.getLong(dbCursor
								.getColumnIndex(Schema.POSTIVE_CONTACT_CREATION_TIME)));
				ret.Gender = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_GENDER));
				ret.Center_Id = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_CENTER_ID));
				ret.Contact_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_ID));
				ret.Is_Deleted = GenUtils.getBoolean(dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_IS_DELETED)));
				ret.Name = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_NAME));
				ret.Phone = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_PHONE));
				ret.Ref_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_ID));
				ret.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_CREATED_BY));
				ret.Ref_Phone = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_PHONE));
				ret.Treatment_id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_TREATMENT_ID));
				retVal.PosContacts.add(ret);
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;
	}

	public static ArrayList<Positive_Contacts> getPendingContactList(
			Context context) {
		ArrayList<Positive_Contacts> retVal = new ArrayList<Positive_Contacts>();
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Positive_Contacts);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_POSTIVE_CONTACT, null,
					Schema.POSITIVE_CONTACT_IS_DELETED + " =0 and ("
							+ Schema.POSITIVE_CONTACT_TREATMENT_ID + "= '' or "
							+ Schema.POSITIVE_CONTACT_TREATMENT_ID
							+ " is null )", null, null, null, null, null);
			while (dbCursor.moveToNext()) {
				Positive_Contacts ret = new Positive_Contacts();
				ret.Address = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_ADDRESS));
				ret.Age = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_AGE));
				ret.Gender = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_GENDER));
				ret.Contact_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_ID));
				ret.Name = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_NAME));
				ret.Phone = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_PHONE));
				ret.Ref_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_ID));
				ret.Ref_Name = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_NAME));
				ret.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_CREATED_BY));
				ret.Ref_Phone = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_PHONE));
				ret.Treatment_id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_TREATMENT_ID));
				retVal.add(ret);
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
		return retVal;
	}

	public static void updateTreatmentId(String treatmentId, String ContactId,
			Context context) {
		DbFactory dbFactory = new DbFactory(context)
				.CreateDatabase(TableEnum.Positive_Contacts);
		try {
			Cursor dbCursor = dbFactory.database.query(
					Schema.TABLE_POSTIVE_CONTACT, null,
					Schema.POSITIVE_CONTACT_ID + " ='" + ContactId + "' and "
							+ Schema.POSITIVE_CONTACT_IS_DELETED + " = 0",
					null, null, null, null, null);
			if (dbCursor.moveToFirst()) {
				Positive_Contacts ret = new Positive_Contacts();
				ret.Address = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_ADDRESS));
				ret.Age = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_AGE));
				ret.Gender = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_GENDER));
				ret.Contact_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_ID));
				ret.Name = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_NAME));
				ret.Phone = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_PHONE));
				ret.Center_Id = dbCursor.getInt(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_CENTER_ID));
				ret.Ref_Id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_ID));
				ret.Ref_Name = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_NAME));
				ret.Created_By = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_CREATED_BY));
				ret.Ref_Phone = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_REF_PHONE));
				ret.Treatment_id = dbCursor.getString(dbCursor
						.getColumnIndex(Schema.POSITIVE_CONTACT_TREATMENT_ID));
				dbFactory.CloseDatabase();
				add(ret.Contact_Id,
						ret.Name,
						ret.Address,
						ret.Age,
						ret.Gender,
						ret.Phone,
						ret.Ref_Id,
						ret.Ref_Phone,
						treatmentId,
						System.currentTimeMillis(),
						false,
						((eComplianceApp) context.getApplicationContext()).LastLoginId,
						String.valueOf(ret.Center_Id), ret.Ref_Name, context);
				return;
			}
		} catch (Exception e) {
		}
		dbFactory.CloseDatabase();
	}
}
