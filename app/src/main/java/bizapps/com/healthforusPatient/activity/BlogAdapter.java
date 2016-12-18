package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bizapps.com.healthforusPatient.Model.BlogModel;
import bizapps.com.healthforusPatient.R;

/**
 * Created by venkatat on 7/31/2016.
 */
public class BlogAdapter extends BaseAdapter {

    public List<BlogModel.BlogData> mList;
    Context mCtx;

    public BlogAdapter(List<BlogModel.BlogData> mList, Context mCtx){
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

        TextView mDocName,mTitle,mDesc;
        ImageView mImage1,mImage2;
        Button mBook;
//        HorizontalScrollView hScrollView;
//        LinearLayout hLayout;

    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        viewHolder holder;
        if(convertView==null){
            convertView =   LayoutInflater.from(mCtx).inflate(R.layout.layout_list_blog, null);
            holder = new viewHolder();
            holder.mDocName = (TextView)convertView.findViewById(R.id.tv_blogdrname);
            holder.mTitle = (TextView)convertView.findViewById(R.id.tv_blogname);
            holder.mDesc = (TextView) convertView.findViewById(R.id.tv_blogdescription);
            holder.mImage1 = (ImageView) convertView.findViewById(R.id.img_blog);
            convertView.setTag(holder);
        }
        else
        {
            holder = (viewHolder)convertView.getTag();
        }


        holder.mDocName.setText(mList.get(i).getDr_name());
        holder.mTitle.setText(mList.get(i).getTitle());
        holder.mDesc.setText(mList.get(i).getDescription());
        Picasso.with(mCtx).load(mList.get(i).getFile_record()).placeholder(R.drawable.images_ph).into(holder.mImage1);



        return convertView;
    }
}


