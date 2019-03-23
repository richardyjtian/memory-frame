package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
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

        Photo photo = thePhotoArray.get(position);

        // Set image
        ImageView img = row.findViewById(R.id.img);
        Picasso.get().load(photo.getImageUri()).fit().centerCrop(Gravity.TOP).into(img);

        // Set text
        TextView label = row.findViewById(R.id.photo_name);
        label.setText(photo.getName());

        return row;
    }

}
