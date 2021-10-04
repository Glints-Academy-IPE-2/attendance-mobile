package com.chessporg.local_attendance_app.api

import com.chessporg.local_attendance_app.data.model.AttendanceListResponse
import com.chessporg.local_attendance_app.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface Api {
    // Get specific user
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>

    // Get all attendances with filter by month and userId
    @GET("attendances")
    fun getAttendances(
        @QueryMap(encoded = false) options: Map<String, String>,
    ): Call<AttendanceListResponse>

    // Check In
    @POST("...")
    fun checkInAttendance(
        @Body date: Date,
    )

    // Check Out
    @POST("...")
    fun checkOutAttendance(
        @Body date: Date,
    )
}