package com.example.thomas.cookfriends.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.cookbean.Category;
import com.example.thomas.cookfriends.widget.MyAdapter;

import java.util.List;


/**
 * Created by Administrator on 2018/3/28.
 */

public class CategoryAdapter extends MyAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> data) {
        super(context, data);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_gv_category,null);
            viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category category = getData().get(position);
        viewHolder.ivIcon.setImageResource(category.getImgId());
        viewHolder.tvTitle.setText(category.getTitle());
        return convertView;
    }
    class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
    }
}
