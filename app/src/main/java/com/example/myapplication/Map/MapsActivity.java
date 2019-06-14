package com.example.myapplication.Map;

import android.content.Context;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.BitmapDescriptor;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Photo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String TAG = "MapsActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = MapsActivity.this;

    private GoogleMap mMap;

    ImageView imageView;
    Target targethehe;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

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

       // setupGridView();

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
                 //   imgUrls.add(photos.get(i).getImage_path());
                    imgLati.add(photos.get(i).getLatitude());
                    imgLong.add(photos.get(i).getLongitude());




                    Log.d(TAG, "pierwszy for " + photos.get(i));
                    Log.d(TAG, "chujstwo  drugi log " + photos.get(i).getLatitude());
                }
                Log.d(TAG, "imgLong.size setupgrid:  " +imgLong.size());

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


        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(mContext.getString(R.string.dbname_photos));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    photos.add(singleSnapshot.getValue(Photo.class));
                }

//                ArrayList<String> imgUrls = new ArrayList<String>();
//                for(int i = 0; i < photos.size(); i++){
//                    imgUrls.add(photos.get(i).getImage_path());
//                }
                ArrayList<String> imgUrls = new ArrayList<String>();
                for(int i = 0; i < photos.size(); i++){
                    Log.d(TAG, "fgdhfgh " +photos.get(i).getImage_path());
                    imgLati.add(photos.get(i).getLatitude());
                    imgLong.add(photos.get(i).getLongitude());

                    //Log.d(TAG, "picasso: " + Picasso.get().load(photos.get(i).getImage_path()));

                    LatLng marker = new LatLng(imgLati.get(i), imgLong.get(i));
                    Picasso.get()
                            .load("https://firebasestorage.googleapis.com/v0/b/phototracker-54d8a.appspot.com/o/photos%2Fusers%2FRv5OaYsfbuetjsPBx0eCd51igSo2%2Fphoto31?alt=media&token=5bf86ee0-d199-4080-956e-029e634358c4")
                            .resize(50,50)
                            .centerCrop()
                            .into(imageView);

                    Picasso.get().load(photos.get(i).getImage_path()).into(target);


                    imageView.buildDrawingCache();
                    Bitmap bmap = imageView.getDrawingCache();

                    //Bitmap bmp = BitmapFactory.decodeStream(photos.get(i).getImage_path().openConnection().getInputStream());


                    mMap.addMarker(new MarkerOptions()
                            .position(marker)
                            .title(photos.get(i).getCaption())
                            .icon(BitmapDescriptorFactory.fromBitmap(bmap))
                    );

                }
                Log.d(TAG, "fgdhfgh " +imgLong.size());

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

    }




    private void someMethod() {
        Picasso.get(this).load("url").into(target);
    }

    @Override
    public void onDestroy() {  // could be in onPause or onStop
        Picasso.get(this).cancelRequest(target);
        super.onDestroy();
    }

}
