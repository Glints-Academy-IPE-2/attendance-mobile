package com.chessporg.local_attendance_app.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.data.model.AttendanceResponse
import com.chessporg.local_attendance_app.databinding.ActivityHomeBinding
import com.chessporg.local_attendance_app.ui.locationview.LocationViewActivity
import com.chessporg.local_attendance_app.utils.DummyData
import com.chessporg.local_attendance_app.utils.GPSBroadcastReceiver
import com.chessporg.local_attendance_app.utils.helper.DateHelper
import com.chessporg.local_attendance_app.utils.helper.GPSHelper
import java.util.*

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dailyAttendanceAdapter: DailyAttendanceAdapter
    private lateinit var locationManager: LocationManager
    private var gpsBroadcastReceiver = GPSBroadcastReceiver()
    private lateinit var viewModel: HomeViewModel

    private var month =
        DateHelper.convertThreeLetterMonthToFullMonthName(
            Date()
                .toString().subSequence(4, 7).toString()
        )

    private var userId = DummyData.userId


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        if (!gpsBroadcastReceiver.isOrderedBroadcast) {
            setGPSBroadcastReceiver()
            Log.d(TAG, "Register GPSBroadcastReceiver: start ...")
        }

        setLocationManager()
        setCurrentMonthProperty(month)
        setDailyAttendanceAdapter(month, userId)
        setTodayAttendanceButton(month, userId)
        setChangeMonthButton()
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

    private fun setDailyAttendanceAdapter(month: String, userId: Int) {
        showLoading(true)

        viewModel.getListAttendance(month, userId).observe(this, {
            if (it != null) {
                dailyAttendanceAdapter = DailyAttendanceAdapter()
                dailyAttendanceAdapter.setList(it)

                binding.rvDailyAttendance.apply {
                    layoutManager = LinearLayoutManager(this@HomeActivity)
                    setHasFixedSize(true)
                    adapter = dailyAttendanceAdapter
                }
            }

            showLoading(false)
        })
    }

    private fun setTodayAttendanceButton(month: String, userId: Int) {
        binding.apply {
            containerTodayAttendence.setOnClickListener {
                val intent = Intent(this@HomeActivity, LocationViewActivity::class.java)
                startActivity(intent)
            }

            viewModel
                .getTodayAttendance(month, userId)
                .observe(this@HomeActivity, {
                    val date = it.date.toString()
                    tvDateDayNumber.text = date.subSequence(8, 10)
                    tvDateDayText.text = date.subSequence(0, 3)
                    tvDateMonth.text = date.subSequence(4, 7)
            })
        }
    }

    private fun setCurrentMonthProperty(month: String) {
        binding.tvMonth.text = month
    }

    private fun setChangeMonthButton() {
        binding.apply {
            ibNextMonth.setOnClickListener {
                if (DateHelper.convertMonthToNumber(month) < 12) {
                    val nextMonthInNumber = DateHelper.convertMonthToNumber(month) + 1
                    val nextMonthInText = DateHelper.convertNumberToMonth(nextMonthInNumber)
                    month = nextMonthInText

                    setCurrentMonthProperty(month)
                    setDailyAttendanceAdapter(month, userId)
                }
            }

            ibPreviousMonth.setOnClickListener {
                if (DateHelper.convertMonthToNumber(month) > 1) {
                    val prevMonthInNumber = DateHelper.convertMonthToNumber(month) - 1
                    val prevMonthInText = DateHelper.convertNumberToMonth(prevMonthInNumber)
                    month = prevMonthInText

                    setCurrentMonthProperty(month)
                    setDailyAttendanceAdapter(month, userId)
                }
            }
        }
    }

    private fun showLoading(isDisplay: Boolean) {
        if (!isDisplay) {
            binding.cvLoading.visibility = View.GONE
        }
        else {
            binding.cvLoading.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gpsBroadcastReceiver)
    }
}