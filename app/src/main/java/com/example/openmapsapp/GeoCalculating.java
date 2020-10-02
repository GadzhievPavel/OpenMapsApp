package com.example.openmapsapp;

import android.util.Log;

import com.yandex.mapkit.geometry.Point;

public class GeoCalculating {
    public static String calcDistanceKm(Point point1, Point point2){
        double f1 = Math.toRadians(point1.getLatitude());
        double f2 = Math.toRadians(point2.getLatitude());
        double l1 = Math.toRadians(point1.getLongitude());
        double l2 = Math.toRadians(point2.getLongitude());
        double angle = Math.acos((Math.sin(f1)*Math.sin(f2))+(Math.cos(f1)*Math.cos(f2)*Math.cos(l2-l1)));
        String dist = String.valueOf(Math.toDegrees(angle)*111);
        return makeDistanceString(dist);
    }

    private static String makeDistanceString(String s){
        String temp="";
        if(s.charAt(0)=='0' && s.charAt(1) =='.'){
            Log.e("makeDistanceString","0.");

            for (int i = 2; i<6;i++){
                temp+=(String.valueOf(s.charAt(i)));
            }
            temp+=("m");
            s=temp;
        }else {
            s+=("km");
        }
        return s;
    }
}
