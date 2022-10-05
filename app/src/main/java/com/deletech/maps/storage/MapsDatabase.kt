package com.deletech.maps.storage
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.deletech.maps.models.*
import com.deletech.maps.storage.daos.*
import com.deletech.maps.utils.Converters
@Database(entities = [PointA::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class MapsDatabase : RoomDatabase() {
     companion object{
         private lateinit var INSTANCE:MapsDatabase
         fun getDatabase(context: Context):MapsDatabase?{
             synchronized(MapsDatabase::class.java){
                 INSTANCE = Room.databaseBuilder(context.applicationContext,
                     MapsDatabase::class.java,"cff_preferences"
                 )
                     .fallbackToDestructiveMigration()
                     .allowMainThreadQueries()
                     .build()
             }
             return INSTANCE
         }}
   //  abstract fun featureDao(): FeatureDao
   //  abstract fun featurePolygonDao():FeaturePolygonDao
    // abstract fun geometryLineDao():GeometryLineDao
   //  abstract fun geometryPolygonDao():GeometryPolylineDao
    // abstract fun pointDao(): PointDao
     abstract fun getPointADao():PointADao


 }
