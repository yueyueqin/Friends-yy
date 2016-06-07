package com.cyan.gas;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cyan.app.MyApplication;
import com.cyan.bean.User;
import com.cyan.community.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author YueYue
 */
public class Information_fill extends FragmentActivity implements OnClickListener,OnItemSelectedListener {


	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private TextView order_timepicker,order_jiayouliang;

	private TextView order_total, order_corrent;
	private TextView station_name,order_name;
	private String gasname;
	private ArrayList<Petrol> gasprice;
	private Spinner order_model;
	private String thegasprice;

	private SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");


	private String[] timeData=new String[13];
	private TimePickerPopupWindow pickerPopupWindow;
	private Button btn;
	private AlertDialog alertDialog;
	private WheelView wheelView;

	User myUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_fill);
		order_timepicker = (TextView) findViewById(R.id.order_timepicker);
		order_timepicker.setOnClickListener(this);
		station_name=(TextView)findViewById(R.id.station_name);
		order_name = (TextView) findViewById(R.id.order_name);

		myUser = MyApplication.getInstance().getCurrentUser();
		order_name.setText(myUser.getUsername());

		order_model = (Spinner) findViewById(R.id.order_model);
		order_jiayouliang = (TextView) findViewById(R.id.order_jiayouliang);
		order_jiayouliang.setOnClickListener(this);
		order_total = (TextView) findViewById(R.id.order_total);
		order_corrent = (TextView) findViewById(R.id.order_corrent);
		order_corrent.setOnClickListener(this);
		init();
	}
	public void init(){
		Bundle bundle = getIntent().getExtras();
		gasname=bundle.getString("Name");
		gasprice=bundle.getParcelableArrayList("Gas");
		station_name.setText(gasname);
		String[] gaspricespinner=new String[gasprice.size()];
		for(int i=0;i<gasprice.size();i++){
			gaspricespinner[i]=gasprice.get(i).getType()+":"+gasprice.get(i).getPrice();
		}
		ArrayAdapter<String> ordermodeladapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,gaspricespinner);
		order_model.setAdapter(ordermodeladapter);
		order_model.setOnItemSelectedListener(this);
		//数字选择器部分数字的声明
		timeData[0]="1";
		timeData[1]="2";
		timeData[2]="3";
		timeData[3]="4";
		timeData[5]="5";
		timeData[6]="6";
		timeData[7]="7";
		timeData[8]="8";
		timeData[9]="9";
		timeData[10]="10";
		timeData[11]="11";
		timeData[12]="12";
	}
	private void initAlertDialog(){
		View view = LayoutInflater.from(Information_fill.this).inflate(
				R.layout.pop_menu, null);
		wheelView = (WheelView) view.findViewById(R.id.timeWheel);
		wheelView.setAdapter(new StrericWheelAdapter(timeData));
		wheelView.setCurrentItem(1);
		wheelView.setCyclic(true);
		wheelView.setInterpolator(new AnticipateOvershootInterpolator());

		view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(alertDialog!=null&&alertDialog.isShowing())
				{
					alertDialog.dismiss();
				}

			}
		});
		view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(alertDialog!=null&&alertDialog.isShowing())
				{
					order_jiayouliang.setText(timeData[Integer.valueOf(wheelView.getCurrentItem())]);

					alertDialog.dismiss();
					double total=Double.parseDouble(order_jiayouliang.getText().toString())*Double.parseDouble(thegasprice);
					order_total.setText(String.valueOf(total));
				}

			}
		});

		alertDialog = new AlertDialog.Builder(Information_fill.this)
				.create();
		alertDialog.show();

		WindowManager.LayoutParams params = alertDialog.getWindow()
				.getAttributes();
		params.width = Information_fill.this.getWindowManager().getDefaultDisplay()
				.getWidth() ;
		int height=Information_fill.this.getWindowManager().getDefaultDisplay()
				.getHeight();
		params.height= WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity= Gravity.BOTTOM;

		Window window = alertDialog.getWindow();
		window.setAttributes(params);
		window.setContentView(view);
		alertDialog.getWindow().setContentView(view);


	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.order_timepicker:
				// TODO Auto-generated method stub
				new SlideDateTimePicker.Builder(getSupportFragmentManager())
						.setListener(listener)
						.setInitialDate(new Date())
						//.setMinDate(minDate)
						//.setMaxDate(maxDate)
						//.setIs24HourTime(true)
						//.setTheme(SlideDateTimePicker.HOLO_DARK)
						//.setIndicatorColor(Color.parseColor("#990000"))
						.build()
						.show();

				break;
			case  R.id.order_jiayouliang:
				initAlertDialog();




				break;
		case R.id.order_corrent:

			if(order_jiayouliang.getText().toString().equals("")||order_jiayouliang.getText()==null){
				Toast.makeText(Information_fill.this, "请完善信息", Toast.LENGTH_SHORT).show();

			}else if (order_name.getText().toString().equals("")||order_name.getText()==null){
			Toast.makeText(Information_fill.this, "请完善信息", Toast.LENGTH_SHORT).show();

		}else if (order_timepicker.getText().toString().equals("")||order_timepicker.getText()==null){
			Toast.makeText(Information_fill.this, "请完善信息", Toast.LENGTH_SHORT).show();

		}

			else {

			Intent order_correntintent = new Intent(Information_fill.this,
					Information_detail.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", order_name.getText().toString());
			bundle.putString("data", order_timepicker.getText().toString());
			bundle.putString("model", thegasprice);
			bundle.putString("gasstation",station_name.getText().toString());
			bundle.putString("volume", order_jiayouliang.getText().toString());
			bundle.putString("total", order_total.getText().toString());
				order_correntintent.putExtras(bundle);
				startActivity(order_correntintent);
			}

		}
		
	}


	private SlideDateTimeListener listener = new SlideDateTimeListener() {

		@Override
		public void onDateTimeSet(Date date)
		{
			order_timepicker.setText(mFormatter.format(date));
			/*Toast.makeText(Information_fill.this,
					mFormatter.format(date), Toast.LENGTH_SHORT).show();*/
		}

		// Optional cancel listener
		@Override
		public void onDateTimeCancel()
		{
			Toast.makeText(Information_fill.this,
					"Canceled", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		String price=gasprice.get(arg2).getPrice();
		int yuanposition=price.indexOf("元");
		thegasprice=price.substring(0,yuanposition);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
