package com.cyan.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cyan.adapter.PostAdapter;
import com.cyan.adapter.QuickSearchAdapter;
import com.cyan.adapter.RecyclerArrayAdapter;
import com.cyan.annotation.ActivityFragmentInject;
import com.cyan.app.MyApplication;
import com.cyan.bean.MyBmobInstallation;
import com.cyan.bean.Post;
import com.cyan.bean.User;
import com.cyan.common.Constants;
import com.cyan.community.R;
import com.cyan.listener.InputWindowListener;
import com.cyan.listener.OnItemClickListener;

import com.cyan.manager.IatSettings;
import com.cyan.manager.JsonParser;
import com.cyan.manager.MyLayoutManager;
import com.cyan.module.post.presenter.PostPresenter;
import com.cyan.module.post.presenter.PostPresenterImpl;
import com.cyan.module.post.view.LoadPostView;
import com.cyan.util.InitiateSearch;
import com.cyan.util.PraiseUtils;
import com.cyan.util.SPUtils;
import com.cyan.widget.recyclerview.EasyRecyclerView;
import com.cyan.widget.refreshlayout.RefreshLayout;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import de.greenrobot.daoexample.QuickSearch;
import de.greenrobot.daoexample.QuickSearchDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 发表信息界面ACtivity
 */

