package com.example.frame;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText pwdField;
    private FirebaseAuth auth;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        View v = findViewById(R.id.bg2);

        v.getBackground().setAlpha(200);

        // login with firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.username);
        pwdField = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LogIn();
            }
        });
    }

    private void LogIn() {
        String email = emailField.getText().toString();
        String password = pwdField.getText().toString();

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
}
