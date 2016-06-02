package com.cyan.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cyan.community.R;
import com.cyan.music.MusicService;
import com.cyan.music.Myadapter;
import com.cyan.music.WebMusicActivity;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:24
 * 类说明：
 */
public class MusicFragment extends PreferenceFragment implements View.OnClickListener
{
    private int count, current = 0, modelplay = 0;
    private String[] name, path;
    private Button btn_play, btn_next,btn_last;
    private ListView listView;
    private static TextView music;
    private MusicService mService;
    private MyServiceConnection conn;
    private SeekBar seekBar;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Spinner spinner;
    private AutoCompleteTextView edit_query;
    private Button serchbutton;
    private MediaPlayer mediaPlayer;

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
        btn_last=(Button)fragmentRootView.findViewById(R.id.btn_last);
        btn_play = (Button) fragmentRootView.findViewById(R.id.btn_play);
        btn_next = (Button) fragmentRootView.findViewById(R.id.btn_next);
        btn_last.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_play.setOnClickListener(this);

        listView = (ListView) fragmentRootView.findViewById(R.id.listview);
        music = (TextView) fragmentRootView.findViewById(R.id.music);
        spinner = (Spinner) fragmentRootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new itemselected());
        edit_query = (AutoCompleteTextView) fragmentRootView.findViewById(R.id.edit_query);
        Log.e("edit_query是否有值？？？", edit_query + "");
        serchbutton = (Button) fragmentRootView.findViewById(R.id.serchbutton);
        serchbutton.setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter(WebMusicActivity.WebMusicaction);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            music.setText(intent.getExtras().getString("music"));
        }
    };

    //开始界面
    public void startthisActivity()
    {
        //播放本地歌曲 如果本地没有音乐 点击网络搜索
        Cursor();
        if (name == null || name.toString().equals("") || path.toString().equals("")) {
            Toast.makeText(getActivity(), "没有发现本地歌曲,请选择网络歌曲", Toast.LENGTH_SHORT).show();
            onClick(serchbutton);
            getActivity().finish();
        } else {
            Log.e("tag", "播放网络文件");
            /*
        主要的几个类：
        PinyinHelper 调用方法的核心类
        HanyuPinyinOutputFormat 输出格式，
        设置HanyuPinyinCaseType（大小写），
        HanyuPinyinToneType（声调的方式），
        HanyuPinyinVCharType（V的输出方式）
         */

            //拼音转换
            HanyuPinyinOutputFormat formatbig = new HanyuPinyinOutputFormat();
            HanyuPinyinOutputFormat formatsmall = new HanyuPinyinOutputFormat();
            formatbig.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            formatsmall.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            String[] valBig = new String[name.length * 10];
            String[] valSmall = new String[name.length * 10];
            for (int k = 0; k < name.length; k++) {
                for (int i = 0; i < name[k].length(); i++) {
                    char c = name[k].charAt(i);
                    try {
                        valBig[i] = new String(Arrays.toString(PinyinHelper.toHanyuPinyinStringArray(c, formatbig)));
                        valSmall[i] = new String(Arrays.toString(PinyinHelper.toHanyuPinyinStringArray(c, formatsmall)));
                    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                        badHanyuPinyinOutputFormatCombination.printStackTrace();
                    }
                }
            }
            //        String[] completename=new String[name.length+valBig.length+valSmall.length];
            String[] completename = concat(concat(name, valBig), valSmall);
            Log.e("拼音拼音拼音拼音", Arrays.toString(completename));

            //自动完成文本搜索
            final String[] serchname = new String[1];
            ArrayAdapter autotextview = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, name);
            Log.e("autotextview是否有值", autotextview + "");
            edit_query.setAdapter(autotextview);
            //自动完成文本弹出框点击事件
            edit_query.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    for (int i = 0; i < name.length; i++) {
                        TextView sousuo = (TextView) view;
                        serchname[0] = sousuo.getText().toString();
                        Log.d("sousuosuosuosuo", "sadddffffffffffffffff" + sousuo.getText().toString());
                        if (sousuo.getText().equals(name[i])) {
                            current = i;
                            onClick(btn_play);

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

    //本地文件查询并且加载自动搜索文本框、实现歌曲名大小写转换
    public void Cursor()
    {
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //返回所有在外部存储卡上的音乐文件的信息
        count = cursor.getCount();
        name = new String[count];
        path = new String[count];
        int[] duration = new int[count];
        int j = 0;
        while (cursor.moveToNext()) {
            //获取歌曲名称 歌曲大小 歌曲路径
            duration[j] = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

            name[j] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            path[j] = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            j++;

        }
        Log.e("tag", "路径、" + Arrays.toString(path));
        cursor.close();
    }

    //数组连接
    private static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    //服务连接 循环播放模式
    class MyServiceConnection implements ServiceConnection
    {
        //连接成功执行这个方法
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.e("服务连接", "成功");

            MusicService.musicBinder binder = (MusicService.musicBinder) service;
            mService = binder.getService();//获取播放音乐的服务
            Log.e("mService---",""+mService);
            mediaPlayer = mService.player;
            onClick(btn_play);
            if (mediaPlayer != null) {
                seekbar();
            }
            //循环播放模式
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    if (modelplay == 0) {
                        onClick(btn_play);
                    } else {
                        onClick(btn_next);
                    }
                }
            });
        }

        public void onServiceDisconnected(ComponentName name)
        {
            Log.e("服务连接", "失败");
        }
    }

    //列表显示及点击事件
    public void gridview()
    {
        Myadapter baseadapter = new Myadapter(getActivity(), name);
        listView.setAdapter(baseadapter);
        //列表单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                current = position;
                onClick(btn_play);
            }
        });
    }
    //播放控制
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btn_play:
                //不播放不存在的歌曲
                if (path[current] == null) {
                    Log.e("没有歌曲信息", "没有歌曲信息");
                    onClick(btn_next);
                    break;
                }
                Log.e("path is:", path[current]);
                music.setText(name[current]);
                mService.play(path[current]);
                break;
            case R.id.btn_next:
                if (current == count - 1) {
                    current = 0;
                } else {
                    current++;
                }
                onClick(btn_play);
                break;
            case R.id.btn_last:
                if (current == 0) {
                    current = count - 1;
                } else {
                    current--;
                }
                onClick(btn_play);
                break;
            case R.id.serchbutton:
                Intent intent = new Intent(getActivity(), WebMusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("serch", edit_query.getText().toString());
                Log.e("开始", "广播" + edit_query.getText().toString());
                bundle.putStringArray("name", name);
                bundle.putStringArray("path", path);

                intent.putExtras(bundle);
                Log.e("开始", "广播");
                startActivity(intent);

                break;
        }
    }



    //进度条
    private class MySeekbar implements SeekBar.OnSeekBarChangeListener
    {
        public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser)
        {
            seekBar.setMax(mediaPlayer.getDuration());
        }

        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        public void onStopTrackingTouch(SeekBar seekBar)
        {
            //进度条拖动进度
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    //如果界面存在就加载seekBar
    public void seekbar()
    {
        seekBar = (SeekBar) getActivity().findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new MySeekbar());
        //定时器记录播放进度
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
        mTimer = new Timer();
        mTimerTask = new TimerTask()
        {
            public void run()
            {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };
        mTimerTask.run();
        mTimer.schedule(mTimerTask, 0, 1000);
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

    //销毁操作
    public void onDestroy()
    {
        super.onDestroy();
        if (conn != null) {
            getActivity().unbindService(conn);
        }
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    //Spinner
    private class itemselected implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id)
        {
            //            Toast.makeText(MainActivity.this, "位置" + position, Toast.LENGTH_SHORT).show();
            if (position == 0) {
                modelplay = 0;
            } else {
                modelplay = 1;
            }
        }

        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    }

    //跳转到网络音乐搜索界面
   /* public void webSerch(View v)
    {
        switch (v.getId()) {
            case R.id.serchbutton:

                break;
        }
    }*/


}  
