package bizapps.com.healthforusPatient.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusPatient.Model.BlogModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.GsonRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        stockData();
    }

    void stockData() {
      TextView textView1 = (TextView) findViewById(R.id.ad1);
      textView1.setText(Html.fromHtml(getString(R.string.ad1)));

      TextView textView2 = (TextView) findViewById(R.id.ad2);
      textView2.setText(Html.fromHtml(getString(R.string.ad2)));

      TextView textView3 = (TextView) findViewById(R.id.ad3);
      textView3.setText(Html.fromHtml(getString(R.string.ad3)));

    }

}
