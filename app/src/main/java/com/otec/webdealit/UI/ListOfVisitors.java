package com.otec.webdealit.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.otec.webdealit.R;
import com.otec.webdealit.Retrofit_.Base_config;
import com.otec.webdealit.Retrofit_.Calls;
import com.otec.webdealit.Utils.utils;
import com.otec.webdealit.model.ViewCount;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfVisitors extends Fragment {


    private List<ViewCount> visitCount;
    private ListOfVisitors listOfVisitors;
    private RecyclerView recyclerView;


    private  String TAG= "ListOfVisitors";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_of_visitors, container, false);
        Bundle bundle = getArguments();

        if(bundle.getInt("table") == 1)
            WEBFLY();
        else
            NETWORK();


        return view;
    }

    private void NETWORK() {
        Calls calls = Base_config.getConnection().create(Calls.class);
        Call<ViewCount> visitCount = calls.getVisitCountNetwork();
        visitCount.enqueue(new Callback<ViewCount>() {
            @Override
            public void onResponse(Call<ViewCount> call, Response<ViewCount> response) {
                Log.d(TAG, "onResponse: 1 "+response.code()+"  "+response.body());
            }

            @Override
            public void onFailure(Call<ViewCount> call, Throwable t) {
                   new utils().message("Error Occurred"+t.getLocalizedMessage(), getContext());
            }
        });
    }




    private void WEBFLY() {
        Calls calls = Base_config.getConnection().create(Calls.class);
        Call<ViewCount> visitCount = calls.getVisitCountWebfly();
        visitCount.enqueue(new Callback<ViewCount>() {
            @Override
            public void onResponse(Call<ViewCount> call, Response<ViewCount> response) {
                Log.d(TAG, "onResponse: 2 "+response.code()+"  "+response.body());
            }

            @Override
            public void onFailure(Call<ViewCount> call, Throwable t) {
                new utils().message("Error Occurred"+t.getLocalizedMessage(), getContext());
            }
        });
    }
}