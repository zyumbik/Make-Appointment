package com.zyumbik.makeanappointment.data_models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/** Created by glebsabirzanov on 05/08/16. */
/** This is a base model class for bank offices */
public class BankOffice {

	private String address, name, title;
	private LatLng latLng;
	private static final String[] toExcludeFromTitle = {", Хабаровск", ",", "ул. ", "улица "};

	public BankOffice(String address, String name, double lat, double lng) {
		this.address = address;
		this.name = name;
		latLng = new LatLng(lat, lng);
		makeTitle();
	}

	private void makeTitle() {
		title = address;
		for (String exclusion : toExcludeFromTitle) {
			if (title.contains(exclusion)) {
				title = title.replace(exclusion, "");
			}
		}
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

	public String getTitle() {
		return title;
	}

	public MarkerOptions getMarkerOptions() {
		return new MarkerOptions().position(latLng).title(title);
	}
}
