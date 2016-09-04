package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ming on 8/10/16.
 */
@ParseClassName("Party")
public class Party extends ParseObject {
    private String name;
    private int scaleType; // house party
    private boolean is21;
    private String coverImg;
    private int accessType; // private, public, invite only
    private String description;//
    private String[] hostIDs;
    private String locationID;

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
//        this.name = name;
    }

    public int getScaleType() {
        return getInt("scaleType");
    }

    public void setScaleType(int scaleType) {
//        this.scaleType = scaleType;
        put("scaleType",scaleType);
    }

    public boolean is21() {
        return getBoolean("is21");
    }

    public void setIs21(boolean is21) {
        put("is21",is21);
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
//        this.accessType = accessType;
        put("accessType",accessType);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
//        this.description = description;
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
//        this.hostIDs = hostIDs;
    }

    public String getLocationID() {
        return getString("locationID");
    }

    public void setLocationID(String locationID) {
        put("locationID",locationID);
//        this.locationID = locationID;
    }
}
