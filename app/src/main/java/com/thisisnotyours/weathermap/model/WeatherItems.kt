package com.thisisnotyours.weathermap.model

import com.google.gson.annotations.SerializedName

//날씨 정보를 담는 데이트 클래스
//class WeatherModel {
//    @SerializedName("response")
//    var rainType = ""   //강수형태
//    var humidity = ""   //습도
//    var sky = ""        //하늘상태
//    var temp = ""       //기온
//    var fcstTime = ""   //예보시각
//}

//XML 파일 형식을 data class 로 구현
//data class WEATHER(val response: RESPONSE)
//data class RESPONSE(val header: HEADER, val body: BODY)
//data class HEADER(val resultCode: Int, val resutMsg: String)
//data class BODY(val dataType: String, val items: ITEMS, val totalCount: Int)
//data class ITEMS(val item: List<ITEM>)
//data class ITEM(val baseDate: String, val baseTime: String, val category: String, val fcstDate: String, val fcstTime: String, val fcstValue: String)
//data class ITEM(val category: String, val fcstDate: String, val fcstTime: String, val fcstValue: String)


//data class 와 class 의 차이???
// : class 앞에 data 를 붙여주면 -> "모델 클래스"

class WeatherReponse {
    @SerializedName("weather") var weather = ArrayList<Weather>()  //ex)  "weather": [{"id":800, "main":"Clear", "description":"clear sky", "icon":"01d"}]
    @SerializedName("main") var main: Main? = null   //ex)  "main": {"temp":290.45, "feels_like":290.32, "temp_min":286.5, "temp_max":293.72, "pressure":1020, "humidity":80}
    @SerializedName("wind") var wind: Wind? = null
    @SerializedName("sys") var sys: Sys? = null
    @SerializedName("clouds") var clouds: Clouds? = null
    @SerializedName("timezone") var timezone: String? = null
    @SerializedName("id") var city_id: Int? = null  //지역 id
    @SerializedName("name") var name: String? = null
//    @SerializedName("timezone") var timezone: ElseValue? = null
//    @SerializedName("name") var name: ElseValue? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main: String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon: String? =null
}

class Main {
    @SerializedName("temp") var temp: Float = 0.toFloat()
    @SerializedName("feels_like") var feels_like: Float = 0.toFloat()
    @SerializedName("temp_min") var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max") var temp_max: Float = 0.toFloat()
    @SerializedName("pressure") var pressure: Float = 0.toFloat()
    @SerializedName("humidity") var humidity: Float = 0.toFloat()
}

class Wind {
    @SerializedName("speed") var speed: Float = 0.toFloat()
    @SerializedName("deg") var deg: Float = 0.toFloat()
    @SerializedName("gust") var gust: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country") var country: String? = null
    @SerializedName("sunrise") var sunrise: String? = null  //일출
    @SerializedName("sunset") var sunset: String? = null   //일몰
}

class Clouds {
    @SerializedName("all") var all: String? = null
}

class Rain {
    @SerializedName("1h") var _1h: String? = null
    @SerializedName("3h") var _3h: String? = null
}

class Snow {
    @SerializedName("1h") var _1h: String? = null
    @SerializedName("3h") var _3h: String? = null
}

//class ElseValue {
//    @SerializedName("timezone") var timezone: String? = null
//    @SerializedName("name") var name: String? = null
//}
