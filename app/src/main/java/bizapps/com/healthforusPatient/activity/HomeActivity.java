package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.R;

public class HomeActivity extends AppCompatActivity {

    TextView tvSearch,tvHealthfeed,tvHealthrecord,tvLogout,tvAds,tvHealthTip;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    public static String MyPREFERENCES = "doctorpluspatient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        BZAppApplication.userID = mSharedPreferences.getString("userID"," ");
        tvSearch = (TextView) findViewById(R.id.tv_search);
        tvHealthfeed = (TextView) findViewById(R.id.tv_healthfeed);
        tvHealthrecord = (TextView) findViewById(R.id.tv_records);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvAds = (TextView) findViewById(R.id.tv_ads);
        //tvHealthTip = (TextView) findViewById(R.id.tv_healthtip);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        tvHealthrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,HealthRecordListActivity.class);
                startActivity(intent);
            }
        });

        tvHealthfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,BlogListActivity.class);
                startActivity(intent);

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("userID","NA");
                editor.commit();
                Intent up_intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(up_intent);
                finish();
            }
        });

//        tvHealthTip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showErrorDialog("Coming Soon!");
//            }
//        });

        tvAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,AdsActivity.class);
                startActivity(intent);
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
