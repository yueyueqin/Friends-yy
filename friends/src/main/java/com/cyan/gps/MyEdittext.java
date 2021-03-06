package com.cyan.gps;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

import com.cyan.community.R;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-30
 * Time: 21:28
 * 类说明：
 */
public class MyEdittext extends AutoCompleteTextView
{
    private Context context;
    private Drawable before;
    private Drawable after;

    public MyEdittext(Context context) {
        super(context);
        this.context = context;
        inint();
    }

    public MyEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        inint();
    }

    public MyEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inint();
    }

    private void inint() {
        before = context.getResources().getDrawable(R.drawable.delete_gray);
        after = context.getResources().getDrawable(R.drawable.delete);
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                setdrawable();
            }

        });
        setdrawable();
    }


    private void setdrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, before, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, after, null);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (after != null && event.getAction() == MotionEvent.ACTION_UP) {

            int eventx = (int) event.getRawX();
            int eventy = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventx, eventy))
                setText("");

        }
        return super.onTouchEvent(event);
    }



}  
