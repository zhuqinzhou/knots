package com.example.qzhu1.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    private String tempFriend;
    private String tempFriend2;

    private static final String USIM_EMAIL_PATTERN = "0-9][a-z][A-Z][_0-9][a-z][A-Z][-_.*@0-9][a-z][A-Z][-_.+";

    private Button addFriendBtn;
    ImageView btnBack;
    private ListView listView;
    private static final String FIREBASEREF = "https://knotedb.firebaseio.com/";
    private Firebase firebaseRef;
    String[] friendList;

    public FriendsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        listView = (ListView) rootView.findViewById(R.id.friendsLV);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView1=(TextView)view.findViewById(R.id.ItemTitle);
                String email = textView1.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, email);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Join Knots");
                sendIntent.putExtra(Intent.EXTRA_TEXT, email+", we may have some bills to take care");
                startActivity(sendIntent);
            }
        });
        //This part is for getting all the friend's list
        firebaseRef = new Firebase(FIREBASEREF);
        Firebase userRef = firebaseRef.child("user");
        final String curUser = KnotsApplication.email.substring(0, KnotsApplication.email.indexOf("@"));
        final List<String> fl = new ArrayList<String>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User it = postSnapshot.getValue(User.class);
                    if (it.getEmail().toLowerCase().equals(KnotsApplication.email.toLowerCase())) {
                        tempFriend = "";
                        tempFriend = it.getFriends();
                        tempFriend2 = tempFriend;
                        while (!tempFriend.equals("")) {
                            fl.add(tempFriend.substring(0, tempFriend.indexOf(";")));
                            tempFriend = tempFriend.substring(tempFriend.indexOf(";") + 1, tempFriend.length());
                        }
                        //System.out.println(fl);
                        friendList = fl.toArray(new String[fl.size()]);
                        break;
                    }
                }
                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();


                for (int i = 0; i < friendList.length; i++) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("ItemTitle", friendList[i]);
                    mylist.add(map);
//                    System.out.println(friendList[i]);
                }

                SimpleAdapter mSimpleAdapter = new SimpleAdapter(getContext(), mylist, R.layout.my_listitem,
                        new String[]{"ItemTitle"},
                        new int[]{R.id.ItemTitle});
                listView.setAdapter(mSimpleAdapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        final EditText addFriendET = (EditText) rootView.findViewById(R.id.addFriend);
        addFriendET.addTextChangedListener(mTextWatcher);
        final Firebase tempRef = userRef.child(curUser);

        addFriendBtn = (Button) rootView.findViewById(R.id.addFriendBtn);
        addFriendBtn.setVisibility(View.INVISIBLE);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempFriend2 = tempFriend2 + addFriendET.getText().toString() + ";";
//                Toast.makeText(getContext(), tempFriend2, Toast.LENGTH_LONG).show();
                Map<String, Object> friendUpdate = new HashMap<>();
                friendUpdate.put("friends", tempFriend2);
                tempRef.updateChildren(friendUpdate);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer, FriendsFragment.newInstance())
                        .addToBackStack(null).commit();
            }
        });

        btnBack = (ImageView) rootView.findViewById(R.id.imageButton7);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        return rootView;

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            addFriendBtn.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //TextTest(s);
//            if(s.toString().matches(USIM_EMAIL_PATTERN)){
//                addFriendBtn.setVisibility(View.VISIBLE);
//            }
            addFriendBtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

//    private void TextTest(CharSequence s){
//        if(s.toString().contains())
//    }

}
