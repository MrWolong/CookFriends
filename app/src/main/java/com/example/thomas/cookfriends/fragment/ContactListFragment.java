package com.example.thomas.cookfriends.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.thomas.cookfriends.ui.chat.ChatActivity;
import com.example.thomas.cookfriends.ui.chat.GroupsActivity;
import com.example.thomas.cookfriends.ui.chat.NewFriendsMsgActivity;
import com.example.thomas.cookfriends.widget.ContactItemView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.example.thomas.cookfriends.app.DemoHelper;
import com.example.thomas.cookfriends.app.DemoHelper.DataSyncListener;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.db.InviteMessgeDao;
import com.example.thomas.cookfriends.db.UserDao;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import java.util.Hashtable;
import java.util.Map;


/**
 * contact list
 *
 */
public class ContactListFragment extends EaseContactListFragment {

    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    private EaseUser user;
    private AlertDialog.Builder builder;
    @Override
    protected void initView() {
        super.initView();
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);

//        registerForContextMenu(listView);
    }

    @Override
    public void refresh() {
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.refresh();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {

        //设置联系人数据
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user = (EaseUser)listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog = builder.create();
                View diaView = View.inflate(getContext(),R.layout.dialog_contact,null);
                dialog.setView(diaView);
                dialog.show();
                Button delete = (Button) diaView.findViewById(R.id.delete);
                Button black = diaView.findViewById(R.id.black);
                delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteContact(user);
                        InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                        dao.deleteMessage(user.getUsername());
                        dialog.dismiss();
                    }
                });
                black.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToBlacklist(user.getUsername());
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });




        contactSyncListener = new ContactSyncListener();
        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        if (DemoHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (DemoHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if(blackListSyncListener != null){
            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if(contactInfoSyncListener != null){
            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }


    protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    break;
                case R.id.group_item:
                    // 进入群聊列表页面
                    startActivity(new Intent(getActivity(), GroupsActivity.class));
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * delete contact
     *
     * @param tobeDeleteUser
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
                    // remove user from memory and database
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteContact(tobeDeleteUser.getUsername());
                    DemoHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            contactList.remove(tobeDeleteUser);
                            contactListLayout.refresh();

                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }).start();

    }

    class ContactSyncListener implements DataSyncListener{
        @Override
        public void onSyncComplete(final boolean success) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DataSyncListener{

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    class ContactInfoSyncListener implements DataSyncListener{

        @Override
        public void onSyncComplete(final boolean success) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
