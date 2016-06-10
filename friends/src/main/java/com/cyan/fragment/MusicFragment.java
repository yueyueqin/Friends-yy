package com.cyan.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyan.community.R;
import com.cyan.library.InteractivePlayerView;
import com.cyan.library.OnActionClickedListener;
import com.cyan.music.MusicService;
import com.cyan.music.Myadapter;
import com.cyan.music.WebMusicActivity;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:24
 * 类说明：
 */
public class MusicFragment extends PreferenceFragment
{
    private int count, current = 0, modelplay = 0;
    private String[] name, path;
    private ImageView btn_play;
    private ImageView btn_next, btn_last, serchbutton;
    private ListView listView;
    private static TextView music;
    private MusicService mService;
    private MyServiceConnection conn;
    private AutoCompleteTextView edit_query;
    private MediaPlayer mediaPlayer;
    private InteractivePlayerView ipv;
    private ImageView control;

    public static MusicFragment newInstance()
    {
        MusicFragment musicFragment = new MusicFragment();
        return musicFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //        return super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_music, null);
        initView(fragmentView);
        startthisActivity();
        return fragmentView;

    }

    private void initView(View fragmentRootView)
    {
        btn_play = (ImageView) fragmentRootView.findViewById(R.id.control);
        btn_next = (ImageView) fragmentRootView.findViewById(R.id.btn_next);
        btn_last = (ImageView) fragmentRootView.findViewById(R.id.btn_last);
        listView = (ListView) fragmentRootView.findViewById(R.id.listview);
        music = (TextView) fragmentRootView.findViewById(R.id.music);
        edit_query = (AutoCompleteTextView) fragmentRootView.findViewById(R.id.edit_query);
        serchbutton = (ImageView) fragmentRootView.findViewById(R.id.serchbutton);
        IntentFilter intentFilter = new IntentFilter(
                WebMusicActivity.WebMusicaction);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        ipv = (InteractivePlayerView) fragmentRootView.findViewById(R.id.ipv);
        ipv.setMax(240);
        ipv.setProgress(11);
        control = (ImageView) fragmentRootView.findViewById(R.id.control);


        serchbutton.setOnClickListener(new click());
        btn_play.setOnClickListener(new click());
        btn_next.setOnClickListener(new click());
        btn_last.setOnClickListener(new click());
        ipv.setOnActionClickedListener(new MyActionClick());
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            music.setText(intent.getExtras().getString("music"));
        }
    };

    // 开始界面
    public void startthisActivity() {

        // 播放本地歌曲 如果本地没有音乐 点击网络搜索
        Cursor();
        if (name == null || name.toString().equals("")
                || path.toString().equals("")) {
            Toast.makeText(getActivity(), "没有发现本地歌曲,请选择网络歌曲", Toast.LENGTH_SHORT)
                    .show();
            click(serchbutton);
        } else {
            // 自动完成文本搜索
            final String[] serchname = new String[1];
            ArrayAdapter autotextview = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_expandable_list_item_1, name);
            edit_query.setAdapter(autotextview);
            // 自动完成文本弹出框点击事件
            edit_query
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            for (int i = 0; i < name.length; i++) {
                                TextView sousuo = (TextView) view;
                                serchname[0] = sousuo.getText().toString();
                                if (sousuo.getText().equals(name[i])) {
                                    current = i;
                                    toplay();
                                }
                            }
                        }
                    });

            Intent intent = new Intent(getActivity(), MusicService.class);
            conn = new MyServiceConnection();
            getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
            gridview();
        }
    }

    // 本地文件查询并且加载自动搜索文本框
    public void Cursor() {
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        // 返回所有在外部存储卡上的音乐文件的信息
        count = cursor.getCount();
        name = new String[count];
        path = new String[count];
        int[] duration = new int[count];
        int j = 0;
        while (cursor.moveToNext()) {
            // 获取歌曲名称 歌曲大小 歌曲路径
            duration[j] = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            name[j] = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            path[j] = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            j++;

        }
        cursor.close();
    }

    // 服务连接 循环播放模式
    class MyServiceConnection implements ServiceConnection {
        // 连接成功执行这个方法
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.musicBinder binder = (MusicService.musicBinder) service;
            mService = binder.getService();// 获取播放音乐的服务
            mediaPlayer = mService.player;
            toplay();
            if (mediaPlayer != null) {
                ipv.setMax(mediaPlayer.getDuration()/1000);
                ipv.setProgress(0);
            }
            // 循环播放模式
            mediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (modelplay == 0) {
                                toplay();
                            } else {
                                click(btn_next);
                            }
                        }
                    });
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    // 列表显示及点击事件
    public void gridview() {
        Myadapter  baseadapter = new Myadapter (getActivity(), name);
        listView.setAdapter(baseadapter);
        // 列表单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                current = position;
                toplay();
            }
        });
    }

    // 销毁操作
    public void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            getActivity().unbindService(conn);
        }
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    // 播放控制
    private class click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.control:
                    // 暂停 开始
                    click(control);
                    break;
                case R.id.btn_next:
                    click(btn_next);
                    break;
                case R.id.btn_last:
                    click(btn_last);
                    break;
                // 跳转到网络音乐搜索界面

                case R.id.serchbutton:
                    // 网络未连接状态下不可用网络搜索
                    boolean flag = false;
                    // 得到网络连接信息
                    ConnectivityManager manager = (ConnectivityManager) getActivity()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    // 去进行判断网络是否连接
                    if (manager.getActiveNetworkInfo() != null) {
                        flag = manager.getActiveNetworkInfo().isAvailable();
                    }
                    if (flag) {
                        Intent intent = new Intent(getActivity(), WebMusicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("serch", edit_query.getText().toString());
                        bundle.putStringArray("name", name);
                        bundle.putStringArray("path", path);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {

                        Toast.makeText(getActivity(), "网络不可用", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }
        }
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.control:
                // 暂停 开始
                if (mediaPlayer.isPlaying()) {
                    mService.playing(true);
                    ipv.stop();
                    control.setBackgroundResource(R.drawable.play);
                } else {
                    mService.playing(false);
                    ipv.start();
                    control.setBackgroundResource(R.drawable.pause);
                }
                break;
            case R.id.btn_next:
                if (current == count - 1) {
                    current = 0;
                } else {
                    current++;
                }
                toplay();
                break;
            case R.id.btn_last:
                if (current == 0) {
                    current = count - 1;
                } else {
                    current--;
                }
                toplay();
                break;
            // 跳转到网络音乐搜索界面

            case R.id.serchbutton:
                // 网络未连接状态下不可用网络搜索
                boolean flag = false;
                // 得到网络连接信息
                ConnectivityManager manager = (ConnectivityManager) getActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                // 去进行判断网络是否连接
                if (manager.getActiveNetworkInfo() != null) {
                    flag = manager.getActiveNetworkInfo().isAvailable();
                }
                if (flag) {
                    Intent intent = new Intent(getActivity(), WebMusicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("serch", edit_query.getText().toString());
                    bundle.putStringArray("name", name);
                    bundle.putStringArray("path", path);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "网络不可用", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    public void toplay() {
        if (count == 0) {
            click(serchbutton);
        } else {
            music.setText(name[current]);
            mService.play(path[current]);
            ipv.stop();

            ipv.setMax(mediaPlayer.getDuration()/1000);
            ipv.setProgress(0);
            ipv.start();
            control.setBackgroundResource(R.drawable.pause);
        }
    }

    private boolean case2=true,case3=true;
    private class MyActionClick implements OnActionClickedListener {
        @Override
        public void onActionClicked(int id) {
            switch (id) {
                case 1:
                    //Called when 1. action is clicked.
                    modelplay=1;
                    if(case2){
                        Toast.makeText(getActivity(),"列表循环",Toast.LENGTH_SHORT).show();
                        case2=true;
                        modelplay=1;
                    }
                    else{
                        Toast.makeText(getActivity(),"单曲循环",Toast.LENGTH_SHORT).show();
                        modelplay=0;
                        case2=false;
                    }
                    break;
                case 2:
                    //Called when 2. action is clicked.
                    if(case2){
                        Toast.makeText(getActivity(),"添加喜爱成功",Toast.LENGTH_SHORT).show();
                        case2=false;
                    }
                    else{
                        Toast.makeText(getActivity(),"取消添加喜爱",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 3:
                    //Called when 3. action is clicked.


                if(case2){
                    Toast.makeText(getActivity(),"单曲循环",Toast.LENGTH_SHORT).show();
                    case2=true;
                    modelplay=0;
                }
                else{
                    Toast.makeText(getActivity(),"列表循环",Toast.LENGTH_SHORT).show();
                    modelplay=1;
                    case2=false;
                }
                    break;
                default:
                    break;
            }
        }
    }
    //保证这个Activity不被销毁
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().moveTaskToBack(true);
            return true;
        }
        return super.getActivity().onKeyDown(keyCode, event);
    }


}
