package com.example.openmapsapp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MapApiServer {

    @GET("search")
    Call<ArrayList<>> search();
}
