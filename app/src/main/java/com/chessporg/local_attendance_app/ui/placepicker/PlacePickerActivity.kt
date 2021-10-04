package com.chessporg.local_attendance_app.ui.placepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.chessporg.local_attendance_app.R
import com.chessporg.local_attendance_app.databinding.ActivityPlacePickerBinding
import com.chessporg.local_attendance_app.ui.home.HomeActivity
import com.chessporg.local_attendance_app.utils.helper.GeoSourceHelper.CIRCLE_CENTER_ICON_ID
import com.chessporg.local_attendance_app.utils.helper.GeoSourceHelper.CIRCLE_CENTER_LAYER_ID
import com.chessporg.local_attendance_app.utils.helper.GeoSourceHelper.CIRCLE_CENTER_SOURCE_ID
import com.chessporg.local_attendance_app.utils.helper.GeoSourceHelper.TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
import com.chessporg.local_attendance_app.utils.helper.GeoSourceHelper.TURF_CALCULATION_FILL_LAYER_ID
import com.chessporg.local_attendance_app.utils.helper.MapHelper.ANIMATE_CAMERA_DURATION
import com.chessporg.local_attendance_app.utils.helper.MapHelper.INDONESIA_ZOOM_VALUE
import com.chessporg.local_attendance_app.utils.helper.MapHelper.ZOOM_VALUE
import com.chessporg.local_attendance_app.utils.helper.MapHelper.firstSetLocation
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeta
import com.mapbox.turf.TurfTransformation
import java.util.*

class PlacePickerActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val PLACE_SELECTION_REQUEST_CODE = 56789
    }

    private lateinit var binding: ActivityPlacePickerBinding
    private var mapView: MapView? = null
    private lateinit var map: MapboxMap
    private var isCircleZoneDrawn = false
    private lateinit var loadedStyleMap: Style
    private var currentPickedLocationLatLng = LatLng(-0.23857894191359583, 119.64293910793991)
    private var currentPickedLocationPoint = Point.fromLngLat(
        currentPickedLocationLatLng.longitude,
        currentPickedLocationLatLng.latitude
    )
    private var currentPickedLocationName = "None"

    private var circleUnit = TurfConstants.UNIT_METERS
    private var circleSteps = 180
    private var circleRadius = 100.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityPlacePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()
        setMapView(savedInstanceState)
        setChangeLocationButton()
        setSubmitLocationButton()
        showSubmitButton(false)
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
                                .target(if (isCircleZoneDrawn) currentPickedLocationLatLng else firstSetLocation)
                                .zoom(if (isCircleZoneDrawn) ZOOM_VALUE else INDONESIA_ZOOM_VALUE)
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

            val sharedPref = getSharedPreferences(getString(R.string.user_data), Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putFloat(getString(R.string.user_work_place_latitude),
                    currentPickedLocationLatLng.latitude.toFloat()
                )
                putFloat(getString(R.string.user_work_place_longitude),
                    currentPickedLocationLatLng.longitude.toFloat()
                )
                putString(getString(R.string.user_work_place_name),
                    currentPickedLocationName
                )
                putString(getString(R.string.user_today_attendance),
                    Date().toString().substring(4, 10)
                )
                apply()
            }

            isCircleZoneDrawn = true
            mapView?.getMapAsync(this)
            showSubmitButton(true)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map
            .setStyle(
                Style.Builder().fromUri(Style.MAPBOX_STREETS)
                    /*
                    .withImage(
                        CIRCLE_CENTER_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                            resources.getDrawable(R.drawable.mapbox_marker_icon_default, this.theme)
                        )!!
                    )
                     */
                    .withSource(
                        GeoJsonSource(
                            CIRCLE_CENTER_SOURCE_ID,
                            Feature.fromGeometry(currentPickedLocationPoint)
                        )
                    )
                    .withSource(GeoJsonSource(TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID))
                    .withLayer(
                        SymbolLayer(CIRCLE_CENTER_LAYER_ID, CIRCLE_CENTER_SOURCE_ID)
                            .withProperties(
                                iconImage(CIRCLE_CENTER_ICON_ID),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconOffset(
                                    arrayOf(0f, -4f)
                                )
                            )
                    )
            ) {
                loadedStyleMap = it
                // Map UI settings
                run {
                    val uiSettings = mapboxMap.uiSettings
                    uiSettings.setAllGesturesEnabled(false)
                }

                // Animate Camera
                run {
                    val position = CameraPosition.Builder()
                        .target(currentPickedLocationLatLng)
                        .zoom(if (isCircleZoneDrawn) ZOOM_VALUE else INDONESIA_ZOOM_VALUE)
                        .build()
                    map.animateCamera(
                        CameraUpdateFactory.newCameraPosition(position),
                        ANIMATE_CAMERA_DURATION
                    )
                    setLocationInformation()
                }

                initPolygonCircleFillLayer()
                currentPickedLocationPoint = Point.fromLngLat(
                    currentPickedLocationLatLng.longitude,
                    currentPickedLocationLatLng.latitude
                )
                drawPolygonCircle(currentPickedLocationPoint)
            }
    }

    private fun drawPolygonCircle(currentPickedLocationPoint: Point) {
        map.getStyle {
            val polygonArea =
                getTurfPolygon(currentPickedLocationPoint, circleRadius, circleSteps, circleUnit)
            val polygonCircleSource = it.getSourceAs<GeoJsonSource>(
                TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
            )
            polygonCircleSource?.setGeoJson(
                Polygon.fromOuterInner(
                    LineString.fromLngLats(TurfMeta.coordAll(polygonArea, false))
                )
            )
        }
    }

    private fun setLocationInformation() {
        binding.apply {
            tvCoordniate.text = getString(
                R.string.latitude_s_longitude_s,
                if (isCircleZoneDrawn) currentPickedLocationLatLng.latitude.toString() else 0.0,
                if (isCircleZoneDrawn) currentPickedLocationLatLng.longitude.toString() else 0.0
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

    private fun getTurfPolygon(
        currentPickedLocationPoint: Point,
        circleRadius: Double,
        circleSteps: Int,
        circleUnit: String
    ): Polygon {
        return TurfTransformation.circle(
            currentPickedLocationPoint,
            circleRadius,
            circleSteps,
            circleUnit
        )
    }

    private fun initPolygonCircleFillLayer() {
        map.getStyle {
            val fillLayer = FillLayer(
                TURF_CALCULATION_FILL_LAYER_ID,
                TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
            )
            fillLayer.setProperties(
                fillColor(Color.parseColor("#FF6C63FF")),
                fillOutlineColor(Color.parseColor("#FF6C63FF")),
                fillOpacity(0.3f)
            )
            it.addLayerBelow(fillLayer, CIRCLE_CENTER_LAYER_ID)
        }
    }

    private fun showSubmitButton(bool: Boolean) {
        binding.apply {
            if (bool) {
                val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240f, resources.displayMetrics)
                vBottomContainer.updateLayoutParams {
                    height = pixels.toInt()
                }
                cvSubmitLocationButton.visibility = View.VISIBLE
            }

            else {
                val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180f, resources.displayMetrics)
                vBottomContainer.updateLayoutParams {
                    height = pixels.toInt()
                }
                cvSubmitLocationButton.visibility = View.GONE
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
        mapView?.onDestroy()
    }
}