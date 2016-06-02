package com.cyan.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cyan.community.R;

import java.util.List;
import java.util.Map;

/**
 * Created by kys-34 on 2016/3/28.
 */
public class Adapter extends BaseAdapter {
    public Context context;
    private List<Map<String,String>> list;
    private LayoutInflater layoutInflater;

    public Adapter(Context context, List<Map<String, String>> list){
        this.context=context;
        this.list=list;
    }
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView1=null;
        TextView textView2=null;

        if (convertView==null){
            layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.musicadapter, null);
            textView1=(TextView)convertView.findViewById(R.id.text1);
            textView2=(TextView)convertView.findViewById(R.id.text2);

            Viewfind viewfind=new Viewfind();
            viewfind.textView1=textView1;
            viewfind.textView2=textView2;
            convertView.setTag(viewfind);
        }
        else {
            Viewfind viewfind=(Viewfind)convertView.getTag();
            textView1=viewfind.textView1;
            textView2=viewfind.textView2;
        }
        textView1.setText("歌曲名:    " + list.get(position).get("songname"));
        textView2.setText("演唱者:    "+ list.get(position).get("singername"));

        return convertView;
    }
    private final class Viewfind{
        public TextView textView1;
        public TextView textView2;
    }


}
