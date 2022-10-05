package com.deletech.maps.models
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Feature {
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("properties")
    @Expose
    var properties: Properties? = null
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null
}