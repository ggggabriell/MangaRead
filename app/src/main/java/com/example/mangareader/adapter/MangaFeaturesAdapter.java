package com.example.mangareader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangareader.R;
import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.MangaModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaFeaturesAdapter extends RecyclerView.Adapter<MangaFeaturesAdapter.ViewHolder> {

    private List<MangaModel> list = new ArrayList<>();

    public MangaFeaturesAdapter(List<MangaModel> list){
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivMangaCover;
        private TextView tvMangaTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMangaCover = itemView.findViewById(R.id.ivMangaCover);
            tvMangaTitle = itemView.findViewById(R.id.tvMangaTitle);
        }
    }

    @NonNull
    @Override
    public MangaFeaturesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mangafeatures_item, parent, false);
        return new MangaFeaturesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaFeaturesAdapter.ViewHolder holder, int position) {
        MangaModel item = list.get(position);

        holder.tvMangaTitle.setText(item.getTitle());





    }

    @Override
    public int getItemCount() {
        return list.size();
    }





}
