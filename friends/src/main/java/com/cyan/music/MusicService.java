package com.cyan.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by liuyongkuan on 2016/3/24 .
 */
public class MusicService extends Service  {
    public static boolean alive=true;
    boolean isplaying = false;
    public MediaPlayer player = new MediaPlayer();
    private musicBinder musicBinder = new musicBinder();


    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    //返回对象
    public class musicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    //播放音乐
    public void play(final String path)  {
        if (isplaying) {
            player.stop();
            Log.e("停止player", "停止----------------------");
        }
        Thread t=new Thread(new Runnable() {
            public void run() {
                try {
                    Log.e("service", "开始播放" + path);
                    player.reset();
                    player.setDataSource(path);
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
                    player.prepare();
                    //缓存成功后执行
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            isplaying = true;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.run();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("播放错误", "错误信息" + what + "   --" + extra);
                switch (extra){
                    case 1004:
                        Toast.makeText(MusicService.this,"播放源错误，开始播放本地歌曲",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void onDestroy() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }
        Log.e("销毁", "销毁------------");
        super.onDestroy();
    }


}
