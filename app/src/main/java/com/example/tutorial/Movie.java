package com.example.tutorial;


public class Movie {
    private String title;
    private String desc;
    private int id;

    protected void onCreate() {
        this.title = "";
        this.desc = "";
    }

    public void setId(int id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getTitle(){
        return title;
    }

    public String getDesc(){
        return desc;
    }

    public int getId(){
        return id;
    }
}
