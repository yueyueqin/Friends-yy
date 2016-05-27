package com.cyan.module.focus.view;

import com.cyan.bean.Focus;

import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public interface FocusView {
    void showProgress();
    void showRecycler();
    void showError();
    void showEmpty();
    void stopLoadmore();
    void addFocus(List<Focus> list);
}
