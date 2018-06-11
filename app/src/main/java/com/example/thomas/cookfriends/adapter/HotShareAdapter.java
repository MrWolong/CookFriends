package com.example.thomas.cookfriends.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.Share;

import java.util.List;


public class HotShareAdapter extends BaseQuickAdapter<Share,BaseViewHolder>{

    public HotShareAdapter(int layoutResId, @Nullable List<Share> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Share item) {
        if(item.getImagePaths() != null){
            Glide.with(mContext).load(item.getImagePaths().get(0)).into(((ImageView) holder.getView(R.id.image)));
        }else{
            holder.getView(R.id.image).setVisibility(View.GONE);
        }
        holder.setText(R.id.content,item.getContent());
    }

}