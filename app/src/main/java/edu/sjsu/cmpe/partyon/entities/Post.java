package edu.sjsu.cmpe.partyon.entities;

import android.icu.text.TimeZoneFormat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Arrays;

/**
 * Created by Ming on 10/19/16.
 */
@ParseClassName("Post")
public class Post extends ParseObject {
    private User author;
    private Party party;
    private String partyID;
    private String textContent;
    private int mediaType;
    private String pathPic;
    private String pathVideo;
    private int viewCount;
    private Like[] likes;
    private int likeCount;
    private String trademarkID;
    private Trademark trademark;
    private String brandID;
    private int state;
    private Reply[] replies;

    public User getAuthor() {
        return (User)getParseUser("author");
    }

    public void setAuthor(User author) {
        put("author",author);
    }

    public Party getParty() {
        return (Party)getParseObject("party");
    }

    public void setParty(Party party) {
        put("party",party);
    }

    public String getPartyID() {
        return getString("partyID");
    }

    public void setPartyID(String partyID) {
        put("partyID", partyID);
    }

    public String getTextContent() {
        return getString("textContent");
    }

    public void setTextContent(String textContent) {
        put("textContent", textContent);
    }

    public int getMediaType() {
        return getInt("mediaType");
    }

    public void setMediaType(int mediaType) {
        put("mediaType", mediaType);
    }

    public String getPathPic() {
        return getString("pathPic");
    }

    public void setPathPic(String pathPic) {
        put("pathPic",pathPic);
    }

    public String getPathVideo() {
        return getString("pathVideo");
    }

    public void setPathVideo(String pathVideo) {
        put("pathVideo", pathVideo);
    }

    public int getViewCount() {
        return getInt("viewCount");
    }

    public void setViewCount(int viewCount) {
        put("viewCount", viewCount);
    }

    public int getLikeCount() {
        return getInt("likeCount");
    }

    public void setLikeCount(int likeCount) {
        put("likeCount", likeCount);
    }


    public String getBrandID() {
        return getString("brandID");
    }

    public void setBrandID(String brandID) {
        put("brandID" , brandID);
    }

    public int getState() {
        return getInt("state");
    }

    public void setState(int state) {
        put("state", state);
    }

    public Like[] getLikes() {

        return (Like[])Arrays.asList(getParseObject("likes")).toArray();
    }

    public void setLikes(Like[] likes) {
        put("likes", likes);
    }

    public String getTrademarkID() {
        return getString("trademarkID");
    }

    public void setTrademarkID(String trademarkID) {
        put("trademarkID", trademarkID);
    }

    public Trademark getTrademark() {
        return (Trademark)getParseObject("trademark");
    }

    public void setTrademark(Trademark trademark) {
        put("trademark", trademark);
    }

    public Reply[] getReplies() {
        return (Reply[])Arrays.asList(getParseObject("replies")).toArray();
    }

    public void setReplies(Reply[] replies) {
        put("replies", replies);
    }
}
