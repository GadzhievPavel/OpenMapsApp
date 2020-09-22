package com.example.openmapsapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MapService  {
    private static MapService instance;
    private static final String BASE_URL="https://nominatim.openstreetmap.org";
    private Retrofit retrofit;

    private MapService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit= new Retrofit.Builder().baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                addConverterFactory(ScalarsConverterFactory.create())
                .client(client).build();
    }

    public static MapService getInstance(){
        if(instance==null){
            instance = new MapService();
        }
        return instance;
    }

    public MapApiServer getMapAPI(){
        return retrofit.create(MapApiServer.class);
    }
}
