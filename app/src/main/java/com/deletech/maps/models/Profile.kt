package com.deletech.maps.models
import com.google.gson.annotations.SerializedName
data class Profile(
    @SerializedName("username")
    val username:String?,
    @SerializedName("password")
    val password: String?

)


