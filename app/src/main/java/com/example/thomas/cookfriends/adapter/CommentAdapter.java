package com.example.thomas.cookfriends.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.Comment;
import com.example.thomas.cookfriends.utils.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/4/23.
 */

public class CommentAdapter extends BaseAdapter{
    private Context context;
    private List<Comment> data;

    public CommentAdapter(Context context,List<Comment> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Comment getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holder = null;
        if (null == convertView) {
            holder = new ViewHoler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            holder.ivAvatar = convertView.findViewById(R.id.iv_comment_avatar);
            holder.tvName = convertView.findViewById(R.id.tv_comment_name);
            holder.tvContent = convertView.findViewById(R.id.tv_comment_content);
            holder.tvTime = convertView.findViewById(R.id.tv_comment_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHoler) convertView.getTag();
        }
        Comment comment = data.get(position);
        Log.i("comment",""+comment);
        Glide.with(context).load(comment.getAuthor().getAvatar()).into(holder.ivAvatar);
        holder.tvName.setText(comment.getAuthor().getNick());
        if (comment.getReply() == null) {
            holder.tvContent.setText(comment.getContent());
        } else {
            holder.tvContent.setText(String.format("回复 %s：%s", comment.getNick(), comment.getContent()));
        }
        //显示发送时间
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comment.getCreatedAt()));
            long millis = calendar.getTimeInMillis();

            holder.tvTime.setText(DateTimeUtils.formatDate(millis));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHoler {
        CircleImageView ivAvatar;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
    }
}
