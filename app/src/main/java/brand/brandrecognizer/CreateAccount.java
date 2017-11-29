package brand.brandrecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class CreateAccount extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "CREATEACCOUNT";

    //Views on this activity
    private Button createaccount_button;
    private EditText email_text;
    private EditText password_text;
    private EditText passwordcheck_text;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        createaccount_button = (Button)findViewById(R.id.createaccount_button);
        createaccount_button.setOnClickListener(this);
        email_text = (EditText)findViewById(R.id.email_text);
        password_text = (EditText)findViewById(R.id.password_text);
        passwordcheck_text = (EditText)findViewById(R.id.passwordcheck_text);


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

    }


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

    public void onClick(View v){

        if(v.getId() == R.id.createaccount_button){

            toggle_button(false);
            String email = email_text.getText().toString();
            String password = password_text.getText().toString();
            String passwordcheck = passwordcheck_text.getText().toString();

            if(password.equals(passwordcheck))
            {
                create_account(email, password);
            }
            else
            {
                Toast.makeText(CreateAccount.this, "Password does not match", Toast.LENGTH_SHORT).show();
                toggle_button(true);
            }

        }
    }

    private void create_account(String email, String password){
        // creates the account using the input email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "failed", Toast.LENGTH_SHORT).show();
                            toggle_button(true);
                        }
                        else if (task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "success", Toast.LENGTH_SHORT).show();
                            toggle_button(true);
                            // create user field with the person's uid
                            Toast.makeText(CreateAccount.this,  mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            String uuid = mAuth.getCurrentUser().getUid();
                            UserInfo userInfo = new UserInfo();
                            userInfo.setEmail(mAuth.getCurrentUser().getEmail());
                            userInfo.setUserName(mAuth.getCurrentUser().getDisplayName());
                            myRef.child(uuid).setValue(userInfo);
                            HashMap hm = new HashMap();
                            hm.put("Name","1");
                            hm.put("Ke","1");
                            myRef.child(uuid).child("searches").push().setValue("test");
                            myRef.child(uuid).child("searches").push().setValue("test1");
                            switch_to_mainmenu();

                        }
                    }

        });

    }

    private void toggle_button(boolean enable){
        // toggle the enabled state of the button
        if(enable)
        {
            createaccount_button.setText("Create Account");
            createaccount_button.setEnabled(true);
        }
        else
        {
            createaccount_button.setText("Creating Account...");
            createaccount_button.setEnabled(false);
        }

    }

    private void switch_to_mainmenu() {
        // changes the intent to the mainmenu
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
    }


}