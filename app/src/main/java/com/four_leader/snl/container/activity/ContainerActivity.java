package com.four_leader.snl.container.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.main.activity.MainActivity;
import com.four_leader.snl.main.fragment.LibraryFragment;
import com.four_leader.snl.main.fragment.MainFragment;
import com.four_leader.snl.notice.fragment.NoticeFragment;
import com.four_leader.snl.onetime.PushAuthActivity;
import com.four_leader.snl.onetime.SetDefaultCategoryPopup;
import com.four_leader.snl.setting.fragment.SettingFragment;

public class ContainerActivity extends AppCompatActivity {

    FragmentManager fm;
    MainFragment mainFragment;
    LibraryFragment libraryFragment;
    NoticeFragment noticeFragment;
    SettingFragment settingFragment;

    boolean isFirstAccess = true;

    TextView mainBtn, libraryBtn, settingBtn;
    RelativeLayout noticeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        init();

        // 푸쉬 동의/거절 정보가 없으면 푸쉬 동의 알람 요청 팝업 띄우기 // 이 정보는 로그인시 boolean 값으로 받아보기
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

        mainFragment = MainFragment.newInstance();
        libraryFragment = new LibraryFragment().newInstance();
        noticeFragment = new NoticeFragment().newInstance();
        settingFragment = new SettingFragment().newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, mainFragment).commit();

        mainBtn = findViewById(R.id.mainBtn);
        libraryBtn = findViewById(R.id.libraryBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        settingBtn = findViewById(R.id.settingBtn);
    }

    private void setDefaultCategories() {
        if(isFirstAccess) {
            Intent intent = new Intent(ContainerActivity.this, SetDefaultCategoryPopup.class);
            startActivityForResult(intent, 0);
        } else {
            getMainPage();
        }
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
            // 가입 후 첫 접속이면 카테고리 설정하도록 하기
            setDefaultCategories();
        }
    }
}
