package com.four_leader.snl;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Category> arrayList;
    LayoutInflater inflater;

    public CategoryAdapter(Context context, ArrayList<Category> dataList) {
        super();
        this.context = context;
        arrayList = dataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_category, null);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.categoryText = convertView.findViewById(R.id.categoryText);
        holder.backgroundLayout = convertView.findViewById(R.id.backgroundLayout);

        Category data = arrayList.get(position);

        holder.categoryText.setText(data.getName());
        if(data.getChecked()) {
            holder.backgroundLayout.setBackgroundResource(R.drawable.category_round_rect_on);
        } else {
            holder.backgroundLayout.setBackgroundResource(R.drawable.category_round_rect_off);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView categoryText;
        public LinearLayout backgroundLayout;
    }


}
