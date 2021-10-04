package com.chessporg.local_attendance_app.ui.blank

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.ui.home.HomeActivity
import com.chessporg.local_attendance_app.ui.placepicker.PlacePickerActivity

class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_blank)

        val sharedPref = getSharedPreferences(getString(R.string.user_data), Context.MODE_PRIVATE)

        val currentWorkingPlaceLatitude =
            sharedPref.getFloat(getString(R.string.user_work_place_latitude), 0.0F)
        val currentWorkingPlaceLongitude =
            sharedPref.getFloat(getString(R.string.user_work_place_longitude), 0.0F)

        if (currentWorkingPlaceLatitude == 0.0F && currentWorkingPlaceLongitude == 0.0F) {
            startActivity(Intent(this, PlacePickerActivity::class.java))
        }
        else {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        finish()
    }
}