package edu.team6.inventory.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;

import edu.team6.inventory.R;
import edu.team6.inventory.activities.ViewItemsActivity;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;


/**
 * Fragment that handles the menu and its options.
 */
public class MenuFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {

    /** A constant used for signing in. */
    private static final int RC_SIGN_IN = 9001;

    /** URL for adding items to SQL server. */
    private final static String EXPORT_URL
            = "http://cssgate.insttech.washington.edu/~canhuynh/I-Inv/addItem.php?";

    /** Google API Client for google service (sign in and out). */
    private GoogleApiClient mGoogleApiClient;

    /** Required empty public constructor. */
    public MenuFragment() {
        // Required empty public constructor
    }


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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);

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
                export();
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
            Toast.makeText(this.getActivity().getApplicationContext(), "Welcome " + acct.getDisplayName() + "!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    this.getActivity().getApplicationContext(),
                    "Sign in failed.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Export SQLite database to a web server. Web server must be defined and running.
     */
    private void export() {
        SQLiteDBHandler dbHandler = new SQLiteDBHandler(this.getActivity());
        List<Item> inventory = dbHandler.getAllItems();

        // Export all items. One at a time.
        for (Item i : inventory) {
            new AddItemTask().execute(new String[]{buildCourseURL(i).toString()});
        }
    }

    /**
     * Builds URL for web service.
     * @param item Item to be converted to URL for web service.
     * @return URL string for web service.
     */
    private String buildCourseURL(Item item) {

        StringBuilder sb = new StringBuilder(EXPORT_URL);

        try {
            sb.append("id=");
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

}
