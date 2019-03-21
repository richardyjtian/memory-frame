package io.github.richardyjtian.photoframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PhotoPropertiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_properties);

        String photo_id = getIntent().getExtras().getString("key");
        Toast.makeText(this, photo_id, Toast.LENGTH_SHORT).show();
    }

    public void finish(View view){
        finish();
    }
}
