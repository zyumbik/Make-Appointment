package com.zyumbik.makeanappointment.data_models;

import android.content.Context;

import com.zyumbik.makeanappointment.utils.NotificationDataPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/** Created by glebsabirzanov on 09/08/16. */
/** Stores data about the appointment to show the notification */
public class AppointmentNotificationData {

	private ArrayList<String> addresses;
	private ArrayList<Long> dates;

	public AppointmentNotificationData() {
		addresses = new ArrayList<>();
		dates = new ArrayList<>();
	}

	public AppointmentNotificationData(String[] addresses, Long[] dates) {
		this();
		if (addresses.length != dates.length) {
			return;
		}
		this.addresses = (ArrayList<String>) Arrays.asList(addresses);
		this.dates = (ArrayList<Long>) Arrays.asList(dates);
	}

	public AppointmentNotificationData(Context context) {
		addresses = (ArrayList<String>) Arrays.asList(NotificationDataPreferences
				.loadArray(context, NotificationDataPreferences.ARRAY_ADDRESSES_NAME, new String[0]));
		dates = (ArrayList<Long>) Arrays.asList(NotificationDataPreferences
				.loadArray(context, NotificationDataPreferences.ARRAY_DATES_NAME, new Long[0]));
	}

	public void sendDataToNotificationPreferences(Context context) {
			NotificationDataPreferences.saveArray(context,
					NotificationDataPreferences.ARRAY_ADDRESSES_NAME, ((String[]) addresses.toArray()));
			NotificationDataPreferences.saveArray(context,
					NotificationDataPreferences.ARRAY_DATES_NAME, ((Long[]) dates.toArray()));
	}

	public void addAppointment(AppointmentData appointment) {
		dates.add(appointment.getTimeInMillis());
		addresses.add(appointment.getOffice().toString());
		removeOldAppointments();
	}

	public void addAppointment(String address, long date) {
		addresses.add(address);
		dates.add(date);
	}

	public void removeAppointment(long appointmentTime) {
		if (appointmentTime != 0) {
			addresses.remove(dates.indexOf(appointmentTime));
			dates.remove(appointmentTime);
		}
	}

	public int length() {
		if (dates.size() == addresses.size()) {
			return dates.size();
		}
		return 0;
	}

	public String getAddress(int i) {
		return addresses.get(i);
	}

	public Long getDate(int i) {
		return dates.get(i);
	}

	public void removeOldAppointments() {
		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis();
		for (Long date : dates) {
			if (date < currentTime) {
				removeAppointment(date);
			}
		}
	}

}
