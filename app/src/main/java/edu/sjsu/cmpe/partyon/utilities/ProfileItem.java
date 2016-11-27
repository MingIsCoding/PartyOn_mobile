package edu.sjsu.cmpe.partyon.utilities;

/**
 * Created by Ming on 11/26/16.
 */

public class ProfileItem {
    private int mIconID;
    private String mTitle;
    private int mId;
    public ProfileItem(int mId, String title, int mIcon) {
        this.mIconID = mIcon;
        this.mTitle = title;
        this.mId = mId;
    }

    public int getIconID() {
        return mIconID;
    }

    public void setIconID(int mIconID) {
        this.mIconID = mIconID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
