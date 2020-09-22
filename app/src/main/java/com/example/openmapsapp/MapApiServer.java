package com.example.openmapsapp;

import com.example.openmapsapp.data.Place;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapApiServer {

    @GET("search?format=geocodejson")
    Call<ArrayList<Place>> search(@Query("q") String query);

}
