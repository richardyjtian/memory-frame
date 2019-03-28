package io.github.richardyjtian.photoframe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameActivity extends AppCompatActivity {

    private ImageView iv;
    private SlideMenu mSlideMenu;
    private TextView pg, sf, lg;
    private Uri imageUri, uri;

//    private final String IMAGE_TAG = "image", TEXT_TAG = "text";
//    private String[] from = {"image", "text"};
//    private int[] to = {R.id.frameimg, R.id.framename};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);


        iv = (ImageView) findViewById(R.id.figure);
        mSlideMenu = (SlideMenu) findViewById(R.id.menu);
        TextView pg = (TextView) findViewById(R.id.gallery);
        TextView sf = (TextView) findViewById(R.id.scan);
        TextView lg = (TextView) findViewById(R.id.logout);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        pg.setClickable(true);
        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File output = new File(Environment.getExternalStorageDirectory(), "Kkk.jpg");


                try {
                    if (output.exists()) {
                        output.delete();
                    } else {
                        output.createNewFile();
                    }
                }catch (IOException e) {
                        e.printStackTrace();
                    }

                imageUri = Uri.fromFile(output);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");

                intent.putExtra("crop", true);

                intent.putExtra("scale", true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,3);
            }

        });

        sf.setClickable(true);
        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrameActivity.this, BTActivity.class);
                startActivity(intent);
            }
        });

        lg.setClickable(true);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideMenu.isOpened()) {
                    mSlideMenu.closeMenu();
                } else {
                    mSlideMenu.openMenu();
                }
            }
        });

//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        SimpleAdapter pictureAdapter = new SimpleAdapter(this, getList(), R.layout.grid_pic, from, to);
//        gridview.setAdapter(pictureAdapter);

    }

    @Override
    public void onBackPressed() {
        if (mSlideMenu.isOpened()) {
            mSlideMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] titles = new String[]{"Photo Gallery", "Find Frame", "Log Out"};
        Integer[] images = {R.drawable.collections, R.drawable.addframe, R.drawable.power};

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("text", titles[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 3){
            if(resultCode == RESULT_OK){
                uri = data.getData();
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivity(intent);
            }
        }
    }

}

