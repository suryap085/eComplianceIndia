/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class CustomExceptionHandler implements UncaughtExceptionHandler {
	public Context context;

	public CustomExceptionHandler(Context c) {
		context = c;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();

		Logger.wtf(context, "Uncaught Exception", stacktrace);
		System.exit(1);
	}
}
