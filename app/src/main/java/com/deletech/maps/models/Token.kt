package com.deletech.maps.models
import com.google.gson.annotations.SerializedName
class Token {
  @SerializedName("access_token")
    var accessToken: String? = "token"
  @SerializedName("expires")
    var expires: Long = 0
}