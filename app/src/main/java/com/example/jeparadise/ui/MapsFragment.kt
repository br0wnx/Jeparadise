package com.example.jeparadise.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.jeparadise.R
import com.example.jeparadise.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.GeoPoint

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var destinationLocation: LatLng? = null
    private lateinit var googleMap: GoogleMap

    private val callback = OnMapReadyCallback { map ->
        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the missing permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_LOCATION
            )
            return@OnMapReadyCallback
        }

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLocation = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLocation).title("Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
            }
        }

        destinationLocation?.let { geo ->
            // Draw route
            val url = getDirectionUrl(googleMap.cameraPosition.target, geo)
            getDirections(url) { directions ->
                // Parse directions JSON
                val points = parseDirectionPoints(directions)

                // Draw polyline on map
                googleMap.addPolyline(PolylineOptions().addAll(points))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragmentContainer) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.buttonDirection.setOnClickListener {
            val destination = destinationLocation ?: return@setOnClickListener
            val gmmIntentUri = Uri.parse("google.navigation:q=${destination.latitude},${destination.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            }
        }
    }

    fun setDestinationLocation(geo: LatLng?) {
        destinationLocation = geo

        if (::googleMap.isInitialized) {
            // Panggil method getDirections untuk menggambar rute dari lokasi saat ini ke destinasi baru
            val url = getDirectionUrl(googleMap.cameraPosition.target, destinationLocation!!)
            getDirections(url) { directions ->
                // Parse directions JSON
                val points = parseDirectionPoints(directions)

                // Draw polyline on map
                googleMap.addPolyline(PolylineOptions().addAll(points))
            }
        }
    }

    private fun getDirectionUrl(origin: LatLng, destination: LatLng): String {
        val apiKey = getString(R.string.google_api_key)
        val originString = "origin=${origin.latitude},${origin.longitude}"
        val destinationString = "destination=${destination.latitude},${destination.longitude}"
        val url = "https://maps.googleapis.com/maps/api/directions/json?$originString&$destinationString&key=$apiKey"
        return url
    }

    private fun getDirections(url: String, callback: (String) -> Unit) {
        // Perform the network request to fetch directions data
        // ...
    }

    private fun parseDirectionPoints(directions: String): List<LatLng> {
        // Parse the directions JSON and extract the points
        // ...
        return listOf()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 1
    }
}
