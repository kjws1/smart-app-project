package com.example.a11_pa

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.a11_pa.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var fLC: FusedLocationProviderClient? = null
    private var callback: LocationCallback? = null
    private var current: LatLng? = null
    var startM: Marker? = null
    var currentM: Marker? = null
    var plOptions: PolylineOptions? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var permissionArray = arrayOf<String>()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            permissionArray =
                permissionArray.plus(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            permissionArray = permissionArray.plus(android.Manifest.permission.ACCESS_FINE_LOCATION)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it.all { permission -> permission.value }) {
                    doLocation()
                } else
                    Toast.makeText(applicationContext, "권한거부", Toast.LENGTH_LONG).show()
            }
        if (permissionArray.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionArray)
        } else
            doLocation()


    }

    private fun doLocation() {
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        fLC = LocationServices.getFusedLocationProviderClient(this)
        callback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                val list = p0.locations
                val location = list[0]
                val latLng = LatLng(location.latitude, location.longitude)

                if (current == null || current != latLng) {
                    val position = CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15f)
                        .build()
                    googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))
                    current = latLng
                }


                if (currentM != startM) {
                    currentM?.remove()
                }

                // When start marker is assigned for the first time
                val options = MarkerOptions().run {
//                    title("현재위치")
                    position(latLng)
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                }

                if (startM == null) {
                    options.title("시작위치")
                    currentM = googleMap?.addMarker(options)
                    startM = currentM
                    startM?.showInfoWindow()
                    plOptions =
                        PolylineOptions().add(latLng).color(Color.BLUE).visible(true).width(5f)
                } else {
                    options.title("현재위치")
                    plOptions?.add(latLng)
                    currentM = googleMap?.addMarker(options)
                    currentM?.showInfoWindow()
                    googleMap?.addPolyline(plOptions!!)
                }
//                val marker = googleMap.addMarker(markerOptions)
//                marker?.showInfoWindow()
            }

        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        fLC?.requestLocationUpdates(locationRequest, callback!!, mainLooper)

        /*
                val latLng = LatLng(37.566610, 126.978403)
                val position = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(16f)
                    .build()
                googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

                val markerOptions = MarkerOptions().run {
                    position(latLng)
                    title("서울시청")
                    snippet("Tel:01-120")
                }
                googleMap?.addMarker(markerOptions)
        */


    }

    override fun onDestroy() {
        super.onDestroy()
        if (fLC != null)
            fLC?.removeLocationUpdates(callback!!)
    }
}