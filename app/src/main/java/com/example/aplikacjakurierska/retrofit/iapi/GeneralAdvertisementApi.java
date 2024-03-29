package com.example.aplikacjakurierska.retrofit.iapi;

import com.example.aplikacjakurierska.retrofit.model.GeneralAdvertisement;
import com.example.aplikacjakurierska.retrofit.model.Product;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GeneralAdvertisementApi {

    @POST("/advert/add")
    Call<GeneralAdvertisement> add(
            @Header("Authorization")String token,
            @Body GeneralAdvertisement generalAdvertisement);
    @GET("/advert/get")
    Call<List<GeneralAdvertisement>> getAll(
            @Header("Authorization")String token
    );
    @GET("/advert/get/{id}")
    Call<Optional<GeneralAdvertisement>> getById(@Header("Authorization")String token,@Path("id")Long id);
    @PUT("/advert/edit/{id}")
    Call<GeneralAdvertisement> editById(@Header("Authorization")String token,@Path("id")Long id,@Body GeneralAdvertisement generalAdvertisement);
    @DELETE("/advert/delete/{id}")Call<Void>deleteById(
            @Header("Authorization")String token,
            @Path("id")Long id);

}
