package com.example.testlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private String getLocation(String imagePath){
        try {
            final ExifInterface exifInterface = new ExifInterface(imagePath);
            float[] latLong = new float[2];
            if (exifInterface.getLatLong(latLong)) {
                String location = getCity(getApplicationContext(), latLong[0], latLong[1]);
                return location;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }


    public static String getCity(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {

                String city = addresses.get(0).getLocality();
                return city;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
