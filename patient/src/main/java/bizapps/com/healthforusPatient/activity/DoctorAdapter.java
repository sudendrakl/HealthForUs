package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bizapps.com.healthforusPatient.Model.SearchModel;
import bizapps.com.healthforusPatient.R;

/**
 * Created by venkatat on 7/3/2016.
 */
public class DoctorAdapter extends BaseAdapter {

    public List<SearchModel.GetData> mList;
    Context mCtx;

    public DoctorAdapter(List<SearchModel.GetData> mList,Context mCtx){
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class viewHolder{

        TextView mDocName,mLocation,mHospital,mExtras;
        ImageView mImage1,mImage2;
        Button mBook,mCall;
        RatingBar ratingBar;
//        HorizontalScrollView hScrollView;
//        LinearLayout hLayout;

    }

    @Override
    public View getView(int i, View convertView, final ViewGroup viewGroup) {
        viewHolder holder;
        if(convertView==null){
            convertView =   LayoutInflater.from(mCtx).inflate(R.layout.doctor_layout, null);
            holder = new viewHolder();
            holder.mDocName = (TextView)convertView.findViewById(R.id.doctor_name);
            holder.mLocation = (TextView)convertView.findViewById(R.id.doctor_location);
            holder.mHospital = (TextView) convertView.findViewById(R.id.doctor_clinic);
            holder.mBook = (Button) convertView.findViewById(R.id.btn_book);
            holder.mExtras = (TextView) convertView.findViewById(R.id.doctor_extras);
            holder.mCall = (Button) convertView.findViewById(R.id.btn_call);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.mImage1 = (ImageView) convertView.findViewById(R.id.img_001);
            holder.mImage2 = (ImageView) convertView.findViewById(R.id.img_002);
            convertView.setTag(holder);
        }
        else
        {
            holder = (viewHolder)convertView.getTag();
        }

        /*if((i%2)==0){
            convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.lightblue));
        }
        else{
            convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.white));
        }*/
        final int pos = i;
        holder.mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView)viewGroup).performItemClick(v, pos, 0);
            }
        });

        holder.mBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView)viewGroup).performItemClick(v, pos, 0);
            }
        });
        Log.e("Adapter name",""+mList.get(i).getName());
        holder.mDocName.setText(mList.get(i).getDr_name());
        holder.mLocation.setText(mList.get(i).getLocation());
        holder.mHospital.setText(mList.get(i).getAssocialted_hospitals());
        String exp = "NA";
        String fee = "NA";
        if(mList.get(i).getExperence()!=null && !mList.get(i).getExperence().equalsIgnoreCase("")) {
            exp = mList.get(i).getExperence();
        }
        if(mList.get(i).getConsultation_fee()!=null && !mList.get(i).getConsultation_fee().equalsIgnoreCase("")) {
            fee = mList.get(i).getConsultation_fee();
        }
        if(mList.get(i).getType().equalsIgnoreCase("doctor")) {
            holder.mExtras.setVisibility(View.VISIBLE);
            holder.mExtras.setText(exp + " yrs exp \u2022 Rs." + fee);
        }
        else{
            holder.mExtras.setVisibility(View.GONE);
        }
        List<String> urls = mList.get(i).getImage();
        String url1 = "www.nourl.com";
        String url2 = "www.nourl.com";

        if(mList.get(i).getImage()!=null && mList.get(i).getImage().size()!=0) {
            if(urls.get(0) != null) {
                if (!urls.get(0).equalsIgnoreCase("")) {
                    url1 = urls.get(0);
                }
            }
            if(urls.size()>1) {
                if (!urls.get(1).equalsIgnoreCase("")) {
                    url2 = urls.get(1);
                }
            }
        }
        Picasso.with(mCtx).load(url1).placeholder(R.drawable.images_ph).into(holder.mImage1);
        Picasso.with(mCtx).load(url2).placeholder(R.drawable.images_ph).into(holder.mImage2);
        int rating = mList.get(i).getRating();
        holder.ratingBar.setNumStars(rating);
        /*if (mList.get(i).getImage().size() != 0) {
            //holder.hLayout.removeAllViews();
            for (String mPath : mList.get(i).getImage()) {
                int borderSize = holder.hLayout.getPaddingTop();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 200);
                params.setMargins(20, 0, 20, 0);
                ImageView thumbView = new ImageView(mCtx);
                thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                thumbView.setLayoutParams(params);
                holder.hLayout.addView(thumbView);
                Picasso.with(mCtx).load(mPath).placeholder(R.drawable.images_ph).into(thumbView);
//                thumbView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(EventActivity.this, GalleryActivity.class);
//                        intent.putStringArrayListExtra(GalleryActivity.EXTRA_NAME, imagesMenu);
//                        startActivity(intent);
//                    }
//                });
            }

        }*/

        return convertView;
    }
}

