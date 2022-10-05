package com.deletech.maps
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.deletech.maps.custom.Resource
import com.deletech.maps.models.LineData
import com.deletech.maps.models.Point
import com.deletech.maps.models.PointA
import com.deletech.maps.models.PolygonData
import com.deletech.maps.repository.GeoPointsRepository
import com.deletech.maps.repository.LinePointsRepository
import com.deletech.maps.repository.PolygonRepository

class MainViewModel (application: Application): AndroidViewModel(application){
    internal var geoPointsRepository: GeoPointsRepository
    internal var linePointsRepository: LinePointsRepository
    internal var polygonRepository:PolygonRepository
    private var  geoPointsObservable= MediatorLiveData<Resource<Point>>()
    private var  linePointsObservable= MediatorLiveData<Resource<LineData>>()
    private var  polygonObservable=MediatorLiveData<Resource<PolygonData>>()
    private val offlineGeoPointsObservable = MediatorLiveData<Resource<List<PointA>>>()
    init {
        geoPointsRepository = GeoPointsRepository(application)
        linePointsRepository = LinePointsRepository(application)
        polygonRepository= PolygonRepository(application)
        geoPointsObservable.addSource(geoPointsRepository.geoPointsObservable){data->geoPointsObservable.setValue(data)}
        linePointsObservable.addSource(linePointsRepository.geoPointsObservable){data->linePointsObservable.setValue(data)}
        polygonObservable.addSource(polygonRepository.geoPointsObservable){data->polygonObservable.setValue(data)}
        offlineGeoPointsObservable.addSource(geoPointsRepository.offlineGeoPointsObservable) { data ->offlineGeoPointsObservable.setValue(data) }
    }
    fun getGeoPoints(){
        geoPointsRepository.points()
    }
    fun observeGeoPoints(): LiveData<Resource<Point>> {
        return geoPointsObservable
    }
    fun getGeoLine(){
        linePointsRepository.line()
    }
    fun observeGeoLinePoints(): LiveData<Resource<LineData>> {
        return linePointsObservable
    }
    fun getGeoPolygon(){
        polygonRepository.polygon()
    }
    fun getOfflinePoint(){
       geoPointsRepository.getOfflinePoints()
    }
    fun polygonObservable(): LiveData<Resource<PolygonData>> {
        return polygonObservable
    }
    fun observeOfflineGeoPointsObservable(): LiveData<Resource<List<PointA>>> {
        return offlineGeoPointsObservable
    }



}