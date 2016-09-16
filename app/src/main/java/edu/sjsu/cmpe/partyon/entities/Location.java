package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ming on 8/29/16.
 */
@ParseClassName("Location")
public class Location extends ParseObject{
    private double longitude;
    private double latitude;
    private String country;
    private String city;
    private String street;
    private String name;
    private String address;

    public long getLongitude() {
        return getLong("longitude");
    }

    public void setLongitude(double longitude) {
        put("longitude",longitude);
    }

    public double getLatitude() {
        return getDouble("latitude");
    }

    public void setLatitude(double latitude) {
        put("latitude",latitude);
    }

    public String getCountry() {
        return getString("country");
    }

    public void setCountry(String country) {
        put("country",country);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
        put("city",city);
    }

    public String getStreet() {
        return getString("street");
    }

    public void setStreet(String street) {
        put("street",street);
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address){
        put("address",address);
    }
    public String getAddress(){
        return getString("address");
    }
}
