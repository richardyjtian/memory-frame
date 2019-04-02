package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class BaseLayout extends FrameLayout {
    protected int radius = 15;

    public BaseLayout(Context context){
        super(context);
        init();
    }

    public BaseLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        radius = getResources().getDimensionPixelSize(R.dimen.radius);
    }
}
