package com.zyumbik.makeanappointment.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Calendar;

/** Created by glebsabirzanov on 09/08/16. */

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		setAlarm(context);
	}

	public void setAlarm(Context context) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(NotificationDataPreferences.loadDateInMillis(context, "AppointmentTime", Calendar.getInstance().getTimeInMillis()));
		Calendar currentTime = Calendar.getInstance();
		System.out.println("Time of appointment: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
		if ((cal.getTimeInMillis() - currentTime.getTimeInMillis()) >= 0) {
			System.out.println("Setting the alarm");
			AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			AlarmBroadcastReceiver receiver = new AlarmBroadcastReceiver();
			IntentFilter filter = new IntentFilter("ALARM_ACTION");
			context.registerReceiver(receiver, filter);

			String notificationMessage = "At " + cal.get(Calendar.HOUR_OF_DAY) + ':' + cal.get(Calendar.MINUTE);

			Intent intent = new Intent("ALARM_ACTION");
			intent.putExtra("message", notificationMessage);
			PendingIntent operation = PendingIntent.getBroadcast(context, 0, intent, 0);
			alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 3600 * 1000, operation);

		}
	}

}
