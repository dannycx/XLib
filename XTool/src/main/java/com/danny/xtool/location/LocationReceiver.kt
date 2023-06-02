/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.location

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

/**
 * 位置注册
 */
class LocationReceiver {
    private lateinit var locationListener: XLocationListener
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.N)
    fun register(cnt: Context, minTime: Long, minDistance: Long, callback: LocationCallback?): Boolean =
        when {
            null == callback -> false
            (ActivityCompat.checkSelfPermission(cnt, Manifest.permission.ACCESS_FINE_LOCATION) != 0 &&
            ActivityCompat.checkSelfPermission(cnt, Manifest.permission.ACCESS_COARSE_LOCATION) != 0) -> false
            !LocationUtil.isLocationEnable(cnt) -> false
            else -> {
                locationManager = cnt.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                locationManager.addNmeaListener(locationListener)
                true
            }
        }

    fun unRegister() {
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }

    inner class XLocationListener: LocationListener {
        override fun onLocationChanged(location: Location) {
            TODO("Not yet implemented")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("Not yet implemented")
        }

        fun getBestLocation() {
        }
    }
}
