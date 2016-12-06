package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ming on 11/20/16.
 */
@ParseClassName("Ticket")
public class Ticket extends ParseObject {
    public final static int STATE_REQUESTED = 0;
    public final static int STATE_INVITED = 1;
    public final static int STATE_UNCHECKED_IN = 2;
    public final static int STATE_CHECKED_IN = 3;
    // when a person quits before the party ends
    public final static int STATE_LEFT = 4;
    public final static int STATE_DONE = 5;
    public final static int STATE_MSG_NOTIFIED = 1;
    public final static int STATE_MSG_UNNOTIFIED = 0;
    public final static int STATE_MSG_READ = 2;
    private String partyID;
    private int msgState;


    public int getMsgState() {
        return getInt("msgState");
    }

    public void setMsgState(int msgState) {
        put("msgState",msgState);
    }

    public String getSenderID() {
        return getString("senderID");
    }

    public void setSenderID(String senderID) {
        put("senderID",senderID);
    }

    public Party getParty() {
        return (Party)getParseObject("party");
    }

    public void setParty(Party party) {
        put("party",party);
    }

    public User getReceiver() {
        return (User)getParseObject("receiver");
    }

    public void setReceiver(User receiver) {
        put("receiver",receiver);
    }

    public User getSender() {
        return (User)getParseObject("sender");
    }

    public void setSender(User sender) {
        put("sender",sender);
    }


    public int getState() {
        return getInt("state");
    }

    public void setState(int state) {
        put("state",state);
    }

    public String getMsg() {
        return getString("msg");
    }

    public void setMsg(String msg) {
        put("msg",msg);
    }

    public String getPartyID() {
        return getString("partyID");
    }

    public void setPartyID(String partyID) {
        put("partyID",partyID);
    }

    public String getReceiverID() {
        return getString("receiverID");
    }

    public void setReceiverID(String receiverID) {
        put("receiverID",receiverID);
    }
}
