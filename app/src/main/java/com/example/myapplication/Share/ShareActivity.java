package com.example.myapplication.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.Utils.BottomNavigationViewHelper;
import com.example.myapplication.Utils.Permissions;
import com.example.myapplication.Utils.SectionsPagerAdapter;

import java.util.Objects;

/** Klasa przeznaczona do przygotowania zdjęcia z atrybutami do udostępnienia do bazy danych
 *
 */
public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";


    private static final int ACTIVITY_NUM = 1;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;


    private Context mContext = ShareActivity.this;

    /** \brief Metoda wywoływana przy tworzeniu activity
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "OnCreate: started ShareActivity");


        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            setupViewPager();
        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }
    /** \brief Metoda przeznaczona do zwracania numeru bieżącej zakładki
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    /** \brief Metoda przeznaczona do zwracania numeru bieżącej zakładki
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(getString(R.string.gallery));
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(getString(R.string.photo));

    }

    /** \brief Metoda przeznaczona do sprawdzenia czy nadane są uprawnienia
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /** \brief Metoda przeznaczona do sprawdzania tabeli uprawnień
     * @return zwraca true lub false w zależności od tego czy dane uprawnienie jest zaznaczone
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /** \brief Metoda przeznaczona do sprawdzania uprawnienia
     * @return zwraca true lub false w zależności od tego czy dane uprawnienie jest zaznaczone
     */
    public boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }


    public int getTask(){
        Log.d(TAG, "getTask: TASK: " + getIntent().getFlags());
        return getIntent().getFlags();
    }







}
