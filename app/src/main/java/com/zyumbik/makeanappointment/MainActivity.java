package com.zyumbik.makeanappointment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.MonthAdapter;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.zyumbik.makeanappointment.custom_views.StepLayout;
import com.zyumbik.makeanappointment.data_models.BankOffice;
import com.zyumbik.makeanappointment.utils.PermissionUtils;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements OfficeMapFragment.OnFragmentInteractionListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		ActivityCompat.OnRequestPermissionsResultCallback,
		StepLayout.onStepSelectListener,
		DatePickerDialog.OnDateSetListener,
		TimePickerDialog.OnTimeSetListener {

	private StepLayout[] steps = new StepLayout[3];
	private GoogleApiClient googleApiClient;
	private GregorianCalendar cal;

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

		initialStepperSetup();

	}

	private void initialStepperSetup() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout mainView = (FrameLayout) inflater.inflate(R.layout.activity_main, null);
		LinearLayout parent = (LinearLayout) mainView.findViewById(R.id.stepper_container);
		for (int i = 0; i < steps.length; i++) {
			steps[i] = (StepLayout) inflater.inflate(R.layout.layout_step, null);
			parent.addView(steps[i]);
			steps[i].initializeStep(i + 1, this);
		}
	}

	private void createMapFragment() {
		FrameLayout container = (FrameLayout) steps[0].findViewById(R.id.step_content_container);
		FragmentManager fragmentManager = getSupportFragmentManager();
		checkLocationPermission();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		OfficeMapFragment map = OfficeMapFragment.newInstance(lastLocation, permissionDenied);
		transaction.add(container.getId(), map);
		transaction.commit();
	}

	private void showDatePicker() {
		DatePickerDialog datePicker = new DatePickerDialog();
		cal = new GregorianCalendar();
		datePicker.initialize(this, cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH));
		datePicker.show(getFragmentManager(), "date_picker");
	}

	private void showTimePicker() {
		TimePickerDialog timePicker = new TimePickerDialog();
		timePicker.initialize(this, cal.get(GregorianCalendar.HOUR_OF_DAY), cal.get(GregorianCalendar.MINUTE), true);
		timePicker.show(getFragmentManager(), "time_picker");
	}

	@Override
	public void onStepSelect(int stepNumber) {
		for (int i = 0; i < steps.length; i++) {
			if (i + 1 != stepNumber) {
				steps[i].deselectStep();
			}
		}
		if (stepNumber == 2) {
			showDatePicker();
		}
	}

	@Override
	public void onMarkerClicked(BankOffice office) {
		steps[0].setSubheadText(office.getAddress());
	}

	@Override
	public void onInfoWindowClick(BankOffice office) {
		steps[0].setSubheadText(office.getAddress());
		steps[0].nextStep();
	}

	@Override
	public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
		showTimePicker();
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

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
