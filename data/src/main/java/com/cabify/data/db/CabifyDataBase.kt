package com.cabify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1)
abstract class CabifyDataBase : RoomDatabase() {
    abstract fun iProductLocal(): IProductLocal
}
