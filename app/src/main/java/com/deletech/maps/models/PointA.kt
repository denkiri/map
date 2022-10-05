package com.deletech.maps.models
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class PointA {
    @PrimaryKey(autoGenerate = true)
    var id = 1
    var doubleList: Holder? = null
}