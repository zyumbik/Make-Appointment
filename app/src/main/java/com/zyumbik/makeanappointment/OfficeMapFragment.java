package com.zyumbik.makeanappointment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.zyumbik.makeanappointment.custom_views.CustomMapView;
import com.zyumbik.makeanappointment.data_models.BankOffice;
import com.zyumbik.makeanappointment.utils.SearchURLConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class OfficeMapFragment extends Fragment implements
		OnMapReadyCallback,
		GoogleMap.OnMarkerClickListener,
		GoogleMap.OnInfoWindowClickListener,
		CustomMapView.OnMapTouchListener {

	// Bundle params names
	private static final String LAT = "LATITUDE", LNG = "LONGITUDE", PERMISSION_DENIED = "PERMISSION";
	// Default place - Khabarovsk
	private static final double KHABAROVSK_LAT = 48.4684, KHABAROVSK_LNG = 135.0813;
	private static final float KHABAROVSK_ZOOM = 10.5f;

	private boolean permissionDenied = false;
	private static double lastLatitude, lastLongitude;

	private static GoogleMap map;
	private static Map<String, BankOffice> offices;

	private CustomMapView mapView;
	private OnFragmentInteractionListener interactionListener;

	public static OfficeMapFragment newInstance(Location lastLocation, boolean permissionDenied) {
		OfficeMapFragment fragment = new OfficeMapFragment();
		Bundle args = new Bundle();

		Bundle bundle = new Bundle();
		if (lastLocation != null) {
			bundle.putDouble(LAT, lastLocation.getLatitude());
			bundle.putDouble(LNG, lastLocation.getLongitude());
		}
		bundle.putBoolean(PERMISSION_DENIED, permissionDenied);

		fragment.setArguments(args);
		return fragment;
	}

	public OfficeMapFragment() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map, container, false);
		if (getArguments() != null) {
			lastLatitude = getArguments().getDouble(LAT);
			lastLongitude = getArguments().getDouble(LNG);
			permissionDenied = getArguments().getBoolean(PERMISSION_DENIED);
		}
		mapView = (CustomMapView) v.findViewById(R.id.map_view);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mapView.setOnMapTouchListener(this);
		mapView.getMapAsync(this);
		return v;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			interactionListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		interactionListener = null;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		enableMyLocation();

		new GetOfficeData().execute();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLngForCamera(), KHABAROVSK_ZOOM));
		map.setOnMarkerClickListener(this);
		map.setOnInfoWindowClickListener(this);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (interactionListener != null) {
			interactionListener.onInfoWindowClick(offices.get(marker.getTitle()));
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (interactionListener != null) {
			interactionListener.onMarkerClicked(offices.get(marker.getTitle()));
		}
		return false;
	}

	private LatLng getLatLngForCamera() {
		if (lastLongitude != 0 && lastLatitude != 0){
			return new LatLng(lastLatitude, lastLongitude);
		} else {
			return new LatLng(KHABAROVSK_LAT, KHABAROVSK_LNG);
		}
	}

	private void getOfficesData() throws IOException, JSONException {
		URL url;
		if (lastLatitude != 0 && lastLongitude != 0) {
			url = new URL(SearchURLConfiguration.getLocationUrlSearch(lastLatitude, lastLongitude));
		} else {
			url = new URL(SearchURLConfiguration.getLocationUrlSearch(KHABAROVSK_LAT, KHABAROVSK_LNG));
		}

		HttpURLConnection urlConnection;
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setReadTimeout(10000);
		urlConnection.setConnectTimeout(15000);
		urlConnection.setDoOutput(true);
		urlConnection.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder sb = new StringBuilder();
		String line, data;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		data = sb.toString();
		System.out.println("JSON: " + data);
		fillOfficesList(data);

	}

	private void fillOfficesList(String data) throws JSONException {
		if (!data.equalsIgnoreCase("")) {

			JSONObject jObject = new JSONObject(data);
			JSONArray jArray = jObject.getJSONArray("results");
			offices = new HashMap<>();

			if (jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject geo = jArray.getJSONObject(i);

					if (!geo.has("permanently_closed")) {
						JSONObject jLocation = geo.getJSONObject("geometry");
						JSONObject jGetLocation = jLocation
								.getJSONObject("location");

						BankOffice office = new BankOffice(geo.getString("vicinity"), geo.getString("name"),
								jGetLocation.getDouble("lat"), jGetLocation.getDouble("lng"));
						offices.put(office.getTitle(), office);
					}
				}
			}
		}
	}

	private void setOfficeMarkers() {
		if (map != null && offices != null) {
			for (Map.Entry<String, BankOffice> office : offices.entrySet()) {
				map.addMarker(office.getValue().getMarkerOptions());
			}
		}
		interactionListener.onMarkersLoaded();
	}

	private void enableMyLocation() {
		if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			permissionDenied = true;
			return;
		}
		map.setMyLocationEnabled(true);
	}

	@Override
	public void onTouch() {
		interactionListener.onMapTouch();
	}

	private class GetOfficeData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (offices == null) {
				try {
					getOfficesData();
				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			setOfficeMarkers();
		}

	}

	public interface OnFragmentInteractionListener {
		void onMarkerClicked(BankOffice office);
		void onInfoWindowClick(BankOffice office);
		void onMarkersLoaded();
		void onMapTouch();
	}

}
