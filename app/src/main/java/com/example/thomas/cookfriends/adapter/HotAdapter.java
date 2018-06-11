package com.example.thomas.cookfriends.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.ui.HotCollectionActivity;
import com.example.thomas.cookfriends.ui.HotShareActivity;
import com.example.thomas.cookfriends.ui.HotUserActivity;
import java.util.List;


public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {
    private Intent intent;
    private List<Integer> list;
    private OnItemClickListener mOnItemClickListener = null;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            image = view.findViewById(R.id.hotimage);
        }
    }
    public HotAdapter(List<Integer> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接在adapter设置监听，点击后进行传值、页面跳转
                int position = holder.getAdapterPosition();
                switch (position) {
                    case 0:
                        intent = new Intent(v.getContext(), HotUserActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(v.getContext(), HotShareActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(v.getContext(), HotCollectionActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        return holder;
    }
    public interface OnItemClickListener {//回调接口

        void onClick(int position);//单击，设置为view是因为我想获得子控件的值

        void onLongClick(int position);//长按
    }

    //定义这个接口的set方法，便于调用

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Integer image_id = list.get(position);
        holder.image.setImageResource(image_id);

        //设置点击和长按事件
        if (mOnItemClickListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}