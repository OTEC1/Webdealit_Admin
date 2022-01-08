package com.otec.webdealit.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Auth {



    @SerializedName(("message"))
    private  Map<String,Object>  list2;

    public Map<String,Object>  getList2() {
        return list2;
    }

    public void setList2(Map<String,Object>  list2) {
        this.list2 = list2;
    }
}
