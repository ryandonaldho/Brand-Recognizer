package brand.brandrecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String brand = getIntent().getExtras().getString("test");
        String url = "https://en.m.wikipedia.org/wiki/" + brand;
        WebView view = (WebView) this.findViewById(R.id.Webview1);
        view.setWebViewClient(new WebViewClient());
        view.loadUrl(url);
    }
}
