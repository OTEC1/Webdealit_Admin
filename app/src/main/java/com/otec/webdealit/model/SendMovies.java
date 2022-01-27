package com.otec.webdealit.model;

public class SendMovies {


    public  String Mtitle,categories,writeUp,fileName;
    public int likes,spin,thumbnail_orentation,year,downloadcount;



    public SendMovies(String mtitle, int year, String categories, String writeUp, String fileName,int likes,int thumbnail_orentation,int spin,int downloadcount) {
        Mtitle = mtitle;
        this.year = year;
        this.categories = categories;
        this.writeUp = writeUp;
        this.fileName = fileName;
        this.likes = likes;
        this.thumbnail_orentation = thumbnail_orentation;
        this.spin = spin;
        this.downloadcount = downloadcount;
    }
}
