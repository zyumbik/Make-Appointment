package com.zyumbik.makeanappointment.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.zyumbik.makeanappointment.R;

/** Created by glebsabirzanov on 09/08/16. */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		createNotification(context, intent.getStringExtra("message"));
	}

	public void createNotification(Context context, String message) {
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.ic_media_play)
						.setContentTitle("Don't forget about the appointment")
						.setContentText(message);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());

	}

}
