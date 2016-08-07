package com.zyumbik.makeanappointment.utils;

/** Created by glebsabirzanov on 06/08/16. */
/** Utility class for building working search */
public class SearchURLConfiguration {

	private static final String DEFAULT_PARAMS = "&name=восточный&type=bank&radius=50000&language=ru",
			BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?&key=AIzaSyBtA7fUuV3FBeNsoymahRIPQpKBe89NRSQ";

	// Search with required location data
	public static String getLocationUrlSearch(double lat, double lng) {
		return BASE + DEFAULT_PARAMS + "&location=" + lat + "," + lng;
	}

	// Construct your own search
	public static String getCustomUrlSearch(double lat, double lng, String... params) {
		StringBuilder builder = new StringBuilder(BASE);
		builder.append("&location=").append(lat).append(",").append(lng);
		for (String param : params) {
			builder.append(param);
		}
		return builder.toString();
	}

}
