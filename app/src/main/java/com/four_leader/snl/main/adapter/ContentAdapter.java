package com.four_leader.snl.main.adapter;

import android.content.Context;
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
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.onetime.DefaultCategory;

import java.util.ArrayList;

public class ContentAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<MainContent> arrayList;
    LayoutInflater inflater;
    Handler togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler;

    public ContentAdapter(Context context, ArrayList<MainContent> dataList, Handler togglePlayLayoutHandler, Handler itemSelectHandler, Handler bookmarkClickHandler) {
        super();
        this.context = context;
        arrayList = dataList;
        this.togglePlayLayoutHandler = togglePlayLayoutHandler;
        this.itemSelectHandler = itemSelectHandler;
        this.bookmarkClickHandler = bookmarkClickHandler;
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
            convertView = inflater.inflate(R.layout.list_content, null);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.playView = convertView.findViewById(R.id.playView);
        holder.playBtn = convertView.findViewById(R.id.playBtn);
        holder.itemLayout = convertView.findViewById(R.id.itemLayout);

        holder.categoryText = convertView.findViewById(R.id.categoryText);
        holder.endDateText = convertView.findViewById(R.id.endDateText);
        holder.titleText = convertView.findViewById(R.id.titleText);
        holder.contentText = convertView.findViewById(R.id.contentText);
        holder.nameText = convertView.findViewById(R.id.nameText);
        holder.dateText = convertView.findViewById(R.id.dateText);
        holder.heartText = convertView.findViewById(R.id.heartText);
        holder.commentText = convertView.findViewById(R.id.commentText);
        holder.bookmarkText = convertView.findViewById(R.id.bookmarkText);
        holder.bookmarkView = convertView.findViewById(R.id.bookmarkView);

        MainContent data = arrayList.get(position);

        holder.categoryText.setText(data.getCategory());
        holder.endDateText.setText("D - "+data.getEndDate());
        holder.titleText.setText(data.getTitle());
        holder.contentText.setText(data.getContent());
        holder.nameText.setText(data.getUserNicname());
        holder.dateText.setText(data.getTime());
        holder.heartText.setText(String.valueOf(data.getHeartCount()));
        holder.commentText.setText(String.valueOf(data.getVoiceCount()));
        holder.bookmarkText.setText(String.valueOf(data.getBookmarkCount()));

        holder.playBtn.setTag(position);
        holder.itemLayout.setTag(position);
        holder.bookmarkView.setTag(position);



        if(data.isPlayLayoutOpen()) {
            holder.playView.setVisibility(View.VISIBLE);
        } else {
            holder.playView.setVisibility(View.GONE);
        }

        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                togglePlayLayoutHandler.sendMessage(msg);
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                itemSelectHandler.sendMessage(msg);
            }
        });

        holder.bookmarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                bookmarkClickHandler.sendMessage(msg);
            }
        });


        return convertView;
    }

    public class ViewHolder {
        public TextView categoryText, endDateText, titleText, contentText,
        nameText, dateText, heartText, commentText, bookmarkText;
        public LinearLayout playView;
        public ImageButton playBtn;
        public LinearLayout itemLayout, bookmarkView;
    }


}
