/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import java.io.IOException
import java.util.*

class LocationUtil2 {
    companion object {
        fun isLocationEnable(context: Context): Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            return locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
                || locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        }
    }

    fun getAddress(context: Context, latitude: Double, longitude: Double): Address? {
        val geo = Geocoder(context, Locale.getDefault())
        try {
            val list =
                geo.getFromLocation(latitude, longitude, 1)
            if (list != null && list.size > 0) {
                return list[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
