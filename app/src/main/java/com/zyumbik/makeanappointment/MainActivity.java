package com.zyumbik.makeanappointment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OfficeMapFragment.OnFragmentInteractionListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		ActivityCompat.OnRequestPermissionsResultCallback {

	private View step1, step2, step3;
	private FragmentManager fragmentManager;
	private GoogleApiClient googleApiClient;
	private ArrayList<BankOffice> offices;

	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	private boolean permissionDenied = false;
	private Location lastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout mainView = (FrameLayout) inflater.inflate(R.layout.activity_main, null);
		LinearLayout parent = (LinearLayout) mainView.findViewById(R.id.stepper_container);
		step1 = inflater.inflate(R.layout.layout_step, null);
		parent.addView(step1);
		step2 = inflater.inflate(R.layout.layout_step, null);
		parent.addView(step2);
		step3 = inflater.inflate(R.layout.layout_step, null);
		parent.addView(step3);
		setContentView(mainView);

		((TextView) step2.findViewById(R.id.button_text)).setText("2");
		((TextView) step3.findViewById(R.id.button_text)).setText("3");
		step3.findViewById(R.id.stepper_connector).setVisibility(View.GONE);

	}

	private void createMapFragment() {
		FrameLayout container = (FrameLayout) step1.findViewById(R.id.step_content_container);
		fragmentManager = getSupportFragmentManager();
		checkLocationPermission();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		OfficeMapFragment map = OfficeMapFragment.newInstance(lastLocation, permissionDenied);
		transaction.add(container.getId(), map);
		transaction.commit();
	}

	protected void onStart() {
		googleApiClient.connect();
		super.onStart();
	}

	protected void onStop() {
		googleApiClient.disconnect();
		super.onStop();
	}

	private void checkLocationPermission() {
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission to access the location is missing.
			permissionDenied = true;
			PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
					android.Manifest.permission.ACCESS_FINE_LOCATION, true);
		}
	}

	@Override
	public void onFragmentLoaded(ArrayList<BankOffice> offices) {
		this.offices = offices;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			permissionDenied = true;
			return;
		}
		lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
		createMapFragment();
	}

	@Override
	public void onConnectionSuspended(int i) {}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		createMapFragment();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
			return;
		}
		permissionDenied = !PermissionUtils.isPermissionGranted(permissions, grantResults, android.Manifest.permission.ACCESS_FINE_LOCATION);
	}

}
