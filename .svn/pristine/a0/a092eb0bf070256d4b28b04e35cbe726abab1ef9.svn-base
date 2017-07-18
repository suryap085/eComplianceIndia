/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import org.opasha.eCompliance.ecompliance.DbOperations.LoggerOperations;

import android.content.Context;
import android.util.Log;

public class Logger {
	public static void e(Context context, String tag, String message) {
		try {
			Log.e(context.getClass().getName() + "." + tag, message);
			LoggerOperations.add(context, context.getClass().getName() + "."
					+ tag, message);
		} catch (Exception e) {
			Log.e(tag, message);
		}
	}

	public static void i(Context context, String tag, String message) {
		try {
			Log.i(context.getClass().getName() + "." + tag, message);
		} catch (Exception e) {
			Log.i(tag, message);
		}
	}

	public static void d(Context context, String tag, String message) {
		try {
			Log.d(context.getClass().getName() + "." + tag, message);
		} catch (Exception e) {
			Log.d(tag, message);
		}
	}

	public static void v(Context context, String tag, String message) {
		try {
			Log.v(context.getClass().getName() + "." + tag, message);
		} catch (Exception e) {
			Log.v(tag, message);
		}
	}

	public static void w(Context context, String tag, String message) {
		try {
			Log.w(context.getClass().getName() + "." + tag, message);
		} catch (Exception e) {
			Log.w(tag, message);
		}
	}

	public static void wtf(Context context, String tag, String message) {
		try {
			Log.wtf(context.getClass().getName() + "." + tag, message);
			LoggerOperations.add(context, context.getClass().getName() + "."
					+ tag, message);
		} catch (Exception e) {
			Log.wtf(tag, message);
		}
	}

	public static void LogToDb(Context context, String tag, String message) {
		try {
			LoggerOperations.add(context, context.getClass().getName() + "."
					+ tag, message);
		} catch (Exception e) {
		}
	}
}