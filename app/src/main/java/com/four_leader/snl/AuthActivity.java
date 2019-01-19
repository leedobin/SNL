package com.four_leader.snl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AuthActivity extends AppCompatActivity {
    private LinearLayout firstAuthLayout;
    private LinearLayout secondAuthLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}
