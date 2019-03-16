package com.four_leader.snl.notice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.four_leader.snl.R;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.notice.activity.NoticeActivity;
import com.four_leader.snl.notice.adapter.NoticeAdapter;
import com.four_leader.snl.notice.vo.Notice;

import java.util.ArrayList;

public class NoticeFragment extends Fragment {

    ListView listView;
    ArrayList<Notice> notices;
    NoticeAdapter adapter;

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
        return v;
    }

    private void getNotices() {
        notices = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Notice notice = new Notice(String.valueOf(i), "user"+i+"님이\n글을 좋아요 했습니다.");
            notices.add(notice);
        }

        adapter = new NoticeAdapter(getActivity().getApplicationContext(), notices, clickItem);
        listView.setAdapter(adapter);
    }

    Handler clickItem = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(getActivity(), ContentActivity.class);
            intent.putExtra("code", notices.get(msg.what).getNum());
            startActivity(intent);
        }
    };

}
