package com.zyumbik.makeanappointment.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zyumbik.makeanappointment.R;
import com.zyumbik.makeanappointment.data_models.AppointmentNotificationData;

import java.util.ArrayList;
import java.util.Calendar;

/** Created by glebsabirzanov on 09/08/16. */
/** Called when notification has to be set */
public class BootBroadcastReceiver extends BroadcastReceiver {

	private AlarmManager alarms;
	private final Calendar currentTime = Calendar.getInstance();
	private Calendar appointmentTime;
	private AppointmentNotificationData data;
	private Context context;
	private int uniqueID = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		setAlarm(context);
	}

	public void setAlarm(Context context) {
		this.context = context;
		alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		appointmentTime = Calendar.getInstance();
		data = new AppointmentNotificationData(context);

		AlarmBroadcastReceiver receiver = new AlarmBroadcastReceiver();
		IntentFilter filter = new IntentFilter("ALARM_ACTION");
		context.registerReceiver(receiver, filter);

		for (int i = 0; i < data.length(); i++) {
			// Create two notifications: a day and an hour before the appointment
			createNotificationForTime(86400000L, i);
			createNotificationForTime(3600000L, i);
		}
	}

	private void createNotificationForTime(long notificationTimeBeforeEvent, int indexInData) {
		// Creates notification at the specified time before appointment
		appointmentTime.setTimeInMillis(data.getDate(uniqueID));
		if ((appointmentTime.getTimeInMillis() - currentTime.getTimeInMillis()) >= notificationTimeBeforeEvent) {

			// Generate notification message
			String notificationMessage = String.format(
					context.getString(R.string.notificationMessage) + data.getAddress(indexInData),
					appointmentTime.get(Calendar.HOUR_OF_DAY), appointmentTime.get(Calendar.MINUTE));

			Intent intent = new Intent("ALARM_ACTION");
			intent.putExtra("message", notificationMessage);
			intent.putExtra("time", data.getDate(indexInData));
			PendingIntent operation = PendingIntent.getBroadcast(context, uniqueID++, intent, 0);
			alarms.set(AlarmManager.RTC_WAKEUP, appointmentTime.getTimeInMillis() - notificationTimeBeforeEvent, operation);
		}
		if (appointmentTime.getTimeInMillis() < currentTime.getTimeInMillis()) {
			data.removeOldAppointments();
		}
	}

}
