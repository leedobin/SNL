package com.four_leader.snl.content.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordActivity extends AppCompatActivity {

    ImageButton closeViewBtn;
    ImageButton startRecordBtn;
    ImageButton stopRecordBtn;
    TextView textView;
    MediaRecorder recorder = new MediaRecorder();
    APIInterface apiInterface;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;

    final String uploadFilePath = "storage/emulated/0/";
    final String uploadFileName = "recorder.mp3";

    String  commentSeq, scriptSeq, userSeq, commentFilename;
    MainContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        closeViewBtn = findViewById(R.id.closeViewBtn);
        startRecordBtn = findViewById(R.id.startRecordBtn);
        stopRecordBtn = findViewById(R.id.stopRecordBtn);
        textView = findViewById(R.id.textView);

        content = (MainContent) getIntent().getSerializableExtra("content");
        textView.setText(content.getContent());


        upLoadServerUri = "http://13.209.4.115/SNL/UPLOAD/UploadToServer.php";

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
                saveRecord();
            }
        });


    }

    public void startRecord() {
        try {

            File file = Environment.getExternalStorageDirectory();
            String path = file.getAbsolutePath() + "/" + "recorder.mp3";

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            Log.d("uploadFile", "mic");
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            Log.d("uploadFile", "3gp");
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            Log.d("uploadFile", "amr");
            recorder.setOutputFile(path);
            Log.d("uploadFile", "set");
            recorder.prepare();
            Log.d("uploadFile", "prepare");
            recorder.start();
            Toast.makeText(this, "start Record", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        recorder.stop();
        recorder.release();
        Toast.makeText(this, "stop Record", Toast.LENGTH_LONG).show();
    }

    public void saveRecord() {
        dialog = ProgressDialog.show(RecordActivity.this, "", "Uploading file...", true);

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ;
                    }
                });
                uploadFile(uploadFilePath + uploadFileName);


                commentSeq="20";
                scriptSeq="21";
                userSeq="22";
                commentFilename=uploadFileName;


                recordInfo(commentSeq, scriptSeq, userSeq, commentFilename);
            }
        }).start();

        Log.d("uploadFile", "saveRecord success!!!!!!!");

        Toast.makeText(this, "save Record", Toast.LENGTH_LONG).show();
    }


    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :" + uploadFilePath + uploadFileName);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            return 0;

        } else {
            try {
                // open a URL connection to the Servlet

                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("upload_file", fileName);

                Log.d("uploadFile", "upload setting success!!!!!!!");

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"upload_file\";filename=\"" + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            //String msg = "File Upload Completed.\n\n See uploaded file here : \n\n" + uploadFileName;

                            Toast.makeText(RecordActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                //close the streams //

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();

                ex.printStackTrace();

                runOnUiThread(new Runnable() {

                    public void run() {

                        Toast.makeText(RecordActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {

                dialog.dismiss();

                e.printStackTrace();

                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(RecordActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }

                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;
        } // End else block
    }

    private void recordInfo(final String commentSeq, final String scriptSeq, final String userSeq, final String commentFilename) {
        Call<String> call = apiInterface.recordInfo(commentSeq, scriptSeq, userSeq, commentFilename);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {

                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");

                    JSONObject obj = new JSONObject(report.get(0).toString());
                    String user_seq = obj.getString("user_seq");

                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("comment_seq", commentSeq);
                    editor.putString("script_seq", scriptSeq);
                    editor.putString("user_seq", userSeq);
                    editor.putString("comment_file_name", commentFilename);

                    Log.i("stag", "user_seq : " + user_seq);

                    editor.commit();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}