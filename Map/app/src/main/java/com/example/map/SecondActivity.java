package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    Button showMap;
    Button showDirection;

    ImageButton helpButton;

    ImageButton reco1Button;
    ImageButton reco2Button;
    ImageButton reco3Button;

    ImageButton favouriteButton;
    Button nonChinese;

    private Spinner spinner;
    private Spinner spinner2;

    String text;

    String timePicked;

    List<String> reco1;
    List<String> reco2;
    List<String> reco3;



    TextView test;
    ImageView reco1Image;
    ImageView reco2Image;
    ImageView reco3Image;
    TextView reco1Title;
    TextView reco2Title;
    TextView reco3Title;
    TextView reco1Text1;
    TextView reco2Text1;
    TextView reco3Text1;
    TextView reco1Text2;
    TextView reco2Text2;
    TextView reco3Text2;
    TextView reco1Text3;
    TextView reco2Text3;
    TextView reco3Text3;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE_PERMISSION = 1;
    Location currentLocation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //showMap = findViewById(R.id.showMap);
        showDirection = findViewById(R.id.directionMap);
        helpButton = findViewById(R.id.helpButton);
        nonChinese = findViewById(R.id.nonChineseButton);
        reco1Button = findViewById(R.id.reco1Button);
        reco2Button = findViewById(R.id.reco2Button);
        reco3Button = findViewById(R.id.reco3Button);
        favouriteButton = findViewById(R.id.favouriteButton);
        spinner = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner.setAdapter(null);
        spinner2.setAdapter(null);
        User.initUsers();
        spinner = (Spinner)findViewById(R.id.spinner1);
        SpinnerAdapter customAdapter = new SpinnerAdapter(this, R.layout.custom_spinner_adapter, User.getUserArrayList());
        Time.initTime();
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        SpinnerAdapter2 customAdapter2 = new SpinnerAdapter2(this, R.layout.custom_spinner_adapter, Time.getTimeArrayList());
        List<String[]> recoRoutes = new ArrayList<>();


        getCurrentLocation();
        showDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, DirectionActivity.class);
                intent.putExtra("data",String.valueOf(User.getCurrentUser()));
                intent.putExtra("time",String.valueOf(Time.getCurrentTime()));
                startActivity(intent);
            }
        });
        reco1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, DirectionReco.class);
                intent.putExtra("start",reco1.get(0));
                intent.putExtra("end",reco1.get(1));
                startActivity(intent);
            }
        });
        reco2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, DirectionReco.class);
                intent.putExtra("start",reco2.get(0));
                intent.putExtra("end",reco2.get(1));
                startActivity(intent);
            }
        });

        reco3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, DirectionReco.class);
                intent.putExtra("start",reco3.get(0));
                intent.putExtra("end",reco3.get(1));
                startActivity(intent);
            }
        });
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, DirectionReco.class);
                intent.putExtra("start","38.892696:121.537935");
                intent.putExtra("end","38.86860801450304:121.53456197968475");
                startActivity(intent);
            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        nonChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.clear();
                customAdapter2.clear();
                Intent intent = new Intent(SecondActivity.this, DirectionActivityNonChinese.class);
                startActivity(intent);
            }
        });

        spinner.setAdapter(customAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                text = User.getCurrentUser();
                Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "You Picked " + User.getCurrentUser());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setAdapter(customAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView2, View view, int i, long l) {
                text = Time.getCurrentTime();
                Toast.makeText(adapterView2.getContext(), text, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "You Picked " + Time.getCurrentTime());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ImageButton lg = findViewById(R.id.lg);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, personActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //REQUEST_CODE_PERMISSION
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude() + 0.0009509, currentLocation.getLongitude() + 0.0049614);
                    Log.d("myTag", "lat1 = " + currentLatLng.latitude + "long" + currentLatLng.longitude);
                    Double lat = currentLatLng.latitude;
                    Double lon = currentLatLng.longitude;
                    List<List<String>> recoList;
                    double[] range = calculateRadiusRange(lat, lon, 2000);
                    try {
                        recoList = getReco(range);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (recoList.get(0) != null) {
                        reco1 = recoList.get(0);
                        reco1Image = findViewById(R.id.reco1Image);
                        reco1Title = findViewById(R.id.reco1Title);
                        reco1Text1 = findViewById(R.id.reco1Text1);
                        reco1Text2 = findViewById(R.id.reco1Text2);
                        reco1Text3 = findViewById(R.id.reco1Text3);
                        String x = reco1.get(2);
                        int resourceId = getResources().getIdentifier(x, "drawable", getPackageName());
                        reco1Image.setImageResource(resourceId);
                        reco1Title.setText(reco1.get(3));
                        reco1Text1.setText(reco1.get(4));
                        reco1Text2.setText(reco1.get(5));
                        reco1Text3.setText(reco1.get(6));
                    }
                    if (recoList.get(1) != null) {
                        reco2 = recoList.get(1);
                        reco2Image = findViewById(R.id.reco2Image);
                        reco2Title = findViewById(R.id.reco2Title);
                        reco2Text1 = findViewById(R.id.reco2Text1);
                        reco2Text2 = findViewById(R.id.reco2Text2);
                        reco2Text3 = findViewById(R.id.reco2Text3);
                        String x = reco2.get(2);
                        int resourceId = getResources().getIdentifier(x, "drawable", getPackageName());
                        reco2Image.setImageResource(resourceId);
                        reco2Title.setText(reco2.get(3));
                        reco2Text1.setText(reco2.get(4));
                        reco2Text2.setText(reco2.get(5));
                        reco2Text3.setText(reco2.get(6));
                    }
                    if (recoList.get(2) != null) {
                        reco3 = recoList.get(2);
                        reco3Image = findViewById(R.id.reco3Image);
                        reco3Title = findViewById(R.id.reco3Title);
                        reco3Text1 = findViewById(R.id.reco3Text1);
                        reco3Text2 = findViewById(R.id.reco3Text2);
                        reco3Text3 = findViewById(R.id.reco3Text3);
                        String x = reco3.get(2);
                        int resourceId = getResources().getIdentifier(x, "drawable", getPackageName());
                        reco3Image.setImageResource(resourceId);
                        reco3Title.setText(reco3.get(3));
                        reco3Text1.setText(reco3.get(4));
                        reco3Text2.setText(reco3.get(5));
                        reco3Text3.setText(reco3.get(6));
                    }
                    Log.d("myTag", "first reco" + recoList.get(0));
                    Log.d("myTag", "first reco" + recoList.get(1));
                    Log.d("myTag", "first reco" + recoList.get(2));
                    Log.d("myTag", "Current " + lat + " - " + lon);
                    Log.d("myTag", "Range lat " + range[0] + " - " + range[1]);
                    Log.d("myTag", "Range long " + range[2] + " - " + range[3]);
                }
            }
        });
    }

    public List<List<String>> getReco(double[] locationMinMax) throws Exception {
        String walkType = text;
        String[] failedSearch = {"incorrect"};
        String[] correctSearch;
        List<List<String>> listOfLists = new ArrayList<>();

        // Reading the data into an array from .csv
        ArrayList<String> WalkList = new ArrayList<String>();
        InputStream is = getResources().openRawResource(R.raw.recodata);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        //Read the data from csv using try catch
        try {
            while ((line = reader.readLine()) != null) {
                WalkList.add(line);
                //
            }
        } catch (IOException e) {
            Log.d("myTag", "caught exception");
        }
        //Remove headers
        WalkList.remove(0);
        String[] matchingWalks;
        // Check if its its within the bounds and correct walk type
        for (int i = 0; i < WalkList.size(); i++) {
            List<String> innerList = new ArrayList<>();
            String[] walkDetails = WalkList.get(i).split(",", 7);
            String[] startLongLat = walkDetails[1].split(":");
            //Log.d("myTag", "Long " + startLongLat[0]);
            //Log.d("myTag", "lat " + startLongLat[1]);
            //Log.d("myTag", Double.parseDouble(startLongLat[0]) + " < " + locationMinMax[0]);
            if ((Double.parseDouble(startLongLat[0]) < locationMinMax[0] || Double.parseDouble(startLongLat[0]) > locationMinMax[1])) {
                Log.d("myTag", "failed ");
                continue;
            }
            //check if its outside the long smaller than min or bigger than max
            if (Double.parseDouble(startLongLat[1]) < locationMinMax[2] || Double.parseDouble(startLongLat[1]) > locationMinMax[3]) {
                Log.d("myTag", "failed ");
                continue;
            }
            for (int k = 0; k < walkDetails.length; k++) {
                innerList.add(walkDetails[k]);
            }
            listOfLists.add(innerList);
        }
        for (int a = 0; a < listOfLists.size(); a++) {
            Log.d("myTag", "Found " + listOfLists.get(a));
        }
        return listOfLists;
    }

    public double[] calculateRadiusRange(double latitude, double longitude, double radiusInMeters) {
        // Convert radius to degrees (approximate values)
        double radiusInDegrees = radiusInMeters / 111000.0;
        // Calculate latitude range
        double minLatitude = latitude - radiusInDegrees;
        double maxLatitude = latitude + radiusInDegrees;

        // Calculate longitude range
        double minLongitude = longitude - radiusInDegrees / Math.cos(Math.toRadians(latitude));
        double maxLongitude = longitude + radiusInDegrees / Math.cos(Math.toRadians(latitude));

        // Return the latitude and longitude range as an array
        return new double[]{minLatitude, maxLatitude, minLongitude, maxLongitude};
    }


}