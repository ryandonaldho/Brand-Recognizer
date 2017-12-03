package brand.brandrecognizer;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
*/
public class AboutFragment extends Fragment {
    public AboutFragment() {
        // Required empty public constructor
    }

    private Context myContext;
    private InputStream stream;
    private BufferedReader reader;
    private AssetManager asset;
    //private Asset
    private String line;
    private String full;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        textView = (TextView)v.findViewById(R.id.about_info);
        textRead();
        return v;
    }

    public void textRead(){
        stream = getResources().openRawResource(R.raw.test);
        reader = new BufferedReader(new InputStreamReader(stream));
        full = "";
        line = "";

        try{
            //stream = asset.open("random.txt");

            while((line = reader.readLine()) != null) {
                full += (line + "\n");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        textView.setText(full);
    }

}
