package com.four_leader.snl.login.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.four_leader.snl.container.activity.ContainerActivity;
import com.four_leader.snl.R;
import com.four_leader.snl.license.activity.LicenseActivity;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button signupBtn, Loginbtn;
    private EditText editEmail, editPwd;
    private CheckBox AutoLoginBtn;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        editEmail = findViewById(R.id.editEmail);
        editPwd = findViewById(R.id.editPwd);
        AutoLoginBtn = findViewById(R.id.AutoLoginBtn);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        editEmail.setText(pref.getString("user_id", ""));
        editPwd.setText(pref.getString("user_pw", ""));

        if(pref.getBoolean("auto_login", false)) {
            doLogin(pref.getString("user_id", ""), pref.getString("user_pw", ""));
        }

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

                String email = editEmail.getText().toString();
                String pwd = editPwd.getText().toString();

                if (email.equals("") || pwd.equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일 및 비밀번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(email, pwd);
            }
        });
    }

    private void doLogin(final String email, final String pwd) {
        Call<String> call = apiInterface.doLogin(email, pwd);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {

                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");
                    JSONObject obj = new JSONObject(report.get(0).toString());
                    String user_seq = obj.getString("user_seq");

                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user_seq", user_seq);
                    Log.i("ttag", "user_seq : " + user_seq);
                    editor.putString("user_id", email);
                    editor.putString("user_pw", pwd);
                    if(AutoLoginBtn.isChecked()) {
                        editor.putBoolean("auto_login", true);
                    }
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
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
}
