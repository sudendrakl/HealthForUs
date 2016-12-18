package bizapps.com.healthforusPatient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.Model.SignupModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.FilePath;
import bizapps.com.healthforusPatient.utills.MultipartRequest;

public class HealthRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = HealthRecordActivity.class.getSimpleName();
    private String selectedFilePath;
//    private String SERVER_URL = "http://doctorapp.rakyow.com/userapp/patientapi/health_record";
    private String SERVER_URL = "http://medico4us.in/healthRecord.php";
    ImageView ivAttachment;
    Button bUpload;
    TextView tvFileName;
    ProgressDialog dialog;
    ProgressDialog progressDialog;
    EditText et_desc;
    SharedPreferences mSharedPreferences;
    public static String MyPREFERENCES = "doctorpluspatient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ivAttachment = (ImageView) findViewById(R.id.ivAttachment);
        bUpload = (Button) findViewById(R.id.b_upload);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        ivAttachment.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        et_desc = (EditText) findViewById(R.id.et_desc);
    }

    @Override
    public void onClick(View v) {
        if(v== ivAttachment){

            //on attachment icon click
            showFileChooser();
        }
        if(v== bUpload){

            //on upload button Click
            if(selectedFilePath != null){
                dialog = ProgressDialog.show(HealthRecordActivity.this,"","Uploading File...",true);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //creating new thread to handle Http Operations
                        uploadFile(selectedFilePath);
//                    }
//                }).start();
            }else{
                Toast.makeText(HealthRecordActivity.this,"Please choose a File First", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showFileChooser() {
        //Intent intent = new Intent();
        //sets the select file to all types of files
        /*intent.setType("file*//*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        //starts new activity to select file and return data

//        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//        intent.putExtra("CONTENT_TYPE", "*/*");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvFileName.setText(selectedFilePath);
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //android upload file to server
    public void uploadFile(final String selectedFilePath){
       // displayProgress();
        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dismissProgress();

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
//                }
//            });

        }else{
            displayProgress();
            HashMap<String, String> params = new HashMap<String, String>();
            String myDesc = "No Description";
            if(et_desc.getText().toString().length()>0){
                myDesc = et_desc.getText().toString();
            }
            Log.e("UserID HealthRecord",""+BZAppApplication.userID);
            params.put("userid", BZAppApplication.userID);
            params.put("description", myDesc);

            BZAppApplication.hRecord = true;

            MultipartRequest mr = new MultipartRequest(SERVER_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    dismissProgress();
                    Log.d("response", response);
                    Gson gson = new Gson();
                    SignupModel obj = gson.fromJson(response, SignupModel.class);
                    //Log.e("Status",""+obj.getStatus());
                    dialog.cancel();
                    if(obj.getMessage()!=null) {
                        showErrorDialog(obj.getMessage());
                    }
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissProgress();
//                Log.e("Volley Request Error", error.getLocalizedMessage());
                }

            }, selectedFile, null,null, params,false);

            Log.e("mr", "" + mr.toString());
            mr.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(mr);

        }
        dialog.cancel();
        //dismissProgress();

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
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
