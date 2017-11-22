package brand.brandrecognizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener  {

    Button goToMap;
    Button goToMoreInfo;

    String brand = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        goToMap = (Button) findViewById(R.id.mapButton);
        goToMoreInfo = (Button) findViewById(R.id.moreInfoButton);

        brand = getIntent().getExtras().getString("brand");

        goToMap.setOnClickListener(this);
        goToMoreInfo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.mapButton:
                Intent mapsView = new Intent(this, MapsActivity.class);
                startActivity(mapsView);
                break;
            case R.id.moreInfoButton:
                Intent intent = new Intent(this,WebActivity.class);
                intent.putExtra("brand",brand);
                startActivity(intent);
                break;
        }
    }
}
