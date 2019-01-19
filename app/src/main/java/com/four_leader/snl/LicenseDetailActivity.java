package com.four_leader.snl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class LicenseDetailActivity extends AppCompatActivity {

    private TextView licenseTitleTv;
    private TextView licenseDetailTv;
    private CheckBox agreeCheckBox;

    String intentdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_detail);

        licenseTitleTv = findViewById(R.id.licenseTitleTv);
        licenseDetailTv = findViewById(R.id.licenseDetailTv);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);

        intentdata = getIntent().getStringExtra("licenseSeq");

        if(intentdata.equals("1")){
            licenseTitleTv.setText("첫번째 약관 동의");
            licenseDetailTv.setText("이거슨 첫번째 약관동의 입니다\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }else if(intentdata.equals("2")){
            licenseTitleTv.setText("두번째 약관 동의");
            licenseDetailTv.setText("이거슨 두번째 약관동의 입니다\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
        agreeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreeCheckBox.isChecked()){
                    Intent intent = new Intent();
                    intent.putExtra("licenseSeq" , intentdata);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
