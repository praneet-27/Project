package com.example.project;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {
    String Base_url = "https://developers.zomato.com/api/v2.1/?";
    String Base_url2 = "https://developers.zomato.com/api/v2.1/?res_id=100200";

    @Headers("user-key: 0192dad7c134cc15e778f3162c719120")
    @GET("geocode")
    Call<ResponseBody> getRestaurant(
            @Query("lat") String lat, @Query("lon") String lon
    );

    @Headers("user-key: 0192dad7c134cc15e778f3162c719120")
    @GET("restaurant")
    Call<ResponseBody> getRestaurantDetails(
            @Query("res_id") String res_id
    );

    @Headers("user-key: 0192dad7c134cc15e778f3162c719120")
    @GET("reviews")
    Call<ResponseBody> getReviews(
            @Query("res_id") String res_id, @Query("count") String count
    );

}
