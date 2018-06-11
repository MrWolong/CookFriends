package com.example.thomas.cookfriends.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "cooks.db";
    public static final int VERSION = 1;
    /**
     * 是否添加了收藏：0不是，1是
     */
    public static final String MY_LIKE = "my_like";
    /**
     * 是否是观看历史浏览记录：0不是，1是
     */
    public static final String HISTORY_LOOK = "history";
    public static final String TABLE_TEXT = "cooks_info";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    /**
     * 关于菜的功效、标签
     */
    public static final String TAGS = "tags";
    /**
     * 菜品的简介
     */
    public static final String IMTRO = "imtro";
    /**
     * 菜品所需材料
     */
    public static final String INGREDIENTS = "ingredients";
    /**
     * 辅助材料
     */
    public static final String BURDEN = "burden";
    /**
     * 图片
     */
    public static final String ALBUMS = "albums";
    public static final String CREATE_TEXT_TABLE = "create table " + TABLE_TEXT + "(" +
            ID + " integer primary key," +
            TITLE + "," +
            TAGS + "," +
            IMTRO + "," +
            INGREDIENTS + "," +
            BURDEN + "," +
            ALBUMS + "," +
            HISTORY_LOOK + " integer default 0," +
            MY_LIKE + " integer default 0)";


    public static final String TABLE_IMGS = "cooks_imgs";
    public static final String IMG = "img";
    public static final String STEP = "step";
    public static String CREATE_IMGS_TABLE = "create table " + TABLE_IMGS + "(" +
            ID + " integer," +
            IMG + "," +
            STEP + ")";
    private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
            + UserDao.TABLE_NAME + " ("
            + UserDao.COLUMN_NAME_NICK + " TEXT, "
            + UserDao.COLUMN_NAME_AVATAR + " TEXT, "
            + UserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

    private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
            + InviteMessgeDao.TABLE_NAME + " ("
            + InviteMessgeDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_TIME + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUPINVITER + " TEXT); ";

    private static final String CREATE_PREF_TABLE = "CREATE TABLE "
            + UserDao.PREF_TABLE_NAME + " ("
            + UserDao.COLUMN_NAME_DISABLED_GROUPS + " TEXT, "
            + UserDao.COLUMN_NAME_DISABLED_IDS + " TEXT);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEXT_TABLE);
        db.execSQL(CREATE_IMGS_TABLE);
        db.execSQL(USERNAME_TABLE_CREATE);
        db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
        db.execSQL(CREATE_PREF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}