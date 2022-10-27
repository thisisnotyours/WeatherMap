package com.thisisnotyours.weathermap

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thisisnotyours.weathermap.adapter.TodayListAdapter
import com.thisisnotyours.weathermap.adapter.WeekListAdapter
import com.thisisnotyours.weathermap.data.api.WeatherApiService
import com.thisisnotyours.weathermap.databinding.ActivityMainBinding
import com.thisisnotyours.weathermap.model.Result
import com.thisisnotyours.weathermap.model.TodayListItem
import com.thisisnotyours.weathermap.model.WeatherReponse
import com.thisisnotyours.weathermap.model.WeekListItems
import com.thisisnotyours.weathermap.viewModel.TodayListViewModel
import com.thisisnotyours.weathermap.viewModel.WeatherViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*


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

class MainActivity : AppCompatActivity() {
    var todayListAdapter: TodayListAdapter? = null
    private lateinit var binding: ActivityMainBinding
    val log = "log_"
    private var mLocationManager: LocationManager? = null
    private var mLoationListener: LocationListener? = null
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private lateinit var mContext: Context
//    val weatherViewModel: WeatherViewModel by viewModels()
    private var weatherViewModel: WeatherViewModel? = null
    private var todayListViewModel: TodayListViewModel? = null


    private fun getLocation() {
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        mLoationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude
                    Log.d(log+"location", "a: "+location.toString())
                    Log.d(log + "위도/경도", "$lat/ $lng")
                }else{
                    Log.e(log+"location","b: "+location)
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }
        }
        checkLocation()
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled()) Log.d(log+"위치_permission","not able")
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        Log.d(log+"위치_able","true")
        mLocationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(log+"version", "SDK_INT: "+Build.VERSION.SDK_INT)

        if (Build.VERSION_CODES.P == Build.VERSION.SDK_INT) {
            Log.d(log+"version", "SDK_INT_P: "+Build.VERSION_CODES.P)
        }else if (Build.VERSION_CODES.Q == Build.VERSION.SDK_INT) {
            Log.d(log+"version", "SDK_INT_Q: "+Build.VERSION_CODES.Q)
        }

        getLocationPermission()

        mContext = applicationContext

        //get lat, lng of current location
