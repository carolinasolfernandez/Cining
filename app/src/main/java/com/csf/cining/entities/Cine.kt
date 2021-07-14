package com.csf.cining.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

/*
@Entity(tableName = "cines")
class Cine(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "website") var website: String,
    @ColumnInfo(name = "latlng") var latlng: GeoPoint,
    @ColumnInfo(name = "image") var image: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: String = "0"
}
 */

data class Cine(
    var name: String? = null,
    var address: String? = null,
    var website: String? = null,
    var latlng: GeoPoint = GeoPoint(0.00, 0.00),
    var image: String? = null
) {
    @DocumentId
    var id: String = "0"
}