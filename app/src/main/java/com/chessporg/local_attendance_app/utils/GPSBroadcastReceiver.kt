package com.chessporg.local_attendance_app.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Debug
import android.util.Log
import android.widget.Toast
import com.chessporg.local_attendance_app.utils.helper.GPSHelper
import java.lang.Exception

class GPSBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "GPSBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !GPSHelper.isGPSAlertDisplayed) {
                GPSHelper.isGPSAlertDisplayed = true
                GPSHelper.showDisabledGPSAlert(context as Activity)
            }
        } catch (e: Exception) {

        }
    }
}