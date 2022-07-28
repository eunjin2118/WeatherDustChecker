package com.example.weatherdustchecker

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherdustchecker.WeatherPageFragment.Companion.newInstance
import java.lang.Exception
import android.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    val PERMISSION_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener {
            lat = it.latitude
            lon = it.longitude

            Log.d("mytag", lat.toString())
            Log.d("mytag", lon.toString())
            locationManager.removeUpdates(locationListener)

            val pagerAdapter = MyPagerAdapter(supportFragmentManager, lat, lon)
            mPager.adapter = pagerAdapter


        }

        if(ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener)
        }else {
            ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION

                ),
            PERMISSION_REQUEST_CODE)
        }


        // 상단 제목 표시줄 숨기기
        supportActionBar?.hide()

        mPager = findViewById(R.id.pager)

        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}

            // 끝
            override fun onPageSelected(position: Int) {
                if(position == 0){
                    Toast.makeText(applicationContext,
                    "날씨 페이지입니다",
                    Toast.LENGTH_SHORT).show()
                }else if(position==1){
                    Toast.makeText(applicationContext,
                        "미세먼지 페이지입니다",
                        Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_CODE){
            var allPermissionGranted = true
            for(result in grantResults){
                allPermissionGranted = (result == PackageManager.PERMISSION_GRANTED)
                if(!allPermissionGranted) break
            }
            if(allPermissionGranted){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }else {
                Toast.makeText(applicationContext,
                "위치 정보 제공 동의가 필요합니다.",
                Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    inner class MyPagerAdapter(fm: FragmentManager, val lat:Double, val lon:Double) : FragmentStatePagerAdapter(fm) {
        override fun getCount() = 2

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> WeatherPageFragment.newInstance(lat, lon)
                1 -> DustPageFragment.newInstance(lat, lon)
                else -> throw Exception("페이지가 존재하지 않음")
            }
        }

    }

}