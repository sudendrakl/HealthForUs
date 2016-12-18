package bizapps.com.healthforusPatient.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.Model.Login;
import bizapps.com.healthforusPatient.Model.SearchModel;
import bizapps.com.healthforusPatient.Model.SpecialisationModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.GsonRequest;
import bizapps.com.healthforusPatient.utills.Utility;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    public Login mData;
    TextView tv_location;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    public static String MyPREFERENCES = "doctorpluspatient";
    public static final int CALLED_ACTIVITY = 1;
    ListView lv_doctors;
    RequestQueue mRequestQueue;
    String locName;
    public List<SearchModel.GetData> docData;
    SearchModel.GetData selectedDoc;
    DoctorAdapter doctorAdapter;
    ProgressDialog progressDialog;
    Button upload;
    Spinner spez;
    View view;
    List<SpecialisationModel.GetSpecialisation> mSpez;
    public ArrayList<String> specialisationList,docId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        if(getIntent().getStringExtra("GetData")!=null) {
            String myData = getIntent().getStringExtra("GetData");
            Gson gson = new Gson();
            mData = gson.fromJson(myData, Login.class);
            BZAppApplication.myData = mData;
            if (mData != null) {
                //setData();
                Log.e("mData",""+mData.getUserid());
            }
        }
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_location.setOnClickListener(this);
        lv_doctors = (ListView) findViewById(R.id.lv_doctors);
        spez = (Spinner) findViewById(R.id.spinner_spez);
        tv_location.setText("Current location : \n"+mSharedPreferences.getString("userLocation","Enter Location"));
        //view = (View) findViewById(R.id.view02);
        /*upload = (Button) findViewById(R.id.btn_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("userID","NA");
                editor.commit();
                Intent up_intent = new Intent(SearchActivity.this,LoginActivity.class);
                startActivity(up_intent);
                finish();
            }
        });*/
        if(!mSharedPreferences.getString("userLocation","Enter Location").equalsIgnoreCase("Enter Location")){
            locName = mSharedPreferences.getString("userLocation","Enter Location");
            //apiCall();
            if(mSpez==null){
                apiCallSpez();
            }
            else{
                setSpinnerData();
            }
        }
        else{
            Intent intent = new Intent(SearchActivity.this, PlaceActivity.class);
            startActivityForResult(intent, CALLED_ACTIVITY);
        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CALLED_ACTIVITY){
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                locName = data.getStringExtra("locationDetails");
                editor.putString("userLocation",locName);
                editor.commit();
                BZAppApplication.mLocation = locName;
                Log.d("Location Address", "" + locName);
                tv_location.setText("Current location : \n"+locName);
                //apiCall();
                if(mSpez==null){
                    apiCallSpez();
                }
                else{
                    setSpinnerData();
                }
            }
        }
    }


    public void apiCall(int spezid){
        displayProgress();
        String url = "http://medico4us.in/doctorSearch.php";
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject mCredentials = new JSONObject();
        Log.e("UserID",""+BZAppApplication.userID);
        try {
            mCredentials.put("userid",BZAppApplication.userID);
            mCredentials.put("location",BZAppApplication.mLocation);
            mCredentials.put("spid",docId.get(spez.getSelectedItemPosition()-1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GsonRequest<SearchModel> myReq = new GsonRequest<SearchModel>(
                Request.Method.POST,
                url,
                SearchModel.class,
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

    private Response.Listener<SearchModel> createMyReqSuccessListener() {
        return new Response.Listener<SearchModel>() {
            @Override
            public void onResponse(SearchModel response) {
                dismissProgress();
                try {
                    Log.e("Search Data", "" + response.getData().get(0).getImage());
                } catch(Exception e) {/*ignore*/}
                try {
                    if(response.getStatus().equalsIgnoreCase("success")){
                        docData = response.getData();
                        setDataListView();
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
                dismissProgress();
                String errorMsg = error.getMessage();

                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG)
                        .show();

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_location:
                Intent intent = new Intent(SearchActivity.this, PlaceActivity.class);
                startActivityForResult(intent, CALLED_ACTIVITY);
                break;
        }
    }

    public void setDataListView(){
        if(docData.size()!=0){
            Log.e("Search Activity",""+docData);
            lv_doctors.setVisibility(View.VISIBLE);
            doctorAdapter = new DoctorAdapter(docData,this);
            lv_doctors.setAdapter(doctorAdapter);
            Utility.setListViewHeightBasedOnChildren(lv_doctors);
//            view.setVisibility(View.VISIBLE);
            lv_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    long viewId = view.getId();

                    selectedDoc = (SearchModel.GetData) adapterView.getItemAtPosition(i);

                    Gson gson = new Gson();
                    String myData = gson.toJson(selectedDoc);

                    if(viewId==R.id.btn_call){
                        String docNum = "tel:"+selectedDoc.getContact();
                        Intent dial = new Intent(Intent.ACTION_DIAL,Uri.parse(docNum));
//                        dial.setAction("android.intent.action.CALL");
//                        dial.setData(Uri.parse(docNum));

                        if(selectedDoc.getContact()!=null) {
                            startActivity(dial);
                        }
                    }
                    else if(viewId==R.id.btn_book){
                        Intent intent = new Intent(SearchActivity.this, BookingActivity.class);
//                        Gson gson1 = new Gson();
//                        String myData1 = gson1.toJson(selectedDoc);
                        intent.putExtra("GetData",myData);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(SearchActivity.this,MainActivityNew.class);
                        intent.putExtra("GetData",myData);
                        startActivity(intent);
                    }

                }
            });
        }
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

    @Override
    public void onResume(){
        super.onResume();
        if(BZAppApplication.isFromBooking){
            BZAppApplication.isFromBooking = false;
            apiCall(BZAppApplication.selectedSpez);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void apiCallSpez(){
        displayProgress();
        String url = "http://medico4us.in/specialisation.php";
        mRequestQueue = Volley.newRequestQueue(this);
        /*JSONObject mCredentials = new JSONObject();
        Log.e("UserID",""+BZAppApplication.userID);
        try {
            mCredentials.put("userid",BZAppApplication.userID);
            mCredentials.put("location",BZAppApplication.mLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        GsonRequest<SpecialisationModel> myReq = new GsonRequest<SpecialisationModel>(
                Request.Method.GET,
                url,
                SpecialisationModel.class,
                createMyReqSuccessListener1(),
                createMyReqErrorListener1()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //headers.put("userId",mSharedPreferences.getString("SoBeerID", " "));
                return headers;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


//        int socketTimeout = 40000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        myReq.setRetryPolicy(policy);

        Log.d("myReq", "" + myReq);
        mRequestQueue.add(myReq);

    }

    private Response.Listener<SpecialisationModel> createMyReqSuccessListener1() {
        return new Response.Listener<SpecialisationModel>() {
            @Override
            public void onResponse(SpecialisationModel response) {
                dismissProgress();
                try {
                    if(response.getStatus().equalsIgnoreCase("success")){
                        mSpez = response.getData();
                        setSpinnerData();
//                        setDataListView();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }

    private Response.ErrorListener createMyReqErrorListener1() {
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

    public void setSpinnerData(){
        specialisationList = new ArrayList<String>();
        docId = new ArrayList<String>();
        specialisationList.add("--Specialization--");
        if(mSpez.size()>0){

            for(int i=0;i<mSpez.size();i++){
                specialisationList.add(mSpez.get(i).getName());
                        docId.add(mSpez.get(i).getId());
            }

        }

        ArrayAdapter<String> data = new ArrayAdapter<String>(
                this, R.layout.spinner_layout, specialisationList);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spez.setAdapter(data);

        spez
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        if (!specialisationList.get(arg2).equalsIgnoreCase("--Specialization--")) {
                            BZAppApplication.selectedSpez = arg2;
                            apiCall(arg2);


                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

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

}