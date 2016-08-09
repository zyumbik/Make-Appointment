package com.zyumbik.makeanappointment.data_models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/** Created by glebsabirzanov on 07/08/16. */
/** Class containing information about the appointment */
public class AppointmentData {

	private int hour, minute, day, month, year;
	private BankOffice office;

	public void setDate(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public void setTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	@Override
	public String toString() {
		return  getTime() + "\n" +
				getDate() + "\n" +
				office;
	}

	public long getTimeInMillis() {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute, 0);
		return cal.getTimeInMillis();
	}
	
	public String getDateTime() {
		return getDate() + " " + getTime();
	}

	public String getDate() {
		return formatNumber(day) + "." + formatNumber(month) + "." + year;
	}

	public String getTime() {
		return formatNumber(hour) + ":" + formatNumber(minute);
	}

	public String formatNumber(int number) {
		return String.format("%1$02d", number);
	}

	public BankOffice getOffice() {
		return office;
	}

	public void setOffice(BankOffice office) {
		this.office = office;
	}

	public JSONObject getAppointmentJSON() throws JSONException {
		JSONObject object = new JSONObject();
		object.put("date", new JSONObject()
				.put("day", day)
				.put("month", month)
				.put("year", year));
		object.put("time", new JSONObject().put("hour", hour).put("minute", minute));
		object.put("address", office.getAddress());
		object.put("latlng", new JSONObject()
				.put("latitude", office.getLatLng().latitude)
				.put("longitude", office.getLatLng().longitude));
		return object;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}
}
