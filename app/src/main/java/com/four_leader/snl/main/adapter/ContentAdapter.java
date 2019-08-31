package com.four_leader.snl.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.four_leader.snl.R;
import com.four_leader.snl.main.vo.MainContent;

import java.util.ArrayList;

public class ContentAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<MainContent> arrayList;
    LayoutInflater inflater;
    Handler togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler, likeHanlder;

    public ContentAdapter(Context context, ArrayList<MainContent> dataList, Handler togglePlayLayoutHandler, Handler itemSelectHandler, Handler bookmarkClickHandler, Handler likeHanlder) {
        super();
        this.context = context;
        arrayList = dataList;
        this.togglePlayLayoutHandler = togglePlayLayoutHandler;
        this.itemSelectHandler = itemSelectHandler;
        this.bookmarkClickHandler = bookmarkClickHandler;
        this.likeHanlder = likeHanlder;
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
            convertView = inflater.inflate(R.layout.list_content, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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
        holder.likeLayout = convertView.findViewById(R.id.likeLayout);
        holder.heartIcon = convertView.findViewById(R.id.heartIcon);
        holder.shareIcon = convertView.findViewById(R.id.shareIcon);

        if(String.valueOf(position).equals("")) {
            Log.i("dadada", "에러래ㅠㅠ");
        } else {
            Log.i("dadada", String.valueOf(position));
        }

        holder.playBtn.setTag(position);
        holder.itemLayout.setTag(position);
        holder.bookmarkView.setTag(position);
        holder.likeLayout.setTag(position);

        final MainContent data = arrayList.get(position);

        holder.categoryText.setText(data.getCategory());
        holder.endDateText.setText("D - "+data.getdDate());
        holder.titleText.setText(data.getTitle());
        holder.contentText.setText(data.getContent());
        holder.nameText.setText(data.getUserNicname());
        holder.dateText.setText(data.getWriteDate());
        holder.heartText.setText(String.valueOf(data.getHeartCount()));
        holder.commentText.setText(String.valueOf(data.getVoiceCount()));
        holder.bookmarkText.setText(String.valueOf(data.getBookmarkCount()));

        if(data.isLiked()) {
            holder.heartIcon.setImageResource(R.drawable.ic_heart_on);
        } else {
            holder.heartIcon.setImageResource(R.drawable.ic_heart_off);
        }

        if(data.isBookmarked()) {
            holder.shareIcon.setImageResource(R.drawable.ic_bookmark_on);
        } else {
            holder.shareIcon.setImageResource(R.drawable.ic_bookmark_off);
        }

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
                if(data.isBookmarked()) {
                    msg.obj = false;
                } else {
                    msg.obj = true;
                }
                bookmarkClickHandler.sendMessage(msg);
            }
        });

        holder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = (int) view.getTag();
                if(data.isLiked()) {
                    msg.obj = false;
                } else {
                    msg.obj = true;
                }
                likeHanlder.sendMessage(msg);
            }
        });



        return convertView;
    }

    public class ViewHolder {
        public TextView categoryText, endDateText, titleText, contentText,
                nameText, dateText, heartText, commentText, bookmarkText;
        public LinearLayout playView;
        public ImageButton playBtn;
        public LinearLayout itemLayout, bookmarkView, likeLayout;
        public ImageView heartIcon, shareIcon;
    }
}
