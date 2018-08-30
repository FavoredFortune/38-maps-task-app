import com.google.android.gms.maps.model.LatLng;

public class Errands {


    String description;
    LatLng start;
    LatLng end;
    String id;
    boolean isComplete;

    public Errand(){

    }

    public Errand(String description, LatLng start, LatLng end){
        this.description = description;
        this.start = start;
        this.end = end;
        this.id = "";
    }

    //factory pattern - a method on a class that creates objects & returns them
    public static Errands fromSnapshot(DataSnapshot snapshot){
        Errands errand = new Errands();

        errand.id = snapshot.getKey();
        errand.description = snapshot.child("description").getValue(String.class);

        errand.isComplete = snapshot.child("isComplete").getValue(boolean);

        float startLat = snapshot.child("start").child("lat").getValue(float.class);
        float startLong = snapshot.child("start").child("long").getValue(float.class);

        errand.start = new LatLng(startLat, startLong);

        float endLat = snapshot.child("end").child("lat").getValue(float.class);
        float endLong = snapshot.child("end").child("long").getValue(float.class);

        errand.end = new LatLng(endLat, endLong);

        return errand;
    }
}
