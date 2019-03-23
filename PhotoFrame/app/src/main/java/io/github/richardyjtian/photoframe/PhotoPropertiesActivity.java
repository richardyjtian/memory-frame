package io.github.richardyjtian.photoframe;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoPropertiesActivity extends AppCompatActivity {

    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_properties);

        ImageView img = findViewById(R.id.img);
        Uri imageUri = Uri.parse(getIntent().getExtras().getString("imageUri"));

        photo = new Photo();
        photo.setImageUri(imageUri);
        Picasso.get().load(imageUri).into(img);
    }

    public void finish(View view){
        setPhotoFields(view);

        PhotoGalleryActivity.photoArray.add(photo);
        PhotoGalleryActivity.ArrayAdapter.notifyDataSetChanged();

        // Save the photoArray to the save file
        FileIO.saveToFile(this, PhotoGalleryActivity.photoArray);

        finish();
    }

    public void setPhotoFields(View view){
        EditText name_et = findViewById(R.id.name);
        String name = name_et.getText().toString();
        if(!name.isEmpty())
            photo.setName(name);

        EditText caption_et = findViewById(R.id.caption);
        String caption = caption_et.getText().toString();
        if(!caption.isEmpty())
            photo.setCaption(caption);

        EditText people_et = findViewById(R.id.people);
        String people = people_et.getText().toString();
        if(!people.isEmpty())
            photo.setPeople(people);

        CheckBox time_taken_cb = findViewById(R.id.time_taken);
        photo.setInclude_time(time_taken_cb.isChecked());

        CheckBox location_taken_cb = findViewById(R.id.location_taken);
        photo.setInclude_location(location_taken_cb.isChecked());
    }
}
