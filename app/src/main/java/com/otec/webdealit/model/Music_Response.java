package com.otec.webdealit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Music_Response {


    @SerializedName("message")
    public List<Object> list;

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
