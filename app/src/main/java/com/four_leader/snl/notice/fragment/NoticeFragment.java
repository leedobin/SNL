package com.four_leader.snl.notice.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.MainContent;
import com.four_leader.snl.notice.adapter.NoticeAdapter;
import com.four_leader.snl.notice.vo.Notice;
import com.four_leader.snl.util.APIClient;
import com.four_leader.snl.util.APIInterface;

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

public class NoticeFragment extends Fragment {

    ListView listView;
    ArrayList<Notice> notices;
    NoticeAdapter adapter;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    public NoticeFragment() {
        // Required empty public constructor
    }

    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notice, container, false);

        listView = v.findViewById(R.id.listView);

        getNotices();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setAlarmRead(i);
            }
        });
        return v;
    }

    private void getNotices() {
        notices = new ArrayList<>();

        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        Call<String> call = apiInterface.getAlarmList(preferences.getString("user_seq", ""));
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {
                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");
                    notices.clear();
                    for (int i = 0; i < report.length(); i++) {

                        JSONObject object = report.getJSONObject(i);
                        MainContent content = new MainContent();

                        content.setCode(object.getString("scriptSeq"));
                        content.setWriterSeq(object.getString("userSeq"));
                        content.setWriteDate(object.getString("scriptDate"));
                        content.setTitle(object.getString("scriptTitle"));
                        content.setContent(object.getString("scriptContent"));
                        content.setDueDate(object.getString("scriptDueDate"));
                        content.setHeartCount(Integer.parseInt(object.getString("scriptLikes")));
                        content.setBookmarkCount(Integer.parseInt(object.getString("scriptShare")));

                        if (object.getString("titleInfoTitle") == "null") {
                            content.setUserNicname(object.getString("userNice"));
                        } else {
                            content.setUserNicname(object.getString("titleInfoTitle") + " " + object.getString("userNice"));
                        }

                        if (object.getString("likeYN").equals("Y")) {
                            content.setLiked(true);
                        } else {
                            content.setLiked(false);
                        }

                        if (object.getString("shareYN").equals("Y")) {
                            content.setBookmarked(true);
                        } else {
                            content.setBookmarked(false);
                        }

                        Date nowDate = new Date();
                        String endStr = object.getString("scriptDueDate");
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date endDate = transFormat.parse(endStr);
                        long diff = endDate.getTime() - nowDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        content.setdDate((int) diffDays);

                        String alarmRead = object.getString("alarmRead");
                        String alarmContent = object.getString("alarmContent");

                        Notice notice = new Notice(object.getString("alarmSeq"), alarmRead, alarmContent, content);
                        notices.add(notice);
                    }

                    adapter = new NoticeAdapter(getActivity().getApplicationContext(), notices);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "내 글 목록을 조회할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity().getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlarmRead(final int pos) {
        Call<String> call = apiInterface.setAlarmRead(notices.get(pos).getSeq());
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("content", notices.get(pos).getContent());
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("content", notices.get(pos).getContent());
                startActivity(intent);
            }
        });


    }
}
