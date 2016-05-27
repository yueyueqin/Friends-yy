package com.cyan.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.cyan.annotation.ActivityFragmentInject;
import com.cyan.community.R;
import com.cyan.fragment.MusicFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:20
 * 类说明：音乐功能
 */
@ActivityFragmentInject(
        contentViewId = R.layout.activity_music,
        toolbarTitle = R.string.music
)
public class MusicActivity extends  BaseActivity
{
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fragment)
    FrameLayout fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, MusicFragment.newInstance());
        transaction.commit();
    }
}  
