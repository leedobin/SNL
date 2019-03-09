package com.four_leader.snl.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.four_leader.snl.R;
import com.four_leader.snl.main.vo.DefaultCategory;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {

    private ArrayList<DefaultCategory> mList;
    private Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView categoryText;
        protected ImageView bottomBar;


        public CustomViewHolder(View view) {
            super(view);
            this.categoryText = view.findViewById(R.id.categoryText);
            this.bottomBar = view.findViewById(R.id.bottomBar);
        }
    }


    public CategoryAdapter(Context mContext, ArrayList<DefaultCategory> list) {
        this.context = mContext;
        this.mList = list;
    }

    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.main_category_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }



    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출됩니다.
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.categoryText.setText(mList.get(position).getName());

        if(mList.get(position).getChecked()) {
            viewholder.bottomBar.setVisibility(View.VISIBLE);
        } else {
            viewholder.bottomBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}