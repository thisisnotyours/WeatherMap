package com.thisisnotyours.weathermap.repositories

import android.util.Log
import com.thisisnotyours.weathermap.data.api.WeatherApiService
import com.thisisnotyours.weathermap.model.WeatherReponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

//retrofit 을 사용하기 위한 빌더생성
private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object openApiObject {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

class WeatherRepo @Inject constructor(private val weatherApiService: WeatherApiService) {

    //set suspend function
    //when the api functions are function,
    // need to use either another suspend function or coroutine.

    //suspend 함수 withContext


    suspend fun get(url: String) =
        withContext(Dispatchers.IO) {

    }


    suspend fun getMainWeatherData(lat: Double, lon: Double, appid: String) =
        withContext(Dispatchers.IO){
            weatherApiService.GetOpenWeatherMap(
                lat,
                lon,
                appid
            )
        }

    suspend fun getCurrentWeather(lat: Double, lon: Double, appId: String?) =
        withContext(Dispatchers.IO){
        weatherApiService.GetCurrentWeather(
            lat,
            lon,
            appId
        )
    }


    //without using suspend
    fun getWeatherRepo(lat: Double, lon: Double, appId: String?){
        val call = openApiObject.retrofitService.GetOpenWeatherMap(37.532600, 127.024612, "cb783112f08bf2fd94a9b08e830369a9")
        call.enqueue(object : retrofit2.Callback<WeatherReponse>{
            override fun onResponse(
                call: Call<WeatherReponse>,
                response: Response<WeatherReponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("log_weatherRepo","200 successful")
                }
            }

            override fun onFailure(call: Call<WeatherReponse>, t: Throwable) {
                Log.e("log_weatherRepo","onFailure: "+t.message.toString())
            }

        })
    }

}