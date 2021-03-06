package de.greenrobot.daoexample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.daoexample.ReplyToMe;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REPLY_TO_ME".
*/
public class ReplyToMeDao extends AbstractDao<ReplyToMe, Long> {

    public static final String TABLENAME = "REPLY_TO_ME";

    /**
     * Properties of entity ReplyToMe.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Yourid = new Property(1, String.class, "yourid", false, "YOURID");
        public final static Property Postid = new Property(2, String.class, "postid", false, "POSTID");
        public final static Property Userid = new Property(3, String.class, "userid", false, "USERID");
        public final static Property User_name = new Property(4, String.class, "user_name", false, "USER_NAME");
        public final static Property Head = new Property(5, String.class, "head", false, "HEAD");
        public final static Property Post_content = new Property(6, String.class, "post_content", false, "POST_CONTENT");
        public final static Property Comment_content = new Property(7, String.class, "comment_content", false, "COMMENT_CONTENT");
        public final static Property Comment_id = new Property(8, String.class, "comment_id", false, "COMMENT_ID");
        public final static Property Reply_content = new Property(9, String.class, "reply_content", false, "REPLY_CONTENT");
        public final static Property Post_author_name = new Property(10, String.class, "post_author_name", false, "POST_AUTHOR_NAME");
        public final static Property Post_author_id = new Property(11, String.class, "post_author_id", false, "POST_AUTHOR_ID");
        public final static Property Create_time = new Property(12, java.util.Date.class, "create_time", false, "CREATE_TIME");
    };


    public ReplyToMeDao(DaoConfig config) {
        super(config);
    }
    
    public ReplyToMeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REPLY_TO_ME\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"YOURID\" TEXT," + // 1: yourid
                "\"POSTID\" TEXT," + // 2: postid
                "\"USERID\" TEXT," + // 3: userid
                "\"USER_NAME\" TEXT," + // 4: user_name
                "\"HEAD\" TEXT," + // 5: head
                "\"POST_CONTENT\" TEXT," + // 6: post_content
                "\"COMMENT_CONTENT\" TEXT," + // 7: comment_content
                "\"COMMENT_ID\" TEXT," + // 8: comment_id
                "\"REPLY_CONTENT\" TEXT," + // 9: reply_content
                "\"POST_AUTHOR_NAME\" TEXT," + // 10: post_author_name
                "\"POST_AUTHOR_ID\" TEXT," + // 11: post_author_id
                "\"CREATE_TIME\" INTEGER);"); // 12: create_time
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REPLY_TO_ME\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ReplyToMe entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String yourid = entity.getYourid();
        if (yourid != null) {
            stmt.bindString(2, yourid);
        }
 
        String postid = entity.getPostid();
        if (postid != null) {
            stmt.bindString(3, postid);
        }
 
        String userid = entity.getUserid();
        if (userid != null) {
            stmt.bindString(4, userid);
        }
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(5, user_name);
        }
 
        String head = entity.getHead();
        if (head != null) {
            stmt.bindString(6, head);
        }
 
        String post_content = entity.getPost_content();
        if (post_content != null) {
            stmt.bindString(7, post_content);
        }
 
        String comment_content = entity.getComment_content();
        if (comment_content != null) {
            stmt.bindString(8, comment_content);
        }
 
        String comment_id = entity.getComment_id();
        if (comment_id != null) {
            stmt.bindString(9, comment_id);
        }
 
        String reply_content = entity.getReply_content();
        if (reply_content != null) {
            stmt.bindString(10, reply_content);
        }
 
        String post_author_name = entity.getPost_author_name();
        if (post_author_name != null) {
            stmt.bindString(11, post_author_name);
        }
 
        String post_author_id = entity.getPost_author_id();
        if (post_author_id != null) {
            stmt.bindString(12, post_author_id);
        }
 
        java.util.Date create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindLong(13, create_time.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ReplyToMe readEntity(Cursor cursor, int offset) {
        ReplyToMe entity = new ReplyToMe( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // yourid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // postid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // user_name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // head
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // post_content
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // comment_content
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // comment_id
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // reply_content
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // post_author_name
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // post_author_id
            cursor.isNull(offset + 12) ? null : new java.util.Date(cursor.getLong(offset + 12)) // create_time
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ReplyToMe entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setYourid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPostid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUser_name(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHead(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPost_content(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setComment_content(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setComment_id(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setReply_content(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPost_author_name(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPost_author_id(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCreate_time(cursor.isNull(offset + 12) ? null : new java.util.Date(cursor.getLong(offset + 12)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ReplyToMe entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ReplyToMe entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
