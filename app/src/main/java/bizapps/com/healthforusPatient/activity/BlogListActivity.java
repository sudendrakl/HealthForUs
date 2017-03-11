package bizapps.com.healthforusPatient.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
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

public class BlogListActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog progressDialog;
    RequestQueue mRequestQueue;
    List<BlogModel.BlogData> blogDataList;
    ListView blogListView;
    BlogAdapter blogAdapter;

    int maxLines = 5;
    String more = "...more\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        blogListView = (ListView) findViewById(R.id.lv_blog);
        //apiCall();
        stockData();
    }

    void stockData() {
        TextView textView1 = (TextView) findViewById(R.id.different_colors_indicate_different_nutrients);
        setLayoutListner(textView1);
        textView1.setOnClickListener(this);
        textView1.setTag(R.id.id2, getString(R.string.different_colors_indicate_different_nutrients));

        TextView textView2 = (TextView) findViewById(R.id.enhance_your_brain_power_with_kapalabhati_yoga);
        setLayoutListner(textView2);
        textView2.setOnClickListener(this);
        textView2.setTag(R.id.id2, getString(R.string.enhance_your_brain_power_with_kapalabhati_yoga));

        TextView textView3 = (TextView) findViewById(R.id.good_sleep_is_a_key_to_happy_life);
        setLayoutListner(textView3);
        textView3.setOnClickListener(this);
        textView3.setTag(R.id.id2, getString(R.string.good_sleep_is_a_key_to_happy_life));

        TextView textView4 = (TextView) findViewById(R.id.what_are_the_benefits_of_barefoot_training);
        setLayoutListner(textView4);
        textView4.setOnClickListener(this);
        textView4.setTag(R.id.id2, getString(R.string.what_are_the_benefits_of_barefoot_training));

        TextView textView5 = (TextView) findViewById(R.id.why_breakfast_is_crucial_for_us);
        setLayoutListner(textView5);
        textView5.setOnClickListener(this);
        textView5.setTag(R.id.id2, getString(R.string.why_breakfast_is_crucial_for_us));
    }

    private void setLayoutListner(final TextView textView) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                final Layout layout = textView.getLayout();

                // Loop over all the lines and do whatever you need with
                // the width of the line
                int end = 0, line1 = 0;
                for (int i = 0; i < maxLines; i++) {
                    if (i == 0) line1 = layout.getLineVisibleEnd(0);
                    end += layout.getLineEnd(0);
                }
                textView.setTag(R.id.id1, true); //initially expanded as nothing done to textview
                textView.setTag(R.id.id3, end);
                textView.setTag(R.id.id4, line1);
                toggleAndSetText(textView);
            }
        });
    }

    private void toggleAndSetText(final TextView tv) {
        tv.postDelayed(new Runnable() {
            @Override public void run() {
                boolean isExpanded = (boolean) tv.getTag(R.id.id1);
                String text = (String) tv.getTag(R.id.id2);
                int end = (int) tv.getTag(R.id.id3);
                int line1 = (int) tv.getTag(R.id.id4);
                if (!isExpanded) {
                    SpannableString content = new SpannableString(text);
                    content.setSpan(new StyleSpan(Typeface.BOLD), 0, line1, 0);
                    content.setSpan(new StyleSpan(Typeface.NORMAL), line1 + 1, content.length(), 0);
                    tv.setText(content);
                } else {
                    SpannableString content = new SpannableString(text.subSequence(0, end - more.length()) + more);
                    content.setSpan(new StyleSpan(Typeface.BOLD), 0, line1, 0);
                    content.setSpan(new StyleSpan(Typeface.NORMAL), line1 + 1, end - more.length(), 0);
                    content.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), end - more.length(), content.length(), 0);
                    content.setSpan(new ForegroundColorSpan(Color.GRAY), end - more.length(), content.length(), 0);
                    tv.setText(content);
                }
                tv.setTag(R.id.id1, !isExpanded);
            }
        }, 50);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: share what
        if (item.getItemId() == R.id.menu_share) {
            //Intent sendIntent = new Intent();
            //sendIntent.setAction(Intent.ACTION_SEND);
            //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            //sendIntent.setType("text/plain");
            //startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onClick(View view) {
        toggleAndSetText((TextView) view);
    }

    public void apiCall(){
        displayProgress();
        String url = "http://doctorapp.rakyow.com/userapp/blog/lists";
        mRequestQueue = Volley.newRequestQueue(this);
        /*JSONObject mCredentials = new JSONObject();

        try {
            mCredentials.put("userid", BZAppApplication.userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        GsonRequest<BlogModel> myReq = new GsonRequest<BlogModel>(
                Request.Method.GET,
                url,
                BlogModel.class,
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

    private Response.Listener<BlogModel> createMyReqSuccessListener() {
        return new Response.Listener<BlogModel>() {
            @Override
            public void onResponse(BlogModel response) {
                dismissProgress();
                try {
                    if(response.getData().size()!=0){
                        blogDataList = response.getData();
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
        if(blogDataList.size()!=0){
            //blogListView.setVisibility(View.VISIBLE);

            blogAdapter = new BlogAdapter(blogDataList,this);
            blogListView.setAdapter(blogAdapter);

            /*blogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        BlogModel.BlogData selectedRecord = (BlogModel.BlogData) adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(BlogListActivity.this,BlogDetailActivity.class);
                        intent.putExtra("guid",selectedRecord.getGuid());
                        startActivity(intent);

                }
            });*/
        }
    }

}
