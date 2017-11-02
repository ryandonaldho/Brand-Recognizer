package brand.brandrecognizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyCKKR9KzqqTGUzzJysbOr_E2nKdPz-8q7M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void mapClick(View view){
        Intent enterIntent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(enterIntent);

    }
    // test
}
