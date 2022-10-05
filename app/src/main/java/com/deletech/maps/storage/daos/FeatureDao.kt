package com.deletech.maps.storage.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deletech.maps.models.Feature
interface FeatureDao {
    /*
    @Query("SELECT * FROM Feature")
    fun getAll():List<Feature>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<Feature>)
    @Query("DELETE FROM Feature")
    fun delete()

     */
}