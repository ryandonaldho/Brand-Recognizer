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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "MAINMENUACTIVITY";

    // Views on this Activity
    private Button signin_button;
    private EditText email_text;
    private EditText password_text;
    private TextView signup_text;

    protected void onCreate(Bundle savedInstanceState){
        // onCreate

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        signin_button = (Button)findViewById(R.id.signin_button);
        signin_button.setOnClickListener(this);
        email_text = (EditText)findViewById(R.id.email_text);
        password_text = (EditText)findViewById(R.id.password_text);
        signup_text = (TextView)findViewById(R.id.signup_text);
        signup_text.setOnClickListener(this);
        toggle_button(true);

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
            sign_in(email, password);
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


    private void sign_in(String email, String password){
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

}
