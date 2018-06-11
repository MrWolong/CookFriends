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

import android.content.Context;
import com.example.thomas.cookfriends.bean.CookUser;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;
import java.util.Map;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";
	Context context;
	public UserDao(Context context) {
		this.context = context;
	}
	/**
	 * save contact list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<CookUser> contactList) {
		CooksDBManager.getCooksDBManager(context).saveContactList(contactList);
	}

	/**
	 * get contact list
	 * 
	 * @return
	 */
	public Map<String, EaseUser> getContactList() {
		
	    return CooksDBManager.getCooksDBManager(context).getContactList();
	}
	
	/**
	 * delete a contact
	 * @param username
	 */
	public void deleteContact(String username){
		CooksDBManager.getCooksDBManager(context).deleteContact(username);
	}
	
	/**
	 * save a contact
	 * @param user
	 */
	public void saveContact(CookUser user){
		CooksDBManager.getCooksDBManager(context).saveContact(user);
	}
	
	public void setDisabledGroups(List<String> groups){
		CooksDBManager.getCooksDBManager(context).setDisabledGroups(groups);
    }
    
    public List<String> getDisabledGroups(){
        return CooksDBManager.getCooksDBManager(context).getDisabledGroups();
    }
    
    public void setDisabledIds(List<String> ids){
		CooksDBManager.getCooksDBManager(context).setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return CooksDBManager.getCooksDBManager(context).getDisabledIds();
    }
}
