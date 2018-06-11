package com.example.thomas.cookfriends.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.UserLikeNum;
import com.example.thomas.cookfriends.ui.UserHomepageActivity;

import java.util.List;

public class HotUserAdapter extends RecyclerView.Adapter<HotUserAdapter.ViewHolder> {

    private Context context;
    private UserLikeNum userLikeNum;
    private List<UserLikeNum> data;
    private OnItemClickListener mOnItemClickListener = null;

    public HotUserAdapter(Context context,List<UserLikeNum> data) {
        this.context  = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_user, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接在adapter设置监听，点击后进行传值、页面跳转
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, UserHomepageActivity.class);
                intent.putExtra("username",data.get(position).getUser().getUsername());
                v.getContext().startActivity(intent);
                Log.e("TTT",data.get(position).getUser().getNick());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        userLikeNum = data.get(position);
        int pos = position + 1;
        String color = "#F44336";

        if (pos == 1) {
            holder.position.setTextColor(Color.parseColor(color));
            holder.position.setText("" + pos);
            holder.show.setVisibility(View.VISIBLE);
        } else if (pos == 2) {
            holder.position.setTextColor(Color.parseColor(color));
            holder.position.setText("" + pos);
            holder.show.setVisibility(View.VISIBLE);
        } else if (pos == 3) {
            holder.position.setTextColor(Color.parseColor(color));
            holder.position.setText("" + pos);
            holder.show.setVisibility(View.VISIBLE);
        } else {
            holder.position.setText("" + pos);
        }

        holder.name.setText(userLikeNum.getUser().getNick());
        holder.num.setText(userLikeNum.getLikeSum().toString());
        Glide.with(context).load(userLikeNum.getUser().getAvatar()).into(holder.imageView);

        //设置点击和长按事件
        if (mOnItemClickListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {//回调接口

        void onClick(int position);//单击，设置为view是因为我想获得子控件的值
    }

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView name;
        TextView num;
        TextView position;
        TextView show;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.hotname);
            num = (TextView) view.findViewById(R.id.hotnum);
            imageView = (ImageView) view.findViewById(R.id.hotimage);
            position = (TextView) view.findViewById(R.id.hotposition);
            show = (TextView) view.findViewById(R.id.hotshow);
        }
    }
}
