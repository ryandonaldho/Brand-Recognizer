package brand.brandrecognizer;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "MAINMENUACTIVITY";
    private static final int RC_SIGN_IN = 9001;

    // Views on this Activity
    private Button signin_button;
    private EditText email_text;
    private EditText password_text;
    private TextView signup_text;
    private SignInButton google_signin_button;

    //google signin objects
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    //facebook signin objects
    private LoginButton facebook_signin_button;

    //Twitter login objects
    private TwitterLoginButton twitter_signin_button;


    CallbackManager mCallbackManager;

    protected void onCreate(Bundle savedInstanceState){
        // onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        Twitter.initialize(this);

        signin_button = (Button)findViewById(R.id.signin_button);
        signin_button.setOnClickListener(this);
        email_text = (EditText)findViewById(R.id.email_text);
        password_text = (EditText)findViewById(R.id.password_text);
        signup_text = (TextView)findViewById(R.id.signup_text);
        signup_text.setOnClickListener(this);

        google_signin_button = (SignInButton)findViewById(R.id.google_login_button);
        google_signin_button.setOnClickListener(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        toggle_button(true);

        facebook_signin_button = (LoginButton)findViewById(R.id.facebook_login_button);
        facebook_signin_button.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        facebook_signin_button.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
        });

        twitter_signin_button = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        twitter_signin_button.setEnabled(true);
        twitter_signin_button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }// End onCreate

    public void onClick(View v) {
        // when some object on the activity is clicked

        if(v.getId() == R.id.signin_button)
        {
            toggle_button(false);
            String email = email_text.getText().toString();
            String password = password_text.getText().toString();
            brandrecog_sign_in(email, password);
        }
        else if(v.getId() == R.id.signup_text && signin_button.isEnabled())
        {// change to the CreateAccount activity the user clicks to sign up
            Intent i = new Intent(this, CreateAccount.class);
            startActivity(i);
        }

    }// End onClick

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void brandrecog_sign_in(String email, String password){
        // log into firebase if the user has an account
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()){
                            Toast.makeText(MainMenuActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            toggle_button(true);
                        }
                        else if (task.isSuccessful()){
                            Toast.makeText(MainMenuActivity.this, "success", Toast.LENGTH_SHORT).show();
                            toggle_button(true);
                            switch_to_mainactivity();
                        }

                    }
        });
    }// End sign_in

    private void google_sign_in() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void google_firebaseAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        switch_to_mainactivity();
                    }
                    else {
                            // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(MainMenuActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                    }

                }
        });

    }


    private void toggle_button(boolean enable){
        // toggles the log in time
        if(enable)
        {
            signin_button.setEnabled(true); // disable button while the system is attempting to log in
            signin_button.setText("Sign In");
            signup_text.setTextColor(Color.parseColor("#000000"));
        }
        else
        {
            signin_button.setEnabled(false); // disable button while the system is attempting to log in
            signin_button.setText("Logging in...");
            signup_text.setTextColor(Color.parseColor("#666666"));
        }

    }// End toggle_button

    private void switch_to_mainactivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        twitter_signin_button.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            google_firebaseAuth(account);
            Log.w(TAG, "signIn: success");

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    switch_to_mainactivity();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(MainMenuActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    switch_to_mainactivity();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(MainMenuActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
