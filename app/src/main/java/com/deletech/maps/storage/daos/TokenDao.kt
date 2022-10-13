package com.deletech.maps.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.deletech.maps.models.Token
@Dao
interface TokenDao {
    @Query("SELECT *FROM Token LIMIT 1")
    fun getToken(): LiveData<Token>
    @Query("SELECT * FROM Token LIMIT 1")
    fun fetch():Token
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(model:Token)
    @Update
    fun updateToken(model: Token)
    @Delete
    fun deleteToken(model: Token)
    @Query("DELETE FROM Token")
    fun delete()
}