package com.chessporg.local_attendance_app.utils.helper

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog

object GPSHelper {

    private const val TAG = "GPSHelper"

    var isGPSAlertDisplayed = false

    fun showDisabledGPSAlert(context: Activity) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.apply {
            setTitle("GPS Setting!")
            setMessage("GPS is not enabled, Do you want to go to settings menu ?")
            setPositiveButton("Setting", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
            setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    context.finish()
                }
            })
            setOnCancelListener {
                context.finish()
            }
            setOnDismissListener {
                isGPSAlertDisplayed = false
            }
            show()
        }
    }
}