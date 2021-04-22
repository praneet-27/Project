package com.example.project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION = 1 ;
    Button b1;
    ListView lv;
    String latt,lonn,url,rating;
    protected LocationManager locationManager;
    ApiInterface api;
    RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        restaurantAdapter = new RestaurantAdapter(home.this, R.layout.row_layout);
        lv = findViewById(R.id.restaurants);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.Base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiInterface.class);

        getRestaurants();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lv.setAdapter(restaurantAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String rrid = ((TextView)view.findViewById(R.id.rid)).getText().toString();
                Intent i = new Intent(home.this,RestaurantDetails.class);
                i.putExtra("r_id",rrid);
                startActivity(i);
            }
        });
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                home.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latt = String.valueOf(lat);
                lonn = String.valueOf(longi);

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getRestaurants() {
        final Call<ResponseBody> call = api.getRestaurant(latt,lonn);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String name="",rid="";
                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray values = jsonObject.getJSONArray("nearby_restaurants");

                    for (int i = 0; i < values.length(); i++) {
                        JSONObject r = values.getJSONObject(i);
                        name=r.getJSONObject("restaurant").getString("name");
                        rid=r.getJSONObject("restaurant").getString("id");
                        url=r.getJSONObject("restaurant").getString("featured_image");
                        rating=r.getJSONObject("restaurant").getJSONObject("user_rating").getString("aggregate_rating");
                        Restaurants rest = new Restaurants(name,url,rid,rating);
                        restaurantAdapter.add(rest);
                    }
                    //Log.d("123455678",""+restaurantAdapter.getCount());

                } catch (Exception e) {
                    Log.d("---error---",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latt=""+location.getLatitude();
        lonn=""+location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
