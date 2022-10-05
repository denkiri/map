package com.deletech.maps.endpoint
import com.deletech.maps.models.Feature
import com.deletech.maps.models.LineData
import com.deletech.maps.models.Point
import com.deletech.maps.models.PolygonData
import retrofit2.Call
import retrofit2.http.*
interface EndPoints {
    @GET("points")
    fun points(): Call<Point>
    @GET("geo/line")
    fun line(): Call<LineData>
    @GET("geo/polygon")
    fun polygon(): Call<PolygonData>


}