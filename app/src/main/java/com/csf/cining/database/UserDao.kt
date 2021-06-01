package com.csf.cining.database

import androidx.room.*
import com.csf.cining.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id")
    fun getUsers(): MutableList<User?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(user: User?)

    @Update
    fun updatePerson(user: User?)

    @Delete
    fun delete(user: User?)

    @Query("SELECT * FROM users WHERE id = :id")
    fun loadPersonById(id: Int): User?
}