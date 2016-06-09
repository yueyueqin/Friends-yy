package com.cyan.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cyan.community.R;


/**
 * @author YueYue TODO 闪屏界面，根据指定时间进行跳转
 *         在activity_splash.xml中加入background属性并传入图片资源ID即可
 */
public class SplashActivity extends BaseActivity {

	private static final long DELAY_TIME = 2000L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// Bmob SDK初始化--只需要这一段代码即可完成初始化
		// 请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		//Bmob.initialize(this, "fce54ed751f43fbfdd89f03d1ca65101");


		redirectByTime();

	}

	/**
	 * 根据时间进行页面跳转
	 */
	private void redirectByTime() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				redictToActivity(SplashActivity.this, MainActivity.class, null);
				finish();
			}
		}, DELAY_TIME);
	}
	/**
	 * Activity跳转
	 *
	 * @param context
	 * @param targetActivity
	 * @param bundle
	 */
	public void redictToActivity(Context context, Class<?> targetActivity,
								 Bundle bundle) {
		Intent intent = new Intent(context, targetActivity);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

}
