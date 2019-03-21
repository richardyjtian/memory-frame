package io.github.richardyjtian.photoframe;

import android.net.Uri;

import java.util.ArrayList;

public class Photo {
    private String name;
    private Uri imageUri;
    private ArrayList<String> people_in_photo;
    private String caption;
    private Boolean include_time;

    public Photo(){

    }

    public Photo(String name, Uri imageUri, ArrayList<String> people_in_photo, String caption, Boolean include_time) {
        this.name = name;
        this.imageUri = imageUri;
        this.people_in_photo = people_in_photo;
        this.caption = caption;
        this.include_time = include_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public ArrayList<String> getPeople_in_photo() {
        return people_in_photo;
    }

    public void setPeople_in_photo(ArrayList<String> people_in_photo) {
        this.people_in_photo = people_in_photo;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getInclude_time() {
        return include_time;
    }

    public void setInclude_time(Boolean include_time) {
        this.include_time = include_time;
    }
}

