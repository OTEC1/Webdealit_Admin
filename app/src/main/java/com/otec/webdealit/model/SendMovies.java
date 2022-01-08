package com.otec.webdealit.model;

public class SendMovies {


    public  String Mtitle,year,categories,writeUp,fileName;
    public int likes;

    public SendMovies(String mtitle, String year, String categories, String writeUp, String fileName,int likes) {
        Mtitle = mtitle;
        this.year = year;
        this.categories = categories;
        this.writeUp = writeUp;
        this.fileName = fileName;
        this.likes = likes;
    }
}
