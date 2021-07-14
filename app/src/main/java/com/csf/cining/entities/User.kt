package com.csf.cining.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class User(
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun verifyUser(email: String, password: String): Boolean {
        return (email == this.email) and (password == this.password)
    }

}
