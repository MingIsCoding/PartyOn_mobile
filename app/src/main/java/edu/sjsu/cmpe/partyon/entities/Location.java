package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ming on 8/29/16.
 */
@ParseClassName("Location")
public class Location extends ParseObject{
    private long longitude;
    private long latitude;
    private String country;
    private String city;
    private String street;
    private String addressName;

    public long getLongitude() {
        return getLong("longitude");
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
        put("longitude",longitude);
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
}
