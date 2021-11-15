package shares.m.arduinomap.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.*
import shares.m.arduinomap.MapApplication
import shares.m.arduinomap.R
import shares.m.arduinomap.models.Point


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val pointsMap = mutableMapOf<String, Point>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @DelicateCoroutinesApi
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        loadPoints()
        mMap.setOnMarkerClickListener { marker ->
            onMarketClick(marker)
            true
        }
    }

    private fun onMarketClick(marker: Marker) {
        pointsMap[marker.id]?.let { point ->
            PointDialogFragment.newInstance(point)
                .show(supportFragmentManager, PointDialogFragment.TAG)
        }
    }

    @DelicateCoroutinesApi
    private fun loadPoints() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val points = (application as? MapApplication)?.pointApi?.getPoints()
                points?.let {
                    withContext(Dispatchers.Main) {
                        showPoints(points)
                    }
                }
            } catch (e: Exception) {
                Log.e("LoadError", e.stackTraceToString())
                withContext(Dispatchers.Main) {
                    ErrorDialogFragment()
                        .show(supportFragmentManager, ErrorDialogFragment.TAG)
                }
            }
        }
    }

    private fun showPoints(points: List<Point>) {
        val latLngBoundsBuilder = LatLngBounds.builder()
        points.forEach { point ->
            val latLng = LatLng(point.latitude, point.longitude)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_arduino)))
            )
            marker?.let {
                pointsMap[marker.id ] = point
            }
            latLngBoundsBuilder.include(latLng)
        }
        val latLngBounds = latLngBoundsBuilder.build()
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, MAP_PADDING))
    }

    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes, applicationContext.theme)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    companion object {

        const val MAP_PADDING = 100

    }

}