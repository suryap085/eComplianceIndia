/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.util.HashMap;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.iritech.driver.UsbNotification;

public class UsbReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() == "android.hardware.usb.action.USB_DEVICE_DETACHED") {
			Logger.LogToDb(context, "eCompliance", "Usb Disconnected");
			UsbNotification mUsbNotification = UsbNotification
					.getInstance(context);
			mUsbNotification.cancelNofitications();
			mUsbNotification.createNotification("IriShield is Disconnected.");
		} else {
			Logger.LogToDb(context, "eCompliance", "Usb Connected");
			UsbNotification mUsbNotification = UsbNotification
					.getInstance(context);
			mUsbNotification.cancelNofitications();
			mUsbNotification.createNotification("IriShield is Connected.");

			UsbManager usbManager = (UsbManager) context
					.getSystemService(Context.USB_SERVICE);
			HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
			for (UsbDevice d : usbDevices.values()) {
				if (d.getVendorId() == 5265 && d.getProductId() == 136) {
					ConfigurationOperations.addConfiguration(
							ConfigurationKeys.key_used_device, "fingerprint",
							context);
					((eComplianceApp) context.getApplicationContext()).IsIris = false;
					return;
				} else {
					if (d.getVendorId() == 8035) {
						if (d.getProductId() >= 61441
								&& d.getProductId() <= 61443) {
							ConfigurationOperations.addConfiguration(
									ConfigurationKeys.key_used_device, "iris",
									context);
							((eComplianceApp) context.getApplicationContext()).IsIris = true;
							return;
						}
					}
				}
			}
		}

		// GetVendor(context);
	}

}
