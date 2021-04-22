package com.example.project;

public class Restaurants {
    String name,rid,url,rating;
    Restaurants(String name,String url,String rid,String rating)
    {
        this.url=url;
        this.name=name;
        this.rid=rid;
        this.rating=rating;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return rid;
    }

    public String getUrl(){
        return url;
    }

    public String getRating() {
        return rating;
    }
}
