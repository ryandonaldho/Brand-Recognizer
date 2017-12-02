package brand.brandrecognizer;

<<<<<<< HEAD

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends FragmentActivity{
=======
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_eye);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

<<<<<<< HEAD
=======
        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }


            MainFragment mainScreenFragment= new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,mainScreenFragment).commit();
        }

>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
    }


}
