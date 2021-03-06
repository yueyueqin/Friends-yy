package com.cyan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cyan.adapter.RecyclerArrayAdapter;
import com.cyan.adapter.UserAdapter;
import com.cyan.annotation.ActivityFragmentInject;
import com.cyan.bean.User;
import com.cyan.community.R;
import com.cyan.module.user.presenter.UserPresenter;
import com.cyan.module.user.presenter.UserPresenterImpl;
import com.cyan.module.user.view.GetUserView;
import com.cyan.ui.UserInfoActivity;
import com.cyan.widget.recyclerview.DividerItemDecoration;
import com.cyan.widget.recyclerview.EasyRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/3/30.
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_list)
public class UserFragment extends BaseFragment implements GetUserView,RecyclerArrayAdapter.OnLoadMoreListener{
    private EasyRecyclerView userList;
    private UserPresenter<User> userPresenter;
    private BmobQuery<User> query;
    private UserAdapter userAdapter;
    public static UserFragment newInstance(String key){
        UserFragment userFragment=new UserFragment();
        Bundle bundle=new Bundle();
        bundle.putString("key", key);
        userFragment.setArguments(bundle);
        return userFragment;
    }
    @Override
    protected void initView(View fragmentRootView) {
       userList=(EasyRecyclerView)fragmentRootView.findViewById(R.id.list);
       userList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        userList.getErrorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPresenter.getUser(query);
            }
        });
        userPresenter=new UserPresenterImpl(getActivity(),this);
        query=new BmobQuery<>();
        query.addWhereContains("username", getArguments().getString("key"));
        getUsers();
        mReSearchObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                userAdapter.clear();
                query = new BmobQuery<>();
                query.addWhereContains("username", s);
                userPresenter.getUser(query);
            }
        });
    }
    private void getUsers(){
        userList.setRefreshEnabled(false);
        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userAdapter=new UserAdapter(getActivity());
        userPresenter.getUser(query);
    }

    @Override
    public void onLoadMore() {
        query.addWhereLessThan("createdAt", userAdapter.getData().get(userAdapter.getData().size()-1));
        userPresenter.getUser(query);
    }

    @Override
    public void addUsers(List<User> list) {
        if(list.size()>0)
            if(userAdapter.getData().size()==0){
                userAdapter.addAll(list);
                userList.setAdapter(userAdapter);
                userAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                        intent.putExtra("user",userAdapter.getData().get(position));
                        startActivity(intent);
                    }
                });
                if(list.size()>=10){userAdapter.setMore(R.layout.view_more, this);
                userAdapter.setNoMore(R.layout.view_nomore).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userAdapter.resumeMore();
                    }
                });
                userAdapter.setError(R.layout.view_error);}
            }else{
                userAdapter.addAll(list);
            }

    }



    @Override
    public void showEmpty() {
         userList.showEmpty();
    }

    @Override
    public void showError() {
        if(userAdapter.getData().size()>=10)
            userAdapter.pauseMore();
        else userList.showError();
    }

    @Override
    public void showProgress(Boolean b) {
       if(b)userList.showProgress();
    }

    @Override
    public void showRecycler() {
      userList.showRecycler();
    }

    @Override
    public void stopLoadmore() {
        if(userAdapter.getData().size()>=10)
            userAdapter.stopMore();
    }

    @Override
    public void stopRefresh() {
      userList.setHeaderRefreshing(false);
    }
}
