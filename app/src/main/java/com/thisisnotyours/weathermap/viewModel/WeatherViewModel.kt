package com.thisisnotyours.weathermap.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thisisnotyours.weathermap.model.Result
import com.thisisnotyours.weathermap.model.Weather
import com.thisisnotyours.weathermap.model.WeatherReponse
import com.thisisnotyours.weathermap.repositories.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo
): ViewModel() {
    private val _liveData = MutableLiveData<Result<WeatherReponse>>()
    private val liveData: LiveData<Result<WeatherReponse>>
        get() = _liveData


    suspend fun getCurrentWeatherData(
        lat: Double,
        lon: Double,
        appId: String
    ) : Response<WeatherReponse> {
        return weatherRepo.getCurrentWeather(lat, lon, appId)
    }

//    suspend fun getData(
//        lat: Double,
//        lon: Double,
//        appid: String
//    ): LiveData<Result<WeatherReponse>> {
//        viewModelScope.launch {
//            _liveData.postValue(Result.loading(null))
//
//            weatherRepo.getCurrentWeather(lat, lon, appid).let {
//                if (it.isSuccessful) {
//                    _liveData.postValue(Result.success(it.body()))
//                }else{
//                    _liveData.postValue(Result.error(it.errorBody().toString(), null))
//                }
//            }
//        }
//        return liveData
//    }


//    fun getCurrentWeatherData(
//        lat: Double,
//        lon: Double,
//        appid: String
//    ): LiveData<Result<WeatherReponse>> {
//
//        viewModelScope.launch {
//            _liveData.postValue(com.thisisnotyours.weathermap.model.Result)
//
//            weatherRepo.getMainWeatherData(lat, lon, appid).let {
//                if (it.is)
//            }
//        }
//    }





}



