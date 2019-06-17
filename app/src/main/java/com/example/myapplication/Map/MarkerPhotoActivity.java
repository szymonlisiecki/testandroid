package com.example.myapplication.Map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class MarkerPhotoActivity extends AppCompatActivity {


    Intent intent;
    private ImageView imageView;
    String text;
    Bundle bundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_photo);

       // imageView = findViewById(R.id.userPhoto);

        bundle = getIntent().getExtras();
        text = bundle.getString("linkdoZdjecia");


        text = getIntent().getStringExtra("linkDoZdjecia");

        if(text !=null) {
            Log.d("szymon", text);
        }


        new DownloadImageTask((ImageView) findViewById(R.id.userPhoto))
                .execute(text);
    }




}
