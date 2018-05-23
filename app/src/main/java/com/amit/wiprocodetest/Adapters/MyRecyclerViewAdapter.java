package com.amit.wiprocodetest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amit.wiprocodetest.R;
import com.amit.wiprocodetest.module.Row;
import com.bumptech.glide.Glide;

import java.util.List;



/**
 * Created by dell on 5/12/2018.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.GithubViewHolder>{

    private List<Row> data;
    private Context context;

    //Constructor
    public MyRecyclerViewAdapter(Context context, List<Row> data){
        this.context= context;
        this.data= data;
    }


    //ViewHolder class: Initialize view items
    class GithubViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mTitle, mDescription;
        GithubViewHolder(View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.image);
            mTitle= (TextView) itemView.findViewById(R.id.title);
            mDescription= (TextView) itemView.findViewById(R.id.description);
        }
    }


    /*Implement onCreateViewHolder and onBindViewHolder */
    @Override
    public MyRecyclerViewAdapter.GithubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent ,false);
        return new GithubViewHolder(view);

    }

    @Override
    public void onBindViewHolder(GithubViewHolder holder, int position) {
        Row row = data.get(position);
        holder.mTitle.setText(row.getTitle());
        holder.mDescription.setText(row.getDescription());
        //Using Glide for image loading
        Glide.with(holder.imageView.getContext()).load(row.getImageHref()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
