package com.example.openmapsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;

import java.util.List;

public class AdapterRecyclerGeoObject extends RecyclerView.Adapter<AdapterRecyclerGeoObject.MyViewHolder> {

    Context context;
    private LayoutInflater layoutInflater;
    List<GeoObjectCollection.Item> items;
    Point myPoint;
    public AdapterRecyclerGeoObject(Context context, List<GeoObjectCollection.Item> items, Point point){
        this.context=context;
        this.layoutInflater= LayoutInflater.from(context);
        this.items = items;
        this.myPoint = point;
    }

    public AdapterRecyclerGeoObject(Context context,List<GeoObjectCollection.Item> items){
        this.context=context;
        this.layoutInflater= LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public AdapterRecyclerGeoObject.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.simple_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerGeoObject.MyViewHolder holder, int position) {
       GeoObject geoObject = items.get(position).getObj();
       Point point = geoObject.getGeometry().get(0).getPoint();
       holder.name.setText(geoObject.getName());
       holder.address.setText(geoObject.getDescriptionText());
       if (null!=myPoint){
           holder.distance.setText(GeoCalculating.calcDistanceKm(myPoint,point)+"km");
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_organizatin);
            address = itemView.findViewById(R.id.address_organizaton);
            distance = itemView.findViewById(R.id.distance);
        }
    }
}
