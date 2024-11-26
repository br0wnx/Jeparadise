package com.example.jeparadise.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.example.jeparadise.R
import com.example.jeparadise.databinding.ActivityDetailBinding
import com.example.jeparadise.ui.MapsFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var destinationLocation: LatLng? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val url = intent.getStringExtra("url")
        val location = intent.getStringExtra("location")
        val distance = intent.getStringExtra("distance")
        val description = intent.getStringExtra("description")
        val category = intent.getStringExtra("category")
        val geoPoint = intent.getParcelableExtra<Parcelable>("geo") as? GeoPoint
        val destinationGeo = geoPoint?.let { LatLng(it.latitude, it.longitude) }

        val fragment = MapsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mapFragmentContainer, fragment)
            .commit()

        Picasso.get().load(url).into(binding.image)
        binding.name.text = name
        binding.location.text = location
        binding.distance.text = distance
        binding.description.text = description
        binding.category.text = category

        val firestore = FirebaseFirestore.getInstance()

        firestore.collectionGroup("place")
            .get()
            .addOnSuccessListener { querySnapshot ->
                var destinationLocation: LatLng? = null
                for (document in querySnapshot.documents) {
                    val geoPoint = document.getGeoPoint("geo")
                    destinationLocation = geoPoint?.let { LatLng(it.latitude, it.longitude) }
                    // Perform actions with destinationLocation
                }

                // Set the destination location in the MapsFragment
                val fragment = MapsFragment().apply {
                    setDestinationLocation(destinationLocation)
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.mapFragmentContainer, fragment)
                    .commit()
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}
