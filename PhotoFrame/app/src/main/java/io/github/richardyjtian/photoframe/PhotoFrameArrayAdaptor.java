package io.github.richardyjtian.photoframe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PhotoFrameArrayAdaptor extends ArrayAdapter<String> {
    // my new class variables, copies of constructor params, but add more if required
    private Context context;
    private ArrayList<String> thePhotoArray; //TODO: change to array of photos

    // constructor
    public PhotoFrameArrayAdaptor(Context _context, int textViewResourceId, ArrayList<String> _thePhotoArray)
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

        // Set background
        row.setBackgroundResource(R.drawable.lbackground);

        // Set text
        TextView label = (TextView) row.findViewById(R.id.photo_name);
        label.setText(thePhotoArray.get(position));

        return row;
    }

}
