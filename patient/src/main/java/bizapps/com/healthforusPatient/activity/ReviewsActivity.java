package bizapps.com.healthforusPatient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.Model.SearchModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.ExpandableHeightListView;

public class ReviewsActivity extends AppCompatActivity {

    ExpandableHeightListView lv_review;
    ReviewAdapter reviewAdapter;
    TextView reviews;
    SearchModel.GetData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        if(getIntent().getStringExtra("GetData")!=null) {
            String myData = getIntent().getStringExtra("GetData");
            Gson gson = new Gson();
            mData = gson.fromJson(myData, SearchModel.GetData.class);
            if (mData != null) {
                //setData();
                Log.e("mData",""+mData.getGuid());
                BZAppApplication.DocID = mData.getGuid();
            }
        }

        reviews = (TextView) findViewById(R.id.tv_reviews);
        lv_review = (ExpandableHeightListView) findViewById(R.id.lv_review);

        if(mData.getReview()!=null && mData.getReview().size()!=0){
            lv_review.setVisibility(View.VISIBLE);
            reviewAdapter = new ReviewAdapter(mData.getReview(),this);
            lv_review.setAdapter(reviewAdapter);
            lv_review.setExpanded(true);
        }
        else{
            reviews.setVisibility(View.VISIBLE);
        }
    }
}
