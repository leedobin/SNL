package com.four_leader.snl.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.main.adapter.CategoryAdapter;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.DefaultCategory;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.notice.activity.NoticeActivity;
import com.four_leader.snl.onetime.PushAuthActivity;
import com.four_leader.snl.onetime.SetDefaultCategoryPopup;
import com.four_leader.snl.setting.activity.SettingActivity;
import com.four_leader.snl.write.activity.WriteActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }





}
