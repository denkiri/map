package com.deletech.maps.models
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class GeometryPolygon {
    @SerializedName("coordinates")
    @Expose
    var coordinates: List<List<List<Float>>>? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
}