package de.greenrobot.daoexample;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig commentToMeDaoConfig;
    private final DaoConfig replyToMeDaoConfig;
    private final DaoConfig recordDaoConfig;
    private final DaoConfig draftDaoConfig;
    private final DaoConfig quickSearchDaoConfig;
    private final CommentToMeDao commentToMeDao;
    private final ReplyToMeDao replyToMeDao;
   private final  RecordDao recordDao;
    private final DraftDao draftDao;
    private final QuickSearchDao quickSearchDao;
    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        commentToMeDaoConfig = daoConfigMap.get(CommentToMeDao.class).clone();
        commentToMeDaoConfig.initIdentityScope(type);

        replyToMeDaoConfig = daoConfigMap.get(ReplyToMeDao.class).clone();
        replyToMeDaoConfig.initIdentityScope(type);

        recordDaoConfig=daoConfigMap.get(RecordDao.class).clone();
        recordDaoConfig.initIdentityScope(type);

        draftDaoConfig=daoConfigMap.get(DraftDao.class).clone();
        draftDaoConfig.initIdentityScope(type);

        quickSearchDaoConfig=daoConfigMap.get(QuickSearchDao.class).clone();
        quickSearchDaoConfig.initIdentityScope(type);

        commentToMeDao = new CommentToMeDao(commentToMeDaoConfig, this);
        replyToMeDao = new ReplyToMeDao(replyToMeDaoConfig, this);
        recordDao=new RecordDao(recordDaoConfig,this);
        draftDao=new DraftDao(draftDaoConfig,this);
        quickSearchDao=new QuickSearchDao(quickSearchDaoConfig,this);
        registerDao(CommentToMe.class, commentToMeDao);
        registerDao(ReplyToMe.class, replyToMeDao);
        registerDao(Record.class,recordDao);
        registerDao(Draft.class,draftDao);
        registerDao(QuickSearch.class,quickSearchDao);
    }
    
    public void clear() {
        commentToMeDaoConfig.getIdentityScope().clear();
        replyToMeDaoConfig.getIdentityScope().clear();
        recordDaoConfig.getIdentityScope().clear();
        draftDaoConfig.getIdentityScope().clear();
        quickSearchDaoConfig.getIdentityScope().clear();
    }

    public CommentToMeDao getCommentToMeDao() {
        return commentToMeDao;
    }

    public ReplyToMeDao getReplyToMeDao() {
        return replyToMeDao;
    }

    public RecordDao getRecordDao() {
        return recordDao;
    }

    public DraftDao getDraftDao(){
        return draftDao;
    }

    public QuickSearchDao getQuickSearchDao() {
        return quickSearchDao;
    }
}