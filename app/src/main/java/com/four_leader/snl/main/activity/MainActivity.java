package com.four_leader.snl.main.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.main.adapter.CategoryAdapter;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.DefaultCategory;
import com.four_leader.snl.main.vo.MainContent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<DefaultCategory> categories;
    RecyclerView categoryListView;
    CategoryAdapter categoryAdapter;
    ArrayList<String> searchType;
    Spinner searchSpinner;

    ArrayList<MainContent> contents;

    ListView listView;
    ImageButton setBtn;
    LinearLayout setLayout;
    ContentAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryListView = findViewById(R.id.categoryListView);
        searchSpinner = findViewById(R.id.searchSpinner);
        listView = findViewById(R.id.listView);
        setBtn = findViewById(R.id.setBtn);
        setLayout = findViewById(R.id.setLayout);

        searchType = new ArrayList<>();
        searchType.add("글제목");
        searchType.add("작성자");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, searchType);
        searchSpinner.setAdapter(spinnerAdapter);

        categories = new ArrayList<>();
        categories.add(new DefaultCategory("명언1", "1", true));
        categories.add(new DefaultCategory("명언2", "2", false));
        categories.add(new DefaultCategory("명언3", "3", false));
        categories.add(new DefaultCategory("명언4", "4", false));
        categories.add(new DefaultCategory("명언5", "5", false));
        categories.add(new DefaultCategory("명언6", "6", false));
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categories);
        categoryListView.setAdapter(categoryAdapter);

        contents = new ArrayList<>();
        getContents();

        contentAdapter = new ContentAdapter(getApplicationContext(), contents, togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler);
        listView.setAdapter(contentAdapter);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setLayout.getVisibility() == View.VISIBLE) {
                    setLayout.setVisibility(View.GONE);
                } else {
                    setLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //# 서버에서 값 받아오게 수정해야 함
    private void getContents() {
        for(int i=0; i<10; i++) {
            MainContent content = new MainContent();
            content.setCode(String.valueOf(i));
            content.setTitle("제목입니다.");
            content.setCategory("명언");
            content.setContent("컨텐츠의 내용입니다. 내용이네요 그래요 내용이예요ㅎㅎ");
            content.setEndDate(10);
            content.setUserNicname("내이름은 작성자");
            content.setTime("2019-02-23 13:59:32");
            content.setHeartCount(10);
            content.setVoiceCount(20);
            content.setBookmarkCount(60);
            content.setFileName("190223135932");
            contents.add(content);
        }
    }

    Handler togglePlayLayoutHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            for(int i=0; i<contents.size(); i++) {
                if(i == msg.what) {
                    contents.get(msg.what).setPlayLayoutOpen(!contents.get(msg.what).isPlayLayoutOpen());
                } else {
                    contents.get(i).setPlayLayoutOpen(false);
                }
            }
            contentAdapter.notifyDataSetChanged();
        }
    };

    Handler itemSelectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
            intent.putExtra("code", contents.get(msg.what).getCode());
            startActivity(intent);
        }
    };

    Handler bookmarkClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(contents.get(msg.what).isBookmarked()) {
                contents.get(msg.what).setBookmarked(!contents.get(msg.what).isBookmarked());
            }
            contentAdapter.notifyDataSetChanged();
        }
    };

}
