package com.four_leader.snl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;

public class SignUp3Activity extends AppCompatActivity {

    private Spinner languageSpinner;
    private SpinnerAdapter spinnerAdapter;
    private ArrayList<String> languageArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        languageSpinner = findViewById(R.id.languageSpinner);

        languageArr = new ArrayList<>();
        languageArr.add("한국어");
        languageArr.add("영어");

        spinnerAdapter = new SpinnerAdapter(getApplicationContext(), languageArr);
        languageSpinner.setAdapter(spinnerAdapter);
    }
}
