package com.four_leader.snl.onetime;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.container.activity.ContainerActivity;
import com.four_leader.snl.login.activity.LoginActivity;
import com.four_leader.snl.main.vo.Category;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetDefaultCategoryActivity extends Activity {

    GridView gridView;
    ArrayList<Category> categories;
    SetDefaultCategoryAdapter setDefaultCategoryAdapter;
    Button okBtn;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_set_default_category);

        gridView = findViewById(R.id.gridView);
        okBtn = findViewById(R.id.okBtn);

        categories = new ArrayList<>();

        getCategory();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (categories.get(i).getChecked()) {
                    categories.get(i).setChecked(false);
                } else {
                    if (getCheckedItemCount() > 2) {
                        Toast.makeText(getApplicationContext(), "카테고리는 최대 3개까지 선택하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        categories.get(i).setChecked(true);
                    }
                }
                setDefaultCategoryAdapter.notifyDataSetChanged();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedCount = 0;
                for(int i=0; i<categories.size(); i++) {
                    if(categories.get(i).getChecked()) {
                        selectedCount += 1;
                    }
                }
                if(selectedCount > 0) {
                    for(Category cate: categories) {
                        if(cate.getChecked()) {
                            addCategories(cate.getSeq());
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "카테고리를 1개 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCategories(String categorySeq) {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.setUserCategory(userSeq, categorySeq);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Log.i("ttt", response.body().toString());
                    JSONArray result = new JSONArray(response.body().toString());
                    categories.clear();
                    for(int i=0; i<result.length(); i++) {
                        JSONObject object = new JSONObject(result.get(i).toString());

                        categories.add(new Category(
                                object.getString("category_seq"),
                                object.getString("category_name"),
                                object.getString("category_step1"),
                                object.getString("category_step2")));

                    }
                    setDefaultCategoryAdapter = new SetDefaultCategoryAdapter(getApplicationContext(), categories);
                    gridView.setAdapter(setDefaultCategoryAdapter);
                } catch (JSONException e) {
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

    private void getCategory() {
        Call<String> call = apiInterface.getCategory();
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Log.i("ttt", response.body().toString());
                    JSONArray result = new JSONArray(response.body().toString());
                    categories.clear();
                    for(int i=0; i<result.length(); i++) {
                        JSONObject object = new JSONObject(result.get(i).toString());

                        categories.add(new Category(
                                object.getString("category_seq"),
                                object.getString("category_name"),
                                object.getString("category_step1"),
                                object.getString("category_step2")));

                    }
                    setDefaultCategoryAdapter = new SetDefaultCategoryAdapter(getApplicationContext(), categories);
                    gridView.setAdapter(setDefaultCategoryAdapter);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "카테고리 목록 조회 실패", Toast.LENGTH_SHORT).show();
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

    private int getCheckedItemCount() {
        int count = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getChecked()) {
                count = count + 1;
            }
        }
        return count;
    }
}
