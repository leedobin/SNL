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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);


    }


}
