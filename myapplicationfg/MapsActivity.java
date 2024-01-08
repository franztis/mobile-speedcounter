package com.example.myapplicationfg;

import androidx.fragment.app.FragmentActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get latitude and longitude values from the intent
        lat = getIntent().getDoubleExtra("lat", 0.0);
        lon = getIntent().getDoubleExtra("long", 0.0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for the initial location
        LatLng initialLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(initialLocation).title("Initial Location"));

        // Move the camera to the initial location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));

        // Retrieve points from the database and add markers
        addMarkersFromDatabase();
    }

    private void addMarkersFromDatabase() {
        SQLiteDatabase database = openOrCreateDatabase("mydb.db", MODE_PRIVATE, null);

        Cursor cursor = database.rawQuery("SELECT * FROM TABLE_GPS_DATA", null);

        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(1);
            double longitude = cursor.getDouble(2);

            // Add marker for each point from the database
            LatLng point = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(point).title("Point from Database"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        }

        cursor.close();
        database.close();
    }
}
