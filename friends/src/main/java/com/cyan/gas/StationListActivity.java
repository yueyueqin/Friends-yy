package com.cyan.gas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cyan.community.R;

import java.util.List;

public class StationListActivity extends Activity {

	private Context mContext;
	private ListView lv_station;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_gas);
		mContext = this;
		initView();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lv_station = (ListView) findViewById(R.id.lv_station);
		if ( lv_station==null)
		{
			Log.e("lv_stationkong","++++++++");
		}
		final List<Station> list = getIntent().getParcelableArrayListExtra("list");
if ( list==null)
		{
			Log.e("listkong","++++++++");
		}
		if ( list==null)
		{
			Log.e("kong","++++++++");
		}
		StationListAdapter adapter = new StationListAdapter(mContext, list);
		if ( adapter==null)
		{
			Log.e("adapterkong","++++++++");
		}
		lv_station.setAdapter(adapter);

		lv_station.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,StationInfoActivity.class);
				intent.putExtra("s", list.get(position));
				intent.putExtra("locLat", getIntent().getDoubleExtra("locLat", 0));
				intent.putExtra("locLon", getIntent().getDoubleExtra("locLon", 0));
				startActivity(intent);
				
			}
		});

	}

}
