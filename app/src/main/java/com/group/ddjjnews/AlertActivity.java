package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.databinding.ActivityAlertBinding;
import com.group.ddjjnews.fragments.ListAlertFragment;

import com.group.ddjjnews.models.User;

import java.util.List;
import java.util.Objects;

public class AlertActivity extends AppCompatActivity {
    public static final int FINE_LOCATION_REQUEST_CODE = 1001;
    ActivityAlertBinding binding;
    ListAlertFragment list, myList;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AlertActivity.this, R.layout.activity_alert);
        setSupportActionBar(binding.alertToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setupPermissions();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        list = new ListAlertFragment();
        viewPagerAdapter.add(list, "All");
        if (User.getCurrentUser() != null) {
            myList = new ListAlertFragment();
            viewPagerAdapter.add(myList, "My request");
        }
        binding.alertPager.setAdapter(viewPagerAdapter); // Set adapter
        binding.alertTab.setupWithViewPager(binding.alertPager);

        if (User.getCurrentUser() == null)
            binding.floatingAction.setVisibility(FloatingActionButton.GONE);
        binding.floatingAction.setOnClickListener(view -> showCreation());

    }

    private void showCreation() {
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

                if (showRationale) {
                    // do something here to handle degraded mode
                } else {
                    Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupPermissions() {
        boolean hasNotPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (!hasNotPerm) {

            if (!LocationManagerCompat.isLocationEnabled(locationManager)){
                return;
            }
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            List<String> providers = locationManager.getProviders(criteria, true);
            Toast.makeText(AlertActivity.this, providers.toString(), Toast.LENGTH_SHORT).show();

            String bestProvider = locationManager.getBestProvider(criteria, true);

            Location l = locationManager.getLastKnownLocation(bestProvider);
            if (l == null)
                Toast.makeText(AlertActivity.this, "Null", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(AlertActivity.this, "Lat: "+l.getLatitude() + " - Long: "+l.getLongitude(), Toast.LENGTH_SHORT).show();


            locationManager.requestLocationUpdates(bestProvider, 6000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Toast.makeText(AlertActivity.this, "" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    Log.d("Alert", "Enabled");
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    Log.d("Alert", "Disabled");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            });

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access location of alert post is required for this app.");
                builder.setTitle("Required permission.");

                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    makeRequestPerms();
                });

                builder.create().show();
            } else
                makeRequestPerms();
        }
    }

    private void makeRequestPerms() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
    }
}