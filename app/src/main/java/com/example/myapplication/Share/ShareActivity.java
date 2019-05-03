package com.example.myapplication.Share;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.myapplication.R;
import com.example.myapplication.Utils.BottomNavigationViewHelper;
import com.example.myapplication.home.SectionsPagerAdapter;

import java.util.Objects;

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    private static final int ACTIVITY_NUM = 2;
    private ViewPager mViewPager;
    private Context mContext = ShareActivity.this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "OnCreate: started ShareActivity");
        //setupBottonNavigationView();

        //narazie bez sprawdzania uprawnien
        setupViewPager();
    }
    public int getCurrentTabNumber() {
        /*
        zwraca numer zakladki
        0 = GalleryFragment
        1 = PhotoFragment
         */
        return mViewPager.getCurrentItem();

    }
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(getString(R.string.gallery));
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(getString(R.string.photo));





    }

    private void setupBottonNavigationView(){
        Log.d(TAG, "setopBottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        //tu była metoda z gita, ktora zastapilem linijka z layout_bottom_navigation.xml "app:labelVisibilityMode="unlabeled">"
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
