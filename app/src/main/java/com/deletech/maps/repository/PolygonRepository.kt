package com.deletech.maps.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.deletech.maps.R
import com.deletech.maps.custom.Resource
import com.deletech.maps.models.PolygonData
import com.deletech.maps.networks.NetworkUtils
import com.deletech.maps.networks.RequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PolygonRepository (application: Application) {
    private val context: Context
    val geoPointsObservable = MutableLiveData<Resource<PolygonData>>()
    init {
        context=application.applicationContext
    }
    fun polygon(){
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            getPolygon()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun getPolygon() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJpc3MiOiJodHRwOi8vMTkyLjE2OC41MC42OTo4MDkwL2xvZ2luIiwiZXhwIjoxNjY0OTE4ODY4LCJhdXRob3JpdGllcyI6WyJzdHVkZW50OnJlYWQiLCJST0xFX0FETUlOVFJBSU5FRSIsImNvdXJzZTpyZWFkIl19.WE10Wom92k0yFGjCwvLwFh_Sw8sq6e0dh8fUZJ5Mr10").polygon()
            call.enqueue(object : Callback<PolygonData> {
                override fun onFailure(call: Call<PolygonData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<PolygonData>?, response: Response<PolygonData>?){
                    if (response != null) {
                        if (response.isSuccessful) {
                            setIsSuccessful(response.body()!!)
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
    private fun setIsSuccessful(parameters: PolygonData) {
        geoPointsObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String) {
        geoPointsObservable.postValue(Resource.error(message, null))
    }

}