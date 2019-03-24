package com.example.frame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FrameActivity extends AppCompatActivity {

//    ImageView v;
//    ImageView homeV;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        logout = (Button) findViewById(R.id.logout);
//        v = (ImageView) findViewById(R.id.home);
//        homeV = (ImageView) findViewById(R.id.fhome);
//        v.setClickable(true);
//        homeV.setClickable(true);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FrameActivity.this, BTActivity.class);
//                startActivity(intent);
//            }
//        });

//        homeV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FrameActivity.this, BTActivity.class);
//                startActivity(intent);
//            }
//        });
    }


}
