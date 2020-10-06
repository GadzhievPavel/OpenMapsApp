package com.example.openmapsapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimatedImageDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.geometry.Circle;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.search_layer.PlacemarkIconType;
import com.yandex.runtime.image.AnimatedImageProvider;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.ui_view.ViewProvider;

import java.security.Provider;
import java.util.List;

public class AdapterRecyclerGeoObject extends RecyclerView.Adapter<AdapterRecyclerGeoObject.MyViewHolder> {

    Context context;
    private LayoutInflater layoutInflater;
    List<GeoObjectCollection.Item> items;
    Point myPoint;
    MapView mapView;

    public AdapterRecyclerGeoObject(Context context, List<GeoObjectCollection.Item> items, MapView mapView, Point point){
        this.context=context;
        this.layoutInflater= LayoutInflater.from(context);
        this.items = items;
        this.myPoint = point;
        this.mapView = mapView;
    }

    public AdapterRecyclerGeoObject(Context context,List<GeoObjectCollection.Item> items, MapView mapView){
        this.context=context;
        this.layoutInflater= LayoutInflater.from(context);
        this.items = items;
        this.mapView = mapView;
    }

    @NonNull
    @Override
    public AdapterRecyclerGeoObject.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.simple_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerGeoObject.MyViewHolder holder, int position) {
        final MapObjectCollection objectCollection = mapView.getMap().getMapObjects().addCollection();
        final GeoObject geoObject = items.get(position).getObj();
        Point point = geoObject.getGeometry().get(0).getPoint();
        holder.name.setText(geoObject.getName());
        holder.address.setText(geoObject.getDescriptionText());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Point point1 = geoObject.getGeometry().get(0).getPoint();
               mapView.getMap().move(new CameraPosition(point1,15,0,0),new Animation(Animation.Type.SMOOTH,4),null);
                //mapView.getMap().getMapObjects().addPlacemark(point1, ImageProvider.fromResource(context,R.drawable.my_marker));
               holder.linearLayout.setBackgroundColor(Color.argb(100,0,255,127));
               PlacemarkMapObject placemarkMapObject = objectCollection.addPlacemark(point1);
               placemarkMapObject.setIcon(ImageProvider.fromResource(context,R.drawable.center));
           }
        });
        if (null!=myPoint){
           holder.distance.setText(GeoCalculating.calcDistanceKm(myPoint,point));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView address;
        TextView distance;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.item_search);
            name = itemView.findViewById(R.id.name_organizatin);
            address = itemView.findViewById(R.id.address_organizaton);
            distance = itemView.findViewById(R.id.distance);
        }
    }
}
