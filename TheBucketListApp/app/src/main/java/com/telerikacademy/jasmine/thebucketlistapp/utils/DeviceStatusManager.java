package com.telerikacademy.jasmine.thebucketlistapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/**
 * Created by Boyko on 14.10.2014 г..
 */
public class DeviceStatusManager {
    private Context mcontext;
    private Boolean sdCardAvailable, sdCardWriteable;

    public DeviceStatusManager(Context context){
        this.mcontext = context;
    }

    public Boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public Boolean isGPSEnabled(){
        LocationManager lm;
        boolean GPS_Status = false;
        boolean gps_enabled = false;

        lm = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Boolean isAccelerometerAvailable(){
        SensorManager sm = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
        return sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null;
    }

    public Boolean isGravitySensorAvailable(){
        SensorManager sm = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
        return sm.getDefaultSensor(Sensor.TYPE_GRAVITY) != null;
    }

    public Boolean isFrontCameraAvailable() {

        int cameraCount = 0;
        boolean isFrontCameraAvailable = false;
        cameraCount = Camera.getNumberOfCameras();

        while (cameraCount > 0) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount--;
            Camera.getCameraInfo(cameraCount, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                isFrontCameraAvailable = true;
                break;
            }
        }

        return isFrontCameraAvailable;
    }

    public boolean isSDCardAvailableAndWriteable() {
        if (isSDCardAvailable() && isSDCardWriteable()) return true;
        else return false;
    }

    public Boolean isSDCardAvailable(){
        checkStorageStatus();
        return sdCardAvailable;
    }

    public boolean isSDCardWriteable() {
        checkStorageStatus();
        return sdCardWriteable;
    }

    public long GetAvailableRAM(){
        //return available RAM in MB
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        return (mi.availMem / 1048576L);
    }

    private void checkStorageStatus()
    {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) sdCardAvailable = sdCardWriteable = true;
        else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdCardAvailable = true;
            sdCardWriteable = false;
        }
        else sdCardAvailable = sdCardWriteable = false;
    }
}
