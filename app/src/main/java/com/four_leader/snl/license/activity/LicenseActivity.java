package com.four_leader.snl.license.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.four_leader.snl.R;
import com.four_leader.snl.signup.activity.SignUp1Activity;

public class LicenseActivity extends AppCompatActivity {

    private LinearLayout firstLicenseLayout;
    private LinearLayout secondLicenseLayout;

    private ImageView firstFowardIv;
    private ImageView secondFowardIv;

    private Button nextBtn;
    private ImageButton backBtn;

    final int REQUEST_CODE = 200;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firstLicenseLayout = findViewById(R.id.firstLicenseLayout);
        secondLicenseLayout = findViewById(R.id.secondLicenseLayout);

        firstFowardIv = findViewById(R.id.firstFowardIv);
        secondFowardIv = findViewById(R.id.secondFowardIv);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);

        firstLicenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDetail("1");
            }
        });

        secondLicenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDetail("2");
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LicenseActivity.this, SignUp1Activity.class);
                startActivity(intent);
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

    public void showLicenseDetail(String licenseSeq){
        intent = new Intent(LicenseActivity.this, LicenseDetailActivity.class);
        intent.putExtra("licenseSeq", licenseSeq);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                if(data.getStringExtra("licenseSeq").equals("1")){
                    firstFowardIv.setImageResource(R.drawable.ic_check);
                }else if(data.getStringExtra("licenseSeq").equals("2")){
                    secondFowardIv.setImageResource(R.drawable.ic_check);
                }
            }
        }
    }
}
