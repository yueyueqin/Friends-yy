package com.cyan.gps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cyan.community.R;
import com.cyan.fragment.GpsFragment;
import com.cyan.ui.BaseActivity;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-30
 * Time: 10:45
 * 类说明：
 */
public class Routplan extends BaseActivity
{

    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rountinfo);
        listView = (ListView) findViewById(R.id.rountlist);
        textView=(TextView) findViewById(R.id.rountinfo_tv);

        if (GpsFragment.distance<1000) {
            textView.setText("总计"+GpsFragment.distance+"m");
        }else {
            textView.setText("总计"+(((float)(GpsFragment.distance))/1000)+"km");
        }
        listView.setAdapter(new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return GpsFragment.arrayList.size();
            }

            @Override
            public Object getItem(int position)
            {
                return GpsFragment.arrayList.get(position);
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.listitem, null);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.item_tv);
                String string = GpsFragment.arrayList.get(position);
                textView.setText(string);
                return convertView;
            }
        });

    }

}  
