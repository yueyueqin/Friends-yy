package com.cyan.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by liuyongkuan on 2016/3/24 .
 */
public class MusicService extends Service  {
    boolean isplaying = false;
    public MediaPlayer player = new MediaPlayer();
    private musicBinder musicBinder = new musicBinder();

    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    // 返回对象
    public class musicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playing(boolean is) {
        if (is) {
            player.pause();
        } else {
            player.start();
        }
    }

    // 播放音乐
    public void play(final String path) {
        if (isplaying) {
            player.stop();
        }

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    player.reset();
                    player.setDataSource(path);
                    // 缓存成功后执行
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            player.start();
                            isplaying = true;
                        }
                    });
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.run();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (extra) {
                    case 1004:
                        Toast.makeText(MusicService.this, "播放源错误",
                                Toast.LENGTH_SHORT).show();
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
        super.onDestroy();
    }
}
