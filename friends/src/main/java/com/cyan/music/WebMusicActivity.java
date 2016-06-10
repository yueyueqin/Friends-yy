package com.cyan.music;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyan.community.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kys-34 on 2016/4/6.
 */
public class WebMusicActivity extends Activity {
    private AsyncHttpResponseHandler resHandler;
    private List<Map<String, String>> list;
    private AutoCompleteTextView editText;
    private MusicService musicService;
    private ServiceConnection connection;
    private String songname, singername;
    private String[] nativename, nativepath, serchresultname, serchresultpath;
    //    private ListView localtext;
    public static final String WebMusicaction = "jason.broadcast.action";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclayout);
        final Button myBtn = (Button) this.findViewById(R.id.button1);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
       setTitle("网络查询");

        //获取传进来的本地音乐数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nativename = bundle.getStringArray("name");
        nativepath = bundle.getStringArray("path");
        String serch = bundle.getString("serch");
        Log.e("bofang播放", "播放的曲目" + serch);
       // Log.e("本地的音乐信息", "sadas----" + nativename[0] + nativepath[0]);
        //设置传进来的搜索数据
        editText = (AutoCompleteTextView) findViewById(R.id.edittext);
        ArrayAdapter autoserch = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, nativename);
        editText.setAdapter(autoserch);
        click(myBtn);

        //自动完成文本点击事件
        if (nativename.length != 0) {
            final String[] serchname = new String[1];
            editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < nativename.length; i++) {
                        TextView sousuo = (TextView) view;
                        serchname[0] = sousuo.getText().toString();
                        Log.d("sousuosuosuosuo", "sadddffffffffffffffff" + sousuo.getText().toString());
                        if (sousuo.getText().equals(nativename[i])) {
                            musicService.play(nativepath[i]);
                            click(myBtn);
                        }
                    }
                }
            });
        }
        editText.setText(serch);

        //网络音乐列表
        final ListView listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(new listclick());
        //本地音乐列表
//        localtext = (ListView) findViewById(R.id.localmusiclist);
//        localtext.setOnItemClickListener(new localclick());

        resHandler = new AsyncHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                //做一些异常处理
                e.printStackTrace();
            }

            //数据解析
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    System.out.println("response is :" + new String(responseBody, "utf-8"));
                    String jsonData = new String(responseBody, "utf-8");
                    Log.e("jsonDAta", "-------------------" + jsonData);

                    RootBean rootBean = new Gson().fromJson(jsonData, RootBean.class);
                    Log.i("rootBean", "-----------" + rootBean.toString());
                    RootBean.ResBody resBody = rootBean.getShowapi_res_body();
                    RootBean.Pagebean pagebean = resBody.getPagebean();
                    List<RootBean.Content> contentlist = pagebean.getContentlist();
                    list = new ArrayList<Map<String, String>>();
                    Map<String, String>[] map = new Map[contentlist.size()];
                    int musiccount = 0;
                    //删除数据中存在空的歌曲名、歌手并且把歌曲名等信息取出
                    for (int i = 0; i < contentlist.size(); i++) {
                        if (contentlist.get(i).getSongname() == null || contentlist.get(i).getDownUrl() == null
                                || contentlist.get(i).getM4a() == null || contentlist.get(i).getSingername() == null) {
                            musiccount += 1;
                            Log.i("musiccount", "the music count is:" + musiccount);
                        } else {
                            int trueposition = i - musiccount;
                            map[trueposition] = new HashMap<String, String>();
                            Log.e("the truemusic count", "is:" + trueposition);
                            Log.i("the contentlist ", "is:" + contentlist.get(i));
                            Log.i("the NAME ", "is:" + contentlist.get(i).getSongname());
                            map[trueposition].put("downUrl", contentlist.get(i).getDownUrl());
                            map[trueposition].put("m4a", contentlist.get(i).getM4a());
                            map[trueposition].put("singername", contentlist.get(i).getSingername());
                            map[trueposition].put("songname", contentlist.get(i).getSongname());
                            list.add(map[trueposition]);
                        }
                    }
                    //填充网络音乐列表
                    Log.e("-----------------------", "-----------------");
                    Adapter myadapter = new Adapter(WebMusicActivity.this, list);
                    listview.setAdapter(myadapter);

                    if (list.size() == 0) {
                        Toast.makeText(WebMusicActivity.this, "未搜索到内容", Toast.LENGTH_SHORT).show();
                    }
                    //在此对返回内容做处理
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(WebMusicActivity.this, MusicService.class);
                connection = new MyServiceConnection();
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        };
        click(myBtn);

    }

    //连接Service并且得到mediaplayer对象
    private class MyServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("WebMusicActivity", "连接成功");
            MusicService.musicBinder binder = (MusicService.musicBinder) service;
            musicService = binder.getService();
            //获取播放音乐的服务
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    //本地音乐列表点击事件
    private class localclick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(WebMusicaction);
            intent.putExtra("music", serchresultname[position]);
            sendBroadcast(intent);
            musicService.play(serchresultpath[position]);
        }
    }

    //网络音乐列表点击事件
    private class listclick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            com.cyan.music.Toast.showToast(WebMusicActivity.this, "加载歌曲：   " + list.get(position).get("songname") + "中，请稍后", 2000);
            musicService.play(list.get(position).get("downUrl"));
            singername = list.get(position).get("singername");
            songname = list.get(position).get("songname");
            final String downUrl = list.get(position).get("downUrl");
            //向第一个列表里填入网络音乐信息
            Intent intent = new Intent(WebMusicaction);
            intent.putExtra("music", songname + "-" + singername);
            sendBroadcast(intent);
            //下载歌曲
            final HttpDownloader httpDownloader=new HttpDownloader();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpDownloader.downfile(downUrl,"cheyou",songname+".mp3");
                }
            }).start();

//            点击音乐后返回上一个activity
//            finish();
        }
    }

    //销毁操作
    public void onDestroy() {
        super.onDestroy();
        if (connection != null) {
            unbindService(connection);
        }else {
           this. finish();
        }
    }

    //点击查询音乐
    public void click(View v) {

        //查询网络音乐信息
        if (editText.getText().toString().equals("")) {
            com.cyan.music.Toast.showToast(WebMusicActivity.this, "请输入要查询的歌曲", 1000);
        } else {
            new com.cyan.music.ShowApiRequest("http://route.showapi.com/213-1", "17287", "818f6192a3854eb7814fef07cdcf56b9")
                    .setResponseHandler(resHandler)
                    .addTextPara("keyword", editText.getText().toString())
                    .addTextPara("page", "1")
                    .post();
        }
    }


}