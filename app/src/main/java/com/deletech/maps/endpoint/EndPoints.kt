package com.deletech.maps.endpoint
import com.deletech.maps.models.*
import retrofit2.Call
import retrofit2.http.*
interface EndPoints {
    @GET("points")
    fun points(): Call<Point>
    @GET("geo/line")
    fun line(): Call<LineData>
    @GET("geo/polygon")
    fun polygon(): Call<PolygonData>
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body userData: Profile): Call<Token>

}