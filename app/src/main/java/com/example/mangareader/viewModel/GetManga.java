package com.example.mangareader.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.MangaModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetManga extends ViewModel {

    private MutableLiveData<Boolean> mGetManga = new MutableLiveData<>();
    public LiveData<Boolean> getManga = mGetManga;


    private List<MangaModel> mangaList = new ArrayList<>();


    public void getManga(){
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

                if(response.code() == 200){
                    mGetManga.setValue(true);
                }else{
                    mGetManga.setValue(false);

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });


    }
}
