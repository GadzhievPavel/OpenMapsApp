package com.example.openmapsapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {
    @SerializedName("geocoding")
    @Expose
    private Geocoding geocoding;

    public Geocoding getGeocoding() {
        return geocoding;
    }

    public void setGeocoding(Geocoding geocoding) {
        this.geocoding = geocoding;
    }

}
