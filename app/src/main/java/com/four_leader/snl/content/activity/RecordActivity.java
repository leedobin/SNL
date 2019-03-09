package com.four_leader.snl.content.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.four_leader.snl.R;

public class RecordActivity extends AppCompatActivity {

    ImageButton closeViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        closeViewBtn = findViewById(R.id.closeViewBtn);

        closeViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
