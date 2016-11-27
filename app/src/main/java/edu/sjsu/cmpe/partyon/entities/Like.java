package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ming on 10/19/16.
 */
@ParseClassName("Like")
public class Like extends ParseObject {
    private Post post;
    private User sender;
    private User receiver;
    public Post getPost(){
        return (Post)getParseObject("post");
    }
    public void setPost(Post post){
        put("post",post);
    }
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
