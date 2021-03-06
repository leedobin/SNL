package com.four_leader.snl.onetime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.login.activity.LoginActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class AuthPopUp extends AppCompatActivity {

    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_auth);

        nextBtn = findViewById(R.id.nextBtn);

        // 이미 권한 허가된 상태면 바로 intent
        int writeStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int micPermission  =ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if(writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && micPermission == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(AuthPopUp.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedPermission.with(AuthPopUp.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                        .check();
            }
        });

    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한 허가", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AuthPopUp.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AuthPopUp.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
