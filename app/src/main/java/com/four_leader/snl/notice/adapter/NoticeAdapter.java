package com.four_leader.snl.notice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.four_leader.snl.R;
import com.four_leader.snl.notice.vo.Notice;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Notice> arrayList;
    LayoutInflater inflater;

    public NoticeAdapter(Context context, ArrayList<Notice> dataList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_notice, null);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.textView = convertView.findViewById(R.id.textView);
        holder.textView.setTag(position);

        Notice data = arrayList.get(position);
        holder.textView.setText(data.getMsg());

        if(data.getRead().equals("Y")) {
            holder.textView.setBackgroundColor(Color.parseColor("#cdcdcd"));
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
    }

}
