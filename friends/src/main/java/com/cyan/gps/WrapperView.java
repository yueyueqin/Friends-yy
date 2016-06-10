package com.cyan.gps;

/**
 * Created by liuzipeng on 16/5/1.
 */
public class WrapperView {
    private com.cyan.gps.FabTagLayout mTarget;

    public WrapperView(com.cyan.gps.FabTagLayout mTarget) {
        this.mTarget = mTarget;
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }

    public int getHeight() {
        return mTarget.getLayoutParams().height;
    }

    public void setHeight(int height) {
        mTarget.getLayoutParams().height = height;
        mTarget.requestLayout();
    }
}
