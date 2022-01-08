package com.otec.webdealit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class listOfmoviecategories {



    @SerializedName(("message"))
    private List<Object> list;

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

}
