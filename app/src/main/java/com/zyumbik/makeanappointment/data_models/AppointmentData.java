package com.zyumbik.makeanappointment.data_models;


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
}
