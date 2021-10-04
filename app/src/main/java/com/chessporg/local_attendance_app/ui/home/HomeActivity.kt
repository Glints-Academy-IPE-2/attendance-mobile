package com.chessporg.local_attendance_app.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.databinding.ActivityHomeBinding
import com.chessporg.local_attendance_app.ui.locationview.LocationViewActivity
import com.chessporg.local_attendance_app.utils.DummyData
import com.chessporg.local_attendance_app.utils.GPSBroadcastReceiver
import com.chessporg.local_attendance_app.utils.helper.DateHelper
import com.chessporg.local_attendance_app.utils.helper.GPSHelper
import timber.log.Timber
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
    private lateinit var sharedPref: SharedPreferences

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

        sharedPref = getSharedPreferences(getString(R.string.user_data), Context.MODE_PRIVATE)

        if (!gpsBroadcastReceiver.isOrderedBroadcast) {
            setGPSBroadcastReceiver()
            Timber.tag(TAG).d("Register GPSBroadcastReceiver: start ...")
        }

        setLocationManager()
        setCurrentMonthProperty(month)
        setDailyAttendanceAdapter(month, userId)
        setTodayAttendanceButton(month, userId)
        setChangeMonthButton()
        setStatusBarColor()

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Timber.tag(TAG).d("First Alert")
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

            val date = Date().toString()
            tvDateDayNumber.text = date.subSequence(8, 10)
            tvDateDayText.text = date.subSequence(0, 3)
            tvDateMonth.text = date.subSequence(4, 7)
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

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        sharedPref = getSharedPreferences(getString(R.string.user_data), Context.MODE_PRIVATE)

        val todayAttendance = sharedPref.getString(getString(R.string.user_today_attendance), "")

        if (todayAttendance != Date().toString().substring(4, 10)) {
            with(sharedPref.edit()) {
                putString(getString(R.string.user_today_attendance), Date().toString().substring(4, 10))
                putString(getString(R.string.user_today_checkin), "")
                putString(getString(R.string.user_today_checkout), "")
                apply()
            }
        }

        val startWorking = sharedPref.getString(getString(R.string.user_today_checkin), "")
        val stopWorking = sharedPref.getString(getString(R.string.user_today_checkout), "")

        when {
            startWorking == "" -> {
                binding.apply {
                    tvTitleAttendance.text = getString(R.string.start_working_to_check_in)
                    tvAttendance.text = getString(R.string.start)

                    Glide
                        .with(this@HomeActivity)
                        .load(R.drawable.ic_round_login_24)
                        .into(ivAttendance)

                }
            }
            stopWorking == "" -> {
                binding.apply {
                    tvTitleAttendance.text = getString(R.string.finish_working_to_check_out)
                    tvAttendance.text = getString(R.string.finish)

                    Glide
                        .with(this@HomeActivity)
                        .load(R.drawable.ic_round_logout_24)
                        .into(ivAttendance)
                }
            }
            else -> {
                binding.apply {
                    tvTitleAttendance.text = getString(R.string.good_job_for_today)
                    tvAttendance.text = getString(R.string.done)

                    Glide
                        .with(this@HomeActivity)
                        .load(R.drawable.ic_round_check_24)
                        .into(ivAttendance)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gpsBroadcastReceiver)
    }
}