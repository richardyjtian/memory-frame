package io.github.richardyjtian.photoframe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button loginBtn;
    public static String email;
    View decorView;
    public static String LOGIN_NAME = "loginName";
    public static String MEM = "mf";
    public static int LOGIN_ACT = 10;
    float downX, downY;
    float screenWidth, screenHeight;
    public static String Name1 = null;
    public static String Password = null;

    EditText username, psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        decorView = getWindow().getDecorView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        View v1 = findViewById(R.id.loginFirstRow);
        v1.getBackground().setAlpha(200);

        View v2 = findViewById(R.id.loginSecondRow);
        v2.getBackground().setAlpha(200);



        loginBtn = (Button) findViewById(R.id.loginButton);
        username = (EditText) findViewById(R.id.loginUsername);
        Name1 = username.toString();
        psw = (EditText) findViewById(R.id.loginPassword);
        Password = psw.toString();

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        username.addTextChangedListener(new JumpText((username), psw));
    }



    private void LogIn() {
        email = username.getText().toString();
        String password = psw.getText().toString();

        // check to see if username and password are valid before attempting to login
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidPassword(password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            // TODO: redirect to logged in UI
                            Intent intent = new Intent(SecondActivity.this, FrameActivity.class);
                            intent.putExtra(LOGIN_NAME, username.getText().toString());
                            intent.putExtra(MEM, "Memory Frame");
                            intent.putExtra("from","SecondActivity");
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SecondActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * @param em - email
     * @return if true if em is valid email
     */
    private boolean isValidEmail(String em) {
        // TODO: add more complex check
        return em.trim().length() > 0;
    }

    /**
     * @param pwd
     * @return if true if the pwd is a valid password
     */
    private boolean isValidPassword(String pwd) {
        // TODO: add more complex check
        return pwd.trim().length() > 0;
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
