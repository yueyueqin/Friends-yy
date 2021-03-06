package com.cyan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/12/3.
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String DATA_BASE_NAME = "post_db";
    public static final int DATA_BASE_VERSION = 1;
    public static final String TABLE_NAME = "comment";
    public static final String TABLE_NAME0 ="reply";
    public DBHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }
    private SQLiteDatabase mDb;
    interface ComToMeTable{
        String _ID = "_id";
        String YOUR_ID="yourid";
        String USER_ID = "userid";
        String POST_ID="postid";
        String COMMENT_ID="commentid";
        String POST_CONTENT = "postcontent";
        String COMMENT_CONTENT= "commentcontent";
        String USER_NAME="username";
        String USER_HEAD="userhead";
        String CREATE_TIME="createtime";
    }
    interface ReplyToMeTable{
        String _ID = "_id";
        String YOUR_ID="yourid";
        String USER_ID = "userid";
        String POST_ID="postid";
        String COMMENT_ID="commentid";
        String POST_CONTENT = "postcontent";
        String COMMENT_CONTENT= "commentcontent";
        String USER_NAME="username";
        String USER_HEAD="userhead";
        String CREATE_TIME="createtime";
        String REPLY_CONTENT="replycontent";
        String POST_AUTHOR_NAME="postauthorname";
        String POST_AUTHOR_ID="postauthorid";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder comToMeStr=new StringBuilder();
        StringBuilder replyToMeStr=new StringBuilder();
        comToMeStr.append("CREATE TABLE IF NOT EXISTS ")
                .append(DBHelper.TABLE_NAME)
                .append(" ( ").append(ComToMeTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(ComToMeTable.YOUR_ID).append(" varchar(20),")
                .append(ComToMeTable.POST_ID).append(" varchar(20),")
                .append(ComToMeTable.COMMENT_ID).append(" varchar(20),")
                .append(ComToMeTable.USER_ID).append(" varchar(20),")
                .append(ComToMeTable.POST_CONTENT)
                .append(" varchar(200),")
                .append(ComToMeTable.COMMENT_CONTENT)
                .append(" varchar(200),")
                .append(ComToMeTable.USER_NAME).append(" varchar(20),")
                .append(ComToMeTable.USER_HEAD).append(" varchar(200),")
                .append(ComToMeTable.CREATE_TIME).append(" varchar(200));");
        replyToMeStr.append("CREATE TABLE IF NOT EXISTS ")
                .append(DBHelper.TABLE_NAME0)
                .append(" ( ").append(ReplyToMeTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(ReplyToMeTable.YOUR_ID).append(" varchar(20),")
                .append(ReplyToMeTable.POST_ID).append(" varchar(20),")
                .append(ReplyToMeTable.COMMENT_ID).append(" varchar(20),")
                .append(ReplyToMeTable.USER_ID).append(" varchar(20),")
                .append(ReplyToMeTable.POST_AUTHOR_ID).append(" varchar(20),")
                .append(ReplyToMeTable.POST_CONTENT)
                .append(" varchar(200),")
                .append(ReplyToMeTable.COMMENT_CONTENT)
                .append(" varchar(200),")
                .append(ReplyToMeTable.USER_NAME).append(" varchar(20),")
                .append(ReplyToMeTable.USER_HEAD).append(" varchar(200),")
                .append(ReplyToMeTable.REPLY_CONTENT).append(" varchar(200),")
                .append(ReplyToMeTable.POST_AUTHOR_NAME).append(" varchar(20),")
                .append(ReplyToMeTable.CREATE_TIME).append(" varchar(200));");
        db.execSQL(comToMeStr.toString());
        db.execSQL(replyToMeStr.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /**
     * 获取数据库操作对象
     * @param isWrite 是否可写
     * @return
     */
    public synchronized SQLiteDatabase getDatabase(boolean isWrite) {

        if(mDb == null || !mDb.isOpen()) {
            if(isWrite) {
                try {
                    mDb=getWritableDatabase();
                } catch(Exception e) {
                    // 当数据库不可写时
                    mDb=getReadableDatabase();
                    return mDb;
                }
            } else {
                mDb=getReadableDatabase();
            }
        }
        // } catch (SQLiteException e) {
        // // 当数据库不可写时
        // mDb = getReadableDatabase();
        // }
        return mDb;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        getDatabase(true);
        return mDb.insertOrThrow(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.update(table, values, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        getDatabase(false);
        return mDb.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql) {
        getDatabase(true);
        mDb.execSQL(sql);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
                        // final
                        String orderBy) {
        getDatabase(false);
        return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }
}
