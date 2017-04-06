package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import bizapps.com.healthforusPatient.BZAppApplication;
import bizapps.com.healthforusPatient.R;

/**
 * Created by venkatat on 11/20/2015.
 */
public class ParkListAdapter extends BaseAdapter {

    private Context myContext;
    private List<ResolveInfo> MyAppList;
    int slots;
    PackageManager myPackageManager;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MySlots" ;
    ParkListAdapter(Context c, int x) {
        myContext = c;
        slots = x;
        myPackageManager = c.getPackageManager();
    }

    String[] slot = {"09:00","09:15","09:30","09:45","10:00","10:15","10:30","10:45","11:00","11:15","11:30","11:45",
            "12:00","12:15","12:30","13:00","13:15","13:45","14:00","14:15","14:30","14:45","15:00","15:15","15:30",
            "15:45","16:00","16:15","16:30","16:45","17:00","17:15","17:30","17:45","18:00","18:15","18:30","18:45",
            "19:00","19:15","19:30","19:45","20:00","20:15","20:30","20:45","21:00","21:15","21:30","21:45",
    };

    @Override
    public int getCount() {
        return slots;
    }

    @Override
    public Object getItem(int position) {
        return slot[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(R.layout.test, null);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.tv_slot);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.text.setText(slot[position]);

        List<String> mySlots = Arrays.asList(BZAppApplication.bookedSlots);

        if(mySlots.contains(slot[position])){
            holder.text.setTextColor(myContext.getResources().getColor(R.color.black80));
        }
        else{
            holder.text.setTextColor(myContext.getResources().getColor(R.color.slot_available));
        }

        /*for(String s: BZAppApplication.bookedSlots){
            for(String s1 : slot){
                holder.text.setText(s1);
                if(s.equalsIgnoreCase(s1)){
                    holder.text.setTextColor(myContext.getResources().getColor(R.color.black80));
                }
                else{
                    holder.text.setTextColor(myContext.getResources().getColor(R.color.slot_available));
                }
            }
            *//*if(s.contains(slot[position])){
                holder.text.setTextColor(myContext.getResources().getColor(R.color.black80));
            }
            else{
                holder.text.setTextColor(myContext.getResources().getColor(R.color.slot_available));
            }*//*
        }
*/
        return convertView;

    }

    static class ViewHolder {
        TextView text;
    }

}
