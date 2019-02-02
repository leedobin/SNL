package com.four_leader.snl.onetime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.four_leader.snl.R;

import java.util.ArrayList;

public class SetDefaultCategoryAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<DefaultCategory> arrayList;
    LayoutInflater inflater;

    public SetDefaultCategoryAdapter(Context context, ArrayList<DefaultCategory> dataList) {
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

        DefaultCategory data = arrayList.get(position);

        holder.categoryText.setText(data.getName());
        if (data.getChecked()) {
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
