package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private Context context;
    private List<Reviews> review;

    public ReviewAdapter(Context context, List<Reviews> revs) {
        this.context = context;
        this.review = revs;
    }
    //public ReviewAdapter()
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.reviewitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reviews reviews =(Reviews) this.getItem(position);
        holder.tv1.setText(reviews.getReviewerName());
        holder.tv2.setText(reviews.getReview());
        holder.tv3.setText(reviews.getRating()+"/5");
        Glide.with(holder.im1.getContext()).load(reviews.photourl).into(holder.im1);
    }

    public Object getItem(int position) {
        return review.get(position);
    }

    @Override
    public int getItemCount() {
        return review.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1,tv2,tv3;
        ImageView im1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.revname);
            tv2=itemView.findViewById(R.id.revrev);
            tv3=itemView.findViewById(R.id.revrating);
            im1=itemView.findViewById(R.id.revimage);
        }
    }
}


