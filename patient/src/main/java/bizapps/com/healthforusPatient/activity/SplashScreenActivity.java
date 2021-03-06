package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.PrefManager;

public class SplashScreenActivity extends BaseActivity {
	Typeface custom_font;
	//TextView health_txt, for_us;
	ImageView icon;
	PrefManager pref;
	RelativeLayout splashLayout;
	private int textAnimTime = 2000;
	private int splashTime = 1500;
	Animation textAnimation;
	Animation localAnimation;
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor editor;
	public static String MyPREFERENCES = "doctorpluspatient";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		editor = mSharedPreferences.edit();

		pref = getAppSharedPreference();
		textAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		localAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

		icon = (ImageView) findViewById(R.id.splash_img);
		splashLayout = (RelativeLayout) findViewById(R.id.splashLayout);
		custom_font = Typeface.createFromAsset(getAssets(), "fonts/KirstyBold.ttf");

//		health_txt = (TextView) findViewById(R.id.health_txt);
//		for_us = (TextView) findViewById(R.id.for_us_txt);
//		health_txt.setVisibility(View.GONE);
//		for_us.setVisibility(View.GONE);

		runOnUiThread(new Runnable() {
			public void run() {
				SplashScreenActivity.this.doAnimation();
			}
		});
		waitText();
//		health_txt.setTypeface(custom_font);
//		for_us.setTypeface(custom_font);
//
//		health_txt.setText("MEDICO");
//		for_us.setText("FOR US");
	}

	public void moveToNextScreen() {
		Class nextActivity = null;
		if(!mSharedPreferences.getString("userID","NA").equalsIgnoreCase("NA")){
			BZAppApplication.userID = mSharedPreferences.getString("userID"," ");
			nextActivity = HomeActivity.class;
		} else {
			nextActivity = LoginActivity.class;
		}

		Intent nextScreen = new Intent(this, nextActivity);
		startActivity(nextScreen);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private void doAnimation() {
		icon.startAnimation(localAnimation);
	}

	private void waitText() {
		this.splashLayout.postDelayed(new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
//						health_txt.setVisibility(View.VISIBLE);
//						for_us.setVisibility(View.VISIBLE);
//						health_txt.startAnimation(textAnimation);
//						for_us.startAnimation(textAnimation);
						waitTime();
					}
				});

			}
		}, this.textAnimTime);
	}

	private void waitTime() {
		this.splashLayout.postDelayed(new Runnable() {
			public void run() {
				moveToNextScreen();

			}
		}, this.splashTime);
	}
}
