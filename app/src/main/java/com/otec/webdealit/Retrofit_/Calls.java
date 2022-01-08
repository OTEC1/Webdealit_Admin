package com.otec.webdealit.Retrofit_;

import com.otec.webdealit.model.Auth;
import com.otec.webdealit.model.SendMovies;
import com.otec.webdealit.model.listOfmoviecategories;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Calls {

    @GET("webdealit_Movie_categories")
    Call<listOfmoviecategories> getMovie_categories();


    @GET("webdealit_lock")
    Call<Auth> getAuth();


    @POST("webdealitAddMovie")
    Call<SendMovies>  sendMovie(@Body SendMovies sendMovies);



}
