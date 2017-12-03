package brand.brandrecognizer;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView myDrawerList;
    private DrawerLayout myDrawerLayout;
    private ArrayAdapter<String> myAdapter;
    private ActionBarDrawerToggle myToggle;
    private String title;
    String [] nav = {"Log out", "History", "Home", "About", "Popular"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        myDrawerList = (ListView)findViewById(R.id.navList);
        myDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        title = getTitle().toString();

        addDrawerItems();
        setupDrawer();

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }

            MainFragment mainScreenFragment= new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainScreenFragment).commit();
        }


    }

    private void addDrawerItems(){
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nav);
        myDrawerList.setAdapter(myAdapter);

        myDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MainActivity.this, "This will move you around", Toast.LENGTH_SHORT).show();

                if(nav[0] == (String) parent.getItemAtPosition(position)){
                    // logs out
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(MainActivity.this, MainMenuActivity.class);
                    startActivity(i);
                }
                else if(nav[1] == (String) parent.getItemAtPosition(position)){
                    // goes to history
                    HistoryFragment historyFragment = new HistoryFragment();
                /*    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, history);
                    transaction.commit();*/
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,historyFragment).addToBackStack(null).commit();


                }
                else if(nav[2] == (String) parent.getItemAtPosition(position)){
                    // goes home
                    MainFragment mainFragment = new MainFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainFragment).commit();

                }
                else if(nav[3] == (String) parent.getItemAtPosition(position)){
                    // shows about
                    AboutFragment aboutFragment = new AboutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,aboutFragment).commit();
                }
                else if(nav[4] == (String) parent.getItemAtPosition(position)){
                    PopularFragment popularFragment = new PopularFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,popularFragment).commit();
                }
            }
        });
    }

    private void setupDrawer(){
        myToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.drawer_open, R.string.drawer_close){

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("BrandRecognizer");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }
        };

        myToggle.setDrawerIndicatorEnabled(true);
        myDrawerLayout.addDrawerListener(myToggle);
    }

    @Override
    protected void onPostCreate(Bundle instanceState){
        super.onPostCreate(instanceState);
        myToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration config){
        super.onConfigurationChanged(config);
        myToggle.onConfigurationChanged(config);
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            return true;
        }

        if(myToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option_tab, menu);
        return true;
    }



}
