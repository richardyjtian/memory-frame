package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MainView extends RelativeLayout {

    private SlideMenu mSlideMenu;

    public MainView(Context context) {
        this(context, null, 0);
    }

    public MainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setParent(SlideMenu slideMenu) {
        mSlideMenu = slideMenu;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlideMenu.isOpened()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mSlideMenu.closeMenu();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mSlideMenu.isOpened()) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }
}