package brand.brandrecognizer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Return a list view of the user past searches
 */

public class HistoryFragment extends Fragment {

    DatabaseReference dref;
    ListView listView;
    ArrayList<String> list=new ArrayList<>();

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_history, container, false);

        listView = v.findViewById(R.id.listview_history);
        listView.setAdapter(null);
        TextView textView = new TextView(getContext());
        textView.setText("Your Search History");
        listView.addHeaderView(textView);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,list);
        listView.setAdapter(adapter);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uuid = currentUser.getUid();
        dref= FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("searches");
        Query query = dref.limitToLast(10);
        //dref.child("users").child(uuid).child("searches");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getValue(String.class));
                // view most recent items first
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
                // view most recent items first
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String)adapter.getItem(i-1);
                System.out.println(value);
                Bundle bundle = new Bundle();
                 bundle.putString("brand",value);
                //WebFragment webFragment = new WebFragment();
                OptionTabFragment optionTabFragment = new OptionTabFragment();
                optionTabFragment.setArguments(bundle);

                //webFragment.setArguments(bundle);

               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, optionTabFragment).addToBackStack(null).commit();
            }
        });


        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option_tab, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete_history: deleteHistory();
        }

        return false;
    }

    public void deleteHistory(){
        String uuid = FirebaseAuth.getInstance().getUid();
        dref= FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("searches");
        DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().child("users").child(uuid);
        db_node.child("searches").setValue(null);
        /*
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("here");
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    //System.out.println(childDataSnapshot.getKey());
                    String key = childDataSnapshot.getKey();
                    dref.child(key).removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

    }





}
