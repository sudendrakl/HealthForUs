package bizapps.com.healthforusPatient.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.PrefManager;

public class MobileVerificationActivity extends BaseActivity {

	LinearLayout userTypeLayout, mobileVerificationId;
	EditText mobileVerification;
	TextView headerType;
	RadioButton doctor_radio, patient_radio;
	Typeface custom_font;
	boolean isMobileVerification = false;
	Button submit;
	Animation rightInAnimation, leftoutAnimation, rightOutAnimation, leftInAnimation;
	ImageView backArrow;
	PrefManager pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_verification);
		
		pref = getAppSharedPreference();
		rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
		leftoutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
		rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
		leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		
		mobileVerificationId = (LinearLayout) findViewById(R.id.mobileVerificationId);
		userTypeLayout = (LinearLayout) findViewById(R.id.user_type_layout);
		mobileVerification = (EditText) findViewById(R.id.mobile_verification_layout);
		headerType = (TextView) findViewById(R.id.header_text);
		doctor_radio = (RadioButton) findViewById(R.id.doctor_radio);
		patient_radio = (RadioButton) findViewById(R.id.patient_radio);
		custom_font = Typeface.createFromAsset(getAssets(), "fonts/KirstyBold.ttf");
		headerType.setTypeface(custom_font);
		
		backArrow = (ImageView) findViewById(R.id.back_arrow);
		backArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isMobileVerification = false;
				updateUI();
				mobileVerificationId.startAnimation(leftInAnimation);
			}
		});
		
		submit = (Button) findViewById(R.id.submit_btn);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(isMobileVerification){
					pref.setMobileNumber(mobileVerification.getText().toString().trim());
				} else {
					isMobileVerification = true;
				}
				updateUI();
			}
		});

		doctor_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				pref.setUserType(isChecked);
			}
		});
		
		updateUI();
	}

	private void updateUI() {
		if (isMobileVerification) {
			headerType.setText("Verify Mobile");
			mobileVerification.setVisibility(View.VISIBLE);
			userTypeLayout.setVisibility(View.GONE);
			backArrow.setVisibility(View.VISIBLE);
			submit.setText("Verify");
			mobileVerificationId.startAnimation(rightInAnimation);
		} else {
			headerType.setText("Select User Type");
			userTypeLayout.setVisibility(View.VISIBLE);
			mobileVerification.setVisibility(View.GONE);
			submit.setText("Next");
			backArrow.setVisibility(View.GONE);
		}
	}
}
