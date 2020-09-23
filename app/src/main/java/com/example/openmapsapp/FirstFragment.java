package com.example.openmapsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.openmapsapp.data.Feature;
import com.example.openmapsapp.data.Geometry;
import com.example.openmapsapp.data.Place;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class FirstFragment extends Fragment {

    private MapView mapView;
    private final String TAG = "Map";
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private final String KEY_BOX = "BOX";
    private BoundingBox boundingBox;
    private LocationManager locationManager;
    Place places;
    private IMapController mapController;
    private GeoPoint point;
    private Marker marker;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @SuppressLint({"MissingPermission", "UseCompatLoadingForDrawables"})
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        mapView = view.findViewById(R.id.map_view);
        view.findViewById(R.id.bubble_image);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setTilesScaledToDpi(true);
        mapView.setMinZoomLevel(4.0);
        mapView.setMaxZoomLevel(19.0);
        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        mapController = mapView.getController();
        mapController.setZoom(18.0);
        MapService.getInstance().getMapAPI().search("54, улица Стройкова, Сысоево, Железнодорожный район, Рязань, Рязанская область, Центральный федеральный округ, 390026, Россия").enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                places = response.body();
                for (Feature feature : places.getFeatures()) {
                    Geometry geometry = feature.getGeometry();
                    point = new GeoPoint(geometry.getCoordinates().get(1).doubleValue(),geometry.getCoordinates().get(0).doubleValue());
                    mapController.setCenter(point);
                    marker.setPosition(point);
                    marker.setTitle("Это здесь");
                    mapView.getOverlays().add(marker);
                }
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.e("MAPTEST",t.getMessage());
            }
        });

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*10,10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        point = new GeoPoint(54.9610646, 41.7551758);
        mapController.setCenter(point);
        marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle("Вы тут");
        mapView.getOverlays().add(marker);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
                  Log.e(TAG+"Location","latitude "+location.getLatitude()+" longitude "+ location.getLongitude());
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        boundingBox = mapView.getBoundingBox();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,boundingBox.toString());
        outState.putParcelable(KEY_BOX,boundingBox);
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}