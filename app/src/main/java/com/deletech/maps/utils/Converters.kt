package com.deletech.maps.utils
import androidx.room.TypeConverter
import com.deletech.maps.models.Holder
import com.google.gson.Gson
class Converters {
    @TypeConverter
    fun fromHolder(h: Holder?): String? {
        val gson = Gson()
        return gson.toJson(h)
    }
    @TypeConverter
    fun toHolder(s: String?): Holder? {
        return Gson().fromJson(s, Holder::class.java)
    }
}