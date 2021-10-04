package com.chessporg.local_attendance_app.data.model

import java.util.*

data class UserResponse(
    val name: String,
    val username: String,
    val email: String,
    val avatar: String,
    val isAdmin: Boolean = false,
    val isApproved: Boolean = false,
    val isVerified: Boolean = false,
    val location: LocationCoordinate,
    val updatedAt: Date,
    val updatedBy: String
)
