package com.four_leader.snl.onetime;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.vo.Content;
import com.four_leader.snl.content.vo.Voice;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPopUp extends Activity {

    String type = "";
    Button okBtn;
    Voice voice;
    MainContent content;
    TextView titleText;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    CheckBox check1, check2, check3, check4, check5, check6, check7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_report);

        titleText = findViewById(R.id.titleText);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);
        check5 = findViewById(R.id.check5);
        check6 = findViewById(R.id.check6);
        check7 = findViewById(R.id.check7);

        type = getIntent().getStringExtra("type");

        if (type.equals("comment")) {
            titleText.setText("댓글 신고하기");
            voice = (Voice) getIntent().getSerializableExtra("voice");
        } else {
            titleText.setText("글 신고하기");
            content = (MainContent) getIntent().getSerializableExtra("content");
        }

        okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("comment")) {
                    reportComment();
                } else {
                    reportScript();
                }
            }
        });
    }

    private void reportComment() {
        String memo = getChecked();
        String commentCode = voice.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.reportComment(commentCode, userSeq, voice.getWriterSeq(), memo);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), "신고가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportScript() {
        String memo = getChecked();
        String scriptCode = content.getCode();
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.reportScript(scriptCode, userSeq, content.getWriterSeq(), memo);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), "신고가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getChecked() {
        String result = "";

        if (check1.isChecked()) {
            result = result + check1.getText().toString();
            result = result + ",";
        }
        if (check2.isChecked()) {
            result = result + check2.getText().toString();
            result = result + ",";
        }
        if (check3.isChecked()) {
            result = result + check3.getText().toString();
            result = result + ",";
        }
        if (check4.isChecked()) {
            result = result + check4.getText().toString();
            result = result + ",";
        }
        if (check5.isChecked()) {
            result = result + check5.getText().toString();
            result = result + ",";
        }
        if (check6.isChecked()) {
            result = result + check6.getText().toString();
            result = result + ",";
        }
        if (check7.isChecked()) {
            result = result + check7.getText().toString();
            result = result + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    public void close(View v) {
        finish();
    }
}
