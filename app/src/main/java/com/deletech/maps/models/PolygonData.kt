package com.deletech.maps.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PolygonData {
    @SerializedName("features")
    @Expose
    var features: List<FeaturePolygon>? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
}