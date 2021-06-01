package com.csf.cining.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class User(
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun verifyUser(username: String, password: String): Boolean {
        return (username == this.username) and (password == this.password)
    }

}
