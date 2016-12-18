package bizapps.com.healthforusPatient.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.Model.Login;
import bizapps.com.healthforusPatient.Model.SearchModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.GsonRequest;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    ParkListAdapter mParkListAdapter;
    GridView mGridView;
    TextView mDate;
    Calendar c = Calendar.getInstance();
    ArrayList<String> mDates = new ArrayList<>();
    ImageView imgNext,imgPrev,userImg;
    int selectedDPos = 0;
    ProgressDialog progressDialog;
    RequestQueue mRequestQueue;
    SearchModel.GetData mData;
    TextView docName,docSplz;
    List<String> bookedSlots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        if(getIntent().getStringExtra("GetData")!=null) {
            String myData = getIntent().getStringExtra("GetData");
            Gson gson = new Gson();
            mData = gson.fromJson(myData, SearchModel.GetData.class);
            if (mData != null) {
                //setData();

            }
        }

        userImg = (ImageView) findViewById(R.id.doc_img);

        String url1 = "www.nourl.com";
        if(mData.getImage()!=null && mData.getImage().size()!=0) {
            if (mData.getImage().get(0) != null) {
                url1 = mData.getImage().get(0);
            }
        }


        Picasso.with(this).load(url1).into(userImg);

        BZAppApplication.isFromBooking = true;
        docName = (TextView) findViewById(R.id.doc_name);
        docSplz = (TextView) findViewById(R.id.doc_splz);

        docName.setText(mData.getDr_name());
        docSplz.setText(mData.getSpecialization());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        imgNext = (ImageView) findViewById(R.id.arrow_next);
        imgPrev = (ImageView) findViewById(R.id.arrow_previous);
        imgNext.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        Calendar now = Calendar.getInstance();
        for(int i=0;i<7;i++){
            if(i!=0) {
                now.add(Calendar.DATE, 1);
            }


            if(String.valueOf(c.get(Calendar.MONTH) + 1).length()==1){
                /*if(String.valueOf(c.get(Calendar.DATE)+i).length()==1){
                    mDates.add("0"+(c.get(Calendar.DATE)+i) + "-" +"0"+(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }
                else{
                    mDates.add((c.get(Calendar.DATE)+i) + "-" +"0"+(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }*/
                if(String.valueOf(now.get(Calendar.DATE)).length()==1){
                    mDates.add("0"+(now.get(Calendar.DATE)) + "-" +"0"+(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }
                else{
                    mDates.add((now.get(Calendar.DATE)) + "-" +"0"+(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }
            }
            else{
                /*if(String.valueOf(c.get(Calendar.DATE)+i).length()==1){
                    mDates.add("0"+(c.get(Calendar.DATE)+i) + "-" +(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }
                else{
                    mDates.add((c.get(Calendar.DATE)+i) + "-" +(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }*/

                if(String.valueOf(now.get(Calendar.DATE)).length()==1){
                    mDates.add("0"+(now.get(Calendar.DATE)) + "-" +(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }
                else{
                    mDates.add((now.get(Calendar.DATE)) + "-" +(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                }

            }
        }

        bookedSlots = mData.getDates();


        mDate = (TextView) findViewById(R.id.tv_date);
        mDate.setText(mDates.get(selectedDPos));
        mGridView = (GridView)findViewById(R.id.gridView1);
        mParkListAdapter = new ParkListAdapter(this,50);
        mGridView.setAdapter(mParkListAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<String> mySlots = Arrays.asList(BZAppApplication.bookedSlots);
                if(!mySlots.contains(parent.getItemAtPosition(position).toString())){
                    apiCall(mDate.getText().toString() + " " + parent.getItemAtPosition(position).toString());
                }
                /*for(String s: BZAppApplication.bookedSlots) {
                    if(!(s.equalsIgnoreCase(parent.getItemAtPosition(position).toString()))) {
                        apiCall(mDate.getText().toString() + " " + parent.getItemAtPosition(position).toString());
                    }
                }*/
            }

        });

        setFreeSlots();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.arrow_next:
                ++selectedDPos;
                if(selectedDPos==6){
                    imgNext.setVisibility(View.GONE);
                }
                if(selectedDPos!=6){
                    imgNext.setVisibility(View.VISIBLE);
                }
                if(selectedDPos==0){
                    imgPrev.setVisibility(View.GONE);
                }
                if(selectedDPos!=0){
                    imgPrev.setVisibility(View.VISIBLE);
                }
                mDate.setText("");
                mDate.setText(mDates.get(selectedDPos));
                setFreeSlots();
                break;
            case R.id.arrow_previous:
                --selectedDPos;
                if(selectedDPos==0){
                    imgPrev.setVisibility(View.GONE);
                }
                if(selectedDPos!=0){
                    imgPrev.setVisibility(View.VISIBLE);
                }
                if(selectedDPos==6){
                    imgNext.setVisibility(View.GONE);
                }
                if(selectedDPos!=6){
                    imgNext.setVisibility(View.VISIBLE);
                }
                mDate.setText("");
                mDate.setText(mDates.get(selectedDPos));
                setFreeSlots();
                break;
        }
    }

    public void apiCall(String dateTime){
        displayProgress();
        String url = "http://medico4us.in/appointment.php";
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject mCredentials = new JSONObject();
        Log.e("UserId",""+BZAppApplication.userID);
        Log.e("UserId",""+mData.getGuid());
        try {
            mCredentials.put("userid", BZAppApplication.userID);
            mCredentials.put("datetime_d",dateTime);
            mCredentials.put("docid",mData.getId());
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
                return headers;
            }
        };

        int socketTimeout = 100000;
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
                        showErrorDialog("your appointment is sent to the doctor");
                    }
                    else {
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
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void setFreeSlots(){
        String mySlots = bookedSlots.get(selectedDPos).replaceAll(" ","");
        BZAppApplication.bookedSlots = mySlots.split(",");
        mParkListAdapter.notifyDataSetChanged();
        mGridView.invalidateViews();

    }
}
