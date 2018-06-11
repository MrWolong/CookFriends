package com.example.thomas.cookfriends.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.app.DemoHelper;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.ui.LoginActivity;
import com.example.thomas.cookfriends.ui.MainActivity;
import com.example.thomas.cookfriends.ui.MyCollectionActivity;
import com.example.thomas.cookfriends.ui.MyLikeActivity;
import com.example.thomas.cookfriends.ui.MyShareActivity;
import com.example.thomas.cookfriends.ui.UserMessageActivity;
import com.example.thomas.cookfriends.ui.chat.BlacklistActivity;
import com.hyphenate.EMCallBack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class MineFragment extends Fragment {
    @BindView(R.id.bgImg)
    ImageView bgImg;
    @BindView(R.id.headImg)
    CircleImageView headImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.brief_intro)
    TextView briefIntro;
    @BindView(R.id.listview)
    ListView listView;
    Unbinder unbinder;
    private List<Map<String,Object>> data = new ArrayList<>();
    private SimpleAdapter simpleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initData();
        initView();
        initListeer();
        return contentView;
    }

    private void initData() {
        Integer[] images = {R.drawable.ic_account_circle, R.drawable.ic_cloud_queue_black, R.drawable.ic_import_contacts,
                R.drawable.ic_favorite, R.drawable.ic_announcement, R.drawable.ic_exit};
        String[] message = {"账号资料", "我的分享", "我的收藏夹", "我的喜欢", "黑名单", "退出登录"};
        Integer[] icons = {R.drawable.ic_keyboard_arrow_right, R.drawable.ic_keyboard_arrow_right, R.drawable.ic_keyboard_arrow_right,
                R.drawable.ic_keyboard_arrow_right, R.drawable.ic_keyboard_arrow_right, R.drawable.ic_keyboard_arrow_right};
        for (int i = 0; i < 6; i++) {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("image", images[i]);
            temp.put("message", message[i]);
            temp.put("icon", icons[i]);
            data.add(temp);
        }
        simpleAdapter = new SimpleAdapter(getApplicationContext(), data, R.layout.item_mine_fragment, new String[] {"image","message","icon"}, new int[] {R.id.image, R.id.message, R.id.icon});
        listView.setAdapter(simpleAdapter);
        setListViewHeightBasedOnChildren();
    }
    // 动态修改listview高度，使得listview能完全展开
    private void setListViewHeightBasedOnChildren() {
        if (listView == null) {
            return;
        }
        if (simpleAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //Toast.makeText(getApplication(), Integer.toString(simpleAdapter.getCount()), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < simpleAdapter.getCount(); i++) {
            View listItem = simpleAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (simpleAdapter.getCount() + 1));
        listView.setLayoutParams(params);
    }

    private void initView() {
        CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
        Glide.with(getContext()).load(cookUser.getAvatar()).into(headImg);
        Glide.with(getContext()).load(cookUser.getCoverPage()).into(bgImg);
        name.setText(cookUser.getNick());
        if (cookUser.getSignature() == null || cookUser.getSignature().equals("")) {
            briefIntro.setText("简介：这个人很懒，什么也没留下...");
        } else
            briefIntro.setText("简介："+cookUser.getSignature());

    }

    private void initListeer() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), UserMessageActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), MyShareActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), MyCollectionActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), MyLikeActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), BlacklistActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        createDialog();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void createDialog() {
            AlertDialog.Builder quitDialog = new AlertDialog.Builder(getActivity());
            quitDialog.setMessage("是否退出登录?");
            quitDialog.setNegativeButton("取消", null);
            quitDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    String st = getResources().getString(R.string.Are_logged_out);
                    pd.setMessage(st);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    DemoHelper.getInstance().logout(true,new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    CookUser.logOut();
                                    pd.dismiss();
                                    // show login screen
                                    ((MainActivity) getActivity()).finish();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));

                                }
                            });
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String message) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
            quitDialog.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
