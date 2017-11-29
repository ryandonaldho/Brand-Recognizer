package brand.brandrecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_eye);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }


            MainFragment mainScreenFragment= new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,mainScreenFragment).addToBackStack(null).commit();
        }

    }


}
