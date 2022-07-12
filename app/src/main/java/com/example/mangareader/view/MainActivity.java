package com.example.mangareader.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mangareader.R;
import com.example.mangareader.adapter.MangaFeaturesAdapter;
import com.example.mangareader.databinding.ActivityMainBinding;
import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.MangaModel;
import com.example.mangareader.view.fragments.Search;
import com.example.mangareader.viewModel.GetMangaViewModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MangaFeaturesAdapter.OnClick{

    private List<MangaModel> mangaList = new ArrayList<>();
    private ActivityMainBinding binding;
    private MangaFeaturesAdapter mangaFeaturesAdapter;
    private GetMangaViewModel mangaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvMangaList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvMangaList.setHasFixedSize(true);
        mangaFeaturesAdapter = new MangaFeaturesAdapter(mangaList, this);
        binding.rvMangaList.setAdapter(mangaFeaturesAdapter);

        mangaViewModel = new ViewModelProvider(this).get(GetMangaViewModel.class);

        mangaViewModel.getManga();
        mangaViewModel.getManga.observe(this, sucess -> {
            if (sucess) {
                mangaList = mangaViewModel.getMangaList();
                mangaFeaturesAdapter = new MangaFeaturesAdapter(mangaList, this);
                binding.rvMangaList.setAdapter(mangaFeaturesAdapter);

            }
        });

        binding.ivSearch.setOnClickListener(v -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fLayout, new Search());
            transaction.commit();
        });


    }
    @Override
    public void OnClickListener(MangaModel manga) {
        Intent intent = new Intent(this, Manga.class);
        intent.putExtra("manga", manga);
        startActivity(intent);
    }



}