package com.cyan.gas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cyan.community.R;

public class StationInfoActivity extends Activity implements OnClickListener {

	private Context mContext;
	private TextView tv_title_right, tv_name, tv_distance, tv_area, tv_addr,tv_order;
	private ImageView iv_back;
private Button bt_tv_nav,bt_tv_order;
	private ScrollView sv;
	private ListView lv_gast_price, lv_price;
	private Station s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_info_gas);
		mContext = this;
		initView();
		setText();
	}

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_order=(TextView)findViewById(R.id.tv_order);
		tv_order.setOnClickListener(this);
		bt_tv_order=(Button)findViewById(R.id.bt_tv_order);
		bt_tv_order.setOnClickListener(this);
		bt_tv_nav = (Button) findViewById(R.id.bt_tv_nav);
		bt_tv_nav.setText("导航 ");
		bt_tv_nav.setOnClickListener(this);
		tv_title_right=(TextView)findViewById(R.id.tv_title_button);
		tv_title_right.setVisibility(View.INVISIBLE);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		lv_gast_price = (ListView) findViewById(R.id.lv_gast_price);
		
		lv_price = (ListView) findViewById(R.id.lv_price);
		sv = (ScrollView) findViewById(R.id.sv);
	}
	
	
	private void setText(){
		s = getIntent().getParcelableExtra("s");
		tv_name.setText(s.getName()+" - "+s.getBrand());
		tv_addr.setText(s.getAddr());
		tv_distance.setText(s.getDistance()+"m");
		tv_area.setText(s.getArea());
		PriceListAdapter gastPriceAdapter = new PriceListAdapter(mContext, s.getGastPriceList());
		lv_gast_price.setAdapter(gastPriceAdapter);
		PriceListAdapter priceAdapter = new PriceListAdapter(mContext, s.getPriceList());
		lv_price.setAdapter(priceAdapter);
		
		sv.smoothScrollTo(0, 0);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_tv_nav:
			Intent intent = new Intent(mContext,RouteActivity.class);
			intent.putExtra("lat", s.getLat());
			intent.putExtra("lon", s.getLon());
			intent.putExtra("locLat", getIntent().getDoubleExtra("locLat", 0));
			intent.putExtra("locLon", getIntent().getDoubleExtra("locLon", 0));
			startActivity(intent);
			break;
			case  R.id.bt_tv_order:
				Intent intent1=new Intent(mContext,Information_fill.class);
				Bundle bundle=new Bundle();
				bundle.putString("Name", s.getName());
				bundle.putParcelableArrayList("Gas", s.getGastPriceList());
				intent1.putExtras(bundle);
				startActivity(intent1);
		default:
			break;
		}
	}

}
