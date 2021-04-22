package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends ArrayAdapter
{
    List list = new ArrayList<>();

    public RestaurantAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }

    public void add(@Nullable Restaurants object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        RestaurantHolder restaurantHolder;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            restaurantHolder = new RestaurantHolder();
            restaurantHolder.tx_name=row.findViewById(R.id.name);
            restaurantHolder.tx_rid=row.findViewById(R.id.rid);
            restaurantHolder.im_url=row.findViewById(R.id.img);
            restaurantHolder.tx_rating=row.findViewById(R.id.rat);
            row.setTag(restaurantHolder);
        }
        else
        {
            restaurantHolder = (RestaurantHolder) row.getTag();
        }
        Restaurants restaurants = (Restaurants) this.getItem(position);
        restaurantHolder.tx_name.setText(restaurants.getName());
        restaurantHolder.tx_rid.setText(restaurants.getID());
        restaurantHolder.tx_rating.setText("Rating: "+restaurants.getRating());
        Glide.with(this.getContext()).load(restaurants.getUrl()).into(restaurantHolder.im_url);
        return row;
    }

}

class RestaurantHolder {
    TextView tx_name,tx_rid,tx_rating;
    ImageView im_url;
}