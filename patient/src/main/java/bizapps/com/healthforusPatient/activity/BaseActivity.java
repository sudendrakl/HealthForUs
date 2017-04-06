package bizapps.com.healthforusPatient.activity;

import android.support.v7.app.AppCompatActivity;
import bizapps.com.healthforusPatient.utills.PrefManager;

public class BaseActivity extends AppCompatActivity{
	
	public PrefManager getAppSharedPreference(){
		PrefManager prefs = new PrefManager(getApplicationContext());
		return prefs;
	}
}
