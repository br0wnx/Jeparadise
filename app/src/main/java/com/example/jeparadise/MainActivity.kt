package com.example.jeparadise

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jeparadise.ui.ViewPagerAdapter
import com.example.jeparadise.databinding.ActivityMainBinding
import com.example.jeparadise.ui.MapsFragment
import com.example.jeparadise.ui.culinary.CulinaryFragment
import com.example.jeparadise.ui.event.EventFragment
import com.example.jeparadise.ui.home.HomeFragment
import com.example.jeparadise.ui.place.PlaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupTab()
        requestLocationPermission()
    }

    private fun setupTab() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "")
        adapter.addFragment(PlaceFragment(), "")
        adapter.addFragment(EventFragment(), "")
        adapter.addFragment(CulinaryFragment(), "")

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_home)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_location)
        binding.tabs.getTabAt(2)!!.setIcon(R.drawable.ic_calendar)
        binding.tabs.getTabAt(3)!!.setIcon(R.drawable.ic_culinary)
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}