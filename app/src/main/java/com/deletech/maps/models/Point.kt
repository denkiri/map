package com.deletech.maps.models
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Point {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @SerializedName("pointA")
    @Expose
    var pointA: List<Double>? = null

    @SerializedName("pointB")
    @Expose
    var pointB: List<Double>? = null

}