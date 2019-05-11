package com.example.myapplication.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Login.LoginActivity;
import com.example.myapplication.Login.RegisterActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


//public class MainActivity extends AppCompatActivity implements View.OnClickListener <---- DO LOGOUTU

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext = MainActivity.this;
//DO LOGOUTU - ale nie działa jeszcze
//    //firebase auth object
//    private FirebaseAuth firebaseAuth;
//
//    //view objects
//    private TextView textViewUserEmail;
//    private Button buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: starting");
        setupBottonNavigationView();
        Toast.makeText(this, "MainActivity", Toast.LENGTH_SHORT).show();
        setupViewPager();

//DO LOGOUTU - ale nie działa jeszcze
//        firebaseAuth = FirebaseAuth.getInstance();
//        ///do logoutu
//        //if the user is not logged in
//        //that means current user will return null
//        if(firebaseAuth.getCurrentUser() == null){
//            //closing this activity
//            finish();
//            //starting login activity
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//
//        //getting current user
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        //initializing views
//        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
//        buttonLogout = (Button) findViewById(R.id.buttonLogout);
//
//        //displaying logged in user name
//        textViewUserEmail.setText("Welcome "+user.getEmail());
//
//        //adding listener to button
//        buttonLogout.setOnClickListener(this);

    }
    private void setupViewPager()
    {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessagesFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_image2vector1);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_map);

    }

    private void setupBottonNavigationView(){
        Log.d(TAG, "setopBottomNavigationView");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        //tu była metoda z gita, ktora zastapilem linijka z layout_bottom_navigation.xml "app:labelVisibilityMode="unlabeled">"
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    //DO LOGOUTU
//    @Override
//    public void onClick(View view) {
//        //if logout is pressed
//        if(view == buttonLogout){
//            //logging out the user
//            firebaseAuth.signOut();
//            //closing activity
//            finish();
//            //starting login activity
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//    }



}
