package com.chessporg.local_attendance_app.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class AttendanceResponse(
    val userId: Int,
    val date: Date,
    @SerializedName("attendance")
    val time: AttendanceTime
)
