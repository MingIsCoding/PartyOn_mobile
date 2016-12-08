package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Ming on 12/7/16.
 */
@ParseClassName("Transaction")
public class Transaction extends ParseObject {
    private String retailerID;
    private String amount;
    private String location;
    private String userID;
    private String items;
    private int state;

    public String getRetailerID() {
        return getString("retailerID");
    }

    public void setRetailerID(String retailerID) {
        put("retailerID",retailerID);
    }

    public String getAmount() {
        return getString("amount");
    }

    public void setAmount(String amount) {
        put("amount",amount);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location",location);
    }

    public String getUserID() {
        return getString("userID");
    }

    public void setUserID(String userID) {
        put("userID",userID);
    }

    public String getItems() {
        return getString("items");
    }

    public void setItems(String items) {
        put("items",items);
    }

    public int getState() {
        return getInt("state");
    }

    public void setState(int state) {
        put("state",state);
    }
}
