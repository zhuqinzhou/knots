package com.example.qzhu1.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactFragment extends Fragment {
    ArrayList<Contact> listContacts;
    ListView lvContacts;
    ImageView btnBack;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        listContacts = new ContactFetcher(getActivity()).fetchAll();
        lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        ContactsAdapter adapterContacts = new ContactsAdapter(getActivity(), listContacts);
        lvContacts.setAdapter(adapterContacts);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView1=(TextView)view.findViewById(R.id.tvEmail);
                String email = textView1.getText().toString();
                TextView textView2=(TextView)view.findViewById(R.id.tvName);
                String name = textView2.getText().toString();
                TextView textView3=(TextView)view.findViewById(R.id.tvPhone);
                String phone = textView3.getText().toString();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra("address", phone);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, email);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Join Knots");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi "+name+"! Take a look at Knots. It's awesome!");
                startActivity(sendIntent);
            }
        });
        btnBack = (ImageView) view.findViewById(R.id.imageButton3);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
