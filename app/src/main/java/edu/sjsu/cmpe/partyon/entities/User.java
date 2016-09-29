package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Ming on 9/21/16.
 */
public class User extends ParseUser {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int gender;
    private String profilePicSmall;
    private String profilePicLarge;
    private String addressLocationID;
    private String currentLocationID;
    private int creditBalance;
    private String facebookID;
    private String googleID;
    private Date birthday;


    public String getFirstName() {
        return getString("firstName");
    }

    public void setFirstName(String firstName) {
        put("firstName",firstName);
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String lastName) {
        put("lastName",lastName);
    }

    public int getGender() {
        return getInt("gender");
    }

    public void setGender(int gender) {
        put("gender",gender);
    }

    public String getProfilePicSmall() {
        return getString("profilePicSmall");
    }

    public void setProfilePicSmall(String profilePicSmall) {
        put("profilePicSmall",profilePicSmall);
    }

    public String getProfilePicLarge() {
        return getString("profilePicLarge");
    }

    public void setProfilePicLarge(String profilePicLarge) {
        put("profilePicLarge",profilePicLarge);
    }

    public String getAddressLocationID() {
        return getString("addressLocationID");
    }

    public void setAddressLocationID(String addressLocationID) {
        put("addressLocationID",addressLocationID);
    }

    public String getCurrentLocationID() {
        return getString("currentLocationID");
    }

    public void setCurrentLocationID(String currentLocationID) {
        put("currentLocationID",currentLocationID);
    }

    public void setState(int state) {
        put("state",state);
    }

    public int getCreditBalance() {
        return getInt("creditBalance");
    }

    public void setCreditBalance(int creditBalance) {
        put("creditBalance",creditBalance);
    }

    public String getGoogleID() {
        return getString("googleID");
    }

    public void setGoogleID(String googleID) {
        put("googleID",googleID);
    }

    public String getFacebookID() {
        return getString("facebookID");
    }

    public void setFacebookID(String facebookID) {
        put("facebookID",facebookID);
    }
    public void setBirthday(Date birthday){
        put("birthday",birthday);
    }
    public Date getBirthday(){
        return getDate("birthday");
    }
}