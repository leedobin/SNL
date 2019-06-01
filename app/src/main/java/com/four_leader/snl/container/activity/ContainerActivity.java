package com.four_leader.snl.container.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.main.fragment.LibraryFragment;
import com.four_leader.snl.main.fragment.MainFragment;
import com.four_leader.snl.main.vo.Category;
import com.four_leader.snl.notice.fragment.NoticeFragment;
import com.four_leader.snl.onetime.PushAuthActivity;
import com.four_leader.snl.setting.fragment.SettingFragment;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContainerActivity extends AppCompatActivity {

    FragmentManager fm;
    MainFragment mainFragment;
    LibraryFragment libraryFragment;
    NoticeFragment noticeFragment;
    SettingFragment settingFragment;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    TextView mainBtn, libraryBtn, settingBtn;
    RelativeLayout noticeBtn;
    public ArrayList<Category> myCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        init();

        // 푸쉬 동의/거절 정보가 없으면 푸쉬 동의 알람 요청 팝업 띄우기 //# 이 정보는 로그인시 boolean 값으로 받아보기
        Intent intent = new Intent(ContainerActivity.this, PushAuthActivity.class);
        startActivityForResult(intent, 100);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainPage();
            }
        });

        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLibraryPage();
            }
        });

        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNoticePage();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettingPage();
            }
        });

    }

    private void init() {
        fm = getSupportFragmentManager();
        myCategories = new ArrayList<>();

        mainFragment = MainFragment.newInstance();
        libraryFragment = new LibraryFragment().newInstance();
        noticeFragment = new NoticeFragment().newInstance();
        settingFragment = new SettingFragment().newInstance();


        mainBtn = findViewById(R.id.mainBtn);
        libraryBtn = findViewById(R.id.libraryBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        settingBtn = findViewById(R.id.settingBtn);


    }

    private void checkCategories() {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.getUserCategory(userSeq);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                Log.i("ttag", result);
                try {

                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");

                    myCategories.clear();
                    for(int i=0; i<report.length(); i++) {
                        JSONObject object = report.getJSONObject(i);
                        myCategories.add(
                                new Category(object.getString("categorySeq"),
                                        object.getString("categoryName"),
                                        object.getString("step1"),
                                        object.getString("step2")));
                    }
                    getMainPage();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "보유 카테고리 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    private void getMainPage() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, mainFragment).commit();
    }

    private void getLibraryPage() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, libraryFragment).commit();
    }

    private void getNoticePage() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, noticeFragment).commit();
    }

    private void getSettingPage() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, settingFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) { // 최초 접속시 push 확인
            if(resultCode == RESULT_OK) { // 권한 허가시 권한 정보 변경
                Toast.makeText(getApplicationContext(), "__년 __시 __분 __초에\npush 알림 수신에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
            } else { // 권한 거절시 권한 정보 변경
                Toast.makeText(getApplicationContext(), "알림 수신에 거절하셨습니다.", Toast.LENGTH_SHORT).show();
            }
            // 해당 유저가 보유한 카테고리 목록 조회 후 카테고리가 하나도 없으면 해당 팝업 띄워주기
            checkCategories();
        }
    }
}
