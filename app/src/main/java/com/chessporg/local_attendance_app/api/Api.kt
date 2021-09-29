package com.chessporg.local_attendance_app.api

import com.chessporg.local_attendance_app.data.model.AttendanceListResponse
import com.chessporg.local_attendance_app.data.model.User
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ) : Call<User>

    @GET("attendances")
    fun getAttendances(
        @QueryMap(encoded = false) options: Map<String, String>,
    ) : Call<AttendanceListResponse>


    @POST("attendances?month={month}&userId={userId}")
    fun checkOutTodayAttendence(
        @Query("month") month: String,
        @Query("userId") userId: Int,
    )
}