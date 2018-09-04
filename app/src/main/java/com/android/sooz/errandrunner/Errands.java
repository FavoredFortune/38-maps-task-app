package com.android.sooz.errandrunner;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class Errands {
    String description;
    LatLng start;
    LatLng end;
    String id;
    boolean isComplete;

    public Errands(){ }

    public Errands(String description, LatLng start, LatLng end){
        this.description = description;
        this.start = start;
        this.end = end;
        this.id = "";
    }


    //factory pattern - a method on a class that creates objects & returns them
    @NonNull
    public static Errands fromSnapshot(DataSnapshot snapshot){
        Errands errand = new Errands();

        errand.id = snapshot.getKey();
        errand.description = snapshot.child("description").getValue(String.class);
        DataSnapshot reff = snapshot.child("isComplete");
        boolean check = (boolean) reff.getValue();

        //testing both FirebaseDB approaches for getting value for child of child - both work
        float startLat = snapshot.child("start/lat").getValue(float.class);
        float startLong = snapshot.child("start").child("long").getValue(float.class);
        errand.start = new LatLng(startLat, startLong);

        float endLat = snapshot.child("end").child("lat").getValue(float.class);
        float endLong = snapshot.child("end").child("long").getValue(float.class);
        errand.end = new LatLng(endLat, endLong);

        return errand;
    }
}
