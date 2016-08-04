package com.zyumbik.makeanappointment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout mainView = (FrameLayout) inflater.inflate(R.layout.activity_main, null);
		LinearLayout parent = (LinearLayout) mainView.findViewById(R.id.stepper_container);
		View step1 = inflater.inflate(R.layout.layout_step, null);
		View step2 = inflater.inflate(R.layout.layout_step, null);
		View step3 = inflater.inflate(R.layout.layout_step, null);
		parent.addView(step1);
		parent.addView(step2);
		parent.addView(step3);
		setContentView(mainView);
	}
}
