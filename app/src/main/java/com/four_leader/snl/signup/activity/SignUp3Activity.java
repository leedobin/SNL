package com.four_leader.snl.signup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.four_leader.snl.signup.adapter.LanguageSpinnerAdapter;
import com.four_leader.snl.R;

import java.util.ArrayList;

public class SignUp3Activity extends AppCompatActivity {

    private Spinner languageSpinner;
    private LanguageSpinnerAdapter languageSpinnerAdapter;
    private ArrayList<String> languageArr;
    private Button signupBtn;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        languageSpinner = findViewById(R.id.languageSpinner);
        signupBtn = findViewById(R.id.signupBtn);
        backBtn = findViewById(R.id.backBtn);

        languageArr = new ArrayList<>();
        languageArr.add("한국어");
        languageArr.add("영어");

        languageSpinnerAdapter = new LanguageSpinnerAdapter(getApplicationContext(), languageArr);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
