package io.github.richardyjtian.photoframe;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.media.ExifInterface;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static android.support.media.ExifInterface.TAG_DATETIME;

public class Photo implements Serializable {
    private String name = "";
    private String imageUri;
    private String caption = "";
    private String people = "";
    private Boolean include_time = false;
    private String time = "";
    private Boolean include_location = false;
    private String location = "";
    private String key;

    public Photo(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }
    public Photo(){}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Uri getImageUri() {
        return Uri.parse(imageUri);
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getPeople() { return people; }
    public void setPeople(String people) { this.people = people; }

    public Boolean getInclude_time() { return include_time; }
    public String getTime() { return time; }

    /* Set by the user */
    /************************************************************************/
    public void setInclude_time(Activity activity, Boolean include_time) {
        this.include_time = include_time;
        if(include_time && time.isEmpty()) {
            setTime(activity);
        }
    }
    private void setTime(Activity activity) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(Uri.parse(imageUri));
            ExifInterface exif = new ExifInterface(inputStream);
            time = exif.getAttribute(TAG_DATETIME);
            Toast.makeText(activity, time, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(activity, "No time found", Toast.LENGTH_SHORT).show();
        }
    }
    /************************************************************************/

    /* Set by the database */
    /************************************************************************/
    public void setInclude_time(Boolean include_time) { this.include_time = include_time; }
    public void setTime(String time) { this.time = time; }
    /************************************************************************/

    public Boolean getInclude_location() { return include_location; }
    public String getLocation() { return location; }

    /* Set by the user */
    /************************************************************************/
    public void setInclude_location(Activity activity, Boolean include_location) {
        this.include_location = include_location;
        if(include_location && location.isEmpty())
            setLocation(activity);
    }
    private void setLocation(Activity activity) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(Uri.parse(imageUri));
            ExifInterface exif = new ExifInterface(inputStream);
            float[] latLong = new float[2];
            if (exif.getLatLong(latLong)) {
                float latitude = latLong[0];
                float longitude = latLong[1];
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    location = addresses.get(0).getLocality();
                    Toast.makeText(activity, location, Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            Toast.makeText(activity, "No location found", Toast.LENGTH_SHORT).show();
        }
    }
    /************************************************************************/

    /* Set by the database */
    /************************************************************************/
    public void setInclude_location(Boolean include_location) { this.include_location = include_location; }
    public void setLocation(String location) { this.location = location; }
    /************************************************************************/

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

}

