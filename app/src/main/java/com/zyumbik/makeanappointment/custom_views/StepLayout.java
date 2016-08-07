package com.zyumbik.makeanappointment.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyumbik.makeanappointment.R;

/** Created by glebsabirzanov on 07/08/16. */
/** Custom view for the step in stepper */
public class StepLayout extends RelativeLayout {

	private int stepNumber = 0;
	private boolean selectable = false;
	private static int currentlySelected = 0;

	private View content, connector;
	private RelativeLayout.LayoutParams connectorParams;
	private TextView subhead, buttonText;

	private onStepSelectListener selectListener;

	// Default settings for steps
	public void initializeStep(int stepNumber, onStepSelectListener selectListener) {
		this.stepNumber = stepNumber;
		this.selectListener = selectListener;
		connector = findViewById(R.id.stepper_connector);
		content = findViewById(R.id.step_content_container);
		subhead = (TextView) findViewById(R.id.text_subhead);
		buttonText = (TextView) findViewById(R.id.button_text);
		connectorParams = (RelativeLayout.LayoutParams) connector.getLayoutParams();
		buttonText.setText(String.valueOf(stepNumber));
		setOnClickListener();
		switch (stepNumber) {
			case 1:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_1_header);
				subhead.setText(R.string.step_1_subhead);
				selectable = true;
				currentlySelected = 1;
				break;
			case 2:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_2_header);
				subhead.setText(R.string.step_2_subhead);
				switchConnectorParams();
				deselectStep();
				break;
			case 3:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_3_header);
				subhead.setText(R.string.step_3_subhead);
				findViewById(R.id.stepper_connector).setVisibility(GONE);
				deselectStep();
				break;
			default:
				break;
		}
	}

	// This step is selected, deselect other steps by sending onStepSelect callback
	public void selectStep() {
		if (currentlySelected != stepNumber) {
			currentlySelected = stepNumber;
			selectListener.onStepSelect(currentlySelected);
			selectable = true;
			content.setVisibility(VISIBLE);
		}
	}

	// Used for selecting exact step if necessary
	public void selectStep(int stepNumber) {
		if (currentlySelected != stepNumber) {
			currentlySelected = stepNumber;
			selectListener.onStepSelect(currentlySelected);
		}
	}

	// Select next step if possible
	public void nextStep() {
		if (currentlySelected < 4) {
			currentlySelected++;
			selectListener.onStepSelect(currentlySelected);
		}
	}

	// Deselect this step
	public void deselectStep() {
		content.setVisibility(GONE);
	}

	// This step is clicked. Select it if possible.
	private void setOnClickListener() {
		super.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectable) {
					selectStep();
				}
			}
		});
	}

	// This step returned some data from user. Now let user to move on by clicking on the title.
	public void setSubheadText(String text) {
		subhead.setText(text);
		if (!subhead.hasOnClickListeners()) {
			subhead.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					nextStep();
				}
			});
		}
	}

	// When step has no content in it, it's connector line should be 32dp.
	// Remove restriction on height by setting "align_bottom" parameter of the connector to 0.
	private void switchConnectorParams() {
		if (currentlySelected == stepNumber) {
			connectorParams.addRule(RelativeLayout.ALIGN_BOTTOM, 0);
		} else {
			connectorParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.container_content_and_subhead);
		}
		connector.setLayoutParams(connectorParams);
	}

	public interface onStepSelectListener {
		void onStepSelect(int stepNumber);
	}

	// Default constructors
	public StepLayout(Context context) {
		super(context);
	}

	public StepLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StepLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

}
