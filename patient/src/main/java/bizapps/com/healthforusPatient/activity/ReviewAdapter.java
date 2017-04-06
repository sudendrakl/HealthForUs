package bizapps.com.healthforusPatient.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bizapps.com.healthforusPatient.Model.SearchModel;
import bizapps.com.healthforusPatient.R;

/**
 * Created by venkatat on 8/9/2016.
 */
public class ReviewAdapter extends BaseAdapter {

    public List<SearchModel.GetReview> mList;
    Context mCtx;

    public ReviewAdapter(List<SearchModel.GetReview> mList,Context mCtx){
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

    public class ViewHolder{
        TextView name,comment;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = LayoutInflater.from(mCtx).inflate(R.layout.layout_review,null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_reviewName);
            holder.comment = (TextView) view.findViewById(R.id.tv_reviewComment);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText("Name : "+mList.get(i).getName());
        holder.comment.setText("Comment : "+mList.get(i).getComment());

        return view;
    }
}
