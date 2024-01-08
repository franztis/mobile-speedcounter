package com.example.myapplicationfg;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity2 extends AppCompatActivity implements LocationListener {
    TextView textView;
    LocationManager lm;
    String s;
    ConstraintLayout mainLayout;
    SQLiteDatabase database;
    double latitude;
    double longitude;
    float spedo;
    long timestamp;
    long prevtimestamp;
    String sql;
    MyTts myTts;
    int counter;

    double lastLatitude = 0.0;
    double lastLongitude = 0.0;
    boolean sedataButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = findViewById(R.id.textView2);
        s = getIntent().getStringExtra("myKey1");
        myTts = new MyTts(this);
        textView.setText(s);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        mainLayout = findViewById(R.id.idac2);
        database=openOrCreateDatabase("mydb.db",MODE_PRIVATE,null);
        String CREATE_TABLE_GPS_DATA = "CREATE TABLE IF NOT EXISTS TABLE_GPS_DATA  (" +
                "COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COLUMN_LATITUDE REAL," +
                "COLUMN_LONGITUDE REAL," +
                "COLUMN_SPEED REAL," +
                "COLUMN_TIMESTAMP INTEGER);";
        database.execSQL(CREATE_TABLE_GPS_DATA);
        sql = "INSERT OR IGNORE INTO TABLE_GPS_DATA (COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_SPEED, COLUMN_TIMESTAMP) VALUES (?, ?, ?, ?)";
    }
    public void sedata (View view){
        stopLocationUpdates();
        Cursor cursor =database.rawQuery("Select * from TABLE_GPS_DATA",null);
        showMessage("warning","speedcounting stoped");
        StringBuilder data = new StringBuilder();

        while(cursor.moveToNext()){
            data.append("latidude"+cursor.getDouble(1)+"\n");
            data.append("lontitude"+cursor.getDouble(2)+"\n");
            data.append("speed"+cursor.getFloat(3)+"\n");
            data.append("timestap"+cursor.getLong(4)+"\n");
            lastLatitude = cursor.getDouble(1); // Assuming latitude is at index 1
            lastLongitude = cursor.getDouble(2);
        }
        showMessage("recird",data.toString());
        sedataButtonPressed = true;


    }
    public void  mape(View view){
        showMessage("warning","speedcounting stoped");

        if (sedataButtonPressed) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("lat", lastLatitude);
            intent.putExtra("long", lastLongitude);
            startActivity(intent);
        } else {
            showMessage("Error", "Please press the sedata button first.");
        }

    }

    public  void lastweek(View view){
        stopLocationUpdates();
        Calendar currentTime = Calendar.getInstance();
        long currentTimeInMillis = currentTime.getTimeInMillis();


        Cursor cursor =database.rawQuery("Select * from TABLE_GPS_DATA",null);
        StringBuilder datar = new StringBuilder()    ;
        while(cursor.moveToNext()){
            long gpsTimeInMillis  = cursor.getLong(4);
            long timeDifferenceInMillis = currentTimeInMillis - gpsTimeInMillis;
            int daysDifference = (int) (timeDifferenceInMillis / (24 * 60 * 60 * 1000));
            if (daysDifference < 7){
                datar.append("latidude"+cursor.getDouble(1)+"\n");
                datar.append("record"+cursor.getDouble(2)+"\n");
                datar.append("speed"+cursor.getFloat(3)+"\n");
                datar.append("timestap"+cursor.getLong(4)+"\n");}

        }
        showMessage("recird",datar.toString());

    }
    public void runer(View view) {
        myTts.speak("please slow down");
        counter=0;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        String speed = String.valueOf(location.getSpeed());
        if(counter==0){
            timestamp=location.getTime();
            prevtimestamp=timestamp;

        }
        else {
            timestamp=location.getTime();
        }

        spedo=location.getSpeed();
        longitude=location.getLongitude();
        latitude=location.getLatitude();




            float gpsSpeed = Float.parseFloat(speed);
            float speedInKilometersPerHour;
            speedInKilometersPerHour = (float) (gpsSpeed* 3.6);
            float limitSpeed = Float.parseFloat(s);
             textView.setText( String.valueOf(speedInKilometersPerHour));
            if (speedInKilometersPerHour>= limitSpeed) {
                myTts.speak("malaka  mano pane slower");
                mainLayout.setBackgroundColor(Color.RED);
                Object[] bindArgs = {latitude, longitude, spedo, timestamp};
               long dif =Math.abs( prevtimestamp-timestamp);
                if(dif >=30000||counter==0){
                    database.execSQL(sql, bindArgs);
                    prevtimestamp=timestamp;counter++;}}
                else{
                mainLayout.setBackgroundColor(Color.WHITE);}}
    private void showMessage(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    private void stopLocationUpdates() {
        if (lm != null) {
            // Check for permission before removing updates
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                 lm.removeUpdates(this);
                mainLayout.setBackgroundColor(Color.WHITE);
                textView.setText("paused");

            }
        }
    }




}
