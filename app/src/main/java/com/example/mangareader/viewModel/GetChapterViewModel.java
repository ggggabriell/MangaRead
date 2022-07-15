package com.example.mangareader.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.ChapterModel;
import com.example.mangareader.domain.model.MangaModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetChapterViewModel extends ViewModel {

    private MutableLiveData<Boolean> mGetChapter = new MutableLiveData<>();
    public LiveData<Boolean> getChapter = mGetChapter;

    private MutableLiveData<Boolean> mGetChapterUrl = new MutableLiveData<>();
    public LiveData<Boolean> getChapterUrl = mGetChapterUrl;


    private List<ChapterModel> chapterList = new ArrayList<>();
    private List<String> pagesUrl = new ArrayList<>();

    public void getChapters(MangaModel manga) {

        MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
        Call<JsonObject> call = api.getChapters(manga.getId(), 100);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject object = response.body();
                JsonArray arrayChapters = object.getAsJsonArray("data");

                for (int i = 0; i < arrayChapters.size(); i++) {
                    JsonObject chapter = (JsonObject) arrayChapters.get(i);

                    String id = "", volume = "", chapterVolume = "", title = "";
                    int pages = 0;


                    try {
                        id = chapter.getAsJsonPrimitive("id").getAsString();

                        JsonObject attributes = chapter.getAsJsonObject("attributes");
                        volume = attributes.getAsJsonPrimitive("volume").getAsString();
                        chapterVolume = attributes.getAsJsonPrimitive("chapter").getAsString();
                        title = attributes.getAsJsonPrimitive("title").getAsString();
                        pages = attributes.getAsJsonPrimitive("pages").getAsInt();

                    } catch (Exception e) {
                    }
                    ChapterModel chapterModel = new ChapterModel(id, volume, chapterVolume, title, pages);
                    chapterList.add(chapterModel);

                }


                if (response.code() == 200) {
                    mGetChapter.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void getChaptersUrl(ChapterModel chapter) {

            MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
            Call<JsonObject> call = api.getChaptersUrl(chapter.getId());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    pagesUrl.clear();
                    JsonObject object = response.body();
                    String hash = "", path = "";

                    try {
                        hash = object.getAsJsonObject("chapter").getAsJsonPrimitive("hash").getAsString();

                        JsonArray arrayPaths = object.getAsJsonObject("chapter").getAsJsonArray("data");
                        for (int j = 0; j < arrayPaths.size(); j++) {
                            path = (String) arrayPaths.get(j).getAsString();
                            pagesUrl.add("https://uploads.mangadex.org/data/" + hash + "/" + path);
                        }

                    } catch (Exception e) {
                    }

                    if(response.code() == 200){
                        mGetChapterUrl.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
    }


    public List<ChapterModel> getChaptersList() {
        return chapterList;
    }

    public List<String> getChapterUrl(){
        return pagesUrl;
    }

}
