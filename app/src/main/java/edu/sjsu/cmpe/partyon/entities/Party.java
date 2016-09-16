package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Ming on 8/10/16.
 */
@ParseClassName("Party")
public class Party extends ParseObject {
    private String name;
    private int scaleType; // house party
    private int ageRangeStart;
    private int ageRangeEnd;
    private String coverImg;
    private int accessType; // private, public, invite only
    private String description;//
    private String[] hostIDs;
    private String locationID;
    private String address;
    private Date StartDateTime;
    private Date EndDateTime;
    private int capacityRangeStart;
    private int capacityRangeEnd;
    private String[] drinkTypes;
    private int state;
//    private String parking;


    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        this.name = name;
        put("name", name);
    }

    public int getScaleType() {
        return getInt("scaleType");
    }

    public void setScaleType(int scaleType) {
        put("scaleType",scaleType);
    }


    public String getCoverImg() {
        return getString("coverImg");
    }

    public void setCoverImg(String coverImg) {
        put("coverImg",coverImg);
    }

    public int getAccessType() {
        return getInt("accessType");
    }

    public void setAccessType(int accessType) {
        put("accessType",accessType);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }


    public String[] getHostIDs() {
        JSONArray jsonArray = getJSONArray("hostIDs");
        String[] stringsArray = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                stringsArray[i] = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringsArray;
    }

    public void setHostIDs(String[] hostIDs) {
        put("hostIDs",hostIDs);
    }

    public String getLocationID() {
        return getString("locationID");
    }

    public void setLocationID(String locationID) {
        put("locationID",locationID);
    }

    public int getAgeRangeStart() {

        return getInt("ageRangeStart");
    }

    public void setAgeRangeStart(int ageRangeStart) {
        this.put("ageRangeStart",ageRangeStart);
    }

    public int getAgeRangeEnd() {
        return getInt("ageRangeEnd");
    }

    public void setAgeRangeEnd(int ageRangeEnd) {
        this.put("ageRangeEnd",ageRangeEnd);
    }

    public Date getStartDateTime() {
        return getDate("startDateTime");
    }

    public void setStartDateTime(Date startDateTime) {
        put("startDateTime", startDateTime);
    }

    public Date getEndDateTime() {
        return getDate("endDateTime");
    }

    public void setEndDateTime(Date endDateTime) {
        put("endDateTime",endDateTime);
    }

    public int getCapacityRangeStart() {
        return getInt("capacityRangeStart");
    }

    public void setCapacityRangeStart(int capacityRangeStart) {
        this.put("capacityRangeStart",capacityRangeStart);
    }

    public int getCapacityRangeEnd() {
        return getInt("capacityRangeEnd");
    }

    public void setCapacityRangeEnd(int capacityRangeEnd) {
        put("capacityRangeEnd",capacityRangeEnd);
    }

    public String[] getDrinkTypes() {
        JSONArray jsonArray = getJSONArray("drinkTypes");
        String[] stringsArray = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                stringsArray[i] = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringsArray;
    }

    public void setDrinkTypes(String[] drinkTypes) {
        this.put("drinkTypes",drinkTypes);
    }
    public void setState(int state){
        put("state",state);
    }
    public int getState(){
        return getInt("state");
    }
    public void setAddress(String address){
        put("address",address);
    }
    public String getAddress(){
        return getString("address");
    }
}
