package com.example.project;

public class Reviews {
    String rating,review,name,photourl;

    Reviews(String a, String b, String c,String d)
    {
        this.rating=a;
        this.review=b;
        this.name=c;
        this.photourl=d;;
    }

    public String getReviewerName() {
        return name;
    }

    public String getPhotourl() {
        return photourl;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}
