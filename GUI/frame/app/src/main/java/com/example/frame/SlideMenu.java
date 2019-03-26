package com.example.frame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class SlideMenu extends HorizontalScrollView {

    private LinearLayout mll;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth, mMenuRightPadding;
    private int mMenuWidth = 0;

    private boolean once = false;
    private boolean isSlideOut;

    public static final int RIGHT_PADDING = 100;


    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;

        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SlideMenu.RIGHT_PADDING,
                context.getResources().getDisplayMetrics());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        if(!once){
            mll = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mll.getChildAt(0);
            mContent = (ViewGroup) mll.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        int action = ev.getAction();
        switch(action){
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if(scrollX >= mMenuWidth/2){
                    this.smoothScrollTo(mMenuWidth, 0);
                    isSlideOut = false;
                }
                else{
                    this.smoothScrollTo(0,0);
                    isSlideOut = true;
                }

                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void slideOutMenu(){
        if(!isSlideOut){
            this.smoothScrollTo(0,0);
            isSlideOut = true;
        }
        else {
            return;
        }
    }

    public void slideInMenu(){
        if(isSlideOut){
            this.smoothScrollTo(mMenuWidth, 0);
            isSlideOut = false;
        }
        else{
            return;
        }
    }

    public void switchMenu(){
        if(isSlideOut){
            slideInMenu();;
        }
        else{
            slideOutMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = l * 1.0f/mMenuWidth;
        float menuScale = 1.0f - scale*0.3f;
        float menuAlpha = 0.0f + 1.0f * (1 - scale);
        float contentScale = 0.8f + 0.2f *scale;

        ViewHelper.setScaleX(mMenu, menuScale);
        ViewHelper.setScaleY(mMenu, menuScale);
        ViewHelper.setAlpha(mMenu, menuAlpha);

        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleY(mContent, contentScale);
        ViewHelper.setScaleX(mContent, contentScale);
    }
}
