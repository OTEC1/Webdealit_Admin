package com.otec.webdealit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ViewCount {
    


    @SerializedName(("message"))
    List <Map<String,Object>> list;

    public List<Map<String,Object>> getList() {
        return list;
    }


}
