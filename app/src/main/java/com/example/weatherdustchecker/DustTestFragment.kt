package com.example.weatherdustchecker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

@JsonDeserialize(using = MyDustDeserializer::class)
data class OpenDustAPIJSONResponse(
    val pm10: Int,
    val pm25: Int)

class MyDustDeserializer : StdDeserializer<OpenDustAPIJSONResponse>(
    OpenDustAPIJSONResponse::class.java
){
    override fun deserialize(
        p: JsonParser?,
        ctxt: DeserializationContext?
    ): OpenDustAPIJSONResponse {

        val node = p?.codec?.readTree<JsonNode>(p)

        var iaqi = node?.get("data")?.get("iaqi")
        var pm10 = iaqi?.get("pm10")?.get("v")?.asInt()
        var pm25 = iaqi?.get("pm25")?.get("v")?.asInt()


        return OpenDustAPIJSONResponse(
            pm10!!,
            pm25!!
        )

    }
}


class DustTestFragment : Fragment(){
    lateinit var dustnumber1: TextView
    lateinit var dustnumber2: TextView
    var TOKEN ="6af6a9d9f6b586e6302950ce5cac2639d22f0b4b"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.dust_test_fragment,
                container, false)

        dustnumber1 = view.findViewById<TextView>(R.id.dust1)
        dustnumber2 = view.findViewById<TextView>(R.id.dust2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var lat = arguments?.getDouble("lat")
        var lng = arguments?.getDouble("lng")
        val url = "https://api.waqi.info/feed/geo:${lat};${lng}/?token=${TOKEN}"

        APICall(object : APICall.APICallback{
            override fun onComplete(result: String) {
                Log.d("mytag", result)

                var mapper = jacksonObjectMapper()

                var data1 = mapper?.readValue<OpenDustAPIJSONResponse>(result)
                dustnumber1.text = data1.pm10.toString()

                var data2 = mapper?.readValue<OpenDustAPIJSONResponse>(result)
                dustnumber2.text = data2.pm25.toString()

            }
        }).execute(URL(url))
    }

    companion object {
        //fun newIntance(status: String, temperature: Double)
        // 요청해서 받아 오는 정보들(위치정보만 있으면 됨)
        fun newIntance(lat: Double, lng: Double)
                : DustTestFragment
        {
            val fragment = DustTestFragment()

            // 번들객체에 필요한 정보를 저장
            val args = Bundle()
            args.putDouble("lat", lat)
            args.putDouble("lng", lng)
            fragment.arguments = args

            return fragment
        }
    }

}