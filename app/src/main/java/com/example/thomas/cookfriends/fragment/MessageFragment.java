package com.example.thomas.cookfriends.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.MessagePagerAdapter;
import com.example.thomas.cookfriends.ui.chat.AddContactActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MessageFragment extends Fragment {
    @BindView(R.id.tv_head_msg)
    TextView tvHeadMsg;
    @BindView(R.id.tv_head_friend)
    TextView tvHeadFriend;
    @BindView(R.id.iv_head_add)
    ImageView imgHeadAdd;
    @BindView(R.id.mfpage)
    ViewPager viewPager;
    private MessagePagerAdapter messagePagerAdapter;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initFragment();
        return contentView;
    }

    private void initFragment() {
        viewPager.setOffscreenPageLimit(2);
        messagePagerAdapter = new MessagePagerAdapter(getChildFragmentManager(), 2);
        viewPager.setAdapter(messagePagerAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_head_msg, R.id.tv_head_friend, R.id.iv_head_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_head_msg:
                tvHeadMsg.setTextColor(getResources().getColor(R.color.white));
                tvHeadMsg.setBackgroundResource(R.drawable.ic_left_blueshape);
                tvHeadFriend.setTextColor(getResources().getColor(
                        R.color.tab_select_color));
                tvHeadFriend.setBackgroundResource(R.drawable.ic_right_whiteshape);
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_head_friend:
                tvHeadMsg.setTextColor(getResources().getColor(
                        R.color.tab_select_color));
                tvHeadMsg.setBackgroundResource(R.drawable.ic_left_whiteshape);
                tvHeadFriend.setTextColor(getResources().getColor(R.color.white));
                tvHeadFriend.setBackgroundResource(R.drawable.ic_right_blueshape);
                viewPager.setCurrentItem(1);
                break;
            case R.id.iv_head_add:
                startActivity(new Intent(getActivity(), AddContactActivity.class));
                break;
        }
    }

}
