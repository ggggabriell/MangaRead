package com.example.mangareader.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangareader.domain.MangaDexAPI;
import com.example.mangareader.domain.RetrofitClient;
import com.example.mangareader.domain.model.ChapterModel;
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


    private List<ChapterModel> chapterList = new ArrayList<>();
    private int sizeChapters;

    public void getChapters(String id) {
        MangaDexAPI api = RetrofitClient.getRetrofitInstance().create(MangaDexAPI.class);
        Call<JsonObject> call = api.getChapters(id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    JsonObject object = response.body();
                    JsonArray arrayChapters = object.getAsJsonArray("data");


                    for (int i = 0; i < arrayChapters.size(); i++) {
                        JsonObject chapter = (JsonObject) arrayChapters.get(i);

                        String id = chapter.getAsJsonPrimitive("id").getAsString();
                        sizeChapters = arrayChapters.size();

                        String volume = "", chapterVolume = "BLABLA", title = "";
                        int pages = 0;


                        JsonObject attributes = chapter.getAsJsonObject("attributes");
                        volume = attributes.getAsJsonPrimitive("volume").getAsString();
                        chapterVolume = attributes.getAsJsonPrimitive("chapter").getAsString();
                        title = attributes.getAsJsonPrimitive("title").getAsString();
                        pages = attributes.getAsJsonPrimitive("pages").getAsInt();


                        ChapterModel chapterModel = new ChapterModel(id, volume, chapterVolume, title, pages);
                        chapterList.add(chapterModel);

                    }
                } catch (Exception e) {
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

    public int getChapterListSize() {
        return sizeChapters;
    }

    public List<ChapterModel> getChaptersList() {
        return chapterList;
    }

}
