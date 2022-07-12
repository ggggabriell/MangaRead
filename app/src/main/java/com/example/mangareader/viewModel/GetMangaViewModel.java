package com.example.mangareader.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.MangaModel;
import com.example.mangareader.view.Manga;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMangaViewModel extends ViewModel {

    private MutableLiveData<Boolean> mGetManga = new MutableLiveData<>();
    public LiveData<Boolean> getManga = mGetManga;


    private MutableLiveData<Boolean> mSearch = new MutableLiveData<>();
    public LiveData<Boolean> search = mSearch;


    private List<MangaModel> mangaList = new ArrayList<>();
    private List<MangaModel> searchMangaList = new ArrayList<>();

    public void getManga() {
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

                    String title = "", author = "", mangaCover = "", desc = "", status = "", state = "", createdAt = "", updatedAt = "", lastVolume = "", lastChapter = "", publicationDemographic = "";
                    int year = 0;

                    try {
                        title = attributes.getAsJsonObject("title").getAsJsonPrimitive("en").getAsString();
                        author = manga.getAsJsonArray("relationships").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsString();
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

                    MangaModel mangaModel = new MangaModel(id, title, author, desc, status, year, state, createdAt, updatedAt, lastVolume, lastChapter, publicationDemographic, mangaCover);
                    mangaList.add(mangaModel);
                }
                getMangaAuthor(mangaList, "features");

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }

    private void getMangaImage(List<MangaModel> list, String type) {
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

                    if (response.code() == 200) {
                        if (type.equals("features")) {
                            mGetManga.setValue(true);
                        } else if (type.equals("search")) {
                            mSearch.setValue(true);
                        }
                    } else {
                        mGetManga.setValue(false);
                        mSearch.setValue(false);
                    }


                }


                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        }

    }

    private void getMangaAuthor(List<MangaModel> list, String type) {

        for (int i = 0; i < list.size(); i++) {

            MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
            Call<JsonObject> callCover = api.getAuthor(list.get(i).getAuthor());

            int finalI = i;
            callCover.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body();

                    try {
                        String author = object.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonPrimitive("name").getAsString();
                        list.get(finalI).setAuthor(author);


                    } catch (Exception e) {
                    }
                    getMangaImage(list, type);

                }


                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        }
    }


    public List<MangaModel> getMangaList() {
        return mangaList;
    }

    public List<MangaModel> getSearchList() {
        return searchMangaList;
    }

    public void searchManga(String search) {

        if (!search.equals("")) {


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

                        String title = "", author = "", mangaCover = "", desc = "", status = "", state = "", createdAt = "", updatedAt = "", lastVolume = "", lastChapter = "", publicationDemographic = "";
                        int year = 0;

                        try {
                            title = attributes.getAsJsonObject("title").getAsJsonPrimitive("en").getAsString();
                            author = manga.getAsJsonArray("relationships").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsString();
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

                        if (!title.equals("")) {
                            MangaModel mangaModel = new MangaModel(id, title, author, desc, status, year, state, createdAt, updatedAt, lastVolume, lastChapter, publicationDemographic, mangaCover);
                            searchMangaList.add(mangaModel);
                        }

                    }
                    getMangaAuthor(searchMangaList, "search");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } else {
            mangaList.clear();
            mSearch.setValue(true);
        }

    }

}
