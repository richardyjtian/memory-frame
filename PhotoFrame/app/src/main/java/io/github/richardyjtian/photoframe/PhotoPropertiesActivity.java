package io.github.richardyjtian.photoframe;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PhotoPropertiesActivity extends AppCompatActivity {

    private int position;
    private Photo photo;

    private EditText name_et;
    private EditText caption_et;
    private EditText people_et;
    private CheckBox time_taken_cb;
    private CheckBox location_taken_cb;

    private DatabaseReference mDatabase;
    private FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_properties);

        ImageView img = findViewById(R.id.img);
        name_et = findViewById(R.id.name);
        caption_et = findViewById(R.id.caption);
        people_et = findViewById(R.id.people);
        time_taken_cb = findViewById(R.id.time_taken);
        location_taken_cb = findViewById(R.id.location_taken);

        position = getIntent().getIntExtra("Position", 0);
        photo = (Photo) getIntent().getSerializableExtra("Photo");

        Picasso.get().load(photo.getImageUri()).into(img);
        name_et.setText(photo.getName());
        caption_et.setText(photo.getCaption());
        people_et.setText(photo.getPeople());
        time_taken_cb.setChecked(photo.getInclude_time());
        location_taken_cb.setChecked(photo.getInclude_location());

        mDatabase = FirebaseDatabase.getInstance().getReference("test4"); // set to user id
    }

    // Called when the done button is clicked
    public void finish(View view){
        setPhotoFields(view);
        // Check if calling activity from PhotoGalleryActivity
        if(getCallingActivity() != null) {
            // Upload the photo to firebase
            Upload.uploadPhoto(this, photo);
        }
        // If calling activity was from PhotoFrameAdaptor
        else {
            // Update the photo in firebase
            Upload.updatePhoto(this, photo);
        }
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

        photo.setInclude_time(this, time_taken_cb.isChecked());

        photo.setInclude_location(this, location_taken_cb.isChecked());
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
