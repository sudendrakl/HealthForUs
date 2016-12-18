package bizapps.com.healthforusPatient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusPatient.Model.Login;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.Config;
import bizapps.com.healthforusPatient.utills.ConnectivityReceiver;
import bizapps.com.healthforusPatient.utills.GsonRequest;
import bizapps.com.healthforusPatient.utills.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button login, register;
	private EditText userName, Pasword;
	private TextView forgotPassword;
	RequestQueue mRequestQueue;
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor editor;
	public static String MyPREFERENCES = "doctorpluspatient";

	//GCM
	private String TAG = LoginActivity.class.getSimpleName();
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private BroadcastReceiver mRegistrationBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();

		login = (Button) findViewById(R.id.submitBtn);
		login.setOnClickListener(this);
		register = (Button) findViewById(R.id.registerBtn);
		register.setOnClickListener(this);
		userName = (EditText) findViewById(R.id.user_name);
		Pasword = (EditText) findViewById(R.id.password);
		forgotPassword = (TextView) findViewById(R.id.forgot_password);
		forgotPassword.setOnClickListener(this);

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				// checking for type intent filter
				if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
					// gcm successfully registered
					// now subscribe to `global` topic to receive app wide notifications
					String token = intent.getStringExtra("token");

					Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

				} else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
					// gcm registration id is stored in our server's MySQL

					Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

				} else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
					// new push notification is received

					Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
				}
			}
		};

		if (checkPlayServices()) {
			registerGCM();
		}
	}

	private void registerGCM() {
//		Intent intent = new Intent(this, GcmIntentService.class);
//		intent.putExtra("key", "register");
//		startService(intent);
	}

	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i(TAG, "This device is not supported. Google Play Services not installed!");
				Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// register GCM registration complete receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Config.REGISTRATION_COMPLETE));

		// register new push message receiver
		// by doing this, the activity will be notified each time a new message arrives
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Config.PUSH_NOTIFICATION));
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.submitBtn) {
			if(valid()){
				Utility.hideKeyboard(this);
				boolean isConnected = ConnectivityReceiver.isConnected();
				if(isConnected){
					apiCall();}
				else{
					Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
				}
				//apiCall();
			}

		} else if (view.getId() == R.id.registerBtn) {
			Intent register = new Intent(this, RegisterActivity.class);
			register.putExtra("isRegister", true);
			startActivity(register);
			overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		} else if (view.getId() == R.id.forgot_password) {
			Intent register = new Intent(this, ForgotPasswordActivity.class);
			startActivity(register);
			overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		}
	}

	public boolean valid(){
		boolean valid = true;

		if(userName.getText().toString().length()==0){
			userName.setError("Username cannot be empty");
			valid = false;
		}
		if(Pasword.getText().toString().length()==0){
			Pasword.setError("Password cannot be blank");
			valid = false;
		}

		return valid;
	}

	public void apiCall(){
		String url = "http://medico4us.in/patientLogin.php";
		mRequestQueue = Volley.newRequestQueue(this);
		JSONObject mCredentials = new JSONObject();

		try {
			mCredentials.put("mobile",userName.getText().toString());
			mCredentials.put("password",Pasword.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		GsonRequest<Login> myReq = new GsonRequest<Login>(
				Request.Method.POST,
				url,
				Login.class,
				mCredentials.toString(),
				null,
				createMyReqSuccessListener(),
				createMyReqErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				//headers.put("userId",mSharedPreferences.getString("SoBeerID", " "));
				return headers;
			}
		};

		int socketTimeout = 20000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);

		Log.d("myReq", "" + myReq);
		mRequestQueue.add(myReq);

	}

	private Response.Listener<Login> createMyReqSuccessListener() {
		return new Response.Listener<Login>() {
			@Override
			public void onResponse(Login response) {
				try {
					Log.e("Patient Login Response",""+response);
					if(response.getStatus().equalsIgnoreCase("success")){
						editor.putString("userID",response.getUserid());
						editor.commit();
						Gson gson = new Gson();
						String myData = gson.toJson(response);
						Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

						intent.putExtra("GetData",myData);
						startActivity(intent);
						finish();
					}
					else{
						showErrorDialog(response.getMessage());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				String errorMsg = error.getMessage();

				Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG)
						.show();

			}
		};
	}

	public void showErrorDialog(String msg){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder.setTitle("Info");

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
//                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//                        // if this button is clicked, close
//                        // current activity
//                        MainActivity.this.finish();
//                    }
//                })
				.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();

					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	//TODO: remove this for test
	//@Override protected void onStart() {
	//	super.onStart();
	//	onTokenRefresh();
	//}
  //
	//public void onTokenRefresh() {
	//	// Get updated InstanceID token.
	//	String refreshedToken = FirebaseInstanceId.getInstance().getToken();
	//	System.out.println("HomeActivity"+" shit : Refreshed token: " + refreshedToken);
	//	// TODO: Implement this method to send any registration to your app's servers.
	//	// send this to server sendRegistrationToServer(refreshedToken);
	//	Intent intent = new Intent(this, RegistrationService.class);
	//	//intent.putExtra(Constants.IntentExtra.AUTH_TOKEN, auth_token); //TODO auth token got via app registration... not sure why its another api
	//	intent.putExtra(Constants.IntentExtra.GCM_KEY, refreshedToken);
	//	startService(intent);
	//}


}
