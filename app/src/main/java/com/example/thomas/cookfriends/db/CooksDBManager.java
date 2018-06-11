package com.example.thomas.cookfriends.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thomas.cookfriends.app.Constant;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.chatbean.InviteMessage;
import  com.example.thomas.cookfriends.bean.chatbean.InviteMessage.InviteMessageStatus;
import com.example.thomas.cookfriends.bean.cookbean.ShowCookersInfo;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CooksDBManager {
    private SQLiteDatabase db;
    private ShowCookersInfo.Result.Data data;
    private static CooksDBManager cooksDBManager;

    private CooksDBManager(Context context) {
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public static CooksDBManager getCooksDBManager(Context context) {
        if (cooksDBManager == null) {
            cooksDBManager = new CooksDBManager(context);
        }
        return cooksDBManager;
    }
    /**
     * 增添数据
     */
    public void insertData(ShowCookersInfo.Result.Data data) {
        Cursor cursor = db.rawQuery("select * from " + DBHelper.TABLE_TEXT + " where " + DBHelper.ID + "=" + data.getId(), null);
        if (cursor.getCount() == 1) {
            cursor.close();
            db.execSQL("update " + DBHelper.TABLE_TEXT + " set " + DBHelper.HISTORY_LOOK + " = " + 1 + " where " + DBHelper.ID + "=" + data.getId());
            return;
        }
        cursor.close();
        String textSql = "insert into " + DBHelper.TABLE_TEXT + "(" + DBHelper.ID + "," + DBHelper.TITLE + "," +
                DBHelper.TAGS + "," + DBHelper.IMTRO + "," + DBHelper.INGREDIENTS + "," + DBHelper.BURDEN + "," +
                DBHelper.ALBUMS + "," +
                DBHelper.HISTORY_LOOK + ")" +
                " values('" + data.getId() + "','" + data.getTitle() + "','" + data.getTags() + "','" + data.getImtro() + "','" + data.getIngredients()
                + "','" + data.getBurden() + "','" + data.getAlbums().get(0) + "',1)";
        db.execSQL(textSql);
        for (int i = 0, length = data.getSteps().size(); i < length; i++) {
            String imgsSql = "insert into " + DBHelper.TABLE_IMGS + "(" + DBHelper.ID + "," + DBHelper.IMG + "," + DBHelper.STEP + ")" + " values('" + data.getId() + "','" +
                    data.getSteps().get(i).getImg() + "','" + data.getSteps().get(i).getStep() + "')";
            db.execSQL(imgsSql);
        }
    }

    /**
     * 当前id的菜谱是否是添加了收藏
     *
     */
    public boolean isLikeNowCook(String id) {
        boolean isLike;
        Cursor cursor = db.rawQuery("select " + DBHelper.MY_LIKE + " from " + DBHelper.TABLE_TEXT + " where " + DBHelper.ID + "=" + id, null);
        cursor.moveToNext();
        isLike = cursor.getInt(0) == 1;
        cursor.close();
        return isLike;
    }

    public ShowCookersInfo.Result.Data getData() {
        return data;
    }

    public void setData(ShowCookersInfo.Result.Data data) {
        this.data = data;
    }

    /**
     * save contact list
     *
     * @param contactList
     */
    synchronized public void saveContactList(List<CookUser> contactList) {
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (CookUser user : contactList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
                if(user.getNick() != null)
                    values.put(UserDao.COLUMN_NAME_NICK, user.getNick());
                if(user.getAvatar() != null)
                    values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
                db.replace(UserDao.TABLE_NAME, null, values);
            }
        }
    }

    /**
     * get contact list
     *
     * @return
     */
    synchronized public Map<String, EaseUser> getContactList() {
        Map<String, EaseUser> users = new Hashtable<String, EaseUser>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                EaseUser user = new EaseUser(username);
                user.setNick(nick);
                user.setAvatar(avatar);
                if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)
                        || username.equals(Constant.CHAT_ROOM)|| username.equals(Constant.CHAT_ROBOT)) {
                    user.setInitialLetter("");
                } else {
                    EaseCommonUtils.setUserInitialLetter(user);
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * delete a contact
     * @param username
     */
    synchronized public void deleteContact(String username){
        if(db.isOpen()){
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }

    /**
     * save a contact
     * @param user
     */
    synchronized public void saveContact(CookUser user){
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
        if(user.getNick() != null)
            values.put(UserDao.COLUMN_NAME_NICK, user.getNick());
        if(user.getAvatar() != null)
            values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
        if(db.isOpen()){
            db.replace(UserDao.TABLE_NAME, null, values);
        }
    }

    public void setDisabledGroups(List<String> groups){
        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
    }

    public List<String>  getDisabledGroups(){
        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
    }

    public void setDisabledIds(List<String> ids){
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }

    public List<String> getDisabledIds(){
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }

    synchronized private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();

        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null,null);
        }
    }

    synchronized private List<String> getList(String column){
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }

        cursor.close();

        String[] array = strVal.split("$");

        if(array.length > 0){
            List<String> list = new ArrayList<String>();
            Collections.addAll(list, array);
            return list;
        }

        return null;
    }

    /**
     * save a message
     * @param message
     * @return  return cursor of the message
     */
    public synchronized Integer saveMessage(InviteMessage message){
        int id = -1;
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_FROM, message.getFrom());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_ID, message.getGroupId());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_Name, message.getGroupName());
            values.put(InviteMessgeDao.COLUMN_NAME_REASON, message.getReason());
            values.put(InviteMessgeDao.COLUMN_NAME_TIME, message.getTime());
            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, message.getStatus().ordinal());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUPINVITER, message.getGroupInviter());
            db.insert(InviteMessgeDao.TABLE_NAME, null, values);

            Cursor cursor = db.rawQuery("select last_insert_rowid() from " + InviteMessgeDao.TABLE_NAME,null);
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
            }

            cursor.close();
        }
        return id;
    }

    /**
     * update message
     * @param msgId
     * @param values
     */
    synchronized public void updateMessage(int msgId,ContentValues values){
        if(db.isOpen()){
            db.update(InviteMessgeDao.TABLE_NAME, values, InviteMessgeDao.COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(msgId)});
        }
    }

    /**
     * get messges
     * @return
     */
    synchronized public List<InviteMessage> getMessagesList(){
        List<InviteMessage> msgs = new ArrayList<InviteMessage>();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from " + InviteMessgeDao.TABLE_NAME + " desc",null);
            while(cursor.moveToNext()){
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_FROM));
                String groupid = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_ID));
                String groupname = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_Name));
                String reason = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_REASON));
                long time = cursor.getLong(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_STATUS));
                String groupInviter = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUPINVITER));

                msg.setId(id);
                msg.setFrom(from);
                msg.setGroupId(groupid);
                msg.setGroupName(groupname);
                msg.setReason(reason);
                msg.setTime(time);
                msg.setGroupInviter(groupInviter);
                msg.setStatus(InviteMessageStatus.values()[status]);
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }

    /**
     * delete invitation message
     * @param from
     */
    synchronized public void deleteMessage(String from){
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_FROM + " = ?", new String[]{from});
        }
    }

    /**
     * delete invitation message
     * @param groupId
     */
    synchronized public void deleteGroupMessage(String groupId){
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_GROUP_ID + " = ?", new String[]{groupId});
        }
    }

    /**
     * delete invitation message
     * @param groupId
     */
    synchronized public void deleteGroupMessage(String groupId, String from){
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_GROUP_ID + " = ? AND " + InviteMessgeDao.COLUMN_NAME_FROM + " = ? ",
                    new String[]{groupId, from});
        }
    }

    synchronized int getUnreadNotifyCount(){
        int count = 0;
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select " + InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT + " from " + InviteMessgeDao.TABLE_NAME, null);
            if(cursor.moveToFirst()){
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    synchronized void setUnreadNotifyCount(int count){
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT, count);

            db.update(InviteMessgeDao.TABLE_NAME, values, null,null);
        }
    }

}
