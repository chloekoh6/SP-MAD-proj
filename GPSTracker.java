package com.sp.android_studio_project;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class GPSTracker extends Service implements LocationListener{
    private Context mContext = null;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    //The min distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    //The min time between updates in ms
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;
    //Declaring a location manager
    protected LocationManager locationManager;

    public GPSTracker() { checkGPSPermissions(); }

    public GPSTracker(Context context) {
        this.mContext = context;
        checkGPSPermissions();
    }

    public Location getLocation() {
        this.canGetLocation = false;
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){
                //no network, prompt user to enable
                showEnableLocationAlert();
            }else{
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    //permission granted
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null){
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch (SecurityException e) {
            e.printStackTrace();
        }
        return location;
    }

    //Function to get latitude
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }


    //Function to get longitude
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /* stop using GPS listener
    calling this function will stop using GPS in app
     */

    public void stopUsingGPS() {
        if (locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /*
    function to check GPS/wifi
    return Boolean
     */
    public boolean canGetLocation() {
        checkGPSPermissions();
        return canGetLocation;
    }

    //check for location permission
    public void checkGPSPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionState1 == PackageManager.PERMISSION_GRANTED &&
                permissionState2 == PackageManager.PERMISSION_GRANTED){
            //permission granted, get gps location
            getLocation();
        }
        else{
            //prompt user to enable location permission
            showEnablePermissionAlert();
        }
    }

    //show settings alert dialog
    public void showEnablePermissionAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        //title
        alertDialog.setTitle("Location Permission Settings");
        //message
        alertDialog.setMessage("Restaurant List Location Permission is not enabled. " +
                "Do you want to go to settings menu?");
        //button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               Intent intent = new Intent();
               //go application setting
               intent.setAction(
                       Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
               Uri uri =Uri.fromParts("package",
                       BuildConfig.APPLICATION_ID, null);
               intent.setData(uri);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               mContext.startActivity(intent);
           }

        });

        //on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }

        });

        alertDialog.show();
    }

    public void showEnableLocationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        //setting title
        alertDialogBuilder.setTitle("Location Service Setting");
        alertDialogBuilder.setMessage("Location service is disabled in your device." +
                "Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable Location Service",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                //Go to location service setting
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                mContext.startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location){ getLocation();}

    @Override
    public void onProviderDisabled(String provider){
    }

    @Override
    public void onProviderEnabled(String provider){
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}