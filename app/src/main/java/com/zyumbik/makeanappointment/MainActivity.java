package com.zyumbik.makeanappointment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OfficeMapFragment.OnFragmentInteractionListener {

	View step1, step2, step3;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		FrameLayout container = (FrameLayout) step1.findViewById(R.id.step_content_container);

		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		OfficeMapFragment map = OfficeMapFragment.newInstance();
		transaction.add(container.getId(), map);
		transaction.commit();

		TextView buttonText2 = (TextView) step2.findViewById(R.id.button_text);
		buttonText2.setText("2");
		TextView buttonText3 = (TextView) step3.findViewById(R.id.button_text);
		buttonText3.setText("3");
		step3.findViewById(R.id.stepper_connector).setVisibility(View.GONE);

	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
