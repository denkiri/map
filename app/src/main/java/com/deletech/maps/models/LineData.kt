package com.deletech.maps.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class LineData {
    @SerializedName("features")
    @Expose
    var features: List<Feature>? = null

    @SerializedName("type")
    @Expose
    var type: String? = null
}