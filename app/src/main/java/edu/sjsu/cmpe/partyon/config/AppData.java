package edu.sjsu.cmpe.partyon.config;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.Ticket;
import edu.sjsu.cmpe.partyon.entities.User;

public class AppData implements Serializable{
    public static boolean isDevMode = false;    // put true to get the dev mode(no login)
    public static boolean isParseAdapterInitiated = false;
    public static String backendServerURL = "https://partyonbackend.herokuapp.com/parse/";//http://10.50.0.21:1337/parse/";
    public static String backendServerAppID = "PartyOn";
    public static boolean isUserLoggedin = false;
    private static User currentUser;

    public static Map<Object,String> objectPersistNameMap;
    public static final String OBJ_NAME_USER = "_User";
    public static final String OBJ_NAME_PARTY = "Party";
    public static final String OBJ_NAME_TICKET = "Ticket";
    public static final String OBJ_NAME_TRANSACTION = "Transaction";
    public static final String OBJ_NAME_LIKE = "Like";
    public static final String OBJ_PARTY_ID = "party_id";
    public static final String OBJ_PARTY_NAME = "party_name";
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static User getUser(){
        if(currentUser == null){
            currentUser = (User) User.getCurrentUser();
        }
        return currentUser;

    }
    public AppData(){
//        initObjectPersistNameMap();

    }
/*    private static void initObjectPersistNameMap(){
        objectPersistNameMap = new HashMap<Object,String>();
        objectPersistNameMap.put(Party.class,"Party");
    }*/
    public static Set<User> selectedPeople;
    public static List<Ticket> messageList;
    public static Ticket getTicketFromMsgListByID(String id){
        for(Ticket t: messageList){
            if(t.getObjectId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    public final static int POINT_NEW_POST = 20;
    public final static int POINT_NEW_TAG_POST = 40;
    public final static int POINT_FOLLOW = 10;
    public final static int POINT_CONSUME = 30;
    public final static int CREDIT_TAG_POST = 30;


    public static void addPoints(final int points, final int credits){
        currentUser.fetchInBackground(new GetCallback<User>() {
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
