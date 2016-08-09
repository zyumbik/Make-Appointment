package com.zyumbik.makeanappointment.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/** Created by glebsabirzanov on 10/08/16. */
/** MapView to handle touch events */
public class CustomMapView extends MapView {

	private OnMapTouchListener listener;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				listener.onTouch();
				break;
			case MotionEvent.ACTION_UP:
				listener.onTouch();
				break;
		}
		return super.dispatchTouchEvent(event);
	}

	public void setOnMapTouchListener(OnMapTouchListener listener) {
		this.listener = listener;
	}

	public CustomMapView(Context context) {
		super(context);
	}

	public CustomMapView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public CustomMapView(Context context, AttributeSet attributeSet, int i) {
		super(context, attributeSet, i);
	}

	public CustomMapView(Context context, GoogleMapOptions googleMapOptions) {
		super(context, googleMapOptions);
	}

	public interface OnMapTouchListener {
		void onTouch();
	}

}
