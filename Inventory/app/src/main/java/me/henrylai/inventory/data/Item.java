package me.henrylai.inventory.data;

import java.io.Serializable;

/**
 * Created by Starwater on 11/3/2016.
 */

public class Item implements Serializable {

    /** The id of the item. */
    private int mId;
    /** The name of the item. */
    private String mName;
    /** The value of the item. */
    private double mValue;
    /** The condition of the item. */
    private String mCondition;
    /** The description of the item. */
    private String mDescription;
    private String mSerialNumber;
    private String mMake;
    private String mModel;

    public Item() {

    }

    /**
     * Constructs an item with the given name.
     * @param theName the name of the item.
     */
    public Item (int theId, String theName) {
        this.mId = theId;
        this.mName = theName;
    }

    public Item (int theId, String theName, double theValue) {
        this(theId, theName);
        this.mValue = theValue;
    }

    public Item (int theId, String theName, double theValue, String theCondition) {
        this(theId, theName, theValue);
        this.mCondition = theCondition;
    }

    public Item (int theId, String theName, double theValue, String theCondition, String theDescription) {
        this(theId, theName, theValue, theCondition);
        this.mDescription = theDescription;
    }

    //no id
    public Item (String theName, double theValue, String theCondition, String theDescription) {
        this(0, theName, theValue, theCondition, theDescription);
    }

        public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmValue() {
        return mValue;
    }

    public void setmValue(double mValue) {
        this.mValue = mValue;
    }

    public String getmCondition() {
        return mCondition;
    }

    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmModel() {
        return mModel;
    }

    public void setmModel(String mModel) {
        this.mModel = mModel;
    }

    public String getmSerialNumber() {
        return mSerialNumber;
    }

    public void setmSerialNumber(String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }

    public String getmMake() {
        return mMake;
    }

    public void setmMake(String mMake) {
        this.mMake = mMake;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
