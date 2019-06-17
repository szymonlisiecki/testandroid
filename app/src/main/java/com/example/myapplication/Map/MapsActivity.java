package com.example.myapplication.Map;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Photo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Vector;

/** Klasa przeznaczona do wyświetlenia mapy oraz markerów
 *
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = MapsActivity.this;

    private GoogleMap mMap;
    private Marker myMarker;

    LatLng position;

    Vector globalURLs = new Vector();
    Vector globalPhotoIDs = new Vector();

    //get curret location
    private FusedLocationProviderClient fusedLocationClient;

    ArrayList<Float> imgLong = new ArrayList<Float>();
    ArrayList<Float> imgLati = new ArrayList<Float>();

    /** \brief Metoda wywoływana przy tworzeniu activity
     */
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
     * \brief Manipuluje mapą, gdy będzie dostępna
     * Metoda uruchamiana gdy mapa jest już gotowa do użycia
     * Możemy tu dodac markery i linie, listenery oraz ustawić kamerę
     * Jeżeli usługa Google Play Services nie jest zainstalowana na bieżącym urządzeniu to użytkownik zostanie poproszony o instalcję
     * Użytkownik zostanie przekierowany do strony z instalacją Google Play Services
     * Po zainstalowaniu użytkownik wróci do aplikacji
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

                    imgLati.add(photos.get(i).getLatitude());
                    imgLong.add(photos.get(i).getLongitude());


                    globalURLs.add(photos.get(i).getImage_path());
                    globalPhotoIDs.add(photos.get(i).getPhoto_id());

                    position = new LatLng(imgLati.get(i), imgLong.get(i));

                    mMap.setOnMarkerClickListener(MapsActivity.this);
                    // loaded bitmap is here (bitmap)
                    myMarker = mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(photos.get(i).getPhoto_id())

                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    /**
     * \brief Metoda przeznaczona do przechwytywania kliknięć w marker
     * Pobiera URL i ID klikniętego zdjęcia i przesyła URL do MarkerPhotoActivity, gdzie następuje wyświetlenie podanego zdjęcia
     * @param marker
     * @return true
     */
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
}