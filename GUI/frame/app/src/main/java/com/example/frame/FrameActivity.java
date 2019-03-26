package com.example.frame;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.frame.SlideMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FrameActivity extends AppCompatActivity {

    private final String IMAGE_TAG = "image", TEXT_TAG = "text";
    private String[] from = {"image", "text"};
    private int[] to = {R.id.frameimg, R.id.framename};

    Button logout;

    private static final String TAG = "MainActivity";
    private SideslipLayout mSideslipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);


        logout = (Button) findViewById(R.id.logout);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        SimpleAdapter pictureAdapter = new SimpleAdapter(this, getList(), R.layout.grid_pic, from, to);
        gridview.setAdapter(pictureAdapter);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] titles = new String[]{"Discover Frame", "Add Frame", "Photo Gallery", "My Settings"};
        Integer[] images = {R.drawable.discover, R.drawable.addframe, R.drawable.collections, R.drawable.settings};

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("text", titles[i]);
            list.add(map);
        }
        return list;
    }

//    private void initView() {
//
//        //mSideslipLayout = (SideslipLayout) findViewById(R.id.sideslip_layout);
//
//        //主界面
//        View homeView = LayoutInflater.from(this).inflate(R.layout.home_layout, null, false);
//        mSideslipLayout.setHomeView(homeView);
//
//        //左边的侧滑菜单
//        View leftView = LayoutInflater.from(this).inflate(R.layout.left_layout, null, false);
//        mSideslipLayout.setLeftViewItem(new SideslipViewItem(leftView, 0.7f));
//
//        //底部的界面
//        //View bottomView = LayoutInflater.from(this).inflate(R.layout.bottom_layout, null, false);
//        //mSideslipLayout.setBottomViewItem(new SideslipViewItem(bottomView, 1f));
//
//        mSideslipLayout.setOnSideslipListener(new SideslipLayout.OnSideslipListener() {
//            @Override
//            public void onShow(int gravity) {
//                Log.i(TAG, "onShow: " + gravity);
//            }
//
//            @Override
//            public void onHide(int gravity) {
//                Log.i(TAG, "onHide: " + gravity);
//            }
//        });
 }

