package com.deletech.maps.storage.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.deletech.maps.models.PointA
@Dao
interface  PointADao {
    @Insert
    abstract fun insert(point: PointA?): Long
    @Query("SELECT * FROM PointA")
    abstract fun getAllPointA(): List<PointA?>?
    @Query("DELETE FROM PointA")
    abstract fun delete()
}