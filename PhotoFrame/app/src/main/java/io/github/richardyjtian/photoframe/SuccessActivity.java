package io.github.richardyjtian.photoframe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


    public class SuccessActivity extends AppCompatActivity {

        Button create;
        View decorView;

        float downX, downY;
        float screenWidth, screenHeight;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_success);

            create = (Button) findViewById(R.id.createBtn);

            decorView = getWindow().getDecorView();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SuccessActivity.this, BTActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downX = event.getX();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float moveDistanceX = event.getX() - downX;
                if (moveDistanceX > 0) {
                    decorView.setX(moveDistanceX);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                float moveDistanceX = event.getX() - downX;
                if (moveDistanceX > screenWidth / 2) {
                    continueMove(moveDistanceX);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    rebackToLeft(moveDistanceX);
                }
            }
            return super.onTouchEvent(event);
        }

        private void continueMove(float moveDistanceX) {
            ValueAnimator anim = ValueAnimator.ofFloat(moveDistanceX, screenWidth);
            anim.setDuration(1000);
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float x = (float) (animation.getAnimatedValue());
                    decorView.setX(x);
                }
            });

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    finish();
                }
            });
        }

        private void rebackToLeft(float moveDistanceX) {
            ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
        }


    }
