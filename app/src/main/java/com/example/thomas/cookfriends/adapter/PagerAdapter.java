package com.example.thomas.cookfriends.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.thomas.cookfriends.fragment.FindFragment;
import com.example.thomas.cookfriends.fragment.HomeFragment;
import com.example.thomas.cookfriends.fragment.MessageFragment;
import com.example.thomas.cookfriends.fragment.MineFragment;
import com.example.thomas.cookfriends.fragment.ShareFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int nNumOfTabs;
    public PagerAdapter(FragmentManager fm, int nNumOfTabs)
    {
        super(fm);
        this.nNumOfTabs = nNumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ShareFragment shareFragment = new ShareFragment();
                return shareFragment;
            case 2:
                MessageFragment messageFragment = new MessageFragment();
                return messageFragment;
            case 3:
                FindFragment findFragment = new FindFragment();
                return findFragment;
            case 4:
                MineFragment mineFragment = new MineFragment();
                return mineFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}
