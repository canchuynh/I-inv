package me.henrylai.inventory.data;

/**
 * Created by Starwater on 11/3/2016.
 */

public class Item {

    /** The name of the item. */
    private String mName;
    /** The value of the item. */
    private double mValue;
    /** The condition of the item. */
    private String mCondition;
    /** The description of the item. */
    private String mDescription;

    /**
     * Constructs an item with the given name.
     * @param theName the name of the item.
     */
    public Item (String theName) {
        this.mName = theName;
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
}
