package com.example.openmapsapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Place {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("geocoding")
    @Expose
    private Geocoding geocoding;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geocoding getGeocoding() {
        return geocoding;
    }

    public void setGeocoding(Geocoding geocoding) {
        this.geocoding = geocoding;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
