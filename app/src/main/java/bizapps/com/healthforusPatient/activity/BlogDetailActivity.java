package bizapps.com.healthforusPatient.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.RoundImage;

public class BlogDetailActivity extends AppCompatActivity {

    public String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        Bundle bundle= getIntent().getExtras();
        guid = bundle.getString("guid");
        Log.e("GUID",""+guid);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.temp_img);
        RoundImage roundedImage = new RoundImage(bm);
//        user_image.setImageDrawable(roundedImage);
    }
}
