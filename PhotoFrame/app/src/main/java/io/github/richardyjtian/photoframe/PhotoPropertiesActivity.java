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

    private int position;
    private Photo photo;

    private EditText name_et;
    private EditText caption_et;
    private EditText people_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_properties);

        ImageView img = findViewById(R.id.img);
        name_et = findViewById(R.id.name);
        caption_et = findViewById(R.id.caption);
        people_et = findViewById(R.id.people);

        position = getIntent().getIntExtra("Position", 0);
        photo = (Photo) getIntent().getSerializableExtra("Photo");

        Picasso.get().load(photo.getImageUri()).into(img);
        name_et.setText(photo.getName());
        caption_et.setText(photo.getCaption());
        people_et.setText(photo.getPeople());
    }

    // Called when the done button is clicked
    public void finish(View view){
        setPhotoFields(view);
        // Check if calling activity from PhotoGalleryActivity
        if(getCallingActivity() != null) {
            PhotoGalleryActivity.photoArray.add(0, photo);

            // Upload the photo to firebase
            Upload.uploadPhoto(this, photo);
        }
        // If calling activity was from PhotoFrameAdaptor
        else {
            PhotoGalleryActivity.photoArray.set(position, photo);

            //TODO: Change the photo in firebase

        }
        // Notify the ArrayAdapter
        PhotoGalleryActivity.ArrayAdapter.notifyDataSetChanged();

        // Save the photoArray to the save file
        FileIO.saveToFile(this, PhotoGalleryActivity.photoArray);

        finish();
    }

    public void setPhotoFields(View view){
        String name = name_et.getText().toString();
        if(!name.isEmpty())
            photo.setName(name);

        String caption = caption_et.getText().toString();
        if(!caption.isEmpty())
            photo.setCaption(caption);

        String people = people_et.getText().toString();
        if(!people.isEmpty())
            photo.setPeople(people);

        CheckBox time_taken_cb = findViewById(R.id.time_taken);
        photo.setInclude_time(time_taken_cb.isChecked());

        CheckBox location_taken_cb = findViewById(R.id.location_taken);
        photo.setInclude_location(location_taken_cb.isChecked());
    }
}
