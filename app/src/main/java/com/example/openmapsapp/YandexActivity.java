package com.example.openmapsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class YandexActivity extends AppCompatActivity implements CameraListener, UserLocationObjectListener, View.OnClickListener, Session.SearchListener {

    private final String MAPKIT_API_KEY = "c1afc545-5c1e-48f2-99af-210cd8c7c768";
    private MapView mapView;
    private FloatingActionButton imageButton;
    private ImageButton searchButton;
    private EditText editText;
    private UserLocationLayer userLocationLayer;
    private LocationManager locationManager;
    private Point myPoint;
    private SearchManager searchManager;
    private List<GeoObjectCollection.Item> items;
    private Session session;
    private RecyclerView listView;
    BottomSheetBehavior behavior;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationUpdated(@NonNull Location location) {
            myPoint = location.getPosition();
            mapView.getMap().move(new CameraPosition(myPoint,18,0,0),new Animation(Animation.Type.SMOOTH,3),null);
        }

        @Override
        public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        final MapKit mapKit = MapKitFactory.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yandex_map);
        mapView = findViewById(R.id.yandex_maps);
        imageButton = findViewById(R.id.my_position_yandex);
        editText = findViewById(R.id.edit_query_yandex);
        searchButton = findViewById(R.id.search_button_yandex);
        listView = findViewById(R.id.recycler_address);
        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setDraggable(false);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        imageButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE);
        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().addCameraListener(this);
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        locationManager = MapKitFactory.getInstance().createLocationManager();
        locationManager.requestSingleUpdate(locationListener);
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    private void submitQuery(String query){
        if(!query.isEmpty()) {
            Log.e("MAP","submitQuery"+query);
            session = searchManager.submit(query, VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()), new SearchOptions(), this);
        }
        else{
            MapObjectCollection collection = mapView.getMap().getMapObjects();
            collection.clear();
        }
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_button_yandex:
                submitQuery(editText.getText().toString());
                break;
            case R.id.my_position_yandex:
                try {
                    mapView.getMap().move(new CameraPosition(myPoint,18,0,0));
                }catch (IllegalArgumentException e){
                    Log.e(" myPoint",e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onSearchResponse(@NonNull Response response) {
        MapObjectCollection collection = mapView.getMap().getMapObjects();
        collection.clear();
        items = response.getCollection().getChildren();
        for (GeoObjectCollection.Item item:response.getCollection().getChildren()){
            Log.e("Map",item.getObj().getName()+" "+ item.getObj().getDescriptionText()+" " );
            Point point = item.getObj().getGeometry().get(0).getPoint();
            if(point != null){
                collection.addPlacemark(point, ImageProvider.fromResource(this,R.drawable.search_layer_advert_pin_dust_default));
            }
        }
        listView.setAdapter(createAdapter());
        behavior.setDraggable(true);
        behavior.setPeekHeight(200,true);
    }

    @Override
    public void onSearchError(@NonNull Error error) {

    }

    @Override
    public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateReason cameraUpdateReason, boolean b) {
        if(b ){
            Log.e("YandexCameraListener","start"+editText.getText().toString()+"end");

            submitQuery(editText.getText().toString());
        }
    }

    private AdapterRecyclerGeoObject createAdapter(){
        AdapterRecyclerGeoObject object;
        if(myPoint!=null){
           object = new AdapterRecyclerGeoObject(this, items,myPoint);
        }else {
            object = new AdapterRecyclerGeoObject(this,items);
        }
     return object;
    }
//    @Override
//    public boolean onObjectTap(@NonNull GeoObjectTapEvent geoObjectTapEvent) {
//        GeoObjectSelectionMetadata selectionMetadata = geoObjectTapEvent.getGeoObject().getMetadataContainer().getItem(GeoObjectSelectionMetadata.class);
//        if(selectionMetadata != null){
//            mapView.getMap().selectGeoObject(selectionMetadata.getId(),selectionMetadata.getLayerId());
//        }
//        return selectionMetadata != null;
//    }
}
