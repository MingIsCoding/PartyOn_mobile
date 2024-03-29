package edu.sjsu.cmpe.partyon.entities;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ming on 9/21/16.
 */
@ParseClassName("_User")
public class User extends ParseUser {
    public final static String ATT_USER_ID = "user_id";
    public final static String ATT_USER_USERNAME = "username";

    /*private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int gender;
    private String email;
    private String profilePicSmall;
    private String profilePicLarge;
    private String addressLocationID;
    private String currentLocationID;
    private int creditBalance;
    private String facebookID;
    private String googleID;
    private Date birthday;
    private Party ongoingParty;
    private List<User> follows;*/
    private float balance;
    private int points;

    public int getPoints() {
        if(getInt("points") == 0){
            return 90;
        }
        return getInt("points");
    }

    public void setPoints(int points) {
        put("points",points);
    }

    public float getBalance() {
        if(getString("balance") == null){
            return 0;
        }
        return Float.parseFloat(getString("balance"));
    }

    public void setBalance(float balance) {
        put("balance",balance);
    }

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

    public Party getOngoingParty() {
        return (Party)getParseObject("ongoingParty");
    }

    public void setOngoingParty(Party ongoingParty) {
        if(ongoingParty == null){
            remove("ongoingParty");
        }else {
            put("ongoingParty",ongoingParty);
        }
    }

    public List<User> getFollows() {
        return getList("follows");
    }
    public void addFollow(User user){
        List<User> list = getFollows();
        if(list == null){
            list = new ArrayList<User>();
        }
        list.add(user);
        setFollows(list);
    }
    public void setFollows(List<User> follows) {
        put("follows",follows);
    }
    public void unFollow(User user){
        List<User> list = getFollows();
        list.remove(user);
        setFollows(list);
        saveInBackground();
    }
    public void addPoints(final int points, final int credits){
        fetchInBackground(new GetCallback<User>() {
            @Override
            public void done(User u, ParseException e) {
                if(points != 0){
                    u.setPoints(u.getPoints() + points);
                }
                if(credits != 0){
                    u.setBalance(u.getBalance() + credits);
                }
            }
        });
    }
}
