package com.otec.webdealit.Utils;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class Webflystore {

    private static FirebaseApp INSTANCE;


    public  static  FirebaseApp getInstance(Context context){
        if(INSTANCE == null)
            INSTANCE = getSecondProject(context);

        return  INSTANCE;
    }



    public  static  FirebaseApp getSecondProject(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyATYD-I3Xsm1flqOdcUGUnrId82UNGfQjE")
                .setApplicationId("1:80442442551:web:891062a0e13b6b60d150b2")
                .setProjectId("webflystore")
                .build();
        FirebaseApp.initializeApp(context,options,"webflystore");
        return  FirebaseApp.getInstance("webflystore");
    }
}
