package com.example.thomas.cookfriends.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.thomas.cookfriends.fragment.ContactListFragment;
import com.example.thomas.cookfriends.fragment.ConversationListFragment;
import com.example.thomas.cookfriends.fragment.FindFragment;
import com.example.thomas.cookfriends.fragment.HomeFragment;
import com.example.thomas.cookfriends.fragment.MessageFragment;
import com.example.thomas.cookfriends.fragment.MineFragment;
import com.example.thomas.cookfriends.fragment.ShareFragment;

public class MessagePagerAdapter extends FragmentStatePagerAdapter {
    int nNumOfTabs;
    public MessagePagerAdapter(FragmentManager fm, int nNumOfTabs)
    {
        super(fm);
        this.nNumOfTabs = nNumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                ConversationListFragment conversationListFragment = new ConversationListFragment();
                return conversationListFragment;
            case 1:
                ContactListFragment contactListFragment = new ContactListFragment();
                return contactListFragment;
        }
        return null;
    }


    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}
