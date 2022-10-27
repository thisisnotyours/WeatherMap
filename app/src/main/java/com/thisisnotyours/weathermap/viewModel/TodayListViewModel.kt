package com.thisisnotyours.weathermap.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thisisnotyours.weathermap.model.TodayListItem
import com.thisisnotyours.weathermap.repositories.TodayListRepo

class TodayListViewModel: ViewModel() {

    //mutable live data actually extends live data -> it can be changed
    private var mTodayItems: MutableLiveData<List<TodayListItem>>? = null
    private var todayListRepo: TodayListRepo? = null

    fun init() {
        if (mTodayItems != null) {
            return
        }
        todayListRepo = TodayListRepo().getInstance()
        mTodayItems = todayListRepo?.getTodayItemsFromServer()
    }

    fun getTodayListItems(): LiveData<List<TodayListItem>>? {  //LiveData(메소드)에서 MutableLiveData 를 return 해줌..
        return mTodayItems                                     //LiveData can indirectly change through mutableLiveData..
    }                                                          //but [Observe] to change data?

}


