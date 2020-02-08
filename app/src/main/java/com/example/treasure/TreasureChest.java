package com.example.treasure;

import com.google.android.gms.maps.model.LatLng;

public class TreasureChest {
    double latitude;
    double longitude;
    double ringLatitude;
    double ringLongitude;
    String hint = "";
    double radius;



    //geters and setters
public void setLatitude(double latitude){
    this.latitude=latitude;
}

public void setLongitude(double longitude)
{
    this.longitude=longitude;
}

public double getLatitude(){
    return latitude;
}

public double getLongitude() {
        return longitude;
    }

public LatLng getCoordiants(){
    LatLng coordinates = new LatLng(latitude, longitude);
    return coordinates;
    }

public double getRingLatitude() {
        return ringLatitude;
    }

 public double getRingLongitude() {
        return ringLongitude;
    }

 public void setRingLatitude(double ringLatitude) {
        this.ringLatitude = ringLatitude;
    }

 public void setRingLongitude(double ringLongitude) {
        this.ringLongitude = ringLongitude;
    }

public LatLng getRingCoordinates()
{
    LatLng coordinates = new LatLng(ringLatitude, ringLongitude);
    return coordinates;
}

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
