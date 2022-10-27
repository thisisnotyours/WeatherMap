package com.thisisnotyours.weathermap.data.api


import com.thisisnotyours.weathermap.model.WeatherReponse
import retrofit2.Response
import retrofit2.http.*

interface WeatherApiService {

//    val API_KEY = "gcCTiaIn3uZyjxCu%2FDA02PQbAmbJOWayjc%2FC5wgQOcnZWqgyQyka4YjZLMtQWDEkuqfS5AsPGQgxat1GRQNsGQ%3D%3D"

    //open weather map api
    @GET("data/2.5/weather")
    fun GetOpenWeatherMap(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String)
    :retrofit2.Call<WeatherReponse>


    //suspend 이용하기 (MEMO 파일 참조)
    @GET("data/2.5/weather")
    suspend fun GetCurrentWeather(@Query("lat") lat: Double
                                  , @Query("lon") lon: Double
                                  , @Query("appid") appid: String?)
    :Response<WeatherReponse>

}

