package com.example.uniquefragrancebd.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uniquefragrancebd.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist")
    fun getWishlistItems(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(wishlistEntity: WishlistEntity)

    @Delete
    suspend fun removeFromWishlist(wishlistEntity: WishlistEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE id = :id)")
    fun isInWishlist(id: String): Flow<Boolean>
}