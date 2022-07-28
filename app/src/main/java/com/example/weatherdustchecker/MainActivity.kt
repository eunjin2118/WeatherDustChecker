package com.example.weatherdustchecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.weatherdustchecker.WeatherPageFragment.Companion.newIntance

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        // TODO : WeatherPageFragment 표시하기 (FrameLayout에 넣어주기)
        val transaction = supportFragmentManager.beginTransaction()
        // TODO : newInstance클래스 메서드 정의해서 status값(문자열),
        // temperature값(Dobule) 전달할 수 있도록 해주기
        // 해당 값은 모두 프래그먼트의 번들 객체에 저장되어야 함
//        transaction.add(R.id.fragment_container,
//            WeatherPageFragment.newIntance(37.58, 126.98))
        transaction.add(R.id.fragment_container,
            DustPageFragment.newInstance(37.58, 126.98))

        transaction.commit()


    }

}