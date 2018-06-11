package com.example.thomas.cookfriends.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by yc on 2018/2/12.
 */

public class CollectionDetailActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.likes)
    ImageView likes;
    @BindView(R.id.likes_num)
    TextView likesNum;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.intro)
    TextView intro;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.likes_sum)
    TextView likesSum;
    @BindView(R.id.like_icon)
    ImageView likeIcon;
    @BindView(R.id.boundary)
    ImageView boundary;
    @BindView(R.id.listview)
    ListView listview;

    private String objectId;
    private List<Share> shareList;
    private Integer sum;
    private ShareFoodAdapter shareFoodAdapter;
    private CookUser user = BmobUser.getCurrentUser(CookUser.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection collection = new Collection();
                collection.setObjectId(objectId);
                BmobRelation relation = new BmobRelation();
                if (likes.getTag().equals(0)) {
                    sum = sum + 1;
                    likesNum.setText(Integer.toString(sum));
                    likes.setImageResource(R.drawable.ic_favorite_pink);
                    likes.setTag(1);
                    Toast.makeText(getApplicationContext(), "已喜欢该收藏夹~", Toast.LENGTH_SHORT).show();
                    relation.add(collection);
                    user.setFocusCollection(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                //Toast.makeText(getApplication(), "关联成功", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(getApplication(), "关联失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    collection.increment("like_sum");
                    collection.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                            }
                        }
                    });
                } else {
                    sum = sum - 1;
                    likesNum.setText(Integer.toString(sum));
                    likes.setImageResource(R.drawable.ic_favorite_border);
                    likes.setTag(0);
                    Toast.makeText(getApplicationContext(), "取消喜欢", Toast.LENGTH_SHORT).show();
                    relation.remove(collection);
                    user.setFocusCollection(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                //Toast.makeText(getApplication(), "关联成功", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(getApplication(), "关联失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    collection.increment("like_sum", -1);
                    collection.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //Toast.makeText(getApplication(), "该笔记本喜欢量减少成功", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(getApplication(), "该笔记本喜欢量减少失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ShareDetailActivity.class);
                intent.putExtra("share",shareList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        BmobQuery<Collection> query = new BmobQuery<Collection>();
        query.addWhereRelatedTo("focusCollection", new BmobPointer(user));
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        likes.setImageResource(R.drawable.ic_favorite_border);
                        likes.setTag(0);
                    } else {
                        likes.setImageResource(R.drawable.ic_favorite_border);
                        likes.setTag(0);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getObjectId().equals(objectId)) {
                                likes.setImageResource(R.drawable.ic_favorite_pink);
                                likes.setTag(1);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }

        });
        BmobQuery<Collection> query2 = new BmobQuery<Collection>();
        query2.getObject(objectId, new QueryListener<Collection>() {
            @Override
            public void done(Collection object, BmobException e) {
                if (e == null) {
                    String user_id = object.getUserOnlyId();
                    if (user.getObjectId().equals(user_id)) {
                        likes.setVisibility(View.GONE);
                        likesNum.setVisibility(View.GONE);
                        boundary.setVisibility(View.VISIBLE);
                        likesSum.setVisibility(View.VISIBLE);
                        likeIcon.setVisibility(View.VISIBLE);
                        toolbar.inflateMenu(R.menu.toolbar_menu);
                        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.delete:
                                        deleteCollection();
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            }
                        });
                    }
                    title.setText(object.getTitle().toString());
                    Glide.with(getApplicationContext()).load(object.getImage()).into(image);
                    sum = object.getLike_sum();
                    likesNum.setText(Integer.toString(object.getLike_sum()));
                    likesSum.setText("该收藏夹已收获" + Integer.toString(object.getLike_sum()) + "个");
                    if (object.getIntroduction().equals("")){
                        intro.setVisibility(View.GONE);
                    } else {
                        intro.setVisibility(View.VISIBLE);
                        intro.setText(object.getIntroduction().toString());
                    }
                    BmobQuery<CookUser> query3 = new BmobQuery<CookUser>();
                    query3.getObject(user_id, new QueryListener<CookUser>() {
                        @Override
                        public void done(CookUser cookUser, BmobException e) {
                            if(e == null){
                                Glide.with(getApplicationContext()).load(cookUser.getAvatar()).into(ivHead);
                                tvName.setText("创建者："+cookUser.getNick()+">>");
                            }
                        }
                    });

                    BmobQuery<Share> query4 = new BmobQuery<Share>();
                    query4.addWhereRelatedTo("collected", new BmobPointer(object));
                    query4.include("user");
                    query4.order("-createdAt");
                    query4.findObjects(new FindListener<Share>() {
                        @Override
                        public void done(List<Share> list, BmobException e) {
                            if(e == null){
                                if(list.size() == 0){
                                    Toast.makeText(getApplicationContext(), "该收藏夹还未添加任何分享哦~", Toast.LENGTH_SHORT).show();
                                }else{
                                    shareList = list;
                                    shareFoodAdapter = new ShareFoodAdapter(getApplicationContext(),list);
                                    listview.setAdapter(shareFoodAdapter);
                                    setListViewHeightBasedOnChildren();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "分享查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "收藏夹查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void initData() {
        objectId = getIntent().getStringExtra("objectId");
    }

    private void deleteCollection() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("确定删除此收藏夹吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Collection collection = new Collection();
                        collection.setObjectId(objectId);
                        collection.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                }else{
                                    Toast.makeText(getApplicationContext(), "删除失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        alertDialog.show();
    }

    private void setListViewHeightBasedOnChildren() {
        if (listview == null) {
            return;
        }
        if (shareFoodAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < shareFoodAdapter.getCount(); i++) {
            View listItem = shareFoodAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (shareFoodAdapter.getCount() - 1));
        listview.setLayoutParams(params);
    }

}
