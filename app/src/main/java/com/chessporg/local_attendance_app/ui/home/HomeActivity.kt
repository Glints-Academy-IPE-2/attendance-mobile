package com.chessporg.local_attendance_app.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.databinding.ActivityHomeBinding
import com.chessporg.local_attendance_app.ui.locationview.LocationViewActivity
import com.chessporg.local_attendance_app.utils.GPSBroadcastReceiver
import com.chessporg.local_attendance_app.utils.helper.GPSHelper

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dailyAttendanceAdapter: DailyAttendanceAdapter
    private lateinit var locationManager: LocationManager
    private var gpsBroadcastReceiver = GPSBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!gpsBroadcastReceiver.isOrderedBroadcast) {
            setGPSBroadcastReceiver()
            Log.d(TAG, "Register GPSBroadcastReceiver: start ...")
        }
        setLocationManager()
        setDailyAttendanceAdapter()
        setTodayAttendanceButton()
        setStatusBarColor()

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "First Alert")
            GPSHelper.showDisabledGPSAlert(this)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.blue_primary, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(com.chessporg.local_attendance_app.R.color.blue_primary)
        }
    }

    private fun setGPSBroadcastReceiver() {
        registerReceiver(gpsBroadcastReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

    private fun setLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun setDailyAttendanceAdapter() {
        dailyAttendanceAdapter = DailyAttendanceAdapter()
        binding.rvDailyAttendance.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = dailyAttendanceAdapter
        }
    }

    private fun setTodayAttendanceButton() {
        binding.containerTodayAttendence.setOnClickListener {
            startActivity(Intent(this, LocationViewActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gpsBroadcastReceiver)
    }
}