@ActivityFragmentInject(
        contentViewId = R.layout.activity_main,
        toolbarTitle = R.string.app_name
)
public class MainActivity extends RefreshActivity implements RefreshLayout.OnRefreshListener, LoadPostView
{

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.mToolbarContainer)
    AppBarLayout mToolbarContainer;
    @InjectView(R.id.list)
    EasyRecyclerView contentList;
    @InjectView(R.id.submit)
    FloatingActionButton submit;
    @InjectView(R.id.id_nv_menu)
    NavigationView idNvMenu;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.view_search)
    RelativeLayout viewSearch;
    @InjectView(R.id.image_search_back)
    ImageView imageSearchBack;
    @InjectView(R.id.edit_text_search)
    EditText editTextSearch;
    @InjectView(R.id.clearSearch)
    ImageView clearSearch;
    @InjectView(R.id.linearLayout_search)
    LinearLayout linearLayoutSearch;
    @InjectView(R.id.line_divider)
    View lineDivider;
    @InjectView(R.id.listView)
    RecyclerView listView;
    @InjectView(R.id.card_search)
    CardView cardSearch;
    private TextView comment;

    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<QuickSearch> quickSearches;

    User myUser;
    ImageView head, background;
    TextView username;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menuItem, messages, records, collection,record,some;
    private long firstclick;
    private PostPresenter<Post> postPresenter;
    private BmobQuery<Post> query;
    private InitiateSearch initiateSearch;
    private QuickSearchAdapter quickSearchAdapter;
    private QuickSearchDao quickSearchDao;


    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private int ret = 0; // 函数调用返回值


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        BmobUpdateAgent.update(this);

        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener()
        {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo)
            {
                // TODO Auto-generated method stub
                Log.i("updatestatus", updateStatus + "");
            }
        });
        initRefreshLayout();
        // setFullTouch();
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        initHead();
        mLayoutManager = new LinearLayoutManager(this);
        contentList.setLayoutManager(mLayoutManager);
        postPresenter = new PostPresenterImpl(this, this, subscription);
        query = new BmobQuery<>();
        postPresenter.loadPost(query);
        contentList.getErrorView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                query = new BmobQuery<>();
                postPresenter.loadPost(query);
            }
        });

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        InitiateSearch();
        HandleSearch();
    }

    @Override
    public void addPosts(final List<Post> list)
    {
        PraiseUtils.flush(MainActivity.this, is_praised, is_collected, list);
        if (list.size() > 0)
            if (posts.size() == 0) {
                posts = (ArrayList<Post>) list;
                postAdapter = new PostAdapter(posts, is_praised, is_collected, MainActivity.this);
                postAdapter.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onClick(View view, Object item)
                    {
                        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                        intent.putExtra("post", posts.get((Integer) item));
                        intent.putExtra("isPraised", is_praised.get(posts.get((Integer) item).getId()));
                        intent.putExtra("isCollected", is_collected.get(posts.get((Integer) item).getId()));
                        startActivityForResult(intent, 0);
                    }
                });
                //cnsnb

            } else {
                if (posts.get(0).getId() < list.get(0).getId()) {
                    posts.addAll(0, list);
                } else {
                    posts.addAll(list);
                }

            }
    }

    @Override
    public void notifyDataSetChanged(boolean b)
    {
        if (b) contentList.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty()
    {
        contentList.showEmpty();
    }

    @Override
    public void showError()
    {
        contentList.showError();
        stopRefreshIconAnimation(menuItem);
    }

    @Override
    public void showProgress(Boolean b)
    {
        if (b) {
            if (menuItem != null)
                startRefreshIconAnimation(menuItem);
            contentList.showProgress();
        }
    }

    @Override
    public void showRecycler()
    {
        contentList.showRecycler();

    }

    @Override
    public void stopLoadMore()
    {
        contentList.setFooterRefreshing(false);
    }

    @Override
    public void stopRefresh()
    {
        contentList.setHeaderRefreshing(false);
        stopRefreshIconAnimation(menuItem);
    }

    @Override
    public void onFooterRefresh()
    {
        query = new BmobQuery<>();
        if (posts.size() > 0)
            query.addWhereLessThan("id", posts.get(posts.size() - 1).getId());
        postPresenter.loadPost(query);

    }

    @Override
    public void onHeaderRefresh()
    {
        query = new BmobQuery<>();
        if (posts.size() > 0)
            query.addWhereGreaterThan("id", posts.get(0).getId());
        postPresenter.loadPost(query);


    }

    public void initHead()
    {

        if (messages == null) {
            messages = idNvMenu.getMenu().getItem(1);
            records = idNvMenu.getMenu().getItem(2);
            collection = idNvMenu.getMenu().getItem(0);
record=idNvMenu.getMenu().getItem(6);
            some=idNvMenu.getMenu().getItem(7);
            idNvMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem)
                {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_collection:
                            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_messages:
                            intent = new Intent(MainActivity.this, MessageActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_records:
                            intent = new Intent(MainActivity.this, RecordActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_settings:
                            intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                       /* case R.id.nav_about:
                            intent = new Intent(MainActivity.this, AboutActivity.class);
                            startActivity(intent);
                            break;*/
                        case R.id.nav_violation:
                            intent = new Intent(MainActivity.this, ViolationActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_gps:
                            intent = new Intent(MainActivity.this, GpsActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_gasorder:
                            intent = new Intent(MainActivity.this, GasorderActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_scanpay:
                            intent = new Intent(MainActivity.this, ScanpayActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case R.id.nav_music:
                            intent = new Intent(MainActivity.this, MusicActivity.class);
                            startActivityForResult(intent, 0);
                            break;

                    }
                    //drawerLayout.closeDrawers();
                    return false;
                }
            });
            View headerView = idNvMenu.getHeaderView(0);
            head = (ImageView) headerView.findViewById(R.id.head);
            head.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (MyApplication.getInstance().getCurrentUser() != null) {
                        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                        startActivityForResult(intent, 0);
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 0);
                    }
                }
            });
            background = (ImageView) headerView.findViewById(R.id.image);
            username = (TextView) headerView.findViewById(R.id.id_username);
        }
        myUser = MyApplication.getInstance().getCurrentUser();
        if (myUser != null) {
            messages.setEnabled(true);
            records.setEnabled(true);
            collection.setEnabled(true);
            record.setEnabled(true);
            some.setEnabled(true);
            username.setText(myUser.getUsername());
            if (myUser.getHead() != null) {
                Glide.with(this).load(myUser.getHead().getFileUrl(getApplicationContext())).into(head);
            } else {
                head.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            }
            if (myUser.getBackground() != null)
                Glide.with(this).load(myUser.getBackground().getFileUrl(this)).into(background);
        } else {
            messages.setEnabled(false);
            records.setEnabled(false);
            collection.setEnabled(false);
            some.setEnabled(false);
            record.setEnabled(false);
            username.setText("请登陆");
            head.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            background.setImageDrawable(getResources().getDrawable(R.drawable.background));
        }
    }

    static class PagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title)
        {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode) {
            case RESULT_OK:
                initHead();
                if (posts.size() != 0) {
                    sub = Observable.create(new Observable.OnSubscribe<PostAdapter>()
                    {
                        @Override
                        public void call(Subscriber<? super PostAdapter> subscriber)
                        {
                            PraiseUtils.flush(MainActivity.this, is_praised, is_collected, posts);
                            subscriber.onNext(postAdapter);
                        }
                    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PostAdapter>()
                    {
                        @Override
                        public void call(PostAdapter postAdapter)
                        {
                            postAdapter.notifyDataSetChanged();
                        }
                    });
                }
                Log.i("userId", myUser.getObjectId());
                refreshInstalllation(myUser.getObjectId());
                break;
            case Constants.LOGOUT:
                MyApplication.getInstance().clearCurrentUser();
                initHead();
                is_collected.clear();
                is_praised.clear();
                postAdapter.notifyDataSetChanged();
                refreshInstalllation("0");
                break;
            case Constants.SAVE_OK:
                initHead();
                for (Post post : posts) {
                    if (post.getAuthor().getObjectId().equals(MyApplication.getInstance().getCurrentUser().getObjectId()))
                        post.setAuthor(MyApplication.getInstance().getCurrentUser());
                }
                postAdapter.notifyDataSetChanged();
                break;
            case Constants.SUBMIT_OK:
                query = new BmobQuery<>();
                if (posts.size() > 0)
                    query.addWhereGreaterThan("id", posts.get(0).getId());
                postPresenter.loadPost(query);
                break;
            case Constants.RESEARCH:
                quickSearchAdapter.clear();
                String textColumn = QuickSearchDao.Properties.Id.columnName;
                String orderBy = textColumn + " DESC";
                Cursor cursor = MyApplication.getInstance().getDb().query(quickSearchDao.getTablename(), quickSearchDao.getAllColumns(), null, null, null, null, orderBy);
                if (cursor != null) {

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        QuickSearch quickSearch = new QuickSearch();
                        quickSearchDao.readEntity(cursor, quickSearch, 0);
                        quickSearchAdapter.addAll(quickSearch);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void InitiateSearch()
    {
        contentList.setListener(new InputWindowListener()
        {
            @Override
            public void show()
            {

            }

            @Override
            public void hide()
            {
                Log.i("input", "hide");
                if (cardSearch.getVisibility() == View.VISIBLE)
                    initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);
            }
        });
        quickSearchDao = MyApplication.getInstance().getDaoSession().getQuickSearchDao();
        String textColumn = QuickSearchDao.Properties.Id.columnName;
        String orderBy = textColumn + " DESC";
        Cursor cursor = MyApplication.getInstance().getDb().query(quickSearchDao.getTablename(), quickSearchDao.getAllColumns(), null, null, null, null, orderBy);
        if (cursor != null) {
            quickSearchAdapter = new QuickSearchAdapter(this);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                QuickSearch quickSearch = new QuickSearch();
                quickSearchDao.readEntity(cursor, quickSearch, 0);
                quickSearchAdapter.addAll(quickSearch);
            }
            quickSearchAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(int position)
                {
                    if (position != 0) {
                        QuickSearch quickSearch = new QuickSearch();
                        quickSearch.setAdd_time(new Date(System.currentTimeMillis()));
                        quickSearch.setContent(quickSearchAdapter.getData().get(position).getContent());
                        quickSearchDao.insert(quickSearch);
                        quickSearchDao.delete(quickSearchAdapter.getData().get(position));
                        quickSearchAdapter.remove(position);
                        quickSearchAdapter.add(0, quickSearch);
                    }
                    initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("key", quickSearchAdapter.getData().get(position).getContent());
                    startActivityForResult(intent, 0);
                }
            });
            listView.setLayoutManager(new MyLayoutManager(this));
            listView.setAdapter(quickSearchAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemHelper<QuickSearch>(quickSearchDao, quickSearchAdapter));
            itemTouchHelper.attachToRecyclerView(listView);
        }
        editTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (editTextSearch.getText().toString().length() == 0) {
                    clearSearch.setImageResource(R.mipmap.ic_keyboard_voice);
                } else {
                    clearSearch.setImageResource(R.mipmap.ic_close);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        //语音部分监听
        clearSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editTextSearch.getText().toString().length() == 0) {

                    Log.e("----------", "============");
                    // TODO Auto-generated method stub
                    editTextSearch.setText(null);// 清空显示内容
                    mIatResults.clear();
                    // 设置参数
                    setParam();
                    boolean isShowDialog = mSharedPreferences.getBoolean(
                            getString(R.string.pref_key_iat_show), true);
                    if (isShowDialog) {
                        // 显示听写对话框
                        mIatDialog.setListener(mRecognizerDialogListener);
                        mIatDialog.show();
                        showTip(getString(R.string.text_begin));
                    } else {
                        // 不显示听写对话框
                        ret = mIat.startListening(mRecognizerListener);
                        if (ret != ErrorCode.SUCCESS) {
                            showTip("听写失败,错误码：" + ret);
                        } else {
                            showTip(getString(R.string.text_begin));
                        }
                    }


                } else {
                    editTextSearch.setText("");
                    listView.setVisibility(View.VISIBLE);
                    ((InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                }
            }
        });
    }

    private void HandleSearch()
    {
        imageSearchBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("search", "back");
                initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);
            }
        });
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (editTextSearch.getText().toString().trim().length() > 0) {
                        for (int i = 0; i < quickSearchAdapter.getData().size(); i++) {
                            if (quickSearchAdapter.getData().get(i).getContent().equals(editTextSearch.getText().toString())) {
                                if (i == 0) {
                                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                    intent.putExtra("key", editTextSearch.getText().toString());
                                    startActivityForResult(intent, 0);
                                    initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);
                                    return true;
                                }
                                quickSearchDao.delete(quickSearchAdapter.getData().get(i));
                                quickSearchAdapter.remove(i);
                                break;
                            }
                        }
                        QuickSearch quickSearch = new QuickSearch();
                        quickSearch.setAdd_time(new Date(System.currentTimeMillis()));
                        quickSearch.setContent(editTextSearch.getText().toString());
                        quickSearchDao.insert(quickSearch);
                        quickSearchAdapter.add(0, quickSearch);
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra("key", editTextSearch.getText().toString());
                        startActivityForResult(intent, 0);
                        initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);

                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener()
    {

        @Override
        public void onInit(int code)
        {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener()
    {

        @Override
        public void onBeginOfSpeech()
        {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error)
        {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech()
        {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast)
        {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data)
        {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj)
        {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }
        }
    };

    private void printResult(RecognizerResult results)
    {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        editTextSearch.setText(resultBuffer.toString());
        editTextSearch.setSelection(editTextSearch.length());
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener()
    {
        public void onResult(RecognizerResult results, boolean isLast)
        {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error)
        {
            showTip(error.getPlainDescription(true));
        }

    };

    private void showTip(final String str)
    {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam()
    {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,
                mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,
                mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,
                mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    @Override
    protected void onResume()
    {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(this);
        super.onPause();
    }

    @OnClick(R.id.submit)
    public void submit()
    {
        User user = User.getCurrentUser(MainActivity.this, User.class);
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivityForResult(intent, 0);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItem = menu.findItem(R.id.action_refresh);
        menuItem.getActionView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.performIdentifierAction(R.id.action_refresh, 0);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startRefreshIconAnimation(item);
                contentList.setHeaderRefreshing(true);
                query = new BmobQuery<>();
                if (posts.size() > 0)
                    //loadMoreQuery();
                    query.addWhereGreaterThan("id", posts.get(0).getId());
                postPresenter.loadPost(query);
                break;
            case R.id.action_search:
                initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (cardSearch.getVisibility() == View.GONE) {
                if ((System.currentTimeMillis() - firstclick) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstclick = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
            }
        }
        if (cardSearch.getVisibility() == View.VISIBLE) {
            Log.i("cardView", "hide");
            initiateSearch.handleToolBar(MainActivity.this, cardSearch, viewSearch, listView, editTextSearch, lineDivider);
            return true;
        }
        return true;
    }

    public void initRefreshLayout()
    {
        contentList.setRefreshListener(this);
        contentList.setFooterRefrehingColorResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        contentList.setHeaderRefreshingColorResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void startRefreshIconAnimation(MenuItem menuItem)
    {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh_icon_rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        menuItem.getActionView().startAnimation(rotation);
        menuItem.getActionView().setClickable(false);
    }

    private void stopRefreshIconAnimation(MenuItem menuItem)
    {
        if (menuItem != null) {
            menuItem.getActionView().clearAnimation();
            menuItem.getActionView().setClickable(true);
        }
    }

    private void refreshInstalllation(final String userId)
    {
        BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(MyApplication.getInstance()));
        Log.i("objectId", BmobInstallation.getCurrentInstallation(MyApplication.getInstance()).getObjectId() + "");
        query.findObjects(this, new FindListener<MyBmobInstallation>()
        {

            @Override
            public void onSuccess(List<MyBmobInstallation> object)
            {
                // TODO Auto-generated method stub
                if (object.size() > 0) {
                    final MyBmobInstallation mbi = object.get(0);
                    Log.i("userId", userId);
                    mbi.setUid(userId);
                    Log.i("objectId", mbi.getObjectId());
                    SPUtils.put(MyApplication.getInstance(), "settings", "installation", mbi.getObjectId());
                    mbi.update(MainActivity.this, new UpdateListener()
                    {

                        @Override
                        public void onSuccess()
                        {
                            // TODO Auto-generated method stub
                            Log.i("objectId", mbi.getObjectId() + "");
                            Log.i("bmob", "设备信息更新成功");
                        }

                        @Override
                        public void onFailure(int code, String msg)
                        {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "设备信息更新失败:" + msg);
                        }
                    });
                } else {
                }
            }

            @Override
            public void onError(int code, String msg)
            {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void finish()
    {
        super.finish();
        BmobQuery.clearAllCachedResults(this);

    }


}
