package com.four_leader.snl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LicenseActivity extends AppCompatActivity {

    private LinearLayout firstLicenseLayout;
    private LinearLayout secondLicenseLayout;

    private ImageView firstFowardIv;
    private ImageView secondFowardIv;

    Intent intent = new Intent(LicenseActivity.this, LicenseDetailActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firstLicenseLayout = findViewById(R.id.firstLicenseLayout);
        secondLicenseLayout = findViewById(R.id.secondLicenseLayout);

        firstFowardIv = findViewById(R.id.firstFowardIv);
        secondFowardIv = findViewById(R.id.secondFowardIv);

        firstLicenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAuthDetail("1");
            }
        });

        secondLicenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAuthDetail("2");
            }
        });
    }

    public void showAuthDetail(String authSeq){
        intent.putExtra("authSeq", authSeq);
        startActivity(intent);
        finish();
    }
}
