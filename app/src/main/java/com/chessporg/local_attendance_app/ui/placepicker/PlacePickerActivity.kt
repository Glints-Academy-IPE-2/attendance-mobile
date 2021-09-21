package com.chessporg.local_attendance_app.ui.placepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.databinding.ActivityPlacePickerBinding
import com.chessporg.local_attendance_app.ui.home.HomeActivity
import com.chessporg.local_attendance_app.utils.helper.MapHelper
import com.chessporg.local_attendance_app.utils.helper.MapHelper.ANIMATE_CAMERA_DURATION
import com.chessporg.local_attendance_app.utils.helper.MapHelper.INDONESIA_ZOOM_VALUE
import com.chessporg.local_attendance_app.utils.helper.MapHelper.ZOOM_VALUE
import com.chessporg.local_attendance_app.utils.helper.MapHelper.firstSetLocation
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions

class PlacePickerActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val PLACE_SELECTION_REQUEST_CODE = 56789
    }

    private lateinit var binding: ActivityPlacePickerBinding

    private var mapView: MapView? = null
    private lateinit var map: MapboxMap
    private var marker: Marker? = null
    private var currentPickedLocationLatLng = LatLng(-0.23857894191359583, 119.64293910793991)
    private var currentPickedLocationName = "None"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityPlacePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()
        setMapView(savedInstanceState)
        setChangeLocationButton()
        setSubmitLocationButton()
    }

    private fun setSubmitLocationButton() {
        binding.apply {
            cvSubmitLocationButton.setOnClickListener {
                startActivity(Intent(this@PlacePickerActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setMapView(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    private fun setChangeLocationButton() {
        binding.cvChangeMapButton.setOnClickListener {
            val intent = PlacePicker.IntentBuilder()
                .accessToken(Mapbox.getAccessToken()!!)
                .placeOptions(
                    PlacePickerOptions.builder()
                        .statingCameraPosition(
                            CameraPosition.Builder()
                                .target(if(marker!=null) currentPickedLocationLatLng else firstSetLocation)
                                .zoom(if(marker!=null) ZOOM_VALUE else INDONESIA_ZOOM_VALUE)
                                .build()
                        )
                        .build()
                )
                .build(this)
            startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val carmenFeature = PlacePicker.getPlace(data)
            val lastCameraPosition = PlacePicker.getLastCameraPosition(data)
            currentPickedLocationName = carmenFeature?.placeName().toString()
            currentPickedLocationLatLng = lastCameraPosition.target

            MapHelper.apply {
                currentWorkingCoordinate = currentPickedLocationLatLng
                currentWorkingPlaceName = currentPickedLocationName
            }

            // Add Marker
            run {
                if (marker != null) {
                    map.removeMarker(marker!!)
                }
                marker = map.addMarker(
                    MarkerOptions()
                        .position(MapHelper.currentWorkingCoordinate)
                        .title("Working Location")
                )
            }
            mapView?.getMapAsync(this)
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setStyle(Style.MAPBOX_STREETS) {
            // Map UI settings
            run {
                val uiSettings = mapboxMap.uiSettings
                uiSettings.setAllGesturesEnabled(false)
            }

            // Animate Camera
            run {
                val position = CameraPosition.Builder()
                    .target(currentPickedLocationLatLng)
                    .zoom((if(marker!= null) ZOOM_VALUE else INDONESIA_ZOOM_VALUE) as Double)
                    .build()
                map.animateCamera(CameraUpdateFactory.newCameraPosition(position), ANIMATE_CAMERA_DURATION)
                setLocationInformation()
            }
        }
    }

    private fun setLocationInformation() {
        binding.apply {
            tvCoordniate.text = getString(
                R.string.latitude_s_longitude_s,
                if(marker != null) currentPickedLocationLatLng.latitude.toString() else 0.0,
                if(marker != null) currentPickedLocationLatLng.longitude.toString() else 0.0
            )
            tvLocationName.text = currentPickedLocationName
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
        mapView?.onDestroy()
    }
}