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

    /** Gets the item name. */
    public String getmName() {
        return mName;
    }

    /** Sets the item name. */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /** Gets the item value. */
    public double getmValue() {
        return mValue;
    }

    /** Sets the item value. */
    public void setmValue(double mValue) {
        this.mValue = mValue;
    }

    /** Gets the item condition. */
    public String getmCondition() {
        return mCondition;
    }

    /** Sets the item condition. */
    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    /** Gets the item description. */
    public String getmDescription() {
        return mDescription;
    }

    /** Sets the item description. */
    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    /** Gets the item model. */
    public String getmModel() {
        return mModel;
    }

    /** Sets the item model. */
    public void setmModel(String mModel) {
        this.mModel = mModel;
    }

    /** Gets the item serial number. */
    public String getmSerialNumber() {
        return mSerialNumber;
    }

    /** Sets the item serial number. */
    public void setmSerialNumber(String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }

    /** Gets the item make. */
    public String getmMake() {
        return mMake;
    }

    /** Sets the item make. */
    public void setmMake(String mMake) {
        this.mMake = mMake;
    }

    /** Gets the item id. */
    public int getmId() {
        return mId;
    }

    /** Gets the item id. */
    protected void setmId(int ID) {
        this.mId = ID;
    }
}
