package edu.sjsu.cmpe.partyon.entities;

import com.parse.ParseObject;

/**
 * Created by Ming on 10/19/16.
 */

public class Trademark extends ParseObject {
    private String name;
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getUri() {
        return getString("uri");
    }

    public void setUri(String uri) {
        put("uri", uri);
    }
}
