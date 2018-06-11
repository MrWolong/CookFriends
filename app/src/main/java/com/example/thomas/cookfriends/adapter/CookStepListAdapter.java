package com.example.thomas.cookfriends.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.cookbean.ShowCookersInfo;
import com.example.thomas.cookfriends.widget.MyAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class CookStepListAdapter extends MyAdapter<ShowCookersInfo.Result.Data.Steps>{
    public CookStepListAdapter(Context context, List<ShowCookersInfo.Result.Data.Steps> data) {
        super(context, data);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_cook_step,null);
            viewHolder.img = convertView.findViewById(R.id.img);
            viewHolder.stepNum = convertView.findViewById(R.id.step_num);
            viewHolder.content = convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ShowCookersInfo.Result.Data.Steps cookStep = getItem(position);
        viewHolder.stepNum.setText(position + 1 + "");
        viewHolder.content.setText(cookStep.getStep().substring(2));
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_placeholder);
        Glide.with(getContext()).load(cookStep.getImg()).apply(options).into(viewHolder.img);
        return convertView;
    }
    class ViewHolder{
        TextView stepNum;
        ImageView img;
        TextView content;

    }
}
