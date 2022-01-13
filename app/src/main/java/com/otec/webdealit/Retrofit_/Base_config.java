package com.otec.webdealit.Retrofit_;

import com.otec.webdealit.Utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Base_config {


  static Retrofit retrofit;

  public  static  Retrofit getConnection(){
      OkHttpClient okHttpClient = new OkHttpClient.Builder()
//              .connectTimeout(90, TimeUnit.SECONDS)
//              .readTimeout(50,TimeUnit.SECONDS)
//              .writeTimeout(50,TimeUnit.SECONDS)
              .build();

      if (retrofit == null){
          retrofit = new Retrofit.Builder()
                  .baseUrl(Constants.BASE_URL)
                  .client(okHttpClient)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
      }
      return  retrofit;
  }
}
