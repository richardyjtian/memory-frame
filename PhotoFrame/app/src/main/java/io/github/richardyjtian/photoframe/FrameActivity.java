package io.github.richardyjtian.photoframe;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.github.richardyjtian.photoframe.BTActivity.address;
import static io.github.richardyjtian.photoframe.RegisterActivity.Name2;
import static io.github.richardyjtian.photoframe.SecondActivity.LOGIN_ACT;
import static io.github.richardyjtian.photoframe.SecondActivity.Name1;
import static io.github.richardyjtian.photoframe.SecondActivity.Password;
import static io.github.richardyjtian.photoframe.SuccessActivity.SUCCESS_ACT;

public class FrameActivity extends AppCompatActivity {

    private ImageView iv;
    private SlideMenu mSlideMenu;
    private TextView pg, sf, lg;
    private Uri imageUri, uri;
    private TextView frameName, UsrName;
    private ProgressDialog progress;
    private String str = "Memory Frame";

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;

    private boolean isBtConnected = false;
    private int flag = 0;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public static PhotoFrameArrayAdaptor ArrayAdapter;

    // dynamic array of Photos (populate at run time)
    public static ArrayList<Photo> photoArray = new ArrayList<Photo>();

    private DatabaseReference mDatabase;
    private StorageReference mStorage;


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
        frameName = (TextView) findViewById(R.id.frameusername);
        UsrName = (TextView) findViewById(R.id.textView2);


//        requestWindowFeature(Window.FEATURE_NO_TITLE);


        Intent intent = getIntent();
        if(getIntent().getStringExtra("from").equals("SecondActivity")){
            frameName.setText(intent.getStringExtra(SecondActivity.MEM).toCharArray(),0,intent.getStringExtra(SecondActivity.MEM).toString().length());
            UsrName.setText(intent.getStringExtra(SecondActivity.LOGIN_NAME).toCharArray(),0, intent.getStringExtra(SecondActivity.LOGIN_NAME).toString().length());
        }
        else if (getIntent().getStringExtra("from").equals("BTActivity")){
            frameName.setText(intent.getStringExtra(BTActivity.EXTRA_FRAME_NAME).toCharArray(), 0, intent.getStringExtra(BTActivity.EXTRA_FRAME_NAME).toString().length());
            UsrName.setText(Name2.toCharArray(),0, Name2.toString().length());
            new ConnectBT().execute(); //Call the class to connect
            //sendText(UsrName.toString() + "*" + Password);
        }



//        frameName.setText("Memory Frame");

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


        //LOGOUT FUNCTION
        lg.setClickable(true);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrameActivity.this, MainActivity.class);
                startActivity(intent);
                Disconnect();
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




        //sendText("username" + "\n" + "passwaord");

    }

    private void sendText(String s){
        if (btSocket!=null)
        {
            try
            {
                msg("Sending");
                btSocket.getOutputStream().write(s.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Connection Error");
                isBtConnected = false;
                msg("Trying to reestablished connection");
                try
                {
                    btSocket.close(); //close connection
                }
                catch (IOException ei)
                { msg("CANNOT DISMISS");}
                new ConnectBT().execute();
            }
        }
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

    AlertDialog alert;
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
                alert.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera();
                alert.dismiss();
            }
        });

        uploadiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageSelect();
                alert.dismiss();
            }
        });

        cameraiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera();
                alert.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Inflate the photo_gallery_dialogbox.xml layout file and set as dialog view
        builder.setView(promptView);
        alert = builder.create();
        alert.show();
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
    Uri photoURI;
    // Open camera to take a photo
    public void camera() {
        // Get storage permissions too
        Permissions.getStoragePermission(this);
        // Get camera permissions first
        Permissions.getCameraPermission(this);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }

    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
            Intent intent = new Intent(FrameActivity.this, PhotoPropertiesActivity.class);
            intent.putExtra("Photo", photo);
            startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
        }

        // Taken a photo
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK
                ) {
            Uri imageUri = photoURI;

            Photo photo = new Photo(imageUri);

            // Start a new activity with the Photo
            Intent intent = new Intent(FrameActivity.this, PhotoPropertiesActivity.class);
            intent.putExtra("Photo", photo);
            startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
        }

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

//        //from register page
//        if(requestCode == SUCCESS_ACT){
//            if(resultCode == RESULT_OK){
//                Intent intent = getIntent();
//                frameName.setText(intent.getStringExtra(BTActivity.EXTRA_FRAME_NAME).toCharArray(), 0, intent.getStringExtra(BTActivity.EXTRA_FRAME_NAME).toString().length());
//                UsrName.setText(registerName.toCharArray(),0, registerName.toString().length());
//            }
//        }
//
//        if(requestCode == LOGIN_ACT){
//            if(resultCode == RESULT_OK){
//                Intent intent = getIntent();
//                frameName.setText(intent.getStringExtra(SecondActivity.MEM).toCharArray(),0,intent.getStringExtra(SecondActivity.MEM).toString().length());
//                UsrName.setText(intent.getStringExtra(SecondActivity.LOGIN_NAME).toCharArray(),0, intent.getStringExtra(SecondActivity.LOGIN_NAME).toString().length());
//            }



    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(FrameActivity.this, "Connecting...", "Please wait");  //Show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //While the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//Get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//Connects to the device's address and checks if it is available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//Create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//Start the connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//If the try failed, you can check the exception here
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            //Sends an error message if the connection is failed
            if (!ConnectSuccess) {
                msg("Connection Failed. Please try again.");
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

        //Method to disconnect the bluetooth connection
        private void Disconnect()
        {
            if (btSocket!=null) //If the btSocket is busy
            {
                try
                {
                    btSocket.close(); //close connection
                }
                catch (IOException e)
                { msg("Error");}
            }

            finish();
        }


        //Method to display a Toast
        public void msg(String s)
        {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}

