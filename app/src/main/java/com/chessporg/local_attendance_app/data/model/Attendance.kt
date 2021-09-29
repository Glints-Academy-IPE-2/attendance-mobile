package com.chessporg.local_attendance_app.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Attendance(
    val userId: Int,
    val date: Date,
    val checkInTime: Date,
    val checkOutTime: Date
)
