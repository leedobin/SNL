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
import com.four_leader.snl.content.activity.ContentActivity;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

    ArrayList<Category> myCategories;
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

        writeBtn = v.findViewById(R.id.writeBtn);

        categoryListView = v.findViewById(R.id.categoryListView);
        searchSpinner = v.findViewById(R.id.searchSpinner);
        listView = v.findViewById(R.id.listView);
        setBtn = v.findViewById(R.id.setBtn);
        setLayout = v.findViewById(R.id.setLayout);
        setContentOptionBtn = v.findViewById(R.id.setContentOptionBtn);

        myCategories = new ArrayList<>();
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
        getMainContents();
    }

    private void getCategories() {

        myCategories.clear();
        myCategories.add(new Category("명언1", "1", true));
        myCategories.add(new Category("명언2", "2", false));
        myCategories.add(new Category("명언3", "3", false));
        myCategories.add(new Category("명언4", "4", false));
        myCategories.add(new Category("명언5", "5", false));
        myCategories.add(new Category("명언6", "6", false));

        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext(), myCategories, categoryClickHandler);
        categoryListView.setLayoutManager(manager);
        categoryListView.setAdapter(categoryAdapter);
    }

    //# 서버에서 값 받아오게 수정해야 함
    private void getMainContents() {
        contents.clear();

        for(int i=0; i<10; i++) {
            MainContent content = new MainContent();
            content.setCode(String.valueOf(i));
            content.setTitle("제목입니다.");
            content.setCategory("명언");
            content.setContent("컨텐츠의 내용입니다. 내용이네요 그래요 내용이예요ㅎㅎ");
            content.setEndDate(10);
            content.setUserNicname("내이름은 작성자");
            content.setTime("2019-02-23 13:59:32");
            content.setHeartCount(10);
            content.setVoiceCount(20);
            content.setBookmarkCount(60);
            content.setFileName("190223135932");
            contents.add(content);
        }

        contentAdapter = new ContentAdapter(getActivity(), contents, togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler);
        listView.setAdapter(contentAdapter);
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
            for(int i = 0; i< myCategories.size(); i++) {
                myCategories.get(i).setChecked(false);
            }
            myCategories.get(position).setChecked(true);
            categoryAdapter.notifyDataSetChanged();
        }
    };

}
