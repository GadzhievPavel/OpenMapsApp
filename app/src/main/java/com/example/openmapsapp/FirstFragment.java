package com.example.openmapsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.openmapsapp.data.Place;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.security.Provider;
import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

public class FirstFragment extends Fragment implements View.OnClickListener {

    private MapView mapView;
    private final String TAG = "Map";
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private final String KEY_BOX = "BOX";
    private BoundingBox boundingBox;
    private LocationManager locationManager;
    Place places;
    private IMapController mapController;
    private ImageButton myLocation;
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
        setupMap();
        requestPermissionsIfNecessary(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        findBestPosition();
//        MapService.getInstance().getMapAPI().search("54, улица Стройкова, Сысоево, Железнодорожный район, Рязань, Рязанская область, Центральный федеральный округ, 390026, Россия").enqueue(new Callback<Place>() {
//            @Override
//            public void onResponse(Call<Place> call, Response<Place> response) {
//                places = response.body();
//                for (Feature feature : places.getFeatures()) {
//                    Geometry geometry = feature.getGeometry();
//                    point = new GeoPoint(geometry.getCoordinates().get(1).doubleValue(),geometry.getCoordinates().get(0).doubleValue());
//                    mapController.setCenter(point);
//                    marker.setPosition(point);
//                    marker.setTitle("Это здесь");
//                    mapView.getOverlays().add(marker);
//                }
//            }
//            @Override
//            public void onFailure(Call<Place> call, Throwable t) {
//                Log.e("MAPTEST",t.getMessage());
//            }
//        });

        getActivity().findViewById(R.id.my_position).setOnClickListener(this);

    }

    private void setupMap(){
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setTilesScaledToDpi(true);
        mapView.setMinZoomLevel(4.0);
        mapView.setMaxZoomLevel(19.0);
        mapController = mapView.getController();
        mapController.setZoom(18.0);
    }

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

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.e(TAG+"Location","latitude "+location.getLatitude()+" longitude "+ location.getLongitude());
            GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
            Log.e(TAG+"Location",point.toString());
            mapController.setCenter(point);
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setTitle("Вы тут");
            mapView.getOverlays().add(marker);
        }
    };

    @SuppressLint({"WrongConstant", "MissingPermission"})
    private void findBestPosition(){
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//        criteria.setCostAllowed(false);
//        String provider = locationManager.getBestProvider(criteria,false);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100 * 10, 5, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100 * 10, 5, locationListener);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_position:
                findBestPosition();
                break;
        }
    }
}