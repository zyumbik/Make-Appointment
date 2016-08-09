package com.zyumbik.makeanappointment.utils;

import android.content.Context;
import android.content.SharedPreferences;

/** Created by glebsabirzanov on 09/08/16. */

public class NotificationDataPreferences {

	public static void saveDateInMillis(Context context, String name, long value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MakeAnAppointment", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(name, value);
		editor.apply();
	}

	public static long loadDateInMillis(Context context, String name, long defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MakeAnAppointment", Context.MODE_PRIVATE);
		return sharedPreferences.getLong(name, defaultValue);
	}

}
