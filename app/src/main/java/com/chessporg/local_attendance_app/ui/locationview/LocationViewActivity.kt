package com.chessporg.local_attendance_app.ui.locationview

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.databinding.ActivityLocationViewBinding
import com.chessporg.local_attendance_app.utils.helper.MapHelper
import com.chessporg.local_attendance_app.utils.helper.MapHelper.DEFAULT_INTERVAL_IN_MILLISECONDS
import com.chessporg.local_attendance_app.utils.helper.MapHelper.DEFAULT_MAX_WAIT_TIME
import com.chessporg.local_attendance_app.utils.helper.MapHelper.currentWorkingCoordinate
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.utils.ColorUtils

class LocationViewActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {

    private lateinit var binding: ActivityLocationViewBinding
    private lateinit var permissionsManager: PermissionsManager

    private var mapView: MapView? = null
    private lateinit var map: MapboxMap
    private lateinit var callback: LocationListeningCallback
    private lateinit var locationEngine: LocationEngine
    private lateinit var loadedStyleMap: Style
    private var circleManager: CircleManager? = null
    private var currentUserLatitude = 0.0
    private var currentUserLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityLocationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackButton()
        setMapView(savedInstanceState)
        setWorkingLocationInformation()
        setStatusBarColor()
    }

    private inner class LocationListeningCallback(activity: LocationViewActivity) : LocationEngineCallback<LocationEngineResult> {

        override fun onSuccess(result: LocationEngineResult?) {
            result?.lastLocation ?: return
            if (result.lastLocation != null) {
                currentUserLatitude = result.lastLocation?.latitude!!
                currentUserLongitude = result.lastLocation?.longitude!!
                val userLatLng = LatLng(currentUserLatitude, currentUserLongitude)

                map.locationComponent.forceLocationUpdate(result.lastLocation)
                val latLngBounds = LatLngBounds.Builder()
                    .include(userLatLng)
                    .include(currentWorkingCoordinate)
                    .build()

                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200), 1000)

                Log.d("Check USER LatLng", userLatLng.toString())
            }
        }

        override fun onFailure(exception: Exception) {
            Toast.makeText(this@LocationViewActivity, "Location Update Failed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.blue_primary, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor =
                resources.getColor(R.color.blue_primary)
        }
    }

    private fun setBackButton() {
        binding.cvBackButton.setOnClickListener {
            finish()
        }
    }

    private fun setMapView(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        callback = LocationListeningCallback(this)
        mapView?.getMapAsync(this)
    }

    private fun setWorkingLocationInformation() {
        binding.apply {
            MapHelper.apply {
                tvCoordniate.text = getString(
                    R.string.latitude_s_longitude_s,
                    currentWorkingCoordinate.latitude.toString(),
                    currentWorkingCoordinate.longitude.toString()
                )
                tvLocationName.text = currentWorkingPlaceName
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponent = map.locationComponent

            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .accuracyAlpha(1f)
                .accuracyColor(Color.TRANSPARENT)
                .backgroundDrawable(R.drawable.bg_user_location)
                .foregroundDrawable(R.drawable.ic_round_emoji_people_24)
                .build()

            locationComponent.apply {
                activateLocationComponent(
                    LocationComponentActivationOptions.builder(
                        this@LocationViewActivity,
                        loadedMapStyle)
                        .locationComponentOptions(customLocationComponentOptions)
                        .build()
                )
                isLocationComponentEnabled = true
                cameraMode = CameraMode.NONE
                renderMode = RenderMode.NORMAL
            }

            initLocationEngine()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 1000)
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        locationEngine.requestLocationUpdates(request, callback, mainLooper)
        locationEngine.getLastLocation(callback)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {

    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            map.getStyle {
                enableLocationComponent(it)
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 1000)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setStyle(Style.MAPBOX_STREETS) {
            loadedStyleMap = it
            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            binding.spinKit.visibility = View.GONE

            // Map UI settings
            run {
                val uiSettings = map.uiSettings
                uiSettings.setAllGesturesEnabled(false)
            }

            // Show Device
            run {
                enableLocationComponent(it)
            }

            // Add Circle Area to working location
            run {
                if (circleManager == null) {
                    circleManager = CircleManager(mapView!!, map, loadedStyleMap)
                }

                val circleOptions = CircleOptions()
                    .withCircleRadius(100f)
                    .withCircleColor(ColorUtils.colorToRgbaString(Color.BLUE))
                    .withCircleOpacity(0.2f)
                    .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.BLUE))
                    .withCircleStrokeWidth(2f)
                    .withCircleStrokeOpacity(0.6f)
                    .withLatLng(currentWorkingCoordinate)

                circleManager?.deleteAll()
                circleManager?.create(circleOptions)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationEngine.removeLocationUpdates(callback)
        mapView?.onDestroy()
    }
}