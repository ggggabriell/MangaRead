package com.example.mangareader.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mangareader.adapter.ChaptersAdapter;
import com.example.mangareader.databinding.ActivityMangaBinding;
import com.example.mangareader.domain.model.ChapterModel;
import com.example.mangareader.domain.model.MangaModel;
import com.example.mangareader.viewModel.GetChapterViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Manga extends AppCompatActivity {

    private ActivityMangaBinding binding;
    private MangaModel manga;
    private GetChapterViewModel chapterViewModel;
    private List<ChapterModel> chapterList = new ArrayList<>();
    private ChaptersAdapter chaptersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manga = (MangaModel) getIntent().getSerializableExtra("manga");

        chapterViewModel = new ViewModelProvider(this).get(GetChapterViewModel.class);
        chapterViewModel.getChapters(manga.getId());

        binding.rvChapter.setLayoutManager( new LinearLayoutManager(this));
        binding.rvChapter.setHasFixedSize(true);

        chapterViewModel.getChapter.observe(this, sucess -> {
            if(sucess){
                binding.tvChapters.setText(chapterViewModel.getChapterListSize() + " chapters");
                chapterList = chapterViewModel.getChaptersList();


                chaptersAdapter = new ChaptersAdapter(chapterList);
                binding.rvChapter.setAdapter(chaptersAdapter);
            }else{
//                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });



        if (manga != null) {
            binding.tvAuthor.setText(manga.getAuthor());
            binding.tvTitle.setText(manga.getTitle());


            binding.tvCreatedAt.setText(manga.getCreatedAt() + " - " + manga.getUpdatedAt());

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