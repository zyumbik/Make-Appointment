package com.zyumbik.makeanappointment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/** Created by glebsabirzanov on 05/08/16. */
/** This is a base model class for bank offices */
public class BankOffice {

	private String address, name;
	private LatLng latLng;

	public BankOffice(String address, String name, double lat, double lng) {
		this.address = address;
		this.name = name;
		latLng = new LatLng(lat, lng);
	}

	public String getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public MarkerOptions getMarkerOptions() {
		return new MarkerOptions().position(latLng).title(name);
	}
}
