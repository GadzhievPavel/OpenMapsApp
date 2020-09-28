package com.example.openmapsapp;

import com.example.openmapsapp.data.Place;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapApiServer {

    @GET("search?format=geocodejson&accept-language=ru")
    Call<Place> search(@Query("q") String query);

}
