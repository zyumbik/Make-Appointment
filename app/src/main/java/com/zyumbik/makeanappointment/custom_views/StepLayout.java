package com.zyumbik.makeanappointment.custom_views;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyumbik.makeanappointment.R;

/** Created by glebsabirzanov on 07/08/16. */
/** Custom view for the step in stepper */
public class StepLayout extends RelativeLayout {

	private int stepNumber = 0;
	private boolean clickable = false;
	private static int currentlySelected = 0;

	private View content, connector;
	private RelativeLayout.LayoutParams connectorParams;
	private TextView subhead, buttonText;
	private ImageButton stepButton;

	private onStepInteractionListener interactionListener;
	private OnClickListener clickListener;

	// Default settings for steps
	public void initializeStep(int stepNumber, onStepInteractionListener interactionListener) {
		this.stepNumber = stepNumber;
		this.interactionListener = interactionListener;
		connector = findViewById(R.id.stepper_connector);
		content = findViewById(R.id.step_content_container);
		subhead = (TextView) findViewById(R.id.text_subhead);
		buttonText = (TextView) findViewById(R.id.button_text);
		stepButton = (ImageButton) findViewById(R.id.step_button);

		connectorParams = (RelativeLayout.LayoutParams) connector.getLayoutParams();
		buttonText.setText(String.valueOf(stepNumber));
		setOnClickListener();

		switch (stepNumber) {
			case 1:
				((TextView) findViewById(R.id.text_header)).setText(R.string.step_1_header);
				subhead.setText(R.string.step_1_subhead);
				clickable = true;
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

	// This step is selected, deselect other steps by sending onStepClick callback
	public void selectStep() {
		currentlySelected = stepNumber;
		content.setVisibility(VISIBLE);
		setButtonBackground(R.drawable.step_button_selected);
		interactionListener.onStepSelect(stepNumber);
		clickable = true;
	}

	// Deselect this step
	public void deselectStep() {
		setButtonBackground(R.drawable.step_button_unselected);
		content.setVisibility(GONE);
		if (stepNumber == 1) {
			switchConnectorParams();
		}
	}

	// Called when the step returned some data from user
	public void stepCompleted() {
		stepButton.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_check_16dp));
		buttonText.setText("");
	}

	// Step can be selected if it is clickable and not selected already
	public boolean isSelectable() {
		return clickable && !(currentlySelected == stepNumber);
	}

	public boolean isStepClickable() {
		return clickable;
	}

	public void setStepClickable(boolean clickable) {
		this.clickable = clickable;
	}

	// This step is clicked. Select it if possible.
	private void setOnClickListener() {
		clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				interactionListener.onStepClick(stepNumber);
			}
		};
		super.setOnClickListener(clickListener);
	}

	// This step returned some data from user. Now let user to move on by clicking on the title.
	public void setSubheadText(String text) {
		subhead.setText(text);
	}

	public View getContent() {
		return content;
	}

	private void setButtonBackground(int drawableResourse) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			stepButton.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), drawableResourse));
		} else {
			stepButton.setBackground(ContextCompat.getDrawable(this.getContext(), drawableResourse));
		}
	}

	public interface onStepInteractionListener {
		void onStepClick(int stepNumber);
		void onStepSelect(int stepNumber);
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
