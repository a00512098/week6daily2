package com.example.week6daily2;

import android.util.SparseArray;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class Utils {
    public static final String TAG = "Log.d";
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String LAT_LONG_REGEX = "^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$";

    public static int[] MAP_TYPES = {
            MAP_TYPE_NORMAL,
            MAP_TYPE_SATELLITE,
            MAP_TYPE_TERRAIN,
            MAP_TYPE_HYBRID
    };

    public static SparseArray<String> MAP_TYPE_NAMES = new SparseArray<>();

    static {
        MAP_TYPE_NAMES.append(MAP_TYPE_NORMAL, "Normal Map");
        MAP_TYPE_NAMES.append(MAP_TYPE_SATELLITE, "Satellite Map");
        MAP_TYPE_NAMES.append(MAP_TYPE_TERRAIN, "Terrain Map");
        MAP_TYPE_NAMES.append(MAP_TYPE_HYBRID, "Hybrid Map");
    }
}
