package edu.sjsu.cmpe.partyon.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.sjsu.cmpe.partyon.entities.Party;

/**
 * Created by Ming on 8/29/16.
 */
public class AppData {
    public static boolean isParseAdapterInitiated = false;
    public static String backendServerURL = "http://642dceed.ngrok.io/parse/";//http://10.50.0.21:1337/parse/";
    public static String backendServerAppID = "PartyOn";


    public static Map<Object,String> objectPersistNameMap;

    public AppData(){
        initObjectPersistNameMap();

    }
    private static void initObjectPersistNameMap(){
        objectPersistNameMap = new HashMap<Object,String>();
        objectPersistNameMap.put(Party.class,"Party");
    }
}