//        getLocation()
//        mLocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
//
//        mLoationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                if (location != null) {
//                    lat = location.latitude
//                    lng = location.longitude
//                    Log.d(log+"location", "a: "+location.toString())
//                    Log.d(log + "위도/경도", "$lat/ $lng")
//
//                }else{
//                    Log.e(log+"location","b: "+location)
//                }
//            }
//
//            override fun onStatusChanged(provider: kotlin.String?, status: Int, extras: Bundle?) {
//                super.onStatusChanged(provider, status, extras)
//            }
//
//            override fun onProviderEnabled(provider: kotlin.String) {
//                super.onProviderEnabled(provider)
//            }
//
//            override fun onProviderDisabled(provider: kotlin.String) {
//                super.onProviderDisabled(provider)
//            }
//        }


        getMainWeatherData()
        getCurrentWeatherData()  //using MVVM, suspend function

        todayWeatherTimeList()
        weekWeatherList()

    }//onCreate..


    //코루틴 사용하기..
    private fun getCurrentWeatherData() {
        lifecycleScope.launch {
            val result = weatherViewModel?.getCurrentWeatherData(37.532600, 127.024612, "cb783112f08bf2fd94a9b08e830369a9")

//            Log.d(log+"result", result.toString())
//                .let {
//                    if (it?.isSuccessful == true) {
//                        Log.d(log+"coroutine","success")
//                    }else{
//                        Log.d(log+"coroutine","fail?")
//                    }
//
//                    if (it?.body() != null) {
//                        Log.d(log+"coroutine","not null")
//                    }else{
//                        Log.d(log+"coroutine","null")
//                    }
//                }
        }
    }



    //GetOpenWeatherMap - suspend function should be called only from a coroutine or another suspend function
    fun getMainWeatherData() {
        //        val call = openApiObject.retrofitService.GetOpenWeatherMap(44.34, 10.99, "cb783112f08bf2fd94a9b08e830369a9")
        val call = openApiObject.retrofitService.GetOpenWeatherMap(37.532600, 127.024612, "cb783112f08bf2fd94a9b08e830369a9")

        call.enqueue(object : retrofit2.Callback<WeatherReponse>{
            override fun onResponse(call: Call<WeatherReponse>, response: Response<WeatherReponse>) {
                Log.d(log+"weather_string", call.request().url().toString())
                Log.d(log+"weather_response", response.body().toString())
                if (response.isSuccessful) {
                    val item: WeatherReponse? = response.body()
                    Log.d(log+"weather_item", item.toString())

                    //지역
                    var country = item?.sys!!.country
                    Log.d(log+"나라",country.toString())

                    //일출
                    var sunrise = item.sys!!.sunrise
                    Log.d(log+"일출", sunrise.toString())

                    //일몰
                    var sunset = item.sys!!.sunset
                    Log.d(log+"일몰", sunset.toString())

                    //main
                    var main = item.weather[0].main
                    Log.d(log+"메인", main.toString())

                    //description
                    var description = item.weather[0].description
                    Log.d(log+"설명", description.toString())

                    //현재기온
                    var temp_c = String.format("%.0f", item.main!!.temp - 273.15)  //켈빈? 화씨 -> 섭씨로 변경
                    var temp_f = String.format("%.2f", item.main!!.temp)
                    Log.d(log+"현재기온", "$temp_c ($temp_f)")

                    //체감온도
                    var feels_like_c = String.format("%.0f", item.main!!.feels_like - 273.15)
                    var feels_like_f = item.main!!.feels_like
                    Log.d(log+"체감온도", "$feels_like_c ($feels_like_f)")

                    //최고기온
                    var temp_max = String.format("%.2f", item.main!!.temp_max - 273.15)

                    //최저기온
                    var temp_min = String.format("%.2f", item.main!!.temp_min - 273.15)
                    Log.d(log+"최고/최저기온", "$temp_max / $temp_min")

                    //습도
                    var humidity = item.main!!.humidity
                    Log.d(log+"습도", "$humidity%")

                    //아이콘
                    var icon = item.weather.get(0).icon
                    Log.d(log+"image", "$icon")

                    Log.d(log+"구름", item.clouds!!.all+"%")   //cloudiness
                    Log.d(log+"timezone", item.timezone!!+"")  //shift in seconds from UTC
                    Log.d(log+"지역_id", item.city_id!!.toString())  //지역 id
                    var area_name = item.name!!
                    Log.d(log+"지역_name", item.name!!+"") //지역 name


                    binding.tvMsg.text = "지역: $country/ $area_name\n\n" +
                            "일출시간: ${sunrise}\n" +
                            "일몰시간: ${sunset}\n\n" +
                            "$main\n" +
                            "$description\n\n" +
                            "현재기온: $temp_c ($temp_f)\n" +
                            "체감온도: $feels_like_c ($feels_like_f)\n\n" +
                            "최고:$temp_max / 최저:$temp_min\n\n" +
                            "습도: $humidity%\n\n" +
                            "아이콘: $icon"

                    binding.tvCurrentTemp.text = "$temp_c"
                    binding.tvCountry.text = "$country/ $area_name"
                    binding.tvCurrrentFeelsLike.text = "체감온도 $feels_like_c º"
                    binding.tvDescription.text = "$main  $description"

                    val cal = Calendar.getInstance()
//                    baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
//                    val time = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)
//                    baseTime = getTime(time)
                    binding.tvCurrentDate.text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(cal.time)
                    binding.tvCurrentTime.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(cal.time)

//                    binding.iv.setImageURI(image)

                    //오늘 시간별 날씨 리스트
//                    todayListViewModel = ViewModelProvider(this@MainActivity).get(TodayListViewModel::class.java)
//                    todayListViewModel!!.init()
//                    todayListViewModel!!.getTodayListItems()?.observe(this@MainActivity, androidx.lifecycle.Observer<List<TodayListItem>> {
//                        todayListAdapter?.notifyDataSetChanged()
//                    })
                    todayWeatherTimeList()

                    //일주일 날씨 리스트
                    weekWeatherList()

                }
            }

            override fun onFailure(call: Call<WeatherReponse>, t: Throwable) {
                Log.e(log+"weather_fail", call.request().url().toString())
                Log.e(log+"weather_fail", t.message.toString())
            }

        })
    }



    private fun todayWeatherTimeList() {
//        val todayList = arrayListOf<TodayListItem>()
//        binding.recyclerTodayList.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//            todayListAdapter = TodayListAdapter(todayList, todayListViewModel?.getTodayListItems()?.value as ArrayList<TodayListItem>)
//            binding.recyclerTodayList.adapter = todayListAdapter
//        }
        val todayList = arrayListOf<TodayListItem>()
        for (i in 0..10) {
            todayList.add(TodayListItem("오전 5시", "흐림", "15.20º", "40%"))
        }
        binding.recyclerTodayList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = TodayListAdapter(todayList) {
                Toast.makeText(this@MainActivity, "${todayList}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun weekWeatherList() {
        val weekList = arrayListOf<WeekListItems>()
        for (i in 0..6) {
            weekList.add(WeekListItems("월요일", "65%", "10.50º", "4º"))
        }
        binding.recyclerWeekList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = WeekListAdapter(weekList) {
                Toast.makeText(this@MainActivity, "${weekList}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
