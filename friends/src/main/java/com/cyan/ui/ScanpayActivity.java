package com.cyan.ui;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.cyan.annotation.ActivityFragmentInject;
import com.cyan.community.R;
import com.cyan.fragment.ScanpayFragment;

import butterknife.ButterKnife;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:28
 * 类说明：扫码支付功能
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_scanpay,
        toolbarTitle = R.string.scanpay
)
public class ScanpayActivity extends  BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, ScanpayFragment.newInstance());
        transaction.commit();
    }

}  
