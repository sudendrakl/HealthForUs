package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import bizapps.com.healthforusPatient.Model.HealthRecordListModel;
import bizapps.com.healthforusPatient.R;

/**
 * Created by venkatat on 7/22/2016.
 */
public class HRAdapter extends BaseAdapter {

    public List<HealthRecordListModel.HealthRecordList> mList;
    Context mCtx;

    public HRAdapter(List<HealthRecordListModel.HealthRecordList> mList,Context mCtx){
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

        TextView mDesc;
        ImageView mImage;


    }

    @Override
    public View getView(int i, View convertView, final ViewGroup viewGroup) {
        final int myPosition;
        viewHolder holder;
        if(convertView==null){
            convertView =   LayoutInflater.from(mCtx).inflate(R.layout.layout_healthrecord, null);
            holder = new viewHolder();
            holder.mDesc = (TextView)convertView.findViewById(R.id.tv_desc);

            //holder.mBook = (Button) convertView.findViewById(R.id.btn_book);
            holder.mImage = (ImageView) convertView.findViewById(R.id.img_dl);

            convertView.setTag(holder);
        }
        else
        {
            holder = (viewHolder)convertView.getTag();
        }
        myPosition = i;
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView)viewGroup).performItemClick(v, myPosition, 0);
            }
        });
        /*if((i%2)==0){
            convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.lightblue));
        }
        else{
            convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.white));
        }*/


        holder.mDesc.setText("Health Record Description \n"+mList.get(i).getDescription());



        return convertView;
    }
}


