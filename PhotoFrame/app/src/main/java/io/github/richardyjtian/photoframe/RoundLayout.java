package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

public class RoundLayout extends BaseLayout{

    private RectF mRectF = new RectF();
    private Paint mPaint;


    public RoundLayout(Context context) {
        super(context);
    }

    public RoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(){
        super.init();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        super.draw(c);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);

        mRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(mRectF, radius, radius, mPaint);
    }
}
