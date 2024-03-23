package com.example.asm_and103_ph44315.service;

import com.example.asm_and103_ph44315.Model.Phone;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
//    172.20.10.4
//    192.168.1.118
    String ipv4 = "192.168.1.118";
    String DOMAIN = "http://"+ ipv4 +":3000/";

    @GET("api/list")
    Call<ArrayList<Phone>> getPhone();
    @POST("list/add")
    Call<Void> post(@Body Phone phone);
    @PUT("api/update/{id}")
    Call<Void> put(@Path("id") String id, @Body Phone phone);
    @DELETE("api/delete/{id}")
    Call<Void> delete(@Path("id") String id);
}
