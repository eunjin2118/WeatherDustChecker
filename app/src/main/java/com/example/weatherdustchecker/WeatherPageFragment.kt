package com.example.weatherdustchecker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class WeatherPageFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.weather_page_fragment,
                container, false)

        // TODO: arguments 값 참조해서 두 개 값 가져오고, 해당하는 뷰에 출력해주기
        val statusText = view.findViewById<TextView>(R.id.weather_status_text)
        val temperatureText = view.findViewById<TextView>(R.id.weather_temp_text)

        statusText.text = arguments?.getString("status")
        temperatureText.text = arguments?.getString("temperature").toString()

        return view
    }

    companion object {
        fun newIntance(status: String, temperature: Double)
            : WeatherPageFragment
        {
            val fragment = WeatherPageFragment()

            // 번들객체에 필요한 정보를 저장
            val args = Bundle()
            args.putString("status", status)
            args.putDouble("temperature", temperature)
            fragment.arguments = args

            return fragment
        }
    }
}