/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

import com.danny.xtool.net.NetTool;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * create by danny
 * 定位工具类
 */
public class LocationUtil {
    private static LocationListenerImpl listener;
    private static LocationManager locationManager;

    public LocationUtil() {
    }

    /**gps是否开启*/
    private static boolean isGpsOpen(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return false;
        }
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**定位是否开启*/
    private static boolean isLocationOpen(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return false;
        }
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void startLocationPermission(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, 0);
    }

    private static Criteria setCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
        criteria.setAltitudeRequired(false);// 海拔
        criteria.setBearingRequired(false);// 方位信息
        criteria.setSpeedRequired(false);// 速度
        criteria.setCostAllowed(true);// 向运营商付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
        return criteria;
    }

    /**获取定位信息*/
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**获取国家*/
    public String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unKnow" : address.getCountryName();
    }

    /**获取城市*/
    public String getCityName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unKnow" : address.getLocality();
    }

    /**获取街道信息*/
    public String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        if (address != null) {
            StringBuilder builder = new StringBuilder();
            int maxLine = address.getMaxAddressLineIndex();
            for (int i = 0; i < maxLine; i++) {
                builder.append(address.getAddressLine(i));
            }
            return builder.toString();
        }
        return null;
    }

    /**注册*/
    @SuppressLint("MissingPermission")
    public static boolean register(Context context, long minTime, float minDistance, LocationCallback callback) {
        if (callback == null) {
            return false;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (isLocationOpen(context)) {
            return false;
        }

        // 优先网络获取,避免室内GPS失效
        String provider = NetTool.INSTANCE.hasInternet(context) ? LocationManager.NETWORK_PROVIDER : LocationManager.GPS_PROVIDER;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            callback.getLastLocation(location);
        } else {
            if (listener == null) {
                listener = new LocationListenerImpl(callback);
            }
            locationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
        }
        return true;
    }

    /**解绑*/
    @SuppressLint("MissingPermission")
    public static boolean unregister() {
        if (locationManager != null) {
            if (listener != null) {
                locationManager.removeUpdates(listener);
                listener = null;
            }
            locationManager = null;
        }
        return true;
    }

    /**定位监听实现类*/
    private static class LocationListenerImpl implements LocationListener {
        LocationCallback callback;

        public LocationListenerImpl(LocationCallback callback) {
            this.callback = callback;
        }

        /**位置改变回调*/
        @Override
        public void onLocationChanged(Location location) {
            if (callback != null) {
                callback.locationChanged(getLocation());
            }
        }

        /**状态改变回调*/
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (callback != null) {
                callback.stateChanged();
            }
        }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }

        @SuppressLint("MissingPermission")
        private Location getLocation() {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            return location;
        }

    }

    public interface LocationCallback {
        // 获取上次位置
        void getLastLocation(Location location);
        // 位置改变回调
        void locationChanged(Location location);
        // 状态改变回调
        void stateChanged();
    }
}
