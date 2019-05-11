package com.example.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.myapplication.Likes.LikesActivity;
import com.example.myapplication.Map.MapsActivity;
import com.example.myapplication.home.MainActivity;
import com.example.myapplication.Profile.ProfileActivity;
import com.example.myapplication.R;
import com.example.myapplication.Share.ShareActivity;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHelper";

    public static void setupBottomNavigationView(BottomNavigationView bottomNavigationView){
        //tutaj powinny być wywolane metody z gita(paczka ktora nei dziala)


    }


    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, MainActivity.class); //ACTIVITY_NUM 0
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_circle:
                        Intent intent2 = new Intent(context, ShareActivity.class);//ACTIVITY_NUM 1
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_alert:
                        Intent intent3 = new Intent(context, LikesActivity.class);//ACTIVITY_NUM 2
                        context.startActivity(intent3);
                        break;

                    case R.id.ic_android:
                        Intent intent4 = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM 3
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_map:
                        Intent intent5 = new Intent(context, MapsActivity.class);//ACTIVITY_NUM 4
                        context.startActivity(intent5);
                        break;


                }

                return false;
            }
        });
    }
}
