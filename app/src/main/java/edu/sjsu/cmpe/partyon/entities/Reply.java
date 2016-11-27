package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ming on 10/19/16.
 */
@ParseClassName("Reply")
public class Reply extends ParseObject {
    private User author;
    private String content;

    public User getAuthor() {
        return (User)getParseUser("author");
    }

    public void setAuthor(User author) {
        put("author",author);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content",content);
    }
}
