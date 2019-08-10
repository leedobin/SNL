package com.four_leader.snl.write.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.four_leader.snl.R;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.write.vo.Save;

import java.util.ArrayList;

public class SaveAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Save> arrayList;
    LayoutInflater inflater;

    public SaveAdapter(Context context, ArrayList<Save> dataList) {
        super();
        this.context = context;
        arrayList = dataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_save, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryText = convertView.findViewById(R.id.categoryText);
        holder.timeText = convertView.findViewById(R.id.timeText);
        holder.titleText = convertView.findViewById(R.id.titleText);
        holder.contentText = convertView.findViewById(R.id.contentText);
        holder.hashtagText = convertView.findViewById(R.id.hashtagText);

        Save data = arrayList.get(position);

        holder.categoryText.setText(data.getCategory1() + " / " + data.getCategory2());
        holder.titleText.setText(data.getTitle());
        holder.contentText.setText(data.getContent());
        holder.contentText.setText(data.getSaveTime());
        holder.hashtagText.setText(data.getHashTag());

        return convertView;
    }

    public class ViewHolder {
        public TextView titleText, contentText, categoryText, timeText, hashtagText;
    }
}
