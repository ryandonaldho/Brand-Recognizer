package brand.brandrecognizer;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateAccount extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "CreateAccount";
    private EditText username_text;
    private EditText password_text;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }

        };

        username_text = (EditText)findViewById(R.id.email_edittext);
        password_text = (EditText)findViewById(R.id.password_edittext);


    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

    }


    public void on_button_click(View v) {
        String username = username_text.getText().toString();
        String password = password_text.getText().toString();
        if (username != "" && password != "")
        {
            createAccount(username, password);
        }

        //Intent intent = new Intent(v.getContext(), MainActivity.class);
        //startActivity(intent);
    }

    private void createAccount(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if(!task.isSuccessful())
                {
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(CreateAccount.this, "success", Toast.LENGTH_SHORT).show();
                    //sendEmailVerification();
                }
                else
                {
                    Toast.makeText(CreateAccount.this, "nope", Toast.LENGTH_SHORT).show();

                }

             }

        });

    }

    private void sendEmailVerification()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(CreateAccount.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(CreateAccount.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void getUserInformation()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

    }



}

