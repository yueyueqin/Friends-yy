package de.greenrobot.daoexample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DRAFT".
*/
public class DraftDao extends AbstractDao<Draft, Long> {

    public static final String TABLENAME = "DRAFT";

    /**
     * Properties of entity Draft.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property Create_time = new Property(2, java.util.Date.class, "create_time", false, "CREATE_TIME");
    };


    public DraftDao(DaoConfig config) {
        super(config);
    }
    
    public DraftDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DRAFT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"CONTENT\" TEXT," + // 1: content
                "\"CREATE_TIME\" INTEGER);"); // 2: create_time
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DRAFT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Draft entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        java.util.Date create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindLong(3, create_time.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Draft readEntity(Cursor cursor, int offset) {
        Draft entity = new Draft( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // content
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)) // create_time
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Draft entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreate_time(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Draft entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Draft entity) {
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
