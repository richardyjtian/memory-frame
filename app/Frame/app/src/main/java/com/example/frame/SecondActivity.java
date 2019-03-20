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
    private EditText usr;
    private EditText psw;
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
        usr = findViewById(R.id.username);
        psw = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LogIn();
            }
        });
    }

    private void LogIn(){
        String email = usr.getText().toString();
        String password = psw.getText().toString();

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





}
