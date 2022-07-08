package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.bumptech.glide.Glide;
import com.example.mangareader.adapter.MangaFeaturesAdapter;
import com.example.mangareader.databinding.ActivityMainBinding;
import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.MangaModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvMangaList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvMangaList.setHasFixedSize(true);
        mangaFeaturesAdapter = new MangaFeaturesAdapter(mangaList);
        binding.rvMangaList.setAdapter(mangaFeaturesAdapter);

        // RECYCLER SEARCH
        binding.rvSearchManga.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvSearchManga.setHasFixedSize(true);
        mangaSearchAdapter = new MangaFeaturesAdapter(searchMangaList);
        binding.rvSearchManga.setAdapter(mangaSearchAdapter);


        MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
        Call<JsonObject> call = api.getManga();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject object = response.body();
                JsonArray arrayMangas = object.getAsJsonArray("data");

                for (int i = 0; i < arrayMangas.size(); i++) {

                    JsonObject manga = (JsonObject) arrayMangas.get(i);
                    String id = manga.getAsJsonPrimitive("id").getAsString();

                    JsonObject attributes = manga.getAsJsonObject("attributes");
                    String title = attributes.getAsJsonObject("title").getAsJsonPrimitive("en").getAsString();

                    String mangaCover = "";
                    String desc = "";
                    String status = "";
                    int year = 0;
                    String state = "";
                    String createdAt = "";
                    String updatedAt = "";
                    String lastVolume = "";
                    String lastChapter = "";
                    String publicationDemographic = "";

                    try {
                        mangaCover = manga.getAsJsonArray("relationships").get(2).getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                        desc = attributes.getAsJsonObject("description").getAsJsonPrimitive("en").getAsString();
                        status = attributes.getAsJsonPrimitive("status").getAsString();
                        year = attributes.getAsJsonPrimitive("year").getAsInt();
                        state = manga.getAsJsonObject("attributes").getAsJsonPrimitive("state").getAsString();
                        createdAt = manga.getAsJsonObject("attributes").getAsJsonPrimitive("createdAt").getAsString();
                        updatedAt = manga.getAsJsonObject("attributes").getAsJsonPrimitive("updatedAt").getAsString();
                        lastVolume = manga.getAsJsonObject("attributes").getAsJsonPrimitive("lastVolume").getAsString();
                        lastChapter = manga.getAsJsonObject("attributes").getAsJsonPrimitive("lastChapter").getAsString();
                        publicationDemographic = manga.getAsJsonObject("attributes").getAsJsonPrimitive("publicationDemographic").getAsString();
                    } catch (Exception ignored) {
                    }

                    MangaModel mangaModel = new MangaModel(id, title, desc, status, year, state, createdAt, updatedAt, lastVolume, lastChapter, publicationDemographic, mangaCover);
                    mangaList.add(mangaModel);
                }
                getMangaImage(mangaList);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                if (c.length() >= 4) {
                    search = c.toString();
                    searchManga(search);
                }

                if(c.equals("")){
                    searchMangaList.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void getMangaImage(List<MangaModel> list) {

        for (int i = 0; i < list.size(); i++) {

            MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
            Call<JsonObject> callCover = api.getCover(list.get(i).getMangaCover());

            int finalI = i;
            callCover.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body();

                    try {
                        String path = object.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonPrimitive("fileName").getAsString();
                        if (list.get(finalI).getMangaCover() != null) {
                            list.get(finalI).setMangaCover(list.get(finalI).getId() + "/" + path);
                        }

                    } catch (Exception e) {
                    }

                    mangaFeaturesAdapter.notifyDataSetChanged();
                    mangaSearchAdapter.notifyDataSetChanged();
                }


                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        }

    }

    private void searchManga(String search) {

        MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
        Call<JsonObject> callSearch = api.searchManga(search);

        callSearch.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                searchMangaList.clear();
                JsonObject object = response.body();
                JsonArray arraySearchMangas = object.getAsJsonArray("data");

                for (int i = 0; i < arraySearchMangas.size(); i++) {

                    JsonObject manga = (JsonObject) arraySearchMangas.get(i);
                    String id = manga.getAsJsonPrimitive("id").getAsString();

                    JsonObject attributes = manga.getAsJsonObject("attributes");

                    String title = "";
                    String mangaCover = "";
                    String desc = "";
                    String status = "";
                    int year = 0;
                    String state = "";
                    String createdAt = "";
                    String updatedAt = "";
                    String lastVolume = "";
                    String lastChapter = "";
                    String publicationDemographic = "";

                    try {
                        title = attributes.getAsJsonObject("title").getAsJsonPrimitive("en").getAsString();
                        mangaCover = manga.getAsJsonArray("relationships").get(2).getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                        desc = attributes.getAsJsonObject("description").getAsJsonPrimitive("en").getAsString();
                        status = attributes.getAsJsonPrimitive("status").getAsString();
                        year = attributes.getAsJsonPrimitive("year").getAsInt();
                        state = manga.getAsJsonObject("attributes").getAsJsonPrimitive("state").getAsString();
                        createdAt = manga.getAsJsonObject("attributes").getAsJsonPrimitive("createdAt").getAsString();
                        updatedAt = manga.getAsJsonObject("attributes").getAsJsonPrimitive("updatedAt").getAsString();
                        lastVolume = manga.getAsJsonObject("attributes").getAsJsonPrimitive("lastVolume").getAsString();
                        lastChapter = manga.getAsJsonObject("attributes").getAsJsonPrimitive("lastChapter").getAsString();
                        publicationDemographic = manga.getAsJsonObject("attributes").getAsJsonPrimitive("publicationDemographic").getAsString();
                    } catch (Exception ignored) {
                    }

                    MangaModel mangaModel = new MangaModel(id, title, desc, status, year, state, createdAt, updatedAt, lastVolume, lastChapter, publicationDemographic, mangaCover);
                    searchMangaList.add(mangaModel);
                }
                getMangaImage(searchMangaList);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}