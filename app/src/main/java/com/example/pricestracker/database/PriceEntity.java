package com.example.pricestracker.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prices")
public class PriceEntity {
    @PrimaryKey
    @NonNull
    public String id = "";
    public String name;
    public double currentPrice;
    public double previousPrice;
    public long timestamp;
}