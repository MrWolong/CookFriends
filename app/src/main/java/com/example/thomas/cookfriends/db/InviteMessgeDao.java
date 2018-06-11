/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.thomas.cookfriends.db;

import android.content.ContentValues;
import android.content.Context;

import com.example.thomas.cookfriends.bean.chatbean.InviteMessage;

import java.util.List;

public class InviteMessgeDao {
	static final String TABLE_NAME = "new_friends_msgs";
	static final String COLUMN_NAME_ID = "id";
	static final String COLUMN_NAME_FROM = "username";
	static final String COLUMN_NAME_GROUP_ID = "groupid";
	static final String COLUMN_NAME_GROUP_Name = "groupname";
	
	static final String COLUMN_NAME_TIME = "time";
	static final String COLUMN_NAME_REASON = "reason";
	public static final String COLUMN_NAME_STATUS = "status";
	static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
	static final String COLUMN_NAME_GROUPINVITER = "groupinviter";
	
	static final String COLUMN_NAME_UNREAD_MSG_COUNT = "unreadMsgCount";
	Context context;
		
	public InviteMessgeDao(Context context){
		this.context = context;
	}
	
	/**
	 * save message
	 * @param message
	 * @return  return cursor of the message
	 */
	public Integer saveMessage(InviteMessage message){
		return CooksDBManager.getCooksDBManager(context).saveMessage(message);
	}
	
	/**
	 * update message
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(int msgId,ContentValues values){
		CooksDBManager.getCooksDBManager(context).updateMessage(msgId, values);
	}
	
	/**
	 * get messges
	 * @return
	 */
	public List<InviteMessage> getMessagesList(){
		return CooksDBManager.getCooksDBManager(context).getMessagesList();
	}
	
	public void deleteMessage(String from){
		CooksDBManager.getCooksDBManager(context).deleteMessage(from);
	}

	public void deleteGroupMessage(String groupId) {
		CooksDBManager.getCooksDBManager(context).deleteGroupMessage(groupId);
	}

	public void deleteGroupMessage(String groupId, String from) {
		CooksDBManager.getCooksDBManager(context).deleteGroupMessage(groupId, from);
	}
	
	public int getUnreadMessagesCount(){
	    return CooksDBManager.getCooksDBManager(context).getUnreadNotifyCount();
	}
	
	public void saveUnreadMessageCount(int count){
		CooksDBManager.getCooksDBManager(context).setUnreadNotifyCount(count);
	}
}
