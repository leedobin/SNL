package com.four_leader.snl.signup.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.util.APIs;
import com.four_leader.snl.util.ConnectAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SignUp1Activity extends AppCompatActivity {

    private Button nextBtn;
    private ImageButton backBtn;
    private EditText eMailEdt, authEdt;
    private int REQUEST_CODE = 200;
    private RelativeLayout relativeLayout, relativeLayout2;
    private ConnectAPI connectAPI;
    int number = new Random().nextInt(100000);
    private String url = APIs.idcheck;
    private String id;
    private boolean isNext = false;
    private TextView text1;
    private ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up1);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);

        eMailEdt = findViewById(R.id.eMail);
        authEdt = findViewById(R.id.authEdt);

        relativeLayout = findViewById(R.id.checkId);
        relativeLayout2 = findViewById(R.id.sendEmail);

        text1 = findViewById(R.id.text1);
        img1 = findViewById(R.id.img1);

        img1.setVisibility(View.INVISIBLE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
                if (isNext && authEdt.getText().toString().equals(String.valueOf(number))) {
                    intent.putExtra("email", eMailEdt.getText().toString());
                    startActivityForResult(intent, 100);
                } else {
                    Toast.makeText(getApplicationContext(), "이메일 인증 또는 중복확인을 해주십시오", Toast.LENGTH_SHORT).show();
                }
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eMailEdt.getText().toString().equals("")) {
                    Toast.makeText(SignUp1Activity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                id = eMailEdt.getText().toString();
                img1.setVisibility(View.INVISIBLE);
                text1.setVisibility(View.VISIBLE);
                try {
                    connectAPI = new ConnectAPI();
                    if (connectAPI.getStatus() == AsyncTask.Status.RUNNING) {
                        connectAPI.cancel(true);
                    }
                    String result = connectAPI.execute(new String[]{url, "user_id=" + id}).get();

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jArrObject = jsonObject.getJSONArray("REPORT");

                    for (int i = 0; i < jArrObject.length(); i++) {
                        JSONObject actor = jArrObject.getJSONObject(i);
                        int idCount = Integer.parseInt(actor.getString("idCount"));

                        if (idCount > 0) {
                            isNext = false;
                            Toast.makeText(getApplicationContext(), "아이디 중복", Toast.LENGTH_SHORT).show();
                        } else {
                            isNext = true;
                            img1.setVisibility(View.VISIBLE);
                            text1.setVisibility(View.INVISIBLE);
                            eMailEdt.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "사용가능 ID", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = new Random().nextInt(100000);
                if(eMailEdt.getText().toString().equals("")) {
                    Toast.makeText(SignUp1Activity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    connectAPI = new ConnectAPI();
                    url = APIs.sendMail;
                    if (connectAPI.getStatus() == AsyncTask.Status.RUNNING) {
                        connectAPI.cancel(true);
                    }
                    connectAPI.execute(new String[]{url, "email=" + eMailEdt.getText().toString() + "&" + "number=" + String.valueOf(number)}).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "전송 하였습니다. 이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
