package com.chessporg.local_attendance_app.utils.helper

import com.mapbox.mapboxsdk.geometry.LatLng

object MapHelper {
    const val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    const val ZOOM_VALUE = 15.0
    const val INDONESIA_ZOOM_VALUE = 2.5
    const val ANIMATE_CAMERA_DURATION = 2500

    var firstSetLocation = LatLng(-0.23857894191359583, 119.64293910793991)
}