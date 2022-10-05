package com.deletech.maps.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.deletech.maps.R
import com.deletech.maps.custom.Resource
import com.deletech.maps.models.LineData
import com.deletech.maps.models.Point
import com.deletech.maps.networks.NetworkUtils
import com.deletech.maps.networks.RequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LinePointsRepository (application: Application){
    private val context: Context
    val geoPointsObservable = MutableLiveData<Resource<LineData>>()
    init {
        context=application.applicationContext
    }
    fun line(){
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            getLine()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun getLine() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJpc3MiOiJodHRwOi8vMTkyLjE2OC41MC42OTo4MDkwL2xvZ2luIiwiZXhwIjoxNjY0OTE4ODY4LCJhdXRob3JpdGllcyI6WyJzdHVkZW50OnJlYWQiLCJST0xFX0FETUlOVFJBSU5FRSIsImNvdXJzZTpyZWFkIl19.WE10Wom92k0yFGjCwvLwFh_Sw8sq6e0dh8fUZJ5Mr10").line()
            call.enqueue(object : Callback<LineData> {
                override fun onFailure(call: Call<LineData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<LineData>?, response: Response<LineData>?){
                    if (response != null) {
                        if (response.isSuccessful) {
                            setIsSuccessful(response.body()!!)
                            val type= response.body()!!.features
                            var geometry=type


                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }

    private fun setIsLoading() {
        geoPointsObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccessful(parameters: LineData) {
        geoPointsObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String) {
        geoPointsObservable.postValue(Resource.error(message, null))
    }
}