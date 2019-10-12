package com.four_leader.snl.content.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.four_leader.snl.login.activity.LoginActivity;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.onetime.ReportPopUp;
import com.four_leader.snl.onetime.SetDefaultCategoryActivity;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    TextView titleText, contentText, dateText, heartText, bookmarkText, nickNameText, reportBtn;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    ImageButton heartIcon, shareIcon;

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
        reportBtn = findViewById(R.id.reportBtn);
        heartIcon = findViewById(R.id.heartIcon);
        shareIcon = findViewById(R.id.shareIcon);

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

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ReportPopUp.class);
                intent.putExtra("type", "script");
                intent.putExtra("content", content);
                startActivity(intent);
            }
        });

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.isLiked()) {
                    setContentLikeOff();
                } else {
                    setContentLikeOn();
                }
            }
        });

        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.isBookmarked()) {
                    if(content.isBookmarked()) {
                        setContentShareOff();
                    } else {
                        setContentShareOn();
                    }
                }
            }
        });
    }

    private void setContentLikeOn() {
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.addLike(userSeq, scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                content.setLiked(true);
                content.setHeartCount(content.getHeartCount() + 1);
                setContent();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setContentLikeOff() {
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.deleteLike(userSeq, scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                content.setLiked(false);
                content.setHeartCount(content.getHeartCount() - 1);
                setContent();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setContentShareOn() {
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        String userId = preferences.getString("user_id", "");
        Call<String> call = apiInterface.share(userSeq, scriptCode, userId);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                content.setBookmarked(true);
                content.setBookmarkCount(content.getBookmarkCount() + 1);
                setContent();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setContentShareOff() {
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.unshare(userSeq, scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                content.setBookmarked(false);
                content.setBookmarkCount(content.getBookmarkCount() - 1);
                setContent();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
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

        if(content.isLiked()) {
            heartIcon.setImageResource(R.drawable.ic_heart_on);
        } else {
            heartIcon.setImageResource(R.drawable.ic_heart_off);
        }

        if(content.isBookmarked()) {
            shareIcon.setImageResource(R.drawable.ic_bookmark_on);
        } else {
            shareIcon.setImageResource(R.drawable.ic_bookmark_off);
        }
    }

    private void getComments(String code) {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.getScriptComment(code, userSeq);
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
                        voice.setWriterSeq(object.getString("user_seq"));
                        Log.i("ttt", "2");
                        if(object.getString("likeYn").equals("Y")) {
                            voice.setHeartClick(true);
                        } else {
                            Log.i("ttt", "1");
                            voice.setHeartClick(false);
                        }
                        String userName = object.getString("user_nick");
                        if(!object.getString("titleinfo_title").equals("null")) {
                            userName = object.getString("titleinfo_title") + " " + userName;
                        }
                        if(userName.equals("null")) {
                            userName = "탈퇴한 회원입니다";
                        }
                        voice.setWriter(userName);
                        voices.add(voice);
                    }
                    adapter = new VoiceAdapter(getApplicationContext(), voices, btnClickHandler, reportHandler);
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
                if(voices.get(msg.what).isHeartClick()) {
                    setCommentHeartOff(msg.what);
                } else {
                    setCommentHeartOn(msg.what);
                }
                voices.get(msg.what).setHeartClick(!voices.get(msg.what).isHeartClick());
            }
            adapter.notifyDataSetChanged();
        }
    };

    Handler reportHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int pos = msg.what;

            Intent intent = new Intent(ContentActivity.this, ReportPopUp.class);
            intent.putExtra("type", "comment");
            intent.putExtra("voice", voices.get(pos));
            intent.putExtra("content", content);
            startActivity(intent);
        }
    };

    private void setCommentHeartOn(final int pos) {
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.setCommentLikeOn(userSeq, voices.get(pos).getCode(), scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                voices.get(pos).setHeartClick(true);
                voices.get(pos).setHeartCount(voices.get(pos).getHeartCount() + 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCommentHeartOff(final int pos) {
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.setCommentLikeOff(userSeq, voices.get(pos).getCode(), scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                voices.get(pos).setHeartClick(false);
                voices.get(pos).setHeartCount(voices.get(pos).getHeartCount() - 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playVoice(String code) {
        playView.setVisibility(View.VISIBLE);
    }

    private void stopVoice() {
        playView.setVisibility(View.GONE);
    }

}
