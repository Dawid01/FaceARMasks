package com.szczepaniak.covidmask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryVieHolder> {


    private File[] files;
    private Context context;
    private RecyclerView recyclerView;

    public GalleryAdapter(File[] files, Context context, RecyclerView recyclerView) {
        this.files = files;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public GalleryVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new GalleryVieHolder(v, recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryVieHolder holder, int position) {

        File file = files[position];
        Glide.with(context).load(file)
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class GalleryVieHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public GalleryVieHolder(@NonNull View itemView, RecyclerView recyclerView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
            int w = recyclerView.getWidth()/3;
            layoutParams.width = w;
            layoutParams.height = w;
            image.setLayoutParams(layoutParams);
        }
    }
}
