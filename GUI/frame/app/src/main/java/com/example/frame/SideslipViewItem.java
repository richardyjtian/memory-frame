package com.example.frame;

import android.view.View;

public class SideslipViewItem {
    private View layout; //View视图
    private float scale = 0.6f; //显示比例,宽度占控件宽度的比例

    public SideslipViewItem(View layout, float scale) {
        this.layout = layout;
        this.scale = scale;
    }

    public View getLayout() {
        return layout;
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}