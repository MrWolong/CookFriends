package com.example.thomas.cookfriends.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.Collection;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class CollectionAdapter extends BaseQuickAdapter<Collection,BaseViewHolder> {

    public CollectionAdapter(int layoutResId, @Nullable List<Collection> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Collection item) {
        Glide.with(mContext).load(item.getImage()).into(((ImageView) holder.getView(R.id.image)));
        holder.setText(R.id.name,item.getTitle());
    }
}
