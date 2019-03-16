package com.four_leader.snl.login.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.four_leader.snl.container.activity.ContainerActivity;
import com.four_leader.snl.main.activity.MainActivity;
import com.four_leader.snl.R;
import com.four_leader.snl.license.activity.LicenseActivity;

public class LoginActivity extends AppCompatActivity {

    private Button signupBtn, Loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LicenseActivity.class);
                startActivity(intent);
            }
        });

        Loginbtn = findViewById(R.id.Loginbtn);
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*로그인 직후 가져와야할 값들
                * 1. 카테고리 정보
                * 2. 유저 테이블 정보
                * 3. 알림 카운트
                * 4. 보유 칭호
                * */
                Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
                startActivity(intent);
            }
        });
    }
}
