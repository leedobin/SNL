package com.four_leader.snl.main.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.container.activity.ContainerActivity;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.main.activity.MainActivity;
import com.four_leader.snl.main.adapter.CategoryAdapter;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.Category;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;
import com.four_leader.snl.write.activity.WriteActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

    RecyclerView categoryListView;
    CategoryAdapter categoryAdapter;
    ArrayList<String> searchType;
    Spinner searchSpinner;

    ArrayList<MainContent> contents;

    ListView listView;
    ImageButton setBtn;
    LinearLayout setLayout, writeBtn;
    ContentAdapter contentAdapter;
    Button setContentOptionBtn;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    ContainerActivity parentActivity;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        parentActivity = (ContainerActivity) getActivity();

        writeBtn = v.findViewById(R.id.writeBtn);

        categoryListView = v.findViewById(R.id.categoryListView);
        searchSpinner = v.findViewById(R.id.searchSpinner);
        listView = v.findViewById(R.id.listView);
        setBtn = v.findViewById(R.id.setBtn);
        setLayout = v.findViewById(R.id.setLayout);
        setContentOptionBtn = v.findViewById(R.id.setContentOptionBtn);

        contents = new ArrayList<>();

        setLayout.setVisibility(View.GONE);
        searchType = new ArrayList<>();
        searchType.add("글제목");
        searchType.add("글내용");
        searchType.add("작성자");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, searchType);
        searchSpinner.setAdapter(spinnerAdapter);


        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setLayout.getVisibility() == View.VISIBLE) {
                    setLayout.setVisibility(View.GONE);
                } else {
                    setLayout.setVisibility(View.VISIBLE);
                    setLayout.setFocusable(true);
                }
            }
        });

        setContentOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout.setVisibility(View.GONE);
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WriteActivity.class);
                startActivity(intent);
            }
        });

        getMainPage();

        return v;
    }

    public void getMainPage() {
        categoryListView.setVisibility(View.VISIBLE);

        getCategories();
    }

    private void getCategories() {
        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
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

                    parentActivity.myCategories.clear();
                    for(int i=0; i<report.length(); i++) {
                        JSONObject object = report.getJSONObject(i);
                        parentActivity.myCategories.add(new Category(
                                object.getString("categorySeq"),
                                object.getString("categoryName"),
                                object.getString("step1"),
                                object.getString("step2")
                        ));
                    }
                    if(parentActivity.myCategories.size() != 0) {
                        parentActivity.myCategories.get(0).setChecked(true);
                        selectCategory(parentActivity.myCategories.get(0).getSeq());
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);

                    categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext(), parentActivity.myCategories, categoryClickHandler);
                    categoryListView.setLayoutManager(manager);
                    categoryListView.setAdapter(categoryAdapter);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "보유 카테고리 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectCategory(String cateSeq) {
        Call<String> call = apiInterface.getScriptByCateSeq(cateSeq);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {

                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");
                    contents.clear();
                    for(int i=0; i<report.length(); i++) {
                        JSONObject object = report.getJSONObject(i);
                        MainContent content = new MainContent();

                        /*
                            "scriptReported": "1",
                            "commentLikes": "100"
                         */

                        content.setCode(object.getString("scriptSeq"));
                        content.setWriterSeq(object.getString("userSeq"));
                        content.setWriteDate(object.getString("scriptDate"));
                        content.setTitle(object.getString("scriptTitle"));
                        content.setContent(object.getString("scriptContent"));
                        content.setDueDate(object.getString("scriptDueDate"));
                        content.setCategory(object.getString("categoryName"));

                        content.setHeartCount(Integer.parseInt(object.getString("scriptLikes")));
                        //# 수정하기 content.setVoiceCount(0);
                        content.setBookmarkCount(Integer.parseInt(object.getString("scriptShare")));

                        if(object.getString("titleInfoTitle") == "null") {
                            content.setUserNicname(object.getString("userNice"));
                        } else {
                            content.setUserNicname(object.getString("titleInfoTitle") + " " + object.getString("userNice"));
                        }

                        Date nowDate = new Date();
                        String endStr = object.getString("scriptDueDate");

                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date endDate = transFormat.parse(endStr);
                        long diff = endDate.getTime() - nowDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        content.setdDate((int) diffDays);

                        //# 인기 많은 댓글의 파일명 가져와야 함 content.setFileName("190223135932");
                        contents.add(content);


                    }
                    contentAdapter = new ContentAdapter(getActivity(), contents, togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler);
                    listView.setAdapter(contentAdapter);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "글 목록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Handler togglePlayLayoutHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            for(int i=0; i<contents.size(); i++) {
                if(i == msg.what) {
                    contents.get(msg.what).setPlayLayoutOpen(!contents.get(msg.what).isPlayLayoutOpen());
                } else {
                    contents.get(i).setPlayLayoutOpen(false);
                }
            }
            contentAdapter.notifyDataSetChanged();
        }
    };

    Handler itemSelectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(getActivity(), ContentActivity.class);
            intent.putExtra("code", contents.get(msg.what).getCode());
            startActivity(intent);
        }
    };

    Handler bookmarkClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(contents.get(msg.what).isBookmarked()) {
                contents.get(msg.what).setBookmarked(!contents.get(msg.what).isBookmarked());
            }
            contentAdapter.notifyDataSetChanged();
        }
    };

    Handler categoryClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position = msg.what;
            for(int i = 0; i< parentActivity.myCategories.size(); i++) {
                parentActivity.myCategories.get(i).setChecked(false);
            }
            parentActivity.myCategories.get(position).setChecked(true);
            categoryAdapter.notifyDataSetChanged();
            selectCategory(parentActivity.myCategories.get(position).getSeq());
        }
    };

}
