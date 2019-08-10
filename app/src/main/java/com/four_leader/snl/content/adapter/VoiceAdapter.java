package com.four_leader.snl.content.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.four_leader.snl.R;
import com.four_leader.snl.content.vo.Voice;

import java.util.ArrayList;

public class VoiceAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Voice> arrayList;
    LayoutInflater inflater;
    Handler btnClickHandler, reportHandler;

    public VoiceAdapter(Context context, ArrayList<Voice> dataList, Handler btnClickHandler, Handler reportHandler) {
        super();
        this.context = context;
        arrayList = dataList;
        this.btnClickHandler = btnClickHandler;
        this.reportHandler = reportHandler;
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
            convertView = inflater.inflate(R.layout.list_voice, null);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.nameText = convertView.findViewById(R.id.nameText);
        holder.speakerBtn = convertView.findViewById(R.id.speakerBtn);
        holder.heartBtn = convertView.findViewById(R.id.heartBtn);
        holder.heartCountText = convertView.findViewById(R.id.heartCountText);
        holder.backgroundLayout = convertView.findViewById(R.id.backgroundLayout);
        holder.reportBtn = convertView.findViewById(R.id.reportBtn);

        holder.speakerBtn.setTag(position);
        holder.heartBtn.setTag(position);
        holder.reportBtn.setTag(position);

        Voice data = arrayList.get(position);

        holder.nameText.setText(data.getWriter());
        holder.heartCountText.setText(String.valueOf(data.getHeartCount()));

        if (data.isItemClick()) {
            holder.backgroundLayout.setBackgroundColor(Color.parseColor("#E6E8EA"));
        } else {
            holder.backgroundLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.speakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                msg.obj = "speaker";
                btnClickHandler.sendMessage(msg);
            }
        });

        holder.heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                msg.obj = "heart";
                btnClickHandler.sendMessage(msg);
            }
        });

        holder.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                reportHandler.sendMessage(msg);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        public TextView nameText, heartCountText, reportBtn;
        public ImageButton speakerBtn, heartBtn;
        public LinearLayout backgroundLayout;
    }


}
