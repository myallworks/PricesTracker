package com.example.pricestracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PriceEntity> prices);

    @Query("SELECT * FROM prices")
    List<PriceEntity> getAllPrices();

    @Query("DELETE FROM prices")
    void deleteAllPrices();
}