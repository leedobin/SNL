package com.four_leader.snl.content.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.adapter.VoiceAdapter;
import com.four_leader.snl.content.vo.Voice;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {

    ArrayList<Voice> voices;
    VoiceAdapter adapter;
    LinearLayout playView;
    ImageButton closePlayViewBtn, backBtn, recordBtn;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        playView = findViewById(R.id.playView);
        closePlayViewBtn = findViewById(R.id.closePlayViewBtn);
        listView = findViewById(R.id.listView);
        backBtn = findViewById(R.id.backBtn);
        recordBtn = findViewById(R.id.recordBtn);

        voices = new ArrayList<>();
        getContentInfo(getIntent().getStringExtra("code"));
        adapter = new VoiceAdapter(getApplicationContext(), voices, btnClickHandler);
        listView.setAdapter(adapter);

        closePlayViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopVoice();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    //#
    private void getContentInfo(String code) {
        for(int i=0; i<10; i++) {
            Voice voice = new Voice();
            voice.setCode(String.valueOf(i));
            voice.setFileName("file"+i);
            voice.setHeartCount(10+i);
            voice.setWriter("댓글작성자"+i);
            voice.setHeartCount(10);
            voice.setBookmarkCount(20);
            voices.add(voice);
        }
    }

    Handler btnClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(String.valueOf(msg.obj).equals("speaker")) {
                int selectedPosition = -1;
                for(int i=0; i<voices.size(); i++) {
                    if(i == msg.what) {
                        if(voices.get(msg.what).isItemClick()) {
                            voices.get(i).setItemClick(false);
                        } else {
                            selectedPosition = i;
                            voices.get(i).setItemClick(true);
                        }
                    } else {
                        voices.get(i).setItemClick(false);
                    }
                }
                if(selectedPosition == -1) {
                    stopVoice();
                } else {
                    playVoice(voices.get(selectedPosition).getFileName());
                }

            } else if(String.valueOf(msg.obj).equals("heart")) {
                voices.get(msg.what).setHeartClick(!voices.get(msg.what).isHeartClick());
            } else if(String.valueOf(msg.obj).equals("bookmark")) {
                Toast.makeText(getApplicationContext(), "댓글을 북마크했습니다.", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    };

    //#
    private void playVoice(String code) {
        playView.setVisibility(View.VISIBLE);
    }

    private void stopVoice() {
        playView.setVisibility(View.GONE);
    }

}
