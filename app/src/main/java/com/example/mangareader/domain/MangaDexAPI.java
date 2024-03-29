package com.example.mangareader.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MangaDexAPI {
//    @GET("/manga/801513ba-a712-498c-8f57-cae55b38cc92")
    @GET("/manga")
    Call<JsonObject> getManga();

    @GET("/cover/{mangaCover}/")
    Call<JsonObject> getCover(@Path("mangaCover") String mangaCover);

    @GET("/manga/")
    Call<JsonObject> searchManga(@Query("title") String search);

    @GET("/author/{author}")
    Call<JsonObject> getAuthor(@Path("author") String author);

    @GET("/chapter/")
    Call<JsonObject> getChapters(@Query("manga") String manga,
                                 @Query("limit") int limit);

    @GET("/at-home/server/{chapter}")
    Call<JsonObject> getChaptersUrl(@Path("chapter") String chapter);
}
