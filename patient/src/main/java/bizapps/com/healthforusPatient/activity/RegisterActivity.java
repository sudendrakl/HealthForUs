package bizapps.com.healthforusPatient.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bizapps.com.healthforusPatient.Model.Login;
import bizapps.com.healthforusPatient.Model.SignupModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.ConnectivityReceiver;
import bizapps.com.healthforusPatient.utills.GsonRequest;
import bizapps.com.healthforusPatient.utills.MultipartRequest;
import bizapps.com.healthforusPatient.utills.UtilNew;
import bizapps.com.healthforusPatient.utills.Utility;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

	public String mGender="male",status;
	public static String imageUrls;
	private String userChoosenTask;
	private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	EditText et_name,et_mob,et_email,et_pwd,et_cpwd,et_age;
	TextView tv_male,tv_female,tv_pic,tv_terms,tv_policy;
	Button mRegister;
	File file1,nFile1,nFile2;
	ProgressDialog progressDialog;
	LinearLayout ll_gender;
	RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_new);

		et_name = (EditText) findViewById(R.id.name);
		et_mob = (EditText) findViewById(R.id.mobile);
		et_email = (EditText) findViewById(R.id.email_id);
		et_age = (EditText) findViewById(R.id.age);
		et_pwd = (EditText) findViewById(R.id.password);
		et_cpwd = (EditText) findViewById(R.id.confirmpassword);

		ll_gender = (LinearLayout) findViewById(R.id.selection_layout);
		tv_male = (TextView) findViewById(R.id.gender_male);
		tv_male.setOnClickListener(this);
		tv_female = (TextView) findViewById(R.id.gender_female);
		tv_female.setOnClickListener(this);
		tv_pic = (TextView) findViewById(R.id.tv_doc1);
		tv_pic.setOnClickListener(this);
		tv_terms = (TextView) findViewById(R.id.t2);
		tv_policy = (TextView) findViewById(R.id.t3);

		tv_terms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				termsDialog("Terms and Conditions","tt");

			}
		});

		tv_policy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				termsDialog("Privacy Policy","PP");

			}
		});

		mRegister = (Button) findViewById(R.id.submitBtn);
		mRegister.setOnClickListener(this);
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				boolean result= UtilNew.checkPermission(RegisterActivity.this);

				if (items[item].equals("Take Photo")) {
					userChoosenTask ="Take Photo";
					if(result)
						cameraIntent();

				} else if (items[item].equals("Choose from Library")) {
					userChoosenTask ="Choose from Library";
					if(result)
						galleryIntent();

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void galleryIntent()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);//
		startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
	}

	private void cameraIntent()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_FILE) {
				Bitmap photo;
									try {
						photo = (Bitmap) MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
						Uri tempUri = getImageUri(getApplicationContext(), photo);
						imageUrls = (getRealPathFromURI(tempUri));
										tv_pic.setText(imageUrls);
					} catch (IOException e) {
						e.printStackTrace();
					}
//                onSelectFromGalleryResult(data);
			}
			else if (requestCode == REQUEST_CAMERA) {
//                onCaptureImageResult(data);
				Bitmap photo;

					photo = (Bitmap) data.getExtras().get("data");
					Uri tempUri = getImageUri(getApplicationContext(), photo);
					imageUrls = (getRealPathFromURI(tempUri));

			}
		}

        /*if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //knop.setVisibility(Button.VISIBLE);


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            m1 = getRealPathFromURI(tempUri);

            mStore.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ok_filled,0);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //file1 = new File(getRealPathFromURI(tempUri));

            //System.out.println(mImageCaptureUri);
        } else if (requestCode == CAMERA_REQUEST1 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //knop.setVisibility(Button.VISIBLE);


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            m2 = getRealPathFromURI(tempUri);
            mDoc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ok_filled,0);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            // file2 = new File(getRealPathFromURI(tempUri));

            //System.out.println(mImageCaptureUri);
        }*/
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.submitBtn:
				if(validate()){
					Utility.hideKeyboard(this);
					boolean isConnected = ConnectivityReceiver.isConnected();
					if(isConnected){
						apiCall();}
					else{
						Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case R.id.tv_doc1:
				selectImage();
				break;
			case R.id.gender_male:
				mGender = "male";
				tv_male.setBackgroundColor(getResources().getColor(R.color.primary));
				tv_male.setTextColor(getResources().getColor(R.color.icons));
				tv_female.setBackgroundColor(getResources().getColor(R.color.icons));
				tv_female.setTextColor(getResources().getColor(R.color.black80));
				ll_gender.setBackground(getResources().getDrawable(R.drawable.rounded_green_textview));
				break;
			case R.id.gender_female:
				mGender = "female";
				tv_male.setBackgroundColor(getResources().getColor(R.color.icons));
				tv_male.setTextColor(getResources().getColor(R.color.black80));
				tv_female.setBackgroundColor(getResources().getColor(R.color.primary));
				tv_female.setTextColor(getResources().getColor(R.color.icons));
				ll_gender.setBackground(getResources().getDrawable(R.drawable.rounded_green_textview));
				break;
		}
	}

	public boolean validate(){
		boolean valid=true;


		if(imageUrls==null){
			//valid = false;
			Toast.makeText(getApplicationContext(),"Please upload image",Toast.LENGTH_LONG).show();
		}

		if(et_name.getText().toString().length()==0 && et_mob.getText().toString().length()==0 &&
				et_email.getText().toString().length()==0 && et_age.getText().toString().length()==0
				&& et_pwd.getText().toString().length()==0 && et_cpwd.getText().toString().length()==0 ){
			valid = false;
			Toast.makeText(getApplicationContext(),"fields cannot be empty",Toast.LENGTH_LONG).show();
		}
		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		if(et_email.getText().toString()!=null) {
			if (!et_email.getText().toString().matches(emailPattern)) {
				valid = false;
				Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_LONG).show();
			}
		}
		if(et_mob.getText().toString().length()!=10){
			valid = false;
			Toast.makeText(getApplicationContext(),"Please enter valid mobile number",Toast.LENGTH_LONG).show();
		}

		if(!(et_cpwd.getText().toString()).equalsIgnoreCase(et_pwd.getText().toString())){
			valid = false;
			Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
		}

		return valid;
	}

	public void showErrorDialog(String msg, final boolean isVerify){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder.setTitle("Info");

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						if(status.equalsIgnoreCase("success") && isVerify){
							verifyAccount();
						}
						else{
							Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
							startActivity(intent);
							finish();
						}
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	public void apiCall() {
		displayProgress();
		if(imageUrls!=null){
			file1 = new File(imageUrls);
			nFile1 = Utility.saveBitmapToFile(file1);
		}

		List<File> nFiles= new ArrayList<>();

		String url = "http://medico4us.in/patient.php";
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("name", et_name.getText().toString());
		params.put("password", et_pwd.getText().toString());
		params.put("mobile", et_mob.getText().toString());
		params.put("email", et_email.getText().toString());
		params.put("age",et_age.getText().toString());
		params.put("gender", mGender);


		MultipartRequest mr = new MultipartRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				dismissProgress();
				Log.d("response", response);
				Gson gson = new Gson();
				SignupModel obj = gson.fromJson(response, SignupModel.class);
				Log.e("Status",""+obj.getStatus());
				status = obj.getStatus();
				showErrorDialog(obj.getData().getMessage(),true);
			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dismissProgress();
			}

		}, nFile1, nFile2,nFiles, params,true);

		Log.e("mr", "" + mr.toString());
		mr.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		Volley.newRequestQueue(this).add(mr);

	}

	public void displayProgress() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
			progressDialog.setMessage("Loading wait...");
		}

		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public void dismissProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void verifyAccount(){
		final AlertDialog builder = new AlertDialog.Builder(this, R.style.InvitationDialog)
				.setPositiveButton("Done", null)
				.setNegativeButton("Cancel", null)
				.create();

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText emailBox = new EditText(this);
		emailBox.setTextColor(getResources().getColor(R.color.icons));
		emailBox.setHintTextColor(getResources().getColor(R.color.icons));
		emailBox.setHint("Email");
		layout.addView(emailBox);

		final EditText codeBox = new EditText(this);
		codeBox.setTextColor(getResources().getColor(R.color.icons));
		codeBox.setHintTextColor(getResources().getColor(R.color.icons));
		codeBox.setHint("Code");
		layout.addView(codeBox);

		builder.setView(layout);

//		final EditText etNickName = new EditText(this);
//		etNickName.setTextColor(Color.parseColor("#FFFFFF"));
//		builder.setView(etNickName);
		builder.setTitle("Verify registration");

		builder.setMessage("Enter emailId and code to activate account");

		builder.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
				btnAccept.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (emailBox.getText().toString().isEmpty() && codeBox.getText().toString().isEmpty()) {
							Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
						}
						else {
							verifyApi(emailBox.getText().toString(),codeBox.getText().toString());
							builder.dismiss();
						}
					}
				});

				final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
				btnDecline.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
					}
				});
			}
		});

		builder.show();

	}

	public void verifyApi(String email,String code){
		displayProgress();
		//String url = "http://doctorapp.rakyow.com/userapp/patientapi/verifycode";
		String url = "http://medico4us.in/patientPasscodeVerification.php";
		mRequestQueue = Volley.newRequestQueue(this);
		JSONObject mCredentials = new JSONObject();

		try {
			mCredentials.put("email",email);
			mCredentials.put("otp",code);
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
				dismissProgress();
				try {
					if(response.getStatus().equalsIgnoreCase("success")){
						showErrorDialog(response.getMessage(),false);
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
				dismissProgress();
				String errorMsg = error.getMessage();

				Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG)
						.show();

			}
		};
	}

	private void CopyAssetsbrochure(String fName) {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try
		{
			files = assetManager.list("");
		}
		catch (IOException e)
		{
			Log.e("tag", e.getMessage());
		}
		for(int i=0; i<files.length; i++)
		{
			String fStr = files[i];
			if(fStr.equalsIgnoreCase(fName))
			{
				InputStream in = null;
				OutputStream out = null;
				try
				{
					in = assetManager.open(files[i]);
					out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + files[i]);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
					break;
				}
				catch(Exception e)
				{
					Log.e("tag", e.getMessage());
				}
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}

	public void termsDialog(String title,String text){
		final Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.terms_layout);

		TextView titleTv = (TextView) dialog.findViewById(R.id.title_text);
		titleTv.setText(title);
		ImageView imgView = (ImageView) dialog.findViewById(R.id.imageView1);
		imgView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		String terms="";
		if(text.equalsIgnoreCase("pp")){
			terms  = "Welcome to Medico4us!\n" +
					"These terms and conditions outline the rules and regulations for the use of Medico4us Website/Application. \n" +
					"By accessing this website, we assume you accept these terms and conditions in full. Do not continue to use Medico4us application or website if you do not accept all of the terms and conditions stated on this page.\n" +
					"The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and any or all Agreements: \"Client\", “You” and “Your” refers to you, the person accessing this website and accepting the Company’s terms and conditions. \"The Company\", “Ourselves”, “We”, “Our” and \"Us\", refers to our Company. “Party”, “Parties”, or “Us”, refers to both the Client and ourselves, or either the Client or ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our assistance to the Client in the most appropriate manner, whether by formal meetings of a fixed duration, or any other means, for the express purpose of meeting the Client’s needs in respect of provision of the Company’s stated services/products, in accordance with and subject to, prevailing law of India. Any use of the above terminology or other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same.\n" +
					"Cookies\n" +
					"We employ the use of cookies. By using Medico4us website you consent to the use of cookies in accordance with Medico4us privacy policy.\n" +
					"Most of the modern day interactive web sites/ applications use cookies to enable us to retrieve user details for each visit. Cookies are used in some areas of our site to enable the functionality of this area and ease of use for those people visiting. Some of our affiliate / advertising partners may also use cookies.\n" +
					"License\n" +
					"Unless otherwise stated, Medico4us and/or it’s licensors own the intellectual property rights for all material on Medico4us All intellectual property rights are reserved. You may view and/or print pages for your own personal use subject to restrictions set in these terms and conditions.\n" +
					"You must not:\n" +
					"Republish material from Medico4us\n" +
					"Sell, rent or sub-license material fromMedico4us\n" +
					"Reproduce, duplicate or copy material from Medico4us\n" +
					"Redistribute content from Medico4us (unless content is specifically made for redistribution).\n" +
					"\n" +
					"User Comments\n" +
					"This Agreement shall begin on the date hereof.\n" +
					"Certain parts of this application/website offer the opportunity for users to post and exchange opinions, information, material and data ('Comments') in areas of the feedback(website/application). Medico4us does not screen, edit, publish or review Comments prior to their appearance on the website and Comments do not reflect the views or opinions of Medico4us, its agents or affiliates. Comments reflect the view and opinion of the person who posts such view or opinion. To the extent permitted by applicable laws Medico4us shall not be responsible or liable for the Comments or for any loss cost, liability, damages or expenses caused and or suffered as a result of any use of and/or posting of and/or appearance of the Comments on this website/application.\n" +
					"Medico4us reserves the right to monitor all Comments and to remove any Comments which it considers in its absolute discretion to be inappropriate, offensive or otherwise in breach of these Terms and Conditions.\n" +
					"You warrant and represent that: You are entitled to post the Comments on our website and have all necessary licenses and consents to do so;\n" +
					"The Comments do not infringe any intellectual property right, including without limitation copyright, patent or trademark, or other proprietary right of any third party;\n" +
					"The Comments do not contain any defamatory, libelous, offensive, indecent or otherwise unlawful material or material which is an invasion of privacy\n" +
					"The Comments will not be used to solicit or promote business or custom or present commercial activities or unlawful activity.\n" +
					"You hereby grant to Medico4us a non-exclusive royalty-free license to use, reproduce, edit and authorize others to use, reproduce and edit any of your Comments in any and all forms, formats or media.\n" +
					"Hyperlinking to our Content\n" +
					"The following organizations may link to our Web site without prior written approval:\n" +
					"Government agencies;\n" +
					"Search engines;\n" +
					"News organizations;\n" +
					"\n" +
					"Online directory distributors when they list us in the directory may link to our Web site/application in the same manner as they hyperlink to the Web sites of other listed businesses; and System wide Accredited Businesses except soliciting non-profit organizations, charity shopping malls, and charity fundraising groups which may not hyperlink to our Web site.\n" +
					"These organizations may link to our home page, to publications or to other Web site information so long as the link: (a) is not in any way misleading; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and its products or services; and (c) fits within the context of the linking party's site.\n" +
					"We may consider and approve in our sole discretion other link requests from the following types of organizations:\n" +
					"Commonly-known consumer and/or business information sources such as Chambers of Commerce, community sites; associations or other groups representing charities, including charity giving sites, online directory distributors; internet portals; accounting, law and consulting firms whose primary clients are businesses; and trade associations.\n" +
					"We will approve link requests from these organizations if we determine that: (a) the link would not reflect unfavorably on us or our accredited businesses (for example, trade associations or other organizations representing inherently suspect types of business, such as work-at-home opportunities, shall not be allowed to link); (b)the organization does not have an unsatisfactory record with us; (c) the benefit to us from the visibility associated with the hyperlink outweighs the absence of Medico4us; and (d) where the link is in the context of general resource information or is otherwise consistent with editorial content in a newsletter or similar product furthering the mission of the organization.\n" +
					"These organizations may link to our home page, to publications or to other Web site information so long as the link: (a) is not in any way misleading; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and it products or services; and (c) fits within the context of the linking party's site.\n" +
					"Approved organizations may hyperlink to our Web site as follows:\n" +
					"By use of our corporate name; or\n" +
					"By use of the uniform resource locator (Web address) being linked to; or\n" +
					"By use of any other description of our Web site or material being linked to that makes sense within the context and format of content on the linking party's site.\n" +
					"No use of (name)’s logo or other artwork will be allowed for linking absent a trademark license agreement.\n" +
					"\n" +
					"Reservation of Rights\n" +
					"We reserve the right at any time and in its sole discretion to request that you remove all links or any particular link to our Web site/application. You agree to immediately remove all links to our Web site upon such request. We also reserve the right to amend these terms and conditions and its linking policy at any time. By continuing to link to our Web site/application, you agree to be bound to and abide by these linking terms and conditions.\n" +
					"Whilst we endeavor to ensure that the information on this website is correct, we do not warrant its completeness or accuracy; nor do we commit to ensuring that the website remains available or that the material on the website is kept up to date.\nWelcome to Medico4us!\n" +
					"These terms and conditions outline the rules and regulations for the use of Medico4us Website/Application. \n" +
					"By accessing this website, we assume you accept these terms and conditions in full. Do not continue to use Medico4us application or website if you do not accept all of the terms and conditions stated on this page.\n" +
					"The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and any or all Agreements: \"Client\", “You” and “Your” refers to you, the person accessing this website and accepting the Company’s terms and conditions. \"The Company\", “Ourselves”, “We”, “Our” and \"Us\", refers to our Company. “Party”, “Parties”, or “Us”, refers to both the Client and ourselves, or either the Client or ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our assistance to the Client in the most appropriate manner, whether by formal meetings of a fixed duration, or any other means, for the express purpose of meeting the Client’s needs in respect of provision of the Company’s stated services/products, in accordance with and subject to, prevailing law of India. Any use of the above terminology or other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same.\n" +
					"Cookies\n" +
					"We employ the use of cookies. By using Medico4us website you consent to the use of cookies in accordance with Medico4us privacy policy.\n" +
					"Most of the modern day interactive web sites/ applications use cookies to enable us to retrieve user details for each visit. Cookies are used in some areas of our site to enable the functionality of this area and ease of use for those people visiting. Some of our affiliate / advertising partners may also use cookies.\n" +
					"License\n" +
					"Unless otherwise stated, Medico4us and/or it’s licensors own the intellectual property rights for all material on Medico4us All intellectual property rights are reserved. You may view and/or print pages for your own personal use subject to restrictions set in these terms and conditions.\n" +
					"You must not:\n" +
					"Republish material from Medico4us\n" +
					"Sell, rent or sub-license material fromMedico4us\n" +
					"Reproduce, duplicate or copy material from Medico4us\n" +
					"Redistribute content from Medico4us (unless content is specifically made for redistribution).\n" +
					"\n" +
					"User Comments\n" +
					"This Agreement shall begin on the date hereof.\n" +
					"Certain parts of this application/website offer the opportunity for users to post and exchange opinions, information, material and data ('Comments') in areas of the feedback(website/application). Medico4us does not screen, edit, publish or review Comments prior to their appearance on the website and Comments do not reflect the views or opinions of Medico4us, its agents or affiliates. Comments reflect the view and opinion of the person who posts such view or opinion. To the extent permitted by applicable laws Medico4us shall not be responsible or liable for the Comments or for any loss cost, liability, damages or expenses caused and or suffered as a result of any use of and/or posting of and/or appearance of the Comments on this website/application.\n" +
					"Medico4us reserves the right to monitor all Comments and to remove any Comments which it considers in its absolute discretion to be inappropriate, offensive or otherwise in breach of these Terms and Conditions.\n" +
					"You warrant and represent that: You are entitled to post the Comments on our website and have all necessary licenses and consents to do so;\n" +
					"The Comments do not infringe any intellectual property right, including without limitation copyright, patent or trademark, or other proprietary right of any third party;\n" +
					"The Comments do not contain any defamatory, libelous, offensive, indecent or otherwise unlawful material or material which is an invasion of privacy\n" +
					"The Comments will not be used to solicit or promote business or custom or present commercial activities or unlawful activity.\n" +
					"You hereby grant to Medico4us a non-exclusive royalty-free license to use, reproduce, edit and authorize others to use, reproduce and edit any of your Comments in any and all forms, formats or media.\n" +
					"Hyperlinking to our Content\n" +
					"The following organizations may link to our Web site without prior written approval:\n" +
					"Government agencies;\n" +
					"Search engines;\n" +
					"News organizations;\n" +
					"\n" +
					"Online directory distributors when they list us in the directory may link to our Web site/application in the same manner as they hyperlink to the Web sites of other listed businesses; and System wide Accredited Businesses except soliciting non-profit organizations, charity shopping malls, and charity fundraising groups which may not hyperlink to our Web site.\n" +
					"These organizations may link to our home page, to publications or to other Web site information so long as the link: (a) is not in any way misleading; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and its products or services; and (c) fits within the context of the linking party's site.\n" +
					"We may consider and approve in our sole discretion other link requests from the following types of organizations:\n" +
					"Commonly-known consumer and/or business information sources such as Chambers of Commerce, community sites; associations or other groups representing charities, including charity giving sites, online directory distributors; internet portals; accounting, law and consulting firms whose primary clients are businesses; and trade associations.\n" +
					"We will approve link requests from these organizations if we determine that: (a) the link would not reflect unfavorably on us or our accredited businesses (for example, trade associations or other organizations representing inherently suspect types of business, such as work-at-home opportunities, shall not be allowed to link); (b)the organization does not have an unsatisfactory record with us; (c) the benefit to us from the visibility associated with the hyperlink outweighs the absence of Medico4us; and (d) where the link is in the context of general resource information or is otherwise consistent with editorial content in a newsletter or similar product furthering the mission of the organization.\n" +
					"These organizations may link to our home page, to publications or to other Web site information so long as the link: (a) is not in any way misleading; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and it products or services; and (c) fits within the context of the linking party's site.\n" +
					"Approved organizations may hyperlink to our Web site as follows:\n" +
					"By use of our corporate name; or\n" +
					"By use of the uniform resource locator (Web address) being linked to; or\n" +
					"By use of any other description of our Web site or material being linked to that makes sense within the context and format of content on the linking party's site.\n" +
					"No use of (name)’s logo or other artwork will be allowed for linking absent a trademark license agreement.\n" +
					"\n" +
					"Reservation of Rights\n" +
					"We reserve the right at any time and in its sole discretion to request that you remove all links or any particular link to our Web site/application. You agree to immediately remove all links to our Web site upon such request. We also reserve the right to amend these terms and conditions and its linking policy at any time. By continuing to link to our Web site/application, you agree to be bound to and abide by these linking terms and conditions.\n" +
					"Whilst we endeavor to ensure that the information on this website is correct, we do not warrant its completeness or accuracy; nor do we commit to ensuring that the website remains available or that the material on the website is kept up to date.\nPrivacy Policy\n" +
					"The information provided in Medico4us are posted by registered members of this website and moderated by the team of Medico4us. Doctors & Patients full name, contact number and email id is the only identifying information that is being collected on the application about a user/reader/vendor when they list/post or comment. Medico4us will sell or make use of such information to third parties. We will occasionally analyze such information and will share the results of such analysis in our advisory section. Medico4us may change the policy anytime by posting changes online. Medico4us is not responsible for third party content accessible through the site, including opinions, advice, rating, reviews, statements, and advertisements, and user shall bear all risks associated with the use of such content. \n" +
					"Medico4us is not responsible for any loss or damage of any sort user may incur from dealing with any third party. This policy describes how Medico4us collects and handles Doctors/Patients/Customers information to help us serve our services better. Personal information is any information that identifies you, such as your name, address, email address, phone number, and previous use of Medico4us application. By visiting our site/application, you accept the practices outlined in this privacy policy.\n" +
					"COLLECTING INFORMATION\n" +
					"Medico4us collects information when you register with Medico4us or update your details to receive our newsletters, participate in discussions, events, promotions or any upcoming activities, which will be informed to you over time-to-time. When you register with Medico4us, we may ask for information such as your name, phone number, email address, city, state, and zip code. Once you create and sign into your account, Medico4us automatically receives information from your browser, such as your IP address, and cookies. Cookies enable our platform to recognize you as you move throughout our website. Your web browser also allows us to track statistics such as type of browsers on our platform, page views, navigational patterns, and high traffic areas. This information does NOT track personally identifiable information about our users.\n" +
					"\n" +
					"PROTECTING INFORMATION\n" +
					"Occasionally, we work with trusted partners who work on our behalf to improve our services to you. These partners may have access to your personal information after they have agreed to our confidentiality agreement. In the case whereMedico4us is acquired by or merged with another company, Medico4us may disclose your personal information to a purchaser that agrees to abide by the terms and conditions of this privacy policy.\n" +
					"We may disclose information about you if required to do so by law, governmental request, process, or court order or based on our good faith belief that there is suspected fraud or situation involving potential threats to the safety of any person or violation of the Medico4us terms of use.\n" +
					"MARKETING COMMUNICATIONS\n" +
					"In addition to using your personal information to improve and enhance your experience with Medico4us, we use information to better target our marketing to your behavioral preferences. If you create an account on our platform, you will be required to provide your email address & contact number and you may automatically be added to our email list and receive updates. If you do not wish to receive Medico4us updates, emails, you may opt out by clicking on the unsubscribe link found at the bottom of all Medico4us marketing emails. Please be aware that it can take up to 30 business days to remove you from our marketing email lists.\n" +
					"\n" +
					"\n" +
					"\n" +
					"NOTIFICATION OF CHANGES\n" +
					"Medico4us is committed to protecting your privacy and may update this policy from time-to-time. Changes will be updated on our website with the date of the most recent update. We will notify you about significant changes to our privacy policy by sending a notice to the primary email address on your account.\n" +
					"Privacy Policy Changes\n" +
					"Although most changes are likely to be minor, Medico4us may change its Privacy Policy from time to time, and in Medico4us sole discretion. Medico4us encourages visitors to frequently check this page for any changes to its Privacy Policy. Your continued use of this site after any change in this Privacy Policy will constitute your acceptance of such change.\n" +
					"Aggregated Statistics\n" +
					"Medico4us may collect statistics about the behavior of visitors to its website/ application. \n" +
					"Medico4us may display this information publicly or provide it to others. However, Medico4us does not disclose your personally-identifying information.\n";
		}
		else{
			terms = "Welcome to Medico4us!\n" +
					"These terms and conditions outline the rules and regulations for the use of Medico4us Website/Application. \n" +
					"By accessing this website, we assume you accept these terms and conditions in full. Do not continue to use Medico4us application or website if you do not accept all of the terms and conditions stated on this page.\n" +
					"The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and any or all Agreements: \"Client\", “You” and “Your” refers to you, the person accessing this website and accepting the Company’s terms and conditions. \"The Company\", “Ourselves”, “We”, “Our” and \"Us\", refers to our Company. “Party”, “Parties”, or “Us”, refers to both the Client and ourselves, or either the Client or ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our assistance to the Client in the most appropriate manner, whether by formal meetings of a fixed duration, or any other means, for the express purpose of meeting the Client’s needs in respect of provision of the Company’s stated services/products, in accordance with and subject to, prevailing law of India. Any use of the above terminology or other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same.\n" +
					"Cookies\n" +
					"We employ the use of cookies. By using Medico4us website you consent to the use of cookies in accordance with Medico4us privacy policy.\n" +
					"Most of the modern day interactive web sites/ applications use cookies to enable us to retrieve user details for each visit. Cookies are used in some areas of our site to enable the functionality of this area and ease of use for those people visiting. Some of our affiliate / advertising partners may also use cookies.\n" +
					"License\n" +
					"Unless otherwise stated, Medico4us and/or it’s licensors own the intellectual property rights for all material on Medico4us All intellectual property rights are reserved. You may view and/or print pages for your own personal use subject to restrictions set in these terms and conditions.\n" +
					"You must not:\n" +
					"Republish material from Medico4us\n" +
					"Sell, rent or sub-license material fromMedico4us\n" +
					"Reproduce, duplicate or copy material from Medico4us\n" +
					"Redistribute content from Medico4us (unless content is specifically made for redistribution).\n" +
					"\n" +
					"User Comments\n" +
					"This Agreement shall begin on the date hereof.\n" +
					"Certain parts of this application/website offer the opportunity for users to post and exchange opinions, information, material and data ('Comments') in areas of the feedback(website/application). Medico4us does not screen, edit, publish or review Comments prior to their appearance on the website and Comments do not reflect the views or opinions of Medico4us, its agents or affiliates. Comments reflect the view and opinion of the person who posts such view or opinion. To the extent permitted by applicable laws Medico4us shall not be responsible or liable for the Comments or for any loss cost, liability, damages or expenses caused and or suffered as a result of any use of and/or posting of and/or appearance of the Comments on this website/application.\n" +
					"Medico4us reserves the right to monitor all Comments and to remove any Comments which it considers in its absolute discretion to be inappropriate, offensive or otherwise in breach of these Terms and Conditions.\n" +
					"You warrant and represent that: You are entitled to post the Comments on our website and have all necessary licenses and consents to do so;\n" +
					"The Comments do not infringe any intellectual property right, including without limitation copyright, patent or trademark, or other proprietary right of any third party;\n" +
					"The Comments do not contain any defamatory, libelous, offensive, indecent or otherwise unlawful material or material which is an invasion of privacy\n" +
					"The Comments will not be used to solicit or promote business or custom or present commercial activities or unlawful activity.\n" +
					"You hereby grant to Medico4us a non-exclusive royalty-free license to use, reproduce, edit and authorize others to use, reproduce and edit any of your Comments in any and all forms, formats or media.\n" +
					"Hyperlinking to our Content\n" +
					"The following organizations may link to our Web site without prior written approval:\n" +
					"Government agencies;\n" +
					"Search engines;\n" +
					"News organizations;\n" +
					"\n" +
					"Online directory distributors when they list us in the directory may link to our Web site/application in the same manner as they hyperlink to the Web sites of other listed businesses; and System wide Accredited Businesses except soliciting non-profit organizations, charity shopping malls, and charity fundraising groups which may not hyperlink to our Web site.\n" +
					"These organizations may link to our home page, to publications or to other Web site information so long as the link: (a) is not in any way misleading; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and its products or services; and (c) fits within the context of the linking party's site.\n" +
					"We may consider and approve in our sole discretion other link requests from the following types of organizations:\n" +
					"Commonly-known consumer and/or business information sources such as Chambers of Commerce, community sites; associations or other groups representing charities, including charity giving sites, online directory distributors; internet portals; accounting, law and consulting firms whose primary clients are businesses; and trade associations.\n" +
					"We will approve link requests from these organizations if we determine that: (a) the link would not reflect unfavorably on us or our accredited businesses (for example, trade associations or other organizations representing inherently suspect types of business, such as work-at-home opportunities, shall not be allowed to link); (b)the organization does not have an unsatisfactory record with us; (c) the benefit to us from the visibility associated with the hyperlink outweighs the absence of Medico4us; and (d) where the link is in the context of general resource information or is otherwise consistent with editorial content in a newsletter or similar product furthering the mission of the organization.\n" +
					"These organizations may link to our home page, to publications or to other Web site information so long as the link: (a) is not in any way misleading; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and it products or services; and (c) fits within the context of the linking party's site.\n" +
					"Approved organizations may hyperlink to our Web site as follows:\n" +
					"By use of our corporate name; or\n" +
					"By use of the uniform resource locator (Web address) being linked to; or\n" +
					"By use of any other description of our Web site or material being linked to that makes sense within the context and format of content on the linking party's site.\n" +
					"No use of (name)’s logo or other artwork will be allowed for linking absent a trademark license agreement.\n" +
					"\n" +
					"Reservation of Rights\n" +
					"We reserve the right at any time and in its sole discretion to request that you remove all links or any particular link to our Web site/application. You agree to immediately remove all links to our Web site upon such request. We also reserve the right to amend these terms and conditions and its linking policy at any time. By continuing to link to our Web site/application, you agree to be bound to and abide by these linking terms and conditions.\n" +
					"Whilst we endeavor to ensure that the information on this website is correct, we do not warrant its completeness or accuracy; nor do we commit to ensuring that the website remains available or that the material on the website is kept up to date.\n";
		}

		TextView termsTv = (TextView) dialog.findViewById(R.id.tv_terms);
		termsTv.setText(terms);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		dialog.show();
	}


}
