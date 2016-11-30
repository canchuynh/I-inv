package edu.team6.inventory.menu;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import edu.team6.inventory.activities.R;


/**
 * Fragment that handles the menu and its options.
 */
public class MenuFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {

    /** A constant used for signing in. */
    private static final int RC_SIGN_IN = 9001;

    /** Google API Client for google service (sign in and out). */
    private GoogleApiClient mGoogleApiClient;

    /** Google User ID */
    private String googleId = "";

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

        updateUserID();
        getActivity().invalidateOptionsMenu();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserID();
        getActivity().invalidateOptionsMenu();
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
    public void onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean signedIn = googleId.equals("");

        // Enable or Disable SignIn
        menu.findItem(R.id.signIn).setEnabled(signedIn);
        // Enable or Disable sign in required functions
        menu.findItem(R.id.logout).setEnabled(!signedIn);
        menu.findItem(R.id.importInv).setEnabled(!signedIn);
        menu.findItem(R.id.export).setEnabled(!signedIn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signIn:
                signIn();
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.logout:
                signOut();
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.export:
                // TODO: if no usererID stop export
                new CloudSync(googleId).export(getActivity()); //TODO: Rename to exportInv?
                return true;
            case R.id.importInv:
                new CloudSync(googleId).importInv(getActivity());
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


        } else {
            Toast.makeText(
                    this.getActivity().getApplicationContext(),
                    "Sign in failed.",
                    Toast.LENGTH_LONG)
                    .show();
        }
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
}
