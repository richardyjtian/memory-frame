package io.github.richardyjtian.photoframe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PhotoGalleryActivity extends AppCompatActivity {

    //TODO: a better way of doing this is saving it all in a file and referencing the file everytime, than having it be static
    // instance of our new custom array adaptor
    public static PhotoFrameArrayAdaptor ArrayAdapter;

    // dynamic array of Restaurants (populate at run time)
    public static ArrayList<String> photoArray = new ArrayList<String>();

    private String name;

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

        name = "";
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO:
                name = "Photo 1";
                // TODO: check if a picture was uploaded
                if(true) {
                    Intent intent = new Intent(PhotoGalleryActivity.this, PhotoPropertiesActivity.class);
                    intent.putExtra("key", "PHOTOID_CHANGETHIS");
                    startActivity(intent);
                }
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
                // TODO:
                if(name.length()>0) {
                    photoArray.add(name);
                    // notify the array adaptor that the array contents have changed (redraw)       
                    ArrayAdapter.notifyDataSetChanged();
                }
            }
        });

        // Inflate the photo_gallery_dialogbox.xml layout file and set as dialog view
        builder.setView(promptView);
        builder.show();
    }
}
