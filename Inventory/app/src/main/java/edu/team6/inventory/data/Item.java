package edu.team6.inventory.data;

import android.util.EventLogTags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

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
    /** The image of the item. */
    private byte[] mImage;

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

    /** Sets the item name.
     *  @param mName the name of the item. */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /** Gets the item value. */
    public double getmValue() {
        return mValue;
    }

    /** Sets the item value.
     *  @param mValue the value of the item. */
    public void setmValue(double mValue) {
        this.mValue = mValue;
    }

    /** Gets the item condition. */
    public String getmCondition() {
        return mCondition;
    }

    /** Sets the item condition.
     *  @param mCondition the condition of the item. */
    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    /** Gets the item description. */
    public String getmDescription() {
        return mDescription;
    }

    /** Sets the item description.
     *  @param mDescription the description of the item. */
    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    /** Gets the item id. */
    public int getmId() { return mId; }

    /** Sets the item id.
     *  @param ID the id of the item. */
    protected void setmId(int ID) {
        this.mId = ID;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     *
     * @param inventoryJSON JSON containing the inventory.
     * @param inventory List of all items.
     * @return reason or null if successful.
     */
    public static String parseCourseJSON(String inventoryJSON, List<Item> inventory) {

            String reason = null;
            if (inventoryJSON != null) {
                try {
                    JSONArray arr = new JSONArray(inventoryJSON);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

                        Item item = new Item(
                                        obj.getInt("ID"),
                                        obj.getString("Name"),
                                        obj.getDouble("Value"),
                                        obj.getString("State"),
                                        obj.getString("Description"));
                        inventory.add(item);
                    }
                } catch (JSONException e) {
                    reason =  "Unable to parse data, Reason: " + e.getMessage();
                }

            }
            return reason;
    }

    /**
     * Sets the image of the item as a byte array.
     * @param mImage the image of the item as a byte array.
     */
    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }

    /**
     * Returns the image of the item.
     * @return The image of the item as a byte array.
     */
    public byte[] getmImage() {
        return this.mImage;
    }

}
