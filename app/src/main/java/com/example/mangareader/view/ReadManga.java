package com.example.mangareader.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.mangareader.R;
import com.example.mangareader.databinding.ActivityReadMangaBinding;
import com.example.mangareader.domain.model.ChapterModel;

public class ReadManga extends AppCompatActivity {

    private ActivityReadMangaBinding binding;
    private ChapterModel chapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadMangaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chapter = (ChapterModel) getIntent().getSerializableExtra("chapter");

            Glide.with(this)
                    .asBitmap()
                    .load(chapter.getPagesUrl().get(0))
                    .centerCrop()
                    .into(binding.ivManga);
        }
}