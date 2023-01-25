package com.cabify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface IProductLocal {
    @Transaction
    suspend fun transaction(entity: List<ProductEntity>){
        delete()
        insert(entity)
    }

    @Query("SELECT * FROM product")
    fun select(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<ProductEntity>)

    @Query("DELETE FROM product")
    fun delete()
}
