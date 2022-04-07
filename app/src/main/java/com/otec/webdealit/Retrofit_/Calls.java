package com.otec.webdealit.Retrofit_;

import com.otec.webdealit.model.Auth;
import com.otec.webdealit.model.Music_Response;
import com.otec.webdealit.model.Music_Upload;
import com.otec.webdealit.model.SendMovies;
import com.otec.webdealit.model.ViewCount;
import com.otec.webdealit.model.listOfmoviecategories;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Calls {

    @GET("webdealit_Movie_categories")
    Call<listOfmoviecategories> getMovie_categories();

    @GET("https://us-central1-webflystore.cloudfunctions.net/appCat/getTimeStamp")
    Call<Object> getTimesTamp();

    @GET("webdealit_lock")
    Call<Auth> getAuth();


    @POST("webdealitAddMovie")
    Call<SendMovies>  sendMovie(@Body SendMovies sendMovies);


    @POST("webdealitAddMusic")
    Call<Music_Upload>  sendMusic(@Body Map<String,Object> sendMovies);


    @GET("Webdealit_Genre")
    Call<Music_Response>  getGenre();



    @GET("webdealitVisitGetCount")
    Call<ViewCount>  getVisitCountWebfly();



    @GET("https://us-central1-webflystore.cloudfunctions.net/Zlearner/LearnGetvisitcount")
    Call<ViewCount>  getVisitCountNetwork();



    @GET("/")
    Call<ViewCount>  getVisitCountboaT();



    @GET("webdealitHomePageTopList")
    Call<Music_Response>  get_web_fly_TopList();




    @POST("webdealitAddPost")
    Call<Object>  addPost(@Body Map<String,Object> objectMap);



    @POST("ImgResize")
    Call<Object>  addimg(@Body Map<String,Object> objectMap);




}
