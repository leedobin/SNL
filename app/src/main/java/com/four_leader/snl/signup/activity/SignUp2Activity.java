package com.four_leader.snl.signup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.four_leader.snl.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp2Activity extends AppCompatActivity {

    private Button nextBtn;
    private EditText pwEdt, pwEdt2;
    private ImageButton backBtn;
    private Intent preIntent;
    String pwPattern = "^(?=.*\\d)(?=.*[a-z]).{6,15}$";
    private ImageView checkImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);
        pwEdt = findViewById(R.id.pwEdt);
        pwEdt2 = findViewById(R.id.pwEdt2);
        checkImg = findViewById(R.id.checkImg);
        checkImg.setVisibility(View.INVISIBLE);

        preIntent = getIntent();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                Matcher matcher = Pattern.compile(pwPattern).matcher(pwEdt.getText().toString());

                if (matcher.matches()) {
                    if (pwEdt.getText().toString().equals(pwEdt2.getText().toString())) {
                        intent.putExtra("email", preIntent.getStringExtra("email"));
                        intent.putExtra("pwd", pwEdt.getText().toString());
                        startActivityForResult(intent, 100);
                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호 규칙에 맞게 다시 설정해 주십시오.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        pwEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!pwEdt.getText().toString().equals("")) {
                    if (pwEdt.getText().toString().equals(pwEdt2.getText().toString())) {
                        checkImg.setVisibility(View.VISIBLE);
                    } else {
                        checkImg.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
