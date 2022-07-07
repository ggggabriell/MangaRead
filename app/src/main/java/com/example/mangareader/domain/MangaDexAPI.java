package com.example.mangareader.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MangaDexAPI {
//    @GET("/manga/801513ba-a712-498c-8f57-cae55b38cc92")
    @GET("/manga")
    Call<JsonObject> getManga();

    @GET("/cover/{mangaCover}/")
    Call<JsonObject> getCover(@Path("mangaCover") String mangaCover);


    @GET("/covers/{mangaCover}/")
    Call<JsonElement> getMangaImage(@Path("mangaCover") String mangaCover);

}
