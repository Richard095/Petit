package com.example.petapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        show_veterinarians(googleMap);
    }


    public void show_veterinarians(GoogleMap googleMap){
        mMap = googleMap;
        float zoomLevel = 15.0f; //Zoom to specific location

        final LatLng clinica_veterinaria = new LatLng(21.9899278,-99.0170365);
        mMap.addMarker(new MarkerOptions().position(clinica_veterinaria).title("Clinica Veterinaria"));

        final LatLng animal_city = new LatLng(21.9909276,-99.0152019);
        mMap.addMarker(new MarkerOptions().position(animal_city).title("Animal City"));



        final LatLng P3 = new LatLng(21.996774,-99.0170452);
        mMap.addMarker(new MarkerOptions().position(P3).title("Veterinaria El Establo"));

        final LatLng P4 = new LatLng(21.9967741,-99.0242956);
        mMap.addMarker(new MarkerOptions().position(P4).title("Clínica Veterinaria Mi Mascota"));

        final LatLng P5 = new LatLng(21.9885962,-99.0172676);
        mMap.addMarker(new MarkerOptions().position(P5).title("+KOTAS y + Clínica Veterinaria"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(P5, zoomLevel));

        final LatLng P6 = new LatLng(21.9849588,-99.0138646);
        mMap.addMarker(new MarkerOptions().position(P6).title("Mundo Animal"));

        final LatLng P8 = new LatLng(21.9817987,-99.0097283);
        mMap.addMarker(new MarkerOptions().position(P8).title("El Reino veterinario"));

        final LatLng P9 = new LatLng(21.9828328,-99.0151983);
        mMap.addMarker(new MarkerOptions().position(P9).title("Veterinaria la Potranca"));

        final LatLng P10 = new LatLng(21.9864468,-99.014914);
        mMap.addMarker(new MarkerOptions().position(P10).title("Veterinaria Mundo Zoo"));



    }

}
