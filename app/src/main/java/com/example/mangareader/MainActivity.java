package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
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
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    String mangaCover = manga.getAsJsonArray("relationships").get(2).getAsJsonObject().getAsJsonPrimitive("id").getAsString();

                    MangaModel mangaModel1 = new MangaModel(id, mangaCover);
                    mangaList.add(mangaModel1);

                    try {
                        JsonObject attributes = manga.getAsJsonObject("attributes");

                        String title = attributes.getAsJsonObject("title").getAsJsonPrimitive("en").getAsString();
                        String desc = attributes.getAsJsonObject("description").getAsJsonPrimitive("en").getAsString();
                        String status = attributes.getAsJsonPrimitive("status").getAsString();
                        int year = attributes.getAsJsonPrimitive("year").getAsInt();
                        String state = manga.getAsJsonObject("attributes").getAsJsonPrimitive("state").getAsString();
                        String createdAt = manga.getAsJsonObject("attributes").getAsJsonPrimitive("createdAt").getAsString();
                        String updatedAt = manga.getAsJsonObject("attributes").getAsJsonPrimitive("updatedAt").getAsString();
                        String lastVolume = manga.getAsJsonObject("attributes").getAsJsonPrimitive("lastVolume").getAsString();
                        String lastChapter = manga.getAsJsonObject("attributes").getAsJsonPrimitive("lastChapter").getAsString();
                        String publicationDemographic = manga.getAsJsonObject("attributes").getAsJsonPrimitive("publicationDemographic").getAsString();

                        MangaModel mangaModel = new MangaModel(id, title, desc, status, year, state, createdAt, updatedAt, lastVolume, lastChapter, publicationDemographic, mangaCover);
                        mangaList.set(i, mangaModel);

                    } catch (Exception e) {
                    }

                }


                getMangaImage();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        binding.tvTitle.setOnClickListener(v -> {
            getImage();
        });
    }

    private void getMangaImage() {

        for (int i = 0; i < mangaList.size(); i++) {

            MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
            Call<JsonObject> callCover = api.getCover(mangaList.get(i).getMangaCover());

            int finalI = i;
            callCover.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body();

                    try {

                        String path = object.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonPrimitive("fileName").getAsString();
                        if (mangaList.get(finalI).getMangaCover() != null) {

                            mangaList.get(finalI).setMangaCover(mangaList.get(finalI).getId() + "/" + path);
                            binding.tvContent.append( mangaList.get(finalI).getMangaCover() + "\n");
                        }

                    } catch (Exception e) {
                    }
                }


                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        }

    }

    private void getImage() {

        MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
        Call<JsonElement> callCover = api.getMangaImage(mangaList.get(1).getMangaCover());

        callCover.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                binding.tvContent.setText(response.code() + "");

                Glide.with(getBaseContext())
                        .asBitmap()
                        .load("https://mangadex.org/covers/" + mangaList.get(0).getMangaCover())
                        .centerCrop()
                        .into(binding.ivMangaCover);
                try {
                    JsonElement image = response.body();

                } catch (Exception e) {
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
}