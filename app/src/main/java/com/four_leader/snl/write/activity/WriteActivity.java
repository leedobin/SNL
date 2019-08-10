package com.four_leader.snl.write.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.main.adapter.CategoryAdapter;
import com.four_leader.snl.main.vo.Category;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;
import com.four_leader.snl.write.vo.Save;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteActivity extends AppCompatActivity {

    Spinner categorySpinner1, categorySpinner2;
    ImageButton backBtn, writeBtn, saveListBtn;
    CheckBox checkBox;
    ArrayList<Category> myAllCategories;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    ArrayList<String> category1, category2;
    ArrayAdapter spinnerAdapter, spinnerAdapter2;
    ArrayList<Save> saves;
    TextView saveBtn;
    EditText titleEdit, contentEdit, hashtagEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        categorySpinner1 = findViewById(R.id.categorySpinner1);
        categorySpinner2 = findViewById(R.id.categorySpinner2);
        backBtn = findViewById(R.id.backBtn);
        writeBtn = findViewById(R.id.writeBtn);
        checkBox = findViewById(R.id.checkBox);
        saveListBtn = findViewById(R.id.saveListBtn);
        saveBtn = findViewById(R.id.saveBtn);
        titleEdit = findViewById(R.id.titleEdit);
        contentEdit = findViewById(R.id.contentEdit);
        hashtagEdit = findViewById(R.id.hashtagEdit);

        myAllCategories = new ArrayList<>();
        saves = new ArrayList<>();
        getCategories();
        getSaveData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "글쓰기 약관에 동의해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (categorySpinner1.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "1차 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (categorySpinner2.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "2차 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(titleEdit.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(contentEdit.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                doWrite();
            }
        });

        saveListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteActivity.this, SaveListActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save save = new Save();
                if (categorySpinner1.getSelectedItemPosition() != 0) {
                    save.setCategory1(category1.get(categorySpinner1.getSelectedItemPosition()));
                }
                if (categorySpinner2.getSelectedItemPosition() != 0) {
                    save.setCategory2(category2.get(categorySpinner2.getSelectedItemPosition()));
                }
                save.setTitle(titleEdit.getText().toString());
                save.setContent(contentEdit.getText().toString());
                save.setHashTag(hashtagEdit.getText().toString());

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                save.setSaveTime(sdf.format(now));

                saves.add(save);

                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Save>>() {
                }.getType();
                String json = gson.toJson(saves, listType);
                editor.putString("saveList", json);
                editor.commit();
                Toast.makeText(getApplicationContext(), "글이 임시 저장되었습니다.", Toast.LENGTH_SHORT).show();
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

        categorySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    category2.clear();
                    category2.add("선택해주세요");
                    spinnerAdapter2.notifyDataSetChanged();
                } else {
                    int objPosition = -1;
                    for (int i = 0; i < myAllCategories.size(); i++) {
                        if (myAllCategories.get(i).getStep2().equals("0")
                                && myAllCategories.get(i).getName().equals(category1.get(position))) {
                            objPosition = i;
                        }
                    }
                    category2.clear();
                    category2.add("선택해주세요");
                    for (int i = 0; i < myAllCategories.size(); i++) {
                        if (myAllCategories.get(i).getStep1().equals(myAllCategories.get(objPosition).getStep1())
                                && !myAllCategories.get(i).getStep2().equals("0")) {
                            category2.add(myAllCategories.get(i).getName());
                        }
                    }
                    spinnerAdapter2.notifyDataSetChanged();
                }
                categorySpinner2.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCategories() {

        category1 = new ArrayList<>();
        category1.add("선택해주세요.");
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category1);
        categorySpinner1.setAdapter(spinnerAdapter);

        category2 = new ArrayList<>();
        category2.add("선택해주세요.");
        spinnerAdapter2 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category2);
        categorySpinner2.setAdapter(spinnerAdapter2);

        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.getUserCategory(userSeq);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {

                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");

                    myAllCategories.clear();
                    for (int i = 0; i < report.length(); i++) {
                        JSONObject object = report.getJSONObject(i);
                        myAllCategories.add(new Category(
                                object.getString("categorySeq"),
                                object.getString("categoryName"),
                                object.getString("step1"),
                                object.getString("step2")
                        ));
                        if (object.getString("step2").equals("0")) {
                            category1.add(object.getString("categoryName"));
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "보유 카테고리 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerAdapter.notifyDataSetChanged();
        spinnerAdapter2.notifyDataSetChanged();

    }

    private void doWrite() {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");

        int categorySeq = -1;
        for(int i=0; i<myAllCategories.size(); i++) {
            if(myAllCategories.get(i).getName().equals(categorySpinner2.getSelectedItem())) {
                categorySeq = i;
            }
        }
        if(categorySeq == -1) {
            Toast.makeText(getApplicationContext(), "글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = apiInterface.writeScript(userSeq, titleEdit.getText().toString(), contentEdit.getText().toString(), categorySeq);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), "글을 작성했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
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
        if (saves == null) {
            saves = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        } else if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                final Save save = (Save) data.getSerializableExtra("resultObj");
                titleEdit.setText(save.getTitle());
                contentEdit.setText(save.getContent());
                hashtagEdit.setText(save.getHashTag());
                for (int i = 0; i < category1.size(); i++) {
                    if (category1.get(i).equals(save.getCategory1())) {
                        categorySpinner1.setSelection(i);
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < category2.size(); i++) {
                            if (category2.get(i).equals(save.getCategory2())) {
                                categorySpinner2.setSelection(i);
                            }
                        }
                    }
                }, 100);
            }
        }
    }
}
