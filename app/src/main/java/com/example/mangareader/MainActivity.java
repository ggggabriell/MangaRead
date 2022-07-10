package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mangareader.adapter.MangaFeaturesAdapter;
import com.example.mangareader.databinding.ActivityMainBinding;
import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.MangaModel;
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

public class MainActivity extends AppCompatActivity {

    private List<MangaModel> mangaList = new ArrayList<>();
    private List<MangaModel> searchMangaList = new ArrayList<>();
    private ActivityMainBinding binding;
    private MangaFeaturesAdapter mangaFeaturesAdapter;
    private MangaFeaturesAdapter mangaSearchAdapter;
    private String search = "";
    private GetMangaViewModel mangaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvMangaList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvMangaList.setHasFixedSize(true);
        binding.rvSearchManga.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvSearchManga.setHasFixedSize(true);

        mangaViewModel = new ViewModelProvider(this).get(GetMangaViewModel.class);

        mangaViewModel.getManga();
        mangaViewModel.getManga.observe(this, sucess -> {
            if (sucess) {
                mangaList = mangaViewModel.getMangaList();
                mangaFeaturesAdapter = new MangaFeaturesAdapter(mangaList);
                binding.rvMangaList.setAdapter(mangaFeaturesAdapter);

            }
        });

        mangaViewModel.search.observe(this, sucess -> {
            if (sucess) {
                searchMangaList = mangaViewModel.getSearchList();
                mangaSearchAdapter = new MangaFeaturesAdapter(searchMangaList);
                binding.rvSearchManga.setAdapter(mangaSearchAdapter);
            }
        });


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                search = c.toString();
                mangaViewModel.searchManga(search);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}