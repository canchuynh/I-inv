package edu.team6.inventory.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.team6.inventory.activities.InventoryActivity;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

/**
 * Created by Can on 11/29/2016.
 */

public class CloudSync {

    /** URL for adding items to SQL server. */
    private final static String EXPORT_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/addItem.php?";

    /** URL for adding items to SQL server. */
    private final static String IMPORT_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/list.php?";

    /** URL for creating tables to SQL server. */
    private final static String CREATE_TABLE_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/createTable.php?";

    /** URL for Dropping the table. */
    private final static String DROP_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/dropTable.php?";

    /** UserID from google sign in. */
    private static String googleId;

    public CloudSync(String googleId) {
        this.googleId = googleId;
        // Creates a new table for the user in the web server.
        new AddItemTask().execute(CREATE_TABLE_URL + "userId=" + this.googleId);
    }

    /**
     * Export SQLite database to a web server. Web server must be defined and running.
     *
     * @param activity the activity calling export
     */
    protected static void export(Activity activity) {

        // Drops table before exporting.
        new AddItemTask().execute(DROP_URL + "userId=" + googleId);

        SQLiteDBHandler dbHandler = new SQLiteDBHandler(activity);

        List<Item> inventory = dbHandler.getAllItems("None");

        // Export all items. One at a time.
        for (Item i : inventory) {
            new AddItemTask().execute(buildAddItemURL(activity, i));
        }

        Toast.makeText(
                activity.getApplicationContext(),
                "Inventory Exported!",
                Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Export SQLite database to a web server. Web server must be defined and running.
     *
     * @param activity the activity calling import
     */
    protected static void importInv(Activity activity) {
        //TODO: See if ID counter increments.
        new DownloadInventoryTask(activity).execute(IMPORT_URL + "userId=" +  googleId);
    }

    /**
     * Builds URL for web service.
     * @param item Item to be converted to URL for web service.
     * @return URL string for web service.
     */
    private static String buildAddItemURL(Activity activity , Item item) {

        StringBuilder sb = new StringBuilder(EXPORT_URL);

        try {
            sb.append("userId=");
            sb.append(googleId);

            sb.append("&id=");
            sb.append(item.getmId());

            sb.append("&name=");
            sb.append(URLEncoder.encode(item.getmName(), "UTF-8"));

            sb.append("&value=");
            sb.append(item.getmValue());

            sb.append("&condition=");
            sb.append(URLEncoder.encode(item.getmCondition(), "UTF-8"));

            sb.append("&description=");
            sb.append(URLEncoder.encode(item.getmDescription(), "UTF-8"));
        } catch(Exception e) {
            Toast.makeText(
                    activity,
                    "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }


    /**
     * AsyncTask class for adding items to web server.
     */
    private static class AddItemTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to export, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String status = (String) jsonObject.get("result");
//                if (status.equals("success")) {
//                    Toast.makeText(this.getClass()getApplicationContext(), "Course successfully added!"
//                            , Toast.LENGTH_LONG)
//                            .show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Failed to add: "
//                                    + jsonObject.get("error")
//                            , Toast.LENGTH_LONG)
//                            .show();
//                }
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
//                        e.getMessage(), Toast.LENGTH_LONG).show();
//            }
        }
    }

    /**
     * AsyncTask class for downloading items from web server.
     */
    private static class DownloadInventoryTask extends AsyncTask<String, Void, String> {

        private SQLiteDBHandler dbHandler;
        private List<Item> inventory;
        private Activity activity;

        public DownloadInventoryTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download inventory: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            inventory = new ArrayList<>();
            result = Item.parseCourseJSON(result, inventory);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!inventory.isEmpty()) {

                if (dbHandler == null) {
                    dbHandler = new SQLiteDBHandler(activity);
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                dbHandler.deleteAllItems();

                // Also, add to the local database
                for (int i = 0; i < inventory.size(); i++) {
                    Item item = inventory.get(i);
                    dbHandler.addItem(item);
                }

                // Refresh Inventory list.
                Intent intent = new Intent(activity, InventoryActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }
}
