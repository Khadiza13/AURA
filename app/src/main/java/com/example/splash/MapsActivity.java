package com.example.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{



    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private  String googlePlacesData;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 5000;

    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    protected synchronized void bulidGoogleApiClient() {

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if(currentLocationMarker != null)
        {
            currentLocationMarker.remove();

        }
        Log.d("lat = ",""+latitude);
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(18));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View v)
    {
        ImageButton searchButton = findViewById(R.id.B_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tf_location = findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList;

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                        if (addressList != null && !addressList.isEmpty()) {
                            mMap.clear(); // Clear existing markers on the map

                            for (int i = 0; i < addressList.size(); i++) {
                                LatLng latLng = new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(location);
                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                            }
                        } else {
                            // Handle the case where no locations were found
                            Toast.makeText(MapsActivity.this, "No locations found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case where the location is empty
                    Toast.makeText(MapsActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ImageButton hospital= findViewById(R.id.B_hopistals);
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Object[] dataTransfer = new Object[2];
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        GoogleMap map1= (GoogleMap) dataTransfer[0];
                        String url1=(String) dataTransfer[1];

                        DownloadUrl downloadURL = new DownloadUrl();
                        try {
                            googlePlacesData = downloadURL.readUrl(url1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<HashMap<String, String>> nearbyPlaceList;
                                DataParser parser = new DataParser();
                                nearbyPlaceList = parser.parse(googlePlacesData);
                                Log.d("nearbyplacesdata","called parse method");
                                showNearbyPlaces(nearbyPlaceList);

                            }
                        });
                    }
                    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
                    {
                        Log.d("nearbyplacesdata", "Number of places: " + nearbyPlaceList.size());
                        for(int i = 0; i < nearbyPlaceList.size(); i++)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                            String placeName = googlePlace.get("place_name");
                            String vicinity = googlePlace.get("vicinity");
                            double lat = Double.parseDouble( googlePlace.get("lat"));
                            double lng = Double.parseDouble( googlePlace.get("lng"));

                            LatLng latLng = new LatLng( lat, lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName + " : "+ vicinity);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                });

                Toast.makeText(MapsActivity.this, "Searching Nearby Hospitals", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton pharma= findViewById(R.id.B_schools);
        pharma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Object[] dataTransfer = new Object[2];
                String pharma = "pharmacy";
                String url = getUrl(latitude, longitude,pharma);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String url1=(String) dataTransfer[1];

                        DownloadUrl downloadURL = new DownloadUrl();
                        try {
                            googlePlacesData = downloadURL.readUrl(url1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<HashMap<String, String>> nearbyPlaceList;
                                DataParser parser = new DataParser();
                                nearbyPlaceList = parser.parse(googlePlacesData);
                                Log.d("nearbyplacesdata","called parse method");
                                showNearbyPlaces(nearbyPlaceList);

                            }
                        });
                    }
                    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
                    {
                        Log.d("nearbyplacesdata", "Number of places: " + nearbyPlaceList.size());
                        for(int i = 0; i < nearbyPlaceList.size(); i++)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                            String placeName = googlePlace.get("place_name");
                            String vicinity = googlePlace.get("vicinity");
                            double lat = Double.parseDouble( googlePlace.get("lat"));
                            double lng = Double.parseDouble( googlePlace.get("lng"));

                            LatLng latLng = new LatLng( lat, lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName + " : "+ vicinity);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                });

                Toast.makeText(MapsActivity.this, "Searching Nearby Pharmacies", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton res= findViewById(R.id.B_restaurants);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Object[] dataTransfer = new Object[2];
                String restaurant = "restaurant";
                String url = getUrl(latitude, longitude,restaurant);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String url1=(String) dataTransfer[1];

                        DownloadUrl downloadURL = new DownloadUrl();
                        try {
                            googlePlacesData = downloadURL.readUrl(url1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<HashMap<String, String>> nearbyPlaceList;
                                DataParser parser = new DataParser();
                                nearbyPlaceList = parser.parse(googlePlacesData);
                                Log.d("nearbyplacesdata","called parse method");
                                showNearbyPlaces(nearbyPlaceList);

                            }
                        });
                    }
                    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
                    {
                        Log.d("nearbyplacesdata", "Number of places: " + nearbyPlaceList.size());
                        for(int i = 0; i < nearbyPlaceList.size(); i++)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                            String placeName = googlePlace.get("place_name");
                            String vicinity = googlePlace.get("vicinity");
                            double lat = Double.parseDouble( googlePlace.get("lat"));
                            double lng = Double.parseDouble( googlePlace.get("lng"));

                            LatLng latLng = new LatLng( lat, lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName + " : "+ vicinity);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                });

                Toast.makeText(MapsActivity.this, "Searching Nearby Restaurants", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton atm= findViewById(R.id.B_atm);
        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Object[] dataTransfer = new Object[2];
                String atms = "atm";
                String url = getUrl(latitude, longitude,atms);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String url1=(String) dataTransfer[1];

                        DownloadUrl downloadURL = new DownloadUrl();
                        try {
                            googlePlacesData = downloadURL.readUrl(url1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<HashMap<String, String>> nearbyPlaceList;
                                DataParser parser = new DataParser();
                                nearbyPlaceList = parser.parse(googlePlacesData);
                                Log.d("nearbyplacesdata","called parse method");
                                showNearbyPlaces(nearbyPlaceList);

                            }
                        });
                    }
                    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
                    {
                        Log.d("nearbyplacesdata", "Number of places: " + nearbyPlaceList.size());
                        for(int i = 0; i < nearbyPlaceList.size(); i++)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                            String placeName = googlePlace.get("place_name");
                            String vicinity = googlePlace.get("vicinity");
                            double lat = Double.parseDouble( googlePlace.get("lat"));
                            double lng = Double.parseDouble( googlePlace.get("lng"));

                            LatLng latLng = new LatLng( lat, lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName + " : "+ vicinity);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                });

                Toast.makeText(MapsActivity.this, "Searching Nearby ATMs", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton bnk= findViewById(R.id.B_lib);
        bnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Object[] dataTransfer = new Object[2];
                String bank = "bank";
                String url = getUrl(latitude, longitude,bank);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String url1=(String) dataTransfer[1];

                        DownloadUrl downloadURL = new DownloadUrl();
                        try {
                            googlePlacesData = downloadURL.readUrl(url1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<HashMap<String, String>> nearbyPlaceList;
                                DataParser parser = new DataParser();
                                nearbyPlaceList = parser.parse(googlePlacesData);
                                Log.d("nearbyplacesdata","called parse method");
                                showNearbyPlaces(nearbyPlaceList);

                            }
                        });
                    }
                    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
                    {
                        Log.d("nearbyplacesdata", "Number of places: " + nearbyPlaceList.size());
                        for(int i = 0; i < nearbyPlaceList.size(); i++)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                            String placeName = googlePlace.get("place_name");
                            String vicinity = googlePlace.get("vicinity");
                            double lat = Double.parseDouble( googlePlace.get("lat"));
                            double lng = Double.parseDouble( googlePlace.get("lng"));

                            LatLng latLng = new LatLng( lat, lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName + " : "+ vicinity);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                });

                Toast.makeText(MapsActivity.this, "Searching Nearby Banks", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton ps= findViewById(R.id.B_gym);
        ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Object[] dataTransfer = new Object[2];
                String p = "police";
                String url = getUrl(latitude, longitude,p);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String url1=(String) dataTransfer[1];

                        DownloadUrl downloadURL = new DownloadUrl();
                        try {
                            googlePlacesData = downloadURL.readUrl(url1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<HashMap<String, String>> nearbyPlaceList;
                                DataParser parser = new DataParser();
                                nearbyPlaceList = parser.parse(googlePlacesData);
                                Log.d("nearbyplacesdata","called parse method");
                                showNearbyPlaces(nearbyPlaceList);

                            }
                        });
                    }
                    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
                    {
                        Log.d("nearbyplacesdata", "Number of places: " + nearbyPlaceList.size());
                        for(int i = 0; i < nearbyPlaceList.size(); i++)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                            String placeName = googlePlace.get("place_name");
                            String vicinity = googlePlace.get("vicinity");
                            double lat = Double.parseDouble( googlePlace.get("lat"));
                            double lng = Double.parseDouble( googlePlace.get("lng"));

                            LatLng latLng = new LatLng( lat, lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName + " : "+ vicinity);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                });

                Toast.makeText(MapsActivity.this, "Searching Nearby Police Stations", Toast.LENGTH_LONG).show();
            }
        });

    }
    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDyOKwbk-L5XoodFJB1ajcd7HOHvH_Tee4");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}