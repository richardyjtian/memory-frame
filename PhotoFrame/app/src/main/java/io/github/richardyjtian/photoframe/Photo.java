package io.github.richardyjtian.photoframe;

import android.net.Uri;

import java.io.Serializable;

public class Photo implements Serializable {
    private String name = "";
    private String imageUri; //Uri is not serializable, so we convert between a string
    private String caption = "";
    private String people = "";
    private Boolean include_time = false;
    private Boolean include_location = false;
    private String key;

    public Photo(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }
    public Photo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUri() { return Uri.parse(imageUri); }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) { this.people = people; }

    public Boolean getInclude_time() {
        return include_time;
    }

    public void setInclude_time(Boolean include_time) {
        this.include_time = include_time;
    }

    public Boolean getInclude_location() { return include_location; }

    public void setInclude_location(Boolean include_location) { this.include_location = include_location; }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

