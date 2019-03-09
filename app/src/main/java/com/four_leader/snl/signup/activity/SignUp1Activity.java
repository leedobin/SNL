package com.four_leader.snl.signup.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.util.ConnectAPI;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignUp1Activity extends AppCompatActivity {

    private Button nextBtn;
    private ImageButton backBtn;
    private EditText eMailEdt;
    private int REQUEST_CODE = 200;
    private RelativeLayout relativeLayout;
    private ConnectAPI connectAPI;

    private String url = "http://13.209.4.115/SNL/idcheck.php";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);
        eMailEdt = findViewById(R.id.eMail);
        relativeLayout = findViewById(R.id.checkId);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tagtt", "id : " + eMailEdt.getText().toString());
                id = eMailEdt.getText().toString();
                try {
                    connectAPI = new ConnectAPI();
                    if(connectAPI.getStatus() == AsyncTask.Status.RUNNING){
                        connectAPI.cancel(true);
                    }
                    String result = connectAPI.execute(new String[]{url, "id="+id}).get();

                    connectAPI.onPostExecute("");
                    Log.i("tagtt" , "result : " + result);

                   JSONObject jsonObject = new JSONObject(result);
                   JSONArray jArrObject  = jsonObject.getJSONArray("REPORT");

                    for (int i=0; i<jArrObject .length(); i++) {
                        JSONObject actor = jArrObject .getJSONObject(i);
                        int idCount = Integer.parseInt(actor.getString("idCount"));

                        if(idCount > 0){
                            Toast.makeText(getApplicationContext(),"아이디 중복", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"사용가능 ID", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
