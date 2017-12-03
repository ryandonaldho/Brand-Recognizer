package brand.brandrecognizer;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {

    DatabaseReference dref;
    ListView listView;
    ArrayList<ListObject> list=new ArrayList<>();


    public PopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_popular, container, false);

        listView = v.findViewById(R.id.listview_popular);

        listView.setAdapter(null);
        TextView textView = new TextView(getContext());
        textView.setText("The most popular seaches");
        listView.addHeaderView(textView);
        final ArrayAdapter<ListObject> adapter=new ArrayAdapter<ListObject>(getActivity(),android.R.layout.simple_dropdown_item_1line,list);
        listView.setAdapter(adapter);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uuid = currentUser.getUid();
        dref= FirebaseDatabase.getInstance().getReference().child("popular");
        Query query = dref.orderByValue().limitToLast(20);
        //dref.child("users").child(uuid).child("searches");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String line = dataSnapshot.getKey().toString() + "  " +dataSnapshot.getValue(Integer.class);
                ListObject listObject = new ListObject();
                listObject.setBrandName(dataSnapshot.getKey().toString());
                listObject.setCount(+dataSnapshot.getValue(Integer.class));
                list.add(listObject);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
                // view most recent items first


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
                String value = adapter.getItem(i-1).getBrandName();
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

        return v;
    }


    public class ListObject {
        String brandName;
        int count;
        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }



        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return brandName + "    " + count;
        }
    }


}
