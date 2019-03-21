package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Set image
        ImageView img = row.findViewById(R.id.img);
        Picasso.get().load(thePhotoArray.get(position).getImageUri()).into(img);

        // Set text
        TextView label = (TextView) row.findViewById(R.id.photo_name);
        label.setText(thePhotoArray.get(position).getName());

        return row;
    }

}
