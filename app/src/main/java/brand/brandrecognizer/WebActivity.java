package brand.brandrecognizer;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String brand = getIntent().getExtras().getString("brand");
        String url = "https://en.m.wikipedia.org/wiki/" + brand;
        WebView view = (WebView) this.findViewById(R.id.Webview1);
        view.setWebViewClient(new WebViewClient());
        view.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
