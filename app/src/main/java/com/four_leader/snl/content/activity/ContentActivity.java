package com.four_leader.snl.content.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.adapter.VoiceAdapter;
import com.four_leader.snl.content.vo.Voice;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentActivity extends AppCompatActivity {

    ArrayList<Voice> voices;
    VoiceAdapter adapter;
    LinearLayout playView;
    ImageButton closePlayViewBtn, backBtn, recordBtn;
    ListView listView;
    MainContent content;
    TextView titleText, contentText, dateText, heartText, bookmarkText, nickNameText;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        playView = findViewById(R.id.playView);
        closePlayViewBtn = findViewById(R.id.closePlayViewBtn);
        listView = findViewById(R.id.listView);
        backBtn = findViewById(R.id.backBtn);
        recordBtn = findViewById(R.id.recordBtn);
        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);
        dateText = findViewById(R.id.dateText);
        heartText = findViewById(R.id.heartText);
        bookmarkText = findViewById(R.id.bookmarkText);
        nickNameText = findViewById(R.id.nickNameText);

        content = (MainContent) getIntent().getSerializableExtra("content");
        voices = new ArrayList<>();

        setContent();
        getComments(content.getCode());

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
                intent.putExtra("content", content);
                startActivity(intent);
            }
        });
    }

    private void setContent() {
        titleText.setText(content.getTitle());
        contentText.setText(content.getContent());
        dateText.setText(content.getWriteDate());
        heartText.setText(String.valueOf(content.getHeartCount()));
        bookmarkText.setText(String.valueOf(content.getBookmarkCount()));
        nickNameText.setText(content.getUserNicname());
    }

    private void getComments(String code) {
        //# 댓글 칭호 가져오는거 해야함!
        //# 댓글에 북마크 기능이 있나염???
        Log.i("Ttt", code);
        Call<String> call = apiInterface.getScriptComment(code);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();
                try {
                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");
                    voices.clear();
                    for(int i=0; i<report.length(); i++) {
                        JSONObject object = report.getJSONObject(i);
                        Voice voice = new Voice();
                        voice.setCode(object.getString("comment_seq"));
                        voice.setHeartCount(Integer.parseInt(object.getString("comment_likes")));
                        voice.setFileName(object.getString("comment_file_name"));
                        voice.setWriter(object.getString("user_nick"));
                        voices.add(voice);
                    }
                    adapter = new VoiceAdapter(getApplicationContext(), voices, btnClickHandler);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "글 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
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
