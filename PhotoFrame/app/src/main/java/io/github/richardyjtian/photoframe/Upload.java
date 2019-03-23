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
    private String[] tags;
    public Upload(){
    }

    public Upload(String name, String url){
        if (name.trim().equals("")){
            name = "no_name";
        }
        this.name = name;
        this.url = url;

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

    private static StorageReference sRef = FirebaseStorage.getInstance().getReference("uploads");
    private static DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("uploads");
    private static StorageTask uploadTask;

    public static void uploadPhoto(final Activity activity, Photo photo){
        Uri imageUri = photo.getImageUri();
        final String name = photo.getName();
        if (imageUri != null){
            final StorageReference fileRef = sRef.child(System.currentTimeMillis()+"."+getFileExtension(activity, imageUri));
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(activity, "upload successful", Toast.LENGTH_SHORT).show();
                    Upload u = new Upload(name, fileRef.getDownloadUrl().toString());
                    String uID = dRef.push().getKey();
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
