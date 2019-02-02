package com.four_leader.snl.onetime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.four_leader.snl.R;

import java.util.ArrayList;

public class SetDefaultCategoryPopup extends Activity {

    GridView gridView;
    ArrayList<DefaultCategory> categories;
    SetDefaultCategoryAdapter setDefaultCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_set_default_category);

        gridView = findViewById(R.id.gridView);
        categories = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            categories.add(new DefaultCategory("카테고리" + i, String.valueOf(i), false));
        }

        setDefaultCategoryAdapter = new SetDefaultCategoryAdapter(getApplicationContext(), categories);
        gridView.setAdapter(setDefaultCategoryAdapter);
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
