package brand.brandrecognizer;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void enter_createaccount(View v)
    {// when enter_button is clicked, go to EnterInfo activity
        Intent intent = new Intent(v.getContext(), CreateAccount.class);
        startActivity(intent);
    }// End to_enterInfo

    public void enter_signin(View v)
    {
        Intent intent = new Intent(v.getContext(), SignIn.class);
        startActivity(intent);
    }

}
