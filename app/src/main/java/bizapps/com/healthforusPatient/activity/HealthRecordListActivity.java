package bizapps.com.healthforusPatient.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.Model.HealthRecordListModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.GsonRequest;

public class HealthRecordListActivity extends AppCompatActivity {

    ListView lv_records;
    RequestQueue mRequestQueue;
    ProgressDialog progressDialog;
    List<HealthRecordListModel.HealthRecordList> hrData;
    HealthRecordListModel.HealthRecordList selectedRecord;
    HRAdapter hrAdapter;
    Button upload_hr;
    SharedPreferences mSharedPreferences;
    public static String MyPREFERENCES = "doctorpluspatient";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_health_record_list);
        upload_hr = (Button) findViewById(R.id.btn_records);
        upload_hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthRecordListActivity.this,HealthRecordActivity.class);
                startActivity(intent);
            }
        });
        lv_records = (ListView) findViewById(R.id.lv_hrcords);
        apiCall();
    }

    public void apiCall(){
        displayProgress();
        //String url = "http://doctorapp.rakyow.com/userapp/patientapi/health_record_list";
        String url = "http://medico4us.in/getHealthRecord.php";
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject mCredentials = new JSONObject();

        try {
            mCredentials.put("userid", mSharedPreferences.getString("userID"," "));
                    } catch (JSONException e) {
            e.printStackTrace();
        }

        GsonRequest<HealthRecordListModel> myReq = new GsonRequest<HealthRecordListModel>(
                Request.Method.POST,
                url,
                HealthRecordListModel.class,
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

    private Response.Listener<HealthRecordListModel> createMyReqSuccessListener() {
        return new Response.Listener<HealthRecordListModel>() {
            @Override
            public void onResponse(HealthRecordListModel response) {
                dismissProgress();
                try {
                    if(response.getData().size()!=0){
                        hrData = response.getData();
                        setDataListView();
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

    public void setDataListView(){
        if(hrData.size()!=0){
            lv_records.setVisibility(View.VISIBLE);

            hrAdapter = new HRAdapter(hrData,this);
            lv_records.setAdapter(hrAdapter);

            lv_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    long viewId = view.getId();
                    if (viewId == R.id.img_dl){
                        int pos = i;
                        selectedRecord = (HealthRecordListModel.HealthRecordList) adapterView.getItemAtPosition(pos);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedRecord.getFile_record()));
                        if (browserIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(browserIntent);
                        }
                    }

                }
            });
        }
    }
}
