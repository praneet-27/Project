package com.example.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantDetails extends AppCompatActivity {
    ApiInterface api;
    String res_id, name, lat,lon,url,rating,phone,timings;
    String revname,review,revrat,revimg;
    ImageView imgv1;
    TextView tv1,tv2,tv3,tv4;
    Button b1,b2;
    RatingBar rb;
    RecyclerView recyclerView;
    private static final int REQUEST_PHONE_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        if (ActivityCompat.checkSelfPermission(RestaurantDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RestaurantDetails.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);}

        Intent intent = getIntent();
        res_id = intent.getStringExtra("r_id");


        b1=findViewById(R.id.direc);
        tv1=findViewById(R.id.rdname);
        tv2=findViewById(R.id.rdlat);
        tv3=findViewById(R.id.rdlon);
        imgv1 = findViewById(R.id.img);
        rb=findViewById(R.id.ratings);
        b2=findViewById(R.id.call);
        tv4=findViewById(R.id.time);

        recyclerView = findViewById(R.id.reviews);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.Base_url2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiInterface.class);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+tv2.getText().toString()+","+tv3.getText().toString()));
                startActivity(intent);

            }
        });

        getRestaurantDetail();
        getReviews();

    }

    private void getReviews() {
        final Call<ResponseBody> call = api.getReviews(res_id,"5");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String res = response.body().string();

                    List<Reviews> list= new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray rev = jsonObject.getJSONArray("user_reviews");
                    for (int i = 0; i < rev.length(); i++) {
                        JSONObject r = rev.getJSONObject(i);
                        revname = r.getJSONObject("review").getJSONObject("user").getString("name");
                        revimg =  r.getJSONObject("review").getJSONObject("user").getString("profile_image");
                        revrat =  r.getJSONObject("review").getString("rating");
                        review = r.getJSONObject("review").getString("review_text");
                        Reviews revie = new Reviews(revrat,review,revname,revimg);
                        Log.d("1234567890-",revie.getReview());
                        list.add(revie);
                        ReviewAdapter reviewAdapter = new ReviewAdapter(RestaurantDetails.this,list);

                        recyclerView.setAdapter(reviewAdapter);

                    }

                }
                catch (Exception e)
                {
                    Log.d("------error-------",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void getRestaurantDetail() {
        final Call<ResponseBody> call = api.getRestaurantDetails(res_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);
                    name = jsonObject.getString("name");
                    lat= jsonObject.getJSONObject("location").getString("latitude");
                    lon= jsonObject.getJSONObject("location").getString("longitude");
                    url= jsonObject.getString("thumb");
                    rating=jsonObject.getJSONObject("user_rating").getString("aggregate_rating");
                    phone=jsonObject.getString("phone_numbers");
                    timings=jsonObject.getString("timings");

                    tv1.setText(name);
                    tv2.setText(lat);
                    tv3.setText(lon);
                    tv4.setText("Timings: "+timings);
                    rb.setRating(Float.parseFloat(rating));
                    Glide.with(RestaurantDetails.this).load(url).into(imgv1);
                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    Log.d("------error-------",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
