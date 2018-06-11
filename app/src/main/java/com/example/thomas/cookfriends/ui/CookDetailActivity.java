package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CookStepListAdapter;
import com.example.thomas.cookfriends.bean.cookbean.ShowCookersInfo;
import com.example.thomas.cookfriends.db.CooksDBManager;
import com.example.thomas.cookfriends.widget.MyListView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CookDetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_back_cookery)
    ImageView ivBack;
    @BindView(R.id.tv_cook_title)
    TextView tvCookTitle;
    @BindView(R.id.header_img)
    ImageView headerImg;
    @BindView(R.id.tv_collect)
    CheckedTextView tvCollect;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.text_intro)
    TextView textIntro;
    @BindView(R.id.materials_layout)
    LinearLayout materialsLayout;
    @BindView(R.id.lv_steps)
    MyListView lvSteps;
    private CookStepListAdapter cookStepListAdapter;
    private ShowCookersInfo.Result.Data data;
    private View dialogView;
    private TextView dialog_number;
    private ImageView dialog_img;
    private TextView dialog_text_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_details);
        ButterKnife.bind(this);

        initData();
        initUI();
    }

    private void initData() {
        data = CooksDBManager.getCooksDBManager(this).getData();
        tvCookTitle.setText(data.getTitle());
    }


    private void initUI() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.details_dialog_layout, null);
        Glide.with(this).load(data.getAlbums().get(0)).into(headerImg);
        name.setText(data.getTitle());
        textIntro.setText(data.getImtro());
        tvCollect.setChecked(CooksDBManager.getCooksDBManager(this).isLikeNowCook(data.getId()));
        addMaterial();//添加食材
        cookStepListAdapter = new CookStepListAdapter(this,data.getSteps());
        lvSteps.setAdapter(cookStepListAdapter);
        lvSteps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowCookersInfo.Result.Data.Steps step = cookStepListAdapter.getItem(position);
                getDialog("第" + (position + 1) + "步", step.getImg(), step.getStep());
            }
        });
    }

    //添加食材
    private void addMaterial() {
        String ingredients = data.getIngredients();//主食材
        String burden = data.getBurden();//辅助食材
        String materialsStr = ingredients + ";" + burden;
        String[] split = materialsStr.split(";");
        //每行放两项  算用多少行
        int lines = (split.length % 2) == 0 ? split.length / 2 : split.length / 2 + 1;
        for (int i = 0; i < lines; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_material, null);
            materialsLayout.addView(view);
            String[] texts = split[i * 2].split(",");
            TextView tv1 = view.findViewById(R.id.text1);
            tv1.setText(texts[0]);
            TextView tv2 = view.findViewById(R.id.text2);
            tv2.setText(texts[1]);

            if (i == lines - 1 && split.length % 2 != 0) {

                continue;
            }
            texts = split[i * 2 + 1].split(",");
            TextView tv3 = view.findViewById(R.id.text3);
            tv3.setText(texts[0]);
            TextView tv4 = view.findViewById(R.id.text4);
            tv4.setText(texts[1]);
            texts = null;
        }
    }

    private AlertDialog dialog;

    private void getDialog(String itemId, String img, String infoText) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).create();
            dialog.show();
            dialog.setContentView(dialogView);
            dialog_number = dialogView.findViewById(R.id.dialog_number);
            dialog_img = dialogView.findViewById(R.id.dialog_img);
            dialog_text_info = dialogView.findViewById(R.id.dialog_text_info);
        }

        if (dialog != null) {
            dialog_number.setText(itemId);
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_placeholder);
            Glide.with(this).load(img).apply(options).into(dialog_img);
            dialog_text_info.setText(infoText);
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
