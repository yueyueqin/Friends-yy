package com.cyan.music;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by kys-34 on 2016/4/1.
 */
public class Toast extends Activity {
    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final android.widget.Toast toast = android.widget.Toast.makeText(activity, word, android.widget.Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }
}
