package com.cyan.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.cyan.adapter.RecordAdapter;
import com.cyan.adapter.RecyclerArrayAdapter;
import com.cyan.annotation.ActivityFragmentInject;
import com.cyan.app.MyApplication;
import com.cyan.community.R;
import com.cyan.widget.recyclerview.EasyRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.daoexample.Record;
import de.greenrobot.daoexample.RecordDao;

/**
 * Created by Administrator on 2016/2/15.
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_userinfo,
        toolbarTitle = R.string.record
)
public class RecordActivity extends BaseActivity
{
    @InjectView(R.id.list)
    EasyRecyclerView recordList;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private RecordAdapter recordAdapter;
    private RecordDao recordDao;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.inject(this);
        getRecords();
    }


    private void getRecords()
    {
        recordList.setRefreshEnabled(false);
        recordList.showProgress();
        recordDao = MyApplication.getInstance().getDaoSession().getRecordDao();
        String textColumn = RecordDao.Properties.Id.columnName;
        String orderBy = textColumn + " DESC";
        String where = RecordDao.Properties.User_id.columnName + " = '" + MyApplication.getInstance().getCurrentUser().getObjectId() + "'";
        cursor = MyApplication.getInstance().getDb().query(recordDao.getTablename(), recordDao.getAllColumns(), where, null, null, null, orderBy);
        if (cursor.getCount() == 0) {
            recordList.showEmpty();
            cursor.close();
            return;
        }
        recordList.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter(this);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Record record = new Record();
            recordDao.readEntity(cursor, record, 0);
            recordAdapter.addAll(record);
        }
        recordAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                Log.i("record", "click");
                Intent intent = new Intent(RecordActivity.this, ContentActivity.class);
                intent.putExtra("type", recordAdapter.getData().get(position).getType());
                intent.putExtra("object_id", recordAdapter.getData().get(position).getObject_id());
                intent.putExtra("parent_id", recordAdapter.getData().get(position).getParent_id());
                startActivityForResult(intent, 0);

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemHelper<Record>(recordDao, recordAdapter));
        itemTouchHelper.attachToRecyclerView(recordList.getRecyclerView());
        recordList.showRecycler();
        recordList.setAdapter(recordAdapter);
        cursor.close();

    }

}
