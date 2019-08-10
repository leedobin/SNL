package com.four_leader.snl.write.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.four_leader.snl.R;
import com.four_leader.snl.write.adapter.SaveAdapter;
import com.four_leader.snl.write.vo.Save;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveListActivity extends AppCompatActivity {

    ImageButton backBtn;
    ListView listView;
    ArrayList<Save> saves;
    SaveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);

        backBtn = findViewById(R.id.backBtn);
        listView = findViewById(R.id.listView);
        saves = new ArrayList<>();
        getSaveData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent getIntent = getIntent();
                getIntent.putExtra("resultObj", saves.get(i));
                setResult(RESULT_OK, getIntent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //# Alert Dialog로 띄워주기
                saves.remove(position);
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Save>>() {
                }.getType();
                String json = gson.toJson(saves, listType);
                editor.putString("saveList", json);
                editor.commit();
                Toast.makeText(getApplicationContext(), "임시 저장 글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                getSaveData();
                return true;
            }
        });
    }

    private void getSaveData() {
        saves.clear();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        Gson gson = new Gson();
        String saveList = pref.getString("saveList", "");
        Type myType = new TypeToken<ArrayList<Save>>() {
        }.getType();
        saves = gson.fromJson(saveList, myType);
        if(saves == null) {
            saves = new ArrayList<>();
        }
        adapter = new SaveAdapter(getApplicationContext(), saves);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}