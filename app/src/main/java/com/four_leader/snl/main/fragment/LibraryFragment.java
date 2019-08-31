package com.four_leader.snl.main.fragment;

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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.four_leader.snl.R;
import com.four_leader.snl.content.activity.ContentActivity;
import com.four_leader.snl.main.adapter.ContentAdapter;
import com.four_leader.snl.main.vo.Category;
import com.four_leader.snl.main.vo.MainContent;
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

public class LibraryFragment extends Fragment {

    ArrayList<Category> categories;
    ArrayList<String> searchType;
    Spinner searchSpinner;
    ImageButton topBtn;
    ArrayList<MainContent> contents;

    ListView listView;
    ContentAdapter contentAdapter;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Button myButton, shareButton;

    public LibraryFragment() {
        // Required empty public constructor
    }


    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);

        searchSpinner = v.findViewById(R.id.searchSpinner);
        listView = v.findViewById(R.id.listView);
        topBtn = v.findViewById(R.id.topBtn);
        myButton = v.findViewById(R.id.mYButton);
        shareButton = v.findViewById(R.id.shareButton);

        categories = new ArrayList<>();
        contents = new ArrayList<>();

        searchType = new ArrayList<>();
        searchType.add("글제목");
        searchType.add("글내용");
        searchType.add("작성자");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, searchType);
        searchSpinner.setAdapter(spinnerAdapter);

        getMyLib();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(listView.getFirstVisiblePosition() > 0) {
                    topBtn.setVisibility(View.VISIBLE);
                } else {
                    topBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.smoothScrollToPosition(0);
            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLib();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShareLib();
            }
        });

        return v;
    }

    private void getMyLib() {

        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        Call<String> call = apiInterface.getMyLib(preferences.getString("user_seq", ""));
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {
                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");
                    contents.clear();
                    for (int i = 0; i < report.length(); i++) {
                        Log.i("ttt", "ddd");
                        JSONObject object = report.getJSONObject(i);
                        MainContent content = new MainContent();

                        content.setCode(object.getString("scriptSeq"));
                        content.setWriterSeq(object.getString("userSeq"));
                        content.setWriteDate(object.getString("scriptDate"));
                        content.setTitle(object.getString("scriptTitle"));
                        content.setContent(object.getString("scriptContent"));
                        content.setDueDate(object.getString("scriptDueDate"));
                        content.setCategory(object.getString("categoryName"));
                        content.setHeartCount(Integer.parseInt(object.getString("scriptLikes")));
                        content.setVoiceCount(Integer.parseInt(object.getString("commentCount")));
                        content.setBookmarkCount(Integer.parseInt(object.getString("scriptShare")));
                        content.setFileName(object.getString("fileName"));

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
                        contents.add(content);
                    }
                    contentAdapter = new ContentAdapter(getActivity(), contents, togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler, likeHanlder);
                    listView.setAdapter(contentAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "공유 목록을 조회할 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    private void getShareLib() {

        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        Call<String> call = apiInterface.getShareLib(preferences.getString("user_seq", ""));
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();

                try {
                    JSONObject resultObj = new JSONObject(result);
                    JSONArray report = resultObj.getJSONArray("REPORT");
                    contents.clear();
                    for (int i = 0; i < report.length(); i++) {
                        Log.i("ttt", "ddd");
                        JSONObject object = report.getJSONObject(i);
                        MainContent content = new MainContent();

                        content.setCode(object.getString("scriptSeq"));
                        content.setWriterSeq(object.getString("userSeq"));
                        content.setWriteDate(object.getString("scriptDate"));
                        content.setTitle(object.getString("scriptTitle"));
                        content.setContent(object.getString("scriptContent"));
                        content.setDueDate(object.getString("scriptDueDate"));
                        content.setCategory(object.getString("categoryName"));
                        content.setHeartCount(Integer.parseInt(object.getString("scriptLikes")));
                        content.setVoiceCount(Integer.parseInt(object.getString("commentCount")));
                        content.setBookmarkCount(Integer.parseInt(object.getString("scriptShare")));
                        content.setFileName(object.getString("fileName"));

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
                        contents.add(content);
                    }
                    contentAdapter = new ContentAdapter(getActivity(), contents, togglePlayLayoutHandler, itemSelectHandler, bookmarkClickHandler, likeHanlder);
                    listView.setAdapter(contentAdapter);
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
            intent.putExtra("content", contents.get(msg.what));
            startActivity(intent);
        }
    };

    Handler bookmarkClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position = msg.what;
            boolean type = (boolean) msg.obj;
            if(type) {
                addBookmark(position);
            } else {
                deleteBookmark(position);
            }
        }
    };

    Handler likeHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position = msg.what;
            boolean type = (boolean) msg.obj;
            if(type) {
                addLike(position);
            } else {
                deleteLike(position);
            }
        }
    };

    private void addLike(final int position) {
        String scriptCode = contents.get(position).getCode();
        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.addLike(userSeq, scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                contents.get(position).setLiked(true);
                contents.get(position).setHeartCount(contents.get(position).getHeartCount() + 1);
                contentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLike(final int position) {
        String scriptCode = contents.get(position).getCode();
        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.deleteLike(userSeq, scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                contents.get(position).setLiked(false);
                contents.get(position).setHeartCount(contents.get(position).getHeartCount() - 1);
                contentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookmark(final int position) {
        String scriptCode = contents.get(position).getCode();
        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        String userId = preferences.getString("user_id", "");
        Call<String> call = apiInterface.share(userSeq, scriptCode, userId);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                contents.get(position).setBookmarked(true);
                contents.get(position).setBookmarkCount(contents.get(position).getBookmarkCount() + 1);
                contentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteBookmark(final int position) {
        String scriptCode = contents.get(position).getCode();
        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        String userSeq = preferences.getString("user_seq", "");
        Call<String> call = apiInterface.unshare(userSeq, scriptCode);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                contents.get(position).setBookmarked(false);
                contents.get(position).setBookmarkCount(contents.get(position).getBookmarkCount() - 1);
                contentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
