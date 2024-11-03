package ir.caspiansoftware.caspianandroidapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.ErrorExt;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PermissionBLL;

public class GPSTracker implements LocationListener { //Service
    private static final String TAG = "GPSTracker";

    private final Activity mActivity;

    public static final int REQUEST_GPS_ACCESS = 56574;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 10 seconds

    private static final int RETRY = 5; // 10 seconds

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Activity activity) {
        this.mActivity = activity;
        getLocation();
    }

    public static void requestForGps(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        int checkSelfPermissionFINE = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkSelfPermissionCOARSE = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (checkSelfPermissionFINE == PackageManager.PERMISSION_DENIED || checkSelfPermissionCOARSE == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, REQUEST_GPS_ACCESS
            );
        }
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            boolean forced = PermissionBLL.forceLocationSaving();

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                try {
                    int retry = 1;
                    do {

                        if (retry > 1)
                            Thread.sleep(1000);

                        this.canGetLocation = true;
                        // if GPS Enabled get lat/long using GPS Services
                        if (isGPSEnabled) {
                            requestLocation(LocationManager.GPS_PROVIDER);

                        }

                        if (isNetworkEnabled && location == null) {
                            requestLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        Log.d(TAG, "lat: " + latitude + ", lon: " + longitude);

                        if (location == null) {
                            latitude = 0;
                            longitude = 0;
                        }
                    } while (location == null && retry++ <= RETRY && forced);

                } catch (SecurityException ex) {
                    Toast.makeText(mActivity, "security_exception", Toast.LENGTH_LONG).show();
                    ErrorExt.get().extractExceptionDetail(ex);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void requestLocation(String networkProvider) {

        int checkSelfPermissionFINE = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkSelfPermissionCOARSE = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (locationManager != null && checkSelfPermissionFINE == PackageManager.PERMISSION_GRANTED && checkSelfPermissionCOARSE == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    networkProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this
            );
            Log.d(TAG, networkProvider);

            location = locationManager.getLastKnownLocation(networkProvider);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(GPSTracker.this);
            } catch (SecurityException ex) {
                Toast.makeText(mActivity, "security_exception", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged(): lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged() ");
    }

    // @Override
    //  public IBinder onBind(Intent arg0) {
    //     return null;
    //}

}