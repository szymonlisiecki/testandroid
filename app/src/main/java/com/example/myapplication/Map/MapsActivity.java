package com.example.myapplication.Map;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.Login.LoginActivity;
import com.example.myapplication.Login.RegisterActivity;
import com.example.myapplication.Utils.ImageManager;
import com.google.android.gms.maps.model.BitmapDescriptor;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = MapsActivity.this;

    private Context context;

    private GoogleMap mMap;
    private Marker myMarker;


    ImageView imageView;

    Target targethehe;

    Target target;
    private Drawable mIcon = null;
    Marker mark;
    LatLng position;
    Bitmap bmap;
    //public static final String EXTRA_TEXT="";
    public static final String EXTRA_TEXT = "com.example.myapplication.EXTRA_NUMBER";
    //HashMap<String, String> PHOTOIDandURL = new HashMap<String, String>();
    HashMap<String, String> URLandPHOTO = new HashMap<String, String>();

    Vector globalURLs = new Vector();
    Vector globalPhotoIDs = new Vector();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    ArrayList<LatLng> danePozycja = new ArrayList<LatLng>();
    ArrayList<String> daneTytul = new ArrayList<String>();
    ArrayList<String> daneLink = new ArrayList<String>();


    Matrix matrix = new Matrix();


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

                ArrayList<String> imgUrls = new ArrayList<String>();
                for(int i = 0; i < photos.size(); i++){
                    //  Log.d(TAG, "fgdhfgh " +photos.get(i).getImage_path());
                    imgLati.add(photos.get(i).getLatitude());
                    imgLong.add(photos.get(i).getLongitude());

                    //link
                    //PHOTOIDandURL.put(photos.get(i).getPhoto_id(), photos.get(i).getImage_path());
                    // URLandPHOTO.put(photos.get(i).getPhoto_id(), photos.get(i).getImage_path());

                    globalURLs.add(photos.get(i).getImage_path());
                    globalPhotoIDs.add(photos.get(i).getPhoto_id());

                    position = new LatLng(imgLati.get(i), imgLong.get(i));

                    mMap.setOnMarkerClickListener(MapsActivity.this);
                    // loaded bitmap is here (bitmap)
                    myMarker = mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    //.title(photos.get(i).getCaption()+ photos.get(i).getImage_path())
                                    .title(photos.get(i).getPhoto_id())
                            // .icon(BitmapDescriptorFactory.fromBitmap(bmap))
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


    //bierzemy ID photy z markera
    public boolean onMarkerClick(final Marker marker) {

        String text="";

        for(int i=0; i< globalPhotoIDs.size(); i++){

            //porównanie PhotoID, przypisanie odpowiedniego linku do zmiennej text
            if(globalPhotoIDs.get(i).equals(marker.getTitle())){

                text = globalURLs.get(i).toString();

            }
        }
        Intent intent = new Intent(MapsActivity.this, MarkerPhotoActivity.class)
                .putExtra("linkDoZdjecia", text);

        startActivity(intent);
        return true;
    }





    public static  InputStream getHttpConnection(String urlString)  throws IOException {

        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("downloadImage" + ex.toString());
        }
        return stream;
    }




}