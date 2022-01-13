package com.otec.webdealit.model;

import com.google.gson.annotations.SerializedName;

public class Music_Upload {


    @SerializedName("message")
    public Object response;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
