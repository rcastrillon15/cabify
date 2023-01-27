package com.cabify.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val _idGenerated: Int = 0,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "ratingBar")
    val ratingBar:Double,
    @ColumnInfo(name = "stock")
    val stock:Int
)
