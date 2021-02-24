package com.WillyFisky.mobile911

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        val drawer = findViewById<ImageView>(R.id.drawer)
        val mapSpinner = findViewById<ProgressBar>(R.id.mapSpinner)
        val sendButton = findViewById<FloatingActionButton>(R.id.sendButton)
        val requestText = findViewById<EditText>(R.id.requestText)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapSpinner.isVisible = true
        mapSpinner.onFinishTemporaryDetach()

        //initializing database
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        sendButton.setOnClickListener {
            writeNewUser(requestText, "nairobi")
        }

        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }
        }

        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_login -> {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    println("Not pressed")
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        drawer.setOnClickListener {
            val actionbar = object : ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close){
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                }

                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                }
            }
            toggle.isDrawerIndicatorEnabled = true
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            println("Clicked ..")
        }
    }

    private fun writeNewUser(request: EditText, location: String?) {
        val user = Request(request.text.toString(), location)
        databaseReference.child("Data").setValue(user)
        request.setText("")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()
        setMapLongClick(mMap)
        val latitude = -1.286389
        val longitude = 36.817223
        val zoomLevel = 15f

        val homeLatLng = LatLng(latitude, longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        mMap.addMarker(MarkerOptions().position(homeLatLng))


    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            map.addMarker(
                    MarkerOptions()
                            .position(latLng)
            )
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

}