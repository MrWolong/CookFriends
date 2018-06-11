package com.example.thomas.cookfriends.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.db.CooksDBManager;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ParseManager {
	private Context appContext;
	private static ParseManager instance = new ParseManager();
	private CookUser CookUser = BmobUser.getCurrentUser(CookUser.class);
	private BmobFile bmobFile;

	private ParseManager() {
	}

	public static ParseManager getInstance() {
		return instance;
	}

	public void onInit(Context context) {
		appContext = context.getApplicationContext();
	}
	//更新昵称
	public boolean updateParseNickName(final String nickname) {
		if(nickname != null) {
			CookUser.setNick(nickname);
			CookUser.update(new UpdateListener() {
				@Override
				public void done(BmobException e) {
					Toast.makeText(appContext,"更新完成", Toast.LENGTH_LONG);
				}
			});
			return true;
		}
		return false;
	}
	//获取用户表信息
	public void getContactInfos(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
		BmobQuery<CookUser> query = new BmobQuery<CookUser>();
		query.addWhereEqualTo("username",usernames);
		query.findObjects(new FindListener<CookUser>() {
			@Override
			public void done(List<CookUser> list, BmobException e) {
				if(list != null) {
					List<EaseUser> easeUsers = new ArrayList<>();
					for (CookUser CookUser : list) {
						EaseUser user = new EaseUser(CookUser.getUsername());
						user.setAvatar(CookUser.getAvatar());
						user.setNickname(CookUser.getNick());
						EaseCommonUtils.setUserInitialLetter(user);
						easeUsers.add(user);
					}
					callback.onSuccess(easeUsers);
				}else{
					callback.onError(e.getErrorCode(),e.getMessage());
				}
			}
		});
	}

	//获取目前用户信息
	public void asyncGetCurrentUserInfo(final EMValueCallBack<EaseUser> callback){
		final String username = EMClient.getInstance().getCurrentUser();
		asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {
			@Override
			public void onSuccess(EaseUser value) {
				callback.onSuccess(value);
			}
			@Override
			public void onError(int error, String errorMsg) {
					callback.onError(error, errorMsg);

			}
		});
	}
	//获取用户信息
	public void asyncGetUserInfo(final String username, final EMValueCallBack<EaseUser> callback){
		BmobQuery<CookUser> query = new BmobQuery<>();
		query.addWhereEqualTo("username",username);
		query.findObjects(new FindListener<CookUser>() {
			@Override
			public void done(List<CookUser> list, BmobException e) {
				if(e == null){
					for(CookUser CookUser : list){
						if(CookUser.getUsername().equals(username)) {
							if(callback!=null){
							EaseUser easeUser = CooksDBManager.getCooksDBManager(appContext).getContactList().get(username);
							if (easeUser != null) {
								easeUser.setNick(CookUser.getNick());
								if (CookUser.getAvatar() != null) {
									easeUser.setAvatar(CookUser.getAvatar());
								}
							} else {
								easeUser = new EaseUser(username);
								easeUser.setNick(CookUser.getNick());
								if (CookUser.getAvatar() != null) {
									easeUser.setAvatar(CookUser.getAvatar());
								}
							}
								callback.onSuccess(easeUser);
						}
						}
					}
				}else{
					if(callback != null){
						callback.onError(e.getErrorCode(), e.getMessage());
					}
				}
			}
		});
	}
	//更新头像
	public String uploadParseAvatar(String data) {
			bmobFile = new BmobFile(new File(data));
			bmobFile.uploadblock( new UploadFileListener() {
				@Override
				public void done(BmobException e) {
					if (e == null) {
						CookUser.setAvatar(bmobFile.getFileUrl());
					}
				}
			});
			return bmobFile.getUrl();
	}
}
