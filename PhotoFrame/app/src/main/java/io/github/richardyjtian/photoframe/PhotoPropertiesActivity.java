package io.github.richardyjtian.photoframe;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoPropertiesActivity extends AppCompatActivity {

    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_properties);

        ImageView img = findViewById(R.id.img);

        Uri imageUri = Uri.parse(getIntent().getExtras().getString("imageUri"));
        //TODO: create a edittext for name
        photo = new Photo();
        photo.setName("Photo 1");
        photo.setImageUri(imageUri);

        Picasso.get().load(imageUri).into(img);
    }

    public void finish(View view){
        PhotoGalleryActivity.photoArray.add(photo);
        PhotoGalleryActivity.ArrayAdapter.notifyDataSetChanged();

        finish();
    }
}
