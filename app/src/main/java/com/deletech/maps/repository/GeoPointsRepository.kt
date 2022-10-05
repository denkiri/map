package com.deletech.maps.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.deletech.maps.R
import com.deletech.maps.custom.Resource
import com.deletech.maps.models.Holder
import com.deletech.maps.models.Point
import com.deletech.maps.models.PointA
import com.deletech.maps.networks.NetworkUtils
import com.deletech.maps.networks.RequestService
import com.deletech.maps.storage.MapsDatabase
import com.deletech.maps.storage.daos.PointADao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class GeoPointsRepository (application: Application){
    private val context: Context
   private val getPointADao: PointADao
   private val db: MapsDatabase
    val geoPointsObservable = MutableLiveData<Resource<Point>>()
    val offlineGeoPointsObservable = MutableLiveData<Resource<List<PointA>>>()
    init {
        context =application.applicationContext
        db =MapsDatabase.getDatabase(application)!!
      getPointADao = db.getPointADao()
    }
    fun points(){
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            getPoints()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    fun savePointA(data: PointA){
       GlobalScope.launch(context = Dispatchers.Main)
        {
          getPointADao.delete()
          data.let { getPointADao.insert(it) }
       }
       }

    fun getOfflinePoints() {
        GlobalScope.launch(context = Dispatchers.Main)
        {
           getPointADao.getAllPointA()
        }
    }
    private fun getPoints() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJpc3MiOiJodHRwOi8vMTkyLjE2OC41MC42OTo4MDkwL2xvZ2luIiwiZXhwIjoxNjY1MDA1NzczLCJhdXRob3JpdGllcyI6WyJzdHVkZW50OnJlYWQiLCJST0xFX0FETUlOVFJBSU5FRSIsImNvdXJzZTpyZWFkIl19.LfyEG2fP7UYvyvkhgsODm4l0LeUwiTEAKDjFktPhqxg").points()
            call.enqueue(object : Callback<Point> {
                override fun onFailure(call: Call<Point>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<Point>?, response: Response<Point>?){
                    if (response != null) {
                        if (response.isSuccessful) {
                                setIsSuccessful(response.body()!!)
                            val mu1 = PointA()
                            val h = Holder()
                            h.pointA = response.body()!!.pointA
                            mu1.doubleList = h
                           savePointA(mu1)
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
    private fun setIsSuccessful(parameters: Point) {
        geoPointsObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String) {
        geoPointsObservable.postValue(Resource.error(message, null))
    }

}