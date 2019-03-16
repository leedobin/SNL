package com.four_leader.snl.write.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.four_leader.snl.R;

public class WriteTermsActivity extends AppCompatActivity {

    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_terms);

        okBtn = findViewById(R.id.okBtn);

        setResult(RESULT_CANCELED);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
