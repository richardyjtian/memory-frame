package io.github.richardyjtian.photoframe;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.common.FirstPartyScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PhotoGalleryActivity extends AppCompatActivity {
    // instance of our new custom array adaptor
    public static PhotoFrameArrayAdaptor ArrayAdapter;

    // dynamic array of Photos (populate at run time)
    public static ArrayList<Photo> photoArray = new ArrayList<Photo>();

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        mDatabase = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()); // set to user id
        mStorage = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photoArray.clear();
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            Photo p = new Photo();

            p.setName(ds.getValue(Upload.class).getName());
            p.setImageUri(ds.getValue(Upload.class).getImageUrl());
            p.setKey(ds.getValue(Upload.class).getKey());
            p.setCaption(ds.getValue(Upload.class).getCaption());
            p.setPeople(ds.getValue(Upload.class).getPeople());
            p.setInclude_time(ds.getValue(Upload.class).getInclude_time());
            p.setTime(ds.getValue(Upload.class).getTime());
            p.setInclude_location(ds.getValue(Upload.class).getInclude_location());
            p.setLocation(ds.getValue(Upload.class).getLocation());

            photoArray.add(0, p);
        }
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter = new PhotoFrameArrayAdaptor(this, android.R.layout.simple_list_item_1, photoArray);

        listView.setAdapter(ArrayAdapter);
        ArrayAdapter.notifyDataSetChanged();
    }

    public void photoSourceDialog(View view){
        // create a new AlertDialog Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the message and the Title
        builder.setTitle("Upload New Photo");
        // setup the dialog so that it cannot be cancelled by the back key (optional)
        builder.setCancelable(true);
        // We need a layout inflater to read our XML file and construct the layout
        LayoutInflater inflater = getLayoutInflater();

        final View promptView = inflater.inflate(R.layout.photo_gallery_dialogbox, null);
        Button upload = (Button) promptView.findViewById(R.id.upload);
        Button camera = (Button) promptView.findViewById(R.id.camera);
        ImageView uploadiv = (ImageView) promptView.findViewById(R.id.collection);
        ImageView cameraiv = (ImageView) promptView.findViewById(R.id.cam);


        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageSelect();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera();
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        uploadiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageSelect();
            }
        });

        cameraiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera();
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Inflate the photo_gallery_dialogbox.xml layout file and set as dialog view
        builder.setView(promptView);
        builder.show();
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    // Browse storage for an image
    public void imageSelect() {
        // Get storage permissions first
        Permissions.getStoragePermission(this);

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private static final int CAMERA_REQUEST = 2;
    // Open camera to take a photo
    public void camera() {
        // Get storage permissions too
        Permissions.getStoragePermission(this);
        // Get camera permissions first
        Permissions.getCameraPermission(this);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private static final int UPLOAD_IMAGE_REQUEST = 1;
    // Called after browsed for an image or taken a photo from the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image data exists
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            Photo photo = new Photo(imageUri);

            // Start a new activity with the Photo
            Intent intent = new Intent(PhotoGalleryActivity.this, PhotoPropertiesActivity.class);
            intent.putExtra("Photo", photo);
            startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
        }

        // Taken a photo
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            Photo photo = new Photo(imageUri);

            // Start a new activity with the Photo
            Intent intent = new Intent(PhotoGalleryActivity.this, PhotoPropertiesActivity.class);
            intent.putExtra("Photo", photo);
            startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
        }
    }
}
