package com.four_leader.snl.notice.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.four_leader.snl.R;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.main.activity.MainActivity;
import com.four_leader.snl.notice.adapter.NoticeAdapter;
import com.four_leader.snl.notice.vo.Notice;
import com.four_leader.snl.setting.activity.SettingActivity;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Notice> notices;
    NoticeAdapter adapter;

    TextView mainBtn, libraryBtn, settingBtn;
    RelativeLayout noticeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        mainBtn = findViewById(R.id.mainBtn);
        libraryBtn = findViewById(R.id.libraryBtn);
        settingBtn = findViewById(R.id.settingBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        listView = findViewById(R.id.listView);

        getNotices();

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0);
                finish();
            }
        });

        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1);
                finish();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(3);
                finish();
            }
        });
    }

    private void getNotices() {
        notices = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Notice notice = new Notice(String.valueOf(i), "user"+i+"님이\n글을 좋아요 했습니다.");
            notices.add(notice);
        }

        adapter = new NoticeAdapter(getApplicationContext(), notices, clickItem);
        listView.setAdapter(adapter);
    }

    Handler clickItem = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(NoticeActivity.this, ContentActivity.class);
            intent.putExtra("code", notices.get(msg.what).getNum());
            startActivity(intent);
        }
    };
}
