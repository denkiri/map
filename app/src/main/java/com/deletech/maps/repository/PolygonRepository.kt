package com.deletech.maps.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.deletech.maps.R
import com.deletech.maps.custom.Resource
import com.deletech.maps.models.GeometryPolygon
import com.deletech.maps.models.PolygonData
import com.deletech.maps.networks.NetworkUtils
import com.deletech.maps.networks.RequestService
import com.deletech.maps.storage.MapsDatabase
import com.deletech.maps.storage.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PolygonRepository (application: Application) {
    private val context: Context
    private val db: MapsDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val geoPointsObservable = MutableLiveData<Resource<GeometryPolygon>>()
    init {
        context=application.applicationContext
        db = MapsDatabase.getDatabase(application)!!
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
            val call = RequestService.getService(preferenceManager.getToken()).polygon()
            call.enqueue(object : Callback<PolygonData> {
                override fun onFailure(call: Call<PolygonData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<PolygonData>?, response: Response<PolygonData>?){
                    if (response != null) {
                        if (response.isSuccessful) {
                          //  setIsSuccessful(response.body()!!)
                            val feature= response.body()!!.features
                            if (feature != null) {
                                for (i in 0 until feature.count()) {
                                    val type=feature[i].type

                                }
                                for (i in 0 until feature.count()) {
                                    val properties=feature[i].properties
                                }
                                for (i in 0 until feature.count()) {
                                    val coordinates= feature[i].geometry
                                   // preferenceManager.saveType(coordinates!!.coordinates.toString())
                                    setIsSuccessful(coordinates!!)
                                }
                            }
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
    private fun setIsSuccessful(parameters: GeometryPolygon) {
        geoPointsObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String) {
        geoPointsObservable.postValue(Resource.error(message, null))
    }


}