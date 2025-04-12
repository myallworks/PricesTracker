package com.example.pricestracker.api;

import com.example.pricestracker.models.LoginReq;
import com.example.pricestracker.models.LoginResponse;
import com.example.pricestracker.models.Price;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("login")
    Call<LoginResponse> login(
            @Body LoginReq loginReq
    );

    @GET("prices")
    Call<List<Price>> getPrices(
            @Header("Authorization") String token
    );
}
