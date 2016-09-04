package edu.sjsu.cmpe.partyon.entities;

import org.json.JSONObject;

/**
 * Created by Ming on 8/29/16.
 */
public interface PersistentEntityInterface {
    public String getPersistName();
    public String getJsonStr();
    public JSONObject getJsonObject();

}
