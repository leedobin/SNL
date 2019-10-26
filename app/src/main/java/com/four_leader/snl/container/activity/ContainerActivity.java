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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.four_leader.snl.write.activity.WriteActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    ImageButton mainBtn, libraryBtn, settingBtn;
    ImageView noticeImg;
    RelativeLayout noticeBtn;
    LinearLayout writeBtn;
    public static ArrayList<Category> myAllCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        init();

        // 푸쉬 동의/거절 정보가 없으면 푸쉬 동의 알람 요청 팝업 띄우기
        checkPushPermission();

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

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.i("ttag", instanceIdResult.getToken());
            }
        });

    }

    private void checkPushPermission() {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String agreement_push_date = preferences.getString("agreement_push", "x");
        if (agreement_push_date.equals("x")) {
            Intent intent = new Intent(ContainerActivity.this, PushAuthActivity.class);
            startActivityForResult(intent, 100);
        } else {
            checkCategories();
        }
    }

    private void init() {
        fm = getSupportFragmentManager();
        myAllCategories = new ArrayList<>();

        mainFragment = MainFragment.newInstance();
        libraryFragment = new LibraryFragment().newInstance();
        noticeFragment = new NoticeFragment().newInstance();
        settingFragment = new SettingFragment().newInstance();

        mainBtn = findViewById(R.id.mainBtn);
        libraryBtn = findViewById(R.id.libraryBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        settingBtn = findViewById(R.id.settingBtn);
        writeBtn = findViewById(R.id.writeBtn);
        noticeImg = findViewById(R.id.noticeImg);

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContainerActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });
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

                    myAllCategories.clear();
                    for (int i = 0; i < report.length(); i++) {
                        JSONObject object = report.getJSONObject(i);
                        myAllCategories.add(
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
        allBottomBtnOff();
        mainBtn.setImageResource(R.drawable.ic_home_on);
        writeBtn.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, mainFragment).commit();
    }

    private void getLibraryPage() {
        allBottomBtnOff();
        libraryBtn.setImageResource(R.drawable.ic_book_on);
        writeBtn.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, libraryFragment).commit();
    }

    private void getNoticePage() {
        allBottomBtnOff();
        noticeImg.setImageResource(R.drawable.ic_bell_on);
        writeBtn.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, noticeFragment).commit();
    }

    private void getSettingPage() {
        allBottomBtnOff();
        settingBtn.setImageResource(R.drawable.ic_setting_on);
        writeBtn.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, settingFragment).commit();
    }

    private void allBottomBtnOff() {
        mainBtn.setImageResource(R.drawable.ic_home_off);
        libraryBtn.setImageResource(R.drawable.ic_book_off);
        noticeImg.setImageResource(R.drawable.ic_bell_off);
        settingBtn.setImageResource(R.drawable.ic_setting_off);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) { // 최초 접속시 push 확인
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM시 dd분 ss초");
            String time = sdf.format(d);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddss");
            String time2 = sdf2.format(d);
            if (resultCode == RESULT_OK) { // 권한 허가시 권한 정보 변경
                Toast.makeText(getApplicationContext(), time + "에\npush 알림 수신에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
                setPushPermission("y" + time2);
            } else if (resultCode == RESULT_CANCELED) { // 권한 거절시 권한 정보 변경
                Toast.makeText(getApplicationContext(), "알림 수신에 거절하셨습니다.", Toast.LENGTH_SHORT).show();
                setPushPermission("n" + time2);
            }
            // 해당 유저가 보유한 카테고리 목록 조회 후 카테고리가 하나도 없으면 해당 팝업 띄워주기
            checkCategories();
        }
    }

    private void setPushPermission(String date) {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        Call<String> call = apiInterface.setPushPermission(preferences.getString("user_id", ""), date);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
