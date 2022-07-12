package com.example.mangareader.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mangareader.databinding.ActivityMangaBinding;
import com.example.mangareader.domain.model.MangaModel;


public class Manga extends AppCompatActivity {

    private ActivityMangaBinding binding;
    private MangaModel manga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manga = (MangaModel) getIntent().getSerializableExtra("manga");

        if (manga != null) {
            binding.tvAuthor.setText(manga.getAuthor());
            binding.tvTitle.setText(manga.getTitle());
            binding.tvCreatedAt.setText(manga.getCreatedAt());
            binding.tvDemographic.setText(manga.getPublicationDemographic());
            binding.tvDesc.setText(manga.getDesc());
            Glide.with(this)
                    .asBitmap()
                    .load("https://mangadex.org/covers/" + manga.getMangaCover())
                    .override(512, 256)
                    .centerCrop()
                    .into(new CustomTarget<Bitmap>() {
                              @Override
                              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                  Drawable dr = new BitmapDrawable(resource);
                                  binding.clMenu.setBackground(dr);
                                  binding.clMenu.getBackground().setAlpha(40);
                              }

                              @Override
                              public void onLoadCleared(@Nullable Drawable placeholder) {

                              }
                          }
                    );

            Glide.with(this)
                    .asBitmap()
                    .load("https://mangadex.org/covers/" + manga.getMangaCover())
                    .centerCrop()
                    .override(256, 512)
                    .into(binding.ivManga);
        }

        binding.ivArrowBack.setOnClickListener(v -> finish());

    }
}