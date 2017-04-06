package bizapps.com.healthforusPatient.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.Model.RatingModel;
import bizapps.com.healthforusPatient.Model.SearchModel;
import bizapps.com.healthforusPatient.R;
import bizapps.com.healthforusPatient.utills.ExpandableHeightListView;
import bizapps.com.healthforusPatient.utills.GravityCompoundDrawable;
import bizapps.com.healthforusPatient.utills.GsonRequest;


public class MainActivityNew extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    Button mBook,mFeedback;
    SearchModel.GetData mData;
    HorizontalScrollView hScrollView;
    LinearLayout hLayout;
    List<String> imagesMenu;
    final Context context = this;
    ArrayList<String> imagesDoc;
    TextView tvExp,tvFee,tvSpecialities,tvLeadership,tvFellowship,tvCertifications,tvAddress
            ,tvDesc,tvEmergency,tvPaycard,tvHomevisit,tvHealthtip,tvSpecialisation,tvRewards,tvMob,tvWeb
            ,tvFb,tvMail,tvLinkedin,tvVideo,tvReview,tvDesignation;
    TextView tvCategory,tvAmbulance,tvCertificationno,tvOwnership,tvDoctors,tvTimings,tvFacilities,tvAffiliation;
    View view01,view02,view03,view04,view05,view06,view07,view08,view09;
    ExpandableHeightListView lv_review;
    ReviewAdapter reviewAdapter;
    LinearLayout llExp,llFee,llFacility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.layout_hospital);

        if (getIntent().getStringExtra("GetData") != null) {
            String myData = getIntent().getStringExtra("GetData");
            Gson gson = new Gson();
            mData = gson.fromJson(myData, SearchModel.GetData.class);
            if (mData != null) {
                //setData();
                Log.e("mData", "" + mData.getGuid());
                BZAppApplication.DocID = mData.getGuid();
            }
        }

        view03 = (View) findViewById(R.id.view_01);
        view04 = (View) findViewById(R.id.view_02);
        view05 = (View) findViewById(R.id.view_05);
        view06 = (View) findViewById(R.id.view_06);
        view07 = (View) findViewById(R.id.view_07);
        view08 = (View) findViewById(R.id.view_08);
        view09 = (View) findViewById(R.id.view_09);
        llExp = (LinearLayout) findViewById(R.id.ll_exp);
        llFee = (LinearLayout) findViewById(R.id.ll_fee);
        tvDesignation = (TextView) findViewById(R.id.tv_designation);
        llFacility = (LinearLayout) findViewById(R.id.ll_facility);

        hScrollView = (HorizontalScrollView) findViewById(R.id.hScroll);
        hLayout = (LinearLayout) findViewById(R.id.thumbnails);
        mBook = (Button) findViewById(R.id.btn_book);
        mBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityNew.this, BookingActivity.class);
                Gson gson = new Gson();
                String myData = gson.toJson(mData);
                intent.putExtra("GetData", myData);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mFeedback = (Button) findViewById(R.id.btn_feedback);
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating();
            }
        });
        tvReview = (TextView) findViewById(R.id.tv_review);
        lv_review = (ExpandableHeightListView) findViewById(R.id.lv_review);
        view01 = (View) findViewById(R.id.view_14);
        view02 = (View) findViewById(R.id.view_15);

        tvFb = (TextView) findViewById(R.id.tv_fb);
        tvVideo = (TextView) findViewById(R.id.tv_video);
        tvLinkedin = (TextView) findViewById(R.id.tv_linkedin);
        tvMail = (TextView) findViewById(R.id.tv_mail);
        tvMob = (TextView) findViewById(R.id.tv_mob);
        tvWeb = (TextView) findViewById(R.id.tv_web);
        tvExp = (TextView) findViewById(R.id.tv_exp);
        tvAffiliation = (TextView) findViewById(R.id.tv_affiliation);
        tvCertifications = (TextView) findViewById(R.id.tv_certifications);
        tvFee = (TextView) findViewById(R.id.tv_fee);
        tvFellowship = (TextView) findViewById(R.id.tv_fellowship);
        tvLeadership = (TextView) findViewById(R.id.tv_leadership);
        tvSpecialities = (TextView) findViewById(R.id.tv_specialities);
        tvAddress = (TextView) findViewById(R.id.tv_docaddr);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvEmergency = (TextView) findViewById(R.id.tv_emergency);
        tvHealthtip = (TextView) findViewById(R.id.tv_healthtip);
        tvHomevisit = (TextView) findViewById(R.id.tv_homevisit);
        tvPaycard = (TextView) findViewById(R.id.tv_paycard);
        tvSpecialisation = (TextView) findViewById(R.id.tv_specialisation);
        tvRewards = (TextView) findViewById(R.id.tv_rewards);

        if (mData.getFacebook() != null && !mData.getFacebook().equalsIgnoreCase("")) {
            tvFb.setText(mData.getFacebook());
            tvFb.setVisibility(View.VISIBLE);
        } else {
            tvFb.setVisibility(View.GONE);
        }

        if (mData.getLinkin() != null && !mData.getLinkin().equalsIgnoreCase("")) {
            tvLinkedin.setText(mData.getLinkin());
            tvLinkedin.setVisibility(View.VISIBLE);
        } else {
            tvLinkedin.setVisibility(View.GONE);
        }

        tvMob.setText(mData.getContact());
        tvMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docNum = "tel:" + mData.getMobile();
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(docNum));

                if (mData.getMobile() != null) {
                    startActivity(dial);
                }
            }
        });

        tvWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mData.getWebsite();
                Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (url != null) {
                    startActivity(implicit);
                }
            }
        });
        tvVideo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video, 0, 0, 0);
        tvVideo.setText(mData.getVideo());
        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mData.getVideo();
                Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (url != null) {
                    startActivity(implicit);
                }
            }
        });

        tvWeb.setText(mData.getWebsite());
        tvRewards.setText("Rewards received : \n" + mData.getReceived_award());
        tvSpecialisation.setText("Specialization : " + mData.getSpecialization());
        tvAddress.setText(mData.getAddress());
        String exp = "NA", fee = "NA";
        if (mData.getExperence() != null && !mData.getExperence().equalsIgnoreCase("")) {
            exp = mData.getExperence() + " years";
        }
        if (mData.getConsultation_fee() != null && !mData.getConsultation_fee().equalsIgnoreCase("")) {
            fee = mData.getConsultation_fee() + " Rs.";
        }

        tvExp.setText("Experience: " + exp);
        tvAffiliation.setText("Affiliation: " + mData.getHospital_affiliation());
        tvAffiliation.setVisibility(View.GONE);
        tvCertifications.setText("Certifications: " + mData.getCertifications());
        tvFee.setText("Consultation fee: " + fee);
        tvFellowship.setText("Fellowship: " + mData.getFellowship());
        tvLeadership.setText("Leadership: " + mData.getLeadership());
        tvSpecialities.setText("Specialities :" + mData.getSpecialties());
        tvDesc.setText("Description : " + mData.getDescription());
        tvEmergency.setText("Emergency");
        tvHealthtip.setText("Health Tip");
        tvHomevisit.setText("Home Visit");
        tvPaycard.setText("Paycard");
        if(mData.getType()!=null){
            if(mData.getType().equalsIgnoreCase("Doctor")){
                if (mData.getPaycard() != null && mData.getPaycard().equalsIgnoreCase("yes")) {
                    tvPaycard.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_payment_card, 0, 0, 0);
                } else {
                    tvPaycard.setVisibility(View.GONE);
                    tvPaycard.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_payment_card, 0, 0, 0);
                }

                if (mData.getHealthtip() != null && mData.getHealthtip().equalsIgnoreCase("yes")) {
                    tvHealthtip.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_health_tips, 0, 0, 0);
                } else {
                    tvHealthtip.setVisibility(View.GONE);
                    tvHealthtip.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_health_tips, 0, 0, 0);
                }

                if (mData.getHomevisit() != null && mData.getHomevisit().equalsIgnoreCase("yes")) {
                    tvHomevisit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_visit, 0, 0, 0);
                } else {
                    tvHomevisit.setVisibility(View.GONE);
                    tvHomevisit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_visit, 0, 0, 0);
                }

                if (mData.getEmergency() != null && mData.getEmergency().equalsIgnoreCase("yes")) {
                    tvEmergency.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_immergency_visit, 0, 0, 0);
                } else {
                    tvEmergency.setVisibility(View.GONE);
                    tvEmergency.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_immergency_visit, 0, 0, 0);
                }
            }
            else{
                tvPaycard.setVisibility(View.GONE);
                tvHealthtip.setVisibility(View.GONE);
                tvHomevisit.setVisibility(View.GONE);
                tvEmergency.setVisibility(View.GONE);
            }
        }


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded);
        collapsingToolbarLayout.setTitle(mData.getDr_name());
        tvDesignation.setText("Designation : " + mData.getDesignation());

        dynamicToolbarColor();

        toolbarTextAppernce();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(12.957932, 77.745316)).title("Hospital"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.957932, 77.745316), 15));

                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
            }
        });
        MapsInitializer.initialize(this);

        if(mData.getImage()!=null) {
            imagesMenu = mData.getImage();
            setDoctorImages();
        }

        tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String myData = gson.toJson(mData);
                Intent intent = new Intent(MainActivityNew.this, ReviewsActivity.class);
                intent.putExtra("GetData", myData);
                startActivity(intent);
            }
        });
        if (mData.getReview() != null && mData.getReview().size() != 0) {
            reviewAdapter = new ReviewAdapter(mData.getReview(), this);
            lv_review.setAdapter(reviewAdapter);
            lv_review.setExpanded(true);
        } else {
            tvReview.setVisibility(View.GONE);
            view01.setVisibility(View.GONE);
            view02.setVisibility(View.GONE);
            lv_review.setVisibility(View.GONE);
        }

        if(mData.getType()!=null){
        if (mData.getType().equalsIgnoreCase("Hospital")) {
            tvDesignation.setVisibility(View.GONE);
            view03.setVisibility(View.GONE);
            view04.setVisibility(View.GONE);
            view05.setVisibility(View.GONE);
            view06.setVisibility(View.GONE);
            view07.setVisibility(View.GONE);
            view08.setVisibility(View.GONE);
            view09.setVisibility(View.GONE);
            tvSpecialities.setVisibility(View.GONE);
            tvAffiliation.setVisibility(View.GONE);
            tvLeadership.setVisibility(View.GONE);
            tvFellowship.setVisibility(View.GONE);
            tvCertifications.setVisibility(View.GONE);
            llExp.setVisibility(View.GONE);
            llFee.setVisibility(View.GONE);
            tvCategory = (TextView) findViewById(R.id.tv_hcategory);
            tvAmbulance = (TextView) findViewById(R.id.tv_ambulance);
            tvCertificationno = (TextView) findViewById(R.id.tv_certification);
            tvOwnership = (TextView) findViewById(R.id.tv_ownership);
            tvDoctors = (TextView) findViewById(R.id.tv_doctors);
            tvTimings = (TextView) findViewById(R.id.tv_timing);
            tvFacilities = (TextView) findViewById(R.id.tv_facilities);
            tvAffiliation = (TextView) findViewById(R.id.tv_hospitalAff);

            tvCategory.setVisibility(View.GONE);
            tvAmbulance.setVisibility(View.GONE);
            tvCertificationno.setVisibility(View.GONE);
            tvOwnership.setVisibility(View.GONE);
            tvDoctors.setVisibility(View.VISIBLE);
            tvTimings.setVisibility(View.VISIBLE);
            tvFacilities.setVisibility(View.VISIBLE);
            tvAffiliation.setVisibility(View.GONE);

            tvCategory.setText("Hospital Category : " + mData.getCategory());
            tvAmbulance.setText(mData.getAmbulace_no());
            tvCertificationno.setText("Certification number : " + mData.getCertification_no());
            if (mData.getOur_doctor() != null) {
                String doctors = mData.getOur_doctor().replaceAll(",", "\n");
                Log.e("Doctors",""+doctors);
                tvDoctors.setText("Our Doctors : \n" + doctors);
            }

            Drawable innerDrawableDoc = (Drawable) getResources().getDrawable(R.drawable.ic_ourdoctors);
            GravityCompoundDrawable gravityDrawableDoc = new GravityCompoundDrawable(innerDrawableDoc);
            innerDrawableDoc.setBounds(0, 0, innerDrawableDoc.getIntrinsicWidth(), innerDrawableDoc.getIntrinsicHeight());
            gravityDrawableDoc.setBounds(0, 0, innerDrawableDoc.getIntrinsicWidth(), innerDrawableDoc.getIntrinsicHeight());
            tvDoctors.setCompoundDrawables(gravityDrawableDoc, null, null, null);
            if (mData.getOpt_timing() != null) {
                String timing = mData.getOpt_timing().toUpperCase();
                Log.e("Timing", "" + timing);
                tvTimings.setText("OPD timings : " + timing);
            }
            String[] temp = mData.getFacilities().split(",");
//            tvFacilities.setText("Facilities : \n" + temp);
            tvFacilities.setText("Facilities :");
            Drawable innerDrawable = (Drawable) getResources().getDrawable(R.drawable.ic_facility);
            GravityCompoundDrawable gravityDrawable = new GravityCompoundDrawable(innerDrawable);
            innerDrawable.setBounds(0, 0, innerDrawable.getIntrinsicWidth(), innerDrawable.getIntrinsicHeight());
            gravityDrawable.setBounds(0, 0, innerDrawable.getIntrinsicWidth(), innerDrawable.getIntrinsicHeight());
            //tvFacilities.setCompoundDrawables(gravityDrawable, null, null, null);
            tvAffiliation.setText("Affiliated to : " + mData.getAffiliated_by());
            tvAffiliation.setVisibility(View.GONE);
            for(String s : temp){
                String s1 = s;
                s1 = s1.substring(0,1).toUpperCase() + s1.substring(1).toLowerCase();
                if(s.equalsIgnoreCase("24 hour open")){
                    s = "hour open";
                }
                String mTemp = s.replaceAll("\\s","_");
                mTemp = mTemp.toLowerCase();
                int  imageName = getResourceString(mTemp,this);
                if(imageName!=0) {
                    TextView tv = new TextView(this);
                    tv.setText(s1);
                    tv.setCompoundDrawablesWithIntrinsicBounds(imageName, 0, 0, 0);
                    tv.setTextSize(16);
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    llFacility.addView(tv);
                }
            }
            llFacility.setVisibility(View.VISIBLE);

        }
    }

    }

    public static int getResourceString(String name, Context context) {

        int nameResourceID = 0;
        try {
            nameResourceID = R.drawable.class.getField(name).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
//        if (nameResourceID == 0) {
//            throw new IllegalArgumentException(
//                    "No resource string found with name " + name);
//        } else {
            return nameResourceID;
//        }
    }

    public void setDoctorImages() {
        hScrollView.setVisibility(View.VISIBLE);
        imagesDoc = new ArrayList<>(imagesMenu);
        if (imagesDoc.size() != 0) {
            hLayout.removeAllViews();
            for (String mPath : imagesDoc) {
                int borderSize = hLayout.getPaddingTop();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 200);
                params.setMargins(20, 0, 20, 0);
                ImageView thumbView = new ImageView(this);
                thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                thumbView.setLayoutParams(params);
                hLayout.addView(thumbView);
                Picasso.with(this).load(mPath).into(thumbView);
                thumbView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivityNew.this, GalleryActivity.class);
                        intent.putStringArrayListExtra(GalleryActivity.EXTRA_NAME, imagesDoc);
                        startActivity(intent);
                    }
                });
            }

        }
    }

    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.profile_pic);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
            }
        });
    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }



    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            startShare();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startShare() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        String shareLink;
        try {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            shareLink = "market://details?id=" + appPackageName;
            shareLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        } catch (android.content.ActivityNotFoundException anfe) {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            shareLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String text =  mData.getDr_name();
        text += "\r\n" + mData.getDesignation();
        if(!TextUtils.isEmpty(mData.getExperence()))
            text += "\r\n"+mData.getExperence() + " yrs";
        text += "\r\n" + mData.getSpecialization();
        text += "\r\n"+ shareLink;

        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void setRating(){
        final Dialog rankDialog = new Dialog(context);
        rankDialog.setContentView(R.layout.rating_layout);
        rankDialog.setCancelable(true);
        rankDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        rankDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        final EditText review = (EditText) rankDialog.findViewById(R.id.et_comment);

        WindowManager manager  = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        int width,height;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                manager.getDefaultDisplay().getSize(point);
            }
            width = point.x;
            height = point.y;
        }

        lp.copyFrom(rankDialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        rankDialog.getWindow().setAttributes(lp);

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("User_Rating",""+ratingBar.getRating());
                Log.e("User Review",""+review.getText());
                ratingApi(ratingBar.getRating(),review.getText().toString());
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rankDialog.show();
    }

    public void ratingApi(float rating, String review){
        RequestQueue mRequestQueue;
        //pBar.setVisibility(View.VISIBLE);
        String url = "http://medico4us.in/feedback.php";
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject quantity = new JSONObject();
        try {
            quantity.put("userid",BZAppApplication.userID);
            quantity.put("docid",mData.getId());
            quantity.put("rating",Math.round(rating));
            quantity.put("comment",review);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GsonRequest<RatingModel> myReq = new GsonRequest<RatingModel>(
                Request.Method.POST,
                url,
                RatingModel.class,
                quantity.toString(),
                null,
                createMyReqSuccessListener2(),
                createMyReqErrorListener2()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                // do not add anything here
                return headers;
            }
        };

        Log.d("myReq", "" + myReq);
        mRequestQueue.add(myReq);

    }

    private Response.Listener<RatingModel> createMyReqSuccessListener2() {
        return new Response.Listener<RatingModel>() {
            @Override
            public void onResponse(RatingModel response) {
                try {
                    //tview.setText(response.getFirstName().toString());
                    Log.e("Json Response", response.toString());
                    if(response!=null){
                        Toast.makeText(getApplicationContext(),response.getStatus(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener2() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = error.getMessage();
                //pBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG)
                        .show();

            }
        };
    }



}
