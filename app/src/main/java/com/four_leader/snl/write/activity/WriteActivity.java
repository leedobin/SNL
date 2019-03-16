package com.four_leader.snl.write.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.four_leader.snl.R;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {

    Spinner categorySpinner1, categorySpinner2;
    ImageButton backBtn, writeBtn;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        categorySpinner1 = findViewById(R.id.categorySpinner1);
        categorySpinner2 = findViewById(R.id.categorySpinner2);
        backBtn = findViewById(R.id.backBtn);
        writeBtn = findViewById(R.id.writeBtn);
        checkBox = findViewById(R.id.checkBox);

        getCategories();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "글쓰기 약관에 동의해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                Intent intent = new Intent(WriteActivity.this, WriteTermsActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void getCategories() {
        ArrayList<String> category1 = new ArrayList<>();
        category1.add("선택해주세요.");
        category1.add("카테고리1-1");
        category1.add("카테고리1-2");
        category1.add("카테고리1-3");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category1);
        categorySpinner1.setAdapter(spinnerAdapter);

        ArrayList<String> category2 = new ArrayList<>();
        category2.add("선택해주세요.");
        category2.add("카테고리2-1");
        category2.add("카테고리2-2");
        category2.add("카테고리2-3");
        ArrayAdapter spinnerAdapter2 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category2);
        categorySpinner2.setAdapter(spinnerAdapter2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }
}
