package com.example.thomas.cookfriends.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;
import com.example.thomas.cookfriends.ui.ImagePagerActivity;
import com.example.thomas.cookfriends.utils.DateTimeUtils;
import com.example.thomas.cookfriends.widget.MyAdapter;
import com.example.thomas.cookfriends.widget.NineGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class ShareFoodAdapter extends BaseAdapter {
    private Context context;
    private List<Share> data;
    public ShareFoodAdapter(Context context, List<Share> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Share getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoler holder = null;
        if (null == view) {
            holder = new ViewHoler();
            view = LayoutInflater.from(context).inflate(R.layout.item_share_fragment, null);
            holder.iv_head = view.findViewById(R.id.item_iv_head);
            holder.tv_nickname = view.findViewById(R.id.item_tv_nickname);
            holder.tv_time = view.findViewById(R.id.item_tv_time);
            holder.tv_content = view.findViewById(R.id.item_tv_content);
            holder.nineGridView = view.findViewById(R.id.gv_img);
            view.setTag(holder);
        } else {
            holder = (ViewHoler) view.getTag();
        }
        final Share share = getItem(i);
        CookUser user = getItem(i).getUser();
        //异步加载用户头像
        Glide.with(context).load(user.getAvatar()).into(holder.iv_head);
        holder.tv_nickname.setText(user.getNick());
        holder.tv_content.setText(share.getContent());
        //显示发送时间
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getItem(i).getCreatedAt()));
            long millis = calendar.getTimeInMillis();

            holder.tv_time.setText(DateTimeUtils.formatDate(millis));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //判断是否有图片
        if (getItem(i).getImagePaths() != null ) {
            holder.nineGridView.setVisibility(View.VISIBLE);
            holder.nineGridView.setAdapter(new GridAdapter(context, getItem(i).getImagePaths()));
        }else{
            holder.nineGridView.setVisibility(View.GONE);
        }
        holder.nineGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, share.getImagePaths());
            }
        });
        holder.nineGridView.setOnTouchInvalidPositionListener(new NineGridView.OnTouchInvalidPositionListener() {
            @Override
            public boolean onTouchInvalidPosition(int motionEvent) {
                return false;
            }
        });

        return view;
    }

    private void imageBrower(int position, List<String> imagePaths) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (java.io.Serializable) imagePaths);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    class ViewHoler {
        CircleImageView iv_head;
        TextView tv_nickname;
        TextView tv_content;
        TextView tv_time;
        NineGridView nineGridView;
    }
}