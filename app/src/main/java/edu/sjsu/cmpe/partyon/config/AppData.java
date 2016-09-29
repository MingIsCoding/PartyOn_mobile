package edu.sjsu.cmpe.partyon.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.sjsu.cmpe.partyon.entities.Party;

/**
 * Created by Ming on 8/29/16.
 */
public class AppData implements Serializable{
    public static boolean isParseAdapterInitiated = false;
    public static String backendServerURL = "https://partyonbackend.herokuapp.com/parse/";//http://10.50.0.21:1337/parse/";
    public static String backendServerAppID = "PartyOn";
    public static boolean isUserLoggedin = false;


    public static Map<Object,String> objectPersistNameMap;
    public static final String OBJ_NAME_PARTY = "Party";
    public static final String OBJ_PARTY_ID = "party_id";
    public static final String OBJ_PARTY_NAME = "party_name";
    public static final String PUB_SESSION_TOKEN = "party_on_pub_token";
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    public AppData(){
//        initObjectPersistNameMap();

    }
/*    private static void initObjectPersistNameMap(){
        objectPersistNameMap = new HashMap<Object,String>();
        objectPersistNameMap.put(Party.class,"Party");
    }*/
}
