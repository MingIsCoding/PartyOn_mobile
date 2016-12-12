package edu.sjsu.cmpe.partyon.entities;

import android.icu.text.TimeZoneFormat;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming on 10/19/16.
 */
@ParseClassName("Post")
public class Post extends ParseObject {
    public final static String POST_ID = "post_id";
    private User author;
    private String authorID;
    private Party party;
    private String partyID;
    private String textContent;
    private int mediaType;
    private String pathPic;
    private String pathVideo;
    private int viewCount;
    private List<Like> likes;
    private String trademarkID;
    private Trademark trademark;
    private String brandID;
    private int state;
    private List<Reply> replies;
    private ParseFile postPhotos;


    private ParseObject imageObject;
    private ParseObject imageFile;

    public String getAuthorID() {
        return getString("authorID");
    }

    public void setAuthorID(String authorID) {
        put("authorID",authorID);
    }

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

    public List<ParseObject> getLikesAsPObject(){
        return getList("likes");
    }
    public List<Like> getLikes() {

        return getList("likes");
    }

    public void setLikes(List<Like> likes) {
        likes.size();
        put("likes", likes);
    }
    public void addLike(Like like){
        List<Like> likes = getLikes();
        if(likes == null){
            likes = new ArrayList<Like>();
        }
        likes.add(like);
        setLikes(likes);
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

    public List<Reply> getReplies() {
        return getList("replies");
    }

    private void setReplies(List<Reply> replies) {
        System.out.println("save replies:"+ replies.size());
        put("replies", replies);
    }

    public void addReply(Reply r){
        List<Reply> replies = getReplies();
        if(replies == null){
            replies = new ArrayList<Reply>();
        }
        replies.add(r);
        this.setReplies(replies);
    }
}
