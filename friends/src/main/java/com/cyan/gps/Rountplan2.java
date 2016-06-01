package com.cyan.gps;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cyan.community.R;
import com.cyan.fragment.GpsFragment;

public class Rountplan2 extends Activity {
	private ListView listView;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rountinfo);
		listView = (ListView) findViewById(R.id.rountlist);
		textView = (TextView) findViewById(R.id.rountinfo_tv);

		if (GpsFragment.distance < 1000) {
			textView.setText("总计" + GpsFragment.distance + "m");
		} else {
			textView.setText("总计" + (((float) (GpsFragment.distance)) / 1000)
					+ "km");
		}
		listView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View view, ViewGroup arg2) {
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.listitem, null);
				}
				TextView textView = (TextView) view.findViewById(R.id.item_tv);
				String string = GpsFragment.arrayList.get(position);
				textView.setText(string);
				return view
				;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return GpsFragment.arrayList.get(position);
			}

			@Override
			public int getCount() {
				return GpsFragment.arrayList.size();
			}
		});
	}

}
