package com.otec.webdealit.Retrofit_;

import com.otec.webdealit.model.Auth;
import com.otec.webdealit.model.Music_Response;
import com.otec.webdealit.model.Music_Upload;
import com.otec.webdealit.model.SendMovies;
import com.otec.webdealit.model.listOfmoviecategories;

import java.util.Map;

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


    @POST("webdealitAddMusic")
    Call<Music_Upload>  sendMusic(@Body Map<String,Object> sendMovies);


    @GET("Webdealit_Genre")
    Call<Music_Response>  getGenre();






}
