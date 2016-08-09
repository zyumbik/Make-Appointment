package com.zyumbik.makeanappointment.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/** Created by glebsabirzanov on 09/08/16. */
/** Class for Android shared preferences management */
public class NotificationDataPreferences {

	public static final String ARRAY_ADDRESSES_NAME = "addresses_array",
			ARRAY_DATES_NAME = "dates_array",
			ARRAY_COORDINATES_NAME = "coordinates_array";

	public static void saveArray(Context context, ArrayList<String> array, String arrayName) {
		// Used for saving addresses and office coordinates in the future
		SharedPreferences sharedPreferences = context.getSharedPreferences("MakeAnAppointment", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(arrayName + "_length", array.size());
		for (int i = 0; i < array.size(); i++) {
			editor.putString(arrayName + "_" + i, array.get(i));
			System.out.println("String put: " + array.get(i));
		}
		editor.apply();
	}

	public static String[] loadArray(Context context, String arrayName, String[] defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MakeAnAppointment", Context.MODE_PRIVATE);
		String[] array = new String[sharedPreferences.getInt(arrayName + "_length", 0)];
		for (int i = 0; i < array.length; i++) {
			array[i] = sharedPreferences.getString(arrayName + "_" + i, null);
		}
		return array;
	}

	public static void saveArray(Context context, String arrayName, ArrayList<Long> array) {
		// Used for saving dates of appointments
		SharedPreferences sharedPreferences = context.getSharedPreferences("MakeAnAppointment", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(arrayName + "_length", array.size());
		for (int i = 0; i < array.size(); i++) {
			System.out.println("Long put: " + array.get(i));
			editor.putLong(arrayName + "_" + i, array.get(i));
		}
		editor.apply();
	}

	public static Long[] loadArray(Context context, String arrayName, Long[] defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MakeAnAppointment", Context.MODE_PRIVATE);
		Long[] array = new Long[sharedPreferences.getInt(arrayName + "_length", 0)];
		for (int i = 0; i < array.length; i++) {
			array[i] = sharedPreferences.getLong(arrayName + "_" + i, 0);
		}
		return array;
	}


}
