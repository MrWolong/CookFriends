package com.example.thomas.cookfriends.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.cookbean.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class CookAdapter extends BaseQuickAdapter<Category,BaseViewHolder> {

    public CookAdapter(int layoutResId, @Nullable List<Category> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Category item) {
        holder.setText(R.id.tv_cook_name, item.getTitle());
        Glide.with(mContext).load(item.getImgId()).into((ImageView) holder.getView(R.id.iv_cook_image));
    }
}
