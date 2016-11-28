package edu.team6.inventory.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public void setPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("inventory", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        String status = prefs.getString(key, "");
        return status;
    }
}
