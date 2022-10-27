package com.thisisnotyours.weathermap.repositories

import androidx.lifecycle.MutableLiveData
import com.thisisnotyours.weathermap.model.TodayListItem

class TodayListRepo {

    private var instance: TodayListRepo? = null
    private val todayItems = arrayListOf<TodayListItem>()


    //today repo 객체
    fun getInstance(): TodayListRepo? {
        if (instance == null) {
            instance = TodayListRepo()
        }
        return instance
    }


    //Data cash like it;s from wen server
    //Pretend to get data from a webservice or online source
    //여기서 retrofit 사용하여 데이터 fetching 해야함
    open fun getTodayItemsFromServer(): MutableLiveData<List<TodayListItem>> {
        setTodayListItems()
        val data: MutableLiveData<List<TodayListItem>> = MutableLiveData<List<TodayListItem>>()
        data.value = todayItems
        return data
    }


    //저장소에 리사이클러뷰 아이템 셋팅
    private fun setTodayListItems() {
        for (i in 0..10) {
            todayItems.add(TodayListItem("오전 5시", "흐림", "15.20º", "40%"))
        }
    }



}