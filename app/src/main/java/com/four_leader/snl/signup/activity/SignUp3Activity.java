package com.four_leader.snl.signup.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.signup.adapter.LanguageSpinnerAdapter;
import com.four_leader.snl.util.APIs;
import com.four_leader.snl.util.ConnectAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SignUp3Activity extends AppCompatActivity {

    private Spinner languageSpinner;
    private LanguageSpinnerAdapter languageSpinnerAdapter;
    private ArrayList<String> languageArr;
    private Button signupBtn;
    private RelativeLayout nickNameCheckBtn;
    private ImageButton backBtn;
    private Intent preIntent;
    private EditText nickNameEdt;
    private ConnectAPI connectAPI;
    private String url = APIs.nicknamecheck;
    private String nick;
    private boolean isNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        languageSpinner = findViewById(R.id.languageSpinner);
        signupBtn = findViewById(R.id.signupBtn);
        backBtn = findViewById(R.id.backBtn);
        nickNameEdt = findViewById(R.id.nickNameEdt);
        nickNameCheckBtn = findViewById(R.id.nickNameCheck);

        preIntent = getIntent();

        preIntent.getStringExtra("email");

        Log.i("tagtt", preIntent.getStringExtra("email"));

        languageArr = new ArrayList<>();
        languageArr.add("한국어");
        languageArr.add("영어");

        languageSpinnerAdapter = new LanguageSpinnerAdapter(getApplicationContext(), languageArr);
        languageSpinner.setAdapter(languageSpinnerAdapter);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tagtt", "email : "+preIntent.getStringExtra("email"));
                Log.i("tagtt", "pwd : "+preIntent.getStringExtra("pwd"));
                Log.i("tagtt", "language : "+languageSpinner.getSelectedItem().toString());
                if(isNext){
                    nick = nickNameEdt.getText().toString();
                    connectAPI = new ConnectAPI();
                    if(connectAPI.getStatus() == AsyncTask.Status.RUNNING){
                        connectAPI.cancel(true);
                    }
                    try {
                        url = APIs.signup;
                        String email = preIntent.getStringExtra("email");
                        String pwd = preIntent.getStringExtra("pwd");
                        String lang = languageSpinner.getSelectedItem().toString();

                        String result = connectAPI.execute(new String[]{url, "id="+email+ "&pwd="+pwd+ "&lang="+lang +"&nick="+nick}).get();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jArrObject  = jsonObject.getJSONArray("REPORT");

                        for (int i=0; i<jArrObject .length(); i++) {
                            JSONObject actor = jArrObject .getJSONObject(i);
                            int idCount = Integer.parseInt(actor.getString("nickCount"));

                            if(idCount > 0){
                                isNext = false;
                                Toast.makeText(getApplicationContext(),"닉네임 중복", Toast.LENGTH_SHORT).show();
                            }else{
                                isNext = true;
                                Toast.makeText(getApplicationContext(),"사용가능 닉네임", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"닉네임 체크",Toast.LENGTH_SHORT).show();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nickNameCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = nickNameEdt.getText().toString();
                connectAPI = new ConnectAPI();
                if(connectAPI.getStatus() == AsyncTask.Status.RUNNING){
                    connectAPI.cancel(true);
                }
                try {
                    String result = connectAPI.execute(new String[]{url, "nick="+nick}).get();

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jArrObject  = jsonObject.getJSONArray("REPORT");

                    for (int i=0; i<jArrObject .length(); i++) {
                        JSONObject actor = jArrObject .getJSONObject(i);
                        int idCount = Integer.parseInt(actor.getString("nickCount"));

                        if(idCount > 0){
                            isNext = false;
                            Toast.makeText(getApplicationContext(),"닉네임 중복", Toast.LENGTH_SHORT).show();
                        }else{
                            isNext = true;
                            Toast.makeText(getApplicationContext(),"사용가능 닉네임", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
