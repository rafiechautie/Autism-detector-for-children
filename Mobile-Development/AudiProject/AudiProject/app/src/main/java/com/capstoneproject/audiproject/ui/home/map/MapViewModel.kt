package com.capstoneproject.audiproject.ui.home.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.audiproject.data.remote.response.IndonesiaItem
import com.capstoneproject.audiproject.data.remote.response.MapsTherapyResponse
import com.capstoneproject.audiproject.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel: ViewModel() {

    private val _listLocation = MutableLiveData<List<IndonesiaItem>>()
    val listLocation: LiveData<List<IndonesiaItem>> = _listLocation


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun findLocation(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDataLocation()
        client.enqueue(object: Callback<MapsTherapyResponse> {
            override fun onResponse(
                call: Call<MapsTherapyResponse>,
                response: Response<MapsTherapyResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listLocation.value = response.body()?.indonesia
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MapsTherapyResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}