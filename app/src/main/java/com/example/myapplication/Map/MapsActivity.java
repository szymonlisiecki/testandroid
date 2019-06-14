package com.example.myapplication.Map;

import android.content.Context;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.BitmapDescriptor;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.maps.model.Marker;
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

    Target target;
    private Drawable mIcon = null;
    Marker mark;
    LatLng position;
    Bitmap bmap;
int i=0;

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
                for(i = 0; i < photos.size()-1; i++){
                    Log.d(TAG, "fgdhfgh " +photos.get(i).getImage_path());
                    imgLati.add(photos.get(i).getLatitude());
                    imgLong.add(photos.get(i).getLongitude());

                    //Log.d(TAG, "picasso: " + Picasso.get().load(photos.get(i).getImage_path()));




                    position = new LatLng(imgLati.get(i), imgLong.get(i));
//                    Picasso.get()
//                            .load("https://firebasestorage.googleapis.com/v0/b/phototracker-54d8a.appspot.com/o/photos%2Fusers%2FRv5OaYsfbuetjsPBx0eCd51igSo2%2Fphoto31?alt=media&token=5bf86ee0-d199-4080-956e-029e634358c4")
//                            .resize(50,50)
//                            .centerCrop()
//                            .into(imageView);

      //              Picasso.get().load(photos.get(i).getImage_path()).into(target);


//                    imageView.buildDrawingCache();
//                    bmap = imageView.getDrawingCache();

                    //Bitmap bmp = BitmapFactory.decodeStream(photos.get(i).getImage_path().openConnection().getInputStream());

                    //Marker marker = new Marker();
     //               PicassoMarker picassoMarker = new PicassoMarker(mark);
//                    Picasso.get()
//                            .load("https://firebasestorage.googleapis.com/v0/b/phototracker-54d8a.appspot.com/o/photos%2Fusers%2FRv5OaYsfbuetjsPBx0eCd51igSo2%2Fphoto31?alt=media&token=5bf86ee0-d199-4080-956e-029e634358c4")
//                            .into(picassoMarker);


                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/phototracker-54d8a.appspot.com/o/photos%2Fusers%2FRv5OaYsfbuetjsPBx0eCd51igSo2%2Fphoto31?alt=media&token=5bf86ee0-d199-4080-956e-029e634358c4").into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            bitmap = scaleDown(bitmap, 300, false);


                            // loaded bitmap is here (bitmap)
                            mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(photos.get(i).getCaption())
                                    //.icon(BitmapDescriptorFactory.fromBitmap(bmap))
                                    //.icon(BitmapDescriptorFactory.fromFile(target))
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            );


                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {}
                    });



                }
                Log.d(TAG, "fgdhfgh " +imgLong.size());

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });



    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

}
