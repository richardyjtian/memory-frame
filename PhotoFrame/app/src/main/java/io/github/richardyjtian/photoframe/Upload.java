package io.github.richardyjtian.photoframe;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Upload {
    private  String name;
    private  String url;
    private  String caption;
    private  String people;
    private  String time;
    private  String location;
    private  String key;

    public Upload(){
    }

    public Upload(String name, String url){
        if (name.trim().equals("")){
            name = "no_name";
        }
        this.name = name;
        this.url = url;

    }
    public Upload(Photo p, String url){
        if (p.getName().equals("")){
            name = "no_name";
        }
        name = p.getName();
        this.url = url;
        caption = p.getCaption();
        people = p.getPeople();
        // TODO: time and location


    }

    public String getName(){
        return  name;
    }
    public void setName(String n){
        name = n;
    }

    public String getImageUrl(){
        return  url;
    }
    public void setImageUrl(String u){
        url = u;
    }

    public String getPeole(){
        return  people;
    }
    public void setPeople(String p){
        people = p;
    }

    public String getCaption(){
        return  caption;
    }
    public void setCaption(String c){
        caption = c;
    }

    public String getKey(){
        return  key;
    }
    public void setKey(String k){
        key = k;
    }

    private static StorageReference sRef = FirebaseStorage.getInstance().getReference("test2");
    private static DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("test2");
    private static StorageTask uploadTask;

    public static void uploadPhoto(final Activity activity, final Photo photo){
        Uri imageUri = photo.getImageUri();
        final String name = photo.getName();
        if (imageUri != null){
            final StorageReference fileRef = sRef.child(System.currentTimeMillis()+"."+getFileExtension(activity, imageUri));
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(activity, "Upload successful", Toast.LENGTH_SHORT).show();
                    Upload u = new Upload(photo, fileRef.getDownloadUrl().toString());
                    String uID = dRef.push().getKey();
                    u.setKey(uID);
                    dRef.child(uID).setValue(u);
                    //dRef.setValue("hello world");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No photo selected", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getFileExtension(Activity activity, Uri uri){
        ContentResolver cr = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
