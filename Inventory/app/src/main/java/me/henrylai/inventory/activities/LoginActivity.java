package me.henrylai.inventory.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import me.henrylai.inventory.R;

public class LoginActivity extends AppCompatActivity {

    // UI Components Declared for this Activity
    public EditText mEmailField;
    public EditText mPasswordField;
    public Button mRegisterButton;
    public Button mLoginButton;
    public Button mGoogleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connecting the UI components
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mGoogleLoginButton = (Button) findViewById(R.id.googleLoginButton);
    }
}
