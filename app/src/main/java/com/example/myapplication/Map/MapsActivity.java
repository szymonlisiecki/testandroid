package com.example.myapplication.Map;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.myapplication.R;
import com.example.myapplication.Utils.BottomNavigationViewHelper;
import com.example.myapplication.Utils.GridImageAdapter;
import com.example.myapplication.models.Photo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = MapsActivity.this;

    private GoogleMap mMap;

    //get curret location
    private FusedLocationProviderClient fusedLocationClient;

    ArrayList<Float> imgLong = new ArrayList<Float>();
    ArrayList<Float> imgLati = new ArrayList<Float>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(this, "Maps Activity", Toast.LENGTH_SHORT).show();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        setupGridView();
    }


    private void setupGridView(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    photos.add(singleSnapshot.getValue(Photo.class));
                }

                ArrayList<String> imgUrls = new ArrayList<String>();
             //   ArrayList<Float> imgLongitudeCords = new ArrayList<Float>();
              //  ArrayList<Float> imgLatitudeCords = new ArrayList<Float>();
                for(int i = 0; i < photos.size(); i++){
                    imgUrls.add(photos.get(i).getImage_path());
                    imgLati.add(photos.get(i).getLatitude());
                    imgLong.add(photos.get(i).getLongitude());
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(this.mMap == null){
            mMap = googleMap;

        }

        String pin = "pin";

        //   ArrayList<Float> imgLongitudeCords = new ArrayList<Float>();
        //  ArrayList<Float> imgLatitudeCords = new ArrayList<Float>();

        ArrayList<LatLng> Pins = new ArrayList<LatLng>();

        for(int i = 0 ; i < imgLong.size() ; i++) {

            pin = pin + String.valueOf(i);

      //      LatLng sydney = new LatLng(53.446980, 14.492280);
            Pins.add(new LatLng(imgLati.get(i), imgLong.get(i)));
            mMap.addMarker(new MarkerOptions().position(Pins.get(i)).title(pin));
        }



        // Add a marker in Sydney and move the camera
     //   LatLng sydney = new LatLng(53.446980, 14.492280);
     //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
     //   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }




}
