package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import junit.framework.Assert;

import java.util.ArrayList;

import bizapps.com.healthforusPatient.R;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "GalleryActivity";
    public static final String EXTRA_NAME = "images";

    private ArrayList<String> _images;
    private GalleryPagerAdapter _adapter;

    ViewPager _pager;
    LinearLayout _thumbnails;
    ImageButton _closeButton;
//    @InjectView(R.id.pager)
//    ViewPager _pager;
//    @InjectView(R.id.thumbnails) LinearLayout _thumbnails;
//    @InjectView(R.id.btn_close) ImageButton _closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        _pager = (ViewPager) findViewById(R.id.pager);
        _thumbnails = (LinearLayout) findViewById(R.id.thumbnails);
        //_closeButton = (ImageButton) findViewById(R.id.btn_close);

        _images = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_NAME);
        Assert.assertNotNull(_images);

        _adapter = new GalleryPagerAdapter(this);
        _pager.setAdapter(_adapter);
        _pager.setOffscreenPageLimit(6); // how many images to load into memory

//        _closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Close clicked");
//                finish();
//            }
//        });
    }

    @Override
    public void onResume(){
        super.onResume();
//        LayoutInflater mInflater = LayoutInflater.from(this);
//        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
//        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
//        mTitleTextView.setText("Images");
//        ImageButton backBtn = (ImageButton) mCustomView.findViewById(R.id.imageView1);
//        backBtn.setOnClickListener(this);
//        getSupportActionBar().setCustomView(mCustomView);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageView1:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                NavUtils.navigateUpTo(this, upIntent);
                startActivity(upIntent);
                finish();
                Runtime.getRuntime().freeMemory();
                break;
        }
    }

    class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return _images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = _thumbnails.getPaddingTop();
            
            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    _pager.getLayoutParams()).bottomMargin - (borderSize*2);
            
            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);
            
            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Thumbnail clicked");

                    // Set the pager position when thumbnail clicked
                    _pager.setCurrentItem(position);
                }
            });
            _thumbnails.addView(thumbView);

//            final SubsamplingScaleImageView imageView =
//                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image);
           final ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
            // Asynchronously load the image and set the thumbnail and pager view
//            Glide.with(_context)
//                    .load(_images.get(position))
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                            imageView.setImage(ImageSource.bitmap(bitmap));
//                            thumbView.setImageBitmap(bitmap);
//                        }
//                    });
            Picasso.with(_context)
                    .load(_images.get(position))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
            /* Save the bitmap or do something with it here */

                            //Set it in the ImageView
                            imageView.setImageBitmap(bitmap);
                            thumbView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        Runtime.getRuntime().freeMemory();
    }
}
