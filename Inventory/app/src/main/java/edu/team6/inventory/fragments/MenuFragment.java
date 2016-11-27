package edu.team6.inventory.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.team6.inventory.R;
import edu.team6.inventory.activities.InventoryActivity;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;


/**
 * Fragment that handles the menu and its options.
 */
public class MenuFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {

    /** A constant used for signing in. */
    private static final int RC_SIGN_IN = 9001;

    /** URL for creating tables to SQL server. */
    private final static String CREATE_TABLE_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/createTable.php?";

    /** URL for Dropping the table. */
    private final static String DROP_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/dropTable.php?";

    /** URL for adding items to SQL server. */
    private final static String EXPORT_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/addItem.php?";

    /** URL for adding items to SQL server. */
    private final static String IMPORT_URL
            = "http://cssgate.insttech.washington.edu/~_450team6/I-Inv/list.php?";

    /** Google API Client for google service (sign in and out). */
    private GoogleApiClient mGoogleApiClient;

    /** Google User ID */
    private String googleId = "";

    /** Required empty public constructor. */
    public MenuFragment() {
        // Required empty public constructor
    }

    /** Tag for logging. */
    private static final String TAG = "MenuFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .enableAutoManage(this.getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        updateUserID();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserID();
    }

    /**
     * Checks shared pref to see if user ID is present, if not set ID to "".
     */
    private void updateUserID() {
        SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
        googleId = sharedPref.getString(getString(R.string.userId), ""); // Get user ID if exist

//        if (googleId.equals("")) signOut(); //TODO: check if google services are running first.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signIn:
                signIn();
                return true;
            case R.id.logout:
                signOut();
                return true;
            case R.id.export:
                // TODO: if no usererID stop export
                export(); //TODO: Rename to exportInv?
                return true;
            case R.id.importInv:
                importInv();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Signs use in with Google Sign In.
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Signs user out from Google.
     */
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //Remove User ID to shared pref.
                        SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
                        sharedPref.edit().putString(getString(R.string.userId), "").commit();
                        // Update User ID.
                        updateUserID();
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Logged out!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Handles the results (Success/Failure) of singing in.
     *
     * @param result Result of signing in.
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            //Save User ID to shared pref.
            SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
            sharedPref.edit().putString(getString(R.string.userId), acct.getId().toString()).commit();
            updateUserID();

            Toast.makeText(
                    this.getActivity().getApplicationContext(),
                    "Welcome " + acct.getDisplayName() + "!",
                    Toast.LENGTH_LONG)
                    .show();

            createTable();
        } else {
            Toast.makeText(
                    this.getActivity().getApplicationContext(),
                    "Sign in failed.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Creates table in web server if one does not already exist. Web server must be defined and running.
     */
    private void createTable() {
        new AddItemTask().execute(CREATE_TABLE_URL + "userId=" + googleId);
    }

    /**
     * Export SQLite database to a web server. Web server must be defined and running.
     */
    private void export() {

        // Drops table before exporting.
        new AddItemTask().execute(DROP_URL + "userId=" + googleId);

        SQLiteDBHandler dbHandler = new SQLiteDBHandler(this.getActivity());

        List<Item> inventory = dbHandler.getAllItems();

        // Export all items. One at a time.
        for (Item i : inventory) {
            new AddItemTask().execute(buildAddItemURL(i).toString());
        }

        Toast.makeText(
                this.getActivity().getApplicationContext(),
                "Inventory Exported!",
                Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Export SQLite database to a web server. Web server must be defined and running.
     */
    private void importInv() {
        //TODO: See if ID counter increments.
        new DownloadInventoryTask().execute(IMPORT_URL + "userId=" +  googleId);
        Log.d(TAG, IMPORT_URL + "userId=" + googleId);
    }

    /**
     * Builds URL for web service.
     * @param item Item to be converted to URL for web service.
     * @return URL string for web service.
     */
    private String buildAddItemURL(Item item) {

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
                    this.getView().getContext(),
                    "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
        Toast.makeText(
                this.getView().getContext(),
                "Unable to establish connection." ,
                Toast.LENGTH_LONG)
                .show();
    }


    /**
     * AsyncTask class for adding items to web server.
     */
    private class AddItemTask extends AsyncTask<String, Void, String> {

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
    private class DownloadInventoryTask extends AsyncTask<String, Void, String> {

        private SQLiteDBHandler dbHandler;
        private List<Item> inventory;

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
                    Log.e(TAG, e.getMessage());
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
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            inventory = new ArrayList<>();
            result = Item.parseCourseJSON(result, inventory);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!inventory.isEmpty()) {

                if (dbHandler == null) {
                    dbHandler = new SQLiteDBHandler(getActivity());
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
                Intent intent = new Intent(getContext(), InventoryActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}
