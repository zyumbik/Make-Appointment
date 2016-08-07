package com.zyumbik.makeanappointment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** Created by glebsabirzanov on 07/08/16. */
/** Model for the step in stepper */
public class Step  extends RelativeLayout {

	private int stepNumber = 0;

	private View content, connector;
	private RelativeLayout.LayoutParams connectorParams;
	private TextView subhead, buttonText;

	public void initializeStep(int stepNumber) {
		this.stepNumber = stepNumber;
		connector = findViewById(R.id.stepper_connector);
		content = findViewById(R.id.step_content_container);
		subhead = (TextView) findViewById(R.id.text_subhead);
		buttonText = (TextView) findViewById(R.id.button_text);
		switch (stepNumber) {
			case 1:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_1_header);
				subhead.setText(R.string.step_1_subhead);
				buttonText.setText("1");
				break;
			case 2:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_2_header);
				subhead.setText(R.string.step_2_subhead);
				buttonText.setText("2");
				break;
			case 3:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_3_header);
				subhead.setText(R.string.step_3_subhead);
				buttonText.setText("3");
				break;
			default:
				break;
		}
	}

	public void selected() {

	}

	public void deselect() {
		content.setVisibility(GONE);
	}

	public Step(Context context) {
		super(context);
	}

	public Step(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Step(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

}
