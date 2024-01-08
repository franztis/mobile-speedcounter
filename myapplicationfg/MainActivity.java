package com.example.myapplicationfg;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextText);
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key1","1000");
        editor.apply();
        Button myButton = findViewById(R.id.button3);
        myButton.setBackgroundColor(ContextCompat.getColor(this, R.color.myButtonColor));




    }
    public void run(View view) {
        String inputValue = editText.getText().toString();

        try {
            // Attempt to parse the input as an integer
            int inputValueInt = Integer.parseInt(inputValue);

            // If successful, start MainActivity2
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("myKey1", inputValue);
            startActivity(intent);
        } catch (NumberFormatException e) {
            // If parsing fails, show a Toast message to the user
            Toast.makeText(this, "Please enter a valid integer", Toast.LENGTH_SHORT).show();
        }
    }





    private void showMessage(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show();
    }
}