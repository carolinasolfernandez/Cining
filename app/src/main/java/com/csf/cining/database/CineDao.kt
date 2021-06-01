package com.csf.cining.database

import androidx.room.*
import com.csf.cining.entities.Cine

@Dao
interface CineDao {
    @Query("SELECT * FROM cines ORDER BY id")
    fun getCines(): MutableList<Cine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createCine(cine: Cine?)

    @Update
    fun updateCine(cine: Cine?)

    @Delete
    fun deleteCine(cine: Cine)

    @Query("SELECT * FROM cines WHERE id = :id")
    fun getCineById(id: Int): Cine?
}