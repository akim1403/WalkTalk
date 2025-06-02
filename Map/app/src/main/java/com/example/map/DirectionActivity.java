package com.example.map;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.map.directionhelpers.TaskLoadedCallback;
import com.example.map.directionhelpers.FetchURL;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;

    private ArrayList<Polyline> polylineList = new ArrayList<>();
    private String type;
    private String time;

    // private LocationCallback locationCallback;
    private MarkerOptions place1, place2, place3;
    Button getDirection;
    Button goHome;
    Button homePage;

    Button btnPlay;
    Button btnStop;

    ProgressBar progressBar;

    private MediaPlayer mediaPlayer;

    private TextView textLatLong;
    private Polyline currentPolyline;

    private String audioName;

    private static final int REQUEST_CODE_PERMISSION = 1;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOADING_DURATION = 15000;

    LocationRequest locationRequest;

    Marker userLocationMarker;

    TextView test;
    List<List<String>> markerPoints;

    MarkerOptions[] routePoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); // Initialize
                                                                                             // fusedLocationProviderClient
        locationRequest = locationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        getDirection = findViewById(R.id.btnGetDirection);
        goHome = findViewById(R.id.btnGoHome);
        homePage = findViewById(R.id.btnGoHomePage);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        Bundle bundle = getIntent().getExtras();
        String data = bundle.get("data").toString();
        String time1 = bundle.get("time").toString();
        type = data;
        time = time1;
        // markerPoints = new double[5][2];
        // markerPoints[0] = new double[]{38.887691, 121.532945};
        // markerPoints[1] = new double[]{38.888000, 121.540777};
        // markerPoints[2] = new double[]{38.884325, 121.544918};
        // markerPoints[3] = new double[]{38.881839, 121.539017};
        // markerPoints[4] = new double[]{38.878373, 121.537386};
        // routePoints = new MarkerOptions[5];

        // textLatLong = findViewById(R.id.textLatLong);
        getLastLocation();
        // place1 = new MarkerOptions().position(new LatLng(38.88404333279727,
        // 121.54452203505474)).title("Location 1");
        // place2 = new MarkerOptions().position(new LatLng(38.86860801450304,
        // 121.53456197968475)).title("Location 2");
        // textLatLong.setText("Latitude: " + place3.getPosition().latitude + ",
        // Longitude: " + place3.getPosition().longitude);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAudio(audioName);
                polylineList.get(0).remove();
                routePoints = new MarkerOptions[markerPoints.size()];
                // new FetchURL(DirectionActivity.this).execute(getUrl(place1.getPosition(),
                // place2.getPosition(), "walking"), "walking");
                for (int i = 0; i < markerPoints.size(); i++) {
                    LatLng markerPos = new LatLng(Double.parseDouble(markerPoints.get(i).get(0)),
                            Double.parseDouble(markerPoints.get(i).get(1)));
                    MarkerOptions markerPlace = new MarkerOptions().position(markerPos).title("Current Location");
                    markerPlace.icon(BitmapDescriptorFactory.fromResource(R.drawable.lilipadd));
                    mMap.addMarker(markerPlace);
                    routePoints[i] = markerPlace;
                }
                new FetchURL(DirectionActivity.this)
                        .execute(getUrl(place1.getPosition(), routePoints[0].getPosition(), "walking"), "walking");
                for (int i = 0; i < routePoints.length - 1; i++) {
                    new FetchURL(DirectionActivity.this).execute(
                            getUrl(routePoints[i].getPosition(), routePoints[i + 1].getPosition(), "walking"),
                            "walking");
                }
                new FetchURL(DirectionActivity.this).execute(
                        getUrl(routePoints[routePoints.length - 1].getPosition(), place2.getPosition(), "walking"),
                        "walking");
            }
        });

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Polyline polyline : polylineList) {
                    polyline.remove();
                }
                new FetchURL(DirectionActivity.this)
                        .execute(getUrl(place2.getPosition(), place3.getPosition(), "walking"), "walking");
            }
        });

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                }
                Intent intent = new Intent(DirectionActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });
        // 27.658143,85.3199503
        // 27.667491,85.3208583
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        // Delay the execution of hiding the ProgressBar
    }

    public double[] calculateRadiusRange(double latitude, double longitude, double radiusInMeters) {
        // Convert radius to degrees (approximate values)
        double radiusInDegrees = radiusInMeters / 111000.0;
        Log.d("myTag", "lat1 = " + latitude);
        // Calculate latitude range
        double minLatitude = latitude - radiusInDegrees;
        double maxLatitude = latitude + radiusInDegrees;

        // Calculate longitude range
        double minLongitude = longitude - radiusInDegrees / Math.cos(Math.toRadians(latitude));
        double maxLongitude = longitude + radiusInDegrees / Math.cos(Math.toRadians(latitude));

        // Return the latitude and longitude range as an array
        return new double[] { minLatitude, maxLatitude, minLongitude, maxLongitude };
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the
            // documentation
            // for ActivityCompat#requestPermissions for more details.
            // REQUEST_CODE_PERMISSION
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_CODE_PERMISSION);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    // mapFragment.getMapAsync(DirectionActivity.this);
                    // 0.0009509, 0.0049614
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude() + 0.0009509,
                            currentLocation.getLongitude() + 0.0049614);
                    double lat = currentLocation.getLatitude() + 0.0009509;
                    double lon = currentLocation.getLongitude() + 0.0049614;
                    // mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Current
                    // Location"));
                    place3 = new MarkerOptions().position(currentLatLng).title("Current Location");
                    place3.icon(BitmapDescriptorFactory.fromResource(R.drawable.lotuss));
                    mMap.addMarker(place3);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
                    double[] coord = new double[0];
                    try {
                        coord = getRoutes(currentLatLng);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    place1 = new MarkerOptions().position(new LatLng(coord[0], coord[1])).title("Location 1");
                    place2 = new MarkerOptions().position(new LatLng(coord[2], coord[3])).title("Location 2");
                    new FetchURL(DirectionActivity.this)
                            .execute(getUrl(place3.getPosition(), place1.getPosition(), "walking"), "walking");
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                    place1.icon(BitmapDescriptorFactory.fromResource(R.drawable.lotuss));
                    place2.icon(BitmapDescriptorFactory.fromResource(R.drawable.lotuss));
                    mMap.addMarker(place1);
                    mMap.addMarker(place2);
                    // textLatLong.setText("Latitude: " + place3.getPosition().latitude + ",
                    // Longitude: " + place3.getPosition().longitude);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // enableUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        ACCESS_LOCATION_REQUEST_CODE);
            }
        }
        // LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),
        // currentLocation.getLongitude());
        // this.mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My
        // Current Location"));
        // LatLng currentLatLng = new LatLng(38.8881962, 121.5293475);
        // this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    private void setUserLocationMarker(Location location) {
        // 0.0009509, 0.0049614
        LatLng latLng = new LatLng(location.getLatitude() + 0.0009509, location.getLongitude() + 0.0049614);
        if (userLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.froggggg));
            markerOptions.rotation(location.getBearing());
            userLocationMarker = mMap.addMarker(markerOptions);
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        } else {
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the
            // documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the
            // documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key="
                + "Your API Key";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        // if (currentPolyline != null)
        // currentPolyline.remove();
        Polyline polyline = currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        polylineList.add(polyline);
    }

    public LatLng getRoute(LatLng currentLocation) {
        Double lat = currentLocation.latitude;
        Double lon = currentLocation.longitude;
        LatLng start = new LatLng(38.88404333279727, 121.54452203505474);
        return start;
    }

    public double[] getRoutes(LatLng currentLocation) throws Exception {
        Double lat = currentLocation.latitude;
        Log.d("myTag", "lat2 = " + lat);
        Double lon = currentLocation.longitude;
        double[] range = calculateRadiusRange(lat, lon, 2000);
        Log.d("myTag", "current = " + lat + "," + lon + "latitude range = " + range[0] + "," + range[1]
                + "longitude range = " + range[2] + "," + range[3]);
        // test.setText("current = " + lat + "," + lon + "latitude range = " + range[0]
        // + "," + range[1] + "longitude range = " + range[2] + "," + range[3]);
        String[] routeInfo = getWalks(range, type, lat, lon);
        audioName = routeInfo[0];
        // test.setText(routeInfo[0]);
        double startLatitude = Double.parseDouble(routeInfo[2]);
        double startLongitude = Double.parseDouble(routeInfo[3]);
        double endLatitude = Double.parseDouble(routeInfo[4]);
        double endLongitude = Double.parseDouble(routeInfo[5]);

        return new double[] { startLatitude, startLongitude, endLatitude, endLongitude };
    }

    public String[] getWalks(double[] locationMinMax, String walk, Double lat, Double lon) throws Exception {
        String walkType = walk;
        String[] failedSearch = { "incorrect" };

        // Reading the data into an array from .csv
        ArrayList<String> WalkList = new ArrayList<String>();
        InputStream is = getResources().openRawResource(R.raw.walk);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        // Read the data from csv using try catch
        try {
            while ((line = reader.readLine()) != null) {
                WalkList.add(line);
            }
        } catch (IOException e) {
            Log.d("myTag", "caught exception");
        }
        // Remove headers
        WalkList.remove(0);

        // Check if its its within the bounds and correct walk type
        for (int i = 0; i < WalkList.size(); i++) {
            // Log.d("myTag", "index " + i);
            String[] walkDetails = WalkList.get(i).split(",", 6);
            String[] locationDetails = walkDetails[2].split(":", 2);
            String[] endLocationDetails = walkDetails[3].split(":", 2);
            String LonLatReplaceTemp = walkDetails[5].replace("\"", "");
            int count = 0;
            for (int k = 0; k < LonLatReplaceTemp.length(); k++) {
                if (LonLatReplaceTemp.charAt(k) == ':')
                    count++;
            }
            Log.d("myTag", "count =  " + count);
            Log.d("myTag", "count1 =  " + walkDetails[5]);
            // Log.d("myTag", "count " + count);
            // get the segment details, Split them up and put into lists of lists
            String[] latLongSegmentPairs = LonLatReplaceTemp.split(",", count);
            // Log.d("myTag", "split " + latLongSegmentPairs[4]);

            // split each pair and add it to the list
            markerPoints = new ArrayList<List<String>>();
            for (int l = 0; l < count; l++) {
                markerPoints.add(Arrays.asList(latLongSegmentPairs[l].split(":")));
            }
            // Log.d("myTag", "split working: " + markerPoints);

            Log.d("myTag", "walkType:  " + walkType + " " + walkDetails[1]);
            Log.d("myTag", "Time: " + time + " " + walkDetails[4]);

            // Check if the user inputs match the current item in the list
            // check if its outside the Lat smaller than min or bigger than max
            if (Double.parseDouble(locationDetails[0]) < locationMinMax[0]
                    || Double.parseDouble(locationDetails[0]) > locationMinMax[1]) {
                continue;
            }
            // check if its outside the long smaller than min or bigger than max
            if (Double.parseDouble(locationDetails[1]) < locationMinMax[2]
                    || Double.parseDouble(locationDetails[1]) > locationMinMax[3]) {
                continue;
            }
            // Function to check if the walk type is correct with any walk length selected
            if (walkDetails[1].equals(walkType) && time.equals("Any Length")) {
                String[] infoArr = { walkDetails[0], walkDetails[1], locationDetails[0], locationDetails[1],
                        endLocationDetails[0], endLocationDetails[1], walkDetails[4], walkDetails[5] };
                Log.d("myTag", "returning " + walkDetails[0] + walkDetails[1] + locationDetails[0] + locationDetails[1]
                        + endLocationDetails[0] + endLocationDetails[1] + walkDetails[4]);
                return infoArr;
            }
            // Log.d("myTag", "detials: " + time + walkDetails[4]+ " " + walkType +
            // walkDetails[1]);
            // Function to check if the time is correct and the walk type is correct
            if (walkDetails[1].equals(walkType) && time.equals(walkDetails[4])) {
                String[] infoArr = { walkDetails[0], walkDetails[1], locationDetails[0], locationDetails[1],
                        endLocationDetails[0], endLocationDetails[1], time, walkDetails[5] };

                Log.d("myTag", "returning " + walkDetails[0] + walkDetails[1] + locationDetails[0] + locationDetails[1]
                        + endLocationDetails[0] + endLocationDetails[1] + walkDetails[4]);

                return infoArr;
            } else {
                Log.d("myTag", "Not Found " + locationDetails[0] + " " + locationDetails[1]);
            }
        }
        String[] infoArr = { "chinese", "Nature", "38.894438", "121.525063", "38.906047", "121.529997" };
        return infoArr;
    }

    public void startAudio(String audioName) {
        int resId = getResources().getIdentifier(audioName, "raw", getPackageName());
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // enableUserLocation();
            } else {

            }
        }
    }
}

// AIzaSyDGkeIui7CMi6Gb_pOZAE7WpaCpLf7EGIo