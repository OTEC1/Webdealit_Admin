package com.otec.webdealit.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.otec.webdealit.Adpater.DaysCount;
import com.otec.webdealit.R;
import com.otec.webdealit.Retrofit_.Base_config;
import com.otec.webdealit.Retrofit_.Calls;
import com.otec.webdealit.Utils.utils;
import com.otec.webdealit.model.ViewCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfVisitors extends Fragment {


    private List<Map<String,Object>> visitCounts;
    private DaysCount listOfVisitors;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    private  String TAG= "ListOfVisitors";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_of_visitors, container, false);
        Bundle bundle = getArguments();
        visitCounts = new ArrayList<>();
        progressBar = (ProgressBar)  view.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.vistcounterdisplay);
        if(bundle.getInt("table") == 1)
            WEBFLY();
        else
            if(bundle.getInt("table") ==2)
            NETWORK();
            else
                BOAT();


        return view;
    }

    private void BOAT() {


        Calls calls = Base_config.getConnection().create(Calls.class);
        Call<ViewCount> visitCount = calls.getVisitCountboaT();
        visitCount.enqueue(new Callback<ViewCount>() {
            @Override
            public void onResponse(Call<ViewCount> call, Response<ViewCount> response) {
                List<Map<String,Object>> e =  response.body().getList();
                for(int x=0; x<e.size(); x++) {
                    Map<String,Object> data = new HashMap<>();
                    data.put("date", e.get(x).get("date"));
                    data.put("count", e.get(x).get("count"));
                    visitCounts.add(data);
                }
                MASHUP(visitCounts);
            }

            @Override
            public void onFailure(Call<ViewCount> call, Throwable t) {
                new utils().message("Error Occurred"+t.getLocalizedMessage(), getContext());
            }
        });

    }

    private void NETWORK() {
        Calls calls = Base_config.getConnection().create(Calls.class);
        Call<ViewCount> visitCount = calls.getVisitCountNetwork();
        visitCount.enqueue(new Callback<ViewCount>() {
            @Override
            public void onResponse(Call<ViewCount> call, Response<ViewCount> response) {

                List<Map<String,Object>> e =  response.body().getList();
                for(int x=0; x<e.size(); x++) {
                    Map<String,Object> data = new HashMap<>();
                    data.put("date", e.get(x).get("date"));
                    data.put("count", e.get(x).get("count"));
                    visitCounts.add(data);
                }
                MASHUP(visitCounts);
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
                List<Map<String,Object>> e =  response.body().getList();
                for(int x=0; x<e.size(); x++) {
                    Map<String,Object> data = new HashMap<>();
                    data.put("date", e.get(x).get("date"));
                    data.put("count", e.get(x).get("count"));
                    visitCounts.add(data);
                }
                Log.d(TAG, "onResponse: "+visitCounts);
               MASHUP(visitCounts);
            }

            @Override
            public void onFailure(Call<ViewCount> call, Throwable t) {
                new utils().message("Error Occurred"+t.getLocalizedMessage(), getContext());
            }
        });
    }

    private void MASHUP(List<Map<String,Object>> visitCounts) {
       LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        listOfVisitors = new DaysCount(visitCounts,getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(listOfVisitors);
        progressBar.setVisibility(View.INVISIBLE);

    }
}