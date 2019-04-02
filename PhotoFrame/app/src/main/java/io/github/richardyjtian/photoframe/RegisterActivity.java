package io.github.richardyjtian.photoframe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

        private Button registerBtn;
        private EditText email, user, psw;

        View decorView;

        float downX, downY;
        float screenWidth, screenHeight;

        private FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            getWindow().setBackgroundDrawableResource(R.drawable.lbackground);

            View v1 = findViewById(R.id.firstrow);
            v1.getBackground().setAlpha(200);
            View v2 = findViewById(R.id.secondrow);
            v2.getBackground().setAlpha(200);
            View v3 = findViewById(R.id.thirdrow);
            v3.getBackground().setAlpha(200);

            decorView = getWindow().getDecorView();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;


            registerBtn = (Button) findViewById(R.id.submitButton);
            email = (EditText) findViewById(R.id.E_mail);
            user = (EditText) findViewById(R.id.username);
            psw = findViewById(R.id.password);
            auth = FirebaseAuth.getInstance();

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, SuccessActivity.class);
                    startActivity(intent);
                }
            });
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.createUserWithEmailAndPassword(email.getText().toString(), psw.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(RegisterActivity.this, "account created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, SuccessActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            });


            email.setOnEditorActionListener(new TextView.OnEditorActionListener(){

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                }
            });

            user.setOnEditorActionListener(new TextView.OnEditorActionListener(){

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                }
            });


        }

        @Override
        public boolean onTouchEvent(MotionEvent event){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                downX = event.getX();
            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE){
                float moveDistanceX = event.getX() - downX;
                if(moveDistanceX > 0){
                    decorView.setX(moveDistanceX);
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                float moveDistanceX = event.getX() - downX;
                if(moveDistanceX > screenWidth/2){
                    continueMove(moveDistanceX);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    rebackToLeft(moveDistanceX);
                }
            }
            return super.onTouchEvent(event);
        }

        private void continueMove(float moveDistanceX){
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

        private void rebackToLeft(float moveDistanceX){
            ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
        }


        @CallSuper
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev){
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View view = getCurrentFocus();
                if (isShouldHideKeyBord(view, ev)) {
                    hideSoftInput(view.getWindowToken());
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
                return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
            }
            return false;
        }

        private void hideSoftInput(IBinder token) {
            if (token != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }



    }

