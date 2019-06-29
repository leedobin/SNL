package com.four_leader.snl.content.activity;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.main.vo.MainContent;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {

    ImageButton closeViewBtn;
    ImageButton startRecordBtn;
    ImageButton stopRecordBtn;
    TextView textView;
    MediaRecorder recorder = new MediaRecorder();
    MainContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        closeViewBtn = findViewById(R.id.closeViewBtn);
        startRecordBtn = findViewById(R.id.startRecordBtn);
        stopRecordBtn = findViewById(R.id.stopRecordBtn);
        textView = findViewById(R.id.textView);

        content = (MainContent) getIntent().getSerializableExtra("content");
        textView.setText(content.getContent());


        closeViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecord();
            }
        });

        stopRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecord();
            }
        });
    }

    public void startRecord(){
        try {

            File file= Environment.getExternalStorageDirectory();
//갤럭시 S4기준으로 /storage/emulated/0/의 경로를 갖고 시작한다.
            String path=file.getAbsolutePath()+"/"+"recoder.mp3";

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//첫번째로 어떤 것으로 녹음할것인가를 설정한다. 마이크로 녹음을 할것이기에 MIC로 설정한다.
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//이것은 파일타입을 설정한다. 녹음파일의경우 3gp로해야 용량도 작고 효율적인 녹음기를 개발할 수있다.
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//이것은 코덱을 설정하는 것이라고 생각하면된다.
            recorder.setOutputFile(path);
//저장될 파일을 저장한뒤
            recorder.prepare();
            recorder.start();
//시작하면된다.
            Toast.makeText(this, "start Record", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopRecord(){
        recorder.stop();
        recorder.release();
        Toast.makeText(this,"stop Record",Toast.LENGTH_LONG).show();
    }
}
