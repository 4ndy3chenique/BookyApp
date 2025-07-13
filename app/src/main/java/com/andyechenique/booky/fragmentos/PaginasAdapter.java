package com.andyechenique.booky.fragmentos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andyechenique.booky.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PaginasAdapter extends RecyclerView.Adapter<PaginasAdapter.ViewHolder> {

    private final Context context;
    private final List<String> urlsPaginas;

    public PaginasAdapter(Context context, List<String> urlsPaginas) {
        this.context = context;
        this.urlsPaginas = urlsPaginas;
    }

    @NonNull
    @Override
    public PaginasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_pagina, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PaginasAdapter.ViewHolder holder, int position) {
        String url = urlsPaginas.get(position);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.bg_card)
                .into(holder.imgPagina);
    }

    @Override
    public int getItemCount() {
        return urlsPaginas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPagina;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPagina = itemView.findViewById(R.id.imgPagina);
        }
    }
}
