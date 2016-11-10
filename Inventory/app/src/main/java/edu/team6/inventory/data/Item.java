package edu.team6.inventory.data;

import java.io.Serializable;

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
    /** The serial number of the item. */
    private String mSerialNumber;
    /** The make of the item. */
    private String mMake;
    /** The model of the item. */
    private String mModel;

    /**
     * Constructs an item object - default constructor
     */
    public Item() {
        // do nothing
    }

    /**
     * Constructs an item with the given name.
     * @param theName the name of the item.
     */
    public Item (int theId, String theName) {
        this.mId = theId;
        this.mName = theName;
    }

    /**
     * Creates an Item with the given property values.
     * @param theId The id of the item.
     * @param theName The name of the item.
     * @param theValue The value of the item.
     * @param theCondition The condition of the item.
     * @param theDescription A description of the item.
     */
    public Item (int theId, String theName, double theValue, String theCondition, String theDescription) {
        this(theId, theName);
        this.mValue = theValue;
        this.mCondition = theCondition;
        this.mDescription = theDescription;
    }

    /**
     * Creates an Item with the given item property values with a default id of 0.
     * @param theName The name of the item.
     * @param theValue The value of the item.
     * @param theCondition The condition of the item.
     * @param theDescription A description of the item.
     */
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
