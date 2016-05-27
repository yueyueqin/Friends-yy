package com.cyan.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.cyan.annotation.ActivityFragmentInject;
import com.cyan.community.R;
import com.cyan.fragment.GpsFragment;

import butterknife.ButterKnife;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:40
 * 类说明：地图导航模块
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_gps,
        toolbarTitle = R.string.gps
)
public class GpsActivity extends  BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, GpsFragment.newInstance());
        transaction.commit();
    }
}  
