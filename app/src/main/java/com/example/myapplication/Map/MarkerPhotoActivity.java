package com.example.myapplication.Map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class MarkerPhotoActivity extends AppCompatActivity {


    Intent intent = getIntent();
    private ImageView imageView = findViewById(R.id.userPhoto);

   // String text = intent.getStringExtra(MapsActivity.EXTRA_TEXT);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_photo);

      //  Log.d("TEXT SUKI", text);
    }




}
