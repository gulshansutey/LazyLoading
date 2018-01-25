package com.technophile.lazyloading;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Technophile on 1/25/2018.
 */

public interface APiInterface {
    public static final String REST_URL = "https://api.flickr.com/";


    @GET("services/rest/?method=flickr.interestingness.getList")
    Call<FlickrModel> getPictures(@Query("api_key")String api_key, @Query("per_page")
            String per_page, @Query("page") String page, @Query("format")String format, @Query("nojsoncallback")String nojsoncallback);

}
