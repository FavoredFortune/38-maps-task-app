package com.android.sooz.errandrunner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int REQUEST_PERMISSION_GRANT = 1;
    private static final String TAG = "";

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LatLng mCurrentLocation;

    int LOCATION_REFRESH_TIME = 1;
    int LOCATION_REFRESH_DISTANCE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Intent data = getIntent();

        FirebaseDatabase.getInstance().getReference("errands/")
                .child(data.getStringExtra("errand item")).child(data.getStringExtra("id")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Errands errand = Errands.fromSnapshot(dataSnapshot);
                mMap.addMarker(new MarkerOptions().title("start").position(errand.start)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.addMarker(new MarkerOptions().title("end").position(errand.end)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                double centerLat = (errand.start.latitude + errand.end.latitude) / 2;
                double centerLng = (errand.start.longitude + errand.end.longitude) / 2;
                LatLng center = new LatLng(centerLat, centerLng);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(center));

                mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeLocationListener();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_PERMISSION_GRANT);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationListener() {
        LocationListener listener = this;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_PERMISSION_GRANT && grantResults[0] == RESULT_OK &&
                requestCode == REQUEST_PERMISSION_GRANT && grantResults[1] == RESULT_OK) {
            initializeLocationListener();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Seattle, WA.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Seattle and move the camera
        LatLng seattle = new LatLng(47, -122);
        mMap.addMarker(new MarkerOptions().position(seattle).title("Marker to Seattle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));
    }

    //show where user is looking when they change location
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrentLocation = latLng;
    }

    @OnClick(R.id.goToMyLocation)
    public void goToMyLocation(){
        if(mCurrentLocation != null) {

            //nice indicator code from classmate Amy Cohen
            //https://github.com/codefellows-seattle-java-401d1/38-maps-task-app/pull/1/files
            mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You are here"));

            mMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));
        }
    }


    @OnClick(R.id.goToMyErrands)
    public void goToMyErrands(){
        Intent intent = new Intent(this, ErrandListActivity.class);
        startActivity(intent);
    }

    //code for enabling and disabling GPS from codeproject.com
    //https://www.codeproject.com/Questions/613512/How-to-start-GPS-ON-and-OFF-programatically-in-And
    @OnClick(R.id.gpsOff)
    public void gpsOff(){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        Toast.makeText(getApplicationContext(),"GPS is now off", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.gpsOn)
    public void gpsOn(){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        Toast.makeText(getApplicationContext(),"GPS is now on", Toast.LENGTH_SHORT).show();
        sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {
        Log.d("GPS", "gps turned on");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("GPS", "gps turned off");
    }


    //adding these lifecycle logs to help me
    //follow app activity better
    @Override
    public void onStart() {
        super.onStart();
        Log.d("LIFECYCLE", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "onResume");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("LIFECYCLE", "onRestart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("LIFECYCLE", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "onDestroy");
    }

}