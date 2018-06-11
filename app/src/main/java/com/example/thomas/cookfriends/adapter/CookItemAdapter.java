package com.example.thomas.cookfriends.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.cookbean.ShowCookersInfo;

import java.util.List;

public class CookItemAdapter extends BaseQuickAdapter<ShowCookersInfo.Result.Data, BaseViewHolder> {

    public CookItemAdapter(int layoutResId, @Nullable List<ShowCookersInfo.Result.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ShowCookersInfo.Result.Data item) {
        holder.setText(R.id.cook_name, item.getTitle());
        holder.setText(R.id.cook_tags, item.getTags());
        holder.setText(R.id.cook_ingredients, item.getIngredients());
        holder.setText(R.id.cook_burden, item.getBurden());
        Glide.with(mContext).load(item.getAlbums().get(0)).into((ImageView) holder.getView(R.id.cook_img));
    }
}
