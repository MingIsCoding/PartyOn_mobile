package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseObject;

/**
 * Created by Ming on 10/19/16.
 */

public class Like extends ParseObject {
    private User sender;
    private User receiver;

    public User getSender() {
        return (User)getParseUser("sender");
    }

    public void setSender(User sender) {
        put("sender",sender);
    }

    public User getReceiver() {
        return (User)getParseUser("receiver");
    }

    public void setReceiver(User receiver) {
        put("receiver",receiver);
    }
}
