package brand.brandrecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }


            MainFragment mainScreenFragment= new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,mainScreenFragment).commit();
        }

    }


}
