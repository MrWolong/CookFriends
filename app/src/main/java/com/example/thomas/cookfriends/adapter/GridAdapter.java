package com.example.thomas.cookfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.thomas.cookfriends.R;

import java.util.List;

/**
 * Created by dm on 16-4-24.
 * 发布图片Gridview适配器
 */
public class GridAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater mLayoutInflater;

    public GridAdapter(Context context,List<String> list) {
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_gv_share,
                    parent, false);
            viewHolder.imageView = convertView
                    .findViewById(R.id.img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        String url = getItem(position);
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(600, 200);
        Glide.with(parent.getContext())
                .load(url)
                .apply(options)
                .into(viewHolder.imageView);
        return convertView;
    }

    private static class MyGridViewHolder {
        ImageView imageView;
    }
}

