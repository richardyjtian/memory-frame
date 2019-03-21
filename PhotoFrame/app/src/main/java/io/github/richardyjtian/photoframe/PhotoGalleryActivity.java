package io.github.richardyjtian.photoframe;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhotoGalleryActivity extends AppCompatActivity {

    //TODO: a better way of doing this is saving it all in a file and referencing the file everytime, than having it be static
    // instance of our new custom array adaptor
    public static PhotoFrameArrayAdaptor ArrayAdapter;

    // dynamic array of Restaurants (populate at run time)
    public static ArrayList<Photo> photoArray = new ArrayList<Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        // create the new adaptors passing important params, such
        // as context, android row style and the array of strings to display
        ArrayAdapter = new PhotoFrameArrayAdaptor(this, android.R.layout.simple_list_item_1, photoArray);

        // get handles to the list view in the Custom Activity layout
        ListView myListView = (ListView) findViewById( R.id.listView2 );

        // TODO: add some action listeners for when user clicks on row in either list view
        // myListView.setOnItemClickListener(myListViewClickedHandler);

        // set the adaptor view
        myListView.setAdapter(ArrayAdapter);
    }

    public void uploadNewPhoto(View view){
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

        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFileChooser();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
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
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Called after browsed for an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image data exists
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Start a new activity with the imageUri
            Intent intent = new Intent(PhotoGalleryActivity.this, PhotoPropertiesActivity.class);
            intent.putExtra("imageUri", imageUri.toString());
            startActivity(intent);
        }
    }
}
