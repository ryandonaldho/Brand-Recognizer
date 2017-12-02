package brand.brandrecognizer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {

    WebView webView;


    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web, container, false);

        webView = v.findViewById(R.id.WebView);

        Bundle bundle = this.getArguments();

        String brand = bundle.getString("brand");
        String url = "https://en.m.wikipedia.org/wiki/" + brand;

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


        return v;
    }

}
