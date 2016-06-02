package com.cyan.music;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cyan.community.R;


/**
 * Created by liuyongkuan on 2016/3/3.
 */

public class Myadapter extends BaseAdapter {
    Context context;
    String[] musicname;

    public Myadapter(Context context, String[] musicname){
        this.context=context;
        this.musicname=musicname;

    }
    public int getCount()
    {
        return musicname.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView localtext1=null;
        TextView localtext2=null;

        if (view==null){
            LayoutInflater layoutInflater= LayoutInflater.from(context);
            view=layoutInflater.inflate(R.layout.baseadapter,null);
            localtext1=(TextView)view.findViewById(R.id.localtext1);
            localtext2=(TextView)view.findViewById(R.id.localtext2);

            ViewHolder viewHolder=new ViewHolder();
            viewHolder.localtext1=localtext1;
            viewHolder.localtext2=localtext2;
            view.setTag(viewHolder);
        }
        else{
            ViewHolder viewHolder=(ViewHolder)view.getTag();
            localtext1=viewHolder.localtext1;
            localtext2=viewHolder.localtext2;
        }

        int fenge=musicname[i].indexOf("-");
        if (fenge>0) {
            Log.e("分割位置", "分割位置" + fenge);
            String text1 = musicname[i].substring(0, fenge);
            String text2 = musicname[i].substring(fenge, musicname[i].length());
            localtext1.setText("歌曲名称" + text2);
            localtext2.setText("歌手" + text1);
        }
        else{
            String text1=musicname[i];
            localtext1.setText("歌曲信息:"+text1);
            localtext2.setText("");
        }

        return view;
    }
    public static class ViewHolder{
        public TextView localtext1,localtext2;
    }
}
