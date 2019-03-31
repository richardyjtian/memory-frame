package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PhotoFrameArrayAdaptor extends ArrayAdapter<Photo> {
    // my new class variables, copies of constructor params, but add more if required
    private Context context;
    private ArrayList<Photo> thePhotoArray;

    // constructor
    public PhotoFrameArrayAdaptor(Context _context, int textViewResourceId, ArrayList<Photo> _thePhotoArray)
    {
        // call base class constructor
        super(_context, textViewResourceId, _thePhotoArray);

        // save the context and the array of strings we were given
        context = _context;
        thePhotoArray = _thePhotoArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate (R.layout.photo_gallery_row, parent, false );
        final int pos = position;
        final Photo photo = thePhotoArray.get(position);

        // Set image
        ImageView img = row.findViewById(R.id.img);
        Picasso.get().load(photo.getImageUri()).fit().centerCrop(Gravity.TOP).into(img);
        // Set image on click
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Start a new activity with the Photo
                Intent intent = new Intent(context, PhotoPropertiesActivity.class);
                intent.putExtra("Photo", photo);
                intent.putExtra("Position", pos);
                context.startActivity(intent);
            }
        });

        // Set text
        TextView label = row.findViewById(R.id.photo_name);
        label.setText(photo.getName());

        // Set delete button on click
        ImageView delete = row.findViewById(R.id.bin);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Photo toDelete = thePhotoArray.get(pos);

                // Remove the photo from firebase
                final String key = toDelete.getKey();
                // Delete picture in storage
                StorageReference imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(photo.getImageUri().toString());
                imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child(key).removeValue();
                    }
                });
            }
        });

        // TODO: have a checkbox to indicate if photo has been successfully uploaded
        return row;
    }

}
