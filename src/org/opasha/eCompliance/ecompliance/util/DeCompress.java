/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DeCompress {
	private static final int BUFFER = 2048;

	private String _directory;
	private String _zipFile;

	public DeCompress(String outDirectory, String zipFile) {
		_directory = outDirectory;
		_zipFile = zipFile;

	}

	public void unzip() {
		try {
			// create a buffer to improve copy performance later.
			byte[] buffer = new byte[BUFFER];

			InputStream theFile = new FileInputStream(_zipFile);
			ZipInputStream stream = new ZipInputStream(theFile);
			String outdir = _directory;

			try {
				ZipEntry entry;
				
				while ((entry = stream.getNextEntry()) != null) {
					String outpath = outdir + "//" + entry.getName();
					FileOutputStream output = null;
					try {
						output = new FileOutputStream(outpath);
						int len = 0;
						while ((len = stream.read(buffer)) > 0) {
							output.write(buffer, 0, len);
						}
					} finally {
						if (output != null)
							output.close();
					}
				}
			} finally {
				stream.close();
			}
		} catch (Exception e) {	
		}
	}

}
