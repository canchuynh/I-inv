package edu.team6.inventory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import edu.team6.inventory.utils.SessionManager;


/**
 * NOT USED DO NOT GRADE THIS CLASS.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    SessionManager manager;

    // UI Components Declared for this Activity
    public EditText mEmailField;
    public EditText mPasswordField;
    public Button mRegisterButton;
    public Button mLoginButton;
    public SignInButton mGoogleLoginButton;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connecting the UI components
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        manager = new SessionManager();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleLoginButton = (SignInButton) findViewById(R.id.googleLoginButton);
        mGoogleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        mGoogleLoginButton.setScopes(gso.getScopeArray());
    }


    public void signIn() {
        Toast.makeText(getApplicationContext(), "Login pressed", Toast.LENGTH_LONG).show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //Session management
            manager.setPreferences(LoginActivity.this, "status", "1");

            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast toast = Toast.makeText(getApplicationContext(), acct.toString(), Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
            finish();
        } else {
            manager.setPreferences(LoginActivity.this, "status", "0");
            // Signed out, show unauthenticated UI.
            Toast toast = Toast.makeText(getApplicationContext(), "Signed out/Failed", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleLoginButton:
                signIn();
                break;
        }
    }

}
