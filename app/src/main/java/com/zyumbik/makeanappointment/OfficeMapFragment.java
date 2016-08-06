package com.zyumbik.makeanappointment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OfficeMapFragment extends Fragment implements OnMapReadyCallback {

	// Bundle params names
	private static final String LAT = "LATITUDE", LNG = "LONGITUDE", PERMISSION_DENIED = "PERMISSION";
	// Default place - Khabarovsk
	private static final double KHABAROVSK_LAT = 48.4684, KHABAROVSK_LNG = 135.0813;
	private static final float KHABAROVSK_ZOOM = 10.5f;

	private boolean permissionDenied = false;
	private static double lastLatitude, lastLongitude;

	private static GoogleMap map;
	private static ArrayList<BankOffice> offices;
	private static final String URL_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?name=восточный&type=bank&radius=50000&language=ru&key=AIzaSyBtA7fUuV3FBeNsoymahRIPQpKBe89NRSQ";

	private OnFragmentInteractionListener mListener;

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
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		return v;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		enableMyLocation();
		new GetOfficeData().execute();

		LatLng latLng;
		if (lastLongitude != 0 && lastLatitude != 0){
			latLng = new LatLng(lastLatitude, lastLongitude);
		} else {
			latLng = new LatLng(KHABAROVSK_LAT, KHABAROVSK_LNG);
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, KHABAROVSK_ZOOM));
	}

	private void getOfficesData() throws IOException, JSONException {
		URL url;
		if (lastLatitude != 0 && lastLongitude != 0) {
			url = new URL(URL_SEARCH + "&location=" + lastLatitude + "," + lastLongitude);
		} else {
			url = new URL(URL_SEARCH + "&location=" + KHABAROVSK_LAT + "," + KHABAROVSK_LNG);
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
			offices = new ArrayList<>();

			if (jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject geo = jArray.getJSONObject(i);

					if (!geo.has("permanently_closed")) {
						JSONObject jLocation = geo.getJSONObject("geometry");
						JSONObject jgetLocation = jLocation
								.getJSONObject("location");

						String address = geo.getString("vicinity");
						String name = geo.getString("name");
						double lat = jgetLocation.getDouble("lat");
						double lng = jgetLocation.getDouble("lng");
						offices.add(new BankOffice(address, name, lat, lng));
					}
				}
			}
			System.out.println(offices.size());
		}
	}

	private void setOfficeMarkers() {
		if (map != null && offices != null) {
			for (BankOffice office : offices) {
				map.addMarker(office.getMarkerOptions());
			}
		}
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

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	private class GetOfficeData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				getOfficesData();
			} catch (IOException | JSONException e) {
				e.printStackTrace();
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
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

}
