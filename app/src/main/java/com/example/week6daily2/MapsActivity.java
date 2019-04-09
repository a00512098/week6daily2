package com.example.week6daily2;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.week6daily2.Utils.EMPTY;
import static com.example.week6daily2.Utils.LAT_LONG_REGEX;
import static com.example.week6daily2.Utils.MAP_TYPES;
import static com.example.week6daily2.Utils.MAP_TYPE_NAMES;
import static com.example.week6daily2.Utils.SPACE;
import static com.example.week6daily2.Utils.TAG;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener {

    private EditText searchET;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searchET = findViewById(R.id.updateMapText);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateMapButton:
                updateMap();
                break;
            case R.id.changeMapFab:
                changeTypeOfMap();
                break;
        }
    }

    private void changeTypeOfMap() {
        int currentType = mMap.getMapType();
        int newType = getNextMapType(currentType);
        mMap.setMapType(newType);
        notifyUserOfMapChange(newType);
    }

    private void notifyUserOfMapChange(int newType) {
        String mapType = MAP_TYPE_NAMES.get(newType);
        Toast.makeText(this, mapType, Toast.LENGTH_SHORT).show();
    }

    private static int getNextMapType(int currentType) {
        if (currentType == MAP_TYPES.length)
            return MAP_TYPES[0];

        return MAP_TYPES[currentType];
    }

    private void updateMap() {
        String address = searchET.getText().toString();

        if (address.isEmpty()) {
            Toast.makeText(this, "Enter a value first", Toast.LENGTH_SHORT).show();
            return;
        }

        // LAT_LONG_REGEX validates if the input is a latitude and longitude value
        // However, this value has to be validated to see if it's in a valid range
        try {
            if (address.matches(LAT_LONG_REGEX)) {
                getLocationFromCoordinates(address);
            } else {
                getLocationFromAddress(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocationFromCoordinates(String position) throws IOException {
        position = position.replace(SPACE, EMPTY); // Check if the string has a space
        String[] values = position.split(",");
        // values[0] -> Latitude
        // values[1] -> Longitude
        double latitude = Double.parseDouble(values[0]);
        double longitude = Double.parseDouble(values[1]);
        if (validateLatLng(latitude, longitude)) {
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                Log.d(TAG, "getLocationFromCoordinates: " + addresses.get(0));
            }
        } else {
            Toast.makeText(this, "Invalid Values", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocationFromAddress(String address) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(address, 1);
        if (addresses != null && addresses.size() > 0) {
            double lat = addresses.get(0).getLatitude();
            double lng = addresses.get(0).getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.addMarker(new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0)));
        } else {
            Toast.makeText(this, "Invalid Address", Toast.LENGTH_SHORT).show();
        }
    }

    // returns true if both values are valid
    private boolean validateLatLng(double lat, double lng) {
        // According to https://stackoverflow.com/a/13824556/3393178
        // Latitude: -85 to +85 (actually -85.05115 for some reason)
        // Longitude: -180 to +180
        return lat >= -85 && lat <= 85 && lng >= -180 && lng <= 180;
    }
}
